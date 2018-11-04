package ru.shpi0.snatrisx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.shpi0.snatrisx.SnatrisX;
import ru.shpi0.snatrisx.base.BaseScreen;
import ru.shpi0.snatrisx.math.Rect;
import ru.shpi0.snatrisx.sprite.Background;

public class AboutScreen extends BaseScreen {

    private int currentScreen = 1;
    private int screens = 2;

    private Background[] bg = new Background[screens];
    private TextureAtlas bgAtlas = new TextureAtlas("about.atlas");


    public AboutScreen(SnatrisX game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        for (int i = 1; i <= 2; i++) {
            bg[i - 1] = new Background(new TextureRegion(bgAtlas.findRegion("about" + i)));
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        bg[currentScreen - 1].draw(batch);
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        for (int i = 1; i <= 2; i++) {
            bg[i - 1].setWidthProportion(worldBounds.getWidth());
        }
    }

    @Override
    public void dispose() {
        bgAtlas.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (currentScreen == screens) {
            game.setScreen(new MenuScreen(game));
        } else {
            currentScreen++;
        }
        return super.touchDown(touch, pointer);
    }
}
