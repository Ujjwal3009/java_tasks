package com.epam.patterns.player;

import java.util.List;

public class MusicPlayer implements Player {
    private List<String> tracks;
    private int currentTrackIndex;
    private boolean isPlaying;
    private boolean isStopped;
    private boolean repeatEnabled;

    public MusicPlayer(List<String> tracks) {
        this.tracks = tracks;
        this.currentTrackIndex = 0;
        this.isPlaying = false;
        this.isStopped = true;
        this.repeatEnabled = false;
    }

    @Override
    public void playOrPause() {
        if (tracks == null || tracks.isEmpty()) {
            return;
        }
        
        if (isStopped || !isPlaying) {
            isPlaying = true;
            isStopped = false;
            String currentTrack = tracks.get(currentTrackIndex);
            System.out.println("Playing: " + currentTrack);
        } else {
            isPlaying = false;
            String currentTrack = tracks.get(currentTrackIndex);
            System.out.println("Paused: " + currentTrack);
        }
    }

    @Override
    public void stop() {
        if (isStopped) {
            System.out.println("Already stopped");
        } else {
            if (tracks != null && !tracks.isEmpty()) {
                String currentTrack = tracks.get(currentTrackIndex);
                System.out.println("Stopping: " + currentTrack);
            }
            isStopped = true;
            isPlaying = false;
        }
    }

    @Override
    public void next() {
        if (tracks == null || tracks.isEmpty()) {
            return;
        }

        if (currentTrackIndex == tracks.size() - 1 && !repeatEnabled) {
            System.out.println("Already on the last track");
        } else if (currentTrackIndex == tracks.size() - 1 && repeatEnabled) {
            currentTrackIndex = 0;
            isPlaying = true;
            isStopped = false;
            String currentTrack = tracks.get(currentTrackIndex);
            System.out.println("Replaying first track: " + currentTrack);
        } else {
            currentTrackIndex = currentTrackIndex + 1;
            isPlaying = true;
            isStopped = false;
            String currentTrack = tracks.get(currentTrackIndex);
            System.out.println("Playing next track: " + currentTrack);
        }
    }

    @Override
    public void previous() {
        if (tracks == null || tracks.isEmpty()) {
            return;
        }

        if (currentTrackIndex == 0) {
            System.out.println("Already on the first track");
        } else {
            currentTrackIndex = currentTrackIndex - 1;
            isPlaying = true;
            isStopped = false;
            String currentTrack = tracks.get(currentTrackIndex);
            System.out.println("Playing previous track: " + currentTrack);
        }
    }

    @Override
    public void repeatOnOrOff() {
        if (repeatEnabled) {
            repeatEnabled = false;
            System.out.println("Repeat off");
        } else {
            repeatEnabled = true;
            System.out.println("Repeat on");
        }
    }

    public List<String> getTracks() {
        return tracks;
    }

    public void setTracks(List<String> tracks) {
        this.tracks = tracks;
    }

    public int getCurrentTrackIndex() {
        return currentTrackIndex;
    }

    public void setCurrentTrackIndex(int currentTrackIndex) {
        this.currentTrackIndex = currentTrackIndex;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void setStopped(boolean isStopped) {
        this.isStopped = isStopped;
    }

    public boolean isRepeatEnabled() {
        return repeatEnabled;
    }

    public void setRepeatEnabled(boolean repeatEnabled) {
        this.repeatEnabled = repeatEnabled;
    }
}
