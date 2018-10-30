package ru.shpi0.snatrisx.game;

import ru.shpi0.snatrisx.math.Rnd;

/**
 * Game field has a matrix with int values:
 * -1 value: square on game field is empty
 * 0..5 values: coloured square on game field
 * 99 value: snake target on game field
 */

public class GameField {

    public static final int MATRIX_WIDTH = 10;
    public static final int MATRIX_HEIGHT = 20;
    public int[][] gameMatrix = new int[MATRIX_HEIGHT][MATRIX_WIDTH];
    private Figure figure = new Figure();
    private boolean isGameOver = false;
    private long score = 0;
    private int newscore;
    private float speedModificator = 1f;
    private float speed = 1f;
    private boolean fieldHaveTarget;

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
        if (!isGameOver) {
            if (figure.isCanMove()) {
                disposeFigure();
            } else {
                speedModificator = 1f;
                generateNewFigure();
                fieldHaveTarget = false;
            }
            if (figure.isSnake() && figure.isFigureOut() && !fieldHaveTarget) {
                putTarget();
            }
            figure.move();
            putFigure();
            if (!figure.isCanMove()) {
                checkLinesOnField();
            }
        }
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
     * @param value
     */
    protected void addScore(long value) {
        score += value;
    }

    /**
     * Check the horizontal lines on game field, and if line is filled with squares - empty it
     */
    private void checkLinesOnField() {
        boolean hasSomeFilledLines = true;
        while (hasSomeFilledLines) {
            hasSomeFilledLines = false;
            for (int i = 0; i < MATRIX_HEIGHT; i++) {
                boolean lineIsFilled = true;
                for (int j = 0; j < MATRIX_WIDTH; j++) {
                    if (gameMatrix[i][j] < 0 || gameMatrix[i][j] > 5) {
                        lineIsFilled = false;
                    }
                }
                if (lineIsFilled) {
                    hasSomeFilledLines = true;
                    newscore = 0;
                    for (int j = 0; j < MATRIX_WIDTH; j++) {
                        newscore += gameMatrix[i][j];
                    }
                    addScore(newscore);
                    for (int j = i; j > 0; j--) {
                        for (int k = 0; k < MATRIX_WIDTH; k++) {
                            gameMatrix[j][k] = gameMatrix[j - 1][k];
                        }
                    }
                    for (int j = 0; j < MATRIX_WIDTH; j++) {
                        gameMatrix[0][j] = -1;
                    }
                    if (speed > 0.75f) {
                        speed -= newscore / 1000f;
                    } else
                    if (speed > 0.5f) {
                        speed -= newscore / 2500f;
                    } else
                    if (speed > 0.3f) {
                        speed -= newscore / 5000f;
                    }
                }
            }
        }
    }

    /**
     * Place the snake tarhet to game field
     */
    private void putTarget() {
        int xPos = (int) (Rnd.nextFloat(0f, 1f) * 10);
        int yPos = (int) (Rnd.nextFloat(0f, 2f));
        gameMatrix[yPos][xPos] = 99;
        fieldHaveTarget = true;
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
        isGameOver = gameOver;
    }
}
