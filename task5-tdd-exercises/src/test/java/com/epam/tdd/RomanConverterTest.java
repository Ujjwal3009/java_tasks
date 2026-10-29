package com.epam.tdd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RomanConverterTest {

    private final RomanConverter converter = new RomanConverter();

    @ParameterizedTest
    @CsvSource({
            "1, I",
            "4, IV",
            "9, IX",
            "58, LVIII",
            "1994, MCMXCIV",
            "3999, MMMCMXCIX"
    })
    void testToRoman(int number, String expected) {
        Assertions.assertEquals(expected, converter.toRoman(number));
    }

    @ParameterizedTest
    @CsvSource({
            "I, 1",
            "IV, 4",
            "IX, 9",
            "LVIII, 58",
            "MCMXCIV, 1994",
            "MMMCMXCIX, 3999"
    })
    void testFromRoman(String roman, int expected) {
        Assertions.assertEquals(expected, converter.fromRoman(roman));
    }
}
