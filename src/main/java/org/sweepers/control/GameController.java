package org.sweepers.control;

import java.io.FileNotFoundException;
import java.util.List;

import org.sweepers.Highscores;
import org.sweepers.Router;
import org.sweepers.models.Highscore;
import org.sweepers.models.Level;
import org.sweepers.view.GameAudio;
import org.sweepers.view.GameView;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

/**
 * The controller handling the game part
 */
public class GameController {
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

    @FXML
    private VBox boxZoom;

    @FXML
    private GridPane grid;

    // Model
    private Level level;

    private GameView gameView;
    private GameAudio gameAudio;
    private int cellSize;
    private Timeline timerTimeline;

    public GameController(Level level) {
        this.level = level;
    }

    @FXML
    private void initialize() {
        // Draw level
        gameView = new GameView(level, grid);
        cellSize = GameView.PREFFERED_CELL_SIZE;

        // Add listeners to level and the mineless cells
        level.addCellListener(gameView::onCellUpdate);
        level.isInitialized().addListener((c, arg1, arg2) -> {
            start();
        });
        start();

        gameView.getClickable().addEventHandler(MouseEvent.MOUSE_CLICKED, this::mouseClicked);

        initializeStatusBar();
        gameAudio = new GameAudio();
    }

    private void start() {
        if (!level.isInitialized().getValue())
            return;

        gameView.drawCells();
        startTimer();
    }

    private void initializeStatusBar() {
        txtFlags.textProperty().bind(level.getFlagged().asString());
        txtMines.textProperty().bind(level.getMines().asString());

        btnRestart.setOnAction(event -> {
            btnRestart.getScene().setRoot(Router.toStartscreen(getClass()));
        });
    }

    private void startTimer() {
        timerTimeline = new Timeline(new KeyFrame(Duration.seconds(0), event -> {
            long elapsedTime = level.getElapsedTime();
            long minutes = elapsedTime / 1000 / 60;
            long seconds = (elapsedTime / 1000) % 60;
            txtTimer.setText(String.format("%d:%02d", minutes, seconds));
        }), new KeyFrame(Duration.millis(10)));
        timerTimeline.setCycleCount(Animation.INDEFINITE);
        timerTimeline.play();
    }

    private void stopTimer() {
        timerTimeline.stop();
    }

    @FXML
    public void zoomIn() {
        gameView.zoom(0.2);
    }

    @FXML
    public void zoomOut() {
        gameView.zoom(-0.2);
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
        int x = Math.floorDiv((int) mouseX, cellSize);
        int y = Math.floorDiv((int) mouseY, cellSize);

        if (!level.isInitialized().get()) {
            level.generateLevel(new Pair<Integer, Integer>(x, y));
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
        int x = Math.floorDiv((int) mouseX, cellSize);
        int y = Math.floorDiv((int) mouseY, cellSize);

        if (!level.isInitialized().get()) {
            level.generateLevel();
        }

        level.flag(x, y);
    }

    private void win() {
        gameAudio.playWin();
        txtEnd.setText("You win!");
        txtEnd.getStyleClass().add("win");
        end();

        if (isHighscore())
            openNewHighscore();
    }

    private void lose() {
        gameAudio.playExplosion();
        txtEnd.setText("You lose!");
        txtEnd.getStyleClass().add("lose");
        end();
    }

    private void end() {
        stopTimer();
        gameView.revealAll();
        level.stop();
        boxEnd.setVisible(true);
        boxZoom.setVisible(false);
    }

    private boolean isHighscore() {
        try {
            List<Highscore> scores = Highscores.getInstance()
                    .getHighscore(level.getSizeSetting() + "_" + level.getDifficultySetting());
            return (scores.size() < 5
                    || level.getTotalTime() < scores.get(scores.size() - 1).time)
                    && level.getSizeSetting() != null
                    && level.getSizeSetting() != "Custom"
                    && level.getDifficultySetting() != null
                    && level.getDifficultySetting() != "Custom"
                    && level.getDifficultySetting() != "Laos";
        } catch (FileNotFoundException e) {   
            e.printStackTrace();
            return false;
        }
    }

    private void openNewHighscore() {
        Stage dialog = new Stage();
        dialog.setMinWidth(250);
        dialog.setMinHeight(380);
        dialog.setTitle("New Highscore");
        dialog.setScene(new Scene(Router.toNewHighscores(getClass(), level)));
        dialog.initOwner(btnRestart.getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.show();
    }
}
