package org.sweepers;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Minesweeper");
        stage.setResizable(false);
        stage.setMinWidth(200);
        stage.setScene(Router.toStartscreen(getClass()));
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