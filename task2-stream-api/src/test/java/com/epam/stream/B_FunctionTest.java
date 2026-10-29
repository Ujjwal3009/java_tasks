package com.epam.stream;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class B_FunctionTest {
    @Test
    public void function1() {
        Function<String, String> func = s -> "(" + s + ")";
        assertEquals("(abc)", func.apply("abc"));
        assertEquals("()", func.apply(""));
    }
}
