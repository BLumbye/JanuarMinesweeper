package org.sweepers.control;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sweepers.Highscores;
import org.sweepers.Router;
import org.sweepers.models.Highscore;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HighscoreController {
    @FXML
    private ComboBox<String> comboSize;
    @FXML
    private ComboBox<String> comboDifficulty;

    @FXML
    private VBox boxScores;

    HBox[] labels;

    Highscores highscores;

    public HighscoreController() {
        labels = new HBox[5];
        highscores = highscores.getInstance();
    }

    @FXML
    public void initialize() {
        // Populate and set combo boxes
        comboSize.getItems().addAll("Small", "Medium", "Large");
        comboSize.getSelectionModel().select("Small");
        comboDifficulty.getItems().addAll("Easy", "Medium", "Hard");
        comboDifficulty.getSelectionModel().select("Easy");

        // Create labels
        for (int i = 0; i < 5; i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Score.fxml"));
                HBox label = loader.load();
                ((Text) label.lookup("#txtNumber")).setText((i + 1) + ".");
                labels[i] = label;
                boxScores.getChildren().add(label);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        updateScores(null);
    }

    @FXML
    private void updateScores(ActionEvent event) {
        String size = comboSize.getValue();
        String difficulty = comboDifficulty.getValue();

        try {
            List<Highscore> scores = highscores.getHighscore(size + "_" + difficulty);
            for (int i = 0; i < 5; i++) {
                HBox label = labels[i];
                if (scores.size() > i) {
                    Highscore score = scores.get(i);
                    ((Text) label.lookup("#txtName")).setText(score.name);
                    long minutes = score.time / 1000 / 60;
                    long seconds = (score.time / 1000) % 60;
                    ((Text) label.lookup("#txtTime")).setText(String.format("%d:%02d", minutes, seconds));
                } else {
                    ((Text) label.lookup("#txtName")).setText("----");
                    ((Text) label.lookup("#txtTime")).setText("--");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void back() {
        Stage stage = (Stage) boxScores.getScene().getWindow();
        stage.setScene(Router.toStartscreen(getClass()));
    }
}
