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
import struct.Alias;
import struct.Command;
import struct.View;

public class LogicController {
	
	private static final String PATH_CONFIG_FILE = "config" + File.separator + "config.txt";
	private static final String NULL_STRING = "";
	
	private static final boolean SEARCH_USER = false;
	private static final boolean SEARCH_BACKGROUND = true;
	
	// Class instances within one LogicController instance
	private static AutoCompleteListener autocompleter;
	private static Logic logic;
	private static CommandHistory commandHistory;
	
	private static boolean mapIndexOutOfBounds = false;
	
	/**
	 * This method is the constructor for a LogicController object.
	 * Possibly catches a FileSystemException from Storage caught by Logic and
	 * passed on to UI.
	 */
	protected LogicController() {
		try {
			logic = new Logic();
		} catch (FileSystemException e) {
			logic = null;
		}
		
		// Initialize the command history object
		commandHistory = new CommandHistory();
		// Initialize the listener for autocomplete
		autocompleter = new AutoCompleteListener();
	}
	
    // ======================================================================
    // Protected methods called by other GUI classes that require some
	// processing of data or Logic methods
    // ======================================================================
	
	/**
	 * This method returns the current filepath of the text file being written
	 * to by the application
	 * 
	 * @return A String containing the filepath of whattodo.txt, or an error
	 * 		   message if the file cannot be found/written
	 */
	protected String getFilePath() {
		if (logic != null) {
			return logic.getFilepath();
		} else {
			return InterfaceController.MESSAGE_ERROR_FILESYSTEM;
		}
	}
	
	/**
	 * This method returns default tasks filtered by Logic in a String[]
	 * for easier formatting by DefaultViewController
	 * 
	 * @return A String[] of default task data from the text file
	 */
	public String[] getDefTasks() {
		// Get the string from logic
		String defTasks = logic.taskDefaultView();
		// Split the string by newline
		String[] defTasksSplit = defTasks.split("\n");
		
		return defTasksSplit;
	}
	
	/**
	 * This method returns default events filtered by Logic in a String[]
	 * for easier formatting by DefaultViewController
	 * 
	 * @return A String[] of default event data from the text file
	 */
	public String[] getDefEvents() {
		// Get the string from logic
		String defEvents = logic.eventDefaultView();
		// Split the string by newline
		String[] defEventsSplit = defEvents.split("\n");
		
		// Filter through the array and reformat the data
		for (int i = 0; i < defEventsSplit.length; i++) {
			defEventsSplit[i] = defEventsSplit[i].replace(';', '\n');
		}
		
		return defEventsSplit;
	}
	
	/**
	 * This method returns all uncompleted tasks filtered by Logic in a 
	 * String[] for easier formatting by AllViewController
	 * 
	 * @return A String[] of all uncompleted task data from the text file
	 */
	public String[] getAllTasks() {
		// Get the String from logic
		String allTasks = logic.taskAllView(false);
		// Split the string by newline
		String[] allTasksSplit = allTasks.split("\n");
		
		return allTasksSplit;
	}
	
	/**
	 * This method returns all uncompleted events filtered by Logic in a 
	 * String[] for easier formatting by AllViewController
	 * 
	 * @return A String[] of all uncompleted event data from the text file
	 */
	public String[] getAllEvents() {
		// Get the String from logic
		String allEvents = logic.eventAllView(false);
		// Split the string by newline
		String[] allEventsSplit = allEvents.split("\n");
		
		// Filter through the array and reformat the data
		for (int i = 0; i < allEventsSplit.length; i++) {
			allEventsSplit[i] = allEventsSplit[i].replace(';', '\n');
		}
		
		return allEventsSplit;
	}
	
	/**
	 * This method returns all unresolved tasks filtered by Logic in a 
	 * String[] for easier formatting by UnresolvedViewController
	 * 
	 * @return A String[] of all unresolved task data from the text file
	 */
	public String[] getUnresTasks() {
		// Get the String from logic
		String unresTasks = logic.taskPastUncompletedView();
		// Split the string by newline
		String[] unresTasksSplit = unresTasks.split("\n");
		
		return unresTasksSplit;
	}
	
	/**
	 * This method returns all unresolved events filtered by Logic in a 
	 * String[] for easier formatting by UnresolvedViewController
	 * 
	 * @return A String[] of all unresolved event data from the text file
	 */
	public String[] getUnresEvents() {
		// Get the String from logic
		String unresEvents = logic.eventPastUncompletedView();
		// Split the string by newline
		String[] unresEventsSplit = unresEvents.split("\n");
		
		// Filter through the array and reformat the data
		for (int i = 0; i < unresEventsSplit.length; i++) {
			unresEventsSplit[i] = unresEventsSplit[i].replace(';', '\n');
		}
		
		return unresEventsSplit;
	}
	
	/**
	 * This method returns all completed tasks filtered by Logic in a 
	 * String[] for easier formatting by DoneViewController
	 * 
	 * @return A String[] of all completed task data from the text file
	 */
	public String[] getDoneTasks() {
		// Get the String from logic
		String doneTasks = logic.taskAllView(true);
		// Split the string by newline
		String[] doneTasksSplit = doneTasks.split("\n");
		
		// Prefix a "done" to the returned data
		for (int i = 0; i < doneTasksSplit.length; i++) {
			if (isNonEmptyElement(doneTasksSplit[i])) {
				doneTasksSplit[i] = InterfaceController.STATUS_DONE + 
						" " + doneTasksSplit[i];
			}
		}
		
		return doneTasksSplit;
	}
	
	/**
	 * This method returns all completed events filtered by Logic in a 
	 * String[] for easier formatting by DoneViewController
	 * 
	 * @return A String[] of all completed event data from the text file
	 */
	public String[] getDoneEvents() {
		// Get the String from logic
		String doneEvents = logic.eventAllView(true);
		// Split the string by newline
		String[] doneEventsSplit = doneEvents.split("\n");
		
		// Filter through the array and reformat the data
		for (int i = 0; i < doneEventsSplit.length; i++) {
			if (isNonEmptyElement(doneEventsSplit[i])) {
				doneEventsSplit[i] = InterfaceController.STATUS_DONE + 
						" " + doneEventsSplit[i];
			}
			doneEventsSplit[i] = doneEventsSplit[i].replace(';', '\n');
		}
		
		return doneEventsSplit;
	}
    
	/**
	 * This method calculates all values required for the summary view and
	 * stores them in a size 5 array in order of display in summary view
	 * 
	 * @return An int[] of size 5, each index storing the number required for
	 * 		   each element of the summary view
	 */
	public int[] getSummaryCount() {
		
		String[] defTasks = InterfaceController.getLogic().getDefTasks();
		String[] defEvents = InterfaceController.getLogic().getDefEvents();
		int[] summary = {0, 0, 0, 0, 0};
		int currentIndex = 0;
		
		currentIndex = getTaskSummaryCount(defTasks, summary, currentIndex);
		currentIndex = getEventSummaryCount(defEvents, summary, currentIndex);
		// Count the unresolved tasks and events as well
		summary[4] = getUnresElementsCount();
		
		return summary;
	}

	/**
	 * This method calculates
	 * 1. The number of events due within the next two days
	 * 2. The number of events that are currently ongoing
	 * and then updates the array summary with the data
	 * 
	 * @param defEvents
	 * 		      A String[] of all the event data from the default view
	 * @param summary
	 * 		      The int[] that stores all the counts for the summary view
	 * @param currentIndex
	 * 		 	  The index of the summary array that is to be modified
	 * @return The index of in the summary array where the method ended 
	 * 		   operation at
	 */
	private int getEventSummaryCount(String[] defEvents, int[] summary, int currentIndex) {
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
		return currentIndex;
	}

	/**
	 * This method calculates
	 * 1. The number of tasks due within the next two days
	 * 2. The number of tasks that are without a deadline (floating)
	 * and then updates the array summary with the data
	 * 
	 * @param defEvents
	 * 		      A String[] of all the task data from the default view
	 * @param summary
	 * 		      The int[] that stores all the counts for the summary view
	 * @param currentIndex
	 * 		 	  The index of the summary array that is to be modified
	 * @return The index of in the summary array where the method ended 
	 * 		   operation at
	 */
	private int getTaskSummaryCount(String[] defTasks, int[] summary, int currentIndex) {
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
		return currentIndex;
	}
	
	/**
	 * This method counts the number of elements there are in the default view
	 * (exclusive of title/date/empty elements)
	 * 
	 * @return The number of elements in the default view
	 */
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
	
	/**
	 * This method counts the number of elements there are in the all view
	 * (exclusive of title/date/empty elements)
	 * 
	 * @return The number of elements in the all view
	 */
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
	
	/**
	 * This method counts the number of elements there are in the search view
	 * (exclusive of title/date/empty elements)
	 * 
	 * @return The number of elements in the search view
	 */
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
	
	/**
	 * This method counts the number of elements there are in the unresolved view
	 * (exclusive of title/date/empty elements)
	 * 
	 * @return The number of elements in the unresolved view
	 */
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
	
	/**
	 * This method counts the number of elements there are in the done view
	 * (exclusive of title/date/empty elements)
	 * 
	 * @return The number of elements in the done view
	 */
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
	
	public ArrayList<Alias> getAliases() {
		ArrayList<Alias> newAliases = new ArrayList<Alias>();
		
		try {
			String aliases = logic.getAliasFileContents();
			// Check if the there are any aliases set in the file yet
			if (!aliases.equals(NULL_STRING)) {
				String[] aliasesSplit = aliases.split("\n");
				for (int i = 0; i < aliasesSplit.length; i++) {
					String[] aliasSplit = aliasesSplit[i].split(";");
					newAliases.add(new Alias(aliasSplit[0], aliasSplit[1]));
				}
			}
		} catch (FileSystemException e) {
			// Do nothing, return an empty alias list to AutoComplete
		}
		
		return newAliases;
	}
	
    public boolean isTitle(String displayData) {
    	
    	String firstWord = displayData.split(" ")[0];
    	return firstWord.equals("FLOAT") || firstWord.equals("TODAY") || 
    			firstWord.equals("TOMORROW") || firstWord.equals("ONGOING");
    }
    
	public boolean isEmpty(String displayData) {
		return displayData.equals(InterfaceController.MESSAGE_EMPTY);
	}
    
    public boolean isTitleOrDate(String displayData) {
    	// Use the definition that a date or title does not have a period in it
    	// whereas an element will definitely have a period after its index
    	return displayData.split(Pattern.quote(".")).length == 1 && 
    			!displayData.equals(InterfaceController.MESSAGE_EMPTY);
    }
    
    public boolean isNonEmptyElement(String displayData) {
    	return !isTitleOrDate(displayData) && 
				!displayData.equals(InterfaceController.MESSAGE_EMPTY);
    }
    
	public boolean isCompleted(String displayData) {
		return displayData.split(" ")[0].equals("done");
	}
    
    private static String mapToFileIndex(String textFieldInput) {
    	
    	String[] textFieldInputSplit = textFieldInput.split("[\\s;]+");
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
        	modifiedString = NULL_STRING;
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
    
    public void toggleAutoComplete() {
    	if (!AutoComplete.isActivated()) {
    		InterfaceController.getTextField().textProperty().addListener(autocompleter);
    		AutoComplete.setActivation(true);
    		
    	} else {
    		InterfaceController.getTextField().textProperty().removeListener(autocompleter);
    		AutoComplete.closePopup();
    		AutoComplete.setActivation(false);
    	}
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
				String searchTerm = returnMessage.split("\n")[0];
				// Add the search terms to the feedback bar and history view
				InterfaceController.getFeedbackLabel().setText(searchTerm);
				HistoryViewController.updateHistView(searchTerm);
			}
			
			SearchViewController.updateSearchView(returnMessage);
		} else {
			// Modify the return message first if it is incorrect and is an operation
			// that uses indices (delete, done, edit)
			if (mapIndexOutOfBounds && 
					(operationType == Command.CommandType.DELETE || 
					operationType == Command.CommandType.DONE || 
					operationType == Command.CommandType.EDIT)) {
				returnMessage = InterfaceController.MESSAGE_INVALID_INDEX;
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
		SummaryViewController.updateSummaryView();
	}
	
    /**
     * This method opens the text file currently set by the application for writing.
     * This text file is opened in the user's default associated application
     */
    protected static void openFileLocation() {
		try {
			HistoryViewController.updateHistView("Opening file...");
			InterfaceController.getFeedbackLabel().setText("Opening file...");
			Desktop.getDesktop().open(
					new File(InterfaceController.getLogic().getFilePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * This method opens the configuration file currently used by the application
     * for storing settings and aliases.
     * This text file is opened in the user's default associated application
     */
    protected static void openConfigLocation() {
    	try {
    		HistoryViewController.updateHistView("Opening config...");
    		InterfaceController.getFeedbackLabel().setText("Opening config...");
    		Desktop.getDesktop().open(new File(PATH_CONFIG_FILE));
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
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
		
		private String lastSearchCommand = NULL_STRING;
		
        @Override
        public void handle(ActionEvent event) {

        	// Get the text field from InterfaceController
        	TextField textField = InterfaceController.getTextField();
        	
            String textFieldInput = textField.getText();
            
            // Add the input into command history
            commandHistory.add(textFieldInput);
            commandHistory.resetIndex();

            // Clear the textField
            textField.setText(NULL_STRING);

            // Do a preliminary parse to determine the type of operation
            Command.CommandType operationType = logic.getCommandType(textFieldInput);
            
            // Perform branching based on the operation type
            switch (operationType) {
            case VIEW:
            	// Run another parse of the command to get the destination view
            	switch(logic.getViewType(textFieldInput)) {
                case DEF:
                	changeView(View.DEFAULT);
                	break;
                case ALL:
                	changeView(View.ALL);
                	break;
                case HIST:
                	changeView(View.HISTORY);
                	break;
                case UNRES:
                	changeView(View.UNRESOLVED);
                	break;
                case SEARCH:
                	changeView(View.SEARCH);
                	break;
                case DONE:
                	changeView(View.DONE);
                	break;
                case HELP:
                	changeView(View.HELP);
                	break;
                case OPENFILE:
                	openFileLocation();
                	break;
                case CONFIG:
                	openConfigLocation();
                	break;
                default:
                	break;
            	}
            	break;
            case EXIT:
            	changeView(View.EXIT);
            	break;
            case SEARCH:
            	// Store the last search command to run the search again dynamically
            	// upon the user's next operation
            	lastSearchCommand = textFieldInput;
            	// Run the command
            	runCommand(operationType, textFieldInput, SEARCH_USER);
            	changeView(View.SEARCH);
            	break;
            // Only modify the user command for these operations by editing the 
            // index from ViewIndexMap
            case DELETE:
            	// Run the command
            	runCommand(operationType, mapToFileIndex(textFieldInput), SEARCH_USER);

            	// Run the last search and update the search view only if the user is in search
            	if (!lastSearchCommand.equals(NULL_STRING) && InterfaceController.getCurrentView() == View.SEARCH) {
            		runCommand(Command.CommandType.SEARCH, lastSearchCommand, SEARCH_BACKGROUND);
            		InterfaceController.updateMainInterface(View.SEARCH);
            	}
            	// If the user is not in search view, do not switch to search view
            	if (!lastSearchCommand.equals(NULL_STRING) && InterfaceController.getCurrentView() != View.SEARCH) {
            		runCommand(Command.CommandType.SEARCH, lastSearchCommand, SEARCH_BACKGROUND);
            	}
            	break;
            case EDIT:
            	// Run the command
            	runCommand(operationType, mapToFileIndex(textFieldInput), SEARCH_USER);

            	// Run the last search and update the search view only if the user is in search
            	if (!lastSearchCommand.equals(NULL_STRING) && InterfaceController.getCurrentView() == View.SEARCH) {
            		runCommand(Command.CommandType.SEARCH, lastSearchCommand, SEARCH_BACKGROUND);
            		InterfaceController.updateMainInterface(View.SEARCH);
            	}
            	// If the user is not in search view, do not switch to search view
            	if (!lastSearchCommand.equals(NULL_STRING) && InterfaceController.getCurrentView() != View.SEARCH) {
            		runCommand(Command.CommandType.SEARCH, lastSearchCommand, SEARCH_BACKGROUND);
            	}
            	break;
            case DONE:
            	// Run the command
            	runCommand(operationType, mapToFileIndex(textFieldInput), SEARCH_USER);

            	// Run the last search and update the search view only if the user is in search
            	if (!lastSearchCommand.equals(NULL_STRING) && InterfaceController.getCurrentView() == View.SEARCH) {
            		runCommand(Command.CommandType.SEARCH, lastSearchCommand, SEARCH_BACKGROUND);
            		InterfaceController.updateMainInterface(View.SEARCH);
            	}
            	// If the user is not in search view, do not switch to search view
            	if (!lastSearchCommand.equals(NULL_STRING) && InterfaceController.getCurrentView() != View.SEARCH) {
            		runCommand(Command.CommandType.SEARCH, lastSearchCommand, SEARCH_BACKGROUND);
            	}
            	break;
            default:
            	// Run the command
            	runCommand(operationType, textFieldInput, SEARCH_USER);

            	// Run the last search and update the search view only if the user is in search
            	if (!lastSearchCommand.equals(NULL_STRING) && InterfaceController.getCurrentView() == View.SEARCH) {
            		runCommand(Command.CommandType.SEARCH, lastSearchCommand, SEARCH_BACKGROUND);
            		InterfaceController.updateMainInterface(View.SEARCH);
            	}
            	// If the user is not in search view, do not switch to search view
            	if (!lastSearchCommand.equals(NULL_STRING) && InterfaceController.getCurrentView() != View.SEARCH) {
            		runCommand(Command.CommandType.SEARCH, lastSearchCommand, SEARCH_BACKGROUND);
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
					changeView(View.UNRESOLVED);
					break;
				case DIGIT4:
					changeView(View.DONE);
					break;
				case DIGIT5:
					changeView(View.SEARCH);
					break;
				case DIGIT6:
					changeView(View.HISTORY);
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
				default:
					// Do nothing
					break;
				}
			} else {
				switch (event.getCode()) {
				case F1:
					changeView(View.HELP);
					break;
				// For opening text and config files
				case F2:
					openFileLocation();
					break;
				case F3:
					openConfigLocation();
					break;
				case F4:
					InterfaceController.toggleAutoCompleteIndicator();
					InterfaceController.getLogic().toggleAutoComplete();
					break;
				default:
					break;
				}
			}
		}
	}
	
	private class HelpHotKeyHandler implements EventHandler<KeyEvent> {
		
		@Override
		public void handle(KeyEvent event) {
			event.consume();
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
				if (event.getCode() == KeyCode.F1) {
					changeView(View.HELP);
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
    			filepathLabel.setStyle(InterfaceController.CSS_UNDERLINE);
    		}
    		if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
    			filepathLabel.setStyle(InterfaceController.CSS_NO_UNDERLINE);
    		}
    	}
    }
    
    private static class PathClickHandler implements EventHandler<MouseEvent> {
    	
    	@Override
    	public void handle(MouseEvent event) {
    		openFileLocation();
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
    			allUnresAttention.setStyle(InterfaceController.CSS_UNDERLINE);
    			allUnresAttention.setStyle(InterfaceController.CSS_UNDERLINE_ITALIC);
    		}
    		if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
    			allUnresAttention.setStyle(InterfaceController.CSS_NO_UNDERLINE);
    			allUnresAttention.setStyle(InterfaceController.CSS_NO_UNDERLINE_ITALIC);
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
    		openConfigLocation();
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
    		if (newValue.split(" ").length == 1 && !newValue.equals(NULL_STRING)) {
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
