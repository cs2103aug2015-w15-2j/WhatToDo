package gui;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.PopupControl;
import javafx.scene.input.KeyCode;

public class AutoComplete {

	// JavaFX components used for the popup
	
	// Used for initPopupList
	private static ListView<Alias> popupList = new ListView<Alias>();
	
	// Used for initPopup
	private static PopupControl popup = new PopupControl();
	
	// Variables to store the command strings
	private static ArrayList<Alias> shortcutCommands;
	private static ArrayList<Alias> operationCommands;
	private static ArrayList<Alias> aliasCommands;
	
	private static boolean isShowing = false;
	private static int numOfResults = -1;
	
	private static final double WIDTH_POPUP = 150;
	private static final double HEIGHT_POPUP = 150;
	
	private static void initAllCommands() {
		initShortcutCommands();
		initOperationCommands();
		initAliasCommands();
	}
	
	private static void initOperationCommands() {
		operationCommands = new ArrayList<Alias>();
		operationCommands.add(new Alias("def", "def"));
		operationCommands.add(new Alias("all", "all"));
		operationCommands.add(new Alias("hist", "hist"));
		operationCommands.add(new Alias("unres", "unres"));
		operationCommands.add(new Alias("done", "done"));
		operationCommands.add(new Alias("search", "search"));
		operationCommands.add(new Alias("help", "help"));
		operationCommands.add(new Alias("openfile", "openfile"));
		operationCommands.add(new Alias("config", "config"));
	}
	
	private static void initShortcutCommands() {
		shortcutCommands = new ArrayList<Alias>();
		shortcutCommands.add(new Alias("add", "add"));
		shortcutCommands.add(new Alias("delete", "delete"));
		shortcutCommands.add(new Alias("edit", "edit"));
		shortcutCommands.add(new Alias("redo", "redo"));
		shortcutCommands.add(new Alias("undo", "undo"));
		shortcutCommands.add(new Alias("set", "set"));
		shortcutCommands.add(new Alias("save", "save"));
		shortcutCommands.add(new Alias("exit", "exit"));
	}
	
	private static void initAliasCommands() {
		aliasCommands = new ArrayList<Alias>();
	}
	
	private static boolean isSubstring(String input, String target) {
		
		if (input.length() > target.length()) {
			return false;
		} else if (input.length() == target.length()) {
			return input.equals(target);
		} else if (target.substring(0, input.length()).equals(input)) {
			return true;
		} else {
			return isSubstring(input, target.substring(1));
		}
	}
	
	private static ArrayList<Alias> getMatchingCommands(String searchTerm) {
		
		ArrayList<Alias> matchedCommands = new ArrayList<Alias>();
		
		// Check for matches in shortcutCommands
		for (int i = 0; i < shortcutCommands.size(); i++) {
			// Check 1: length of command in array not shorter than searchTerm
			if (shortcutCommands.get(i).getAlias().length() >= searchTerm.length()) {
				// Check 2: if lengths are the same
				if (shortcutCommands.get(i).getAlias().length() == searchTerm.length()) {
					// Check 3: if both are the same string
					if (shortcutCommands.get(i).getAlias().equals(searchTerm)) {
						matchedCommands.add(shortcutCommands.get(i));
					}
				} else {
					// If shortcutCommand.length > searchTerm.length
					// Check 4: if searchTerm is a substring of shortcutCommand
					if (isSubstring(searchTerm, shortcutCommands.get(i).getAlias())) {
						matchedCommands.add(shortcutCommands.get(i));
					}
				}
			}
		}
		// Check for matches in operationCommands
		for (int i = 0; i < operationCommands.size(); i++) {
			// Check 1: length of command in array not shorter than searchTerm
			if (operationCommands.get(i).getAlias().length() >= searchTerm.length()) {
				// Check 2: if lengths are the same
				if (operationCommands.get(i).getAlias().length() == searchTerm.length()) {
					// Check 3: if both are the same string
					if (operationCommands.get(i).getAlias().equals(searchTerm)) {
						matchedCommands.add(operationCommands.get(i));
					}
				} else {
					// If shortcutCommand.length > searchTerm.length
					// Check 4: if searchTerm is a substring of shortcutCommand
					if (isSubstring(searchTerm, operationCommands.get(i).getAlias())) {
						matchedCommands.add(operationCommands.get(i));
					}
				}
			}
		}
		
		// TODO: check for matches in aliasCommands
		// Omitted until there is a method to get alias from config
		
		return matchedCommands;
	}
	
	public static boolean isShowing() {
		return isShowing;
	}
	
	private static void initPopupList(String searchTerm) {
		
		// Perform a matching search and get the results
		ArrayList<Alias> matchedCommands = getMatchingCommands(searchTerm);
		numOfResults = matchedCommands.size();
		
		// Convert from ArrayList to ObservableList
		ObservableList<Alias> matchedList = FXCollections.observableArrayList(matchedCommands);
		popupList.getItems().clear();
		popupList.setItems(matchedList);
		
		popupList.setMinSize(WIDTH_POPUP, HEIGHT_POPUP);
		popupList.setMaxSize(WIDTH_POPUP, HEIGHT_POPUP);
	}
	
	public static void initPopup(String searchTerm) {
		
		initAllCommands();
		initPopupList(searchTerm);

		// Only show the popup list if there are results
		if (numOfResults > 0) {
			popup.getScene().setRoot(popupList);
			isShowing = true;
			popup.show(MainApp.stage);
			switchFocus();
		} else {
			isShowing = false;
			popup.hide();
		}
	}
	
	public static void closePopup() {
		popup.hide();
	}
	
	public static void switchFocus() {
		popupList.requestFocus();
		popupList.getSelectionModel().select(0);
	}
}

class Alias {
	 
	private String alias;
	private String original;
	
	Alias(String alias, String original) {
		this.alias = alias;
		this.original = original;
	}
	
	public String getAlias() {
		return alias;
	}
	
	public String getOriginal() {
		return original;
	}
	
	public boolean isUserDefined() {
		return !alias.equals(original);
	}
	
	@Override
	public String toString() {
		// Display both the alias and original if it is user defined
		if (isUserDefined()) {
			return alias + " : " + original;
		} else {
			return alias;
		}
	}
}
