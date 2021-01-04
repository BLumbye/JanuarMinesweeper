package org.sweepers;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setWidth(400);
        stage.setHeight(400);

        // StackPane as root node
        StackPane stackPane = new StackPane();

        // Add labels
        HBox hBox = new HBox(8);

        Label label1 = new Label("Some text");
        Label label2 = new Label("More text");
        Button button = new Button("Wups!");
        button.setOnAction(event -> {
            System.out.println(event);
        });
        hBox.getChildren().addAll(label1, label2, button);

        stackPane.getChildren().add(hBox);

        // Create scene
        Scene scene = new Scene(stackPane);
        scene.setFill(Color.BLANCHEDALMOND);

        stage.setTitle("Hello World!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}