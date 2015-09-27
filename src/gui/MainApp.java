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

    // Scenes used for display
    protected static Scene scrollView, splitView, defaultView;

    // Class field for primaryStage
    protected static Stage stage;

    @Override
    public void start(Stage primaryStage) {

        stage = primaryStage;

        // Initialize both scenes
        scrollView = ScrollViewController.initScrollView();
        splitView = SplitViewController.initSplitView();

        // Set the stage
        stage.setScene(scrollView);
        stage.setWidth(MIN_WINDOW_WIDTH);
        stage.setHeight(MIN_WINDOW_HEIGHT);
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
