package org.sweepers.models;

/**
 * This is an abstract class that is inherited by the cells with and without
 * mines.
 */
public abstract class Cell implements Cloneable {
    private int x, y;
    private boolean revealed;
    private boolean flagged;

    /**
     * Creates a new cell.
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell
     */
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        revealed = false;
        flagged = false;
    }

    /**
     * Sets the revealed boolean to true.
     */
    public void reveal() {
        revealed = true;
    }

    /**
     * Toggles the flagged boolean.
     * 
     * @return the result of the flagged boolean
     */
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

    // #region Getters and setters
    /**
     * @return if the cell is revealed
     */
    public boolean isRevealed() {
        return revealed;
    }

    /**
     * @return if the cell is flagged
     */
    public boolean isFlagged() {
        return flagged;
    }

    /**
     * @return the x position of the cell
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y position of the cell
     */
    public int getY() {
        return y;
    }
    // #endregion
}
