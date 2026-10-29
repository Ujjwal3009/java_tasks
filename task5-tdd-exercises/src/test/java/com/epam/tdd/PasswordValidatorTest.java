package com.epam.tdd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PasswordValidatorTest {

    private final PasswordValidator validator = new PasswordValidator();

    @Test
    void testValidPassword() {
        Assertions.assertTrue(validator.validate("Password123"));
        Assertions.assertTrue(validator.validate("Abcdefg1"));
    }

    @Test
    void testTooShort() {
        Assertions.assertFalse(validator.validate("Pass1"));
    }

    @Test
    void testMissingUppercase() {
        Assertions.assertFalse(validator.validate("password123"));
    }

    @Test
    void testMissingLowercase() {
        Assertions.assertFalse(validator.validate("PASSWORD123"));
    }

    @Test
    void testMissingDigit() {
        Assertions.assertFalse(validator.validate("PasswordOnly"));
    }

    @Test
    void testNull() {
        Assertions.assertFalse(validator.validate(null));
    }
}
