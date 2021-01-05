package org.sweepers;
/**
 * This class defines the cells that contain mines
 */
public class Mine extends Cell {

    public Mine(int x, int y) {
        super(x, y);
    }

    /**
     * This method reveals the mine when it is clicked on
     * @return this value is used to end the game when a mine is revealed
     */
    public boolean onClick() {
        revealed = true;
        return true;
    }
}
