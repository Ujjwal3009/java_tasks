package com.epam.tdd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BowlingGameTest {

    private BowlingGame game;

    @BeforeEach
    void setUp() {
        game = new BowlingGame();
    }

    private void rollMany(int n, int pins) {
        for (int i = 0; i < n; i++) {
            game.roll(pins);
        }
    }

    private void rollSpare() {
        game.roll(5);
        game.roll(5);
    }

    private void rollStrike() {
        game.roll(10);
    }

    @Test
    void testGutterGame() {
        rollMany(20, 0);
        Assertions.assertEquals(0, game.score());
    }

    @Test
    void testAllOnes() {
        rollMany(20, 1);
        Assertions.assertEquals(20, game.score());
    }

    @Test
    void testOneSpare() {
        rollSpare();
        game.roll(3);
        rollMany(17, 0);
        Assertions.assertEquals(16, game.score());
    }

    @Test
    void testOneStrike() {
        rollStrike();
        game.roll(3);
        game.roll(4);
        rollMany(16, 0);
        Assertions.assertEquals(24, game.score());
    }

    @Test
    void testPerfectGame() {
        rollMany(12, 10);
        Assertions.assertEquals(300, game.score());
    }
}
