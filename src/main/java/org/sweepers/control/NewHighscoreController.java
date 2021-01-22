package org.sweepers.control;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import org.sweepers.Highscores;
import org.sweepers.models.Highscore;
import org.sweepers.models.Level;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The controller handling the new high score popup.
 */
public class NewHighscoreController {
    @FXML
    private TextField fieldName;

    @FXML
    private VBox boxScores;

    private Level level;
    private Highscores highscores;

    /**
     * @param level the level with the data for the high score
     */
    public NewHighscoreController(Level level) {
        this.level = level;
        highscores = Highscores.getInstance();
    }

    @FXML
    private void initialize() {
        // Create labels
        try {
            List<Highscore> scores = new ArrayList<>(
                    highscores.getHighscore(level.getSizeSetting() + "_" + level.getDifficultySetting()));
            scores.add(new Highscore("YOU", level.getTotalTime()));
            scores.sort((e1, e2) -> (int) (e1.time - e2.time));
            if (scores.size() > 5)
                scores.remove(5);

            for (int i = 0; i < 5; i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Score.fxml"));
                HBox label = loader.load();
                ((Text) label.lookup("#txtNumber")).setText((i + 1) + ".");

                if (scores.size() > i) {
                    Highscore score = scores.get(i);
                    ((Text) label.lookup("#txtName")).setText(score.name);

                    // Calculate the time in minutes and seconds
                    long minutes = score.time / 1000 / 60;
                    long seconds = (score.time / 1000) % 60;
                    ((Text) label.lookup("#txtTime")).setText(String.format("%d:%02d", minutes, seconds));

                    if (score.name.equals("YOU") && score.time == level.getTotalTime()) {
                        label.getStyleClass().add("you");
                    }
                } else {
                    ((Text) label.lookup("#txtName")).setText("----");
                    ((Text) label.lookup("#txtTime")).setText("--");
                }

                boxScores.getChildren().add(label);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create filter for text field
        UnaryOperator<Change> textFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z0-9_.-]{0,4}")) {
                return change;
            }
            return null;
        };
        fieldName.setTextFormatter(new TextFormatter<String>(textFilter));
    }

    @FXML
    private void cancel() {
        Stage stage = (Stage) fieldName.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void save() {
        try {
            highscores.setHighscore(level.getSizeSetting() + "_" + level.getDifficultySetting(),
                    new Highscore(fieldName.getText(), level.getTotalTime()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        cancel();
    }
}