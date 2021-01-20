package org.sweepers.view;

import org.sweepers.components.ZoomableScrollPane;
import org.sweepers.models.Cell;
import org.sweepers.models.Level;
import org.sweepers.models.Mine;
import org.sweepers.models.Mineless;

import javafx.beans.value.ObservableValue;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * This class visualizes the game itself
 */
public class GameView {
    public static final int PREFFERED_CELL_SIZE = 32;

    public static final Color[] numberColors = { Color.BLUE, Color.GREEN, Color.RED, Color.PURPLE, Color.MAROON,
            Color.TURQUOISE, Color.BLACK, Color.GRAY };

    private Rectangle[][] overlays;
    private ImageView[][] flags;
    private Rectangle[][] flagSquares;

    private Image flagImage, mineImage;
    private ZoomableScrollPane scrollPane;
    private ImageView winGif;

    private Group container;
    private Group gCell, gOverlay, gGrid, gFlag, gMineSquares, gFlagSquares;

    private Font numberFont;

    private Level level;

    boolean mineSquaresActive = false;

    public GameView(Level level, GridPane target) {
        this.level = level;

        overlays = new Rectangle[level.getWidth()][level.getHeight()];
        flags = new ImageView[level.getWidth()][level.getHeight()];
        flagSquares = new Rectangle[level.getWidth()][level.getHeight()];

        flagImage = new Image("/flag.png");
        mineImage = new Image("/bomb.png");

        numberFont = Font.font("Arial", FontWeight.BOLD, 24);

        createNodes(target);
        drawGridAndOverlay();
    }

    private void createNodes(GridPane target) {
        // Create win gif
        // winGif = new ImageView(new Image("/confetti.gif"));
        // winGif.setOpacity(0);
        // winGif.setPickOnBounds(false);

        // Create groups
        gCell = new Group();
        gOverlay = new Group();
        gGrid = new Group();
        gFlag = new Group();
        gMineSquares = new Group();
        gMineSquares.setOpacity(0);
        gFlagSquares = new Group();
        gFlagSquares.setOpacity(0);

        container = new Group(gCell, gOverlay, gGrid, gFlag, gMineSquares, gFlagSquares);

        // Create outer nodes
        scrollPane = new ZoomableScrollPane(container);
        target.getChildren().add(scrollPane);

        scrollPane.getScaleValue().addListener(this::onZoom);
    }

    public Group getClickableCanvas() {
        return container;
    }

    /**
     * Zoom in and out on the scroll pane.
     * 
     * @param difference value that is added to the scale of the content
     */
    public void zoom(double difference) {
        scrollPane.zoom(difference);
    }

    private void onZoom(ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
        double gridLimit = 0.5;
        double numberLimit = 0.35;

        gGrid.setOpacity(((double) newVal - gridLimit) * 20);
        gCell.setOpacity(((double) newVal - numberLimit) * 20);
        gFlag.setOpacity(((double) newVal - numberLimit) * 20);
        gFlagSquares.setOpacity(1 - gFlag.getOpacity());

        if (mineSquaresActive) {
            gMineSquares.setOpacity(1 - gFlag.getOpacity());
        }
    }

    /**
     * Draws the content of all the cells.
     * 
     * @param cells the 2D array of cells
     */
    public void drawCells() {
        Cell[][] cells = level.getLevel();
        for (int y = 0; y < cells.length; y++) {
            for (int x = 0; x < cells[0].length; x++) {
                if (cells[y][x] instanceof Mine) {
                    // Draw image of mine
                    ImageView iv = new ImageView(mineImage);
                    iv.setFitWidth(PREFFERED_CELL_SIZE);
                    iv.relocate(x * PREFFERED_CELL_SIZE, y * PREFFERED_CELL_SIZE);
                    gCell.getChildren().add(iv);

                    // Draw rectangle of mine
                    Rectangle rect = new Rectangle(x * PREFFERED_CELL_SIZE, y * PREFFERED_CELL_SIZE,
                            PREFFERED_CELL_SIZE, PREFFERED_CELL_SIZE);
                    rect.setFill(Color.BLACK);
                    rect.setStroke(Color.BLACK);
                    rect.setStrokeWidth(1);
                    gMineSquares.getChildren().add(rect);
                } else {
                    int number = ((Mineless) cells[y][x]).getNeighbors();
                    if (number != 0) {
                        // Draw number
                        Text num = new Text(x * PREFFERED_CELL_SIZE, (y + 0.5) * PREFFERED_CELL_SIZE,
                                String.valueOf(number));
                        num.setFill(numberColors[number - 1]);
                        num.setFont(numberFont);
                        num.setWrappingWidth(PREFFERED_CELL_SIZE);
                        num.setTextAlignment(TextAlignment.CENTER);
                        num.setTextOrigin(VPos.CENTER);
                        gCell.getChildren().add(num);
                    }
                }
            }
        }
    }

    /**
     * Draws the gray grid on top of the cells, and the grid.
     */
    public void drawGridAndOverlay() {
        // Draw the overlay
        for (int x = 0; x < level.getWidth(); x++) {
            for (int y = 0; y < level.getHeight(); y++) {
                Rectangle rect = new Rectangle(x * PREFFERED_CELL_SIZE, y * PREFFERED_CELL_SIZE, PREFFERED_CELL_SIZE,
                        PREFFERED_CELL_SIZE);
                rect.setFill(Color.GRAY);
                rect.setStroke(Color.GRAY);
                rect.setStrokeWidth(1);
                overlays[x][y] = rect;
                gOverlay.getChildren().add(rect);
            }
        }

        // Draw the grid
        Color gridColor = Color.BLACK;
        int gridThickness = 2;
        for (int y = 0; y <= level.getHeight() * PREFFERED_CELL_SIZE; y += PREFFERED_CELL_SIZE) {
            Line line = new Line(0, y, level.getWidth() * PREFFERED_CELL_SIZE, y);
            line.setStroke(gridColor);
            line.setStrokeWidth(gridThickness);
            gGrid.getChildren().add(line);
        }
        for (int x = 0; x <= level.getWidth() * PREFFERED_CELL_SIZE; x += PREFFERED_CELL_SIZE) {
            Line line = new Line(x, 0, x, level.getHeight() * PREFFERED_CELL_SIZE);
            line.setStroke(gridColor);
            line.setStrokeWidth(gridThickness);
            gGrid.getChildren().add(line);
        }
    }

    /**
     * Automatically gets called when a cell is updated.
     */
    public void onCellUpdate(Cell oldCell, Cell newCell) {
        if (!oldCell.isRevealed() && newCell.isRevealed()) {
            revealCell(newCell.getX(), newCell.getY());
        }
        if (!oldCell.isFlagged() && newCell.isFlagged()) {
            drawFlag(newCell.getX(), newCell.getY());
        }
        if (oldCell.isFlagged() && !newCell.isFlagged()) {
            clearFlag(newCell.getX(), newCell.getY());
        }
    }

    /**
     * Removes the overlay covering a cell.
     * 
     * @param x the x index of the cell
     * @param y the y index of the cell
     */
    public void revealCell(int x, int y) {
        gOverlay.getChildren().remove(overlays[x][y]);
        overlays[x][y] = null;
    }

    /**
     * Draws a flag on a cell.
     * 
     * @param x the x index of the cell
     * @param y the y index of the cell
     */
    public void drawFlag(int x, int y) {
        // Draw image of mine
        ImageView iv = new ImageView(flagImage);
        iv.setFitWidth(PREFFERED_CELL_SIZE);
        iv.relocate(x * PREFFERED_CELL_SIZE, y * PREFFERED_CELL_SIZE);
        flags[x][y] = iv;
        gFlag.getChildren().add(iv);

        // Draw rectangle of mine
        Rectangle rect = new Rectangle(x * PREFFERED_CELL_SIZE, y * PREFFERED_CELL_SIZE, PREFFERED_CELL_SIZE,
                PREFFERED_CELL_SIZE);
        rect.setFill(Color.ORANGE);
        rect.setStroke(Color.ORANGE);
        rect.setStrokeWidth(1);
        flagSquares[x][y] = rect;
        gFlagSquares.getChildren().add(rect);
    }

    /**
     * Clears a flag from a cell.
     * 
     * @param x the x index of the cell
     * @param y the y index of the cell
     */
    public void clearFlag(int x, int y) {
        gFlag.getChildren().remove(flags[x][y]);
        flags[x][y] = null;
        gFlagSquares.getChildren().remove(flagSquares[x][y]);
        flagSquares[x][y] = null;
    }

    /**
     * Clears the whole overlay.
     */
    public void revealAll() {
        gOverlay.setOpacity(0);
        mineSquaresActive = true;
        gMineSquares.setOpacity(1 - gFlag.getOpacity());
    }

    /**
     * Plays the confetti gif.
     */
    public void playWinGif() {
        winGif.setOpacity(1);
    }
}
