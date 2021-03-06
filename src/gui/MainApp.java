/**
 * This class contains the main driver class that runs the entire program and 
 * starts the user interface.
 *
 * @@author A0124123Y
 */

package gui;

import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    protected static final double WIDTH_HELP_DIALOG = 800;
    protected static final double HEIGHT_HELP_DIALOG = 800;
    
    private static final String TITLE_STAGE = "WhatToDo";
    private static final String TITLE_HELP = "Help Dialog";
    
    protected static final String LOG_START = "Starting WhatToDo";
    protected static final String LOG_CLOSE = "Closing WhatToDo";
    protected static final String LOG_HELP_OPEN = "Opening Help dialog";
    protected static final String LOG_HELP_CLOSE = "Closing Help dialog";
    protected static final String LOG_INVALID_INDEX = "Invalid index has been entered";
    protected static final String LOG_FILE_NOT_FOUND = "Unable to open whattodo.txt";
    protected static final String LOG_CONFIG_NOT_FOUND = "Unable to open config.txt";
    protected static final String LOG_ALIAS_NOT_FOUND = "Unable to open alias.txt";
    protected static final String LOG_FILE_NOT_CREATED = "Unable to create the file";
    
    private static final String PATH_ICON = "gui/resources/icon.png";

	// ============================================================
	// Scenes and stages used by the application
	// ============================================================
    
    protected static Scene scene, helpScene;
    protected static Stage stage, help;

	// ============================================================
	// Logger object used to log events and details
	// ============================================================
    
    protected static final Logger logger = Logger.getLogger(MainApp.class.getName()); 
    
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
        logger.log(Level.INFO, LOG_START);
        
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
        stage.getIcons().add(new Image(PATH_ICON));

        stage.setWidth(MIN_WINDOW_WIDTH);
        stage.setHeight(MIN_WINDOW_HEIGHT);
        stage.setMinHeight(MIN_WINDOW_HEIGHT);
        stage.setMinWidth(MIN_WINDOW_WIDTH);
        
        // Event handling for the summary view
        stage.addEventFilter(KeyEvent.KEY_PRESSED, 
        		Handlers.getTabPressHandler());
        
        // Event handling for hotkeys
        stage.addEventFilter(KeyEvent.KEY_PRESSED, 
        		Handlers.getHotKeyHandler());
        
        // Focus handling for the autocomplete popup
        stage.focusedProperty().addListener(
        		Listeners.getLostFocusListener());
        
        // Reposition listeners for the autocomplete popup
        stage.xProperty().addListener(
        		Listeners.getWidthPositionListener());
        stage.yProperty().addListener(
        		Listeners.getHeightPositionListener());
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
        		Listeners.getCloseHelpListener());
        
        // Event handling for hotkeys
        help.addEventFilter(KeyEvent.KEY_PRESSED, 
        		Handlers.getHelpHotKeyHandler());
    }
}
