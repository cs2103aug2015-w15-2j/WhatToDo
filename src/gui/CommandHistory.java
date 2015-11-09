/**
 * This class creates a history object that contains all instances of commands
 * that has been entered by the user, and provides methods to access those
 * commands when called by a text handler
 * 
 * @@author A0124238L
 */

package gui;

import java.util.ArrayList;

public class CommandHistory {
	private static final int PARAM_FIRST_INDEX = 0;
	private static final int PARAM_SIZE_HIST = 1;
	private static final int PARAM_DEFAULT = -1;
	private static final int OFFSET = 1;
	
	private static final String EMPTY_STRING = "";

    private ArrayList<String> history;
    private int currentIndex;

    /**
     * CommandHistory constructor
     */
    public CommandHistory() {
        history = new ArrayList<String>();
        currentIndex = history.size() - OFFSET;
    }

    /**
     * This method adds a new command to the command history
     * 
     * @param newCommand
     * 		      The command to be added
     */
    public void add(String newCommand) {
        history.add(newCommand);
        currentIndex++;
    }
    
    /**
     * This method resets the index to the most recent command entered
     */
    public void resetIndex() {
        currentIndex = history.size() - OFFSET;
    }

    /**
     * This method returns the previous command entered by the user
     * 
     * @return The previous command relative to the current index of command history
     */
    protected String getPrevious() {

    	// If command history is empty
    	if (history.isEmpty()) {
    		return EMPTY_STRING;
    	} else if (currentIndex == PARAM_FIRST_INDEX) {
    		// currentIndex = 0, meaning the user has viewed till the very first command
    		return history.get(currentIndex);
    	} else if (currentIndex == history.size() - OFFSET) {
    		// currentIndex = history.size() - 1, meaning the user has viewed till the 
    		// very last command
    		String returnCommand = history.get(currentIndex);
    		// If the returned command is the same as the one already displayed, display the
    		// previous command instead. Only works for history.size() > 1
    		if (isSameAsDisplayed(returnCommand) && history.size() > PARAM_SIZE_HIST) {
    			currentIndex--;
    			returnCommand = history.get(currentIndex);
    		}
    		return returnCommand;
    	} else if (currentIndex == PARAM_DEFAULT) {
    		currentIndex++;
    		return history.get(currentIndex);
    	} else {
    		currentIndex--;
    		String returnCommand = history.get(currentIndex);
    		return returnCommand;
    	}
    }

    /**
     * This method returns the next command entered by the user
     * 
     * @return The next command relative to the current index of command history
     */
    protected String getNext() {

    	// If command history is empty
    	if (history.isEmpty()) {
    		return EMPTY_STRING;
    	} else if (currentIndex == history.size() - OFFSET) {
    		// currentIndex = history.size() - 1, meaning the user has viewed 
    		// till the very last command
    		return history.get(currentIndex);
    	} else if (currentIndex == PARAM_FIRST_INDEX) {
    		// currentIndex = 0, meaning the user has viewed till the very first command
    		String returnCommand = history.get(currentIndex);
    		// If the returned command is the same as the one already displayed, display the
    		// next command instead. Only works for history.size() > 1
    		if (isSameAsDisplayed(returnCommand) && history.size() > PARAM_SIZE_HIST) {
    			currentIndex++;
    			returnCommand = history.get(currentIndex);
    		}
    		return returnCommand;
    	} else if (currentIndex == history.size()) {
    		currentIndex--;
    		return history.get(currentIndex);
    	} else {
    		currentIndex++;
    		String returnCommand = history.get(currentIndex);
    		return returnCommand;
    	}
    }
    
	// ================================================================================
    // Private method used to perform misc operations required for command history
    // ================================================================================
    
    /**
     * This method checks if the command to be returned is the same as the
     * one currently being displayed in the text field
     * 
     * @param returnCommand
     * 			  The command that is returned by either getPrevious() or getNext()
     * @return true if returnCommand is the same as the command displayed in the 
     * 		   text field, false otherwise
     */
    private boolean isSameAsDisplayed(String returnCommand) {
    	String displayed = InterfaceController.getTextField().getText();
    	return returnCommand.equals(displayed);
    }
    
	// ================================================================================
    // Rewrites for JUnit testing, same functionality but removed dependency on GUI
    // Uses a String to simulate the text field output that CommandHistory modifies
    // ================================================================================
    
    private String simulatedTextField;
    
    public void updateField(String update) {
    	simulatedTextField = update;
    }
    
    public String getText() {
    	return simulatedTextField;
    }
    
    public String getPreviousTest() {
    	
    	// If command history is empty
    	if (history.isEmpty()) {
    		return EMPTY_STRING;
    	} else if (currentIndex == PARAM_FIRST_INDEX) {
    		// currentIndex = 0, meaning the user has viewed till the very first command
    		return history.get(currentIndex);
    	} else if (currentIndex == history.size() - OFFSET) {
    		// currentIndex = history.size() - 1, meaning the user has viewed till the 
    		// very last command
    		String returnCommand = history.get(currentIndex);
    		// If the returned command is the same as the one already displayed, display the
    		// previous command instead. Only works for history.size() > 1
    		if (returnCommand.equals(simulatedTextField) && history.size() > PARAM_SIZE_HIST) {
    			currentIndex--;
    			returnCommand = history.get(currentIndex);
    		}
    		return returnCommand;
    	} else if (currentIndex == PARAM_DEFAULT) {
    		currentIndex++;
    		return history.get(currentIndex);
    	} else {
    		currentIndex--;
    		String returnCommand = history.get(currentIndex);
    		return returnCommand;
    	}
    }

    public String getNextTest() {

    	// If command history is empty
    	if (history.isEmpty()) {
    		return EMPTY_STRING;
    	} else if (currentIndex == history.size() - OFFSET) {
    		// currentIndex = history.size() - 1, meaning the user has viewed 
    		// till the very last command
    		return history.get(currentIndex);
    	} else if (currentIndex == PARAM_FIRST_INDEX) {
    		// currentIndex = 0, meaning the user has viewed till the very first command
    		String returnCommand = history.get(currentIndex);
    		// If the returned command is the same as the one already displayed, display the
    		// next command instead. Only works for history.size() > 1
    		if (returnCommand.equals(simulatedTextField) && history.size() > PARAM_SIZE_HIST) {
    			currentIndex++;
    			returnCommand = history.get(currentIndex);
    		}
    		return returnCommand;
    	} else if (currentIndex == history.size()) {
    		currentIndex--;
    		return history.get(currentIndex);
    	} else {
    		currentIndex++;
    		String returnCommand = history.get(currentIndex);
    		return returnCommand;
    	}
    }
}
