package ru.shpi0.snatrisx.game;

import ru.shpi0.snatrisx.math.Rnd;


/**
 * Game figure class with figure moving logic
 */

public class Figure {

    private GameField gameField;

    public static final int MIN_FIG_SIZE = 4;
    public static final int MAX_FIG_SIZE = 5;

    private int figureSize;

    private boolean isSnake;
    private boolean canMove;
    private boolean canTurn;
    private boolean canRotate;
    private boolean isLeftColumnEmpty;
    private boolean isTopRowEmpty;
    private int[] rotatexCoords;
    private int[] rotateyCoords;
    private int[] tempArr;
    private int topxCoord;
    private int topyCoord;
    private int temp;
    private int maxxCoord;
    private int maxyCoord;
    int figureMaxLength;
    protected int[] xCoords;
    protected int[] yCoords;
    private int[] nextYCoords;
    private int[] nextXCoords;
    private Direction direction;
    private BlockColor blockColor;
    private int initCoord;
    private int nextX, nextY;
    private int numOfRotationTries = 0;

    public Figure() {
        this((int) (Rnd.nextFloat(1f, 9f)));
    }

    public Figure(int initCoord) {
        this.gameField = GameField.getInstance();
        newFigure(initCoord);
    }


    /**
     * Re-init figure parameters on game start or game restart
     *
     * @param initCoord
     */
    public void newFigure(int initCoord) {
        this.initCoord = initCoord;
        this.figureSize = gameField.getDiffLvl() == DiffLvl.HARD ? generateFigureLength() : 4;
        this.rotatexCoords = new int[this.figureSize];
        this.rotateyCoords = new int[this.figureSize];
        this.xCoords = new int[this.figureSize];
        this.yCoords = new int[this.figureSize];
        this.nextYCoords = new int[this.figureSize];
        this.nextXCoords = new int[this.figureSize];
        this.isSnake = true;
        this.canMove = true;
        this.direction = Direction.DOWN;
        this.blockColor = BlockColor.values()[(int) (Rnd.nextFloat(0f, 5f))];
        for (int i = 0; i < this.xCoords.length; i++) {
            this.xCoords[i] = initCoord;
        }
        for (int i = figureSize - 1; i >= 0; i--) {
            this.yCoords[i] = i - figureSize;
        }
    }

    /**
     * Generate figure length with chance of 25% to generate 5-square figure
     * @return
     */
    private int generateFigureLength() {
        if (Rnd.nextFloat(0f, 99f) > 75f) {
            return MAX_FIG_SIZE;
        }
        return MIN_FIG_SIZE;
    }

    /**
     * Re-init figure with random coordinates
     */
    public void newFigure() {
        newFigure((int) (Rnd.nextFloat(1f, 9f)));
    }

    protected boolean isFigureOut() {
        for (int i = 0; i < figureSize; i++) {
            if (yCoords[i] < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Find minimum value in array
     *
     * @param coords
     * @return
     */
    private int minCoords(int[] coords) {
        int result = 0;
        if (coords.length > 0) {
            result = coords[0];
            for (int i = 0; i < coords.length; i++) {
                if (coords[i] < result) {
                    result = coords[i];
                }
            }
        } else {
            throw new RuntimeException("Incorrect array length");
        }
        return result;
    }

    /**
     * Find maximum value in array
     *
     * @param coords
     * @return
     */
    private int maxCoords(int[] coords) {
        int result = 0;
        if (coords.length > 0) {
            result = coords[0];
            for (int i = 0; i < coords.length; i++) {
                if (coords[i] > result) {
                    result = coords[i];
                }
            }
        } else {
            throw new RuntimeException("Incorrect array length");
        }
        return result;
    }

    /**
     * Get minimum X (left side) coordinate of figure
     *
     * @return
     */
    private int minCoordX() {
        return minCoords(xCoords);
    }

    /**
     * Get maximum X (right side) coordinate of figure
     *
     * @return
     */
    private int maxCoordX() {
        return maxCoords(xCoords);
    }

    /**
     * Get minimum Y (top side) coordinate of figure
     *
     * @return
     */
    private int minCoordY() {
        return minCoords(yCoords);
    }

    /**
     * Get maximum Y (bottom side) coordinate of figure
     *
     * @return
     */
    private int maxCoordY() {
        return maxCoords(yCoords);
    }

    /**
     * Check is figure can move left to 1 square
     *
     * @return
     */
    private boolean canFigureMoveLeft(int xCoord[], int yCoord[]) {
        if (minCoords(xCoord) == 0) {
            return false;
        }
        for (int i = 0; i < figureSize; i++) {
            if (xCoord[i] < GameField.MATRIX_WIDTH) {
                if (xCoord[i] == 0) {
                    return false;
                }
                if (GameField.getInstance().gameMatrix[yCoord[i]][xCoord[i] - 1] != -1) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Check is figure can move right to 1 square
     *
     * @return
     */
    private boolean canFigureMoveRight(int xCoord[], int yCoord[]) {
        if (maxCoords(xCoord) >= GameField.MATRIX_WIDTH - 1) {
            return false;
        }
        for (int i = 0; i < figureSize; i++) {
            if (xCoord[i] == GameField.MATRIX_WIDTH - 1) {
                return false;
            }
            if (GameField.getInstance().gameMatrix[yCoord[i]][xCoord[i] + 1] != -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method moves a figure
     */
    public void move() {
        if (isSnake && canMove) {
            switch (direction) {
                case DOWN:
                    nextY = yCoords[figureSize - 1] + 1;
                    nextX = xCoords[figureSize - 1];
                    break;
                case UP:
                    nextY = yCoords[figureSize - 1] - 1;
                    nextX = xCoords[figureSize - 1];
                    break;
                case LEFT:
                    nextX = xCoords[figureSize - 1] - 1;
                    nextY = yCoords[figureSize - 1];
                    break;
                case RIGHT:
                    nextX = xCoords[figureSize - 1] + 1;
                    nextY = yCoords[figureSize - 1];
                    break;
            }
            if (nextX >= 0 && nextY >= 0 && nextX < GameField.MATRIX_WIDTH && nextY < GameField.MATRIX_HEIGHT) {
                if (GameField.getInstance().gameMatrix[nextY][nextX] >= 0 && GameField.getInstance().gameMatrix[nextY][nextX] <= 5) {
                    canMove = false;
                    GameField.getInstance().setGameOver(true);
                }
                if (GameField.getInstance().gameMatrix[nextY][nextX] == 99) {
                    GameField.getInstance().playEatSound();
                    isSnake = false;
                    direction = Direction.DOWN;
                    GameField.getInstance().addScore(10);
                }
            }
            if (nextY < 0 || nextY >= GameField.MATRIX_HEIGHT || nextX < 0 || nextX >= GameField.MATRIX_WIDTH) {
                canMove = false;
                GameField.getInstance().setGameOver(true);
            } else {
                for (int i = 0; i < figureSize; i++) {
                    if (i < figureSize - 1) {
                        yCoords[i] = yCoords[i + 1];
                        xCoords[i] = xCoords[i + 1];
                    } else {
                        xCoords[i] = nextX;
                        yCoords[i] = nextY;
                    }
                }
            }
        } else {
            for (int i = 0; i < figureSize; i++) {
                nextYCoords[i] = yCoords[i] + 1;
                if (nextYCoords[i] >= GameField.MATRIX_HEIGHT) {
                    canMove = false;
                    GameField.getInstance().playPutSound();
                } else {
                    try {
                        if (GameField.getInstance().gameMatrix[nextYCoords[i]][xCoords[i]] != -1) {
                            canMove = false;
                            GameField.getInstance().playPutSound();
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        if (!isSnake && !canMove) {
                            GameField.getInstance().setGameOver(true);
                        }
                    }
                }
            }
            if (canMove) {
                for (int i = 0; i < figureSize; i++) {
                    yCoords[i]++;
                }
            }
        }
    }

    /**
     * Rotate figure using temporary matrix
     * @param matrix
     * @param tmpMatrix
     * @param figLength
     */
    private void rotate(int[][] matrix, int[][] tmpMatrix, int figLength) {
        // Заполняем матрицу
        for (int j = 0; j < figLength; j++) {
            for (int l = 0; l < figLength; l++) {
                matrix[l][j] = -1;
                for (int i = 0; i < figureSize; i++) {
                    if (rotatexCoords[i] == j && rotateyCoords[i] == l) {
                        matrix[rotateyCoords[i]][rotatexCoords[i]] = getBlockColor().getValue();
                    }
                }
            }
        }

        // Переворачиваем матрицу на 90 град
        for (int i = 0; i < figLength; i++) {
            for (int j = 0; j < figLength; j++) {
                tmpMatrix[j][figLength - 1 - i] = matrix[i][j];
            }
        }

        // Сдвигаем фигуру в левый верхний угол матрицы, чтобы не скакала по экрану
        isLeftColumnEmpty = true;
        while (isLeftColumnEmpty) {
            for (int i = 0; i < figLength; i++) {
                if (tmpMatrix[i][0] != -1) {
                    isLeftColumnEmpty = false;
                }
            }
            if (isLeftColumnEmpty) {
                for (int i = 0; i < figLength; i++) {
                    for (int j = 0; j < figLength; j++) {
                        if (j == figLength - 1) {
                            tmpMatrix[i][j] = -1;
                        } else {
                            tmpMatrix[i][j] = tmpMatrix[i][j + 1];
                        }
                    }
                }
            }
        }
        isTopRowEmpty = true;
        while (isTopRowEmpty) {
            for (int i = 0; i < figLength; i++) {
                if (tmpMatrix[0][i] != -1) {
                    isTopRowEmpty = false;
                }
            }
            if (isTopRowEmpty) {
                tempArr = tmpMatrix[0];
                for (int i = 0; i < figLength - 1; i++) {
                    tmpMatrix[i] = tmpMatrix[i + 1];
                }
                tmpMatrix[figLength - 1] = tempArr;
            }
        }

        // Проецируем координаты фигуры обратно из матрицы в основную игровую матрицу
        temp = 0;
        for (int i = 0; i < figLength; i++) {
            for (int j = 0; j < figLength; j++) {
                if (tmpMatrix[i][j] != -1) {
                    nextXCoords[temp] = j + topxCoord;
                    nextYCoords[temp] = i + topyCoord;
                    temp++;
                }
            }
        }
    }

    /**
     * Method rotates a figure
     */
    public void rotate() {
        figureMaxLength = Math.max(Math.abs(maxCoordX() - minCoordX()), Math.abs(maxCoordY() - minCoordY())) + 1;

        if (!(figureMaxLength == 5 || figureMaxLength == 4 || figureMaxLength == 3 || canMove)) {
            return;
        }

        topxCoord = minCoordX();
        topyCoord = minCoordY();
        maxxCoord = maxCoordX();
        maxyCoord = maxCoordY();

        // Получаем координаты для матрицы
        for (int i = 0; i < figureSize; i++) {
            rotatexCoords[i] = Math.abs(xCoords[i] - maxxCoord);
            rotateyCoords[i] = Math.abs(yCoords[i] - maxyCoord);
        }

        //Делаем разворот
        rotate(new int[figureMaxLength][figureMaxLength], new int[figureMaxLength][figureMaxLength], figureMaxLength);

        // Проверяем, возможно ли перевернуть фигуру по новым координатам
        canRotate = canRotateWithNewCoords(nextYCoords, nextXCoords);

        // Если фигура находится у границы, пробуем ее сдвинуть и проверяем, можно ли ее повернуть
        numOfRotationTries = 0;
        if (!canRotate && minCoords(nextXCoords) <= 0 && canFigureMoveRight(nextXCoords, nextYCoords)) {
            while (!canRotate && numOfRotationTries < 3) {
                for (int i = 0; i < figureSize; i++) {
                    nextXCoords[i]++;
                }
                numOfRotationTries++;
                canRotate = canRotateWithNewCoords(nextYCoords, nextXCoords);
            }
        } else {
            if (!canRotate && maxCoords(nextXCoords) > GameField.MATRIX_WIDTH - 1 && canFigureMoveLeft(nextXCoords, nextYCoords)) {
                while (!canRotate && numOfRotationTries < 3) {
                    for (int i = 0; i < figureSize; i++) {
                        nextXCoords[i]--;
                    }
                    numOfRotationTries++;
                    canRotate = canRotateWithNewCoords(nextYCoords, nextXCoords);
                }
            }
        }

        // Если возможно, делаем переворот фигуры
        if (canRotate) {
            for (int i = 0; i < figureSize; i++) {
                xCoords[i] = nextXCoords[i];
                yCoords[i] = nextYCoords[i];
            }
        }

    }

    private boolean canRotateWithNewCoords(int[] newYCoords, int[] newXCoords) {
        boolean result = true;
        for (int i = 0; i < figureSize; i++) {
            if (newYCoords[i] > 0 &&
                    newYCoords[i] < GameField.MATRIX_HEIGHT &&
                    newXCoords[i] > 0 &&
                    newXCoords[i] < GameField.MATRIX_WIDTH) {
                if (GameField.getInstance().gameMatrix[newYCoords[i]][newXCoords[i]] != -1) {
                    result = false;
                }
            } else {
                result = false;
            }
        }
        return result;
    }

    /**
     * Move figure left on 1 square
     */
    public void moveLeft() {
        if (minCoordX() > 0) {
            for (int i = 0; i < figureSize; i++) {
                nextXCoords[i] = xCoords[i] - 1;
            }
        }
        tryTurnLeftOrRight();
    }

    /**
     * Move figure right on 1 square
     */
    public void moveRight() {
        if (maxCoordX() < GameField.MATRIX_WIDTH - 1) {
            for (int i = 0; i < figureSize; i++) {
                nextXCoords[i] = xCoords[i] + 1;
            }
            tryTurnLeftOrRight();
        }
    }

    /**
     * Check if figure can move and do it
     */
    private void tryTurnLeftOrRight() {
        if (isFigureOut()) {
            canTurn = true;
            for (int i = 0; i < figureSize; i++) {
                if (GameField.getInstance().gameMatrix[yCoords[i]][nextXCoords[i]] != -1) {
                    canTurn = false;
                }
            }
            if (canTurn) {
                for (int i = 0; i < figureSize; i++) {
                    xCoords[i] = nextXCoords[i];
                }
            }
        }
    }

    /**
     * Is figure in snake or tetris game mode
     *
     * @return
     */
    public boolean isSnake() {
        return isSnake;
    }

    /**
     * Is figure can moving
     *
     * @return
     */
    public boolean isCanMove() {
        return canMove;
    }

    public int[] getxCoords() {
        return xCoords;
    }

    public int[] getyCoords() {
        return yCoords;
    }

    public Direction getDirection() {
        return direction;
    }

    public BlockColor getBlockColor() {
        return blockColor;
    }

    public int getFigureSize() {
        return figureSize;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
