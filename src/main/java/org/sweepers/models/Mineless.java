package org.sweepers.models;
/**
 * This class defines the cells that don't contain mines.
 */
public class Mineless extends Cell {
    private int neighbors;

    public Mineless(int x, int y, int n) {
        super(x, y);
        neighbors = n;
    }

    /**
     * This method reveals the cell when it is clicked on
     * @return this value means that the game goes on
     */
    public boolean onClick() {
        revealed = true;
        return false;
    }

    public int getNeighbors() {
        return neighbors;
    }
}
