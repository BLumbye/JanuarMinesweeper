package org.sweepers.models;
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

    public void reveal() {
        revealed = true;
    }

    public boolean isRevealed() {
        return revealed;
    }
}
