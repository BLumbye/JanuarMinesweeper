package org.sweepers;

import org.sweepers.view.GameAudio;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Starts the minesweeper - laos edition game.
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Load font
        Font.loadFont(getClass().getResourceAsStream("/fonts/Whacky_Joe.ttf"), 12);

        // Start music!
        GameAudio.getInstance();

        // Add icon
        stage.getIcons().add(new Image("/sprites/bomb-outline.png"));

        // Set up stage
        stage.setTitle("Minesweeper");
        stage.setMinWidth(550);
        stage.setMinHeight(425);
        stage.setWidth(650);
        stage.setHeight(500);
        stage.setScene(new Scene(Router.toStartscreen()));
        stage.show();
    }

    @Override
    public void stop() {
        Highscores.getInstance().saveHighscores();
    }

    /**
     * Just calls the launch - which in turn calls the start function
     * @param args not used
     */
    public static void main(String[] args) {
        launch(args);
    }
}