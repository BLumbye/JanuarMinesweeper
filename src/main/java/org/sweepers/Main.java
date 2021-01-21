package org.sweepers;

import org.sweepers.view.GameAudio;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Load font
        Font.loadFont(getClass().getResourceAsStream("/fonts/Whacky_Joe.ttf"), 12);

        // Start music!
        GameAudio.getInstance();

        // Set up stage
        stage.setTitle("Minesweeper");
        stage.setMinWidth(475);
        stage.setMinHeight(425);
        stage.setWidth(650);
        stage.setHeight(500);
        stage.setScene(new Scene(Router.toStartscreen(getClass())));
        stage.show();
    }

    @Override
    public void stop() {
        Highscores.getInstance().saveHighscores();
    }

    public static void main(String[] args) {
        launch(args);
    }
}