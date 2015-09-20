import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class WhatToDo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    // Create the controls
    TextField textUserInput;
    Label labelOutput;

    // Class field for primaryStage
    Stage stage;

    @Override
    public void start(Stage primaryStage) {

        stage = primaryStage;

        // Set up the text field
        textUserInput = new TextField();
        textUserInput.requestFocus();
        textUserInput.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Display the entered text in labelOutput
                        labelOutput.setText(textUserInput.getText());
                        // Clear the text previously entered
                        textUserInput.setText("");
                    }
                }
        );

        // Set up the initial label
        labelOutput = new Label("Enter a command:");

        // Set up a spacer node for layout of text field
        Region spacer = new Region();

        // Add margins to the scene
        VBox.setMargin(labelOutput, new Insets(10));
        VBox.setMargin(textUserInput, new Insets(0, 10, 10, 10));
        VBox pane = new VBox(labelOutput, spacer, textUserInput);
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Construct the scene
        Scene scene = new Scene(pane, 600, 400);

        // Display the scene
        stage.setScene(scene);
        stage.setTitle("WhatToDo");
        stage.show();
    }
}
