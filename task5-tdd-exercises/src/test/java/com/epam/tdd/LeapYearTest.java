package com.epam.tdd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LeapYearTest {

    private final LeapYear leapYear = new LeapYear();

    @Test
    void testTypicalCommonYear() {
        Assertions.assertFalse(leapYear.isLeapYear(2001));
    }

    @Test
    void testTypicalLeapYear() {
        Assertions.assertTrue(leapYear.isLeapYear(1996));
    }

    @Test
    void testAtypicalCommonYear() {
        Assertions.assertFalse(leapYear.isLeapYear(1900));
    }

    @Test
    void testAtypicalLeapYear() {
        Assertions.assertTrue(leapYear.isLeapYear(2000));
    }
}
