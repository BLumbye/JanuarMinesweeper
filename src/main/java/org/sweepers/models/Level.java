package org.sweepers.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.util.Pair;

/**
 * This class contains a 2D array of cells and methods that describes a level of
 * minesweeper.
 */
public class Level {
    private Cell[][] level;
    private int height;
    private int width;
    private int mines;
    private int revealed;

    public Level(int height, int width, int mines) throws IllegalArgumentException {
        // Check preconditions
        if (height < 4 || height > 100 || width < 4 || width > 100) {
            throw new IllegalArgumentException("Width and height must be between 4 and 100!");
        }

        if (mines >= height * width) {
            throw new IllegalArgumentException("The number of mines chosen exceeds the amount of cells!");
        }

        if (mines < 1) {
            throw new IllegalArgumentException("You are a boring person, there has to be mines in \"MINE\"Sweeper!");
        }

        // Initialize
        this.height = height;
        this.width = width;
        this.mines = mines;
        level = new Cell[this.height][this.width];
        revealed = 0;
        generateLevel();
    }

    /**
     * A handler for clicking on a cell in the level.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return This boolean defines whether the game continues or if the player lost
     *         by clicking on a mine (true = game over)
     */
    public boolean onClick(int x, int y) {
        if (!level[y][x].isRevealed()) {
            revealed++;
            level[y][x].reveal();
            return level[y][x] instanceof Mine;
        }
        return false;
    }

    public Cell[][] getLevel() {
        return level;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getMines() {
        return mines;
    }

    public boolean gameWon() {
        return revealed + mines == width * height;
    }

    /**
     * This method helps the constructor by placing cells with and without mines
     */
    private void generateLevel() {
        // Populate valid spots
        List<Pair<Integer,Integer>> validSpots = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
               validSpots.add(new Pair<Integer,Integer>(x, y));
            }
        }

        // Places mines
        Random rand = new Random();
        for (int i = 0; i < mines; i++) {
            int index = rand.nextInt(validSpots.size());
            Pair<Integer,Integer> spot = validSpots.get(index);
            level[spot.getValue()][spot.getKey()] = new Mine(spot.getKey(), spot.getValue());
            validSpots.remove(index);
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
     * 
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
