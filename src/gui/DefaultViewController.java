package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by adrian on 1/10/15.
 */
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
        header.getChildren().addAll(welcomeLabel, filepathLabel);

        // Construct the HBox with display scroll panes
        // ==================================================
        // Set up the labels
        historyLabel = new Label(HEADER_HISTORY);
        taskLabel = new Label(HEADER_TASK);
        eventLabel = new Label(HEADER_EVENT);

        // Set up the scroll panes
        historyPane = initVertScrollPane();
        taskPane = initVertScrollPane();
        eventPane = initVertScrollPane();

        // Set the VBoxes
        historyVBox.getChildren().addAll(historyLabel, historyPane);
        taskVBox.getChildren().addAll(taskLabel, taskPane);
        eventVBox.getChildren().addAll(eventLabel, eventPane);

        // Set the horizontal scroll panes as one HBox
        scrollPanes.getChildren().addAll(historyVBox, taskVBox, eventVBox);

        // Construct the overall VBox
        mainPane.getChildren().addAll(header, scrollPanes, inputTextField);

        // Add margins to the view
        VBox.setMargin(welcomeLabel, new Insets(PADDING_PREF));
        VBox.setMargin(filepathLabel, new Insets(PADDING_PREF));
        HBox.setMargin(historyVBox, new Insets(
                PADDING_PREF, PADDING_PREF / 2, 0, PADDING_PREF));
        HBox.setMargin(taskVBox, new Insets(
                PADDING_PREF, PADDING_PREF / 2, 0, PADDING_PREF / 2));
        HBox.setMargin(eventVBox, new Insets(
                PADDING_PREF, PADDING_PREF, 0, PADDING_PREF / 2));
        VBox.setMargin(inputTextField, new Insets(PADDING_PREF));
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
}
