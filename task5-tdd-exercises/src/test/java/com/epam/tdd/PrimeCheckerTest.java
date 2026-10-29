package com.epam.tdd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PrimeCheckerTest {

    private final PrimeChecker primeChecker = new PrimeChecker();

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29})
    void testPrimeNumbers(int n) {
        Assertions.assertTrue(primeChecker.isPrime(n));
    }

    @ParameterizedTest
    @ValueSource(ints = {-5, 0, 1, 4, 6, 8, 9, 10, 12, 15, 100})
    void testNonPrimeNumbers(int n) {
        Assertions.assertFalse(primeChecker.isPrime(n));
    }
}
