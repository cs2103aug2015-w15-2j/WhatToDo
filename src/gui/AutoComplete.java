package gui;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.control.ListView;
import javafx.scene.control.PopupControl;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.PopupWindow.AnchorLocation;

public class AutoComplete {

	// JavaFX components used for the popup
	
	// Used for initPopupList
	private static ListView<Alias> popupList;
	
	// Used for initPopup
	private static Popup popup;
	
	// Variables to store the command strings
	private static ArrayList<Alias> shortcutCommands;
	private static ArrayList<Alias> operationCommands;
	private static ArrayList<Alias> aliasCommands;
	
	private static boolean isShowing = false;
	private static boolean showingBeforeLoseFocus = false;
	private static int numOfResults = -1;
	
	private static final double WIDTH_POPUP = 200;
	private static final double HEIGHT_POPUP = 150;
	
	private static void initAllCommands() {
		initShortcutCommands();
		initOperationCommands();
		initAliasCommands();
	}
	
	private static void initShortcutCommands() {
		shortcutCommands = new ArrayList<Alias>();
		shortcutCommands.add(new Alias("def", "def"));
		shortcutCommands.add(new Alias("all", "all"));
		shortcutCommands.add(new Alias("hist", "hist"));
		shortcutCommands.add(new Alias("unres", "unres"));
		shortcutCommands.add(new Alias("help", "help"));
		shortcutCommands.add(new Alias("openfile", "openfile"));
		shortcutCommands.add(new Alias("config", "config"));
	}
	
	private static void initOperationCommands() {
		operationCommands = new ArrayList<Alias>();
		operationCommands.add(new Alias("add", "add"));
		operationCommands.add(new Alias("delete", "delete"));
		operationCommands.add(new Alias("edit", "edit"));
		operationCommands.add(new Alias("done", "done"));
		operationCommands.add(new Alias("search", "search"));
		operationCommands.add(new Alias("redo", "redo"));
		operationCommands.add(new Alias("undo", "undo"));
		operationCommands.add(new Alias("set", "set"));
		operationCommands.add(new Alias("save", "save"));
		operationCommands.add(new Alias("exit", "exit"));
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
	
	private static boolean isShortcutCommand(Alias alias) {
		boolean isShortcut = false;
		for (int i = 0; i < shortcutCommands.size(); i++) {
			if (alias.getOriginal().equals(shortcutCommands.get(i).getOriginal())) {
				isShortcut = true;
				break;
			}
		}
		return isShortcut;
	}
	
	private static void initPopupList() {
		
		popupList = new ListView<Alias>();
		
		popupList.setMinSize(WIDTH_POPUP, HEIGHT_POPUP);
		popupList.setMaxSize(WIDTH_POPUP, HEIGHT_POPUP);
		
		popupList.setOnKeyPressed(InterfaceController.getLogic().getAutoCompleteSelectHandler());
	}
	
	public static void initPopup() {
		
		initAllCommands();
		initPopupList();

		// Only show the popup list if there are results
		popup = new Popup();
	}
	
	public static void updatePopup(String searchTerm) {
		
		// Perform a matching search and get the results
		ArrayList<Alias> matchedCommands = getMatchingCommands(searchTerm);
		numOfResults = matchedCommands.size();
		List<Alias> matchedToSort = matchedCommands;
		Collections.sort(matchedToSort);
		
		// Convert from ArrayList to ObservableList
		ObservableList<Alias> matchedList = FXCollections.observableArrayList(matchedToSort);
		popupList.getItems().clear();
		popupList.setItems(matchedList);
		
		// Only show the popup list if there are results
		if (numOfResults > 0) {
			String textFieldInput = InterfaceController.getTextField().getText();
			String firstResult = popupList.getItems().get(0).getAlias();
			if (numOfResults == 1 && textFieldInput.equals(firstResult)) {
				isShowing = false;
				popup.hide();
			} else {
				// Wrap popupList in a Pane to prevent sizing problems
				Pane popupPane = new Pane(popupList);
				popupPane.getStyleClass().add("pane");

				popup.getScene().setRoot(popupPane);
				isShowing = true;
				popup.show(MainApp.stage);

				// Calculate window locations to set autocomplete anchor
				setX(MainApp.stage.getX());
				setY(MainApp.stage.getY());

				switchFocus();
			}
		} else {
			isShowing = false;
			popup.hide();
		}
	}
	
	public static void showPopup() {
		isShowing = true;
		popup.show(MainApp.stage);
	}
	
	public static void closePopup() {
		isShowing = false;
		popup.hide();
	}
	
	public static String getSelectedItem() {
		// Run a check to see if the returned item is a shortcut command
		// Any command but a shortcut command should have a space appended
		// to the end for convenience
		Alias selected = popupList.getSelectionModel().getSelectedItem();
		if (!isShortcutCommand(selected)) {
			return selected.getAlias() + " "; 
		} else {
			return selected.getAlias();
		}
	}
	
	public static void switchFocus() {
		popupList.requestFocus();
		popupList.getSelectionModel().select(0);
		popupList.scrollTo(0);
	}
	
	public static void setX(double coord) {
		popup.setX(coord + 6 + InterfaceController.WIDTH_SIDEBAR + InterfaceController.MARGIN_TEXT_FIELD);
	}
	
	public static void setY(double coord) {
		popup.setY(coord + MainApp.stage.getHeight() - InterfaceController.HEIGHT_FEEDBACK - InterfaceController.HEIGHT_TEXT_FIELD - HEIGHT_POPUP);
	}
}

class Alias implements Comparable<Alias> {
	 
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
	
	@Override
	public int compareTo(Alias b) {
		if (this.alias.equals(b.alias)) {
			return this.original.compareTo(b.original);
		} else {
			return this.alias.compareTo(b.alias);
		}
	}
}
