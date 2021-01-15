package org.sweepers.control;

import org.sweepers.models.Level;
import org.sweepers.view.GameView;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
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
    private Text txtEnd;

    @FXML
    private Text txtFlags;

    @FXML
    private Text txtMines;

    @FXML
    private Text txtTimer;

    @FXML
    private Button btnRestart;

    @FXML
    private VBox boxEnd;

    // Model
    private Level level;

    private GameView gameView;
    private int cellWidth, cellHeight;
    private int canvasWidth, canvasHeight;
    private Timeline timerTimeline;

    public GameController(Level level) {
        this.level = level;
    }

    @FXML
    private void initialize() {
        double ratio = resizeGame();

        // Draw level
        gameView = new GameView(canvasLower.getGraphicsContext2D(), canvasMiddle.getGraphicsContext2D(),
                canvasUpper.getGraphicsContext2D(), canvasFlag.getGraphicsContext2D(), cellWidth, cellHeight, ratio);
        gameView.drawGridAndOverlay(canvasHeight, canvasWidth);

        // Add listeners to level and the mineless cells
        level.addCellListener(gameView::onCellUpdate);
        level.isInitialized().addListener((c, arg1, arg2) -> { start(); });
        start();
        
        canvasFlag.addEventHandler(MouseEvent.MOUSE_CLICKED, this::mouseClicked);

        initializeStatusBar();
    }

    private void start() {
        if (!level.isInitialized().getValue()) return;
        
        gameView.drawCells(level.getLevel(), canvasHeight, canvasWidth);
        startTimer();
    }

    private double resizeGame() {
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
        return ratio;
    }

    private void initializeStatusBar() {
        txtFlags.textProperty().bind(level.getFlagged().asString());
        txtMines.textProperty().bind(level.getMines().asString());

        btnRestart.setOnAction(event -> {
            try {
                // Set up controllers
                StartscreenController startscreenController = new StartscreenController();
                
                // Load FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Startscreen.fxml"));
                loader.setController(startscreenController);

                // Create scene
                Scene scene = new Scene(loader.load());

                Stage stage = (Stage) btnRestart.getScene().getWindow();
                stage.setScene(scene);
            } catch (Exception e) {
                //TODO: handle exception
            }
        });
    }

    private void startTimer() {
        timerTimeline = new Timeline(
            new KeyFrame(Duration.seconds(0), event -> {
                long elapsedTime = level.getElapsedTime();
                long minutes = elapsedTime / 1000 / 60;
                long seconds = (elapsedTime / 1000) % 60;
                txtTimer.setText(String.format("%d:%02d", minutes, seconds));
            }),
            new KeyFrame(Duration.millis(200))
        );
        timerTimeline.setCycleCount(Animation.INDEFINITE);
        timerTimeline.play();
    }

    private void stopTimer() {
        timerTimeline.stop();
    }

    private void mouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            leftMouseClicked(event);
        } else if (event.getButton() == MouseButton.SECONDARY) {
            rightMouseClicked(event);
        }
    }

    private void leftMouseClicked(MouseEvent event) {
        if (!level.isInProgress())
            return;

        double mouseX = event.getX();
        double mouseY = event.getY();
        int x = Math.floorDiv((int) mouseX, cellWidth);
        int y = Math.floorDiv((int) mouseY, cellHeight);

        if (!level.isInitialized().get()) {
            level.generateLevel(new Pair<Integer,Integer>(x, y));
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
        if (!level.isInProgress())
            return;

        double mouseX = event.getX();
        double mouseY = event.getY();
        int x = Math.floorDiv((int) mouseX, cellWidth);
        int y = Math.floorDiv((int) mouseY, cellHeight);

        if (!level.isInitialized().get()) {
            level.generateLevel();
        }
        if (!level.getLevel()[y][x].isRevealed())    
        level.flag(x, y);
    }

    private void win() {
        txtEnd.setText("You win!");
        txtEnd.getStyleClass().add("win");
        end();
    }

    private void lose() {
        txtEnd.setText("You lose!");
        txtEnd.getStyleClass().add("lose");
        end();
    }

    private void end() {
        stopTimer();
        gameView.revealAll();
        level.stop();
        boxEnd.setVisible(true);
    }
}
