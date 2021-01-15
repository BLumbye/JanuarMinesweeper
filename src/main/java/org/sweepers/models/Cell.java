package org.sweepers.models;

/**
 * This is an abstract class that is inherited by the cells with and without
 * mines
 */
public abstract class Cell implements Cloneable {
    protected int x, y;
    protected boolean revealed;
    protected boolean flagged;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        revealed = false;
        flagged = false;
    }

    public void reveal() {
        revealed = true;
    }

    public boolean toggleFlagged() {
        return flagged = !flagged;
    }

    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    //#region Getters and setters
    public boolean isRevealed() {
        return revealed;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    //#endregion
}
