package org.sweepers.view;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import org.sweepers.models.Cell;
import org.sweepers.models.Mine;
import org.sweepers.models.Mineless;

/**
 * This class visualizes the game itself
 */
public class GameView {
    public static final Color[] numberColors = { Color.BLUE, Color.GREEN, Color.RED, Color.PURPLE, Color.MAROON,
            Color.TURQUOISE, Color.BLACK, Color.GRAY };
    private GraphicsContext gcCell, gcOverlay, gcGrid, gcFlag;
    private int cellWidth, cellHeight;
    private double ratio;
    private Image flagImage, bombImage;

    public GameView(GraphicsContext gcCell, GraphicsContext gcOverlay, GraphicsContext gcGrid, GraphicsContext gcFlag,
            int cellWidth, int cellHeight, double ratio) {
        this.gcCell = gcCell;
        this.gcOverlay = gcOverlay;
        this.gcGrid = gcGrid;
        this.gcFlag = gcFlag;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.ratio = ratio;
        
        flagImage = new Image("/flag.png");
        bombImage = new Image("/bomb.png");
    }
    
    /**
     * Draws the content of all the cells.
     * @param level the 2D array of cells
     * @param height the height of the canvas
     * @param width the width of the canvas
     */
    public void drawCells(Cell[][] level, int height, int width) {
        gcCell.setFont(Font.font("Arial", FontWeight.BOLD, 24 * ratio));

        // Draw lower layer - mines and numbers
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                if (level[y][x] instanceof Mine) {
                    // Draw Image
                    gcCell.drawImage(bombImage, x * cellWidth, y * cellHeight, cellWidth, cellHeight);
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
     * @param height the height of the canvas
     * @param width the width of the canvas
     */
    public void drawGridAndOverlay(int height, int width) {
        // Draw the overlay
        gcOverlay.setFill(Color.GREY);
        gcOverlay.fillRect(0, 0, width, height);
        
        // Draw the grid
        gcGrid.setStroke(Color.BLACK);
        gcGrid.setLineWidth(2 * ratio);
        for (int y = 0; y <= height; y += cellHeight) {
            gcGrid.strokeLine(0, y, width, y);
        }
        for (int x = 0; x <= width; x += cellWidth) {
            gcGrid.strokeLine(x, 0, x, height);
        }
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
    }

    /**
     * Clears a flag from a cell.
     * @param x the x index of the cell
     * @param y the y index of the cell
     */
    public void clearFlag(int x, int y){
        gcFlag.clearRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
    }

    /**
     * Clears the whole overlay.
     */
    public void revealAll() {
        gcOverlay.clearRect(0, 0, gcOverlay.getCanvas().getWidth(), gcOverlay.getCanvas().getHeight());
    }

}
