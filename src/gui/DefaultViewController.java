package gui;

import java.nio.file.FileSystemException;

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
import javafx.scene.shape.Line;

public class DefaultViewController {

    /* ================================================================================
     * Variables used in scene construction
     * ================================================================================
     */
    private static final String HEADER_WELCOME = "Welcome to WhatToDo";
    private static final String HEADER_WRITE = "Currently writing to ";
    private static final String HEADER_HISTORY = "History";
    private static final String HEADER_TASK = "Tasks";
    private static final String HEADER_EVENT = "Events";

    private static final String PATH_CSS_DEFAULT = "/gui/stylesheets/DefaultView.css";

    private static final String NEWLINE = "\n" + " ";

    private static final double PADDING_UNIT = 10.0;

    private static Logic logic;

    /* ================================================================================
     * JavaFX controls used in the scene:
     *
     *  Scene defaultScene
     *      VBox mainPane
     *          StackPane welcomeStack
     *              Label welcomeLabel
     *          StackPane filepathStack
     *              Label filepathLabel
     *          Line lineTop
     *          HBox paneLabels
     *              StackPane historyStack
     *                  Label historyLabel
     *              StackPane taskStack
     *                  Label taskLabel
     *              StackPane eventStack
     *                  Label eventLabel
     *          Line lineBot
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
    private static Line lineTop, lineBot;
    private static ScrollPane historyPane, taskPane, eventPane;
    private static HBox paneLabels, scrollPanes;
    private static Label welcomeLabel, filepathLabel,
            historyLabel, taskLabel, eventLabel;
    private static TextField textField;

    /**
     * Creates the default UI scene for the program used to display feedback
     * to the user when commands are entered.
     * Consists of three separate scroll panes with History, Tasks, and Events.
     *
     * @return A Scene object which is used to set primaryStage
     */
    public static Scene initDefaultView() {

        // Create the logic object for this scene and get the filepath
    	try{
    		logic = new Logic();
    	}
    	catch(FileSystemException e){
    		//TODO print some error msg about failure to set up txt file
    	}
        
        String filepath = logic.getFilepath();

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

        filepathLabel = new Label(HEADER_WRITE + filepath);
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
        historyStack.setAlignment(Pos.CENTER_LEFT);

        taskStack = new StackPane(taskLabel);
        taskStack.setAlignment(Pos.CENTER_LEFT);

        eventStack = new StackPane(eventLabel);
        eventStack.setAlignment(Pos.CENTER_LEFT);

        // welcomeStack, filepathStack
        welcomeStack = new StackPane(welcomeLabel);
        welcomeStack.setAlignment(Pos.CENTER_LEFT);

        filepathStack = new StackPane(filepathLabel);
        filepathStack.setAlignment(Pos.CENTER_LEFT);
        filepathStack.setMaxHeight(100);

        // scrollPanes
        scrollPanes = new HBox(historyPane, taskPane, eventPane);
        HBox.setHgrow(historyPane, Priority.ALWAYS);
        HBox.setHgrow(taskPane, Priority.ALWAYS);
        HBox.setHgrow(eventPane, Priority.ALWAYS);

        // lineTop, lineBot
        lineTop = new Line(0, 0, MainApp.MIN_WINDOW_WIDTH, 0);
        lineBot = new Line(0, 0, MainApp.MIN_WINDOW_WIDTH, 0);

        lineTop.endXProperty().bind(MainApp.stage.widthProperty());
        lineBot.endXProperty().bind(MainApp.stage.widthProperty());

        // paneLabels
        paneLabels = new HBox(historyStack, taskStack, eventStack);
        HBox.setHgrow(historyStack, Priority.ALWAYS);
        HBox.setHgrow(taskStack, Priority.ALWAYS);
        HBox.setHgrow(eventStack, Priority.ALWAYS);

        // mainPane
        mainPane = new VBox(welcomeStack, filepathStack,
                lineTop, paneLabels, lineBot, scrollPanes, textField);
        VBox.setVgrow(scrollPanes, Priority.ALWAYS);

        /* ================================================================================
         * Read from the file and print to the Task and Event scroll panes
         * ================================================================================
         */
        updateView();

        /* ================================================================================
         * Add style classes to the components
         * ================================================================================
         */
        historyLabel.getStyleClass().add("label-scroll-history");
        taskLabel.getStyleClass().add("label-scroll-tasks");
        eventLabel.getStyleClass().add("label-scroll-events");
        welcomeLabel.getStyleClass().add("label-welcome");
        filepathLabel.getStyleClass().add("label-filepath");

        historyPane.getStyleClass().add("scroll-pane-history");
        taskPane.getStyleClass().add("scroll-pane-tasks");
        eventPane.getStyleClass().add("scroll-pane-events");

        lineTop.getStyleClass().add("line");
        lineBot.getStyleClass().add("line");

        // Set margin since CSS does not support direct margins for certain components
        VBox.setMargin(textField, new Insets(PADDING_UNIT));

        // defaultScene
        defaultScene = new Scene(mainPane);
        defaultScene.getStylesheets().add(
                DefaultViewController.class.getResource(PATH_CSS_DEFAULT).toExternalForm());

        return defaultScene;
    }

    /**
     * Refreshes the Task and Event scroll panes with updated information after
     * every operation has been performed on the file.
     */
    public static void updateView() {

        // Clear the old information
        taskBox.getChildren().clear();
        eventBox.getChildren().clear();

        // Redisplay the Tasks
        Label tempLabel = new Label(logic.readTasks());
        tempLabel.setWrapText(true);

        HBox tempBox = new HBox(tempLabel);
        taskBox.getChildren().addAll(tempBox);

        // Redisplay the Events
        tempLabel = new Label(logic.readEvents());
        tempLabel.setWrapText(true);

        tempBox = new HBox(tempLabel);
        eventBox.getChildren().addAll(tempBox);
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

            String textFieldInput = textField.getText();

            // Clear the textField
            textField.setText("");

            // Run the operation
            String returnMessage = logic.runOperation(textFieldInput);

            // Wrap the output in a Label for display
            // Use a newline to standardize line spacings across all scroll panes
            Label returnLabel = new Label(returnMessage + NEWLINE);
            returnLabel.setWrapText(true);

            HBox returnBox = new HBox(returnLabel);

            // Add the returnMessage to the History pane
            historyBox.getChildren().add(returnBox);

            // Update the Tasks and Events
            updateView();
        }
    }
}
