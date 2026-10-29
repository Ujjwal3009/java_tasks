package com.epam.effective;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;

public class SimpleLfuCacheService<K, V> implements CacheService<K, V> {

    private static class Node<K, V> {
        final K key;
        V value;
        int frequency;
        Instant lastAccessed;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.frequency = 1;
            this.lastAccessed = Instant.now();
        }
    }

    private final int maxSize;
    private final Duration expiryDuration;
    private final BiConsumer<K, V> removalListener;

    private final Map<K, Node<K, V>> cache = new HashMap<>();
    private final Map<Integer, LinkedHashSet<K>> frequencyMap = new HashMap<>();
    private int minFrequency = 0;

    private final AtomicLong evictionCount = new AtomicLong(0);
    private final AtomicLong totalPutNanos = new AtomicLong(0);
    private final AtomicLong putCount = new AtomicLong(0);

    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public SimpleLfuCacheService() {
        this(100_000, Duration.ofSeconds(5), (k, v) -> System.out.println("Evicted: " + k + " -> " + v));
    }

    public SimpleLfuCacheService(int maxSize, Duration expiryDuration, BiConsumer<K, V> removalListener) {
        this.maxSize = maxSize;
        this.expiryDuration = expiryDuration;
        this.removalListener = removalListener != null ? removalListener : (k, v) -> {};
    }

    @Override
    public V get(K key) {
        rwLock.writeLock().lock();
        try {
            evictExpired();
            var node = cache.get(key);
            if (node == null) {
                return null;
            }
            if (isExpired(node)) {
                removeNode(node);
                evictionCount.incrementAndGet();
                removalListener.accept(node.key, node.value);
                return null;
            }
            updateFrequency(node);
            node.lastAccessed = Instant.now();
            return node.value;
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    @Override
    public void put(K key, V value) {
        var start = System.nanoTime();
        rwLock.writeLock().lock();
        try {
            evictExpired();
            var node = cache.get(key);
            if (node != null) {
                node.value = value;
                node.lastAccessed = Instant.now();
                updateFrequency(node);
            } else {
                if (cache.size() >= maxSize) {
                    evictLfu();
                }
                var newNode = new Node<>(key, value);
                cache.put(key, newNode);
                frequencyMap.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
                minFrequency = 1;
            }
        } finally {
            rwLock.writeLock().unlock();
            var duration = System.nanoTime() - start;
            totalPutNanos.addAndGet(duration);
            putCount.incrementAndGet();
        }
    }

    private void updateFrequency(Node<K, V> node) {
        var currentFreq = node.frequency;
        var keys = frequencyMap.get(currentFreq);
        if (keys != null) {
            keys.remove(node.key);
            if (keys.isEmpty()) {
                frequencyMap.remove(currentFreq);
                if (minFrequency == currentFreq) {
                    minFrequency++;
                }
            }
        }
        node.frequency++;
        frequencyMap.computeIfAbsent(node.frequency, k -> new LinkedHashSet<>()).add(node.key);
    }

    private void evictLfu() {
        var keys = frequencyMap.get(minFrequency);
        if (keys != null && !keys.isEmpty()) {
            var evictKey = keys.iterator().next();
            var node = cache.get(evictKey);
            if (node != null) {
                removeNode(node);
                evictionCount.incrementAndGet();
                removalListener.accept(node.key, node.value);
            }
        }
    }

    private void evictExpired() {
        Iterator<Map.Entry<K, Node<K, V>>> it = cache.entrySet().iterator();
        while (it.hasNext()) {
            var entry = it.next();
            var node = entry.getValue();
            if (isExpired(node)) {
                it.remove();
                var keys = frequencyMap.get(node.frequency);
                if (keys != null) {
                    keys.remove(node.key);
                    if (keys.isEmpty()) {
                        frequencyMap.remove(node.frequency);
                    }
                }
                evictionCount.incrementAndGet();
                removalListener.accept(node.key, node.value);
            }
        }
    }

    private boolean isExpired(Node<K, V> node) {
        return Duration.between(node.lastAccessed, Instant.now()).compareTo(expiryDuration) > 0;
    }

    private void removeNode(Node<K, V> node) {
        cache.remove(node.key);
        var keys = frequencyMap.get(node.frequency);
        if (keys != null) {
            keys.remove(node.key);
            if (keys.isEmpty()) {
                frequencyMap.remove(node.frequency);
            }
        }
    }

    @Override
    public long getEvictionCount() {
        return evictionCount.get();
    }

    @Override
    public double getAveragePutTimeNanos() {
        var count = putCount.get();
        return count == 0 ? 0.0 : (double) totalPutNanos.get() / count;
    }

    @Override
    public int size() {
        rwLock.readLock().lock();
        try {
            return cache.size();
        } finally {
            rwLock.readLock().unlock();
        }
    }
}
