package org.sweepers.models;

/**
 * This class defines the cells that don't contain mines.
 */
public class Mineless extends Cell {
    private int neighbors;

    /**
     * Creates a new mineless cell.
     * @param x the x position of the mineless cell
     * @param y the y position of the mineless cell
     * @param n the number of mines surrounding the cell
     */
    public Mineless(int x, int y, int n) {
        super(x, y);
        neighbors = n;
    }

    /**
     * @return how many of the cells neighbors are mines
     */
    public int getNeighbors() {
        return neighbors;
    }
}
