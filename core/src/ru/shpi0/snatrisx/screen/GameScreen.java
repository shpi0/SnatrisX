package ru.shpi0.snatrisx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import ru.shpi0.snatrisx.SnatrisX;
import ru.shpi0.snatrisx.base.BaseScreen;
import ru.shpi0.snatrisx.game.Direction;
import ru.shpi0.snatrisx.game.GameField;
import ru.shpi0.snatrisx.math.Rect;
import ru.shpi0.snatrisx.sprite.Background;
import ru.shpi0.snatrisx.sprite.CloseBtn;
import ru.shpi0.snatrisx.sprite.Logo;
import ru.shpi0.snatrisx.sprite.MusicButton;
import ru.shpi0.snatrisx.sprite.PauseBtn;
import ru.shpi0.snatrisx.sprite.ResumeBtn;
import ru.shpi0.snatrisx.sprite.SoundButton;
import ru.shpi0.snatrisx.sprite.Square;
import ru.shpi0.snatrisx.sprite.Target;

public class GameScreen extends BaseScreen {

    private GameField gameField = GameField.getInstance();

    private Stage stage;
    private Label.LabelStyle scoreLabelStyle = new Label.LabelStyle();
    private BitmapFont font = new BitmapFont();
    private Label scoreLabel;

    private Rect upBtnArea = new Rect();
    private Rect downBtnArea = new Rect();
    private Rect leftBtnArea = new Rect();
    private Rect rightBtnArea = new Rect();

    private Texture backgroundTexture;
    private Background bg;

    private CloseBtn closeBtn;
    private PauseBtn pauseBtn;
    private ResumeBtn resumeBtn;
    private SoundButton soundOnButton;
    private SoundButton soundOffButton;
    private MusicButton musicOnButton;
    private MusicButton musicOffButton;
    private TextureAtlas buttonsAtlas;

    private Target target;
    private Texture targetTexture;

    private Logo gameOver;
    private Texture gameOverTexture;

    private Logo pauseMsg;
    private Texture pauseTexture;

    private Square[][] squares = new Square[6][GameField.MATRIX_WIDTH * GameField.MATRIX_HEIGHT];
    private Square[] blackSquares = new Square[GameField.MATRIX_WIDTH * GameField.MATRIX_HEIGHT];
    private TextureAtlas squareAtlas;

    private boolean destroyingAnimationProcess = false;
    private Integer lineIdxToDestroy;
    private int destroyLinesFrame = 0;

    private float stateTime = 0f;

    public GameScreen(SnatrisX game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        backgroundTexture = new Texture("bg.jpg");
        bg = new Background(new TextureRegion(backgroundTexture));
        buttonsAtlas = new TextureAtlas("gamebtns.atlas");
        closeBtn = new CloseBtn(new TextureRegion(buttonsAtlas.findRegion("close")));
        pauseBtn = new PauseBtn(new TextureRegion(buttonsAtlas.findRegion("pause")));
        resumeBtn = new ResumeBtn(new TextureRegion(buttonsAtlas.findRegion("resume")));
        soundOnButton = new SoundButton(new TextureRegion(buttonsAtlas.findRegion("soundson")));
        soundOffButton = new SoundButton(new TextureRegion(buttonsAtlas.findRegion("soundsoff")));
        musicOnButton = new MusicButton(new TextureRegion(buttonsAtlas.findRegion("musicon")));
        musicOffButton = new MusicButton(new TextureRegion(buttonsAtlas.findRegion("musicoff")));
        pauseTexture = new Texture("pauseMsg.png");
        pauseMsg = new Logo(new TextureRegion(pauseTexture));
        targetTexture = new Texture("target.png");
        target = new Target(new TextureRegion(targetTexture));
        squareAtlas = new TextureAtlas("square.atlas");
        gameOverTexture = new Texture("gameover.png");
        gameOver = new Logo(new TextureRegion(gameOverTexture));
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                squares[i][j] = new Square(new TextureRegion(squareAtlas.findRegion("square" + i)));
            }
        }
        for (int i = 0; i < blackSquares.length; i++) {
            blackSquares[i] = new Square(new TextureRegion(squareAtlas.findRegion("square6")));
        }
        stage = new Stage(new ScreenViewport());
        scoreLabelStyle.font = font;
        scoreLabelStyle.fontColor = Color.WHITE;
        scoreLabel = new Label("Score: 0", scoreLabelStyle);
        int row_height = Gdx.graphics.getWidth() / 12;
        scoreLabel.setSize(Gdx.graphics.getWidth(),row_height);
//        scoreLabel.setPosition(0,Gdx.graphics.getHeight()-row_height*2);
//        scoreLabel.setAlignment(Align.right);
        scoreLabel.setPosition(0f,0f);
        scoreLabel.setAlignment(Align.right);
        stage.addActor(scoreLabel);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        draw(delta);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        bg.resize(worldBounds);
        closeBtn.resize(worldBounds);
        pauseBtn.resize(worldBounds);
        resumeBtn.resize(worldBounds);
        soundOnButton.resize(worldBounds);
        soundOffButton.resize(worldBounds);
        musicOnButton.resize(worldBounds);
        musicOffButton.resize(worldBounds);
        target.resize(worldBounds);
        gameOver.setHeightProportion(0.5f);
        gameOver.resize(worldBounds);
        pauseMsg.setWidthProportion(0.75f);
        pauseMsg.resize(worldBounds);
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
        bg.draw(batch);
        stateTime += delta;
        if (stateTime > gameField.getSpeed() * gameField.getSpeedModificator() * gameField.getDifficultModificator()) {
            stateTime = 0f;
            gameField.update();
            scoreLabel.setText("Score: " + gameField.getScore());
        }
        closeBtn.draw(batch);
        if (gameField.isPaused()) {
            resumeBtn.draw(batch);
        } else {
            pauseBtn.draw(batch);
        }
        drawGameField();
        if (gameField.isGameOver()) {
            gameOver.draw(batch);
        }
        if (gameField.isPaused()) {
            pauseMsg.draw(batch);
        }
        if (game.isMusicEnabled()) {
            musicOnButton.draw(batch);
        } else {
            musicOffButton.draw(batch);
        }
        if (gameField.isSoundsEnabled()) {
            soundOnButton.draw(batch);
        } else {
            soundOffButton.draw(batch);
        }
        batch.end();
    }

    private void drawGameField() {
        for (int i = 0; i < GameField.MATRIX_HEIGHT; i++) {
            for (int j = 0; j < GameField.MATRIX_WIDTH; j++) {
                if (gameField.gameMatrix[i][j] == -1 || gameField.gameMatrix[i][j] == 99) {
                    blackSquares[i * 10 + j].pos.y = (worldBounds.getTop() - blackSquares[i * 10 + j].getHalfHeight()) - (i * blackSquares[i * 10 + j].getHeight());
                    blackSquares[i * 10 + j].pos.x = (worldBounds.getLeft() + blackSquares[i * 10 + j].getHalfWidth()) + (j * blackSquares[i * 10 + j].getWidth()) + (worldBounds.getHalfWidth() - (blackSquares[i * 10 + j].getHalfWidth() * GameField.MATRIX_WIDTH));
                    blackSquares[i * 10 + j].draw(batch);
                }
                if (gameField.gameMatrix[i][j] >= 0 && gameField.gameMatrix[i][j] <= 5) {
                    if (!destroyingAnimationProcess) {
                        lineIdxToDestroy = gameField.pickLineFromQueue();
                        if (lineIdxToDestroy != null) {
                            destroyingAnimationProcess = true;
                        } else {
                            gameField.setHasLinesToDrop(false);
                        }
                    } else {
                        if (lineIdxToDestroy == i) {
                            if (destroyLinesFrame > 20) {
                                destroyingAnimationProcess = false;
                                destroyLinesFrame = 0;
                                gameField.dropLinesDown(lineIdxToDestroy);
                                break;
                            }
                            squares[gameField.gameMatrix[i][j]][i * 10 + j].setScale(1f - ((destroyLinesFrame + 1) * 0.04f));
                            destroyLinesFrame++;
                        }
                    }
                    if (!destroyingAnimationProcess) {
                        squares[gameField.gameMatrix[i][j]][i * 10 + j].setScale(1f);
                    }
                    squares[gameField.gameMatrix[i][j]][i * 10 + j].pos.y = (worldBounds.getTop() - squares[gameField.gameMatrix[i][j]][i * 10 + j].getHalfHeight()) - (i * squares[gameField.gameMatrix[i][j]][i * 10 + j].getHeight());
                    squares[gameField.gameMatrix[i][j]][i * 10 + j].pos.x = (worldBounds.getLeft() + squares[gameField.gameMatrix[i][j]][i * 10 + j].getHalfWidth()) + (j * squares[gameField.gameMatrix[i][j]][i * 10 + j].getWidth()) + (worldBounds.getHalfWidth() - (squares[gameField.gameMatrix[i][j]][i * 10 + j].getHalfWidth() * GameField.MATRIX_WIDTH));
                    squares[gameField.gameMatrix[i][j]][i * 10 + j].draw(batch);
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
        if (upBtnArea.isMe(touch) && !closeBtn.isMe(touch) && !resumeBtn.isMe(touch) && !soundOnButton.isMe(touch) && !musicOnButton.isMe(touch)) {
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
        if (gameOver.isMe(touch)) {
            if (gameField.isGameOver()) {
                gameField.newGame();
            }
        }
        if (resumeBtn.isMe(touch)) {
            gameField.setPaused(!gameField.isPaused());
        }
        if (soundOnButton.isMe(touch)) {
            gameField.setSoundsEnabled(!gameField.isSoundsEnabled());
        }
        if (musicOnButton.isMe(touch)) {
            if (game.isMusicEnabled()) {
                game.musicStop();
                game.setMusicEnabled(false);
            } else {
                game.musicPlay();
                game.setMusicEnabled(true);
            }
        }
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (closeBtn.isMe(touch)) {
            gameField.newGame();
            game.setScreen(new MenuScreen(game));
        }
        closeBtn.setScale(1f);
        return super.touchUp(touch, pointer);
    }

    @Override
    public void dispose() {
        gameField.dispose();
        stage.dispose();
        font.dispose();
        pauseTexture.dispose();
        backgroundTexture.dispose();
        buttonsAtlas.dispose();
        targetTexture.dispose();
        squareAtlas.dispose();
        gameOverTexture.dispose();
        super.dispose();
    }
}
