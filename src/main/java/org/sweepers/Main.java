package org.sweepers;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Minesweeper");
        stage.setMinWidth(475);
        stage.setMinHeight(425);
        stage.setWidth(600);
        stage.setHeight(400);
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