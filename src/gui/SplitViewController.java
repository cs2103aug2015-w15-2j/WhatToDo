/**
 * This class contains the components and methods used to construct the GUI's
 * split view
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

public class SplitViewController {

    /* ================================================================================
     * Variables used in scene construction
     * ================================================================================
     */
    private static final String EMPTY_LINE = "\n" + " " + "\n";
    private static final String MESSAGE_TASK = "Upcoming Tasks: " + EMPTY_LINE;
    private static final String MESSAGE_EVENT = "Upcoming Events: " + EMPTY_LINE;
    private static final String VIEW_STATE = "split";

    private static final int INDEX_TASK = 0;
    private static final int INDEX_OPMSG = 0;
    private static final int INDEX_EVENT = 1;

    private static final double TAB_PADDING = 30;
    private static final double PREF_PADDING = 5;

    /* ================================================================================
     * JavaFX controls used in the scene
     * ================================================================================
     */
    private static TextField inputTextField;
    private static Label taskLabel, eventLabel;
    private static HBox scrollPanes;
    private static ScrollPane taskScrollPane, eventScrollPane;
    private static VBox taskLabels, eventLabels;

    /**
     * Creates a split UI scene for the program used to display both tasks and events
     * concurrently to the user when the "search" command is entered.
     *
     * @return A Scene object which is used to set primaryStage
     */
    public static Scene initSplitView() {

        // Set up the title labels for both scroll panes
        taskLabel = new Label(MESSAGE_TASK);
        eventLabel = new Label(MESSAGE_EVENT);

        // Set up the text field to get user input
        inputTextField = new TextField();
        inputTextField.requestFocus();
        inputTextField.setOnAction(new TextEventHandler());

        /* ================================================================================
         * Building the Task display ScrollPane
         * ================================================================================
         */
        // Set up a VBox to add Tasks
        taskLabels = new VBox(taskLabel);
        taskLabels.heightProperty().addListener(new ScrollListener());

        // Wrap the VBox containing Tasks into a ScrollPane
        taskScrollPane = new ScrollPane(taskLabels);
        taskScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        taskScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        taskScrollPane.setFitToWidth(true);

        /* ================================================================================
         * Building the Event display ScrollPane
         * ================================================================================
         */
        // Set up a VBox to add Events
        eventLabels = new VBox(eventLabel);
        eventLabels.heightProperty().addListener(new ScrollListener());

        // Wrap the VBox containing Events into a ScrollPane
        eventScrollPane = new ScrollPane(eventLabels);
        eventScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        eventScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        eventScrollPane.setFitToWidth(true);

        // Combine the Task and Event ScrollPanes into a HBox
        scrollPanes = new HBox();
        scrollPanes.getChildren().addAll(taskScrollPane, eventScrollPane);

        // Set taskScrollPane and eventScrollPane to fill the window equally
        HBox.setHgrow(taskScrollPane, Priority.ALWAYS);
        HBox.setHgrow(eventScrollPane, Priority.ALWAYS);

        // Set scrollPanes to fit the window height
        VBox.setVgrow(scrollPanes, Priority.ALWAYS);

        /* ================================================================================
         * Building the overall scene
         * ================================================================================
         */
        // Adjust layout by setting margins for the individual components
        VBox.setMargin(taskLabel, new Insets(PREF_PADDING, PREF_PADDING, 0, PREF_PADDING));
        VBox.setMargin(eventLabel, new Insets(PREF_PADDING, PREF_PADDING, 0, PREF_PADDING));
        VBox.setMargin(inputTextField, new Insets(PREF_PADDING));

        // Set up the final VBox that comprises the scene
        VBox pane = new VBox(scrollPanes, inputTextField);

        // Construct the scene
        return new Scene(pane);
    }

    /**
     * Adds nodes to the split view scene
     *
     * @param taskReturnHBox
     *            HBox containing Label with task information returned from Logic
     * @param eventReturnHBox
     *            HBox containing Label with event information returned from Logic
     */
    public static void addToView(HBox taskReturnHBox, HBox eventReturnHBox) {
        taskLabels.getChildren().add(taskReturnHBox);
        eventLabels.getChildren().add(eventReturnHBox);
    }

    /* ================================================================================
     * private EventHandler and ChangeListener classes
     * ================================================================================
     */
    /**
     * Private class implementing ChangeListener.
     * Sets the current window position of both scrollbars to the bottommost vertical
     * position whenever the size of either scroll pane changes
     */
    private static class ScrollListener implements ChangeListener<Object> {
        @Override
        public void changed(ObservableValue<? extends Object> observable,
                            Object oldValue,
                            Object newValue) {
            taskScrollPane.setVvalue(taskScrollPane.getVmax());
            eventScrollPane.setVvalue(eventScrollPane.getVmax());
        }
    }

    /**
     * Private class implementing EventHandler.
     * Sends the text input by the user as a String to the Logic component to work on
     * when the user presses the enter button
     */
    private static class TextEventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            // Create the Logic component to receive user input
            Logic logicUnit = new Logic();

            // Determine if there is a need to switch scenes
            String userInput = inputTextField.getText();
            boolean swap = logicUnit.isSwapCommand(VIEW_STATE, userInput);

            ArrayList<String> returnMessages = logicUnit.runOperation(userInput);

            if (swap) {
                // Swap to scroll view and display the data
                MainApp.stage.setScene(MainApp.scrollView);

                MainApp.stage.setWidth(MainApp.MIN_WINDOW_WIDTH);
                MainApp.stage.setHeight(MainApp.MIN_WINDOW_HEIGHT);
                MainApp.stage.setMinWidth(MainApp.MIN_WINDOW_WIDTH);
                MainApp.stage.setMinHeight(MainApp.MIN_WINDOW_HEIGHT);

                // Display the operation return message in scroll view
                String opReturnMessage = returnMessages.get(INDEX_OPMSG);

                // Wrap the return messages in UI controls for display
                Label opReturnLabel = new Label(opReturnMessage + EMPTY_LINE);
                opReturnLabel.setWrapText(true);
                HBox opReturnHBox = new HBox(opReturnLabel);
                HBox.setMargin(opReturnLabel, new Insets(0, TAB_PADDING, 0, TAB_PADDING));

                ScrollViewController.addToView(opReturnHBox);
            } else {
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

                // Add the task and event information to the display
                taskLabels.getChildren().add(taskReturnHBox);
                eventLabels.getChildren().add(eventReturnHBox);
            }
            // Clear the text field
            inputTextField.setText("");
        }
    }
}
