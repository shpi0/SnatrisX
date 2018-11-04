package ru.shpi0.snatrisx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayDeque;
import java.util.Deque;

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

    public static final int MATRIX_WIDTH = 10;
    public static final int MATRIX_HEIGHT = 20;
    public int[][] gameMatrix = new int[MATRIX_HEIGHT][MATRIX_WIDTH];
    private Figure figure = new Figure();
    private boolean isGameOver = false;
    private long score = 0;
    private int newScore;
    private float speedModificator = 1f;
    private float speed = 1f;
    private boolean isPaused = false;
    private boolean isSoundsEnabled = true;

    private static final GameField ourInstance = new GameField();

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

    /**
     * Restart the game
     */
    public void newGame() {
        score = 0;
        fillGameMatrix();
        isGameOver = false;
        figure.newFigure();
    }

    /**
     * Add game score
     *
     * @param value
     */
    protected void addScore(long value) {
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
                        newScore += gameMatrix[i][j];
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
        for (int i = 0; i < Figure.FIG_SIZE; i++) {
            if (figure.yCoords[i] >= 0 && figure.yCoords[i] < MATRIX_HEIGHT && figure.xCoords[i] >= 0 && figure.xCoords[i] < MATRIX_WIDTH) {
                gameMatrix[figure.yCoords[i]][figure.xCoords[i]] = -1;
            }
        }
    }

    /**
     * Put figure to it's coordinates on game field
     */
    private void putFigure() {
        for (int i = 0; i < Figure.FIG_SIZE; i++) {
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
                    if (figure.getDirection() != Direction.DOWN) {
                        figure.setDirection(direction);
                    }
                    break;
                case DOWN:
                    if (figure.getDirection() != Direction.UP) {
                        figure.setDirection(direction);
                    }
                    break;
                case RIGHT:
                    if (figure.getDirection() != Direction.LEFT) {
                        figure.setDirection(direction);
                    }
                    break;
                case LEFT:
                    if (figure.getDirection() != Direction.RIGHT) {
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

    public long getScore() {
        return score;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public float getSpeedModificator() {
        return speedModificator;
    }

    public void setGameOver(boolean gameOver) {
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
        return isSoundsEnabled;
    }

    public void setSoundsEnabled(boolean soundsEnabled) {
        isSoundsEnabled = soundsEnabled;
    }

    protected void playEatSound() {
        if (isSoundsEnabled) {
            soundEat.play(1.0f);
        }
    }

    protected void playPutSound() {
        if (isSoundsEnabled) {
            soundPut.play(1.0f);
        }
    }

    protected void playCrashSound() {
        if (isSoundsEnabled) {
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
}
