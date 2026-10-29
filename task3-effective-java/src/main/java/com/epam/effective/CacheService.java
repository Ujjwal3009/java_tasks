package com.epam.effective;

public interface CacheService<K, V> {
    V get(K key);

    void put(K key, V value);

    long getEvictionCount();

    double getAveragePutTimeNanos();

    int size();
}
