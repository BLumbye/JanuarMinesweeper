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
    public static final Color[] numberColors = {Color.BLUE, Color.GREEN, Color.RED, Color.PURPLE, Color.MAROON, Color.TURQUOISE, Color.BLACK, Color.GRAY};
    GraphicsContext gcLower;
    GraphicsContext gcMiddle;
    GraphicsContext gcUpper;

    public GameView(GraphicsContext gcLower, GraphicsContext gcMiddle, GraphicsContext gcUpper) {
        this.gcLower = gcLower;
        this.gcMiddle = gcMiddle;
        this.gcUpper = gcUpper;
    }

    public void drawLevel(Cell[][] level, int height, int width) {
        int cellHeight = height / level.length;
        int cellWidth = width / level[0].length;

        Image bombImage = new Image("/bomb.png");
        gcLower.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Draw lower layer - mines and numbers
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[0].length; x++) {
                if (level[y][x] instanceof Mine) {
                    // Draw Image
                    gcLower.drawImage(bombImage, x * cellWidth, y * cellHeight, cellWidth, cellHeight);
                } else {
                    int number = ((Mineless) level[y][x]).getNeighbors();
                    if (number != 0) {
                        gcLower.setFill(numberColors[number - 1]);
                        gcLower.setTextAlign(TextAlignment.CENTER);
                        gcLower.setTextBaseline(VPos.CENTER);
                        gcLower.fillText(String.valueOf(number), (x + 0.5) * cellWidth, (y + 0.5) * cellHeight);
                    }
                }
            }
        }

        /*// Draw middle layer
        gcMiddle.setFill(Color.GREY);
        gcMiddle.fillRect(0,0, height , width);


        // Draw Upper layer
        gcUpper.setStroke(Color.BLACK);
        gcUpper.setLineWidth(2);
        for (int y = 0; y <= height; y += cellHeight){
            gcUpper.strokeLine(0, y, width, y);
        }
        for (int x = 0; x <= width; x += cellWidth) {
            gcUpper.strokeLine(x, 0, x, height);
        }*/



    }
}
