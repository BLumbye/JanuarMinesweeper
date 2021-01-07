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

    public int getNeighbors() {
        return neighbors;
    }
}
