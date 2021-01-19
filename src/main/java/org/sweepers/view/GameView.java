package org.sweepers.view;

import org.sweepers.components.ZoomableScrollPane;
import org.sweepers.models.Cell;
import org.sweepers.models.Mine;
import org.sweepers.models.Mineless;

import javafx.beans.value.ObservableValue;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * This class visualizes the game itself
 */
public class GameView {
    public static final int PREFFERED_CELL_SIZE = 32;

    public static final Color[] numberColors = { Color.BLUE, Color.GREEN, Color.RED, Color.PURPLE, Color.MAROON,
            Color.TURQUOISE, Color.BLACK, Color.GRAY };

    private GraphicsContext gcCell, gcOverlay, gcGrid, gcFlag, gcMineSquares, gcFlagSquares;
    private Canvas cCell, cOverlay, cGrid, cFlag, cMineSquares, cFlagSquares;
    private int cellWidth, cellHeight;
    private int canvasWidth, canvasHeight;
    private Image flagImage, bombImage;
    private ZoomableScrollPane scrollPane;

    boolean mineSquaresActive = false;

    public GameView(int gameWidth, int gameHeight, GridPane target) {
        this.cellWidth = PREFFERED_CELL_SIZE;
        this.cellHeight = PREFFERED_CELL_SIZE;
        
        canvasWidth = gameWidth * cellWidth;
        canvasHeight = gameHeight * cellHeight;
        
        flagImage = new Image("/flag.png");
        bombImage = new Image("/bomb.png");

        createNodes(target);
        drawGridAndOverlay();
    }

    private void createNodes(GridPane target) {
        // Create canvas
        cCell = new Canvas(canvasWidth, canvasHeight);
        cOverlay = new Canvas(canvasWidth, canvasHeight);
        cGrid = new Canvas(canvasWidth, canvasHeight);
        cFlag = new Canvas(canvasWidth, canvasHeight);
        cFlagSquares = new Canvas(canvasWidth, canvasHeight);
        cMineSquares = new Canvas(canvasWidth, canvasHeight);

        // Set gc's
        gcCell = cCell.getGraphicsContext2D();
        gcOverlay = cOverlay.getGraphicsContext2D();
        gcGrid = cGrid.getGraphicsContext2D();
        gcFlag = cFlag.getGraphicsContext2D();
        gcFlagSquares = cFlagSquares.getGraphicsContext2D();
        gcMineSquares = cMineSquares.getGraphicsContext2D();

        // Create outer nodes
        StackPane stackPane = new StackPane(cCell, cOverlay, cGrid, cFlag, cMineSquares, cFlagSquares);
        scrollPane = new ZoomableScrollPane(stackPane);
        target.getChildren().add(scrollPane);

        scrollPane.getScaleValue().addListener(this::onZoom);
    }

    public Canvas getClickableCanvas() {
        return cFlagSquares;
    }

    public void zoom(double difference) {
        scrollPane.zoom(difference);
    }

    private void onZoom(ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
        double gridLimit = 0.5;
        double numberLimit = 0.35;

        cGrid.setOpacity(((double) newVal - gridLimit) * 20);
        cCell.setOpacity(((double) newVal - numberLimit) * 20);
        cFlag.setOpacity(((double) newVal - numberLimit) * 20);
        cFlagSquares.setOpacity(1 - cFlag.getOpacity());
        
        if (mineSquaresActive) {
            cMineSquares.setOpacity(1 - cFlag.getOpacity());
        }
    }
    
    /**
     * Draws the content of all the cells.
     * @param level the 2D array of cells
     * @param canvasHeight the height of the canvas
     * @param canvasWidth the width of the canvas
     */
    public void drawCells(Cell[][] level) {
        gcCell.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gcMineSquares.setFill(Color.BLACK);
        cMineSquares.setOpacity(0);

        // Draw lower layer - mines and numbers
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                if (level[y][x] instanceof Mine) {
                    // Draw Image
                    gcCell.drawImage(bombImage, x * cellWidth, y * cellHeight, cellWidth, cellHeight);
                    gcMineSquares.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
                } else {
                    int number = ((Mineless) level[y][x]).getNeighbors();
                    if (number != 0) {
                        gcCell.setFill(numberColors[number - 1]);
                        gcCell.setTextAlign(TextAlignment.CENTER);
                        gcCell.setTextBaseline(VPos.CENTER);
                        gcCell.fillText(String.valueOf(number), (x + 0.5) * cellWidth, (y + 0.5) * cellHeight);
                    }
                }
            }
        }
    }

    /**
     * Draws the gray grid on top of the cells, and the grid.
     * @param canvasWidth the width of the canvas
     * @param canvasHeight the height of the canvas
     */
    public void drawGridAndOverlay() {
        // Draw the overlay
        gcOverlay.setFill(Color.GREY);
        gcOverlay.fillRect(0, 0, canvasWidth, canvasHeight);
        
        // Draw the grid
        gcGrid.setStroke(Color.BLACK);
        gcGrid.setLineWidth(2);
        for (int y = 0; y <= canvasHeight; y += cellHeight) {
            gcGrid.strokeLine(0, y, canvasWidth, y);
        }
        for (int x = 0; x <= canvasWidth; x += cellWidth) {
            gcGrid.strokeLine(x, 0, x, canvasHeight);
        }

        // Set flag squares color
        cFlagSquares.setOpacity(0);
        gcFlagSquares.setFill(Color.ORANGE);
    }

    /**
     * Automatically gets called when a cell is updated.
     */
    public void onCellUpdate(Cell oldCell, Cell newCell) {
        if (!oldCell.isRevealed() && newCell.isRevealed()) {
            revealCell(newCell.getX(), newCell.getY());
        }
        if (!oldCell.isFlagged() && newCell.isFlagged()){
            drawFlag(newCell.getX(), newCell.getY());
        }
        if (oldCell.isFlagged() && !newCell.isFlagged()){
            clearFlag(newCell.getX(), newCell.getY());
        }
    }

    /**
     * Clears the overlay from a cell.
     * @param x the x index of the cell
     * @param y the y index of the cell
     */
    public void revealCell(int x, int y) {
        gcOverlay.clearRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
    }

    /**
     * Draws a flag on a cell.
     * @param x the x index of the cell
     * @param y the y index of the cell
     */
    public void drawFlag(int x, int y){
        gcFlag.drawImage(flagImage, x * cellWidth, y * cellHeight, cellWidth, cellHeight);
        gcFlagSquares.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
    }

    /**
     * Clears a flag from a cell.
     * @param x the x index of the cell
     * @param y the y index of the cell
     */
    public void clearFlag(int x, int y){
        gcFlag.clearRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
        gcFlagSquares.clearRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
    }

    /**
     * Clears the whole overlay.
     */
    public void revealAll() {
        gcOverlay.clearRect(0, 0, gcOverlay.getCanvas().getWidth(), gcOverlay.getCanvas().getHeight());
        mineSquaresActive = true;
        cMineSquares.setOpacity(1 - cFlag.getOpacity());
    }

}
