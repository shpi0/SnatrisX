package ru.shpi0.snatrisx.base;

import java.util.UUID;

public class GamePreferences {

    private boolean musicOn;
    private boolean soundsOn;
    private UUID userUUID;
    private String userName;

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

    public UUID getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setNewUserUUID() {
        setUserUUID(UUID.randomUUID());
    }
}
