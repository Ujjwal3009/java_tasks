package com.epam.tdd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FizzBuzzTest {

    private final FizzBuzz fizzBuzz = new FizzBuzz();

    @ParameterizedTest
    @CsvSource({
            "1, 1",
            "2, 2",
            "3, Fizz",
            "6, Fizz",
            "5, Buzz",
            "10, Buzz",
            "15, FizzBuzz",
            "30, FizzBuzz"
    })
    void testFizzBuzz(int input, String expected) {
        Assertions.assertEquals(expected, fizzBuzz.convert(input));
    }
}
