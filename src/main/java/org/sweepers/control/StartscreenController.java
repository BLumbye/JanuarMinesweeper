package org.sweepers.control;

import java.io.IOException;
import java.util.Map;
import java.util.function.UnaryOperator;

import org.sweepers.models.Level;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
  private Button btnStart;

  private int width, height, mines;
  private double percentageMines;

  private static final Map<String,Pair<Integer,Integer>> SIZES = Map.of(
    "SMALL", new Pair<Integer,Integer>(9,9),
    "MEDIUM", new Pair<Integer,Integer>(16,16),
    "LARGE", new Pair<Integer,Integer>(30,16)
  );

  private static final Map<String,Double> DIFFICULTIES = Map.of(
    "EASY", 10.0 / (9 * 9),
    "MEDIUM", 40.0 / (16 * 16),
    "HARD", 100.0 / (30 * 16),
    "LAOS", 0.8
  );

  @FXML
  public void initialize() {
    // Set standard values: small - easy
    sizeButton("SMALL", btnSizeSmall);
    difficultyButton("EASY", btnDifficultyEasy);

    // Add listeners to size buttons
    btnSizeSmall.setOnAction(event -> { sizeButton("SMALL", btnSizeSmall); });
    btnSizeMedium.setOnAction(event -> { sizeButton("MEDIUM", btnSizeMedium); });
    btnSizeLarge.setOnAction(event -> { sizeButton("LARGE", btnSizeLarge); });
    btnSizeCustom.setOnAction(event -> { sizeButton("CUSTOM", btnSizeCustom); });

    // Add listeners to difficulty buttons
    btnDifficultyEasy.setOnAction(event -> { difficultyButton("EASY", btnDifficultyEasy); });
    btnDifficultyMedium.setOnAction(event -> { difficultyButton("MEDIUM", btnDifficultyMedium); });
    btnDifficultyHard.setOnAction(event -> { difficultyButton("HARD", btnDifficultyHard); });
    txtLaos.setOnMouseClicked(event -> { difficultyButton("LAOS", null); });
    btnDifficultyCustom.setOnAction(event -> { difficultyButton("CUSTOM", btnDifficultyCustom); });

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

    // Start button
    btnStart.setOnAction(this::startGame);
  }

  public void sizeButton(String key, Button btn) {
    if (key.equals("CUSTOM")) {
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

  public void difficultyButton(String key, Button btn) {
    if (key.equals("CUSTOM")) {
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

  public void startGame(ActionEvent event) {
    try {
      validateFields();
      Level level = new Level(width, height, mines);
      GameController gameController = new GameController(level);
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/GameView.fxml"));
      loader.setController(gameController);
      Scene scene = new Scene(loader.load());
      // scene.getStylesheets().add(getClass().getResource("/GameView.css").toExternalForm());
      Stage stage = (Stage) btnStart.getScene().getWindow();
      stage.setScene(scene);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void validateFields() {
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

  public void updateMines() {
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