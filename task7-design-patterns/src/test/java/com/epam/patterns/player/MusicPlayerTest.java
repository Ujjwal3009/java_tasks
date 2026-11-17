package com.epam.patterns.player;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MusicPlayerTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;
    private MusicPlayer player;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        List<String> tracks = Arrays.asList("Track 1", "Track 2", "Track 3");
        player = new MusicPlayer(tracks);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    public void testPlayDisplaysCorrectMessage() {
        // play when stopped
        player.playOrPause();
        assertTrue(outputStreamCaptor.toString().contains("Playing: Track 1"));
        assertTrue(player.isPlaying());
        assertFalse(player.isStopped());

        outputStreamCaptor.reset();

        // pause when playing
        player.playOrPause();
        assertTrue(outputStreamCaptor.toString().contains("Paused: Track 1"));
        assertFalse(player.isPlaying());
    }

    @Test
    public void testStopWhenAlreadyStopped() {
        // Initially the player is stopped
        player.stop();
        assertTrue(outputStreamCaptor.toString().contains("Already stopped"));

        outputStreamCaptor.reset();

        // Start playing, then stop
        player.playOrPause();
        player.stop();
        assertTrue(outputStreamCaptor.toString().contains("Stopping: Track 1"));
        assertTrue(player.isStopped());
        assertFalse(player.isPlaying());
    }

    @Test
    public void testNextWhenOnLastTrackWithoutRepeat() {
        // Go to track 2
        player.next();
        // Go to track 3 (last track)
        player.next();
        outputStreamCaptor.reset();

        // Try next on last track with repeat disabled
        player.next();
        assertTrue(outputStreamCaptor.toString().contains("Already on the last track"));
        assertEquals(2, player.getCurrentTrackIndex());
    }

    @Test
    public void testNextWhenOnLastTrackWithRepeat() {
        // Enable repeat
        player.repeatOnOrOff();
        // Go to track 2
        player.next();
        // Go to track 3 (last track)
        player.next();
        outputStreamCaptor.reset();

        // Next with repeat enabled should replay first track
        player.next();
        assertTrue(outputStreamCaptor.toString().contains("Replaying first track: Track 1"));
        assertEquals(0, player.getCurrentTrackIndex());
    }

    @Test
    public void testPreviousWhenOnFirstTrack() {
        // Initially on first track
        player.previous();
        assertTrue(outputStreamCaptor.toString().contains("Already on the first track"));
        assertEquals(0, player.getCurrentTrackIndex());

        outputStreamCaptor.reset();

        // Move to next track, then go back
        player.next();
        player.previous();
        assertTrue(outputStreamCaptor.toString().contains("Playing previous track: Track 1"));
        assertEquals(0, player.getCurrentTrackIndex());
    }

    @Test
    public void testRepeatToggle() {
        assertFalse(player.isRepeatEnabled());

        player.repeatOnOrOff();
        assertTrue(outputStreamCaptor.toString().contains("Repeat on"));
        assertTrue(player.isRepeatEnabled());

        outputStreamCaptor.reset();

        player.repeatOnOrOff();
        assertTrue(outputStreamCaptor.toString().contains("Repeat off"));
        assertFalse(player.isRepeatEnabled());
    }
}
