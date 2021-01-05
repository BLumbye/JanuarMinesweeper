package org.sweepers.control;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import org.sweepers.models.Level;
import org.sweepers.view.GameView;

//
public class GameController {
    @FXML
    private Canvas canvasLower;

    @FXML
    private Canvas canvasMiddle;

    @FXML
    private Canvas canvasUpper;

    // Model
    Level level;

    public GameController(Level level) {
        this.level = level;
    }

    @FXML
    private void initialize() {
        // Draw level
        GameView gameView = new GameView(canvasLower.getGraphicsContext2D(), canvasMiddle.getGraphicsContext2D(), canvasUpper.getGraphicsContext2D());
        gameView.drawLevel(level.getLevel(), 400, 400);
    }
}
