package ru.shpi0.snatrisx.game;

/**
 * Colors and indexes for colors of game figures
 */

public enum BlockColor {
    GREEN  (0),
    BLUE   (1),
    YELLOW (2),
    RED    (3),
    BROWN  (4),
    WHITE  (5);

    private int value;

    BlockColor(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
