package ru.shpi0.snatrisx.game;

import ru.shpi0.snatrisx.math.Rnd;

import static ru.shpi0.snatrisx.game.Direction.DOWN;

/**
 * Game figure class with figure moving logic
 */

public class Figure {

    public static final int FIG_SIZE = 4;

    private boolean isSnake;
    private boolean canMove;
    private boolean canTurn;
    private boolean canRotate;
    private boolean isLeftColumnEmpty;
    private boolean isTopRowEmpty;
    private int[][] figureMatrix4 = new int[4][4];
    private int[][] tmpFigureMatrix4 = new int[4][4];
    private int[][] figureMatrix3 = new int[3][3];
    private int[][] tmpFigureMatrix3 = new int[3][3];
    private int[] rotatexCoords = new int[FIG_SIZE];
    private int[] rotateyCoords = new int[FIG_SIZE];
    private int[] tempArr;
    private int topxCoord;
    private int topyCoord;
    private int temp;
    private int maxxCoord;
    private int maxyCoord;
    int figureMaxLenght;
    protected int[] xCoords = new int[FIG_SIZE];
    protected int[] yCoords = new int[FIG_SIZE];
    private int[] nextYCoords = new int[FIG_SIZE];
    private int[] nextXCoords = new int[FIG_SIZE];
    private Direction direction;
    private BlockColor blockColor;
    private int initCoord;
    private int nextX, nextY;

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
        yCoords[3] = -1;
        yCoords[2] = -2;
        yCoords[1] = -3;
        yCoords[0] = -4;
    }

    /**
     * Re-init figure with random coordinates
     */
    public void newFigure() {
        newFigure((int) (Rnd.nextFloat(1f, 9f)));
    }

    protected boolean isFigureOut() {
        for (int i = 0; i < FIG_SIZE; i++) {
            if (yCoords[i] < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get minimum X (left side) coordinate of figure
     *
     * @return
     */
    private int minCoordX() {
        int result = xCoords[0];
        for (int i = 0; i < xCoords.length; i++) {
            if (xCoords[i] < result) {
                result = xCoords[i];
            }
        }
        return result;
    }

    /**
     * Get maximum X (right side) coordinate of figure
     *
     * @return
     */
    private int maxCoordX() {
        int result = xCoords[0];
        for (int i = 0; i < xCoords.length; i++) {
            if (xCoords[i] > result) {
                result = xCoords[i];
            }
        }
        return result;
    }

    /**
     * Get minimum Y (top side) coordinate of figure
     *
     * @return
     */
    private int minCoordY() {
        int result = yCoords[0];
        for (int i = 0; i < yCoords.length; i++) {
            if (yCoords[i] < result) {
                result = yCoords[i];
            }
        }
        return result;
    }

    /**
     * Get maximum Y (bottom side) coordinate of figure
     *
     * @return
     */
    private int maxCoordY() {
        int result = yCoords[0];
        for (int i = 0; i < yCoords.length; i++) {
            if (yCoords[i] > result) {
                result = yCoords[i];
            }
        }
        return result;
    }

    /**
     * Method moves a figure
     */
    public void move() {
        if (isSnake && canMove) {
            switch (direction) {
                case DOWN:
                    nextY = yCoords[3] + 1;
                    nextX = xCoords[3];
                    break;
                case UP:
                    nextY = yCoords[3] - 1;
                    nextX = xCoords[3];
                    break;
                case LEFT:
                    nextX = xCoords[3] - 1;
                    nextY = yCoords[3];
                    break;
                case RIGHT:
                    nextX = xCoords[3] + 1;
                    nextY = yCoords[3];
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
                yCoords[0] = yCoords[1];
                xCoords[0] = xCoords[1];
                yCoords[1] = yCoords[2];
                xCoords[1] = xCoords[2];
                yCoords[2] = yCoords[3];
                xCoords[2] = xCoords[3];
                xCoords[3] = nextX;
                yCoords[3] = nextY;
            }
        } else {
            for (int i = 0; i < FIG_SIZE; i++) {
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
                yCoords[0]++;
                yCoords[1]++;
                yCoords[2]++;
                yCoords[3]++;
            }
        }
    }

    /**
     * Method rotates a figure
     */
    public void rotate() {
        figureMaxLenght = Math.max(Math.abs(maxCoordX() - minCoordX()), Math.abs(maxCoordY() - minCoordY())) + 1;

        if (!(figureMaxLenght == 4 || figureMaxLenght == 3)) {
            return;
        }

        topxCoord = minCoordX();
        topyCoord = minCoordY();
        maxxCoord = maxCoordX();
        maxyCoord = maxCoordY();

        // Получаем координаты для матрицы 4х4 или 3х3
        for (int i = 0; i < FIG_SIZE; i++) {
            rotatexCoords[i] = Math.abs(xCoords[i] - maxxCoord);
            rotateyCoords[i] = Math.abs(yCoords[i] - maxyCoord);
        }

        if (figureMaxLenght == 4) {

            // Заполняем матрицу 4х4
            for (int j = 0; j < figureMaxLenght; j++) {
                for (int l = 0; l < figureMaxLenght; l++) {
                    figureMatrix4[l][j] = -1;
                    for (int i = 0; i < FIG_SIZE; i++) {
                        if (rotatexCoords[i] == j && rotateyCoords[i] == l) {
                            figureMatrix4[rotateyCoords[i]][rotatexCoords[i]] = getBlockColor().getValue();
                        }
                    }
                }
            }

            // Переворачиваем матрицу 4х4 на 90 град
            for (int i = 0; i < figureMaxLenght; i++) {
                for (int j = 0; j < figureMaxLenght; j++) {
                    tmpFigureMatrix4[j][figureMaxLenght - 1 - i] = figureMatrix4[i][j];
                }
            }

            // Сдвигаем фигуру в левый верхний угол матрицы, чтобы не скакала по экрану
            isLeftColumnEmpty = true;
            while (isLeftColumnEmpty) {
                for (int i = 0; i < figureMaxLenght; i++) {
                    if (tmpFigureMatrix4[i][0] != -1) {
                        isLeftColumnEmpty = false;
                    }
                }
                if (isLeftColumnEmpty) {
                    for (int i = 0; i < figureMaxLenght; i++) {
                        for (int j = 0; j < figureMaxLenght; j++) {
                            if (j == figureMaxLenght - 1) {
                                tmpFigureMatrix4[i][j] = -1;
                            } else {
                                tmpFigureMatrix4[i][j] = tmpFigureMatrix4[i][j + 1];
                            }
                        }
                    }
                }
            }
            isTopRowEmpty = true;
            while (isTopRowEmpty) {
                for (int i = 0; i < figureMaxLenght; i++) {
                    if (tmpFigureMatrix4[0][i] != -1) {
                        isTopRowEmpty = false;
                    }
                }
                if (isTopRowEmpty) {
                    tempArr = tmpFigureMatrix4[0];
                    tmpFigureMatrix4[0] = tmpFigureMatrix4[1];
                    tmpFigureMatrix4[1] = tmpFigureMatrix4[2];
                    tmpFigureMatrix4[2] = tmpFigureMatrix4[3];
                    tmpFigureMatrix4[3] = tempArr;
                }
            }

            // Проецируем координаты фигуры обратно из матрицы 4х4 в основную игровую матрицу
            temp = 0;
            for (int i = 0; i < figureMaxLenght; i++) {
                for (int j = 0; j < figureMaxLenght; j++) {
                    if (tmpFigureMatrix4[i][j] != -1) {
                        nextXCoords[temp] = j + topxCoord;
                        nextYCoords[temp] = i + topyCoord;
                        temp++;
                    }
                }
            }

        }

        if (figureMaxLenght == 3) {

            // Заполняем матрицу 3х3
            for (int j = 0; j < figureMaxLenght; j++) {
                for (int l = 0; l < figureMaxLenght; l++) {
                    figureMatrix3[l][j] = -1;
                    for (int i = 0; i < FIG_SIZE; i++) {
                        if (rotatexCoords[i] == j && rotateyCoords[i] == l) {
                            figureMatrix3[rotateyCoords[i]][rotatexCoords[i]] = getBlockColor().getValue();
                        }
                    }
                }
            }

            // Переворачиваем матрицу 3х3 на 90 град
            for (int i = 0; i < figureMaxLenght; i++) {
                for (int j = 0; j < figureMaxLenght; j++) {
                    tmpFigureMatrix3[j][figureMaxLenght - 1 - i] = figureMatrix3[i][j];
                }
            }

            // Сдвигаем фигуру в левый верхний угол матрицы, чтобы не скакала по экрану
            isLeftColumnEmpty = true;
            for (int i = 0; i < figureMaxLenght; i++) {
                if (tmpFigureMatrix3[i][0] != -1) {
                    isLeftColumnEmpty = false;
                }
            }
            if (isLeftColumnEmpty) {
                for (int i = 0; i < figureMaxLenght; i++) {
                    for (int j = 0; j < figureMaxLenght; j++) {
                        if (j == figureMaxLenght - 1) {
                            tmpFigureMatrix3[i][j] = -1;
                        } else {
                            tmpFigureMatrix3[i][j] = tmpFigureMatrix3[i][j + 1];
                        }
                    }
                }
            }
            isTopRowEmpty = true;
            for (int i = 0; i < figureMaxLenght; i++) {
                if (tmpFigureMatrix3[0][i] != -1) {
                    isTopRowEmpty = false;
                }
            }
            if (isTopRowEmpty) {
                tempArr = tmpFigureMatrix3[0];
                tmpFigureMatrix3[0] = tmpFigureMatrix3[1];
                tmpFigureMatrix3[1] = tmpFigureMatrix3[2];
                tmpFigureMatrix3[2] = tempArr;
            }

            // Проецируем координаты фигуры обратно из матрицы 3х3 в основную игровую матрицу
            temp = 0;
            for (int i = 0; i < figureMaxLenght; i++) {
                for (int j = 0; j < figureMaxLenght; j++) {
                    if (tmpFigureMatrix3[i][j] != -1) {
                        nextXCoords[temp] = j + topxCoord;
                        nextYCoords[temp] = i + topyCoord;
                        temp++;
                    }
                }
            }

        }

        // Проверяем, возможно ли перевернуть фигуру по новым координатам
        canRotate = true;
        for (int i = 0; i < FIG_SIZE; i++) {
            if (nextYCoords[i] > 0 &&
                    nextYCoords[i] < GameField.MATRIX_HEIGHT &&
                    nextXCoords[i] > 0 &&
                    nextXCoords[i] < GameField.MATRIX_WIDTH) {
                if (GameField.getInstance().gameMatrix[nextYCoords[i]][nextXCoords[i]] != -1) {
                    canRotate = false;
                }
            } else {
                canRotate = false;
            }
        }

        // Если возможно, делаем переворот фигуры
        if (canRotate) {
            for (int i = 0; i < FIG_SIZE; i++) {
                xCoords[i] = nextXCoords[i];
                yCoords[i] = nextYCoords[i];
            }
        }

    }

    /**
     * Move figure left on 1 square
     */
    public void moveLeft() {
        if (minCoordX() > 0) {
            for (int i = 0; i < FIG_SIZE; i++) {
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
            for (int i = 0; i < FIG_SIZE; i++) {
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
            for (int i = 0; i < FIG_SIZE; i++) {
                if (GameField.getInstance().gameMatrix[yCoords[i]][nextXCoords[i]] != -1) {
                    canTurn = false;
                }
            }
            if (canTurn) {
                for (int i = 0; i < FIG_SIZE; i++) {
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
