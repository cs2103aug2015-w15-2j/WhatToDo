package gui;

import java.nio.file.FileSystemException;
import java.util.ArrayList;

import backend.Logic;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import struct.View;

public class LogicController {

	private static final String MESSAGE_ERROR_FILESYSTEM = "Failed to create the file.";
	
	private static Logic logic;
	private static CommandHistory commandHistory;
	
	public LogicController() {
		
		try {
			logic = new Logic();
		} catch (FileSystemException e) {
			logic = null;
		}
		
		// Initialize the command history object
		commandHistory = new CommandHistory();
	}
	
	public String getFilePath() {
		
		if (logic != null) {
			return logic.getFilepath();
		} else {
			return MESSAGE_ERROR_FILESYSTEM;
		}
	}
	
	public ArrayList<String> getDefTasks() {
		
		ArrayList<String> temp = new ArrayList<String>();
		
		// Get the string from logic
		String defTasks = logic.taskDefaultView();
		
		// Split the string by newline
		String[] defTasksSplit = defTasks.split("\n");
		
		// Traverse the split string array and ignore the empty lines
		for (int i = 0; i < defTasksSplit.length; i++) {
			if (!defTasksSplit[i].equals("")) {
				// Add it to the ArrayList				
				temp.add(defTasksSplit[i]);
			}
		}
		
		return temp;
	}
	
	public ArrayList<String> getDefEvents() {
		
		ArrayList<String> temp = new ArrayList<String>();
		
		// Get the string from logic
		String defEvents = logic.eventDefaultView();
		
		// Split the string by newline
		String[] defEventsSplit = defEvents.split("\n");
		
		// Traverse the split string array and ignore the empty lines
		for (int i = 0; i < defEventsSplit.length; i++) {
			if (!defEventsSplit[i].equals("")) {
				// Add it to the ArrayList
				temp.add(defEventsSplit[i]);
			}
		}
		
		return temp;
	}
	
	private static void changeView(View view) {
		
		switch(InterfaceController.getCurrentView()) {
        /* ================================================================================
         * Default view
         * ================================================================================
         */
        case DEFAULT:
        	switch (view) {
        	case ALL:
        		InterfaceController.updateMainInterface(View.ALL);
        		break;
        	case HISTORY:
        		InterfaceController.updateMainInterface(View.HISTORY);
        		break;
        	case UNRESOLVED:
        		// TODO
        		//InterfaceController.updateMainInterface(View.UNRESOLVED);
        		break;
        	case SEARCH:
        		// TODO
        		//InterfaceController.updateMainInterface(View.SEARCH);
        		break;
        	case HELP:
        		HelpController.toggleHelpDialog();
        		break;
        	default:
        		// Do nothing if already in this view
            	break;
        	}
        	break;
        /* ================================================================================
         * All view
         * ================================================================================
         */
        case ALL:
        	switch (view) {
        	case DEFAULT:
        		InterfaceController.updateMainInterface(View.DEFAULT);
        		break;
        	case HISTORY:
        		InterfaceController.updateMainInterface(View.HISTORY);
        		break;
        	case UNRESOLVED:
        		// TODO
        		//InterfaceController.updateMainInterface(View.UNRESOLVED);
        		break;
        	case SEARCH:
        		// TODO
        		//InterfaceController.updateMainInterface(View.SEARCH);
        		break;
        	case HELP:
        		HelpController.toggleHelpDialog();
        		break;
        	default:
        		// Do nothing if already in this view
        		break;
        	}
        	break;
        /* ================================================================================
         * History view
         * ================================================================================
         */
        case HISTORY:
        	switch (view) {
        	case DEFAULT:
        		InterfaceController.updateMainInterface(View.DEFAULT);
        		break;
        	case ALL:
        		InterfaceController.updateMainInterface(View.ALL);
        		break;
        	case UNRESOLVED:
        		// TODO
        		//InterfaceController.updateMainInterface(View.HISTORY);
        		break;
        	case SEARCH:
        		// TODO
        		//InterfaceController.updateMainInterface(View.SEARCH);
        		break;
        	case HELP:
        		HelpController.toggleHelpDialog();
        		break;
        	default:
        		// Do nothing if already in this view
        		break;
        	}
        	break;
        /* ================================================================================
         * Done view
         * ================================================================================
         */
        case UNRESOLVED:
        	switch (view) {
        	case DEFAULT:
        		InterfaceController.updateMainInterface(View.DEFAULT);
        		break;
        	case ALL:
        		InterfaceController.updateMainInterface(View.ALL);
        		break;
        	case HISTORY:
        		InterfaceController.updateMainInterface(View.HISTORY);
        		break;
        	case SEARCH:
        		// TODO
        		//InterfaceController.updateMainInterface(View.SEARCH);
        		break;
        	case HELP:
        		HelpController.toggleHelpDialog();
        		break;
        	default:
        		// Do nothing if already in this view
        		break;
        	}
        	break;
        	/* ================================================================================
        	 * Search view
        	 * ================================================================================
        	 */
        case SEARCH:
        	switch (view) {
        	case DEFAULT:
        		InterfaceController.updateMainInterface(View.DEFAULT);
        	case ALL:
        		InterfaceController.updateMainInterface(View.ALL);
        		break;
        	case HISTORY:
        		InterfaceController.updateMainInterface(View.HISTORY);
        		break;
        	case UNRESOLVED:
        		// TODO
        		//InterfaceController.updateMainInterface(DoneViewController.initDoneView());
        		break;
        	case HELP:
        		HelpController.toggleHelpDialog();
        		break;
        	default:
        		// Do nothing if already in this view
        		break;
        	}
        	break;
        	
        default: // do nothing, should not enter
        	break;
        }
	}

	private static void runCommand(String textFieldInput) {
		
		// Execute the command
		String returnMessage = logic.executeCommand(textFieldInput);
		
		// Add the returnMessage to the feedback bar and history view
		InterfaceController.getFeedbackLabel().setText(returnMessage);
		HistoryViewController.updateHistView(returnMessage);

		// Update the necessary views
		DefaultViewController.updateDefView();
		AllViewController.updateAllView();
	}
	
	/* ================================================================================
     * Getters to allow InterfaceController to access the private handling classes
     * ================================================================================
     */
	
	public TextInputHandler getTextInputHandler() {
		return new TextInputHandler();
	}
	
	public KeyPressHandler getKeyPressHandler() {
		return new KeyPressHandler();
	}
	
	public ButtonHoverHandler getButtonHoverHandler(View buttonType) {
		return new ButtonHoverHandler(buttonType);
	}
	
	public ButtonClickHandler getButtonClickHandler(View buttonType) {
		return new ButtonClickHandler(buttonType);
	}
	
	public CloseHelpHandler getCloseHelpHandler() {
		return new CloseHelpHandler();
	}
	
	public ScrollListener getScrollListener(View scrollpane) {
		return new ScrollListener(scrollpane);
	}
	
	public HeightListener getHeightListener() {
		return new HeightListener();
	}
	
	public WidthListener getWidthListener() {
		return new WidthListener();
	}
	
	/* ================================================================================
     * Private event handlers for InterfaceController
     * ================================================================================
     */	
	
	private static class TextInputHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {

        	// Get the text field from InterfaceController
        	TextField textField = InterfaceController.getTextField();
        	
            String textFieldInput = textField.getText();

            // Add the input into command history
            commandHistory.add(textFieldInput);
            commandHistory.resetIndex();

            // Clear the textField
            textField.setText("");

            // Switch view depending on the input
            switch (textFieldInput) {
            case "def":
            	changeView(View.DEFAULT);
            	break;
            case "all":
            	changeView(View.ALL);
            	break;
            case "hist":
            	changeView(View.HISTORY);
            	break;
            case "unres":
            	changeView(View.UNRESOLVED);
            	break;
            case "search":
            	changeView(View.SEARCH);
            	break;
            case "help":
            	changeView(View.HELP);
            	break;
            default:
                // Run the command
                runCommand(textFieldInput);
                break;
            }
        }
    }
	
	private static class KeyPressHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent event) {
        	// If up key pressed
            if (event.getCode() == KeyCode.UP) {
            	String prevCommand = commandHistory.getPrevious();
            	InterfaceController.getTextField().setText(prevCommand);
            	// Required for positionCaret to work correctly
            	Platform.runLater(new Runnable() {
            		@Override
            		public void run() {
            			InterfaceController.getTextField().positionCaret(prevCommand.length());
            		}
            	});
            }
            // If down key pressed
            if (event.getCode() == KeyCode.DOWN) {
            	String nextCommand = commandHistory.getNext();
            	InterfaceController.getTextField().setText(nextCommand);
            	// Required for positionCaret to work correctly
            	Platform.runLater(new Runnable() {
            		@Override
            		public void run() {
            			InterfaceController.getTextField().positionCaret(nextCommand.length());
            		}
            	});
            }
        }
    }
    
    private class ButtonHoverHandler implements EventHandler<MouseEvent> {
    	
    	private View buttonType;
    	
    	ButtonHoverHandler(View buttonType) {
    		this.buttonType = buttonType;
    	}
    	
    	@Override
    	public void handle(MouseEvent event) {
    		// For handling mouse hovers
    		if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
    			ImageView hover;
    			switch(buttonType) {
    			case DEFAULT:
    				if (InterfaceController.getCurrentView() != View.DEFAULT) {
    					hover = new ImageView(InterfaceController.PATH_DEFAULT_HOVER);
    					InterfaceController.getHomeButton().getChildren().clear();
    					InterfaceController.getHomeButton().getChildren().add(hover);
    				}
    				break;
    			case ALL:
    				if (InterfaceController.getCurrentView() != View.ALL) {
    					hover = new ImageView(InterfaceController.PATH_ALL_HOVER);
    					InterfaceController.getAllButton().getChildren().clear();
    					InterfaceController.getAllButton().getChildren().add(hover);
    				}
    				break;
    			case HISTORY:
    				if (InterfaceController.getCurrentView() != View.HISTORY) {
    					hover = new ImageView(InterfaceController.PATH_HIST_HOVER);
    					InterfaceController.getHistButton().getChildren().clear();
    					InterfaceController.getHistButton().getChildren().add(hover);
    				}
    				break;
    			case UNRESOLVED:
    				if (InterfaceController.getCurrentView() != View.UNRESOLVED) {
    					hover = new ImageView(InterfaceController.PATH_UNRESOLVED_HOVER);
    					InterfaceController.getDoneButton().getChildren().clear();
    					InterfaceController.getDoneButton().getChildren().add(hover);
    				}
    				break;
    			case SEARCH:
    				if (InterfaceController.getCurrentView() != View.SEARCH) {
    					hover = new ImageView(InterfaceController.PATH_SEARCH_HOVER);
    					InterfaceController.getSearchButton().getChildren().clear();
    					InterfaceController.getSearchButton().getChildren().add(hover);
    				}
    				break;
    			case HELP:
    				// Do not change the button if help dialog is showing
    				if (!MainApp.help.isShowing()) {
    					hover = new ImageView(InterfaceController.PATH_HELP_HOVER);
    					InterfaceController.getHelpButton().getChildren().clear();
    					InterfaceController.getHelpButton().getChildren().add(hover);
    				}
    				break;
    			default:
    				break;
    			}
    		}
    		// For handling mouse not hovering
    		if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
    			ImageView hover;
    			switch(buttonType) {
    			case DEFAULT:
    				if (InterfaceController.getCurrentView() != View.DEFAULT) {
    					hover = new ImageView(InterfaceController.PATH_DEFAULT);
    					InterfaceController.getHomeButton().getChildren().clear();
    					InterfaceController.getHomeButton().getChildren().add(hover);
    				}
    				break;
    			case ALL:
    				if (InterfaceController.getCurrentView() != View.ALL) {
    					hover = new ImageView(InterfaceController.PATH_ALL);
    					InterfaceController.getAllButton().getChildren().clear();
    					InterfaceController.getAllButton().getChildren().add(hover);
    				}
    				break;
    			case HISTORY:
    				if (InterfaceController.getCurrentView() != View.HISTORY) {
    					hover = new ImageView(InterfaceController.PATH_HIST);
    					InterfaceController.getHistButton().getChildren().clear();
    					InterfaceController.getHistButton().getChildren().add(hover);
    				}
    				break;
    			case UNRESOLVED:
    				if (InterfaceController.getCurrentView() != View.UNRESOLVED) {
    					hover = new ImageView(InterfaceController.PATH_UNRESOLVED);
    					InterfaceController.getDoneButton().getChildren().clear();
    					InterfaceController.getDoneButton().getChildren().add(hover);
    				}
    				break;
    			case SEARCH:
    				if (InterfaceController.getCurrentView() != View.SEARCH) {
    					hover = new ImageView(InterfaceController.PATH_SEARCH);
    					InterfaceController.getSearchButton().getChildren().clear();
    					InterfaceController.getSearchButton().getChildren().add(hover);
    				}
    				break;
    			case HELP:
    				// Do not change the button if help dialog is showing
    				if (!MainApp.help.isShowing()) {
    					hover = new ImageView(InterfaceController.PATH_HELP);
    					InterfaceController.getHelpButton().getChildren().clear();
    					InterfaceController.getHelpButton().getChildren().add(hover);
    				}
    				break;
    			default:
    				break;
    			}
    		}
    	}
    }
    
    private static class ButtonClickHandler implements EventHandler<MouseEvent> {
    	
    	View buttonType;
    	
    	ButtonClickHandler(View buttonType) {
    		this.buttonType = buttonType;
    	}
    	
    	@Override
    	public void handle(MouseEvent event) {
    		changeView(buttonType);
    	}
    }
    
    private static class CloseHelpHandler implements ChangeListener<Boolean> {
    	
    	@Override
    	public void changed(ObservableValue<? extends Boolean> observable, 
    			Boolean oldValue, Boolean newValue) {
    		// If the help window was closed by user's mouse click
    		if (newValue == false) {
    			// Do not toggle to avoid resetting the help open/close state
    			HelpController.closeHelpDialog();
    		}
    	}
    }
    
    private static class ScrollListener implements ChangeListener<Number> {
    	
    	View scrollpane;
    	
    	ScrollListener(View scrollpane) {
    		this.scrollpane = scrollpane;
    	}
    	
    	@Override
    	public void changed(ObservableValue<? extends Number> observable, 
    			Number oldValue, Number newValue) {
    		
    		// Set the v-value of the scroll pane to the height of the content box
    		switch (scrollpane) {
    		case HISTORY:
    			HistoryViewController.getHistScroll().setVvalue((Double)newValue);
    			break;
    		case UNRESOLVED:
    			// TODO
    			//DoneViewController.getDoneScroll().setVvalue((Double)newValue);
    			break;
    		default:
    			// Ignore, should not enter
    			break;
    		}
    	}
    }
    
    private static class HeightListener implements ChangeListener<Number> {

    	@Override
    	public void changed(ObservableValue<? extends Number> observable,
    			Number oldValue, Number newValue) {

    		// Set the height of the sidebar separator to window height
    		InterfaceController.getSbLine().setEndY((Double)newValue);

    		// Set the height of the scroll pane separator to
    		// window height - height of the filepath bar(31) -
    		// height of feedback bar(31) - height of text bar(40) - 
    		// height of viewLine(1)
    		DefaultViewController.getDefScrollLine().setEndY((Double)newValue - 
    				InterfaceController.HEIGHT_FILEPATH - 
    				InterfaceController.HEIGHT_FEEDBACK - 
    				InterfaceController.HEIGHT_TEXT_BOX - 
    				InterfaceController.HEIGHT_HORIZ_LINE);
    	}
    }

    private static class WidthListener implements ChangeListener<Number> {

    	@Override
    	public void changed(ObservableValue<? extends Number> observable,
    			Number oldValue, Number newValue) {

    		// Set the width of the feedback, filepath and view box separators to
    		// window width - size of sidebar(50) - width of line(1)
    		InterfaceController.getFeedbackLine().setEndX(
    				(Double)newValue - InterfaceController.WIDTH_SIDEBAR);
    		InterfaceController.getViewLine().setEndX(
    				(Double)newValue - InterfaceController.WIDTH_SIDEBAR);
    		InterfaceController.getFilepathLine().setEndX(
    				(Double)newValue - InterfaceController.WIDTH_SIDEBAR);
    	}
    }
}
