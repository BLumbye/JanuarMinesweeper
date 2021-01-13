package org.sweepers.control;

import java.io.IOException;
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
import javafx.stage.Stage;
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

  @FXML
  public void initialize() {
    // Set standard values: small - easy
    setSize(9, 9);
    setDifficulty(0.12345);

    // Add listeners to size buttons
    btnSizeSmall.setOnAction(event -> {
      setSize(9, 9);
    });
    btnSizeMedium.setOnAction(event -> {
      setSize(16, 16);
    });
    btnSizeLarge.setOnAction(event -> {
      setSize(30, 16);
    });
    btnSizeCustom.setOnAction(event -> {
      fieldHeight.setDisable(false);
      fieldWidth.setDisable(false);
    });

    // Add listeners to difficulty buttons
    btnDifficultyEasy.setOnAction(event -> {
      setDifficulty(0.12345);
    });
    btnDifficultyMedium.setOnAction(event -> {
      setDifficulty(0.15625);
    });
    btnDifficultyHard.setOnAction(event -> {
      setDifficulty(0.20625);
    });
    btnDifficultyCustom.setOnAction(event -> {
      fieldMines.setDisable(false);
    });

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
    fieldWidth.focusedProperty().addListener((arg0, oldValue, newValue) -> {
      if (!newValue) { // On focus lost
        int val = Math.max(4, Math.min(100, Integer.parseInt(fieldWidth.getText())));
        fieldWidth.setText(String.valueOf(val));
        width = val;
      }
    });
    fieldHeight.focusedProperty().addListener((arg0, oldValue, newValue) -> {
      if (!newValue) { // On focus lost
        int val = Math.max(4, Math.min(100, Integer.parseInt(fieldHeight.getText())));
        fieldHeight.setText(String.valueOf(val));
        height = val;
      }
    });
    fieldMines.focusedProperty().addListener((arg0, oldValue, newValue) -> {
      if (!newValue) { // On focus lost
        int val = Math.max(1, Math.min(width * height, Integer.parseInt(fieldMines.getText())));
        fieldMines.setText(String.valueOf(val));
        mines = val;
      }
    });

    // Start button
    btnStart.setOnAction(this::startGame);
  }

  public void startGame(ActionEvent event) {
    try {
      Level level = new Level(width, height, mines);
      GameController gameController = new GameController(level);
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/GameView.fxml"));
      loader.setController(gameController);
      Scene scene = new Scene(loader.load());
      scene.getStylesheets().add(getClass().getResource("/GameView.css").toExternalForm());
      Stage stage = (Stage) btnStart.getScene().getWindow();
      stage.setScene(scene);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
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
    mines = (int) Math.round(width * height * percentageMines);
    fieldMines.setText(String.valueOf(mines));
  }
}
