package ru.shpi0.snatrisx;

import com.badlogic.gdx.Game;

import ru.shpi0.snatrisx.screen.MenuScreen;

public class SnatrisX extends Game {

    private Game game;

    public SnatrisX() {
        super();
        game = this;
    }

    @Override
    public void create() {
        setScreen(new MenuScreen(game));
    }
}
