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
    private final String EMPTY_LINE = "\n" + " " + "\n";
    private final double TAB_PADDING = 30;
    private final double MIN_WINDOW_HEIGHT = 400;
    private final double MIN_WINDOW_WIDTH = 650;
    private final double PREF_PADDING = 5;
    private final String TITLE_STAGE = "WhatToDo";
    private final String MESSAGE_WELCOME = "Welcome to WhatToDo." + EMPTY_LINE;

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
        stage.setHeight(MIN_WINDOW_HEIGHT);
        stage.setWidth(MIN_WINDOW_WIDTH);

        stage.setMinHeight(MIN_WINDOW_HEIGHT);
        stage.setMinWidth(MIN_WINDOW_WIDTH);
        stage.show();
    }

    /**
     * Creates the default UI scene for the program from UI controls
     *
     * @return A Scene object which is used to set primaryStage
     */
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
        labelScrollPane.setFitToWidth(true);

        // Set labelScrollPane to grow with stage resize
        VBox.setVgrow(labelScrollPane, Priority.ALWAYS);
        // Add ChangeListener to change scroll position when height of outputLabels changes
        outputLabels.heightProperty().addListener(new ScrollListener());

        // Add margins to the scene
        VBox.setMargin(welcomeLabel, new Insets(PREF_PADDING, PREF_PADDING, 0, PREF_PADDING));
        VBox.setMargin(inputTextField, new Insets(PREF_PADDING));
        VBox pane = new VBox(labelScrollPane, inputTextField);

        // Construct the scene
        return new Scene(pane, MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT);
    }

    // -----------------private EventHandler and ChangeListener classes-----------------

    /*
     * ChangeListener implementation.
     * Checks for when the text output height exceeds window height
     */
    private class ScrollListener implements ChangeListener<Object> {
        @Override
        public void changed(ObservableValue<? extends Object> observable,
                            Object oldValue,
                            Object newValue) {
            labelScrollPane.setVvalue(labelScrollPane.getVmax());
        }
    }

    /*
     * EventHandler implementation.
     * Checks for when the user presses the Enter key to send information to the program
     */
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
            Label returnMsgLabel = new Label(returnMessage + EMPTY_LINE);
            returnMsgLabel.setWrapText(true);
            HBox returnMsgHBox = new HBox(returnMsgLabel);
            HBox.setMargin(returnMsgLabel, new Insets(0, TAB_PADDING, 0, TAB_PADDING));

            outputLabels.getChildren().add(returnMsgHBox);

            // Clear the text field
            inputTextField.setText("");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
