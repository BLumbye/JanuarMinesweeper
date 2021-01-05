package org.sweepers;
/**
 * This is an abstract class that is inherited by the cells with and without mines
 */
public abstract class Cell {
    protected int x, y;
    protected boolean revealed;

    public Cell(int x, int y){
        this.x = x;
        this.y = y;
        revealed = false;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public abstract boolean onClick();
}
