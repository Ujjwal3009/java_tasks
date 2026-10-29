package com.epam.tdd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringCalculatorTest {

    private final StringCalculator calc = new StringCalculator();

    @Test
    void testEmptyString() {
        Assertions.assertEquals(0, calc.add(""));
    }

    @Test
    void testSingleNumber() {
        Assertions.assertEquals(5, calc.add("5"));
    }

    @Test
    void testTwoNumbersCommaSeparated() {
        Assertions.assertEquals(8, calc.add("3,5"));
    }

    @Test
    void testMultipleNumbers() {
        Assertions.assertEquals(15, calc.add("1,2,3,4,5"));
    }

    @Test
    void testNewlineDelimiter() {
        Assertions.assertEquals(6, calc.add("1\n2,3"));
    }

    @Test
    void testCustomDelimiter() {
        Assertions.assertEquals(3, calc.add("//;\n1;2"));
        Assertions.assertEquals(6, calc.add("//***\n1***2***3"));
    }

    @Test
    void testNegativeNumbersThrowException() {
        var ex = Assertions.assertThrows(IllegalArgumentException.class, () -> calc.add("1,-2,3,-4"));
        Assertions.assertTrue(ex.getMessage().contains("-2"));
        Assertions.assertTrue(ex.getMessage().contains("-4"));
    }

    @Test
    void testNumbersGreaterThan1000Ignored() {
        Assertions.assertEquals(2, calc.add("2,1001"));
    }
}
