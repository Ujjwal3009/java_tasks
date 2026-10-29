package com.epam.tdd;

public class BowlingGame {

    private final int[] rolls = new int[21];
    private int currentRoll = 0;

    public void roll(int pins) {
        rolls[currentRoll++] = pins;
    }

    public int score() {
        int totalScore = 0;
        int rollIndex = 0;

        for (int frame = 0; frame < 10; frame++) {
            if (isStrike(rollIndex)) {
                totalScore += 10 + rolls[rollIndex + 1] + rolls[rollIndex + 2];
                rollIndex++;
            } else if (isSpare(rollIndex)) {
                totalScore += 10 + rolls[rollIndex + 2];
                rollIndex += 2;
            } else {
                totalScore += rolls[rollIndex] + rolls[rollIndex + 1];
                rollIndex += 2;
            }
        }

        return totalScore;
    }

    private boolean isStrike(int rollIndex) {
        return rolls[rollIndex] == 10;
    }

    private boolean isSpare(int rollIndex) {
        return rolls[rollIndex] + rolls[rollIndex + 1] == 10;
    }
}
