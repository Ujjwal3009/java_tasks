package com.epam.tdd;

import java.util.LinkedHashMap;
import java.util.Map;

public class RomanConverter {

    private static final Map<Integer, String> NUM_TO_ROMAN = new LinkedHashMap<>();

    static {
        NUM_TO_ROMAN.put(1000, "M");
        NUM_TO_ROMAN.put(900, "CM");
        NUM_TO_ROMAN.put(500, "D");
        NUM_TO_ROMAN.put(400, "CD");
        NUM_TO_ROMAN.put(100, "C");
        NUM_TO_ROMAN.put(90, "XC");
        NUM_TO_ROMAN.put(50, "L");
        NUM_TO_ROMAN.put(40, "XL");
        NUM_TO_ROMAN.put(10, "X");
        NUM_TO_ROMAN.put(9, "IX");
        NUM_TO_ROMAN.put(5, "V");
        NUM_TO_ROMAN.put(4, "IV");
        NUM_TO_ROMAN.put(1, "I");
    }

    public String toRoman(int number) {
        if (number <= 0 || number > 3999) {
            throw new IllegalArgumentException("Number must be between 1 and 3999");
        }

        var sb = new StringBuilder();
        int remaining = number;

        for (Map.Entry<Integer, String> entry : NUM_TO_ROMAN.entrySet()) {
            while (remaining >= entry.getKey()) {
                sb.append(entry.getValue());
                remaining -= entry.getKey();
            }
        }

        return sb.toString();
    }

    public int fromRoman(String roman) {
        if (roman == null || roman.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid roman numeral");
        }

        int total = 0;
        int prev = 0;

        for (int i = roman.length() - 1; i >= 0; i--) {
            int current = charValue(roman.charAt(i));
            if (current < prev) {
                total -= current;
            } else {
                total += current;
            }
            prev = current;
        }

        return total;
    }

    private int charValue(char c) {
        return switch (Character.toUpperCase(c)) {
            case 'I' -> 1;
            case 'V' -> 5;
            case 'X' -> 10;
            case 'L' -> 50;
            case 'C' -> 100;
            case 'D' -> 500;
            case 'M' -> 1000;
            default -> throw new IllegalArgumentException("Illegal character: " + c);
        };
    }
}
