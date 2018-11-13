package ru.shpi0.snatrisx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayDeque;
import java.util.Deque;

import ru.shpi0.snatrisx.base.FileProcessor;
import ru.shpi0.snatrisx.base.GamePreferences;
import ru.shpi0.snatrisx.base.UserBestScores;
import ru.shpi0.snatrisx.math.Rnd;

/**
 * Game field has a matrix with int values:
 * -1 value: square on game field is empty
 * 0..5 values: coloured square on game field
 * 99 value: snake target on game field
 */

public class GameField {

    private Deque<Integer> linesToDrop = new ArrayDeque<Integer>();
    private boolean hasLinesToDrop = false;

    private Sound soundCrash = Gdx.audio.newSound(Gdx.files.internal("sounds/crash.mp3"));
    private Sound soundEat = Gdx.audio.newSound(Gdx.files.internal("sounds/eat.mp3"));
    private Sound soundPut = Gdx.audio.newSound(Gdx.files.internal("sounds/put.mp3"));

    public static final int MATRIX_WIDTH = 11;
    public static final int MATRIX_HEIGHT = 16;
    public int[][] gameMatrix = new int[MATRIX_HEIGHT][MATRIX_WIDTH];
    private Figure figure;
    private boolean isGameOver = true;
    private int score = 0;
    private int newScore;
    private float speedModificator = 1f;
    private float speed = 1f;
    private float difficultModificator = 1f;
    private boolean isPaused = false;
    private Direction lastDirection;

    private DiffLvl diffLvl;

    private static final GameField ourInstance = new GameField();
    private GamePreferences gamePreferences;
    private UserBestScores userBestScores;

    public static GameField getInstance() {
        return ourInstance;
    }

    private GameField() {
        fillGameMatrix();
    }

    /**
     * Update game field when figure is moving
     */
    public void update() {
        if (figure == null) {
            newGame();
            isGameOver = true;
        }
        if (!isGameOver && !isPaused && !hasLinesToDrop) {
            if (figure.isCanMove()) {
                disposeFigure();
            } else {
                speedModificator = 1f;
                generateNewFigure();
            }
            if (figure.isSnake() && figure.isFigureOut() && !hasFieldTarget()) {
                putTarget();
            }
            figure.move();
            putFigure();
            if (!figure.isCanMove()) {
                checkLinesOnField();
            }
        }
        lastDirection = figure.getDirection();
    }

    private boolean hasFieldTarget() {
        for (int i = 0; i < MATRIX_HEIGHT; i++) {
            for (int j = 0; j < MATRIX_WIDTH; j++) {
                if (gameMatrix[i][j] == 99) {
                    return true;
                }
            }
        }
        return false;
    }

    public void changeDiffLvl() {
        switch (diffLvl) {
            case EASY:
                setDifficultModificator(0.75f);
                diffLvl = DiffLvl.NORM;
                break;
            case NORM:
                setDifficultModificator(0.65f);
                diffLvl = DiffLvl.HARD;
                break;
            case HARD:
                setDifficultModificator(1f);
                diffLvl = DiffLvl.EASY;
                break;
        }
    }

    /**
     * Restart the game
     */
    public void newGame() {
        if (diffLvl == null) {
            diffLvl = DiffLvl.EASY;
        }
        score = 0;
        fillGameMatrix();
        isGameOver = false;
        if (figure == null) {
            figure = new Figure();
        } else {
            figure.newFigure();
        }
    }

    /**
     * Add game score
     *
     * @param value
     */
    protected void addScore(int value) {
        score += value;
    }

    /**
     * Check the horizontal lines on game field, and if line is filled with squares - empty it
     */
    private void checkLinesOnField() {
            // Проверяем игровое поле, каждую горизонтальную линию, начиная снизу
            for (int i = MATRIX_HEIGHT - 1; i >= 0; i--) {

                // Определяем, заполнена ли текущая линия
                boolean lineIsFilled = true;
                for (int j = 0; j < MATRIX_WIDTH; j++) {
                    if (gameMatrix[i][j] < 0 || gameMatrix[i][j] > 5) {
                        lineIsFilled = false;
                    }
                }

                // Если текущая линия заполнена, то:
                if (lineIsFilled) {
                    // 1. Добавляем пользователю очки за каждый квадратик в поле
                    newScore = 0;
                    for (int j = 0; j < MATRIX_WIDTH; j++) {
                        newScore += gameMatrix[i][j] + 1;
                    }
                    addScore(newScore);

                    // 2. Увеличиваем игровую скорость
                    if (speed > 0.75f) {
                        speed -= newScore / 1000f;
                    } else if (speed > 0.5f) {
                        speed -= newScore / 2500f;
                    } else if (speed > 0.3f) {
                        speed -= newScore / 5000f;
                    }

                    // 3. Добавляем текущую линию в очередь на удаление и прерываем проверку
                    putLineToQueue(i);
                    hasLinesToDrop = true;
                    break;
                }
                hasLinesToDrop = false;
            }
    }

    /**
     * Drop all lines to empty line and check field again
     * @param line
     */
    public void dropLinesDown(int line) {
        for (int j = line; j > 0; j--) {
            for (int k = 0; k < MATRIX_WIDTH; k++) {
                gameMatrix[j][k] = gameMatrix[j - 1][k];
            }
        }
        for (int j = 0; j < MATRIX_WIDTH; j++) {
            gameMatrix[0][j] = -1;
        }
        checkLinesOnField();
    }

    /**
     * Put index of line which should be destroyed to queue
     * @param lineIdx
     */
    public void putLineToQueue(int lineIdx) {
        linesToDrop.offer(lineIdx);
        hasLinesToDrop = true;
    }

    public Integer pickLineFromQueue() {
        return linesToDrop.poll();
    }

    /**
     * Place the snake target to game field
     */
    private void putTarget() {
        int xPos = (int) (Rnd.nextFloat(0f, 9f));
        int yPos = (int) (Rnd.nextFloat(0f, 4f));
        gameMatrix[yPos][xPos] = 99;
    }

    /**
     * Remove figure from it's coordinates on game field
     */
    private void disposeFigure() {
        for (int i = 0; i < figure.getFigureSize(); i++) {
            if (figure.yCoords[i] >= 0 && figure.yCoords[i] < MATRIX_HEIGHT && figure.xCoords[i] >= 0 && figure.xCoords[i] < MATRIX_WIDTH) {
                gameMatrix[figure.yCoords[i]][figure.xCoords[i]] = -1;
            }
        }
    }

    /**
     * Put figure to it's coordinates on game field
     */
    private void putFigure() {
        for (int i = 0; i < figure.getFigureSize(); i++) {
            if (figure.yCoords[i] >= 0 && figure.yCoords[i] < MATRIX_HEIGHT && figure.xCoords[i] >= 0 && figure.xCoords[i] < MATRIX_WIDTH) {
                gameMatrix[figure.yCoords[i]][figure.xCoords[i]] = figure.getBlockColor().getValue();
            }
        }
    }

    /**
     * Generates new figure
     */
    private void generateNewFigure() {
        figure.newFigure();
    }

    /**
     * Fill empty game matrix
     */
    private void fillGameMatrix() {
        for (int i = 0; i < MATRIX_HEIGHT; i++) {
            for (int j = 0; j < MATRIX_WIDTH; j++) {
                gameMatrix[i][j] = -1;
            }
        }
    }

    /**
     * Functionality for up, left, right and down areas on game screen
     *
     * @param direction
     */
    public void areaTouched(Direction direction) {
        if (figure.isSnake()) {
            switch (direction) {
                case UP:
                    if (lastDirection != Direction.DOWN) {
                        figure.setDirection(direction);
                    }
                    break;
                case DOWN:
                    if (lastDirection != Direction.UP) {
                        figure.setDirection(direction);
                    }
                    break;
                case RIGHT:
                    if (lastDirection != Direction.LEFT) {
                        figure.setDirection(direction);
                    }
                    break;
                case LEFT:
                    if (lastDirection != Direction.RIGHT) {
                        figure.setDirection(direction);
                    }
                    break;
            }
        } else {
            switch (direction) {
                case LEFT:
                    disposeFigure();
                    figure.moveLeft();
                    putFigure();
                    break;
                case RIGHT:
                    disposeFigure();
                    figure.moveRight();
                    putFigure();
                    break;
                case DOWN:
                    speedModificator = 0.1f;
                    break;
                case UP:
                    disposeFigure();
                    figure.rotate();
                    putFigure();
                    break;
            }
        }
    }

    public float getSpeed() {
        return speed;
    }

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public float getSpeedModificator() {
        return speedModificator;
    }

    public float getDifficultModificator() {
        return difficultModificator;
    }

    public void setDifficultModificator(float difficultModificator) {
        this.difficultModificator = difficultModificator;
    }

    public void setGameOver(boolean gameOver) {
        if (score > userBestScores.getLastBestScore()) {
            userBestScores.setLastBestScore(score);
        }
        playCrashSound();
        isGameOver = gameOver;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public boolean isSoundsEnabled() {
        return gamePreferences.isSoundsOn();
    }

    public void setSoundsEnabled(boolean soundsEnabled) {
        gamePreferences.setSoundsOn(soundsEnabled);
        FileProcessor.saveGamePreferencesToFile(gamePreferences);
    }

    protected void playEatSound() {
        if (gamePreferences.isSoundsOn()) {
            soundEat.play(1.0f);
        }
    }

    protected void playPutSound() {
        if (gamePreferences.isSoundsOn()) {
            soundPut.play(1.0f);
        }
    }

    protected void playCrashSound() {
        if (gamePreferences.isSoundsOn()) {
            soundCrash.play(1.0f);
        }
    }

    public void dispose() {
        soundPut.dispose();
        soundEat.dispose();
        soundCrash.dispose();
    }

    public void setHasLinesToDrop(boolean hasLinesToDrop) {
        this.hasLinesToDrop = hasLinesToDrop;
    }

    public boolean isSnakeMode() {
        return figure.isSnake();
    }

    public DiffLvl getDiffLvl() {
        return diffLvl;
    }

    public void setGamePreferences(GamePreferences gamePreferences) {
        this.gamePreferences = gamePreferences;
    }

    public void setUserBestScores(UserBestScores userBestScores) {
        this.userBestScores = userBestScores;
    }
}
