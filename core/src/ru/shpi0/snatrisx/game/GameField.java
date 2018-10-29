package ru.shpi0.snatrisx.game;

import ru.shpi0.snatrisx.math.Rnd;

public class GameField {

    public static final int MATRIX_WIDTH = 10;
    public static final int MATRIX_HEIGHT = 20;
    public int[][] gameMatrix = new int[MATRIX_HEIGHT][MATRIX_WIDTH];
    /**
     *  -1   - empty
     *  0..5 - square
     *  99   - target
     */
    private Figure figure = new Figure();
    private boolean isGameOver = false;
    private float speedModificator = 1f;

    private static final GameField ourInstance = new GameField();

    public static GameField getInstance() {
        return ourInstance;
    }

    private GameField() {
        fillGameMatrix();
        putTarget();
    }

    public void update() {
        if (!isGameOver) {
            if (figure.isCanMove()) {
                disposeFigure();
            } else {
                speedModificator = 1f;
                generateNewFigure();
                putTarget();
            }
            figure.move();
            putFigure();
            if (!figure.isCanMove()) {
                checkLinesOnField();
            }
        }
    }

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
                    for (int j = i; j > 0; j--) {
                        for (int k = 0; k < MATRIX_WIDTH; k++) {
                            gameMatrix[j][k] = gameMatrix[j - 1][k];
                        }
                    }
                    for (int j = 0; j < MATRIX_WIDTH; j++) {
                        gameMatrix[0][j] = -1;
                    }
                }
            }
        }
    }

    private void putTarget() {
        int xPos = (int) (Rnd.nextFloat(0f, 1f) * 10);
        int yPos = (int) (Rnd.nextFloat(3f, 4f));
        gameMatrix[yPos][xPos] = 99;
    }

    private void disposeFigure() {
        for (int i = 0; i < Figure.FIG_SIZE; i++) {
            if (figure.yCoords[i] >= 0 && figure.yCoords[i] < MATRIX_HEIGHT && figure.xCoords[i] >= 0 && figure.xCoords[i] < MATRIX_WIDTH) {
                gameMatrix[figure.yCoords[i]][figure.xCoords[i]] = -1;
            }
        }
    }

    private void putFigure() {
        for (int i = 0; i < Figure.FIG_SIZE; i++) {
            if (figure.yCoords[i] >= 0 && figure.yCoords[i] < MATRIX_HEIGHT && figure.xCoords[i] >= 0 && figure.xCoords[i] < MATRIX_WIDTH) {
                gameMatrix[figure.yCoords[i]][figure.xCoords[i]] = figure.getBlockColor().getValue();
            }
        }
    }

    private void generateNewFigure() {
        figure.newFigure();
    }

    private void fillGameMatrix() {
        for (int i = 0; i < MATRIX_HEIGHT; i++) {
            for (int j = 0; j < MATRIX_WIDTH; j++) {
                gameMatrix[i][j] = -1;
            }
        }
    }

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
