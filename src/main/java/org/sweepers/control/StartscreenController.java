package org.sweepers.control;

import java.util.Map;
import java.util.function.UnaryOperator;

import org.sweepers.Router;
import org.sweepers.models.Level;
import org.sweepers.view.Animations;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;

public class StartscreenController {
    @FXML
    private Button btnSizeSmall;
    @FXML
    private Button btnSizeMedium;
    @FXML
    private Button btnSizeLarge;
    @FXML
    private Button btnSizeCustom;

    @FXML
    private Button btnDifficultyEasy;
    @FXML
    private Button btnDifficultyMedium;
    @FXML
    private Button btnDifficultyHard;
    @FXML
    private Text txtLaos;
    @FXML
    private Button btnDifficultyCustom;

    @FXML
    private TextField fieldWidth;
    @FXML
    private TextField fieldHeight;
    @FXML
    private TextField fieldMines;

    @FXML
    private Circle circle;
    @FXML
    private StackPane stackc;

    @FXML
    private Group grpTitle;

    private int width, height, mines;
    private double percentageMines;

    private String sizeSetting;
    private String difficultySetting;

    public static final Map<String,Pair<Integer,Integer>> SIZES = Map.of(
        "Small", new Pair<Integer,Integer>(9,9),
        "Medium", new Pair<Integer,Integer>(16,16),
        "Large", new Pair<Integer,Integer>(30,16)
    );

    public static final Map<String,Double> DIFFICULTIES = Map.of(
        "Easy", 10.0 / (9 * 9),
        "Medium", 40.0 / (16 * 16),
        "Hard", 100.0 / (30 * 16),
        "Laos", 0.8
    );

    @FXML
    public void initialize() {
        // Set standard values: small - easy
        sizeButton("Small", btnSizeSmall);
        difficultyButton("Easy", btnDifficultyEasy);

        // Add listeners to size buttons
        btnSizeSmall.setOnAction(event -> { sizeButton("Small", btnSizeSmall); });
        btnSizeMedium.setOnAction(event -> { sizeButton("Medium", btnSizeMedium); });
        btnSizeLarge.setOnAction(event -> { sizeButton("Large", btnSizeLarge); });
        btnSizeCustom.setOnAction(event -> { sizeButton("Custom", btnSizeCustom); });

        // Add listeners to difficulty buttons
        btnDifficultyEasy.setOnAction(event -> { difficultyButton("Easy", btnDifficultyEasy); });
        btnDifficultyMedium.setOnAction(event -> { difficultyButton("Medium", btnDifficultyMedium); });
        btnDifficultyHard.setOnAction(event -> { difficultyButton("Hard", btnDifficultyHard); });
        txtLaos.setOnMouseClicked(event -> { difficultyButton("Laos", null); });
        btnDifficultyCustom.setOnAction(event -> { difficultyButton("Custom", btnDifficultyCustom); });

        // Allow only numbers in fields
        UnaryOperator<Change> numberFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[0-9]*")) {
                return change;
            }
            return null;
        };
        fieldWidth.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), width, numberFilter));
        fieldHeight.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), height, numberFilter));
        fieldMines.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), mines, numberFilter));

        // Clamp numbers in fields
        fieldWidth.focusedProperty().addListener((arg0, oldValue, newValue) -> { if (!newValue) { validateFields(); } });
        fieldHeight.focusedProperty().addListener((arg0, oldValue, newValue) -> { if (!newValue) { validateFields(); } });
        fieldMines.focusedProperty().addListener((arg0, oldValue, newValue) -> { if (!newValue) { validateFields(); } });

        // Resize middle circle
        circle.radiusProperty().bind(stackc.heightProperty().multiply(0.4));

        Animations.titleAnimation(grpTitle);
    }

    private void sizeButton(String key, Button btn) {
        sizeSetting = key;

        if (key.equals("Custom")) {
            fieldHeight.setDisable(false);
            fieldWidth.setDisable(false);
        } else {
            Pair<Integer,Integer> size = SIZES.get(key);
            setSize(size.getKey(), size.getValue());
        }

        btnSizeSmall.getStyleClass().remove("selected");
        btnSizeMedium.getStyleClass().remove("selected");
        btnSizeLarge.getStyleClass().remove("selected");
        btnSizeCustom.getStyleClass().remove("selected");
        if (btn != null) btn.getStyleClass().add("selected");
    }

    private void difficultyButton(String key, Button btn) {
        difficultySetting = key;

        if (key.equals("Custom")) {
            fieldMines.setDisable(false);
        } else {
            setDifficulty(DIFFICULTIES.get(key));
        }

        btnDifficultyEasy.getStyleClass().remove("selected");
        btnDifficultyMedium.getStyleClass().remove("selected");
        btnDifficultyHard.getStyleClass().remove("selected");
        btnDifficultyCustom.getStyleClass().remove("selected");
        if (btn != null) btn.getStyleClass().add("selected");
    }

    @FXML
    private void startGame(ActionEvent event) {
        validateFields();
        Level level = new Level(width, height, mines, sizeSetting, difficultySetting);
        txtLaos.getScene().setRoot(Router.toGame(getClass(), level));
    }

    @FXML
    private void quitGame(ActionEvent event) {
        Stage stage = (Stage) txtLaos.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void highscores(ActionEvent event) {
        txtLaos.getScene().setRoot(Router.toHighscores(getClass()));
    }

    private void validateFields() {
        // Validate width
        int widthVal = clamp(Integer.parseInt(fieldWidth.getText()), 4, 100);
        fieldWidth.setText(String.valueOf(widthVal));
        width = widthVal;

        // Validate height
        int heightVal = clamp(Integer.parseInt(fieldHeight.getText()), 4, 100);
        fieldHeight.setText(String.valueOf(heightVal));
        height = heightVal;

        // Validate mines
        updateMines();
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        updateMines();
        fieldWidth.setDisable(true);
        fieldWidth.setText(String.valueOf(width));
        fieldHeight.setDisable(true);
        fieldHeight.setText(String.valueOf(height));
    }

    public void setDifficulty(double percentage) {
        percentageMines = percentage;
        updateMines();
        fieldMines.setDisable(true);
    }

    private void updateMines() {
        if (fieldMines.isDisable()) {
            mines = (int) Math.round(width * height * percentageMines);
            fieldMines.setText(String.valueOf(mines));
        } else {
            int val = clamp(Integer.parseInt(fieldMines.getText()), 1, width * height - 10);
            fieldMines.setText(String.valueOf(val));
            mines = val;
        }
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}