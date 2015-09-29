/**
 * This class contains the components and methods used to construct the GUI's
 * scroll view
 *
 * @author Adrian
 */

package gui;

import backend.Logic;
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

import java.util.ArrayList;

public class ScrollViewController {

    /* ================================================================================
     * Variables used in scene construction
     * ================================================================================
     */
    private static final String EMPTY_LINE = "\n" + " " + "\n";
    private static final String MESSAGE_WELCOME = "Welcome to WhatToDo." + EMPTY_LINE;
    private static final String VIEW_STATE = "scroll";

    private static final int INDEX_TASK = 0;
    private static final int INDEX_OPMSG = 0;
    private static final int INDEX_EVENT = 1;

    private static final double TAB_PADDING = 30;
    private static final double PREF_PADDING = 5;
    private static final double BUFFER_HORIZONTAL = 100;

    /* ================================================================================
     * JavaFX controls used in the scene
     * ================================================================================
     */
    private static TextField inputTextField;
    private static Label welcomeLabel;
    private static ScrollPane labelScrollPane;
    private static VBox outputLabels;

    /**
     * Creates the default scroll UI scene for the program used to display feedback
     * to the user when commands are entered.
     *
     * @return A Scene object which is used to set primaryStage
     */
    public static Scene initScrollView() {

        // Set up the initial label with the welcome message
        welcomeLabel = new Label(MESSAGE_WELCOME);

        // Set up a VBox to add labels
        outputLabels = new VBox(welcomeLabel);
        outputLabels.heightProperty().addListener(new ScrollListener());

        // Set up the text field to get user input
        inputTextField = new TextField();
        inputTextField.requestFocus();
        inputTextField.setOnAction(new TextInputHandler());

        // Wrap the VBox containing the output labels into a ScrollPane
        labelScrollPane = new ScrollPane(outputLabels);
        labelScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        labelScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        labelScrollPane.setFitToWidth(true);

        // Set the ScrollPane to fit the height of the window
        VBox.setVgrow(labelScrollPane, Priority.ALWAYS);

        // Adjust the layout by adding margins to the individual components
        VBox.setMargin(welcomeLabel, new Insets(PREF_PADDING, PREF_PADDING, 0, PREF_PADDING));
        VBox.setMargin(inputTextField, new Insets(PREF_PADDING));

        // Set up the final VBox that comprises the scene
        VBox pane = new VBox(labelScrollPane, inputTextField);

        // Construct the scene
        return new Scene(pane);
    }

    /**
     * Adds a node to the display VBox
     *
     * @param opReturnHBox
     *            The HBox to be added to the display VBox. Comprises the label with
     *            the operation return message from Logic.
     */
    public static void addToView(HBox opReturnHBox) {
        outputLabels.getChildren().add(opReturnHBox);
    }

    /* ================================================================================
     * private EventHandler and ChangeListener classes
     * ================================================================================
     */
    /**
     * Private class implementing ChangeListener.
     * Sets the current window position of the scrollbar to the bottommost vertical
     * position whenever the size of the scroll pane changes
     */
    private static class ScrollListener implements ChangeListener<Object> {
        @Override
        public void changed(ObservableValue<? extends Object> observable,
                            Object oldValue,
                            Object newValue) {
            labelScrollPane.setVvalue(labelScrollPane.getVmax());
        }
    }

    /**
     * Private class implementing EventHandler.
     * Sends the text input by the user as a String to the Logic component to work on
     * when the user presses the enter button
     */
    private static class TextInputHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {

            // Create the Logic component to receive user input
            Logic logicUnit = new Logic();

            // Determine if there is a need to switch scenes
            String userInput = inputTextField.getText();
            boolean swap = logicUnit.isSwapCommand(VIEW_STATE, userInput);

            ArrayList<String> returnMessages = logicUnit.runOperation(userInput);

            if (swap) {
                // Swap to split view and display the data
                MainApp.stage.setScene(MainApp.splitView);

                MainApp.stage.setWidth(MainApp.MIN_WINDOW_WIDTH + BUFFER_HORIZONTAL);
                MainApp.stage.setHeight(MainApp.MIN_WINDOW_HEIGHT);
                MainApp.stage.setMinWidth(MainApp.MIN_WINDOW_WIDTH + BUFFER_HORIZONTAL);
                MainApp.stage.setMinHeight(MainApp.MIN_WINDOW_HEIGHT);

                // Receive task and event information
                String taskReturnMessage = returnMessages.get(INDEX_TASK);
                String eventReturnMessage = returnMessages.get(INDEX_EVENT);

                // Wrap the return messages in UI controls for display
                Label taskReturnLabel = new Label(taskReturnMessage + EMPTY_LINE);
                Label eventReturnLabel = new Label(eventReturnMessage + EMPTY_LINE);

                taskReturnLabel.setWrapText(true);
                eventReturnLabel.setWrapText(true);

                HBox taskReturnHBox = new HBox(taskReturnLabel);
                HBox eventReturnHBox = new HBox(eventReturnLabel);

                HBox.setMargin(taskReturnLabel, new Insets(0, TAB_PADDING, 0, TAB_PADDING));
                HBox.setMargin(eventReturnLabel, new Insets(0, TAB_PADDING, 0, TAB_PADDING));

                SplitViewController.addToView(taskReturnHBox, eventReturnHBox);
            } else {
                // Display the operation return message in scroll view
                String opReturnMessage = returnMessages.get(INDEX_OPMSG);

                // Wrap the return messages in UI controls for display
                Label opReturnLabel = new Label(opReturnMessage + EMPTY_LINE);
                opReturnLabel.setWrapText(true);
                HBox opReturnHBox = new HBox(opReturnLabel);
                HBox.setMargin(opReturnLabel, new Insets(0, TAB_PADDING, 0, TAB_PADDING));

                // Add the operation return message to the display
                outputLabels.getChildren().add(opReturnHBox);
            }
            // Clear the text field
            inputTextField.setText("");
        }
    }
}
