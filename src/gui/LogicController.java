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
	protected static final String MESSAGE_EMPTY = "There are no items to display.";
	private static final String MESSAGE_INVALID_INDEX = "Invalid index number entered.";
	
	private static final String CSS_UNDERLINE = "-fx-underline: true;";
	private static final String CSS_NO_UNDERLINE = "-fx-underline: false;";
	private static final String CSS_UNDERLINE_ITALIC = CSS_UNDERLINE + "-fx-font-style: italic;";
	private static final String CSS_NO_UNDERLINE_ITALIC = CSS_NO_UNDERLINE + "-fx-font-style: italic;";
	
	private static final boolean USER_SEARCH = false;
	private static final boolean BACKGROUND_SEARCH = true;
	
	private static Logic logic;
	private static CommandHistory commandHistory;
	
	private static boolean mapIndexOutOfBounds = false;
	
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
		String allTasks = logic.taskAllView(false);
		
		// Split the string by newline
		String[] allTasksSplit = allTasks.split("\n");
		
		return allTasksSplit;
	}
	
	public String[] getAllEvents() {
		
		// Get the String from logic
		String allEvents = logic.eventAllView(false);
		
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
	
	public String[] getDoneTasks() {
		
		// Get the String from logic
		String doneTasks = logic.taskAllView(true);
		
		// Split the string by newline
		String[] doneTasksSplit = doneTasks.split("\n");
		
		return doneTasksSplit;
	}
	
	public String[] getDoneEvents() {
		
		// Get the String from logic
		String doneEvents = logic.eventAllView(true);
		
		// Split the string by newline
		String[] doneEventsSplit = doneEvents.split("\n");
		
		return doneEventsSplit;
	}
    
	public int[] getSummaryCount() {
		
		String[] defTasks = InterfaceController.getLogic().getDefTasks();
		String[] defEvents = InterfaceController.getLogic().getDefEvents();
		int[] summary = {0, 0, 0, 0, 0};
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
		
		// Count the unresolved tasks and events as well
		summary[4] = getUnresElementsCount();
		
		return summary;
	}
	
	public int getDefElementsCount() {
		
		int count = 0;
		
		String[] temp = InterfaceController.getLogic().getDefTasks();
		for (int i = 0; i < temp.length; i++) {
			if (isNonEmptyElement(temp[i])) {
				count++;
			}
		}
		temp = InterfaceController.getLogic().getDefEvents();
		for (int i = 0; i < temp.length; i++) {
			if (isNonEmptyElement(temp[i])) {
				count++;
			}
		}
		
		return count;
	}
	
	public int getAllElementsCount() {
		
		int count = 0;
		
		String[] temp = InterfaceController.getLogic().getAllTasks();
		for (int i = 0; i < temp.length; i++) {
			if (isNonEmptyElement(temp[i])) {
				count++;
			}
		}
		temp = InterfaceController.getLogic().getAllEvents();
		for (int i = 0; i < temp.length; i++) {
			if (isNonEmptyElement(temp[i])) {
				count++;
			}
		}
		
		return count;
	}
	
	public int getSearchElementsCount(ArrayList<String> taskResults, 
			ArrayList<String> eventResults) {
		
		int count = 0;
		
		for (int i = 0; i < taskResults.size(); i++) {
			if (isNonEmptyElement(taskResults.get(i))) {
				count++;
			}
		}
		
		for (int i = 0; i < eventResults.size(); i++) {
			if (isNonEmptyElement(eventResults.get(i))) {
				count++;
			}
		}
		
		return count;
	}
	
	public int getUnresElementsCount() {
		
		int count = 0;
		
		String[] temp = InterfaceController.getLogic().getUnresTasks();
		for (int i = 0; i < temp.length; i++) {
			if (isNonEmptyElement(temp[i])) {
				count++;
			}
		}
		temp = InterfaceController.getLogic().getUnresEvents();
		for (int i = 0; i < temp.length; i++) {
			if (isNonEmptyElement(temp[i])) {
				count++;
			}
		}
		
		return count;
	}
	
	public int getDoneElementsCount() {
		
		int count = 0;
		
		String[] temp = InterfaceController.getLogic().getDoneTasks();
		for (int i = 0; i < temp.length; i++) {
			if (isNonEmptyElement(temp[i])) {
				count++;
			}
		}
		temp = InterfaceController.getLogic().getDoneEvents();
		for (int i = 0; i < temp.length; i++) {
			if (isNonEmptyElement(temp[i])) {
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
    
	public boolean isEmpty(String displayData) {
		return displayData.equals(LogicController.MESSAGE_EMPTY);
	}
    
    public boolean isTitleOrDate(String displayData) {
    	// Use the definition that a date or title does not have a period in it
    	// whereas an element will definitely have a period after its index
    	return displayData.split(Pattern.quote(".")).length == 1 && 
    			!displayData.equals(MESSAGE_EMPTY);
    }
    
    public boolean isNonEmptyElement(String displayData) {
    	return !isTitleOrDate(displayData) && 
				!displayData.equals(LogicController.MESSAGE_EMPTY);
    }
    
	public boolean isCompleted(String displayData) {
		return displayData.split(" ")[0].equals("done");
	}
    
    private static String mapToFileIndex(String textFieldInput) {
    	
    	String[] textFieldInputSplit = textFieldInput.split(" ");
    	String modifiedString = textFieldInput;
    	try {
    		int viewIndex = Integer.parseInt(textFieldInputSplit[1]);
        	int fileIndex = ViewIndexMap.get(viewIndex);
        	
        	// Check if the index has exceeded the allowable size of the array
        	// -1 should be returned by ViewIndexMap.get()
        	mapIndexOutOfBounds = fileIndex == -1 && viewIndex != -1;
        	
        	// Proceed with normal operation
        	// Negative and zero indices are handled by CommandParser
        	textFieldInputSplit[1] = String.valueOf(fileIndex);
        	modifiedString = "";
        	for (int i = 0; i < textFieldInputSplit.length; i++) {
        		modifiedString += textFieldInputSplit[i] + " ";
        	}
        	// Remove the extra space appended by the for loop
        	modifiedString = modifiedString.substring(0, modifiedString.length() - 1);
    	} catch (NumberFormatException e) {
    		// User did not enter an integer and hence exception is thrown
    		// Do not modify the string and pass through to CommandParser to reject
    		mapIndexOutOfBounds = true;
    	}
    	
    	return modifiedString;
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
        	case DONE:
        		InterfaceController.updateMainInterface(View.DONE);
        		break;
        	case SEARCH:
        		InterfaceController.updateMainInterface(View.SEARCH);
        		break;
        	case HELP:
        		HelpController.toggleHelpDialog();
        		break;
        	case SUMMARY:
        		InterfaceController.updateMainInterface(View.SUMMARY);
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
        	case DONE:
        		InterfaceController.updateMainInterface(View.DONE);
        		break;
        	case SEARCH:
        		InterfaceController.updateMainInterface(View.SEARCH);
        		break;
        	case HELP:
        		HelpController.toggleHelpDialog();
        		break;
        	case SUMMARY:
        		InterfaceController.updateMainInterface(View.SUMMARY);
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
        	case DONE:
        		InterfaceController.updateMainInterface(View.DONE);
        		break;
        	case SEARCH:
        		InterfaceController.updateMainInterface(View.SEARCH);
        		break;
        	case HELP:
        		HelpController.toggleHelpDialog();
        		break;
        	case SUMMARY:
        		InterfaceController.updateMainInterface(View.SUMMARY);
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
         * Unresolved view
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
        	case DONE:
        		InterfaceController.updateMainInterface(View.DONE);
        		break;
        	case SEARCH:
        		InterfaceController.updateMainInterface(View.SEARCH);
        		break;
        	case HELP:
        		HelpController.toggleHelpDialog();
        		break;
        	case SUMMARY:
        		InterfaceController.updateMainInterface(View.SUMMARY);
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
        case DONE:
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
        	case SEARCH:
        		InterfaceController.updateMainInterface(View.SEARCH);
        		break;
        	case HELP:
        		HelpController.toggleHelpDialog();
        		break;
        	case SUMMARY:
        		InterfaceController.updateMainInterface(View.SUMMARY);
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
        	case DONE:
        		InterfaceController.updateMainInterface(View.DONE);
        		break;
        	case HELP:
        		HelpController.toggleHelpDialog();
        		break;
        	case SUMMARY:
        		InterfaceController.updateMainInterface(View.SUMMARY);
        		break;
        	case EXIT:
        		InterfaceController.closeMainInterface();
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

	private static void runCommand(Command.CommandType operationType, String textFieldInput, boolean isBackgroundUpdate) {
		
		// Execute the command
		String returnMessage = logic.executeCommand(textFieldInput);
		
		if (operationType == Command.CommandType.SEARCH) {
			// Do not update the feedback bar and history view if the search operation
			// is a background update of the last search term
			if (!isBackgroundUpdate) {
				// Add the returnMessage to the feedback bar and history view
				InterfaceController.getFeedbackLabel().setText("Displaying results...");
				HistoryViewController.updateHistView("Displaying results...");
			}
			
			SearchViewController.updateSearchView(returnMessage);
		} else {
			// Modify the return message first if it is incorrect and is an operation
			// that uses indices (delete, done, edit)
			if (mapIndexOutOfBounds && 
					(operationType == Command.CommandType.DELETE || 
					operationType == Command.CommandType.DONE || 
					operationType == Command.CommandType.EDIT)) {
				returnMessage = MESSAGE_INVALID_INDEX;
			}
			// Add the returnMessage to the feedback bar and history view
			InterfaceController.getFeedbackLabel().setText(returnMessage);
			HistoryViewController.updateHistView(returnMessage);
		}

		// Update the necessary views
		DefaultViewController.updateDefView();
		AllViewController.updateAllView();
		UnresolvedViewController.updateUnresView();
		DoneViewController.updateDoneView();
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
	
	public HotKeyHandler getHotKeyHandler() {
		return new HotKeyHandler();
	}
	
	public HelpHotKeyHandler getHelpHotKeyHandler() {
		return new HelpHotKeyHandler();
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
	
	public UnresHoverHandler getUnresHoverHandler(Label allUnresAttention) {
		return new UnresHoverHandler(allUnresAttention);
	}
	
	public UnresClickHandler getUnresClickHandler() {
		return new UnresClickHandler();
	}
	
	public ConfigClickHandler getConfigClickHandler() {
		return new ConfigClickHandler();
	}
	
	public AutoCompleteSelectHandler getAutoCompleteSelectHandler() {
		return new AutoCompleteSelectHandler();
	}
	
	public AutoCompleteListener getAutoCompleteListener() {
		return new AutoCompleteListener();
	}
	
	public LostFocusListener getLostFocusListener() {
		return new LostFocusListener();
	}
	
	public CloseHelpListener getCloseHelpListener() {
		return new CloseHelpListener();
	}
	
	public ScrollListener getScrollListener(View scrollpane) {
		return new ScrollListener(scrollpane);
	}
	
	public WidthPositionListener getWidthPositionListener() {
		return new WidthPositionListener();
	}
	
	public HeightPositionListener getHeightPositionListener() {
		return new HeightPositionListener();
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
		
		private String lastSearchCommand = "";
		
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
            case "done":
            	changeView(View.DONE);
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
            		// Store the last search command to run the search again dynamically
            		// upon the user's next operation
            		lastSearchCommand = textFieldInput;
                    // Run the command
                    runCommand(operationType, textFieldInput, USER_SEARCH);
                    changeView(View.SEARCH);
            		break;
            	// Only modify the user command for these operations by editing the 
            	// index from ViewIndexMap
            	case DELETE:
                    // Run the command
                    runCommand(operationType, mapToFileIndex(textFieldInput), USER_SEARCH);
                    
            		// Run the last search and update the search view only if the user is in search
            		if (!lastSearchCommand.equals("") && InterfaceController.getCurrentView() == View.SEARCH) {
            			runCommand(Command.CommandType.SEARCH, lastSearchCommand, BACKGROUND_SEARCH);
            			InterfaceController.updateMainInterface(View.SEARCH);
            		}
            		// If the user is not in search view, do not switch to search view
            		if (!lastSearchCommand.equals("") && InterfaceController.getCurrentView() != View.SEARCH) {
            			runCommand(Command.CommandType.SEARCH, lastSearchCommand, BACKGROUND_SEARCH);
            		}
            		break;
            	case EDIT:
                    // Run the command
                    runCommand(operationType, mapToFileIndex(textFieldInput), USER_SEARCH);
                    
            		// Run the last search and update the search view only if the user is in search
            		if (!lastSearchCommand.equals("") && InterfaceController.getCurrentView() == View.SEARCH) {
            			runCommand(Command.CommandType.SEARCH, lastSearchCommand, BACKGROUND_SEARCH);
            			InterfaceController.updateMainInterface(View.SEARCH);
            		}
            		// If the user is not in search view, do not switch to search view
            		if (!lastSearchCommand.equals("") && InterfaceController.getCurrentView() != View.SEARCH) {
            			runCommand(Command.CommandType.SEARCH, lastSearchCommand, BACKGROUND_SEARCH);
            		}
            		break;
            	case DONE:
                    // Run the command
                    runCommand(operationType, mapToFileIndex(textFieldInput), USER_SEARCH);
                    
            		// Run the last search and update the search view only if the user is in search
            		if (!lastSearchCommand.equals("") && InterfaceController.getCurrentView() == View.SEARCH) {
            			runCommand(Command.CommandType.SEARCH, lastSearchCommand, BACKGROUND_SEARCH);
            			InterfaceController.updateMainInterface(View.SEARCH);
            		}
            		// If the user is not in search view, do not switch to search view
            		if (!lastSearchCommand.equals("") && InterfaceController.getCurrentView() != View.SEARCH) {
            			runCommand(Command.CommandType.SEARCH, lastSearchCommand, BACKGROUND_SEARCH);
            		}
            		break;
            	default:
                    // Run the command
                    runCommand(operationType, textFieldInput, USER_SEARCH);
                    
            		// Run the last search and update the search view only if the user is in search
            		if (!lastSearchCommand.equals("") && InterfaceController.getCurrentView() == View.SEARCH) {
            			runCommand(Command.CommandType.SEARCH, lastSearchCommand, BACKGROUND_SEARCH);
            			InterfaceController.updateMainInterface(View.SEARCH);
            		}
            		// If the user is not in search view, do not switch to search view
            		if (!lastSearchCommand.equals("") && InterfaceController.getCurrentView() != View.SEARCH) {
            			runCommand(Command.CommandType.SEARCH, lastSearchCommand, BACKGROUND_SEARCH);
            		}
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
        		// Only register keypress when autocomplete is now showing
        		if (!AutoComplete.isShowing()) {
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
        	}
            // If down key pressed
        	if (event.getCode() == KeyCode.DOWN) {
        		// Only register keypress when autocomplete is not showing
        		if (!AutoComplete.isShowing()) {
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
    }
	
	private static class TabPressHandler implements EventHandler<KeyEvent> {
		
		@Override
		public void handle(KeyEvent event) {
			// Display the summary view
			if (event.getCode() == KeyCode.TAB) {
				if (!SummaryViewController.isShowing()) {
					SummaryViewController.startShowing();
					changeView(View.SUMMARY);
				} else {
					SummaryViewController.stopShowing();
					InterfaceController.updateMainInterface(InterfaceController.getCurrentView());
				}
			}
		}
	}

	private static class HotKeyHandler implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent event) {
			if (event.isControlDown()) {
				switch (event.getCode()) {
				// For regular number keys
				case DIGIT1:
					changeView(View.DEFAULT);
					break;
				case DIGIT2:
					changeView(View.ALL);
					break;
				case DIGIT3:
					changeView(View.HISTORY);
					break;
				case DIGIT4:
					changeView(View.UNRESOLVED);
					break;
				case DIGIT5:
					changeView(View.DONE);
					break;
				case DIGIT6:
					changeView(View.SEARCH);
					break;
				case DIGIT7:
					changeView(View.HELP);
					break;
				// For users with a number pad
				case NUMPAD1:
					changeView(View.DEFAULT);
					break;
				case NUMPAD2:
					changeView(View.ALL);
					break;
				case NUMPAD3:
					changeView(View.HISTORY);
					break;
				case NUMPAD4:
					changeView(View.UNRESOLVED);
					break;
				case NUMPAD5:
					changeView(View.DONE);
					break;
				case NUMPAD6:
					changeView(View.SEARCH);
					break;
				case NUMPAD7:
					changeView(View.HELP);
					break;
				// For opening text and config files
				case T:
					InterfaceController.openFileLocation();
					break;
				case S:
					InterfaceController.openConfigLocation();
					break;
				default:
					// Do nothing
					break;
				}
			} else {
				// Do nothing
			}
		}
	}
	
	private class HelpHotKeyHandler implements EventHandler<KeyEvent> {
		
		@Override
		public void handle(KeyEvent event) {
			if (event.isControlDown()) {
				switch (event.getCode()) {
				// For regular number key
				case DIGIT7:
					changeView(View.HELP);
					break;
				case NUMPAD7:
					changeView(View.HELP);
					break;
				default:
					// Do nothing
					break;
				}
			} else {
				// Do nothing
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
    					InterfaceController.getUnresButton().getChildren().clear();
    					InterfaceController.getUnresButton().getChildren().add(hover);
    				}
    				break;
    			case DONE:
    				if (InterfaceController.getCurrentView() != View.DONE) {
    					hover = new ImageView(InterfaceController.PATH_DONE_HOVER);
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
    					InterfaceController.getUnresButton().getChildren().clear();
    					InterfaceController.getUnresButton().getChildren().add(hover);
    				}
    				break;
    			case DONE:
    				if (InterfaceController.getCurrentView() != View.DONE) {
    					hover = new ImageView(InterfaceController.PATH_DONE);
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
    		// When a user clicks a button without pressing TAB to exit the summary view
    		// Sets the isShowing value in SummaryViewController
    		if (SummaryViewController.isShowing()) {
    			SummaryViewController.stopShowing();
    		}
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
    
    private static class UnresHoverHandler implements EventHandler<MouseEvent> {
    	
    	Label allUnresAttention;
    	
    	UnresHoverHandler(Label allUnresAttention) {
    		this.allUnresAttention = allUnresAttention;
    	}
    	
    	@Override
    	public void handle(MouseEvent event) {
    		if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
    			allUnresAttention.setStyle(CSS_UNDERLINE);
    			allUnresAttention.setStyle(CSS_UNDERLINE_ITALIC);
    		}
    		if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
    			allUnresAttention.setStyle(CSS_NO_UNDERLINE);
    			allUnresAttention.setStyle(CSS_NO_UNDERLINE_ITALIC);
    		}
    	}
    }
    
    private static class UnresClickHandler implements EventHandler<MouseEvent> {
    	@Override
    	public void handle(MouseEvent event) {
    		SummaryViewController.stopShowing();
    		InterfaceController.updateMainInterface(View.UNRESOLVED);
    	}
    }
    
    private static class ConfigClickHandler implements EventHandler<MouseEvent> {
    	
    	@Override
    	public void handle(MouseEvent event) {
    		InterfaceController.openConfigLocation();
    	}
    }
    
    private static class AutoCompleteSelectHandler implements EventHandler<KeyEvent> {
    	
    	@Override
    	public void handle(KeyEvent event) {
    		if (event.getCode() == KeyCode.ENTER) {
    			
    			InterfaceController.getTextField().setText(AutoComplete.getSelectedItem());
    			
    			// Position the caret
    			Platform.runLater(new Runnable() {
    				@Override
    				public void run() {
    					String text = InterfaceController.getTextField().getText();
    					InterfaceController.getTextField().positionCaret(text.length());
    				}
    			});
    			
    			AutoComplete.closePopup();
    		}
    	}
    }
    
    private static class AutoCompleteListener implements ChangeListener<String> {
    	@Override
    	public void changed(ObservableValue<? extends String> observable, 
    			String oldValue, String newValue) {
    		// Only perform autocompletion when the string is within one word
    		// and is not empty
    		if (newValue.split(" ").length == 1 && !newValue.equals("")) {
    			AutoComplete.updatePopup(newValue);
    		} else {
    			AutoComplete.closePopup();
    		}
    	}
    }
    
    private static class LostFocusListener implements ChangeListener<Boolean> {
    	
    	boolean showingBeforeLostFocus = false;
    	
    	@Override
    	public void changed(ObservableValue<? extends Boolean> observable, 
    			Boolean oldValue, Boolean newValue) {
    		        	
    		if (!newValue) {
    			showingBeforeLostFocus = AutoComplete.isShowing();
    			AutoComplete.closePopup();
    		} else {
    			if (showingBeforeLostFocus) {
    				AutoComplete.showPopup();
    			}
    		}
    	}
    }
    
    private static class CloseHelpListener implements ChangeListener<Boolean> {
    	
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
    		default:
    			// Ignore, should not enter
    			break;
    		}
    	}
    }
    
    private static class WidthPositionListener implements ChangeListener<Number> {
    	
    	@Override
    	public void changed(ObservableValue<? extends Number> observable, 
    			Number oldValue, Number newValue) {
    		AutoComplete.setX((double)newValue);
    	}
    }
    
    private static class HeightPositionListener implements ChangeListener<Number> {
    	
    	@Override
    	public void changed(ObservableValue<? extends Number> observable, 
    			Number oldValue, Number newValue) {
    		AutoComplete.setY((double)newValue);
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
    		
    		AutoComplete.setY(MainApp.stage.getY());
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
