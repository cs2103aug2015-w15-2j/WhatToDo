/**
 * This class contains all the EventHandler private classes used in the application
 * and provides getter methods for other GUI classes to access them
 * 
 * @@author A0124123Y
 */

package gui;

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

public class Handlers {

	private static final String COMMAND_SEARCH = "search";
	
	private static final boolean SEARCH_USER = false;
	private static final boolean SEARCH_BACKGROUND = true;
	
	private static LogicController handlerLogicControl = InterfaceController.getLogic();
	
    // ======================================================================
    // Getters to allow GUI components in InterfaceController to access the
    // private EventHandler classes
    // ======================================================================
	
    // EventHandlers
	protected static TextInputHandler getTextInputHandler() {
		return new TextInputHandler();
	}
	
	protected static KeyPressHandler getKeyPressHandler() {
		return new KeyPressHandler();
	}
	
	protected static TabPressHandler getTabPressHandler() {
		return new TabPressHandler();
	}
	
	protected static HotKeyHandler getHotKeyHandler() {
		return new HotKeyHandler();
	}
	
	protected static HelpHotKeyHandler getHelpHotKeyHandler() {
		return new HelpHotKeyHandler();
	}
	
	protected static ButtonHoverHandler getButtonHoverHandler(View buttonType) {
		return new ButtonHoverHandler(buttonType);
	}
	
	protected static ButtonClickHandler getButtonClickHandler(View buttonType) {
		return new ButtonClickHandler(buttonType);
	}
	
	protected static PathHoverHandler getPathHoverHandler(Label filepathLabel) {
		return new PathHoverHandler(filepathLabel);
	}
	
	protected static PathClickHandler getPathClickHandler() {
		return new PathClickHandler();
	}
	
	protected static UnresHoverHandler getUnresHoverHandler(Label allUnresAttention) {
		return new UnresHoverHandler(allUnresAttention);
	}
	
	protected static UnresClickHandler getUnresClickHandler() {
		return new UnresClickHandler();
	}
	
	protected static ConfigClickHandler getConfigClickHandler() {
		return new ConfigClickHandler();
	}
	
	protected static AutoCompleteSelectHandler getAutoCompleteSelectHandler() {
		return new AutoCompleteSelectHandler();
	}
	
    // ======================================================================
    // Private EventHandler class definitions
    // ======================================================================
	
	/**
	 * This class implements a handler for the text field to perform certain
	 * operations upon pressing the ENTER key
	 */
	private static class TextInputHandler implements EventHandler<ActionEvent> {
		private String lastSearchCommand = LogicController.NULL_STRING;
		
        @Override
        public void handle(ActionEvent event) {

        	// Get the text field from InterfaceController
        	TextField textField = InterfaceController.getTextField();
            String textFieldInput = textField.getText();
            
            // Add the input into command history
            LogicController.getHistory().add(textFieldInput);
            LogicController.getHistory().resetIndex();

            textField.setText(LogicController.NULL_STRING);

            // Do a preliminary parse to determine the type of operation
            Command.CommandType operationType = LogicController.getLogic().getCommandType(textFieldInput);
            
            // Perform branching based on the operation type
            switch (operationType) {
            case VIEW:
            	// Run another parse of the command to get the destination view
            	switch(LogicController.getLogic().getViewType(textFieldInput)) {
                case DEF:
                	handlerLogicControl.changeView(View.DEFAULT);
                	break;
                case ALL:
                	handlerLogicControl.changeView(View.ALL);
                	break;
                case HIST:
                	handlerLogicControl.changeView(View.HISTORY);
                	break;
                case UNRES:
                	handlerLogicControl.changeView(View.UNRESOLVED);
                	break;
                case SEARCH:
                	handlerLogicControl.changeView(View.SEARCH);
                	break;
                case DONE:
                	handlerLogicControl.changeView(View.DONE);
                	break;
                case HELP:
                	handlerLogicControl.changeView(View.HELP);
                	break;
                case OPENFILE:
                	handlerLogicControl.openFileLocation();
                	break;
                case CONFIG:
                	handlerLogicControl.openConfigLocation();
                	break;
                default:
                	break;
            	}
            	break;
            case EXIT:
            	handlerLogicControl.changeView(View.EXIT);
            	break;
            case SEARCH:
            	// Store the last search command to run the search again dynamically
            	// upon the user's next operation
            	// Modify the search query by replacing the first word with "search" to account
            	// for aliases since there is no parsing here
            	String[] textFieldInputSplit = textFieldInput.split(" ");
            	textFieldInputSplit[0] = COMMAND_SEARCH;
            	lastSearchCommand = "";
            	for (int i = 0; i < textFieldInputSplit.length; i++) {
            		lastSearchCommand += textFieldInputSplit[i] + " ";
            	}
            	lastSearchCommand = lastSearchCommand.substring(0, lastSearchCommand.length() - 1);
            	handlerLogicControl.runCommand(operationType, textFieldInput, SEARCH_USER);
            	handlerLogicControl.changeView(View.SEARCH);
            	break;
            // Only modify the user command for these operations by editing the 
            // index from ViewIndexMap
            case DELETE:
            	// Run the command
            	handlerLogicControl.runCommand(
            			operationType, 
            			handlerLogicControl.mapToFileIndex(textFieldInput), 
            			SEARCH_USER);
            	runBackgroundUpdate();
            	break;
            case EDIT:
            	// Run the command
            	handlerLogicControl.runCommand(
            			operationType, 
            			handlerLogicControl.mapToFileIndex(textFieldInput), 
            			SEARCH_USER);
            	runBackgroundUpdate();
            	break;
            case DONE:
            	// Run the command
            	handlerLogicControl.runCommand(
            			operationType, 
            			handlerLogicControl.mapToFileIndex(textFieldInput), 
            			SEARCH_USER);
            	runBackgroundUpdate();
            	break;
            default:
            	// Run the command
            	handlerLogicControl.runCommand(operationType, textFieldInput, SEARCH_USER);
            	runBackgroundUpdate();
            	break;
            }

        }

        /**
         * This method runs the background update to SEARCH view and automatically swaps
         * updates the view if the user is already in SEARCH view
         */
		private void runBackgroundUpdate() {
			// Run the last search and update the search view only if the user is in search
			if (!lastSearchCommand.equals(LogicController.NULL_STRING) && 
					InterfaceController.getCurrentView() == View.SEARCH) {
				handlerLogicControl.runCommand(
						Command.CommandType.SEARCH, lastSearchCommand, SEARCH_BACKGROUND);
				InterfaceController.updateMainInterface(View.SEARCH);
			}
			// If the user is not in search view, do not switch to search view
			if (!lastSearchCommand.equals(LogicController.NULL_STRING) && 
					InterfaceController.getCurrentView() != View.SEARCH) {
				handlerLogicControl.runCommand(
						Command.CommandType.SEARCH, lastSearchCommand, SEARCH_BACKGROUND);
			}
		}
    }
	
	/** 
	 * This class implements a handler for key presses in the text field to run
	 * the LogicController.getHistory() methods when the user presses a UP or DOWN key
	 */
	private static class KeyPressHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent event) {
        	// If up key pressed
        	if (event.getCode() == KeyCode.UP) {
        		// Only register keypress when autocomplete is now showing
        		if (!AutoComplete.isShowing()) {
        			String prevCommand = LogicController.getHistory().getPrevious();
        			InterfaceController.getTextField().setText(prevCommand);
        			handlerLogicControl.setCaretToEnd(prevCommand);
        		}
        	}
            // If down key pressed
        	if (event.getCode() == KeyCode.DOWN) {
        		// Only register keypress when autocomplete is not showing
        		if (!AutoComplete.isShowing()) {
        			String nextCommand = LogicController.getHistory().getNext();
        			InterfaceController.getTextField().setText(nextCommand);
        			handlerLogicControl.setCaretToEnd(nextCommand);
        		}
            }
        }
    }
	
	/**
	 * This class implements a handler for the TAB key in the main stage in MainApp
	 * to control the display of the summary view
	 */
	private static class TabPressHandler implements EventHandler<KeyEvent> {
		@Override
		public void handle(KeyEvent event) {
			// Display the summary view
			if (event.getCode() == KeyCode.TAB) {
				if (!SummaryViewController.isShowing()) {
					SummaryViewController.startShowing();
					handlerLogicControl.changeView(View.SUMMARY);
				} else {
					SummaryViewController.stopShowing();
					InterfaceController.updateMainInterface(InterfaceController.getCurrentView());
				}
			}
		}
	}

	/** 
	 * This class implements a handler for all instances of hotkey combinations
	 * in MainApp's main stage
	 */
	private static class HotKeyHandler implements EventHandler<KeyEvent> {
		@Override
		public void handle(KeyEvent event) {
			if (event.isControlDown()) {
				switch (event.getCode()) {
				// For regular number keys
				case DIGIT1:
					handlerLogicControl.changeView(View.DEFAULT);
					break;
				case DIGIT2:
					handlerLogicControl.changeView(View.ALL);
					break;
				case DIGIT3:
					handlerLogicControl.changeView(View.UNRESOLVED);
					break;
				case DIGIT4:
					handlerLogicControl.changeView(View.DONE);
					break;
				case DIGIT5:
					handlerLogicControl.changeView(View.SEARCH);
					break;
				case DIGIT6:
					handlerLogicControl.changeView(View.HISTORY);
					break;
				case DIGIT7:
					handlerLogicControl.changeView(View.HELP);
					break;
				// For users with a number pad
				case NUMPAD1:
					handlerLogicControl.changeView(View.DEFAULT);
					break;
				case NUMPAD2:
					handlerLogicControl.changeView(View.ALL);
					break;
				case NUMPAD3:
					handlerLogicControl.changeView(View.HISTORY);
					break;
				case NUMPAD4:
					handlerLogicControl.changeView(View.UNRESOLVED);
					break;
				case NUMPAD5:
					handlerLogicControl.changeView(View.DONE);
					break;
				case NUMPAD6:
					handlerLogicControl.changeView(View.SEARCH);
					break;
				case NUMPAD7:
					handlerLogicControl.changeView(View.HELP);
					break;
				default:
					// Do nothing
					break;
				}
			} else {
				switch (event.getCode()) {
				case F1:
					handlerLogicControl.changeView(View.HELP);
					break;
				// For opening text and config files
				case F2:
					handlerLogicControl.openFileLocation();
					break;
				case F3:
					handlerLogicControl.openConfigLocation();
					break;
				case F4:
					InterfaceController.toggleAutoCompleteIndicator();
					handlerLogicControl.toggleAutoComplete();
					break;
				default:
					break;
				}
			}
		}
	}
	
	/**
	 * This class implements a handler for hotkeys registered by the help
	 * stage in MainApp instead of the regular stage
	 */
	private static class HelpHotKeyHandler implements EventHandler<KeyEvent> {
		@Override
		public void handle(KeyEvent event) {
			event.consume();
			if (event.isControlDown()) {
				switch (event.getCode()) {
				// For regular number key
				case DIGIT7:
					handlerLogicControl.changeView(View.HELP);
					break;
				case NUMPAD7:
					handlerLogicControl.changeView(View.HELP);
					break;
				default:
					// Do nothing
					break;
				}
			} else {
				if (event.getCode() == KeyCode.F1) {
					handlerLogicControl.changeView(View.HELP);
				}
			}
		}
	}
    
	/**
	 * This class implements a handler that changes the button images when
	 * a mouse is hovered over the button
	 */
    private static class ButtonHoverHandler implements EventHandler<MouseEvent> {
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
    
    /**
     * This class implements a handler for the buttons when a mouse click is
     * registered by the mouse
     */
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
    		handlerLogicControl.changeView(buttonType);
    	}
    }
    
    /**
     * This class implements a handler which underlines the filepath when hovered over
     */
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
    
    /**
     * This class implements a handler for registering mouse clicks in the filepath bar
     */
    private static class PathClickHandler implements EventHandler<MouseEvent> {
    	@Override
    	public void handle(MouseEvent event) {
    		handlerLogicControl.openFileLocation();
    	}
    }
    
    /**
     * This class implements a handler that underlines the prompt text in the summary 
     * view regarding unresolved tasks
     */
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
    
    /**
     * This class implements a handler that registers mouse clicks on the prompt text
     * and performs a view switch when clicked
     */
    private static class UnresClickHandler implements EventHandler<MouseEvent> {
    	@Override
    	public void handle(MouseEvent event) {
    		SummaryViewController.stopShowing();
    		InterfaceController.updateMainInterface(View.UNRESOLVED);
    	}
    }
    
    /**
     * This class implements a handler that registers mouse clicks on the config
     * button in the filepath bar
     */
    private static class ConfigClickHandler implements EventHandler<MouseEvent> {
    	@Override
    	public void handle(MouseEvent event) {
    		handlerLogicControl.openConfigLocation();
    	}
    }
    
    /**
     * This class implements a handler in the autocomplete popup that registers
     * ENTER keypresses when the user wishes to select a particular keyword suggested
     * by autocomplete
     */
    private static class AutoCompleteSelectHandler implements EventHandler<KeyEvent> {
    	@Override
    	public void handle(KeyEvent event) {
    		if (event.getCode() == KeyCode.ENTER) {
    			InterfaceController.getTextField().setText(AutoComplete.getSelectedItem());
				String text = InterfaceController.getTextField().getText();
				handlerLogicControl.setCaretToEnd(text);
    			AutoComplete.closePopup();
    		}
    	}
    }
}
