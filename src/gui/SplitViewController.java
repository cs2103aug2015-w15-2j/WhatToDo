package gui;

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

public class SplitViewController {

    // Variables used in scene construction
    private static final String EMPTY_LINE = "\n" + " " + "\n";
    private static final String MESSAGE_TASK = "Upcoming Tasks: " + EMPTY_LINE;
    private static final String MESSAGE_EVENT = "Upcoming Events: " + EMPTY_LINE;

    private static final double TAB_PADDING = 30;
    private static final double PREF_PADDING = 5;

    // Controls
    private static TextField inputTextField;
    private static Label taskLabel, eventLabel;
    private static HBox scrollPanes;
    private static ScrollPane taskScrollPane, eventScrollPane;
    private static VBox taskLabels, eventLabels;

    public static Scene initSplitView() {

        // Set up the labels for both scroll panes
        taskLabel = new Label(MESSAGE_TASK);
        eventLabel = new Label(MESSAGE_EVENT);

        // Set up text field for user input
        inputTextField = new TextField();
        inputTextField.requestFocus();
        inputTextField.setOnAction(new TextEventHandler());

        /* ================================================================================
         * Building the Task display ScrollPane
         * ================================================================================
         */
        // Set up Task VBox
        taskLabels = new VBox(taskLabel);
        taskLabels.heightProperty().addListener(new ScrollListener());

        // Set up the Task ScrollPane
        taskScrollPane = new ScrollPane(taskLabels);
        taskScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        taskScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        taskScrollPane.setFitToWidth(true);

        /* ================================================================================
         * Building the Event display ScrollPane
         * ================================================================================
         */
        // Set up the Event Vbox
        eventLabels = new VBox(eventLabel);
        eventLabels.heightProperty().addListener(new ScrollListener());

        // Set up the Event ScrollPane
        eventScrollPane = new ScrollPane(eventLabels);
        eventScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        eventScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        eventScrollPane.setFitToWidth(true);

        // Combine the Task and Event ScrollPanes
        scrollPanes = new HBox();
        scrollPanes.getChildren().addAll(taskScrollPane, eventScrollPane);

        // Set taskScrollPane and eventScrollPane to fill the window equally
        HBox.setHgrow(taskScrollPane, Priority.ALWAYS);
        HBox.setHgrow(eventScrollPane, Priority.ALWAYS);

        // Set scrollPanes to fill the window vertically
        VBox.setVgrow(scrollPanes, Priority.ALWAYS);

        /* ================================================================================
         * Building the overall scene
         * ================================================================================
         */
        // Setting margins for individual components
        VBox.setMargin(taskLabel, new Insets(PREF_PADDING, PREF_PADDING, 0, PREF_PADDING));
        VBox.setMargin(eventLabel, new Insets(PREF_PADDING, PREF_PADDING, 0, PREF_PADDING));
        VBox.setMargin(inputTextField, new Insets(PREF_PADDING));
        VBox pane = new VBox(scrollPanes, inputTextField);

        return new Scene(pane);
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
            taskScrollPane.setVvalue(taskScrollPane.getVmax());
            eventScrollPane.setVvalue(eventScrollPane.getVmax());
        }
    }

    /*
     * EventHandler implementation.
     * Checks for when the user presses the Enter key to send information to the program
     */
    private static class TextEventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            // Test functionality by switching scenes only when the keyword "switch"
            // is entered by the user
            String keyword = inputTextField.getText();
            if (keyword.toLowerCase().equals("switch")) {
                // Switch scene to ScrollView
                MainApp.stage.setScene(MainApp.scrollView);
                MainApp.stage.setWidth(MainApp.MIN_WINDOW_WIDTH);
                MainApp.stage.setHeight(MainApp.MIN_WINDOW_HEIGHT);

                // Set the new minimum widths and heights
                MainApp.stage.setMinWidth(MainApp.MIN_WINDOW_WIDTH);
                MainApp.stage.setMinHeight(MainApp.MIN_WINDOW_HEIGHT);
            } else {
                // Create the Labels to add to the ScrollPanes
                Label taskReturnMsg = new Label(inputTextField.getText() + EMPTY_LINE);
                Label eventReturnMsg = new Label(inputTextField.getText() + EMPTY_LINE);
                taskReturnMsg.setWrapText(true);
                eventReturnMsg.setWrapText(true);

                // Add the return messages to the ScrollPanes
                HBox taskReturnHBox = new HBox(taskReturnMsg);
                HBox.setMargin(taskReturnMsg, new Insets(0, TAB_PADDING, 0, TAB_PADDING));
                taskLabels.getChildren().add(taskReturnHBox);

                HBox eventReturnHBox = new HBox(eventReturnMsg);
                HBox.setMargin(eventReturnMsg, new Insets(0, TAB_PADDING, 0, TAB_PADDING));
                eventLabels.getChildren().add(eventReturnHBox);
            }
            // Clear the text field
            inputTextField.setText("");
        }
    }
}
