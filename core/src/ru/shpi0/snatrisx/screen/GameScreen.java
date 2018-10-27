package ru.shpi0.snatrisx.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.shpi0.snatrisx.base.BaseScreen;
import ru.shpi0.snatrisx.math.Rect;
import ru.shpi0.snatrisx.math.Rnd;
import ru.shpi0.snatrisx.sprite.CloseBtn;
import ru.shpi0.snatrisx.sprite.Square;
import ru.shpi0.snatrisx.sprite.Target;

public class GameScreen extends BaseScreen {

    private static final int MATRIX_WIDTH = 10;
    private static final int MATRIX_HEIGHT = 20;
    private int[][] gameMatrix = new int[MATRIX_HEIGHT][MATRIX_WIDTH];
    /**
     *  0 - empty
     *  1 - square
     * -1 - target
     */

    private CloseBtn closeBtn;
    private Texture closeBtnTexture;

    private Target target;
    private Texture targetTexture;

    private Square square;
    private TextureAtlas squareAtlas;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        closeBtnTexture = new Texture("close.png");
        closeBtn = new CloseBtn(new TextureRegion(closeBtnTexture));
        targetTexture = new Texture("target.png");
        target = new Target(new TextureRegion(targetTexture));
        squareAtlas = new TextureAtlas("square.atlas");
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        closeBtn.resize(worldBounds);
        target.resize(worldBounds);
    }

    private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        closeBtn.draw(batch);
        target.draw(batch);
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

    private void putTarget() {
        int xPos = (int) (Rnd.nextFloat(0f,   1f) * 10);
        int yPos = (int) (Rnd.nextFloat(0f, 0.4f) * 10);
        gameMatrix[xPos][yPos] = -1;
    }

    private void fillGameMatrix() {
        for (int i = 0; i < MATRIX_WIDTH; i++) {
            for (int j = 0; j < MATRIX_HEIGHT; j++) {
                gameMatrix[i][j] = 0;
            }
        }
    }

    @Override
    public void dispose() {
        closeBtnTexture.dispose();
        targetTexture.dispose();
        squareAtlas.dispose();
        super.dispose();
    }
}
