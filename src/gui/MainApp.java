package gui;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.*;

public class MainApp extends Application {

    // Variables used in scene construction
    protected static final double MIN_WINDOW_HEIGHT = 400;
    protected static final double MIN_WINDOW_WIDTH = 650;
    protected static final String TITLE_STAGE = "WhatToDo";

    // Class field for primaryStage
    protected static Stage stage;

    @Override
    public void start(Stage primaryStage) {

        stage = primaryStage;

        // Initialize the scene
        Scene scene = SplitViewController.initSplitView();

        // Set the stage
        stage.setScene(scene);
        stage.setTitle(TITLE_STAGE);

        // Customize the window
        stage.setMinHeight(MIN_WINDOW_HEIGHT);
        stage.setMinWidth(MIN_WINDOW_WIDTH);
        stage.getIcons().add(new Image("gui/icon.png"));

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
