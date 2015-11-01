/**
 * This class contains the main application that runs the entire program and GUI
 *
 * @author Adrian
 */

package gui;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import struct.View;
import javafx.scene.*;

public class MainApp extends Application {

	// ============================================================
	// Variables used in stage configuration
	// ============================================================
	
    protected static final double MIN_WINDOW_HEIGHT = 700;
    protected static final double MIN_WINDOW_WIDTH = 1000;
    
    protected static final double WIDTH_HELP_DIALOG = 600;
    protected static final double HEIGHT_HELP_DIALOG = 800;
    
    private static final String TITLE_STAGE = "WhatToDo";
    private static final String TITLE_HELP = "Help Dialog";

	// ============================================================
	// Scenes and stages used by the application
	// ============================================================
    protected static Scene scene, helpScene;
    protected static Stage stage, help;

	/**
	 * This method is the driver method which starts the application and
	 * displays the interface
	 */
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        InterfaceController.initMainInterface();
        HelpController.initHelpScene();

        initPrimaryStage();
        initHelpStage();

        stage.show();
        
        // Set the first view to be the default view
        InterfaceController.updateMainInterface(View.DEFAULT);
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the default Scene, window title, window icon, default window
     * dimensions, and the minimum and maximum window sizes
     */
    public static void initPrimaryStage() {
        stage.setScene(scene);

        // Customize the stage
        stage.setTitle(TITLE_STAGE);
        stage.getIcons().add(new Image("gui/resources/icon.png"));

        stage.setWidth(MIN_WINDOW_WIDTH);
        stage.setHeight(MIN_WINDOW_HEIGHT);
        stage.setMinHeight(MIN_WINDOW_HEIGHT);
        stage.setMinWidth(MIN_WINDOW_WIDTH);
        
        // Event handling for the summary view
        stage.addEventFilter(KeyEvent.KEY_PRESSED, 
        		InterfaceController.getLogic().getTabPressHandler());
        
        // Event handling for hotkeys
        stage.addEventHandler(KeyEvent.KEY_PRESSED, 
        		InterfaceController.getLogic().getHotKeyHandler());
        
        // Focus handling for the autocomplete popup
        stage.focusedProperty().addListener(
        		InterfaceController.getLogic().getLostFocusListener());
        
        // Reposition listeners for the autocomplete popup
        stage.xProperty().addListener(
        		InterfaceController.getLogic().getWidthPositionListener());
        stage.yProperty().addListener(
        		InterfaceController.getLogic().getHeightPositionListener());
    }
    
    // Initializes the help Scene, window title, default window dimensions
    public static void initHelpStage() {
    	
    	help = new Stage();
    	help.setScene(helpScene);
    	
    	help.setTitle(TITLE_HELP);
        help.setWidth(WIDTH_HELP_DIALOG);
        help.setHeight(HEIGHT_HELP_DIALOG);
        help.setResizable(false);
        
        // Change listener for when window is closed by user click
        // Performs a strict window close instead of window toggle which loops infinitely
        help.showingProperty().addListener(
        		InterfaceController.getLogic().getCloseHelpListener());
        
        // Event handling for hotkeys
        help.addEventHandler(KeyEvent.KEY_PRESSED, 
        		InterfaceController.getLogic().getHelpHotKeyHandler());
    }
}
