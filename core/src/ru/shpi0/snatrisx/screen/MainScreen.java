package ru.shpi0.snatrisx.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.shpi0.snatrisx.SnatrisX;
import ru.shpi0.snatrisx.base.BaseScreen;
import ru.shpi0.snatrisx.base.FileProcessor;
import ru.shpi0.snatrisx.base.GamePreferences;
import ru.shpi0.snatrisx.base.UserBestScores;
import ru.shpi0.snatrisx.game.Direction;
import ru.shpi0.snatrisx.game.GameField;
import ru.shpi0.snatrisx.math.Rect;
import ru.shpi0.snatrisx.sprite.Background;
import ru.shpi0.snatrisx.sprite.ButtonDiffLvl;
import ru.shpi0.snatrisx.sprite.ButtonMoveDown;
import ru.shpi0.snatrisx.sprite.ButtonMoveLeft;
import ru.shpi0.snatrisx.sprite.ButtonMoveRight;
import ru.shpi0.snatrisx.sprite.ButtonMoveUp;
import ru.shpi0.snatrisx.sprite.ButtonRotate;
import ru.shpi0.snatrisx.sprite.CoinAcceptor;
import ru.shpi0.snatrisx.sprite.ScoreScreen;
import ru.shpi0.snatrisx.sprite.Square;
import ru.shpi0.snatrisx.sprite.Target;
import ru.shpi0.snatrisx.sprite.TextMessage;
import ru.shpi0.snatrisx.sprite.TextMessageDigit;
import ru.shpi0.snatrisx.sprite.UIExitButton;
import ru.shpi0.snatrisx.sprite.UIMusicButton;
import ru.shpi0.snatrisx.sprite.UIPauseButton;
import ru.shpi0.snatrisx.sprite.UISoundButton;

public class MainScreen extends BaseScreen {

    GameField gameField = GameField.getInstance();

    private Texture backgroundTexture;
    private Background bg;

    private TextureAtlas squareAtlas;
    private Square[][] squares = new Square[6][GameField.MATRIX_WIDTH * GameField.MATRIX_HEIGHT];
    private Target target;
    private float stateTimeForGame = 0f;
    private boolean destroyingAnimationProcess = false;
    private Integer lineIdxToDestroy;
    private int destroyLinesFrame = 0;

    private TextureAtlas coinAcceptorAtlas;
    private CoinAcceptor[] coinAcceptor;
    private int acceptorCurrentFrame = 0;
    private boolean isDroppingCoin = false;
    private float stateTime = 0f;

    private TextureAtlas textMessagesAtlas;
    private TextMessage gameOverMessage;
    private TextMessage scoreMessage;
    private TextMessage pauseMessage;
    private TextMessage insertCoinMessage;
    private TextMessage diffEasyMessage;
    private TextMessage diffNormMessage;
    private TextMessage diffHardMessage;
    private TextMessageDigit[][] digits;
    private boolean show1stMessage = true;
    private float stateTimeForText = 0f;
    private boolean showDiffLvl = false;
    private float showDiffLvlvState = 0f;

    private TextureAtlas gameButtons;
    private ButtonMoveLeft buttonMoveLeft;
    private ButtonMoveLeft buttonMoveLeftTouched;
    private ButtonMoveRight buttonMoveRight;
    private ButtonMoveRight buttonMoveRightTouched;
    private ButtonMoveUp buttonMoveUp;
    private ButtonMoveUp buttonMoveUpTouched;
    private ButtonMoveDown buttonMoveDown;
    private ButtonMoveDown buttonMoveDownTouched;
    private ButtonRotate buttonRotate;
    private ButtonRotate buttonRotateTouched;
    private ButtonDiffLvl buttonDiffLvl;
    private ButtonDiffLvl buttonDiffLvlTouched;
    private boolean leftButtonTouched = false;
    private boolean rightButtonTouched = false;
    private boolean upButtonTouched = false;
    private boolean downButtonTouched = false;
    private boolean rotateButtonTouched = false;
    private boolean diffButtonTouched = false;

    private TextureAtlas uiButtons;
    private UIExitButton uiExitButton;
    private UIExitButton uiExitButtonTouched;
    private UIMusicButton uiMusicOnButton;
    private UIMusicButton uiMusicOffButton;
    private UISoundButton uiSoundOnButton;
    private UISoundButton uiSoundOffButton;
    private UIPauseButton uiPauseButton;
    private UIPauseButton uiResumeButton;
    private boolean exitButtonTouched = false;

    private Texture scoreScreenTexture;
    private ScoreScreen scoreScreen;

    private GamePreferences gamePreferences = game.getGamePreferences();
    private UserBestScores userBestScores = game.getUserBestScores();

    public MainScreen(SnatrisX game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        squareAtlas = new TextureAtlas("square.atlas");
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                squares[i][j] = new Square(new TextureRegion(squareAtlas.findRegion("square" + i)));
            }
        }
        target = new Target(new TextureRegion(squareAtlas.findRegion("target")));


        textMessagesAtlas = new TextureAtlas("text.atlas");
        gameOverMessage = new TextMessage(new TextureRegion(textMessagesAtlas.findRegion("gameover")));
        pauseMessage = new TextMessage(new TextureRegion(textMessagesAtlas.findRegion("pause")));
        insertCoinMessage = new TextMessage(new TextureRegion(textMessagesAtlas.findRegion("insertcoin")));
        scoreMessage = new TextMessage(new TextureRegion(textMessagesAtlas.findRegion("score")));
        diffEasyMessage = new TextMessage(new TextureRegion(textMessagesAtlas.findRegion("diffeasy")));
        diffNormMessage = new TextMessage(new TextureRegion(textMessagesAtlas.findRegion("diffnorm")));
        diffHardMessage = new TextMessage(new TextureRegion(textMessagesAtlas.findRegion("diffhard")));
        digits = new TextMessageDigit[7][10];
        for (int i = 0; i < digits.length; i++) {
            for (int j = 0; j < digits[i].length; j++) {
                digits[i][j] = new TextMessageDigit(new TextureRegion(textMessagesAtlas.findRegion(String.valueOf(j))));
            }
        }

        gameButtons = new TextureAtlas("gamebuttons.atlas");
        buttonMoveLeft = new ButtonMoveLeft(new TextureRegion(gameButtons.findRegion("left")));
        buttonMoveLeftTouched = new ButtonMoveLeft(new TextureRegion(gameButtons.findRegion("lefttouched")));
        buttonMoveRight = new ButtonMoveRight(new TextureRegion(gameButtons.findRegion("right")));
        buttonMoveRightTouched = new ButtonMoveRight(new TextureRegion(gameButtons.findRegion("righttouched")));
        buttonMoveDown = new ButtonMoveDown(new TextureRegion(gameButtons.findRegion("down")));
        buttonMoveDownTouched = new ButtonMoveDown(new TextureRegion(gameButtons.findRegion("downtouched")));
        buttonMoveUp = new ButtonMoveUp(new TextureRegion(gameButtons.findRegion("up")));
        buttonMoveUpTouched = new ButtonMoveUp(new TextureRegion(gameButtons.findRegion("uptouched")));
        buttonRotate = new ButtonRotate(new TextureRegion(gameButtons.findRegion("rotate")));
        buttonRotateTouched = new ButtonRotate(new TextureRegion(gameButtons.findRegion("rotatetouched")));
        buttonDiffLvl = new ButtonDiffLvl(new TextureRegion(gameButtons.findRegion("difflvl")));
        buttonDiffLvlTouched = new ButtonDiffLvl(new TextureRegion(gameButtons.findRegion("difflvltouched")));

        uiButtons = new TextureAtlas("uibuttons.atlas");
        uiExitButton = new UIExitButton(new TextureRegion(uiButtons.findRegion("close1")));
        uiExitButtonTouched = new UIExitButton(new TextureRegion(uiButtons.findRegion("close2")));
        uiMusicOnButton = new UIMusicButton(new TextureRegion(uiButtons.findRegion("musicon")));
        uiMusicOffButton = new UIMusicButton(new TextureRegion(uiButtons.findRegion("musicoff")));
        uiSoundOnButton = new UISoundButton(new TextureRegion(uiButtons.findRegion("soundson")));
        uiSoundOffButton = new UISoundButton(new TextureRegion(uiButtons.findRegion("soundoff")));
        uiPauseButton = new UIPauseButton(new TextureRegion(uiButtons.findRegion("pause")));
        uiResumeButton = new UIPauseButton(new TextureRegion(uiButtons.findRegion("play")));

        scoreScreenTexture = new Texture("scorescreen.png");
        scoreScreen = new ScoreScreen(new TextureRegion(scoreScreenTexture));

        coinAcceptorAtlas = new TextureAtlas("coinacceptor.atlas");
        coinAcceptor = new CoinAcceptor[coinAcceptorAtlas.getRegions().size];
        for (int i = 0; i < coinAcceptor.length; i++) {
            coinAcceptor[i] = new CoinAcceptor(new TextureRegion(coinAcceptorAtlas.findRegion("coin" + i)));
        }

        backgroundTexture = new Texture("bg.jpg");
        bg = new Background(new TextureRegion(backgroundTexture));
    }

    @Override
    public void resize(Rect worldBounds) {
        GameField.getInstance().setGamePreferences(gamePreferences);
        GameField.getInstance().setUserBestScores(userBestScores);
        bg.resize(worldBounds);
        gameOverMessage.resize(worldBounds);
        pauseMessage.resize(worldBounds);
        insertCoinMessage.resize(worldBounds);
        scoreMessage.resize(worldBounds);
        for (int i = 0; i < digits.length; i++) {
            for (int j = 0; j < digits[i].length; j++) {
                digits[i][j].resize(worldBounds);
            }
        }
        buttonMoveLeft.resize(worldBounds);
        buttonMoveLeftTouched.resize(worldBounds);
        buttonMoveRight.resize(worldBounds);
        buttonMoveRightTouched.resize(worldBounds);
        buttonMoveDown.resize(worldBounds);
        buttonMoveDownTouched.resize(worldBounds);
        buttonMoveUp.resize(worldBounds);
        buttonMoveUpTouched.resize(worldBounds);
        buttonRotate.resize(worldBounds);
        buttonRotateTouched.resize(worldBounds);
        buttonDiffLvl.resize(worldBounds);
        buttonDiffLvlTouched.resize(worldBounds);
        uiExitButton.resize(worldBounds);
        uiExitButtonTouched.resize(worldBounds);
        uiMusicOnButton.resize(worldBounds);
        uiMusicOffButton.resize(worldBounds);
        uiSoundOnButton.resize(worldBounds);
        uiSoundOffButton.resize(worldBounds);
        uiPauseButton.resize(worldBounds);
        uiResumeButton.resize(worldBounds);
        scoreScreen.resize(worldBounds);
        diffEasyMessage.resize(worldBounds);
        diffNormMessage.resize(worldBounds);
        diffHardMessage.resize(worldBounds);
        for (int i = 0; i < coinAcceptor.length; i++) {
            coinAcceptor[i].resize(worldBounds);
        }
        target.resize(worldBounds);
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                squares[i][j].resize(worldBounds);
            }
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        draw(delta);
    }

    private void draw(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        stateTimeForGame += delta;
        if (stateTimeForGame > gameField.getSpeed() * gameField.getSpeedModificator() * gameField.getDifficultModificator()) {
            stateTimeForGame = 0f;
            gameField.update();
        }
        bg.draw(batch);
        if (exitButtonTouched) {
            uiExitButtonTouched.draw(batch);
        } else {
            uiExitButton.draw(batch);
        }
        if (gameField.isPaused()) {
            uiResumeButton.draw(batch);
        } else {
            uiPauseButton.draw(batch);
        }
        if (gameField.isSoundsEnabled()) {
            uiSoundOnButton.draw(batch);
        } else {
            uiSoundOffButton.draw(batch);
        }
        if (game.isMusicEnabled()) {
            uiMusicOnButton.draw(batch);
        } else {
            uiMusicOffButton.draw(batch);
        }
        scoreScreen.draw(batch);
        if (gameField.isGameOver()) {
            if (showDiffLvl) {
                showDiffLvlvState += delta;
                if (showDiffLvlvState > 2f) {
                    showDiffLvl = false;
                    showDiffLvlvState = 0f;
                }
                switch (gameField.getDiffLvl()) {
                    case EASY:
                        diffEasyMessage.draw(batch);
                        break;
                    case NORM:
                        diffNormMessage.draw(batch);
                        break;
                    case HARD:
                        diffHardMessage.draw(batch);
                        break;
                }
            } else {
                stateTimeForText += delta;
                if (stateTimeForText > 1f) {
                    show1stMessage = !show1stMessage;
                    stateTimeForText = 0f;
                }
                if (show1stMessage) {
                    gameOverMessage.draw(batch);
                } else {
                    insertCoinMessage.draw(batch);
                }
            }
        } else {
            if (gameField.isPaused()) {
                pauseMessage.draw(batch);
            } else {
                scoreMessage.draw(batch);
                drawScores();
            }
        }
        if (rotateButtonTouched) {
            buttonRotateTouched.draw(batch);
        } else {
            buttonRotate.draw(batch);
        }
        if (leftButtonTouched) {
            buttonMoveLeftTouched.draw(batch);
        } else {
            buttonMoveLeft.draw(batch);
        }
        if (rightButtonTouched) {
            buttonMoveRightTouched.draw(batch);
        } else {
            buttonMoveRight.draw(batch);
        }
        if (upButtonTouched) {
            buttonMoveUpTouched.draw(batch);
        } else {
            buttonMoveUp.draw(batch);
        }
        if (downButtonTouched) {
            buttonMoveDownTouched.draw(batch);
        } else {
            buttonMoveDown.draw(batch);
        }
        if (diffButtonTouched) {
            buttonDiffLvlTouched.draw(batch);
        } else {
            buttonDiffLvl.draw(batch);
        }
        if (!isDroppingCoin) {
            coinAcceptor[coinAcceptor.length - 1].draw(batch);
        } else {
            stateTime += delta;
            coinAcceptor[acceptorCurrentFrame].draw(batch);
            if (stateTime > 0.1f) {
                acceptorCurrentFrame++;
                if (acceptorCurrentFrame > coinAcceptor.length - 1) {
                    acceptorCurrentFrame = 0;
                    isDroppingCoin = false;
                    gameField.newGame();
                }
                stateTime = 0f;
            }
        }
        drawGameField();
        batch.end();
    }

    private void drawScores() {
        int s = gameField.getScore();
        int[] sc = new int[7];
        int idx = 6;
        while (s > 0) {
            sc[idx] = s % 10;
            s /= 10;
            idx--;
        }
        while (idx >= 0) {
            sc[idx] = 0;
            idx--;
        }
        for (int i = 0; i < digits.length; i++) {
            digits[i][sc[i]].pos.x = worldBounds.pos.x + digits[i][sc[i]].getHalfWidth() * (i + 1) - digits[i][sc[i]].getHalfWidth();
            digits[i][sc[i]].draw(batch);
        }

    }

    private void drawGameField() {
        for (int i = 0; i < GameField.MATRIX_HEIGHT; i++) {
            for (int j = 0; j < GameField.MATRIX_WIDTH; j++) {
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
                    squares[gameField.gameMatrix[i][j]][i * 10 + j].pos.y = (worldBounds.getTop() - squares[gameField.gameMatrix[i][j]][i * 10 + j].getHalfHeight()) - (i * squares[gameField.gameMatrix[i][j]][i * 10 + j].getHeight()) - 0.04467f + 0.0015f; // 0.04467f
                    squares[gameField.gameMatrix[i][j]][i * 10 + j].pos.x = (worldBounds.getLeft() + squares[gameField.gameMatrix[i][j]][i * 10 + j].getHalfWidth()) + (j * squares[gameField.gameMatrix[i][j]][i * 10 + j].getWidth()) + (worldBounds.getHalfWidth() - (squares[gameField.gameMatrix[i][j]][i * 10 + j].getHalfWidth() * GameField.MATRIX_WIDTH)) + 0.002f;
                    squares[gameField.gameMatrix[i][j]][i * 10 + j].draw(batch);
                }
                if (gameField.gameMatrix[i][j] == 99) {
                    target.pos.y = (worldBounds.getTop() - target.getHalfHeight()) - (i * target.getHeight()) - 0.04467f;
                    target.pos.x = (worldBounds.getLeft() + target.getHalfWidth()) + (j * target.getWidth()) + (worldBounds.getHalfWidth() - (target.getHalfWidth() * GameField.MATRIX_WIDTH)) + 0.002f;
                    target.draw(batch);
                }
            }
        }
    }

    @Override
    public void dispose() {
        squareAtlas.dispose();
        textMessagesAtlas.dispose();
        gameButtons.dispose();
        uiButtons.dispose();
        scoreScreenTexture.dispose();
        coinAcceptorAtlas.dispose();
        backgroundTexture.dispose();
        super.dispose();
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (uiExitButton.isMe(touch) && exitButtonTouched) {
            FileProcessor.saveGamePreferencesToFile(gamePreferences);
            FileProcessor.saveUserBestScoresToFile(userBestScores);
            Gdx.app.exit();
        }
        exitButtonTouched = false;
        leftButtonTouched = false;
        rightButtonTouched = false;
        upButtonTouched = false;
        downButtonTouched = false;
        rotateButtonTouched = false;
        diffButtonTouched = false;
        return super.touchUp(touch, pointer);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (uiExitButton.isMe(touch)) {
            exitButtonTouched = true;
        }
        if (uiMusicOnButton.isMe(touch)) {
            if (game.isMusicEnabled()) {
                game.musicStop();
                game.setMusicEnabled(false);
            } else {
                game.musicPlay();
                game.setMusicEnabled(true);
            }
        }
        if (uiSoundOnButton.isMe(touch)) {
            gameField.setSoundsEnabled(!gameField.isSoundsEnabled());
        }
        if (uiPauseButton.isMe(touch) && !gameField.isGameOver()) {
            gameField.setPaused(!gameField.isPaused());
        }
        if (coinAcceptor[acceptorCurrentFrame].isMe(touch) && gameField.isGameOver() && !buttonMoveRight.isMe(touch) && !buttonRotate.isMe(touch)) {
            isDroppingCoin = true;
        }
        if (buttonMoveLeft.isMe(touch) && !buttonMoveUp.isMe(touch) && !buttonMoveDown.isMe(touch)) {
            leftButtonTouched = true;
            if (!gameField.isGameOver()) {
                gameField.areaTouched(Direction.LEFT);
            }
        }
        if (buttonMoveRight.isMe(touch) && !buttonMoveUp.isMe(touch) && !buttonMoveDown.isMe(touch)) {
            rightButtonTouched = true;
            if (!gameField.isGameOver()) {
                gameField.areaTouched(Direction.RIGHT);
            }
        }
        if (buttonMoveUp.isMe(touch) && !buttonMoveRight.isMe(touch) && !buttonMoveLeft.isMe(touch)) {
            upButtonTouched = true;
            if (!gameField.isGameOver() && gameField.isSnakeMode()) {
                gameField.areaTouched(Direction.UP);
            }
        }
        if (buttonMoveDown.isMe(touch) && !buttonMoveRight.isMe(touch) && !buttonMoveLeft.isMe(touch)) {
            downButtonTouched = true;
            if (!gameField.isGameOver()) {
                gameField.areaTouched(Direction.DOWN);
            }
        }
        if (buttonRotate.isMe(touch) && !buttonDiffLvl.isMe(touch)) {
            rotateButtonTouched = true;
            if (!gameField.isGameOver() && !gameField.isSnakeMode()) {
                gameField.areaTouched(Direction.UP);
            }
        }
        if (buttonDiffLvl.isMe(touch)) {
            diffButtonTouched = true;
            if (gameField.isGameOver() && !buttonRotate.isMe(touch)) {
                gameField.changeDiffLvl();
                showDiffLvl = true;
            }
        }
        return super.touchDown(touch, pointer);
    }
}
