/**
 * This class contains the main application that runs the entire program and GUI
 *
 * @author Adrian
 */

package gui;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.*;

public class MainApp extends Application {

    /* ================================================================================
     * Variables used in scene construction
     * ================================================================================
     */
    protected static final double MIN_WINDOW_HEIGHT = 576;
    protected static final double MIN_WINDOW_WIDTH = 1024;
    protected static final String TITLE_STAGE = "WhatToDo";

    /* ================================================================================
      * Scenes and stage used by the program
      * ================================================================================
      */
    protected static Scene defaultView;
    protected static Stage stage;

    @Override
    public void start(Stage primaryStage) {

        stage = primaryStage;

        // Initialize both scenes
        defaultView = DefaultViewController.initDefaultView();
        //InterfaceController.initMainInterface();

        // Initialize the default stage to be displayed
        initPrimaryStage();

        // Run
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the default Scene, window title, window icon, default window
     * dimensions, and the minimum and maximum window sizes
     */
    public static void initPrimaryStage() {
        // Set the scene to be displayed
        stage.setScene(defaultView);

        // Customize the stage
        stage.setTitle(TITLE_STAGE);
        stage.getIcons().add(new Image("gui/resources/icon.png"));

        stage.setWidth(MIN_WINDOW_WIDTH);
        stage.setHeight(MIN_WINDOW_HEIGHT);
        stage.setMinHeight(MIN_WINDOW_HEIGHT);
        stage.setMinWidth(MIN_WINDOW_WIDTH);
    }
}
