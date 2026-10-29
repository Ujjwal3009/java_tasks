package com.epam.stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

class A_PredicateTest {
    @Test
    public void a_predicate1() {
        Predicate<String> pred = s -> s != null && s.length() > 4;
        Assertions.assertTrue(pred.test("abcde"));
        Assertions.assertFalse(pred.test("abcd"));
        Assertions.assertFalse(pred.test(""));
    }
}
