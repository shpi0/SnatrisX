package ru.shpi0.snatrisx.game;

import ru.shpi0.snatrisx.math.Rnd;

import static ru.shpi0.snatrisx.game.Direction.DOWN;

public class Figure {

    public static final int FIG_SIZE = 4;

    private boolean isSnake;
    private boolean canMove;
    protected int[] xCoords = new int[FIG_SIZE];
    protected int[] yCoords = new int[FIG_SIZE];
    private Direction direction;
    private BlockColor blockColor;
    private int initCoord;

    public Figure() {
        this((int) (Rnd.nextFloat(1f, 9f)));
    }

    public Figure(int initCoord) {
        newFigure(initCoord);
    }

    public void newFigure(int initCoord) {
        this.initCoord = initCoord;
        this.isSnake = true;
        this.canMove = true;
        this.direction = DOWN;
        this.blockColor = BlockColor.values()[(int) (Rnd.nextFloat(0f, 0.5f) * 10)];
        for (int i = 0; i < xCoords.length; i++) {
            xCoords[i] = initCoord;
        }
            yCoords[3] = -1;
            yCoords[2] = -2;
            yCoords[1] = -3;
            yCoords[0] = -4;
    }

    public void newFigure() {
        newFigure((int) (Rnd.nextFloat(1f, 9f)));
    }

    public void move() {
        if (isSnake) {
            yCoords[0] = yCoords[1];
            xCoords[0] = xCoords[1];
            yCoords[1] = yCoords[2];
            xCoords[1] = xCoords[2];
            yCoords[2] = yCoords[3];
            xCoords[2] = xCoords[3];
            switch (direction) {
                case DOWN:
                    yCoords[3]++;
                    break;
                case UP:
                    yCoords[3]--;
                    break;
                case LEFT:
                    xCoords[3]--;
                    break;
                case RIGHT:
                    xCoords[3]++;
                    break;
            }
        } else {
            if (canMove) {
                yCoords[0]++;
                yCoords[1]++;
                yCoords[2]++;
                yCoords[3]++;
            }
        }
    }

    public void moveLeft() {
        xCoords[0]--;
        xCoords[1]--;
        xCoords[2]--;
        xCoords[3]--;
    }

    public void moveRight() {
        xCoords[0]++;
        xCoords[1]++;
        xCoords[2]++;
        xCoords[3]++;
    }

    public boolean isSnake() {
        return isSnake;
    }

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
