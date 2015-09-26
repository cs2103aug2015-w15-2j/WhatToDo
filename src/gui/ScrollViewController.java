package gui;

import backend.Logic;
import backend.Parser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ScrollViewController {

    // Variables used in scene construction
    private static final String EMPTY_LINE = "\n" + " " + "\n";

    private static final double TAB_PADDING = 30;
    private static final double PREF_PADDING = 5;

    private static final String MESSAGE_WELCOME = "Welcome to WhatToDo." + EMPTY_LINE;

    // Create the controls
    private static TextField inputTextField;
    private static Label welcomeLabel;
    private static ScrollPane labelScrollPane;
    private static VBox outputLabels;

    /**
     * Creates the default UI scene for the program from UI controls
     *
     * @return A Scene object which is used to set primaryStage
     */
    public static Scene initScrollView() {
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
        return new Scene(pane, MainApp.MIN_WINDOW_WIDTH, MainApp.MIN_WINDOW_HEIGHT);
    }

    /* ================================================================================
     * private EventHandler and ChangeListener classes
     * ================================================================================
     */

    /*
     * ChangeListener implementation.
     * Checks for when the text output height exceeds window height
     */
    private static class ScrollListener implements ChangeListener<Object> {
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
    private static class TextInputHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {

            // Create a Logic object to receive the user input and send to Parser
            Logic logicUnit = new Logic();
            String returnMessage = logicUnit.runOperation(inputTextField.getText());

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
}
