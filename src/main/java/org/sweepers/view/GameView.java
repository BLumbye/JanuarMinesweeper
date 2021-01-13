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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

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

    public void drawGridOverlay(int height, int width) {
        // Draw middle layer
        gcOverlay.setFill(Color.GREY);
        gcOverlay.fillRect(0, 0, width, height);
        
        // Draw Upper layer
        gcGrid.setStroke(Color.BLACK);
        gcGrid.setLineWidth(2 * ratio);
        for (int y = 0; y <= height; y += cellHeight) {
            gcGrid.strokeLine(0, y, width, y);
        }
        for (int x = 0; x <= width; x += cellWidth) {
            gcGrid.strokeLine(x, 0, x, height);
        }
    }

/*
    public void propertyChange(PropertyChangeEvent evt) {
        //gameInProgress
        if ("gameInProgress".equals(evt.getPropertyName()){
            revealAll();
        }
        //Cells
        //revealed
        else if ("revealed".equals(evt.getPropertyName()){
            
        }
        //flagged
        else if ("flagged".equals(evt.getPropertyName()){

        }
    }
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

    public void revealCell(int x, int y) {
        gcOverlay.clearRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
    }

    public void drawFlag(int x, int y){
        gcFlag.drawImage(flagImage, x * cellWidth, y * cellHeight, cellWidth, cellHeight);
    }

    public void clearFlag(int x, int y){
        gcFlag.clearRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
    }

    public void revealAll() {
        gcOverlay.clearRect(0, 0, gcOverlay.getCanvas().getWidth(), gcOverlay.getCanvas().getHeight());
    }

}
