package org.sweepers;

import java.io.IOException;

import org.sweepers.control.GameController;
import org.sweepers.control.HighscoreController;
import org.sweepers.control.NewHighscoreController;
import org.sweepers.control.StartscreenController;
import org.sweepers.models.Level;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Router {
    public static Parent toStartscreen(Class<?> clazz) {
        // Set up controllers
        StartscreenController startscreenController = new StartscreenController();

        // Load FXML
        FXMLLoader loader = new FXMLLoader(clazz.getResource("/fxml/Startscreen.fxml"));
        loader.setController(startscreenController);

        // Create scene
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Parent toHighscores(Class<?> clazz) {
        // Set up controllers
        HighscoreController highscoreController = new HighscoreController();

        // Load FXML
        FXMLLoader loader = new FXMLLoader(clazz.getResource("/fxml/Highscores.fxml"));
        loader.setController(highscoreController);

        // Create scene
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Parent toNewHighscores(Class<?> clazz, Level level) {
        // Set up controllers
        NewHighscoreController newHighscoreController = new NewHighscoreController(level);

        // Load FXML
        FXMLLoader loader = new FXMLLoader(clazz.getResource("/fxml/NewHighscore.fxml"));
        loader.setController(newHighscoreController);

        // Create scene
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Parent toGame(Class<?> clazz, Level level) {
        // Set up controllers
        GameController gameController = new GameController(level);

        // Load FXML
        FXMLLoader loader = new FXMLLoader(clazz.getResource("/fxml/GameView.fxml"));
        loader.setController(gameController);

        // Create scene
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
