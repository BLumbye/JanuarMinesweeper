package org.sweepers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.sweepers.control.GameController;
import org.sweepers.models.Level;

import java.net.URL;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Instantiate level model
        Level level = new Level(8, 8, 10);

        // Set up controllers
        GameController gameController = new GameController(level);

        // Create root element
        StackPane root = new StackPane();

        // Load FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GameView.fxml"));
        loader.setController(gameController);
        root.getChildren().add(loader.load());

        // Create scene
        Scene scene = new Scene(root);
        scene.setFill(Color.WHITE);

        stage.setTitle("Minesweeper");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}