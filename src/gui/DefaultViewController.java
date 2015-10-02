package gui;

import backend.Logic;
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
import javafx.scene.layout.*;

import java.util.ArrayList;

public class DefaultViewController {

    /* ================================================================================
     * Variables used in scene construction
     * ================================================================================
     */
    private static final String HEADER_WELCOME = "Welcome to WhatToDo";
    private static final String HEADER_WRITE = "Currently writing to ";
    private static final String HEADER_FILEPATH = "C:/Users/ToDo.txt";
    private static final String HEADER_HISTORY = "History";
    private static final String HEADER_TASK = "Tasks";
    private static final String HEADER_EVENT = "Events";

    private static final String PATH_CSS_DEFAULT = "/gui/stylesheets/DefaultView.css";

    private static final double PADDING_UNIT = 10.0;

    /* ================================================================================
     * JavaFX controls used in the scene:
     *
     *  Scene defaultScene
     *      VBox mainPane
     *          StackPane welcomeStack
     *              Label welcomeLabel
     *          StackPane filepathStack
     *              Label filepathLabel
     *          HBox paneLabels
     *              StackPane historyStack
     *                  Label historyLabel
     *              StackPane taskStack
     *                  Label taskLabel
     *              StackPane eventStack
     *                  Label eventLabel
     *          HBox scrollPanes
     *              ScrollPane historyPane
     *                  VBox historyBox
     *              ScrollPane taskPane
     *                  VBox taskBox
     *              ScrollPane eventPane
     *                  VBox eventBox
     *          TextField textField
     * ================================================================================
     */
    private static Scene defaultScene;
    private static VBox mainPane, historyBox, taskBox, eventBox;
    private static StackPane welcomeStack, filepathStack,
            historyStack, taskStack, eventStack;
    private static ScrollPane historyPane, taskPane, eventPane;
    private static HBox paneLabels, scrollPanes;
    private static Label welcomeLabel, filepathLabel,
            historyLabel, taskLabel, eventLabel;
    private static TextField textField;

    public static Scene initDefaultView() {

        /* ================================================================================
         * Construct the scene from bottom up starting with the deepest nested nodes
         * ================================================================================
         */

        // textField
        textField = new TextField();
        textField.requestFocus();
        textField.setOnAction(new TextInputHandler());

        // historyBox, taskBox, eventBox
        historyBox = new VBox();
        historyBox.heightProperty().addListener(new HistoryScroller());

        taskBox = new VBox();
        taskBox.heightProperty().addListener(new TaskScroller());

        eventBox = new VBox();
        eventBox.heightProperty().addListener(new EventScroller());

        // welcomeLabel, filepathLabel, historyLabel, taskLabel, eventLabel
        historyLabel = new Label(HEADER_HISTORY);
        taskLabel = new Label(HEADER_TASK);
        eventLabel = new Label(HEADER_EVENT);
        welcomeLabel = new Label(HEADER_WELCOME);

        filepathLabel = new Label(HEADER_WRITE + HEADER_FILEPATH);
        filepathLabel.setWrapText(true);

        // historyPane, taskPane, eventPane
        historyPane = new ScrollPane(historyBox);
        historyPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        historyPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        historyPane.setFitToWidth(true);
        historyPane.prefViewportWidthProperty().bind(historyBox.widthProperty());

        taskPane = new ScrollPane(taskBox);
        taskPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        taskPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        taskPane.setFitToWidth(true);
        taskPane.prefViewportWidthProperty().bind(historyBox.widthProperty());

        eventPane = new ScrollPane(eventBox);
        eventPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        eventPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        eventPane.setFitToWidth(true);
        eventPane.prefViewportWidthProperty().bind(historyBox.widthProperty());

        // historyStack, taskStack, eventStack
        historyStack = new StackPane(historyLabel);
        historyStack.setAlignment(Pos.CENTER);

        taskStack = new StackPane(taskLabel);
        taskStack.setAlignment(Pos.CENTER);

        eventStack = new StackPane(eventLabel);
        eventStack.setAlignment(Pos.CENTER);

        // welcomeStack, filepathStack
        welcomeStack = new StackPane(welcomeLabel);
        welcomeStack.setAlignment(Pos.CENTER);

        filepathStack = new StackPane(filepathLabel);
        filepathStack.setAlignment(Pos.CENTER);
        filepathStack.setMaxHeight(100);

        // scrollPanes
        scrollPanes = new HBox(historyPane, taskPane, eventPane);
        HBox.setHgrow(historyPane, Priority.ALWAYS);
        HBox.setHgrow(taskPane, Priority.ALWAYS);
        HBox.setHgrow(eventPane, Priority.ALWAYS);

        // paneLabels
        paneLabels = new HBox(historyStack, taskStack, eventStack);
        HBox.setHgrow(historyStack, Priority.ALWAYS);
        HBox.setHgrow(taskStack, Priority.ALWAYS);
        HBox.setHgrow(eventStack, Priority.ALWAYS);

        // mainPane
        mainPane = new VBox(welcomeStack, filepathStack,
                paneLabels, scrollPanes, textField);
        VBox.setVgrow(scrollPanes, Priority.ALWAYS);

        /* ================================================================================
         * Add style classes to the components
         * ================================================================================
         */
        historyLabel.getStyleClass().add("label-scroll-title");
        taskLabel.getStyleClass().add("label-scroll-title");
        eventLabel.getStyleClass().add("label-scroll-title");
        welcomeLabel.getStyleClass().add("label-title");
        filepathLabel.getStyleClass().add("label-filepath");

        historyPane.getStyleClass().add("scroll-pane-history");
        taskPane.getStyleClass().add("scroll-pane-tasks");
        eventPane.getStyleClass().add("scroll-pane-events");

        // Set margin using VBox since CSS does not support direct margins for TextFields
        VBox.setMargin(textField, new Insets(
                2 * PADDING_UNIT, PADDING_UNIT, PADDING_UNIT, PADDING_UNIT));

        // defaultScene
        defaultScene = new Scene(mainPane);
        defaultScene.getStylesheets().add(
                DefaultViewController.class.getResource(PATH_CSS_DEFAULT).toExternalForm());

        return defaultScene;
    }

    /* ================================================================================
     * Private ChangeListener classes
     *
     * Classes implementing ChangeListener for the individual panes.
     * Sets the current window position of both scrollbars to the bottommost vertical
     * position whenever the size of historyPane, taskPane, and eventPane changes.
     * ================================================================================
     */
    private static class HistoryScroller implements ChangeListener<Object> {
        @Override
        public void changed(ObservableValue<? extends Object> observable,
                            Object oldValue, Object newValue) {
            historyPane.setVvalue(historyPane.getVmax());
        }
    }

    private static class TaskScroller implements ChangeListener<Object> {
        @Override
        public void changed(ObservableValue<? extends Object> observable,
                            Object oldValue, Object newValue) {
            taskPane.setVvalue(taskPane.getVmax());
        }
    }

    private static class EventScroller implements ChangeListener<Object> {
        @Override
        public void changed(ObservableValue<? extends Object> observable,
                            Object oldValue, Object newValue) {
            eventPane.setVvalue(eventPane.getVmax());
        }
    }

    /* ================================================================================
     * Private EventHandler class
     *
     * Class implementing EventHandler.
     * Sends the text input by the user as a String to the Logic component to work on
     * when the user presses the enter button
     * ================================================================================
     */
    private static class TextInputHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {

            // Create a Logic object to parse the user input
            Logic logic = new Logic();

            // Get the appropriate output from Logic
            ArrayList<String> returnMessages = logic.runOperation(textField.getText());
        }
    }
}
