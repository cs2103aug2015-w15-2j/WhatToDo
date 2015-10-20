package gui;

import java.util.ArrayList;

public class CommandHistory {

    private ArrayList<String> history;
    private int currentIndex;

    public CommandHistory() {
        history = new ArrayList<String>();
        currentIndex = history.size() - 1;
    }

    public void add(String newCommand) {
        history.add(newCommand);
        currentIndex++;
    }

    public void resetIndex() {
        currentIndex = history.size() - 1;
    }
    
    private boolean isSameAsDisplayed(String returnCommand) {
    	String displayed = InterfaceController.getTextField().getText();
    	return returnCommand.equals(displayed);
    }

    public String getPrevious() {

    	// If command history is empty
    	if (history.isEmpty()) {
    		return "";
    	} else if (currentIndex == 0) {
    		// currentIndex = 0, meaning the user has viewed till the very first command
    		return history.get(currentIndex);
    	} else if (currentIndex == history.size() - 1) {
    		// currentIndex = history.size() - 1, meaning the user has viewed till the 
    		// very last command
    		String returnCommand = history.get(currentIndex);
    		// If the returned command is the same as the one already displayed, display the
    		// previous command instead. Only works for history.size() > 1
    		if (isSameAsDisplayed(returnCommand) && history.size() > 1) {
    			currentIndex--;
    			returnCommand = history.get(currentIndex);
    		}
    		return returnCommand;
    	} else if (currentIndex == -1) {
    		currentIndex++;
    		return history.get(currentIndex);
    	} else {
    		currentIndex--;
    		String returnCommand = history.get(currentIndex);
    		return returnCommand;
    	}
    }

    public String getNext() {

    	// If command history is empty
    	if (history.isEmpty()) {
    		return "";
    	} else if (currentIndex == history.size() - 1) {
    		// currentIndex = history.size() - 1, meaning the user has viewed 
    		// till the very last command
    		return history.get(currentIndex);
    	} else if (currentIndex == 0) {
    		// currentIndex = 0, meaning the user has viewed till the very first command
    		String returnCommand = history.get(currentIndex);
    		// If the returned command is the same as the one already displayed, display the
    		// next command instead. Only works for history.size() > 1
    		if (isSameAsDisplayed(returnCommand) && history.size() > 1) {
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
    
    /* ================================================================================
     * Rewrites for JUnit testing, same functionality but removed dependency on GUI
     * Uses a String to simulate the text field output that CommandHistory modifies
     * ================================================================================
     */
    
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
    		return "";
    	} else if (currentIndex == 0) {
    		// currentIndex = 0, meaning the user has viewed till the very first command
    		return history.get(currentIndex);
    	} else if (currentIndex == history.size() - 1) {
    		// currentIndex = history.size() - 1, meaning the user has viewed till the 
    		// very last command
    		String returnCommand = history.get(currentIndex);
    		// If the returned command is the same as the one already displayed, display the
    		// previous command instead. Only works for history.size() > 1
    		if (returnCommand.equals(simulatedTextField) && history.size() > 1) {
    			currentIndex--;
    			returnCommand = history.get(currentIndex);
    		}
    		return returnCommand;
    	} else if (currentIndex == -1) {
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
    		return "";
    	} else if (currentIndex == history.size() - 1) {
    		// currentIndex = history.size() - 1, meaning the user has viewed 
    		// till the very last command
    		return history.get(currentIndex);
    	} else if (currentIndex == 0) {
    		// currentIndex = 0, meaning the user has viewed till the very first command
    		String returnCommand = history.get(currentIndex);
    		// If the returned command is the same as the one already displayed, display the
    		// next command instead. Only works for history.size() > 1
    		if (returnCommand.equals(simulatedTextField) && history.size() > 1) {
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
