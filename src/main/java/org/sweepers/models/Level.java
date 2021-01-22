package org.sweepers.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
    private BooleanProperty initialized;
    private boolean inProgress;
    private long startTime;

    // For highscore
    private long totalTime;
    private String sizeSetting;
    private String difficultySetting;

    private List<BiConsumer<Cell, Cell>> cellListeners;

    /**
     * Creates a new level object.
     * @param height the number of cells on the vertical axis - must be between 4 and 100
     * @param width the number of cells on the horizontal axis - must be between 4 and 100
     * @param mines the number of mines that should be but on the level - must be between 1 and width * height - 10
     * @param sizeSetting the size setting that is chosen - used for high score
     * @param difficultySetting the difficulty setting that is chosen - used for high score
     * @throws IllegalArgumentException if the width, height, or mines isn't between the allowed values
     */
    public Level(int height, int width, int mines, String sizeSetting, String difficultySetting)
            throws IllegalArgumentException {

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
        initialized = new SimpleBooleanProperty(false);
        cellListeners = new ArrayList<>();
        inProgress = true;

        this.sizeSetting = sizeSetting;
        this.difficultySetting = difficultySetting;
    }

    /**
     * Add a listener, that gets called when a cell is updated.
     * @param func the function that should get called
     */
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
     */
    public boolean onClick(int x, int y) {
        return onClick(x, y, true);
    }

    private boolean onClick(int x, int y, boolean solveBlanks) {
        Cell oldCell = (Cell) level[y][x].clone();

        if (level[y][x].isFlagged()) {
            return false;
        } else if (!level[y][x].isRevealed()) {
            revealed++;
            level[y][x].reveal();

            // Blank cell clearer
            if ((level[y][x] instanceof Mineless && ((Mineless) level[y][x]).getNeighbors() == 0) && solveBlanks) {
                // A list of all the blank cells that should be checked
                List<Mineless> unchecked = new ArrayList<>();
                unchecked.add((Mineless) level[y][x]);

                while (!unchecked.isEmpty()) {
                    int _x = unchecked.get(0).getX();
                    int _y = unchecked.get(0).getY();
                    unchecked.remove(0);

                    for (int i = Math.max(_y - 1, 0); i <= Math.min(_y + 1, height - 1); i++) {
                        for (int j = Math.max(_x - 1, 0); j <= Math.min(_x + 1, width - 1); j++) {
                            // If the cell is not already revealed or flagged then click it
                            if (!level[i][j].isFlagged() && !level[i][j].isRevealed()) {
                                onClick(((Mineless) level[i][j]).getX(), ((Mineless) level[i][j]).getY(), false);

                                // If the cell is blank, then add it to the list so its neighbors get checked
                                if (((Mineless) level[i][j]).getNeighbors() == 0) {
                                    unchecked.add((Mineless) level[i][j]);
                                }
                            }
                        }
                    }
                }
            }

            // Trigger listeners
            cellListeners.forEach(listener -> {
                listener.accept(oldCell, level[y][x]);
            });

            return level[y][x] instanceof Mine;
        } else {
            int flaggedNeighbors = 0;
            for (int i = Math.max(y - 1, 0); i <= Math.min(y + 1, height - 1); i++) {
                for (int j = Math.max(x - 1, 0); j <= Math.min(x + 1, width - 1); j++) {
                    if (level[i][j].isFlagged()) {
                        flaggedNeighbors++;
                    }
                }
            }
            if (flaggedNeighbors == ((Mineless) level[y][x]).getNeighbors()) {
                boolean lost = false;
                for (int i = Math.max(y - 1, 0); i <= Math.min(y + 1, height - 1); i++) {
                    for (int j = Math.max(x - 1, 0); j <= Math.min(x + 1, width - 1); j++) {
                        if (!level[i][j].isFlagged() && !level[i][j].isRevealed()) {
                            if (onClick(j, i)) {
                                lost = true;
                            }
                        }
                    }
                }
                return lost;
            }
        }
        return false;
    }

    /**
     * Toggle the flag on a cell.
     * 
     * @param x the x-coordinate of the cell that's getting toggled
     * @param y the y-coordinate of the cell that's getting toggled
     */
    public void flag(int x, int y) {
        if (!level[y][x].isFlagged() && flagged.get() >= 9999 || level[y][x].isRevealed())
            return;

        Cell oldCell = (Cell) level[y][x].clone();
        flagged.set(flagged.get() + (level[y][x].toggleFlagged() ? 1 : -1));

        cellListeners.forEach(listener -> {
            listener.accept(oldCell, level[y][x]);
        });
    }

    /**
     * Returns if the player has won the game. Checks that the correct number of
     * cells are revealed, and that no mines are revealed.
     * @return if the game is won
     */
    public boolean gameWon() {
        return revealed + mines.get() == width * height
                && !Arrays.stream(level).flatMap(Arrays::stream).anyMatch(c -> c instanceof Mine && c.isRevealed());
    }

    /**
     * Saves the time and sets inProgress to false.
     */
    public void stop() {
        totalTime = getElapsedTime();
        inProgress = false;
    }

    /**
     * Counts the amount of neighbors that are mines (includes diagonals).
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the number of neighbor cells that contains mines
     */
    public int countNeighbors(int x, int y) {
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

    // #region Generate level methods
    /**
     * Starts the game with a predefined level. Only used for testing.
     * 
     * @param lvl the predefined level
     */
    public void generateTestLevel(Cell[][] lvl) {
        level = lvl;

        // Places mineless cells
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if (level[y][x] == null) {
                    level[y][x] = new Mineless(x, y, countNeighbors(x, y));
                }
            }
        }

        initialized.set(true);
        startTime = System.currentTimeMillis();
    }

    /**
     * This public generateLevel method is used to generate a level with a free
     * start. The free start ensures that there are no mines on the startPosition
     * and the 8 sorrounding cells.
     * 
     * @param startPosition the position that shouldn't have mines
     */
    public void generateLevel(Pair<Integer, Integer> startPosition) {
        // Populate valid spots
        List<Pair<Integer, Integer>> validSpots = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                validSpots.add(new Pair<Integer, Integer>(x, y));
            }
        }

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

    /**
     * This public generateLevel method is used to generate a level without a free
     * start.
     */
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

        initialized.set(true);
        startTime = System.currentTimeMillis();
    }
    // #endregion

    // #region Getters and setters
    /**
     * @return the 2D array of cells
     */
    public Cell[][] getLevel() {
        return level;
    }

    /**
     * @return the number of cells on the vertical axis
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return the number of cells on the horizontal axis
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the property for the number of mines
     */
    public IntegerProperty getMines() {
        return mines;
    }

    /**
     * @return the boolean property for if the game is initialized yet
     */
    public BooleanProperty isInitialized() {
        return initialized;
    }

    /**
     * @return if the game is currently in progress
     */
    public boolean isInProgress() {
        return inProgress;
    }

    /**
     * @return the property that cointains the number of flagged cells
     */
    public IntegerProperty getFlagged() {
        return flagged;
    }

    /**
     * @return the number of flagged cells
     */
    public int getFlaggedInt() {
        return flagged.get();
    }

    /**
     * @return the number of milliseconds since the first click
     */
    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * @return the size setting that was chosen on the start screen
     */
    public String getSizeSetting() {
        return sizeSetting;
    }

    /**
     * @return the difficulty setting that was chosen on the start screen
     */
    public String getDifficultySetting() {
        return difficultySetting;
    }

    /**
     * @return the time it took to complete the level - will be 0 before level is completed
     */
    public long getTotalTime() {
        return totalTime;
    }
    // #endregion
}
