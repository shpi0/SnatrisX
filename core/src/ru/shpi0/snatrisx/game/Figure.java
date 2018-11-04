package ru.shpi0.snatrisx.game;

import ru.shpi0.snatrisx.math.Rnd;

import static ru.shpi0.snatrisx.game.Direction.DOWN;

/**
 * Game figure class with figure moving logic
 */

public class Figure {

    public static final int MAX_FIG_SIZE = 4;

    private boolean isSnake;
    private boolean canMove;
    private boolean canTurn;
    private boolean canRotate;
    private boolean isLeftColumnEmpty;
    private boolean isTopRowEmpty;
    private int[][] figureMatrix5 = new int[5][5];
    private int[][] tmpFigureMatrix5 = new int[5][5];
    private int[][] figureMatrix4 = new int[4][4];
    private int[][] tmpFigureMatrix4 = new int[4][4];
    private int[][] figureMatrix3 = new int[3][3];
    private int[][] tmpFigureMatrix3 = new int[3][3];
    private int[] rotatexCoords = new int[MAX_FIG_SIZE];
    private int[] rotateyCoords = new int[MAX_FIG_SIZE];
    private int[] tempArr;
    private int topxCoord;
    private int topyCoord;
    private int temp;
    private int maxxCoord;
    private int maxyCoord;
    int figureMaxLength;
    protected int[] xCoords = new int[MAX_FIG_SIZE];
    protected int[] yCoords = new int[MAX_FIG_SIZE];
    private int[] nextYCoords = new int[MAX_FIG_SIZE];
    private int[] nextXCoords = new int[MAX_FIG_SIZE];
    private Direction direction;
    private BlockColor blockColor;
    private int initCoord;
    private int nextX, nextY;
    private int numOfRotationTries = 0;

    public Figure() {
        this((int) (Rnd.nextFloat(1f, 9f)));
    }

    public Figure(int initCoord) {
        newFigure(initCoord);
    }


    /**
     * Re-init figure parameters on game start or game restart
     *
     * @param initCoord
     */
    public void newFigure(int initCoord) {
        this.initCoord = initCoord;
        this.isSnake = true;
        this.canMove = true;
        this.direction = DOWN;
        this.blockColor = BlockColor.values()[(int) (Rnd.nextFloat(1f, 6f))];
        for (int i = 0; i < xCoords.length; i++) {
            xCoords[i] = initCoord;
        }
        for (int i = MAX_FIG_SIZE - 1; i >= 0; i--) {
            yCoords[i] = i - MAX_FIG_SIZE;
        }
    }

    /**
     * Re-init figure with random coordinates
     */
    public void newFigure() {
        newFigure((int) (Rnd.nextFloat(1f, 9f)));
    }

    protected boolean isFigureOut() {
        for (int i = 0; i < MAX_FIG_SIZE; i++) {
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
        for (int i = 0; i < MAX_FIG_SIZE; i++) {
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
        for (int i = 0; i < MAX_FIG_SIZE; i++) {
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
                    nextY = yCoords[MAX_FIG_SIZE - 1] + 1;
                    nextX = xCoords[MAX_FIG_SIZE - 1];
                    break;
                case UP:
                    nextY = yCoords[MAX_FIG_SIZE - 1] - 1;
                    nextX = xCoords[MAX_FIG_SIZE - 1];
                    break;
                case LEFT:
                    nextX = xCoords[MAX_FIG_SIZE - 1] - 1;
                    nextY = yCoords[MAX_FIG_SIZE - 1];
                    break;
                case RIGHT:
                    nextX = xCoords[MAX_FIG_SIZE - 1] + 1;
                    nextY = yCoords[MAX_FIG_SIZE - 1];
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
                    direction = DOWN;
                    GameField.getInstance().addScore(10);
                }
            }
            if (nextY < 0 || nextY >= GameField.MATRIX_HEIGHT || nextX < 0 || nextX >= GameField.MATRIX_WIDTH) {
                canMove = false;
                GameField.getInstance().setGameOver(true);
            } else {
                for (int i = 0; i < MAX_FIG_SIZE; i++) {
                    if (i < MAX_FIG_SIZE - 1) {
                        yCoords[i] = yCoords[i + 1];
                        xCoords[i] = xCoords[i + 1];
                    } else {
                        xCoords[i] = nextX;
                        yCoords[i] = nextY;
                    }
                }
            }
        } else {
            for (int i = 0; i < MAX_FIG_SIZE; i++) {
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
                for (int i = 0; i < MAX_FIG_SIZE; i++) {
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
                for (int i = 0; i < MAX_FIG_SIZE; i++) {
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

        // Получаем координаты для матрицы 4х4 или 3х3
        for (int i = 0; i < MAX_FIG_SIZE; i++) {
            rotatexCoords[i] = Math.abs(xCoords[i] - maxxCoord);
            rotateyCoords[i] = Math.abs(yCoords[i] - maxyCoord);
        }

        if (figureMaxLength == 5) {

            rotate(figureMatrix5, tmpFigureMatrix5, figureMaxLength);

        }

        if (figureMaxLength == 4) {

            rotate(figureMatrix4, tmpFigureMatrix4, figureMaxLength);

        }

        if (figureMaxLength == 3) {

            rotate(figureMatrix3, tmpFigureMatrix3, figureMaxLength);

        }

        // Проверяем, возможно ли перевернуть фигуру по новым координатам
        canRotate = canRotateWithNewCoords(nextYCoords, nextXCoords);

        // Если фигура находится у границы, пробуем ее сдвинуть и проверяем, можно ли ее повернуть
        numOfRotationTries = 0;
        if (!canRotate && minCoords(nextXCoords) <= 0 && canFigureMoveRight(nextXCoords, nextYCoords)) {
            while (!canRotate && numOfRotationTries < 3) {
                for (int i = 0; i < MAX_FIG_SIZE; i++) {
                    nextXCoords[i]++;
                }
                numOfRotationTries++;
                canRotate = canRotateWithNewCoords(nextYCoords, nextXCoords);
            }
        } else {
            if (!canRotate && maxCoords(nextXCoords) > GameField.MATRIX_WIDTH - 1 && canFigureMoveLeft(nextXCoords, nextYCoords)) {
                while (!canRotate && numOfRotationTries < 3) {
                    for (int i = 0; i < MAX_FIG_SIZE; i++) {
                        nextXCoords[i]--;
                    }
                    numOfRotationTries++;
                    canRotate = canRotateWithNewCoords(nextYCoords, nextXCoords);
                }
            }
        }

        // Если возможно, делаем переворот фигуры
        if (canRotate) {
            for (int i = 0; i < MAX_FIG_SIZE; i++) {
                xCoords[i] = nextXCoords[i];
                yCoords[i] = nextYCoords[i];
            }
        }

    }

    private boolean canRotateWithNewCoords(int[] newYCoords, int[] newXCoords) {
        boolean result = true;
        for (int i = 0; i < MAX_FIG_SIZE; i++) {
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
            for (int i = 0; i < MAX_FIG_SIZE; i++) {
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
            for (int i = 0; i < MAX_FIG_SIZE; i++) {
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
            for (int i = 0; i < MAX_FIG_SIZE; i++) {
                if (GameField.getInstance().gameMatrix[yCoords[i]][nextXCoords[i]] != -1) {
                    canTurn = false;
                }
            }
            if (canTurn) {
                for (int i = 0; i < MAX_FIG_SIZE; i++) {
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

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
