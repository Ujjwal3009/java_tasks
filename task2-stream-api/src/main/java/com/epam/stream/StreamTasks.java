package com.epam.stream;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamTasks {

    public static Stream<Prop> generate(int n) {
        return IntStream.range(0, n)
                .mapToObj(i -> new Prop(UUID.randomUUID(), "prop-" + i, i));
    }

    public static Stream<UUID> toIds(List<List<Prop>> structure) {
        return structure.stream()
                .flatMap(List::stream)
                .map(Prop::id);
    }

    public static long countEven(Stream<Integer> stream) {
        return stream.filter(Objects::nonNull)
                .filter(n -> n % 2 == 0)
                .count();
    }

    public static Stream<Character> firstWord(Stream<Character> stream) {
        return stream.dropWhile(Character::isWhitespace)
                .takeWhile(c -> !Character.isWhitespace(c));
    }

    public static Stream<String> nonNullProperties(Stream<Prop> stream) {
        return stream.flatMap(p -> {
            var props = Stream.<String>builder();
            if (p.id() != null) props.add(p.id().toString());
            if (p.name() != null) props.add(p.name());
            props.add(String.valueOf(p.value()));
            return props.build();
        });
    }

    public static List<Prop> sortByValueThenName(Stream<Prop> stream) {
        return stream.sorted(Comparator.comparingInt(Prop::value)
                        .thenComparing(Prop::name, Comparator.nullsLast(String::compareTo)))
                .toList();
    }

    public static List<Prop> filterNonNullName(Stream<Prop> stream) {
        return stream.filter(p -> p.name() != null).toList();
    }

    public static Stream<Prop> distinctById(Stream<Prop> stream) {
        Set<UUID> seen = ConcurrentHashMap.newKeySet();
        return stream.filter(p -> p.id() != null && seen.add(p.id()));
    }

    public static String maxValuedPropName(Stream<Prop> stream) {
        return stream.max(Comparator.comparingInt(Prop::value))
                .map(Prop::name)
                .orElse(null);
    }

    public static Map<String, Set<UUID>> findNameConflicts(Stream<Prop> stream) {
        var grouped = stream.filter(p -> p.name() != null && p.id() != null)
                .collect(Collectors.groupingBy(
                        Prop::name,
                        Collectors.mapping(Prop::id, Collectors.toSet())
                ));

        return grouped.entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Map<String, Integer> sumValuesByName(Stream<Prop> stream) {
        return stream.filter(p -> p.name() != null)
                .collect(Collectors.groupingBy(
                        Prop::name,
                        Collectors.summingInt(Prop::value)
                ));
    }

    public record OddEvenSum(int evenSum, int oddSum) {}

    public static OddEvenSum sumsOddEven(Stream<Integer> stream) {
        Map<Boolean, Integer> sums = stream.filter(Objects::nonNull)
                .collect(Collectors.partitioningBy(
                        n -> n % 2 == 0,
                        Collectors.summingInt(Integer::intValue)
                ));
        return new OddEvenSum(sums.getOrDefault(true, 0), sums.getOrDefault(false, 0));
    }

    public record MinMaxNames(String minName, String maxName) {}

    public static MinMaxNames minMaxValuedPropNames(Stream<Prop> stream) {
        List<Prop> list = stream.toList();
        if (list.isEmpty()) {
            return new MinMaxNames(null, null);
        }
        var min = list.stream().min(Comparator.comparingInt(Prop::value)).map(Prop::name).orElse(null);
        var max = list.stream().max(Comparator.comparingInt(Prop::value)).map(Prop::name).orElse(null);
        return new MinMaxNames(min, max);
    }

    public static <T> Function<T, T> fold(Stream<Function<T, T>> functions) {
        return functions.reduce(Function.identity(), Function::andThen);
    }

    public static <T, RT, AT, RF, AF, R> Collector<T, ?, R> partitioningCollector(
            Predicate<? super T> predicate,
            Collector<? super T, AT, RT> collTrue,
            Collector<? super T, AF, RF> collFalse,
            BiFunction<RT, RF, R> constructor
    ) {
        record Acc<AT, AF>(AT accTrue, AF accFalse) {}

        return Collector.of(
                () -> new Acc<>(collTrue.supplier().get(), collFalse.supplier().get()),
                (acc, t) -> {
                    if (predicate.test(t)) {
                        collTrue.accumulator().accept(acc.accTrue(), t);
                    } else {
                        collFalse.accumulator().accept(acc.accFalse(), t);
                    }
                },
                (acc1, acc2) -> new Acc<>(
                        collTrue.combiner().apply(acc1.accTrue(), acc2.accTrue()),
                        collFalse.combiner().apply(acc1.accFalse(), acc2.accFalse())
                ),
                acc -> constructor.apply(
                        collTrue.finisher().apply(acc.accTrue()),
                        collFalse.finisher().apply(acc.accFalse())
                )
        );
    }

    public record SumAndNullCount(int sum, long nullCount) {}

    public static SumAndNullCount sumAndNullCount(Stream<Integer> stream) {
        Collector<Integer, ?, SumAndNullCount> collector = partitioningCollector(
                Objects::nonNull,
                Collectors.summingInt(n -> n),
                Collectors.counting(),
                SumAndNullCount::new
        );
        return stream.collect(collector);
    }
}
