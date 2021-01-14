package org.sweepers.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Pair;

/**
 * This class contains a 2D array of cells and methods that describes a level of
 * minesweeper.
 */
public class Level {
    /** y first - x second */
    private Cell[][] level;
    private int height;
    private int width;
    private IntegerProperty mines;
    private int revealed;
    private IntegerProperty flagged;
    private boolean initialized;
    private long startTime;

    private List<BiConsumer<Cell, Cell>> cellListeners;

    public Level(int height, int width, int mines) throws IllegalArgumentException {

        // Check preconditions
        if (height < 4 || height > 100 || width < 4 || width > 100) {
            throw new IllegalArgumentException("Width and height must be between 4 and 100!");
        }

        // Max mines is subtracted by 9 because of the "free start"
        if (mines >= height * width - 9) {
            throw new IllegalArgumentException("The number of mines chosen exceeds the amount of cells!");
        }

        if (mines < 1) {
            throw new IllegalArgumentException("You are a boring person, there has to be mines in \"MINE\"Sweeper!");
        }

        // Initialize
        this.height = height;
        this.width = width;
        this.mines = new SimpleIntegerProperty(mines);
        level = new Cell[this.height][this.width];
        revealed = 0;
        flagged = new SimpleIntegerProperty(0);
        initialized = false;
        cellListeners = new ArrayList<>();
    }

    public void addCellListener(BiConsumer<Cell, Cell> func) {
        cellListeners.add(func);
    }

    /**
     * A handler for clicking on a cell in the level.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return This boolean defines whether the game continues or if the player lost
     *         by clicking on a mine (true = game over)
     * @throws CloneNotSupportedException
     */
    public boolean onClick(int x, int y) {
        Cell oldCell = (Cell) level[y][x].clone();

        if (!level[y][x].isRevealed()) {
            revealed++;
            level[y][x].reveal();

            if ((level[y][x] instanceof Mineless && ((Mineless) level[y][x]).getNeighbors() == 0)) {
                for (int i = Math.max(y - 1, 0); i <= Math.min(y + 1, height - 1); i++) {
                    for (int j = Math.max(x - 1, 0); j <= Math.min(x + 1, width - 1); j++) {
                        if (!level[i][j].isFlagged()) {
                            onClick(j, i);
                        }
                    }
                }
            }

            cellListeners.forEach(listener -> {
                listener.accept(oldCell, level[y][x]);
            });

            return level[y][x] instanceof Mine;
        }
        return false;
    }

    public void flag(int x, int y) {
        if(!level[y][x].isFlagged() && flagged.get() >= 9999) return;
        
        Cell oldCell = (Cell) level[y][x].clone();
        flagged.set(flagged.get() + (level[y][x].toggleFlagged() ? 1 : -1));

        cellListeners.forEach(listener -> {
            listener.accept(oldCell, level[y][x]);
        });
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

    public IntegerProperty getMines() {
        return mines;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public IntegerProperty getFlagged() {
        return flagged;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    public boolean gameWon() {
        return revealed + mines.get() == width * height && !Arrays.stream(level).flatMap(Arrays::stream).anyMatch(c -> c instanceof Mine && c.isRevealed());
    }

    public void generateLevel(Pair<Integer, Integer> startPosition) {
        // Populate valid spots
        List<Pair<Integer, Integer>> validSpots = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                validSpots.add(new Pair<Integer, Integer>(x, y));
            }
        }

        // TODO: Write test for this
        // Remove startPosition and fields around from valid spots
        for (int i = Math.max(startPosition.getValue() - 1, 0); i <= Math.min(startPosition.getValue() + 1,
                height - 1); i++) {
            for (int j = Math.max(startPosition.getKey() - 1, 0); j <= Math.min(startPosition.getKey() + 1,
                    width - 1); j++) {
                validSpots.remove(new Pair<Integer, Integer>(j, i));
            }
        }

        generateLevel(validSpots);
    }

    public void generateLevel() {
        // Populate valid spots
        List<Pair<Integer, Integer>> validSpots = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                validSpots.add(new Pair<Integer, Integer>(x, y));
            }
        }

        generateLevel(validSpots);
    }

    /**
     * This method helps the constructor by placing cells with and without mines
     */
    private void generateLevel(List<Pair<Integer, Integer>> validSpots) {
        initialized = true;
        startTime = System.currentTimeMillis();

        // Places mines
        Random rand = new Random();
        for (int i = 0; i < mines.get(); i++) {
            int index = rand.nextInt(validSpots.size());
            Pair<Integer, Integer> spot = validSpots.get(index);
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
