package org.sweepers.control;

import java.net.URISyntaxException;

import org.sweepers.models.Level;
import org.sweepers.view.GameView;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.stage.Screen;

//
public class GameController {
    private static final int PREFFERED_CELL_SIZE = 32;

    @FXML
    private Canvas canvasLower;

    @FXML
    private Canvas canvasMiddle;

    @FXML
    private Canvas canvasUpper;

    @FXML
    private Label txtOverlay;

    // Model
    private Level level;

    private GameView gameView;
    private int cellWidth, cellHeight;
    private boolean gameInProgress;

    public GameController(Level level) {
        this.level = level;
    }

    @FXML
    private void initialize() {
        int screenWidth = (int) Screen.getPrimary().getBounds().getWidth() - 100;
        int screenHeight = (int) Screen.getPrimary().getBounds().getHeight() - 200;
        int canvasWidth = level.getWidth() * PREFFERED_CELL_SIZE;
        int canvasHeight = level.getHeight() * PREFFERED_CELL_SIZE;

        double ratio = 1;
        if (canvasWidth > screenWidth || canvasHeight > screenHeight) {
            ratio = Math.min((double) screenWidth / canvasWidth, (double) screenHeight / canvasHeight);
            int newCellSize = (int) Math.floor(PREFFERED_CELL_SIZE * ratio);
            ratio = (double) newCellSize / PREFFERED_CELL_SIZE;
            canvasWidth = level.getWidth() * newCellSize;
            canvasHeight = level.getHeight() * newCellSize;
        }

        canvasLower.setWidth(canvasWidth);
        canvasLower.setHeight(canvasHeight);
        canvasMiddle.setWidth(canvasWidth);
        canvasMiddle.setHeight(canvasHeight);
        canvasUpper.setWidth(canvasWidth);
        canvasUpper.setHeight(canvasHeight);

        cellHeight = canvasHeight / level.getHeight();
        cellWidth = canvasWidth / level.getWidth();

        // Draw level
        gameView = new GameView(canvasLower.getGraphicsContext2D(), canvasMiddle.getGraphicsContext2D(),
                canvasUpper.getGraphicsContext2D(), cellWidth, cellHeight, ratio);
        gameView.drawLevel(level.getLevel(), canvasHeight, canvasWidth);

        canvasUpper.addEventHandler(MouseEvent.MOUSE_CLICKED, this::mouseClicked);
        gameInProgress = true;
    }

    private void mouseClicked(MouseEvent event) {
        if (!gameInProgress)
            return;

        double mouseX = event.getX();
        double mouseY = event.getY();
        int x = Math.floorDiv((int) mouseX, cellWidth);
        int y = Math.floorDiv((int) mouseY, cellHeight);
        if (level.onClick(x, y)) {
            lose();
        }
        gameView.revealCell(x, y);
        if (level.gameWon()) {
            win();
        }
    }

    private void win() {
        txtOverlay.setText("You win!");
        txtOverlay.getStyleClass().add("win");
        gameView.revealAll();
        gameInProgress = false;
    }

    private void lose() {
        txtOverlay.setText("You lose!");
        txtOverlay.getStyleClass().add("lose");
        gameView.revealAll();
        gameInProgress = false;
    }
}
