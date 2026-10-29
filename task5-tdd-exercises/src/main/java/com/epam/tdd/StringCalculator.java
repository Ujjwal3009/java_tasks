package com.epam.tdd;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StringCalculator {

    public int add(String numbers) {
        if (numbers == null || numbers.trim().isEmpty()) {
            return 0;
        }

        String delimiter = ",|\n";
        String content = numbers;

        if (numbers.startsWith("//")) {
            int delimiterEnd = numbers.indexOf("\n");
            var customDelim = numbers.substring(2, delimiterEnd);
            delimiter = Pattern.quote(customDelim);
            content = numbers.substring(delimiterEnd + 1);
        }

        String[] tokens = content.split(delimiter);
        int sum = 0;
        List<Integer> negatives = new ArrayList<>();

        for (String token : tokens) {
            String trimmed = token.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            int val = Integer.parseInt(trimmed);
            if (val < 0) {
                negatives.add(val);
            } else if (val <= 1000) {
                sum += val;
            }
        }

        if (!negatives.isEmpty()) {
            throw new IllegalArgumentException("negatives not allowed: " + negatives);
        }

        return sum;
    }
}
