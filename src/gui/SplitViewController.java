package gui;

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
    private static final String MESSAGE_TASK = "Upcoming Tasks: ";
    private static final String MESSAGE_EVENT = "Upcoming Events: ";

    private static final double BUFFER_HORIZONTAL = 100;
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

        /* ================================================================================
         * Building the Task display ScrollPane
         * ================================================================================
         */
        // Set up Task VBox
        taskLabels = new VBox(taskLabel);

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

        return new Scene(pane,
                MainApp.MIN_WINDOW_WIDTH + BUFFER_HORIZONTAL,
                MainApp.MIN_WINDOW_HEIGHT);
    }
}
