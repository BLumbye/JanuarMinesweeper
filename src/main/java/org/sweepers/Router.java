package org.sweepers;

import java.io.IOException;

import org.sweepers.control.GameController;
import org.sweepers.control.HighscoreController;
import org.sweepers.control.NewHighscoreController;
import org.sweepers.control.StartscreenController;
import org.sweepers.models.Level;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * A helper class that returns the root node for every scene in the game. It
 * also loads and connects the associated controllers.
 */
public class Router {
    /**
     * @return the root node for the start screen scene
     */
    public static Parent toStartscreen() {
        // Set up controllers
        StartscreenController startscreenController = new StartscreenController();

        // Load FXML
        FXMLLoader loader = new FXMLLoader(Router.class.getResource("/fxml/Startscreen.fxml"));
        loader.setController(startscreenController);

        // Create scene
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return the root node for the high score scene
     */
    public static Parent toHighscores() {
        // Set up controllers
        HighscoreController highscoreController = new HighscoreController();

        // Load FXML
        FXMLLoader loader = new FXMLLoader(Router.class.getResource("/fxml/Highscores.fxml"));
        loader.setController(highscoreController);

        // Create scene
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param level the level object to save the high score for
     * @return the root node for the new high score popup
     */
    public static Parent toNewHighscores(Level level) {
        // Set up controllers
        NewHighscoreController newHighscoreController = new NewHighscoreController(level);

        // Load FXML
        FXMLLoader loader = new FXMLLoader(Router.class.getResource("/fxml/NewHighscore.fxml"));
        loader.setController(newHighscoreController);

        // Create scene
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param level the level object that should be played
     * @return the root node for the game scene
     */
    public static Parent toGame(Level level) {
        // Set up controllers
        GameController gameController = new GameController(level);

        // Load FXML
        FXMLLoader loader = new FXMLLoader(Router.class.getResource("/fxml/GameView.fxml"));
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
