package ru.shpi0.snatrisx.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.shpi0.snatrisx.base.BaseScreen;
import ru.shpi0.snatrisx.game.Direction;
import ru.shpi0.snatrisx.game.GameField;
import ru.shpi0.snatrisx.math.Rect;
import ru.shpi0.snatrisx.sprite.CloseBtn;
import ru.shpi0.snatrisx.sprite.Square;
import ru.shpi0.snatrisx.sprite.Target;

public class GameScreen extends BaseScreen {

    private GameField gameField = GameField.getInstance();

    private Rect upBtnArea = new Rect();
    private Rect downBtnArea = new Rect();
    private Rect leftBtnArea = new Rect();
    private Rect rightBtnArea = new Rect();

    private CloseBtn closeBtn;
    private Texture closeBtnTexture;

    private Target target;
    private Texture targetTexture;

    private Square[][] squares = new Square[5][GameField.MATRIX_WIDTH * GameField.MATRIX_HEIGHT];
    private TextureAtlas squareAtlas;

    private float stateTime = 0f;
    private float speed = 1f;

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
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                squares[i][j] = new Square(new TextureRegion(squareAtlas.findRegion("square" + i)));
            }
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        draw(delta);
    }

    @Override
    public void resize(Rect worldBounds) {
        closeBtn.resize(worldBounds);
        target.resize(worldBounds);
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                squares[i][j].resize(worldBounds);
            }
        }
        upBtnArea.pos.set(0f, worldBounds.getTop());
        upBtnArea.setHeight(worldBounds.getHeight() * 0.5f);
        upBtnArea.setWidth(worldBounds.getHeight());
        downBtnArea.pos.set(0f, worldBounds.getBottom());
        downBtnArea.setHeight(worldBounds.getHeight() * 0.5f);
        downBtnArea.setWidth(worldBounds.getHeight());
        leftBtnArea.pos.set(worldBounds.getLeft(), 0f);
        leftBtnArea.setHeight(worldBounds.getHeight() * 0.5f);
        leftBtnArea.setWidth(worldBounds.getHeight() * 0.5f);
        rightBtnArea.pos.set(worldBounds.getRight(), 0f);
        rightBtnArea.setHeight(worldBounds.getHeight() * 0.5f);
        rightBtnArea.setWidth(worldBounds.getHeight() * 0.5f);
    }

    private void draw(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        stateTime += delta;
        if (stateTime > speed) {
            stateTime = 0f;
            gameField.update();
        }
        closeBtn.draw(batch);
        drawGameField();
        batch.end();
    }

    private void drawGameField() {
        for (int i = 0; i < GameField.MATRIX_HEIGHT; i++) {
            for (int j = 0; j < GameField.MATRIX_WIDTH; j++) {
                if (gameField.gameMatrix[i][j] >= 0 && gameField.gameMatrix[i][j] <= 5) {
                    squares[gameField.gameMatrix[i][j]][i + j].pos.y = (worldBounds.getTop() - squares[gameField.gameMatrix[i][j]][i + j].getHalfHeight()) - (i * squares[gameField.gameMatrix[i][j]][i + j].getHeight());
                    squares[gameField.gameMatrix[i][j]][i + j].pos.x = (worldBounds.getLeft() + squares[gameField.gameMatrix[i][j]][i + j].getHalfWidth()) + (j * squares[gameField.gameMatrix[i][j]][i + j].getWidth()) + (worldBounds.getHalfWidth() - (squares[gameField.gameMatrix[i][j]][i + j].getHalfWidth() * GameField.MATRIX_WIDTH));
                    squares[gameField.gameMatrix[i][j]][i + j].draw(batch);
                }
                if (gameField.gameMatrix[i][j] == 99) {
                    target.pos.y = (worldBounds.getTop() - target.getHalfHeight()) - (i * target.getHeight());
                    target.pos.x = (worldBounds.getLeft() + target.getHalfWidth()) + (j * target.getWidth()) + (worldBounds.getHalfWidth() - (target.getHalfWidth() * GameField.MATRIX_WIDTH));
                    target.draw(batch);
                }
            }
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (closeBtn.isMe(touch)) {
            closeBtn.setScale(1.25f);
        }
        if (upBtnArea.isMe(touch) && !closeBtn.isMe(touch)) {
            gameField.areaTouched(Direction.UP);
        }
        if (downBtnArea.isMe(touch)) {
            gameField.areaTouched(Direction.DOWN);
        }
        if (leftBtnArea.isMe(touch)) {
            gameField.areaTouched(Direction.LEFT);
        }
        if (rightBtnArea.isMe(touch)) {
            gameField.areaTouched(Direction.RIGHT);
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

    @Override
    public void dispose() {
        closeBtnTexture.dispose();
        targetTexture.dispose();
        squareAtlas.dispose();
        super.dispose();
    }
}
