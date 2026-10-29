package com.epam.stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

class StreamTasksTest {

    @Test
    void testGenerate() {
        var list = StreamTasks.generate(5).toList();
        Assertions.assertEquals(5, list.size());
        Assertions.assertEquals(0, list.get(0).value());
        Assertions.assertEquals(4, list.get(4).value());
    }

    @Test
    void testToIds() {
        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();
        var structure = List.of(
                List.of(new Prop(id1, "a", 1)),
                List.of(new Prop(id2, "b", 2))
        );
        var ids = StreamTasks.toIds(structure).toList();
        Assertions.assertEquals(List.of(id1, id2), ids);
    }

    @Test
    void testCountEven() {
        Assertions.assertEquals(3, StreamTasks.countEven(Stream.of(1, 2, 3, 4, 5, 6, null)));
    }

    @Test
    void testFirstWord() {
        var chars = "   hello world".chars().mapToObj(c -> (char) c);
        var result = StreamTasks.firstWord(chars).map(String::valueOf).reduce("", String::concat);
        Assertions.assertEquals("hello", result);
    }

    @Test
    void testNonNullProperties() {
        var id = UUID.randomUUID();
        var list = StreamTasks.nonNullProperties(Stream.of(
                new Prop(id, "alpha", 10),
                new Prop(null, null, 20)
        )).toList();
        Assertions.assertTrue(list.contains(id.toString()));
        Assertions.assertTrue(list.contains("alpha"));
        Assertions.assertTrue(list.contains("10"));
        Assertions.assertTrue(list.contains("20"));
        Assertions.assertFalse(list.contains("null"));
    }

    @Test
    void testSortByValueThenName() {
        var p1 = new Prop(UUID.randomUUID(), "b", 2);
        var p2 = new Prop(UUID.randomUUID(), "a", 2);
        var p3 = new Prop(UUID.randomUUID(), "c", 1);
        var sorted = StreamTasks.sortByValueThenName(Stream.of(p1, p2, p3));
        Assertions.assertEquals("c", sorted.get(0).name());
        Assertions.assertEquals("a", sorted.get(1).name());
        Assertions.assertEquals("b", sorted.get(2).name());
    }

    @Test
    void testFilterNonNullName() {
        var p1 = new Prop(UUID.randomUUID(), null, 1);
        var p2 = new Prop(UUID.randomUUID(), "valid", 2);
        var list = StreamTasks.filterNonNullName(Stream.of(p1, p2));
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals("valid", list.get(0).name());
    }

    @Test
    void testDistinctById() {
        var id = UUID.randomUUID();
        var p1 = new Prop(id, "first", 1);
        var p2 = new Prop(id, "second", 2);
        var list = StreamTasks.distinctById(Stream.of(p1, p2)).toList();
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals("first", list.get(0).name());
    }

    @Test
    void testMaxValuedPropName() {
        var p1 = new Prop(UUID.randomUUID(), "low", 1);
        var p2 = new Prop(UUID.randomUUID(), "high", 100);
        Assertions.assertEquals("high", StreamTasks.maxValuedPropName(Stream.of(p1, p2)));
    }

    @Test
    void testFindNameConflicts() {
        var p1 = new Prop(UUID.randomUUID(), "shared", 1);
        var p2 = new Prop(UUID.randomUUID(), "shared", 2);
        var p3 = new Prop(UUID.randomUUID(), "unique", 3);
        Map<String, Set<UUID>> conflicts = StreamTasks.findNameConflicts(Stream.of(p1, p2, p3));
        Assertions.assertTrue(conflicts.containsKey("shared"));
        Assertions.assertFalse(conflicts.containsKey("unique"));
        Assertions.assertEquals(2, conflicts.get("shared").size());
    }

    @Test
    void testSumValuesByName() {
        var p1 = new Prop(UUID.randomUUID(), "x", 10);
        var p2 = new Prop(UUID.randomUUID(), "x", 20);
        var p3 = new Prop(UUID.randomUUID(), "y", 5);
        var sumMap = StreamTasks.sumValuesByName(Stream.of(p1, p2, p3));
        Assertions.assertEquals(30, sumMap.get("x"));
        Assertions.assertEquals(5, sumMap.get("y"));
    }

    @Test
    void testSumsOddEven() {
        var sums = StreamTasks.sumsOddEven(Stream.of(1, 2, 3, 4, 5));
        Assertions.assertEquals(6, sums.evenSum());
        Assertions.assertEquals(9, sums.oddSum());
    }

    @Test
    void testMinMaxValuedPropNames() {
        var p1 = new Prop(UUID.randomUUID(), "minItem", 10);
        var p2 = new Prop(UUID.randomUUID(), "midItem", 50);
        var p3 = new Prop(UUID.randomUUID(), "maxItem", 90);
        var minMax = StreamTasks.minMaxValuedPropNames(Stream.of(p1, p2, p3));
        Assertions.assertEquals("minItem", minMax.minName());
        Assertions.assertEquals("maxItem", minMax.maxName());
    }

    @Test
    void testFold() {
        Function<String, String> f = StreamTasks.fold(Stream.of(String::trim, s -> s.replace('b', 'c')));
        Assertions.assertEquals("cat", f.apply(" bat"));
    }

    @Test
    void testPartitioningCollector() {
        var result = StreamTasks.sumAndNullCount(Stream.of(10, null, 20, null, 30));
        Assertions.assertEquals(60, result.sum());
        Assertions.assertEquals(2, result.nullCount());
    }
}
