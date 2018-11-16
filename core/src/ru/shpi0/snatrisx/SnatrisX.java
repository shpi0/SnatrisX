package ru.shpi0.snatrisx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import ru.shpi0.snatrisx.base.FileProcessor;
import ru.shpi0.snatrisx.base.GamePreferences;
import ru.shpi0.snatrisx.base.UserBestScores;
import ru.shpi0.snatrisx.screen.MainScreen;

public class SnatrisX extends Game {

    private SnatrisX game;
    private GamePreferences gamePreferences;
    private UserBestScores userBestScores;

    private Music music;
    private boolean isMusicAlreadyPlaying = false;

    public SnatrisX() {
        super();
        game = this;
    }

    public void musicStop() {
        music.stop();
        gamePreferences.setMusicOn(false);
        FileProcessor.saveGamePreferencesToFile(gamePreferences);
    }

    public void musicPlay() {
        music.play();
        gamePreferences.setMusicOn(true);
        FileProcessor.saveGamePreferencesToFile(gamePreferences);
    }

    public boolean isMusicEnabled() {
        return gamePreferences.isMusicOn();
    }

    public void setMusicEnabled(boolean musicEnabled) {
        gamePreferences.setMusicOn(musicEnabled);
        FileProcessor.saveGamePreferencesToFile(gamePreferences);
    }

    public GamePreferences getGamePreferences() {
        return gamePreferences;
    }

    public UserBestScores getUserBestScores() {
        return userBestScores;
    }

    @Override
    public void dispose() {
        music.dispose();
        super.dispose();
    }

    @Override
    public void create() {
        if (FileProcessor.ifPrefsFileExist()) {
            gamePreferences = FileProcessor.loadGamePreferencesFromFile();
        } else {
            gamePreferences = new GamePreferences();
            gamePreferences.setNewUserUUID();
            gamePreferences.setMusicOn(true);
            gamePreferences.setSoundsOn(true);
            FileProcessor.saveGamePreferencesToFile(gamePreferences);
        }
        if (FileProcessor.ifScoresFileExist()) {
            userBestScores = FileProcessor.loadUserBestScoresFromFile();
        } else {
            userBestScores = new UserBestScores();
            userBestScores.setLastBestScore(0);
            FileProcessor.saveUserBestScoresToFile(userBestScores);
        }
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/loop.mp3"));
        if (!isMusicAlreadyPlaying) {
            music.setVolume(0.25f);
            music.setLooping(true);
            if (isMusicEnabled()) {
                music.play();
            }
        }
        setScreen(new MainScreen(game));
    }
}
