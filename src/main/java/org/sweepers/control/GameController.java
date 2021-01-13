package org.sweepers.control;

import org.sweepers.models.Level;
import org.sweepers.view.GameView;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.util.Pair;

/**
 * The controller handling the game part
 */
public class GameController {
    private static final int PREFFERED_CELL_SIZE = 32;

    @FXML
    private Canvas canvasLower;

    @FXML
    private Canvas canvasMiddle;

    @FXML
    private Canvas canvasUpper;

    @FXML
    private Canvas canvasFlag;

    @FXML
    private Label txtOverlay;

    // Model
    private Level level;

    private GameView gameView;
    private int cellWidth, cellHeight;
    private int canvasWidth, canvasHeight;
    private boolean gameInProgress = true;

    public GameController(Level level) {
        this.level = level;
    }

    @FXML
    private void initialize() {
        int screenWidth = (int) Screen.getPrimary().getBounds().getWidth() - 100;
        int screenHeight = (int) Screen.getPrimary().getBounds().getHeight() - 200;
        canvasWidth = level.getWidth() * PREFFERED_CELL_SIZE;
        canvasHeight = level.getHeight() * PREFFERED_CELL_SIZE;

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
        canvasFlag.setWidth(canvasWidth);
        canvasFlag.setHeight(canvasHeight);

        cellHeight = canvasHeight / level.getHeight();
        cellWidth = canvasWidth / level.getWidth();

        // Draw level
        gameView = new GameView(canvasLower.getGraphicsContext2D(), canvasMiddle.getGraphicsContext2D(),
                canvasUpper.getGraphicsContext2D(), canvasFlag.getGraphicsContext2D(), cellWidth, cellHeight, ratio);
        gameView.drawGridOverlay(canvasHeight, canvasWidth);

        // Add listeners to level and the mineless cells
        level.addCellListener(gameView::onCellUpdate);
        // level.addPropertyChangeListener("gameInProgress", gameView);
        
        
        canvasFlag.addEventHandler(MouseEvent.MOUSE_CLICKED, this::mouseClicked);
    }

    private void mouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            leftMouseClicked(event);
        } else if (event.getButton() == MouseButton.SECONDARY) {
            rightMouseClicked(event);
        }
    }

    private void leftMouseClicked(MouseEvent event) {
        if (!gameInProgress)
            return;

        double mouseX = event.getX();
        double mouseY = event.getY();
        int x = Math.floorDiv((int) mouseX, cellWidth);
        int y = Math.floorDiv((int) mouseY, cellHeight);

        if (!level.isInitialized()) {
            level.generateLevel(new Pair<Integer,Integer>(x, y));
            gameView.drawCells(level.getLevel(), canvasHeight, canvasWidth);
        }

        if (level.getLevel()[y][x].isFlagged()){
            return;
        }
        
        if (level.onClick(x, y)) {
            lose();
        }
        
        if (level.gameWon()) {
            win();
        }
    }

    private void rightMouseClicked(MouseEvent event) {
        if (!gameInProgress)
            return;

        double mouseX = event.getX();
        double mouseY = event.getY();
        int x = Math.floorDiv((int) mouseX, cellWidth);
        int y = Math.floorDiv((int) mouseY, cellHeight);

        if (!level.isInitialized()) {
            level.generateLevel();
            gameView.drawCells(level.getLevel(), canvasHeight, canvasWidth);
        }
            
        level.flag(x, y);
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
