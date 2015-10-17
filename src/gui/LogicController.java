package gui;

import java.nio.file.FileSystemException;
import java.util.ArrayList;

import backend.Logic;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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
	
	/* ================================================================================
     * Getters to allow InterfaceController to access the private handling classes
     * ================================================================================
     */
	
	public TextInputHandler getTextInputHandler() {
		return new TextInputHandler();
	}
	
	public UpKeyHandler getUpKeyHandler() {
		return new UpKeyHandler();
	}
	
	public DownKeyHandler getDownKeyHandler() {
		return new DownKeyHandler();
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

            /*
             * Perform different operations depending on the currentView and the command
             * ================================================================================
             */
            
            switch(InterfaceController.currentView) {
            // Default view
            case InterfaceController.VIEW_DEFAULT:
            	switch (textFieldInput) {
            	case "all":
            		InterfaceController.currentView = InterfaceController.VIEW_ALL;
            		InterfaceController.updateMainInterface(InterfaceController.VIEW_ALL);
            		break;
            	case "hist":
            		// TODO
            		//InterfaceController.currentView = InterfaceController.VIEW_HISTORY;
            		//InterfaceController.updateMainInterface(HistoryViewController.initHistoryView());
            		break;
            	case "done":
            		// TODO
            		//InterfaceController.currentView = InterfaceController.VIEW_COMPLETED;
            		//InterfaceController.updateMainInterface(DoneViewController.initDoneView());
            		break;
            	default:
            		// Run the operation
                	String returnMessage = logic.executeCommand(textFieldInput);
                	// Add the returnMessage to the History pane
                	InterfaceController.getFeedbackLabel().setText(returnMessage);

                	// Update the Tasks and Events
                	DefaultViewController.updateDefView();
                	break;
            	}
            	break;
            // All view
            case InterfaceController.VIEW_ALL:
            	switch (textFieldInput) {
            	case "def":
            		InterfaceController.currentView = InterfaceController.VIEW_DEFAULT;
            		InterfaceController.updateMainInterface(InterfaceController.VIEW_DEFAULT);
            		break;
            	case "hist":
            		// TODO
            		//InterfaceController.currentView = InterfaceController.VIEW_HISTORY;
            		//InterfaceController.updateMainInterface(HistoryViewController.initHistoryView());
            		break;
            	case "done":
            		// TODO
            		//InterfaceController.currentView = InterfaceController.VIEW_COMPLETED;
            		//InterfaceController.updateMainInterface(DoneViewController.initDoneView());
            		break;
            	default:
            		// Run the operation
                	String returnMessage = logic.executeCommand(textFieldInput);
                	// Add the returnMessage to the History pane
                	InterfaceController.getFeedbackLabel().setText(returnMessage);

                	// Update the Tasks and Events
                	AllViewController.updateAllView();
                	break;
            	}
            	break;
            // History view
            case InterfaceController.VIEW_HIST:
            	switch (textFieldInput) {
            	case "def":
            		InterfaceController.currentView = InterfaceController.VIEW_DEFAULT;
            		InterfaceController.updateMainInterface(InterfaceController.VIEW_DEFAULT);
            		break;
            	case "all":
            		InterfaceController.currentView = InterfaceController.VIEW_ALL;
            		InterfaceController.updateMainInterface(InterfaceController.VIEW_ALL);
            		break;
            	case "done":
            		// TODO
            		//InterfaceController.currentView = InterfaceController.VIEW_COMPLETED;
            		//InterfaceController.updateMainInterface(DoneViewController.initDoneView());
            		break;
            	default:
            		// Run the operation
                	String returnMessage = logic.executeCommand(textFieldInput);
                	// Add the returnMessage to the History pane
                	InterfaceController.getFeedbackLabel().setText(returnMessage);

                	// Update the Tasks and Events
                	DefaultViewController.updateDefView();
                	break;
            	}
            	break;
            // Completed view
            case InterfaceController.VIEW_DONE:
            	switch (textFieldInput) {
            	case "def":
            		InterfaceController.currentView = InterfaceController.VIEW_DEFAULT;
            		InterfaceController.updateMainInterface(InterfaceController.VIEW_DEFAULT);
            		break;
            	case "all":
            		InterfaceController.currentView = InterfaceController.VIEW_ALL;
            		InterfaceController.updateMainInterface(InterfaceController.VIEW_ALL);
            		break;
            	case "hist":
            		// TODO
            		//InterfaceController.currentView = InterfaceController.VIEW_HISTORY;
            		//InterfaceController.updateMainInterface(HistoryViewController.initHistoryView());
            		break;
            	default:
            		// Run the operation
                	String returnMessage = logic.executeCommand(textFieldInput);
                	// Add the returnMessage to the History pane
                	InterfaceController.getFeedbackLabel().setText(returnMessage);

                	// Update the Tasks and Events
                	DefaultViewController.updateDefView();
                	break;
            	}
            	break;
            default: // do nothing, should not enter
            }
            
        }
    }
	
	private static class UpKeyHandler implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.UP) {
                InterfaceController.getTextField().setText(commandHistory.getPrevious());
            }
        }
    }

    private static class DownKeyHandler implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.DOWN) {
                InterfaceController.getTextField().setText(commandHistory.getNext());
            }
        }
    }
    
    private static class HeightListener implements ChangeListener<Number> {

    	@Override
    	public void changed(ObservableValue<? extends Number> observable,
    			Number oldValue, Number newValue) {

    		// Set the height of the sidebar separator to
    		// window height - height of filepath bar(35) - height of line(1)
    		InterfaceController.getSbLine().setEndY((Double)newValue);

    		// Set the height of the scroll pane separator to
    		// window height - height of the filepath bar(35) -
    		// height of feedback bar(35) - height of text bar(45) - 
    		// 2 * height of line(1)
    		DefaultViewController.getDefScrollLine().setEndY((Double)newValue - 
    				InterfaceController.HEIGHT_FILEPATH - 
    				InterfaceController.HEIGHT_FEEDBACK - 
    				InterfaceController.HEIGHT_TEXTFIELD);
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
    		InterfaceController.getDefLine().setEndX(
    				(Double)newValue - InterfaceController.WIDTH_SIDEBAR);
    		InterfaceController.getFilepathLine().setEndX(
    				(Double)newValue - InterfaceController.WIDTH_SIDEBAR);
    	}
    }
}
