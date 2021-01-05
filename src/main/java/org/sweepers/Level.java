package org.sweepers;

import java.util.Random;

/**
 * This class contains a 2D array of cells and methods that describes a level of minesweeper.
 */
public class Level {
    private Cell[][] level;
    private int height;
    private int width;
    private int mines;
    private int revealed;

    public Level(int height, int width, int mines) {
        this.height = height;
        this.width = width;
        this.mines = mines;
        level = new Cell[this.height][this.width];
        revealed = 0;
        generateLevel();
    }

    /**
     * A handler for clicking on a cell in the level.
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return This boolean defines whether the game continues or if the player lost by clicking on a mine (true = game over)
     */
    public boolean onClick(int x, int y) {
        if (!level[y][x].isRevealed()) {
            revealed++;
            return level[y][x].onClick();
        } else {
            return false;
        }
    }

    /**
     * This method helps the constructor by placing cells with and without mines
     */
    private void generateLevel() {
        // Places mines
        Random rand = new Random();
        for (int i = 0; i < mines; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            if (level[y][x] == null) {
                level[y][x] = new Mine(x, y);
            } else {
                i--;
            }
        }

        // Places mineless cells
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (level[y][x] == null) {
                    level[y][x] = new Mineless(x, y, countNeighbors(x, y));
                }
            }
        }
    }

    /**
     * Counts the amount of neighbors that are mines (includes diagonals).
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the number of neighbor cells that contains mines
     */
    private int countNeighbors(int x, int y) {
        int count = 0;
        for (int i = Math.max(y - 1, 0); i <= Math.min(y + 1, height - 1); i++) {
            for (int j = Math.max(x - 1, 0); j <= Math.min(x + 1, width - 1); j++) {
                if (level[i][j] instanceof Mine) {
                    count++;
                }
            }
        }
        return count;
    }
}
