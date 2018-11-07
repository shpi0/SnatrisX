package ru.shpi0.snatrisx.game;

/**
 * Colors and indexes for colors of game figures
 */

public enum BlockColor {
    GREEN  (0),
    RED    (1),
    YELLOW (2),
    PINK   (3),
    BLUE   (4);

    private int value;

    BlockColor(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
