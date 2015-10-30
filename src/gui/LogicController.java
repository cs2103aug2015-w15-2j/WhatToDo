package gui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import backend.Logic;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import struct.Command;
import struct.View;

public class LogicController {

	private static final String MESSAGE_ERROR_FILESYSTEM = "Failed to create the file.";
	private static final String CSS_UNDERLINE = "-fx-underline: true";
	private static final String CSS_NO_UNDERLINE = "-fx-underline: false";
	
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
	
	public String[] getDefTasks() {
		
		// Get the string from logic
		String defTasks = logic.taskDefaultView();
		
		// Split the string by newline
		String[] defTasksSplit = defTasks.split("\n");
		
		return defTasksSplit;
	}
	
	public String[] getDefEvents() {
		
		// Get the string from logic
		String defEvents = logic.eventDefaultView();
		
		// Split the string by newline
		String[] defEventsSplit = defEvents.split("\n");

		return defEventsSplit;
	}
	
	public String[] getAllTasks() {
		
		// Get the String from logic
		String allTasks = logic.taskAllUncompletedView();
		
		// Split the string by newline
		String[] allTasksSplit = allTasks.split("\n");
		
		return allTasksSplit;
	}
	
	public String[] getAllEvents() {
		
		// Get the String from logic
		String allEvents = logic.eventAllUncompletedView();
		
		// Split the string by newline
		String[] allEventsSplit = allEvents.split("\n");
		
		return allEventsSplit;
	}
	
	public String[] getUnresTasks() {
		
		// Get the String from logic
		String unresTasks = logic.taskPastUncompletedView();
		
		// Split the string by newline
		String[] unresTasksSplit = unresTasks.split("\n");
		
		return unresTasksSplit;
	}
	
	public String[] getUnresEvents() {
		
		// Get the String from logic
		String unresEvents = logic.eventPastUncompletedView();
		
		// Split the string by newline
		String[] unresEventsSplit = unresEvents.split("\n");
		
		return unresEventsSplit;
	}
    
	public int[] getSummaryCount() {
		
		String[] defTasks = InterfaceController.logicControl.getDefTasks();
		String[] defEvents = InterfaceController.logicControl.getDefEvents();
		int[] summary = {0, 0, 0, 0};
		int currentIndex = 0;
		
		// First count for tasks and update the count array
		for (int i = 0; i < defTasks.length; i++) {
			String temp = defTasks[i];
			if (isTitle(temp)) {
				// Switch array index to increment the right counter
				temp = temp.split(" ")[0];
				switch(temp) {
				case "TODAY":
					currentIndex = 0;
					break;
				case "TOMORROW":
					currentIndex = 0;
					break;
				case "FLOAT":
					currentIndex = 2;
					break;
				default:
					break;
				}
			} else {
				// Increment the counter in the currentIndex
				if (temp.split(Pattern.quote(".")).length > 1) {
					summary[currentIndex]++;
				}
			}
		}
		
		// Count for events and update the count array
		for (int i = 0; i < defEvents.length; i++) {
			String temp = defEvents[i];
			if (isTitle(temp)) {
				// Switch array index to increment the right counter
				temp = temp.split(" ")[0];
				switch(temp) {
				case "TODAY":
					currentIndex = 1;
					break;
				case "TOMORROW":
					currentIndex = 1;
					break;
				case "ONGOING":
					currentIndex = 3;
					break;
				default:
					break;
				}
			} else {
				// Increment the counter in the currentIndex
				if (temp.split(Pattern.quote(".")).length > 1) {
					summary[currentIndex]++;
				}
			}
		}
		
		return summary;
	}
	
	public int getDefElementsCount() {
		
		int count = 0;
		
		String[] temp = InterfaceController.logicControl.getDefTasks();
		for (int i = 0; i < temp.length; i++) {
			if (!isTitleOrDate(temp[i])) {
				count++;
			}
		}
		temp = InterfaceController.logicControl.getDefEvents();
		for (int i = 0; i < temp.length; i++) {
			if (!isTitleOrDate(temp[i])) {
				count++;
			}
		}
		
		return count;
	}
	
	public int getAllElementsCount() {
		
		int count = 0;
		
		String[] temp = InterfaceController.logicControl.getAllTasks();
		for (int i = 0; i < temp.length; i++) {
			if (!isTitleOrDate(temp[i])) {
				count++;
			}
		}
		temp = InterfaceController.logicControl.getAllEvents();
		for (int i = 0; i < temp.length; i++) {
			if (!isTitleOrDate(temp[i])) {
				count++;
			}
		}
		
		return count;
	}
	
	public int getSearchElementsCount(ArrayList<String> taskResults, 
			ArrayList<String> eventResults) {
		
		int count = 0;
		
		for (int i = 0; i < taskResults.size(); i++) {
			if (!isTitleOrDate(taskResults.get(i))) {
				count++;
			}
		}
		
		for (int i = 0; i < eventResults.size(); i++) {
			if (!isTitleOrDate(eventResults.get(i))) {
				count++;
			}
		}
		
		return count;
	}
	
	public int getUnresElementsCount() {
		
		int count = 0;
		
		String[] temp = InterfaceController.logicControl.getUnresTasks();
		for (int i = 0; i < temp.length; i++) {
			if (!isTitleOrDate(temp[i])) {
				count++;
			}
		}
		temp = InterfaceController.logicControl.getUnresEvents();
		for (int i = 0; i < temp.length; i++) {
			if (!isTitleOrDate(temp[i])) {
				count++;
			}
		}
		
		return count;
	}
	
    public boolean isTitle(String displayData) {
    	
    	String firstWord = displayData.split(" ")[0];
    	return firstWord.equals("FLOAT") || firstWord.equals("TODAY") || 
    			firstWord.equals("TOMORROW") || firstWord.equals("ONGOING");
    }
    
    public boolean isTitleOrDate(String displayData) {
    	// Use the definition that a date or title does not have a period in it
    	// whereas an element will definitely have a period after its index
    	return displayData.split(Pattern.quote(".")).length == 1;
    }
    
    private static String mapToFileIndex(String textFieldInput) {
    	
    	String[] textFieldInputSplit = textFieldInput.split(" ");
    	int viewIndex = Integer.parseInt(textFieldInputSplit[1]);
    	
    	textFieldInputSplit[1] = String.valueOf(ViewIndexMap.get(viewIndex));
    	String modifiedString = "";
    	for (int i = 0; i < textFieldInputSplit.length; i++) {
    		modifiedString += textFieldInputSplit[i] + " ";
    	}
    	// Remove the extra space appended by the for loop
    	return modifiedString.substring(0, modifiedString.length() - 1);
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
        		InterfaceController.updateMainInterface(View.UNRESOLVED);
        		break;
        	case SEARCH:
        		InterfaceController.updateMainInterface(View.SEARCH);
        		break;
        	case HELP:
        		HelpController.toggleHelpDialog();
        		break;
        	case EXIT:
        		InterfaceController.closeMainInterface();
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
        		InterfaceController.updateMainInterface(View.UNRESOLVED);
        		break;
        	case SEARCH:
        		InterfaceController.updateMainInterface(View.SEARCH);
        		break;
        	case HELP:
        		HelpController.toggleHelpDialog();
        		break;
        	case EXIT:
        		InterfaceController.closeMainInterface();
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
        		InterfaceController.updateMainInterface(View.UNRESOLVED);
        		break;
        	case SEARCH:
        		InterfaceController.updateMainInterface(View.SEARCH);
        		break;
        	case HELP:
        		HelpController.toggleHelpDialog();
        		break;
        	case EXIT:
        		InterfaceController.closeMainInterface();
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
        		InterfaceController.updateMainInterface(View.SEARCH);
        		break;
        	case HELP:
        		HelpController.toggleHelpDialog();
        		break;
        	case EXIT:
        		InterfaceController.closeMainInterface();
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
        		break;
        	case ALL:
        		InterfaceController.updateMainInterface(View.ALL);
        		break;
        	case HISTORY:
        		InterfaceController.updateMainInterface(View.HISTORY);
        		break;
        	case UNRESOLVED:
        		InterfaceController.updateMainInterface(View.UNRESOLVED);
        		break;
        	case HELP:
        		HelpController.toggleHelpDialog();
        		break;
        	default:
        		// Do nothing if already in this view
        		break;
        	case EXIT:
        		InterfaceController.closeMainInterface();
        		break;
        	}
        	break;
        	
        default: // do nothing, should not enter
        	break;
        }
	}

	private static void runCommand(String textFieldInput, boolean isSearch) {
		
		// Execute the command
		String returnMessage = logic.executeCommand(textFieldInput);
		
		if (isSearch) {
			// Add the returnMessage to the feedback bar and history view
			InterfaceController.getFeedbackLabel().setText("Displaying results...");
			HistoryViewController.updateHistView("Displaying results...");
			
			SearchViewController.updateSearchView(returnMessage);
		} else {
			// Add the returnMessage to the feedback bar and history view
			InterfaceController.getFeedbackLabel().setText(returnMessage);
			HistoryViewController.updateHistView(returnMessage);
		}

		// Update the necessary views
		DefaultViewController.updateDefView();
		AllViewController.updateAllView();
		UnresolvedViewController.updateUnresView();
		InterfaceController.updateFilePathBar();
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
	
	public TabPressHandler getTabPressHandler() {
		return new TabPressHandler();
	}
	
	public ButtonHoverHandler getButtonHoverHandler(View buttonType) {
		return new ButtonHoverHandler(buttonType);
	}
	
	public ButtonClickHandler getButtonClickHandler(View buttonType) {
		return new ButtonClickHandler(buttonType);
	}
	
	public PathHoverHandler getPathHoverHandler(Label filepathLabel) {
		return new PathHoverHandler(filepathLabel);
	}
	
	public PathClickHandler getPathClickHandler() {
		return new PathClickHandler();
	}
	
	public ConfigClickHandler getConfigClickHandler() {
		return new ConfigClickHandler();
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

            // Do a preliminary parse to determine the type of operation
            Command.CommandType operationType = logic.getCommandType(textFieldInput);
            
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
            case "help":
            	changeView(View.HELP);
            	break;
            case "openfile":
            	InterfaceController.openFileLocation();
            	break;
            case "config":
            	InterfaceController.openConfigLocation();
            	break;
            default:
            	// Perform branching based on the operation type
            	switch (operationType) {
            	case EXIT:
            		changeView(View.EXIT);
            		break;
            	case SEARCH:
                    // Run the command
                    runCommand(textFieldInput, true);
                    changeView(View.SEARCH);
            		break;
            	// Only modify the user command for these operations by editing the 
            	// index from ViewIndexMap
            	case DELETE:
                    // Run the command
                    runCommand(mapToFileIndex(textFieldInput), false);
            		break;
            	case EDIT:
                    // Run the command
                    runCommand(mapToFileIndex(textFieldInput), false);
            		break;
            	case DONE:
                    // Run the command
                    runCommand(mapToFileIndex(textFieldInput), false);
            		break;
            	default:
                    // Run the command
                    runCommand(textFieldInput, false);
            		break;
            	}
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
	
	private static class TabPressHandler implements EventHandler<KeyEvent> {
		
		private static View previous;
		
		@Override
		public void handle(KeyEvent event) {
			// Display the summary view
			if (event.getEventType() == KeyEvent.KEY_PRESSED) {
				if (event.getCode() == KeyCode.TAB) {
					// Prevent infinite looping when user keeps the TAB key pressed
					if (InterfaceController.getCurrentView() != View.SUMMARY) {
						previous = InterfaceController.getCurrentView();
					}
					InterfaceController.updateMainInterface(View.SUMMARY);
				}
			}
			// Revert back to the previous view
			if (event.getEventType() == KeyEvent.KEY_RELEASED) {					
				if (event.getCode() == KeyCode.TAB) {
					InterfaceController.updateMainInterface(previous);
				}
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
    				if (InterfaceController.getCurrentView() != View.DEFAULT && 
    				InterfaceController.getCurrentView() != View.SUMMARY) {
    					hover = new ImageView(InterfaceController.PATH_DEFAULT_HOVER);
    					InterfaceController.getHomeButton().getChildren().clear();
    					InterfaceController.getHomeButton().getChildren().add(hover);
    				}
    				break;
    			case ALL:
    				if (InterfaceController.getCurrentView() != View.ALL && 
    				InterfaceController.getCurrentView() != View.SUMMARY) {
    					hover = new ImageView(InterfaceController.PATH_ALL_HOVER);
    					InterfaceController.getAllButton().getChildren().clear();
    					InterfaceController.getAllButton().getChildren().add(hover);
    				}
    				break;
    			case HISTORY:
    				if (InterfaceController.getCurrentView() != View.HISTORY && 
    				InterfaceController.getCurrentView() != View.SUMMARY) {
    					hover = new ImageView(InterfaceController.PATH_HIST_HOVER);
    					InterfaceController.getHistButton().getChildren().clear();
    					InterfaceController.getHistButton().getChildren().add(hover);
    				}
    				break;
    			case UNRESOLVED:
    				if (InterfaceController.getCurrentView() != View.UNRESOLVED && 
    				InterfaceController.getCurrentView() != View.SUMMARY) {
    					hover = new ImageView(InterfaceController.PATH_UNRESOLVED_HOVER);
    					InterfaceController.getDoneButton().getChildren().clear();
    					InterfaceController.getDoneButton().getChildren().add(hover);
    				}
    				break;
    			case SEARCH:
    				if (InterfaceController.getCurrentView() != View.SEARCH && 
    				InterfaceController.getCurrentView() != View.SUMMARY) {
    					hover = new ImageView(InterfaceController.PATH_SEARCH_HOVER);
    					InterfaceController.getSearchButton().getChildren().clear();
    					InterfaceController.getSearchButton().getChildren().add(hover);
    				}
    				break;
    			case HELP:
    				// Do not change the button if help dialog is showing
    				if (!MainApp.help.isShowing() && 
    						InterfaceController.getCurrentView() != View.SUMMARY) {
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
    				if (InterfaceController.getCurrentView() != View.DEFAULT && 
    				InterfaceController.getCurrentView() != View.SUMMARY) {
    					hover = new ImageView(InterfaceController.PATH_DEFAULT);
    					InterfaceController.getHomeButton().getChildren().clear();
    					InterfaceController.getHomeButton().getChildren().add(hover);
    				}
    				break;
    			case ALL:
    				if (InterfaceController.getCurrentView() != View.ALL && 
    				InterfaceController.getCurrentView() != View.SUMMARY) {
    					hover = new ImageView(InterfaceController.PATH_ALL);
    					InterfaceController.getAllButton().getChildren().clear();
    					InterfaceController.getAllButton().getChildren().add(hover);
    				}
    				break;
    			case HISTORY:
    				if (InterfaceController.getCurrentView() != View.HISTORY && 
    				InterfaceController.getCurrentView() != View.SUMMARY) {
    					hover = new ImageView(InterfaceController.PATH_HIST);
    					InterfaceController.getHistButton().getChildren().clear();
    					InterfaceController.getHistButton().getChildren().add(hover);
    				}
    				break;
    			case UNRESOLVED:
    				if (InterfaceController.getCurrentView() != View.UNRESOLVED && 
    				InterfaceController.getCurrentView() != View.SUMMARY) {
    					hover = new ImageView(InterfaceController.PATH_UNRESOLVED);
    					InterfaceController.getDoneButton().getChildren().clear();
    					InterfaceController.getDoneButton().getChildren().add(hover);
    				}
    				break;
    			case SEARCH:
    				if (InterfaceController.getCurrentView() != View.SEARCH && 
    				InterfaceController.getCurrentView() != View.SUMMARY) {
    					hover = new ImageView(InterfaceController.PATH_SEARCH);
    					InterfaceController.getSearchButton().getChildren().clear();
    					InterfaceController.getSearchButton().getChildren().add(hover);
    				}
    				break;
    			case HELP:
    				// Do not change the button if help dialog is showing
    				if (!MainApp.help.isShowing() && 
    						InterfaceController.getCurrentView() != View.SUMMARY) {
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
    
    private static class PathHoverHandler implements EventHandler<MouseEvent> {
    	
    	Label filepathLabel;
    	
    	PathHoverHandler(Label filepathLabel) {
    		this.filepathLabel = filepathLabel;
    	}
    	
    	@Override
    	public void handle(MouseEvent event) {
    		
    		if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
    			filepathLabel.setStyle(CSS_UNDERLINE);
    		}
    		
    		if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
    			filepathLabel.setStyle(CSS_NO_UNDERLINE);
    		}
    	}
    }
    
    private static class PathClickHandler implements EventHandler<MouseEvent> {
    	
    	@Override
    	public void handle(MouseEvent event) {
    		InterfaceController.openFileLocation();
    	}
    }
    
    private static class ConfigClickHandler implements EventHandler<MouseEvent> {
    	
    	@Override
    	public void handle(MouseEvent event) {
    		InterfaceController.openConfigLocation();
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
