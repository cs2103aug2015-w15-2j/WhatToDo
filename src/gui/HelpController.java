/**
 * This class initializes the help window of the application and provides methods 
 * to interact (open, close, toggle) with the window
 * 
 * @@author A0124123Y
 */

package gui;

import java.util.logging.Level;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import struct.View;

public class HelpController {

    // Used for initHelpDialog
    private static ImageView helpImage;
    private static ScrollPane helpScroll;
    
    private static boolean isHelpOpen;
    
    private static final String PATH_HELP_DIALOG = "gui/resources/help_dialog.jpg";
    
    /**
     * This method initializes all the interface components for the help window
     */
    public static void initHelpScene() {
    	isHelpOpen = false;
    	
    	helpImage = new ImageView(PATH_HELP_DIALOG);
    	helpScroll = new ScrollPane(helpImage);
    	
    	// Component formatting
    	helpImage.setFitWidth(MainApp.WIDTH_HELP_DIALOG);
    	helpImage.setPreserveRatio(true);

    	helpScroll.setFitToWidth(true);
    	helpScroll.setVbarPolicy(ScrollBarPolicy.NEVER);
    	helpScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
    	
    	MainApp.helpScene = new Scene(helpScroll);
    }
    
	// ================================================================================
    // Protected methods use to control the visibility of the help dialog
    // ================================================================================
    
    protected static void toggleHelpDialog() {
    	if (!isHelpOpen) {
    		openHelpDialog();
    	} else {
    		closeHelpDialog();
    	}
    }
    
    protected static void openHelpDialog() {
		// Set help button to selected
		InterfaceController.changeButtonToSelected(View.HELP);
		isHelpOpen = true;
		MainApp.help.show();
		MainApp.help.requestFocus();
		MainApp.logger.log(Level.INFO, MainApp.LOG_HELP_OPEN);
    }
    
    protected static void closeHelpDialog() {
		// Set help button to unselected
		InterfaceController.changeButtonToUnselected(View.HELP);
		isHelpOpen = false;
		MainApp.help.close();
		MainApp.logger.log(Level.INFO, MainApp.LOG_HELP_CLOSE);
    }
}
