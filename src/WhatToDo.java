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

import todo.*;

public class WhatToDo extends Application {

    // Variables used in scene construction
    private final double MIN_WINDOW_HEIGHT = 400;
    private final double MIN_WINDOW_WIDTH = 650;
    private final double PREF_PADDING = 5;
    private final String TITLE_STAGE = "WhatToDo";
    private final String MESSAGE_WELCOME = "Welcome to WhatToDo." + '\n' + " ";

    // Create the controls
    private TextField inputTextField;
    private Label welcomeLabel;
    private ScrollPane labelScrollPane;
    private VBox outputLabels;

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
        welcomeLabel = new Label(MESSAGE_WELCOME);

        // Set up the VBox for Labels with entered text
        outputLabels = new VBox(welcomeLabel);

        // Setting up the text field used for user input
        inputTextField = new TextField();
        inputTextField.requestFocus();
        inputTextField.setOnAction(new TextInputHandler());

        // Set up the ScrollPane for the labels
        labelScrollPane = new ScrollPane(outputLabels);
        labelScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        labelScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox.setMargin(labelScrollPane, new Insets(0, PREF_PADDING, 0, PREF_PADDING));

        // Set labelScrollPane to grow with stage resize
        VBox.setVgrow(labelScrollPane, Priority.ALWAYS);
        // Add ChangeListener to change scroll position when height of outputLabels changes
        outputLabels.heightProperty().addListener(new ScrollListener());

        // Add margins to the scene
        VBox.setMargin(outputLabels, new Insets(PREF_PADDING));
        VBox.setMargin(inputTextField, new Insets(PREF_PADDING));
        VBox pane = new VBox(labelScrollPane, inputTextField);

        // Construct the scene
        return new Scene(pane);
    }

    // private EventHandler and ChangeListener classes
    private class ScrollListener implements ChangeListener {
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            labelScrollPane.setVvalue(labelScrollPane.getVmax());
        }
    }

    private class TextInputHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {

            // Create a todo.Parser object to parse the input and return the command type
            Parser parser = new Parser();

            // Create a Logic object to perform the operation
            Logic logicUnit = new Logic();
            String returnMessage = logicUnit.runOperation(
                    parser.getCommandType(inputTextField.getText()));

            // Display the result notification message in the window
            outputLabels.getChildren().add(new Label(returnMessage));

            // Clear the text field
            inputTextField.setText("");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
