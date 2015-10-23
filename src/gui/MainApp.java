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
    protected static final double MIN_WINDOW_HEIGHT = 800;
    protected static final double MIN_WINDOW_WIDTH = 800;
    
    protected static final double WIDTH_HELP_DIALOG = 600;
    protected static final double HEIGHT_HELP_DIALOG = 800;
    
    private static final String TITLE_STAGE = "WhatToDo";
    private static final String TITLE_HELP = "Help Dialog";

    /* ================================================================================
      * Scenes and stage used by the program
      * ================================================================================
      */
    protected static Scene scene, helpScene;
    protected static Stage stage, help;

    @Override
    public void start(Stage primaryStage) {

        stage = primaryStage;

        // Initialize both scenes
        InterfaceController.initMainInterface();
        HelpController.initHelpScene();

        // Initialize the different stages
        initPrimaryStage();
        initHelpStage();

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
        stage.setScene(scene);

        // Customize the stage
        stage.setTitle(TITLE_STAGE);
        stage.getIcons().add(new Image("gui/resources/icon.png"));

        stage.setWidth(MIN_WINDOW_WIDTH);
        stage.setHeight(MIN_WINDOW_HEIGHT);
        stage.setMinHeight(MIN_WINDOW_HEIGHT);
        stage.setMinWidth(MIN_WINDOW_WIDTH);
    }
    
    public static void initHelpStage() {
    	
    	help = new Stage();
    	help.setScene(helpScene);
    	
    	help.setTitle(TITLE_HELP);
        help.setWidth(WIDTH_HELP_DIALOG);
        help.setHeight(HEIGHT_HELP_DIALOG);
        help.setResizable(false);
        
        help.showingProperty().addListener(
        		InterfaceController.logicControl.getCloseHelpHandler());
    }
}
