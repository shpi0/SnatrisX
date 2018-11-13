package ru.shpi0.snatrisx.base;

public class GamePreferences {

    private boolean musicOn;
    private boolean soundsOn;

    public boolean isMusicOn() {
        return musicOn;
    }

    public void setMusicOn(boolean musicOn) {
        this.musicOn = musicOn;
    }

    public boolean isSoundsOn() {
        return soundsOn;
    }

    public void setSoundsOn(boolean soundsOn) {
        this.soundsOn = soundsOn;
    }
}
