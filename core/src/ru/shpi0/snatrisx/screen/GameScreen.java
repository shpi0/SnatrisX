package ru.shpi0.snatrisx.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.shpi0.snatrisx.base.BaseScreen;
import ru.shpi0.snatrisx.math.Rect;
import ru.shpi0.snatrisx.sprite.CloseBtn;

public class GameScreen extends BaseScreen {

    private CloseBtn closeBtn;
    private Texture closeBtnTexture;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        closeBtnTexture = new Texture("close.png");
        closeBtn = new CloseBtn(new TextureRegion(closeBtnTexture));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        closeBtn.resize(worldBounds);
    }

    private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        closeBtn.draw(batch);
        batch.end();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (closeBtn.isMe(touch)) {
            closeBtn.setScale(1.25f);
        }
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (closeBtn.isMe(touch)) {
            Gdx.app.exit();
        }
        closeBtn.setScale(1f);
        return super.touchUp(touch, pointer);
    }
}
