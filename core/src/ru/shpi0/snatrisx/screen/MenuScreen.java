package ru.shpi0.snatrisx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ru.shpi0.snatrisx.base.BaseScreen;

public class MenuScreen extends BaseScreen {
    SpriteBatch batch;
    Texture img;
    Texture square1;

    private int width = 0;
    private int height = 0;
    private Vector2 initVector = new Vector2();
    private Vector2 moveVector = new Vector2();
    private Vector2 destVector = new Vector2();
    private float alpha = 0f;
    private boolean growUp = true;
    private boolean leftMove = false;
    private boolean rightMove = false;
    private boolean upMove = false;
    private boolean downMove = false;

    @Override
    public void show() {
        super.show();
        batch = new SpriteBatch();
        img = new Texture("snatris_logo.png");
        square1 = new Texture("square_blue.png");
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        initVector.x = (width - img.getWidth()) / 2;
        initVector.y = (height - img.getHeight()) / 2;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.setColor(1, 1, 1, alpha);
        batch.draw(img, initVector.x, initVector.y);
        batch.end();
        calculateAlpha();
        if (Math.abs(destVector.x - initVector.x) > 0.5f || Math.abs(destVector.y - initVector.y) > 0.5f) {
            initVector.add(moveVector);
        }
        if (leftMove) {
            destVector.x = initVector.x - 1f;
            destVector.y = initVector.y;
            norMoveVector();
        }
        if (rightMove) {
            destVector.x = initVector.x + 1f;
            destVector.y = initVector.y;
            norMoveVector();
        }
        if (upMove) {
            destVector.x = initVector.x;
            destVector.y = initVector.y + 1f;
            norMoveVector();
        }
        if (downMove) {
            destVector.x = initVector.x;
            destVector.y = initVector.y - 1f;
            norMoveVector();
        }
    }

    private void calculateAlpha() {
        if (growUp) {
            alpha += 0.006f;
        } else {
            alpha -= 0.006f;
        }
        if (alpha >= 1f) {
            growUp = false;
        }
        if (alpha <= 0.007f) {
            growUp = true;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        destVector.x = screenX - (img.getWidth() / 2);
        destVector.y = (height - screenY) - (img.getHeight() / 2);
        norMoveVector();
        return super.touchDown(screenX, screenY, pointer, button);
    }

    private void norMoveVector() {
        moveVector = destVector.cpy().sub(initVector);
        moveVector.nor();
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                upMove = false;
                break;
            case Input.Keys.DOWN:
                downMove = false;
                break;
            case Input.Keys.LEFT:
                leftMove = false;
                break;
            case Input.Keys.RIGHT:
                rightMove = false;
                break;
        }
        return super.keyUp(keycode);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                upMove = true;
                break;
            case Input.Keys.DOWN:
                downMove = true;
                break;
            case Input.Keys.LEFT:
                leftMove = true;
                break;
            case Input.Keys.RIGHT:
                rightMove = true;
                break;
        }
        return super.keyDown(keycode);
    }
}
