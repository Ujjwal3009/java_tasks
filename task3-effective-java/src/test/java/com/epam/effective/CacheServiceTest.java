package com.epam.effective;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class CacheServiceTest {

    @Test
    void testSimpleLfuCacheEvictionBySize() {
        var evictedCount = new AtomicInteger(0);
        var cache = new SimpleLfuCacheService<String, CacheEntry>(
                2,
                Duration.ofSeconds(10),
                (k, v) -> evictedCount.incrementAndGet()
        );

        cache.put("k1", new CacheEntry("v1"));
        cache.put("k2", new CacheEntry("v2"));

        // access k1 to increase its frequency
        cache.get("k1");

        // inserting k3 should evict k2 (LFU)
        cache.put("k3", new CacheEntry("v3"));

        Assertions.assertNotNull(cache.get("k1"));
        Assertions.assertNull(cache.get("k2"));
        Assertions.assertNotNull(cache.get("k3"));
        Assertions.assertEquals(1, evictedCount.get());
        Assertions.assertTrue(cache.getAveragePutTimeNanos() > 0);
    }

    @Test
    void testSimpleLfuCacheExpiration() throws InterruptedException {
        var evictedCount = new AtomicInteger(0);
        var cache = new SimpleLfuCacheService<String, CacheEntry>(
                10,
                Duration.ofMillis(50),
                (k, v) -> evictedCount.incrementAndGet()
        );

        cache.put("temp", new CacheEntry("value"));
        Assertions.assertNotNull(cache.get("temp"));

        Thread.sleep(80);

        Assertions.assertNull(cache.get("temp"));
        Assertions.assertTrue(cache.getEvictionCount() >= 1);
    }

    @Test
    void testGuavaLruCache() throws InterruptedException {
        var evictedCount = new AtomicInteger(0);
        var cache = new GuavaLruCacheService<String, CacheEntry>(
                2,
                Duration.ofMillis(100),
                (k, v) -> evictedCount.incrementAndGet()
        );

        cache.put("g1", new CacheEntry("val1"));
        cache.put("g2", new CacheEntry("val2"));

        // access g1, g2 is least recently used
        cache.get("g1");
        cache.put("g3", new CacheEntry("val3"));

        Assertions.assertNotNull(cache.get("g1"));
        Assertions.assertNull(cache.get("g2"));
        Assertions.assertNotNull(cache.get("g3"));

        Thread.sleep(150);
        Assertions.assertNull(cache.get("g1"));
        Assertions.assertTrue(cache.getAveragePutTimeNanos() > 0);
    }

    @Test
    void testConcurrencySupport() throws InterruptedException {
        var cache = new SimpleLfuCacheService<String, CacheEntry>(
                1000,
                Duration.ofSeconds(10),
                null
        );

        var executor = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 200; i++) {
            final int id = i;
            executor.submit(() -> {
                cache.put("key" + id, new CacheEntry("data" + id));
                cache.get("key" + id);
            });
        }

        executor.shutdown();
        Assertions.assertTrue(executor.awaitTermination(3, TimeUnit.SECONDS));
        Assertions.assertTrue(cache.size() > 0);
    }
}
