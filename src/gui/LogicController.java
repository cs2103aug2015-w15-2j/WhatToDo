/**
 * This class acts as a intermediary between InterfaceController and Logic, contains 
 * all class instances required for logical operations by InterfaceController, and
 * provides methods for InterfaceController to access those operations.
 * 
 * @@author A0124123Y
 */

package gui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.regex.Pattern;

import backend.Logic;
import javafx.application.Platform;
import struct.Alias;
import struct.Command;
import struct.View;

public class LogicController {
	

	protected static final int INDEX_UNRES = 4;
	
	protected static final char NEWLINE_CHAR = '\n';
	protected static final char SEMICOLON = ';';
	protected static final String NEWLINE = "\n";
	protected static final String NULL_STRING = "";
	
	protected static final String PATH_CONFIG_FILE = "config" + File.separator + "config.txt";
	private static final String MESSAGE_OPEN_CONFIG = "Opening config...";
	private static final String MESSAGE_OPEN_FILE = "Opening file...";
	
	// Class instances within one LogicController instance
	private static Listeners.AutoCompleteListener autocompleter;
	private static Logic logic;
	private static CommandHistory commandHistory;
	
	private static boolean mapIndexOutOfBounds = false;
	
    // ======================================================================
    // LogicController constructor
    // ======================================================================
	
	/**
	 * This method is the constructor for a LogicController object.
	 * Possibly catches a FileSystemException from Storage caught by Logic and
	 * passed on to UI.
	 */
	public LogicController() {
		try {
			logic = new Logic();
		} catch (FileSystemException e) {
			logic = null;
			MainApp.logger.log(Level.SEVERE, MainApp.LOG_FILE_NOT_CREATED);
		}
		
		// Initialize the command history object
		commandHistory = new CommandHistory();
		// Initialize the listener for autocomplete
		autocompleter = Listeners.getAutoCompleteListener();
	}
	
	/**
	 * Getter for the CommandHistory object to work with Handlers class
	 * @return the CommandHistory object
	 */
	public static CommandHistory getHistory() {
		return commandHistory;
	}
	
	/**
	 * Getter for the Logic object to work with the Handlers class
	 * @return the Logic object
	 */
	public static Logic getLogic() {
		return logic;
	}
	
    // ======================================================================
    // Public methods called by other GUI classes that require some
	// processing of data or Logic methods
    // ======================================================================
	
	/**
	 * This method returns the current filepath of the text file being written
	 * to by the application
	 * 
	 * @return A String containing the filepath of whattodo.txt, or an error
	 * 		   message if the file cannot be found/written
	 */
	public String getFilePath() {
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
		String[] defTasksSplit = defTasks.split(NEWLINE);
		
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
		String[] defEventsSplit = defEvents.split(NEWLINE);
		
		// Filter through the array and reformat the data
		for (int i = 0; i < defEventsSplit.length; i++) {
			defEventsSplit[i] = defEventsSplit[i].replace(SEMICOLON, NEWLINE_CHAR);
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
		String[] allTasksSplit = allTasks.split(NEWLINE);
		
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
		String[] allEventsSplit = allEvents.split(NEWLINE);
		
		// Filter through the array and reformat the data
		for (int i = 0; i < allEventsSplit.length; i++) {
			allEventsSplit[i] = allEventsSplit[i].replace(SEMICOLON, NEWLINE_CHAR);
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
		String[] unresTasksSplit = unresTasks.split(NEWLINE);
		
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
		String[] unresEventsSplit = unresEvents.split(NEWLINE);
		
		// Filter through the array and reformat the data
		for (int i = 0; i < unresEventsSplit.length; i++) {
			unresEventsSplit[i] = unresEventsSplit[i].replace(SEMICOLON, NEWLINE_CHAR);
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
		String[] doneTasksSplit = doneTasks.split(NEWLINE);
		
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
		String[] doneEventsSplit = doneEvents.split(NEWLINE);
		
		// Filter through the array and reformat the data
		for (int i = 0; i < doneEventsSplit.length; i++) {
			if (isNonEmptyElement(doneEventsSplit[i])) {
				doneEventsSplit[i] = InterfaceController.STATUS_DONE + 
						" " + doneEventsSplit[i];
			}
			doneEventsSplit[i] = doneEventsSplit[i].replace(SEMICOLON, NEWLINE_CHAR);
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
		summary[INDEX_UNRES] = getUnresElementsCount();
		
		return summary;
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
	
	/**
	 * This method gets the list of added Aliases by calling Logic's getAliasFileContents()
	 * method, and then converting the returned Strings into the Alias type and storing them
	 * in an array
	 * 
	 * @return An Alias[] of aliases read from alias.txt
	 */
	protected ArrayList<Alias> getAliases() {
		ArrayList<Alias> newAliases = new ArrayList<Alias>();
		
		try {
			String aliases = logic.getAliasFileContents();
			// Check if the there are any aliases set in the file yet
			if (!aliases.equals(NULL_STRING)) {
				String[] aliasesSplit = aliases.split(NEWLINE);
				for (int i = 0; i < aliasesSplit.length; i++) {
					String[] aliasSplit = aliasesSplit[i].split(";");
					newAliases.add(new Alias(aliasSplit[0], aliasSplit[1]));
				}
			}
		} catch (FileSystemException e) {
			// Do nothing, return an empty alias list to AutoComplete
			MainApp.logger.log(Level.SEVERE, MainApp.LOG_ALIAS_NOT_FOUND);
		}
		
		return newAliases;
	}
	
    // ======================================================================
    // Boolean methods which are used in other GUI classes to check for 
	// certain conditions which the data must meet
    // ======================================================================
	
	/**
	 * This method checks if the data is a title, meaning it is a header for a
	 * section that is not a date ("FLOAT", "TODAY" etc)
	 * 
	 * @param displayData
	 * 		      The line of data read in as a String
	 * @return true if displayData matches either of the title keywords, false
	 * 		   otherwise
	 */
	protected boolean isTitle(String displayData) {
    	
    	String firstWord = displayData.split(" ")[0];
    	return firstWord.equals("FLOAT") || firstWord.equals("TODAY") || 
    			firstWord.equals("TOMORROW") || firstWord.equals("ONGOING");
    }
    
    /**
     * This method checks if the data is an empty message, output when there are
     * no results ("There are no results to display.")
     * 
     * @param displayData
     * 		      The line of data read in as a String
     * @return true if displayData matches the empty message response, false
     * 		   otherwise
     */
	protected boolean isEmpty(String displayData) {
		return displayData.equals(InterfaceController.MESSAGE_EMPTY);
	}
    
	/**
	 * This method checks if the data is either a title or a date, meaning it
	 * does not contain task/event data
	 * 
	 * @param displayData
	 *  		  The line of data read in as a String
	 * @return true if displayData matches either a title or a date, false
	 * 		   otherwise
	 */
	protected boolean isTitleOrDate(String displayData) {
    	// Use the definition that a date or title does not have a period in it
    	// whereas an element will definitely have a period after its index
    	return displayData.split(Pattern.quote(".")).length == 1 && 
    			!displayData.equals(InterfaceController.MESSAGE_EMPTY);
    }
    
    /**
     * This method checks if the data is a String which contains data, and
     * that the data is not an empty message
     * 
     * @param displayData
     * 		      The line of data read in as a String
     * @return true if displayData is not a title, date or empty element, false
     * 		   otherwise
     */
	protected boolean isNonEmptyElement(String displayData) {
    	return !isTitleOrDate(displayData) && 
				!displayData.equals(InterfaceController.MESSAGE_EMPTY);
    }
    
    /**
     * This method checks if a particular task/event has been marked as completed
     * 
     * @param displayData
     * 		      The line of data read in as a String
     * @return true if the data has been marked as done, false otherwise
     */
	protected boolean isCompleted(String displayData) {
		return displayData.split(" ")[0].equals("done");
	}
    
    // ======================================================================
    // Additional operations used to change certain program features
    // ======================================================================
    
    /**
     * This method toggles the activation status of the autocomplete feature by
     * adding/deleting the listener depending on the activation status
     */
    protected void toggleAutoComplete() {
    	if (!AutoComplete.isActivated()) {
    		InterfaceController.getTextField().textProperty().addListener(autocompleter);
    		AutoComplete.setActivation(true);
    	} else {
    		InterfaceController.getTextField().textProperty().removeListener(autocompleter);
    		AutoComplete.closePopup();
    		AutoComplete.setActivation(false);
    	}
    }
	
    /**
     * This method opens the text file currently set by the application for writing.
     * This text file is opened in the user's default associated application
     */
    protected void openFileLocation() {
		try {
			HistoryViewController.updateHistView(MESSAGE_OPEN_FILE);
			InterfaceController.getFeedbackLabel().setText(MESSAGE_OPEN_FILE);
			Desktop.getDesktop().open(
					new File(InterfaceController.getLogic().getFilePath()));
		} catch (IOException e) {
			MainApp.logger.log(Level.SEVERE, MainApp.LOG_FILE_NOT_FOUND);
		}
    }
    
    /**
     * This method opens the configuration file currently used by the application
     * for storing settings.
     * This text file is opened in the user's default associated application
     */
    protected void openConfigLocation() {
    	try {
    		HistoryViewController.updateHistView(MESSAGE_OPEN_CONFIG);
    		InterfaceController.getFeedbackLabel().setText(MESSAGE_OPEN_CONFIG);
    		Desktop.getDesktop().open(new File(PATH_CONFIG_FILE));
    	} catch (IOException e) {
    		MainApp.logger.log(Level.SEVERE, MainApp.LOG_CONFIG_NOT_FOUND);
    	}
    }
    
    // ======================================================================
    // Private methods for running commands and switching views accessible 
    // only within LogicController
    // ======================================================================
    
    /**
     * This method takes in an input target View to switch to, and then either
     * 1. Swaps to it if currentView != target view
     * 2. Do nothing if currentView == target view
     * 
     * @param view
     * 		      The target View to switch to
     */
	protected void changeView(View view) {
		
		switch(InterfaceController.getCurrentView()) {
		    // currentView == DEFAULT
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
	    	// currentView == ALL
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
	    	// currentView == HISTORY
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
	    	// currentView == UNRESOLVED
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
	    	// currentView == DONE
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
	    	// currentView == SEARCH
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
	
	/**
	 * This method runs the user entered command by calling Logic's executeCommand()
	 * method and getting the return message
	 * 
	 * @param operationType
	 * 		      The type of operation being entered by the user (ADD, EDIT, DELETE..)
	 * @param textFieldInput
	 * 		      The input String entered by the user into the text field
	 * @param isBackgroundUpdate
	 * 		      A boolean flag indicating whether the current operation is a background
	 * 			  update being run by a SEARCH command
	 */
	protected void runCommand(Command.CommandType operationType, 
			String textFieldInput, boolean isBackgroundUpdate) {
		// Execute the command
		String returnMessage = logic.executeCommand(textFieldInput);
		if (operationType == Command.CommandType.SEARCH) {
			// Do not update the feedback bar and history view if the search operation
			// is a background update of the last search term
			if (!isBackgroundUpdate) {
				String searchTerm = returnMessage.split(NEWLINE)[0];
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
	
	// runCommand(), edited for JUnit testing
	public void runCommandTest(String textFieldInput) {
		// Execute the command
		logic.executeCommand(textFieldInput);
	}
	
	/**
	 * This method modifies the String input by a user by extracting the view 
	 * index of delete, done and edit operations, mapping it to the file index, 
	 * and then replacing it before passing it to the Logic component
	 * 
	 * @param textFieldInput
	 * 		      The input String that was entered by the user into the text 
	 * 			  field
	 * @return A String with the view index entered by the user replaced by the
	 * 		   file index
	 */
	protected String mapToFileIndex(String textFieldInput) {
    	
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
    		MainApp.logger.log(Level.WARNING, MainApp.LOG_INVALID_INDEX);
    	}
    	
    	return modifiedString;
    }
    
	/**
	 * This method sets the caret position to the end of the line of text
	 * 
	 * @param text
	 * 		      The line of text to set the caret position of
	 */
	protected void setCaretToEnd(String text) {
		// Required for positionCaret to work correctly
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				InterfaceController.getTextField().positionCaret(text.length());
			}
		});
	}
	
    // ======================================================================
    // Misc private methods used for calculations and other methods
    // ======================================================================
    
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
}
