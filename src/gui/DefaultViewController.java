package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DefaultViewController {

    /* ================================================================================
     * Variables used in scene construction
     * ================================================================================
     */
    private static final String HEADER_WELCOME = "Welcome to WhatToDo";
    private static final String HEADER_HISTORY = "History";
    private static final String HEADER_TASK = "Tasks";
    private static final String HEADER_EVENT = "Events";

    private static final double PADDING_PREF = 5.0;

    /* ================================================================================
     * JavaFX controls used in the scene
     * ================================================================================
     */
    // Main display pane containing all the components
    private static VBox mainPane;

    // Header and its components
    private static VBox header;
    private static Label welcomeLabel, filepathLabel;

    // Scroll panes and its components
    private static HBox scrollPanes;
    private static VBox historyVBox, taskVBox, eventVBox;
    private static Label historyLabel, taskLabel, eventLabel;
    private static StackPane historyStack, taskStack, eventStack;
    private static ScrollPane historyPane, taskPane, eventPane;

    // The text field
    private static TextField inputTextField;

    public static Scene initDefaultView() {

        // Construct the header
        // ==================================================
        // Set up the header labels
        welcomeLabel = new Label(HEADER_WELCOME);
        filepathLabel = new Label("todo.txt");

        welcomeLabel.setWrapText(true);
        filepathLabel.setWrapText(true);

        // Store the labels in the header
        header = new VBox(welcomeLabel, filepathLabel);

        // Construct the HBox with display scroll panes
        // ==================================================
        // Set up the labels into StackPanes for alignment
        historyLabel = new Label(HEADER_HISTORY);
        taskLabel = new Label(HEADER_TASK);
        eventLabel = new Label(HEADER_EVENT);

        historyStack = new StackPane(historyLabel);
        taskStack = new StackPane(taskLabel);
        eventStack = new StackPane(eventLabel);

        // Set up the scroll panes
        historyPane = initVertScrollPane();
        taskPane = initVertScrollPane();
        eventPane = initVertScrollPane();

        // Set the VBoxes
        historyVBox = new VBox(historyStack, historyPane);
        taskVBox = new VBox(taskStack, taskPane);
        eventVBox = new VBox(eventStack, eventPane);

        // Set the horizontal scroll panes as one HBox
        scrollPanes = new HBox(historyVBox, taskVBox, eventVBox);

        VBox.setVgrow(historyPane, Priority.ALWAYS);
        VBox.setVgrow(taskPane, Priority.ALWAYS);
        VBox.setVgrow(eventPane, Priority.ALWAYS);
        HBox.setHgrow(historyVBox, Priority.ALWAYS);
        HBox.setHgrow(taskVBox, Priority.ALWAYS);
        HBox.setHgrow(eventVBox, Priority.ALWAYS);

        VBox.setVgrow(scrollPanes, Priority.ALWAYS);

        // Construct the text field
        // ==================================================
        inputTextField = new TextField();
        inputTextField.requestFocus();
        inputTextField.setOnAction(new TextInputHandler());

        // Construct the overall VBox
        // ==================================================
        mainPane = new VBox(header, scrollPanes, inputTextField);

        // Add margins to the view
        VBox.setMargin(welcomeLabel, new Insets(PADDING_PREF, PADDING_PREF, 0, PADDING_PREF));
        VBox.setMargin(filepathLabel, new Insets(0, PADDING_PREF, PADDING_PREF, PADDING_PREF));
        HBox.setMargin(historyVBox, new Insets(
                PADDING_PREF, PADDING_PREF / 2, 0, PADDING_PREF));
        HBox.setMargin(taskVBox, new Insets(
                PADDING_PREF, PADDING_PREF / 2, 0, PADDING_PREF / 2));
        HBox.setMargin(eventVBox, new Insets(
                PADDING_PREF, PADDING_PREF, 0, PADDING_PREF / 2));
        VBox.setMargin(inputTextField, new Insets(PADDING_PREF));

        // Construct the scene
        return new Scene(mainPane);
    }

    private static ScrollPane initVertScrollPane() {

        // Create the interior VBox and store in in the pane
        VBox paneVBox = new VBox();
        ScrollPane tempPane = new ScrollPane(paneVBox);

        // Set the ScrollPane properties
        tempPane.setFitToWidth(true);
        tempPane.setFitToHeight(true);
        tempPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tempPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Set automatic scroll-to-bottom for the scroll pane
        ((VBox)tempPane.getContent()).heightProperty().addListener(
                new ScrollListener(tempPane));

        return tempPane;
    }

    private static class ScrollListener implements ChangeListener<Object> {

        private ScrollPane scrollPane;

        public ScrollListener(ScrollPane scrollPane) {
            this.scrollPane = scrollPane;
        }

        @Override
        public void changed(ObservableValue<? extends Object> observable,
                            Object oldValue, Object newValue) {
            this.scrollPane.setVvalue(this.scrollPane.getVmax());
        }
    }

    private static class TextInputHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            inputTextField.setText("");
        }
    }
}
