package org.sweepers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.sweepers.control.GameController;
import org.sweepers.models.Level;

public class Main extends Application {
    private static int width, height, mines;

    @Override
    public void start(Stage stage) throws Exception {
        // Instantiate level model
        Level level = new Level(width, height, mines);

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
        scene.getStylesheets().add(getClass().getResource("/GameView.css").toExternalForm());
        scene.setFill(Color.WHITE);

        stage.setTitle("Minesweeper");
        stage.setResizable(false);
        stage.setMinWidth(200);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        width = Integer.parseInt(args[0]);
        height = Integer.parseInt(args[1]);
        mines = Integer.parseInt(args[2]);
        launch(args);
    }

}