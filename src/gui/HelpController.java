package gui;

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
    
    protected static final String PATH_HELP_DIALOG = "gui/resources/help_dialog.jpg";
    
    public static void initHelpScene() {
    	
    	isHelpOpen = false;
    	
    	helpImage = new ImageView(PATH_HELP_DIALOG);
    	helpImage.setFitWidth(MainApp.WIDTH_HELP_DIALOG);
    	helpImage.setPreserveRatio(true);

    	helpScroll = new ScrollPane(helpImage);
    	helpScroll.setFitToWidth(true);
    	helpScroll.setVbarPolicy(ScrollBarPolicy.NEVER);
    	helpScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
    	
    	MainApp.helpScene = new Scene(helpScroll);
    }
    
    /* ================================================================================
     * Public methods to control the help dialog window
     * ================================================================================
     */
    
    public static void toggleHelpDialog() {
    	if (!isHelpOpen) {
    		openHelpDialog();
    	} else {
    		closeHelpDialog();
    	}
    }
    
    public static void openHelpDialog() {
		// Set help button to selected
		InterfaceController.changeButtonToSelected(View.HELP);
		
		isHelpOpen = true;
		MainApp.help.show();
		MainApp.help.requestFocus();
    }
    
    public static void closeHelpDialog() {
		// Set help button to unselected
		InterfaceController.changeButtonToUnselected(View.HELP);
		isHelpOpen = false;
		MainApp.help.close();
    }
}
