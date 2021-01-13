package org.sweepers;

import org.sweepers.control.StartscreenController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Set up controllers
        StartscreenController startscreenController = new StartscreenController();
        
        // Load FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Startscreen.fxml"));
        loader.setController(startscreenController);

        // Create scene
        Scene scene = new Scene(loader.load());

        stage.setTitle("Minesweeper");
        stage.setResizable(false);
        stage.setMinWidth(200);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}