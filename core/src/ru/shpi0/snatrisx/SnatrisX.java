package ru.shpi0.snatrisx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import ru.shpi0.snatrisx.screen.MainScreen;

public class SnatrisX extends Game {

    private SnatrisX game;

    private Music music;
    private boolean isMusicAlreadyPlaying = false;
    private boolean musicEnabled = true;

    public SnatrisX() {
        super();
        game = this;
    }

    public void musicStop() {
        music.stop();
        musicEnabled = false;
    }

    public void musicPlay() {
        music.play();
        musicEnabled = true;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public void setMusicAlreadyPlaying(boolean musicAlreadyPlaying) {
        isMusicAlreadyPlaying = musicAlreadyPlaying;
    }

    public void setMusicEnabled(boolean musicEnabled) {
        this.musicEnabled = musicEnabled;
    }

    @Override
    public void dispose() {
        music.dispose();
        super.dispose();
    }

    @Override
    public void create() {
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/loop.mp3"));
        if (!isMusicAlreadyPlaying) {
            music.setVolume(0.25f);
            music.setLooping(true);
            music.play();
        }
        setScreen(new MainScreen(game));
    }
}
