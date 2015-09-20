import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    // Variables used in scene construction
    private final double MIN_WINDOW_HEIGHT = 400;
    private final double MIN_WINDOW_WIDTH = 650;
    private final double PREF_PADDING = 5;
    private final String TITLE_STAGE = "WhatToDo";
    private final String MESSAGE_WELCOME = "Welcome to WhatToDo.";

    // Create the controls
    TextField userInput;
    Label commandLabel;
    ScrollPane labelPane;

    // Class field for primaryStage
    Stage stage;

    @Override
    public void start(Stage primaryStage) {

        stage = primaryStage;

        // Initialize the scene
        Scene scene = initializeScene();

        // Set the stage
        stage.setScene(scene);
        stage.setTitle(TITLE_STAGE);
        stage.setMinHeight(MIN_WINDOW_HEIGHT);
        stage.setMinWidth(MIN_WINDOW_WIDTH);
        stage.show();
    }

    private Scene initializeScene() {
        // Set up the initial label
        commandLabel = new Label(MESSAGE_WELCOME);

        // Set up the VBox for Labels with entered text
        VBox outputLabels = new VBox(commandLabel);

        // Setting up the text field used for user input
        userInput = new TextField();
        userInput.requestFocus();
        userInput.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Create a new Label for the entered text
                        Label newInput = new Label(userInput.getText());
                        // Add the label to the VBox
                        outputLabels.getChildren().add(newInput);
                        // Clear the text field
                        userInput.setText("");
                    }
                }
        );

        // Set up the ScrollPane for the labels
        labelPane = new ScrollPane(outputLabels);
        labelPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        labelPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox.setMargin(labelPane, new Insets(0, PREF_PADDING, 0, PREF_PADDING));

        // Set labelPane to grow with stage resize
        VBox.setVgrow(labelPane, Priority.ALWAYS);
        // Add ChangeListener to change scroll position when height of outputLabels changes
        outputLabels.heightProperty().addListener(
                new ChangeListener() {
                    @Override
                    public void changed(
                            ObservableValue observable, Object oldValue, Object newValue) {
                        labelPane.setVvalue(labelPane.getHmax());
                    }
                }
        );

        // Add margins to the scene
        VBox.setMargin(outputLabels, new Insets(PREF_PADDING));
        VBox.setMargin(userInput, new Insets(PREF_PADDING));
        VBox pane = new VBox(labelPane, userInput);

        // Construct the scene
        return new Scene(pane);
    }
}
