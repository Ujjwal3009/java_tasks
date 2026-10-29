package com.epam.effective;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

public class GuavaLruCacheService<K, V> implements CacheService<K, V> {

    private final Cache<K, V> cache;
    private final AtomicLong totalPutNanos = new AtomicLong(0);
    private final AtomicLong putCount = new AtomicLong(0);
    private final AtomicLong manualEvictionCount = new AtomicLong(0);

    public GuavaLruCacheService() {
        this(100_000, Duration.ofSeconds(5), (k, v) -> System.out.println("Guava Evicted: " + k + " -> " + v));
    }

    public GuavaLruCacheService(int maxSize, Duration expiryDuration, BiConsumer<K, V> listener) {
        RemovalListener<K, V> guavaListener = notification -> {
            if (notification.wasEvicted()) {
                manualEvictionCount.incrementAndGet();
                if (listener != null) {
                    listener.accept(notification.getKey(), notification.getValue());
                }
            }
        };

        this.cache = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .expireAfterAccess(expiryDuration.toMillis(), TimeUnit.MILLISECONDS)
                .recordStats()
                .removalListener(guavaListener)
                .build();
    }

    @Override
    public V get(K key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void put(K key, V value) {
        var start = System.nanoTime();
        try {
            cache.put(key, value);
        } finally {
            var duration = System.nanoTime() - start;
            totalPutNanos.addAndGet(duration);
            putCount.incrementAndGet();
        }
    }

    @Override
    public long getEvictionCount() {
        cache.cleanUp();
        return cache.stats().evictionCount();
    }

    @Override
    public double getAveragePutTimeNanos() {
        var count = putCount.get();
        return count == 0 ? 0.0 : (double) totalPutNanos.get() / count;
    }

    @Override
    public int size() {
        cache.cleanUp();
        return (int) cache.size();
    }
}
