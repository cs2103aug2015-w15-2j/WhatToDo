package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import struct.View;

import java.util.regex.Pattern;

public class InterfaceController {

    // ==================================================
    // JavaFX controls used in the general interface
    // ==================================================

    // Used for initFilePathBar
	private static ImageView filepathConfig;
    private static HBox filepathBox, filepathLabelBox, filepathConfigBox;
    private static VBox filepathBoxWithLine;
    private static Label filepathLabel;
    private static Line filepathLine;

    // Used for initSideBarDefButton
    private static VBox sbDefBox;
    private static ImageView sbDefImage;
    
    // Used for initSideBarAllButton
    private static VBox sbAllBox;
    private static ImageView sbAllImage;
    
    // Used for initSideBarUnresButton
    private static VBox sbUnresBox;
    private static ImageView sbUnresImage;
    
    // Used for initSideBarDoneButton
    private static VBox sbDoneBox;
    private static ImageView sbDoneImage;
    
    // Used for initSideBarSearchButton
    private static VBox sbSearchBox;
    private static ImageView sbSearchImage;
    
    // Used for initSideBarHistoryButton
    private static VBox sbHistBox;
    private static ImageView sbHistImage;
    
    // Used for initSideBarHelpButton
    private static VBox sbHelpBox;
    private static ImageView sbHelpImage;
    
    // Used for initSideBarACIndicator
    private static VBox sbACBox;
    private static ImageView sbACImage;
    
    // Used for initSideBar
    private static VBox sbBox;
    private static HBox sbBoxWithLine;
    private static Line sbLine;

    // Used for initTextField
    private static VBox textBox;
    private static TextField textField;

    // Used for initFeedbackBar
    private static VBox feedbackBox, feedbackBoxWithLine;
    private static Label feedbackLabel;
    private static Line feedbackLine;

    // Used for initMainInterface
    private static Scene mainScene;
    private static VBox contentBoxNoSideBar, mainBox;
    private static HBox contentBoxWithSideBar, viewBox;
    private static Line viewLine;
    
    // Used for updateMainInterface
    protected static HBox defBox, allBox, histBox, doneBox, unresBox;
    protected static VBox summaryBox, searchBox;

    private static LogicController logicControl;  
    private static View currentView;
    
    // ======================================================================
    // Constants and fixed Strings used for program operations
    // ======================================================================
    
    // File paths for all images used
    protected static final String PATH_DEFAULT = "gui/resources/home.png";
    protected static final String PATH_DEFAULT_SELECTED = "gui/resources/home_selected.png";
    protected static final String PATH_DEFAULT_HOVER = "gui/resources/home_hover.png";
    protected static final String PATH_ALL = "gui/resources/all.png";
    protected static final String PATH_ALL_SELECTED = "gui/resources/all_selected.png";
    protected static final String PATH_ALL_HOVER = "gui/resources/all_hover.png";
    protected static final String PATH_HIST = "gui/resources/history.png";
    protected static final String PATH_HIST_SELECTED = "gui/resources/history_selected.png";
    protected static final String PATH_HIST_HOVER = "gui/resources/history_hover.png";
    protected static final String PATH_UNRESOLVED = "gui/resources/unresolved.png";
    protected static final String PATH_UNRESOLVED_SELECTED = "gui/resources/unresolved_selected.png";
    protected static final String PATH_UNRESOLVED_HOVER = "gui/resources/unresolved_hover.png";
    protected static final String PATH_DONE = "gui/resources/done.png";
    protected static final String PATH_DONE_SELECTED = "gui/resources/done_selected.png";
    protected static final String PATH_DONE_HOVER = "gui/resources/done_hover.png";
    protected static final String PATH_SEARCH = "gui/resources/search.png";
    protected static final String PATH_SEARCH_SELECTED = "gui/resources/search_selected.png";
    protected static final String PATH_SEARCH_HOVER = "gui/resources/search_hover.png";
    protected static final String PATH_HELP = "gui/resources/help.png";
    protected static final String PATH_HELP_SELECTED = "gui/resources/help_selected.png";
    protected static final String PATH_HELP_HOVER = "gui/resources/help_hover.png";
    protected static final String PATH_AC = "gui/resources/acIndicator.png";
    protected static final String PATH_CONFIG = "gui/resources/config.png";
    protected static final String PATH_TICK = "gui/resources/tick.png";
    
    protected static final String STATUS_TODO = "todo";
    protected static final String STATUS_DONE = "done";
    
    // Dimension variables used for sizing JavaFX components
    protected static final double WIDTH_DEFAULT = 100;
    protected static final double WIDTH_DEFAULT_BUTTON = 50;
    protected static final double WIDTH_SIDEBAR = 71;
    
    protected static final double HEIGHT_FILEPATH = 31;
    protected static final double HEIGHT_FEEDBACK = 31;
    protected static final double HEIGHT_TEXT_BOX = 40;
    protected static final double HEIGHT_TEXT_FIELD = 26;
    
    protected static final double WIDTH_VERT_LINE = 1;
    protected static final double HEIGHT_HORIZ_LINE = 1;
    
    protected static final double MARGIN_TEXT_BAR = 20;
    protected static final double MARGIN_TEXT_ELEMENT = 10;
    protected static final double MARGIN_TEXT_ELEMENT_HEIGHT = 3;
    protected static final double MARGIN_TEXT_ELEMENT_SEPARATOR = 10;
    protected static final double MARGIN_TEXT_FIELD = (HEIGHT_TEXT_BOX - HEIGHT_TEXT_FIELD) / 2;
    protected static final double MARGIN_BUTTON = 20;
    protected static final double MARGIN_COMPONENT = 10;
    protected static final double MARGIN_SCROLL = 30;
    protected static final double MARGIN_ARBITRARY = 6;
    protected static final double MARGIN_TICK = 10;
    protected static final double MARGIN_AC_INDICATOR = 13;

    // ===============================================================
    // Protected methods to initialize, update and close the main 
    // interface
    // ===============================================================
    
    /**
     * This method initializes all the components of the interface except for 
     * the viewBox, the area where the views are displayed.
     * 
     * Views are saved as HBoxes within this class, and swapped into viewBox
     * as and when called by updateMainInterface
     */
    protected static void initMainInterface() {
    	// Initial view set to ALL, 
    	// just a dummy state other than DEFAULT
    	currentView = View.ALL;
        logicControl = new LogicController();
        ViewIndexMap.initAllMaps();
        initSubComponents();
        initViewComponents();
        
        // Initial view will be empty
        viewBox = new HBox();
        viewLine = new Line(0, 0, WIDTH_DEFAULT, 0);

        contentBoxNoSideBar = new VBox(
        		filepathBoxWithLine, 
        		viewBox, 
        		viewLine, 
        		feedbackBoxWithLine, 
        		textBox);
        contentBoxWithSideBar = new HBox(sbBoxWithLine, contentBoxNoSideBar);
        mainBox = new VBox(contentBoxWithSideBar);
        mainScene = new Scene(mainBox);
        
        // Component formatting
        VBox.setVgrow(viewBox, Priority.ALWAYS);
        HBox.setHgrow(contentBoxNoSideBar, Priority.ALWAYS);
        VBox.setVgrow(contentBoxWithSideBar, Priority.ALWAYS);
        
        // Set resize listeners for the main scene
        mainScene.heightProperty().addListener(logicControl.getHeightListener());
        mainScene.widthProperty().addListener(logicControl.getWidthListener());

        // CSS
        viewLine.getStyleClass().add("line");
        mainScene.getStylesheets().add(InterfaceController.class.getResource(
                "/gui/stylesheets/Interface.css").toExternalForm());

        // Set the scene in MainApp
        MainApp.scene = mainScene;
    }
    
    /**
     * This method changes the view currently displayed in viewBox to the one
     * specified in the method call
     * 
     * @param view
     * 		      The view to display in the application window
     */
    protected static void updateMainInterface(View view) {
    	viewBox.getChildren().clear();
    	
    	switch (view) {
    	case DEFAULT:
    		DefaultViewController.updateDefView();
    		viewBox.getChildren().add(defBox);
    		
    		// Component formatting
            HBox.setHgrow(defBox, Priority.ALWAYS);
            
            changeButtonToUnselected(currentView);
            changeButtonToSelected(View.DEFAULT);
            
            currentView = View.DEFAULT;
    		break;
    		
    	case ALL:
    		AllViewController.updateAllView();
    		viewBox.getChildren().add(allBox);
    		
    		// Component formatting
            HBox.setHgrow(allBox, Priority.ALWAYS);
            
            changeButtonToUnselected(currentView);
            changeButtonToSelected(View.ALL);
            
            currentView = View.ALL;
    		break;
    		
    	case HISTORY:
    		viewBox.getChildren().add(histBox);
    		
    		// Component formatting
            HBox.setHgrow(histBox, Priority.ALWAYS);
    		
            changeButtonToUnselected(currentView);
            changeButtonToSelected(View.HISTORY);
            
            currentView = View.HISTORY;
    		break;
    		
    	case UNRESOLVED:
    		UnresolvedViewController.updateUnresView();
    		viewBox.getChildren().add(unresBox);
    		
    		// Component formatting
    		HBox.setHgrow(unresBox, Priority.ALWAYS);
    		
            changeButtonToUnselected(currentView);
            changeButtonToSelected(View.UNRESOLVED);
            
            currentView = View.UNRESOLVED;
    		break;
    		
    	case DONE:
    		DoneViewController.updateDoneView();
    		viewBox.getChildren().add(doneBox);
    		
    		// Component formatting
    		HBox.setHgrow(doneBox, Priority.ALWAYS);
    		
            changeButtonToUnselected(currentView);
            changeButtonToSelected(View.DONE);
            
            currentView = View.DONE;
    		break;
    		
    	case SEARCH:
    		viewBox.getChildren().add(searchBox);
    		
    		// Component formatting
            HBox.setHgrow(searchBox, Priority.ALWAYS);
    		
            changeButtonToUnselected(currentView);
            changeButtonToSelected(View.SEARCH);
            
            currentView = View.SEARCH;
    		break;
    		
    	case SUMMARY:
    		SummaryViewController.updateSummaryView();
    		viewBox.getChildren().add(summaryBox);
    		
    		// Component formatting
            HBox.setHgrow(summaryBox, Priority.ALWAYS);
    		break;
    		
    	default: //ignore
    		break;
    	}
    }
    
    protected static void closeMainInterface() {
    	currentView = View.EXIT;
    	MainApp.stage.close();
    }
    
    // ===============================================================
    // Protected methods to update and modify certain UI elements
    // from other GUI components
    // ===============================================================
    
    /**
     * This method updates the filepath shown in the filepath bar by calling
     * the getFilePath() method in LogicController
     */
    protected static void updateFilePathBar() {
    	filepathLabel = new Label(logicControl.getFilePath());
    	filepathLabelBox.getChildren().clear();
    	filepathLabelBox.getChildren().add(filepathLabel);
        
    	HBox.setHgrow(filepathLabelBox, Priority.ALWAYS);
    	
    	// Event handlers for mouse interaction
        filepathLabel.addEventHandler(
        		MouseEvent.MOUSE_ENTERED, logicControl.getPathHoverHandler(filepathLabel));
        filepathLabel.addEventHandler(
        		MouseEvent.MOUSE_EXITED, logicControl.getPathHoverHandler(filepathLabel));
        filepathLabel.addEventHandler(
        		MouseEvent.MOUSE_CLICKED, logicControl.getPathClickHandler());
    }

    /**
     * This method turns the autocomplete indicator in the sidebar
     * on/off based on whether autocomplete is activated
     */
    protected static void toggleAutoCompleteIndicator() {
    	sbACBox.getChildren().clear();
    	
    	if (!AutoComplete.isActivated()) {
    		sbACBox.getChildren().add(sbACImage);
    	}
    }
    
    /**
     * This method switches the button image to its corresponding selected
     * state
     * 
     * @param view
     *            The view which button is to be changed
     */
    protected static void changeButtonToSelected(View view) {
    	switch (view) {
    	case DEFAULT:
    		sbDefImage = new ImageView(PATH_DEFAULT_SELECTED);
    		sbDefBox.getChildren().clear();
    		sbDefBox.getChildren().add(sbDefImage);
    		break;
    	case ALL:
    		sbAllImage = new ImageView(PATH_ALL_SELECTED);
    		sbAllBox.getChildren().clear();
    		sbAllBox.getChildren().add(sbAllImage);
    		break;
    	case HISTORY:
    		sbHistImage = new ImageView(PATH_HIST_SELECTED);
    		sbHistBox.getChildren().clear();
    		sbHistBox.getChildren().add(sbHistImage);
    		break;
    	case UNRESOLVED:
    		sbUnresImage = new ImageView(PATH_UNRESOLVED_SELECTED);
    		sbUnresBox.getChildren().clear();
    		sbUnresBox.getChildren().add(sbUnresImage);
    		break;
    	case DONE:
    		sbDoneImage = new ImageView(PATH_DONE_SELECTED);
    		sbDoneBox.getChildren().clear();
    		sbDoneBox.getChildren().add(sbDoneImage);
    		break;
    	case SEARCH:
    		sbSearchImage = new ImageView(PATH_SEARCH_SELECTED);
    		sbSearchBox.getChildren().clear();
    		sbSearchBox.getChildren().add(sbSearchImage);
    		break;
    	case HELP:
    		sbHelpImage = new ImageView(PATH_HELP_SELECTED);
    		sbHelpBox.getChildren().clear();
    		sbHelpBox.getChildren().add(sbHelpImage);
    		break;
    	default:
    		// Do nothing
    		break;
    	}
    }
    
    /**
     * This method switches the button image to its corresponding unselected
     * state
     * 
     * @param view
     *            The view which button is to be changed
     */
    protected static void changeButtonToUnselected(View view) {
    	switch (view) {
    	case DEFAULT:
    		sbDefImage = new ImageView(PATH_DEFAULT);
    		sbDefBox.getChildren().clear();
    		sbDefBox.getChildren().add(sbDefImage);
    		break;
    	case ALL:
    		sbAllImage = new ImageView(PATH_ALL);
    		sbAllBox.getChildren().clear();
    		sbAllBox.getChildren().add(sbAllImage);
    		break;
    	case HISTORY:
    		sbHistImage = new ImageView(PATH_HIST);
    		sbHistBox.getChildren().clear();
    		sbHistBox.getChildren().add(sbHistImage);
    		break;
    	case UNRESOLVED:
    		sbUnresImage = new ImageView(PATH_UNRESOLVED);
    		sbUnresBox.getChildren().clear();
    		sbUnresBox.getChildren().add(sbUnresImage);
    		break;
    	case DONE:
    		sbDoneImage = new ImageView(PATH_DONE);
    		sbDoneBox.getChildren().clear();
    		sbDoneBox.getChildren().add(sbDoneImage);
    		break;
    	case SEARCH:
    		sbSearchImage = new ImageView(PATH_SEARCH);
    		sbSearchBox.getChildren().clear();
    		sbSearchBox.getChildren().add(sbSearchImage);
    		break;
    	case HELP:
    		sbHelpImage = new ImageView(PATH_HELP);
    		sbHelpBox.getChildren().clear();
    		sbHelpBox.getChildren().add(sbHelpImage);
    		break;
    	default:
    		// Do nothing
    		break;
    	}
    }
    
    
    /**
     * This method takes in an input String and creates a HBox with all the required
     * data formatted correctly to be inserted into one of the task/event windows.
     * 
     * @param displayData
     * 		      The task/event data to be displayed in the window
     * @param numOfElements
     * 		      The total number of tasks/events. Used for formatting the index box
     * @param index
     * 		      The view index of the particular task/event
     * @param isTask
     * 			  A boolean flag indicating whether the data to be input is a task
     * @return A HBox with the correct task/event data type(title, empty, data) 
     * 		   formatted for insertion into the scroll pane
     */
    protected static HBox initDisplayElement(String displayData, int numOfElements, 
    		int index, boolean isTask, View targetView) {
    	// If is a date or title element
    	if (InterfaceController.getLogic().isTitleOrDate(displayData)) {
    		return initTitleOrDateElement(displayData);
    	} else {
    		// Determine whether the element data is an element or a null response
    		String[] displayDataSplit = displayData.split(Pattern.quote("."));
    		// If no items to display
    		if (displayDataSplit.length == 1) {
    			return initNoResultElement(displayData);
    		} else {
    			return initDataElement(displayData, numOfElements, index, 
    					isTask, displayDataSplit, targetView);
    		}
    	}
    }
    
    // ===============================================================
    // Getters for JavaFX components required for LogicController
    // ===============================================================
    
    // Getters for textField and feedbackLabel to allow updates
    protected static TextField getTextField() {
    	return textField;
    }
    
    protected static Label getFeedbackLabel() {
    	return feedbackLabel;
    }
    
    // Getters for line components for binding to window sizes
    protected static Line getSbLine() {
    	return sbLine;
    }
    
    protected static Line getFeedbackLine() {
    	return feedbackLine;
    }
    
    protected static Line getViewLine() {
    	return viewLine;
    }
    
    protected static Line getFilepathLine() {
    	return filepathLine;
    }
    
    // Getters for buttons to allow access to the EventHandlers
    // in LogicController
    protected static VBox getHomeButton() {
    	return sbDefBox;
    }
    
    protected static VBox getAllButton() {
    	return sbAllBox;
    }
    
    protected static VBox getHistButton() {
    	return sbHistBox;
    }
    
    protected static VBox getUnresButton() {
    	return sbUnresBox;
    }
    
    protected static VBox getDoneButton() {
    	return sbDoneBox;
    }
    
    protected static VBox getSearchButton() {
    	return sbSearchBox;
    }
    
    protected static VBox getHelpButton() {
    	return sbHelpBox;
    }
    
    // ===============================================================
    // Getter and setter for currentView and logicControl
    // ===============================================================
    
    protected static View getCurrentView() {
    	return currentView;
    }
    
    protected static void setCurrentView(View newView) {
    	currentView = newView;
    }
    
    protected static LogicController getLogic() {
    	return logicControl;
    }
    
    // ======================================================================
    // Private methods for initializing element components of all the views
    // Used in initDisplayElement()
    // ======================================================================
    
    /**
     * This method calls the correct initDataElement() method based on whether
     * the task/event data is marked with a status field or not
     * 
     * @param displayData
     * 		      The task/event data to be displayed in the window
     * @param numOfElements
     * 		      The total number of tasks/events. Used for formatting the index box
     * @param index
     * 		      The view index of the particular task/event
     * @param isTask
     * 		      A boolean flag indicating whether the data to be input is a task
     * @param displayDataSplit
     * 		      A String[] of displayData split by a period(.). Used in 
     * 			  initDisplayElement() for branching, reused to avoid recomputation
     * @return A HBox with only the task/event data formatted for insertion into the
     * 		   scroll pane
     */
    private static HBox initDataElement(String displayData, int numOfElements, 
			int index, boolean isTask, String[] displayDataSplit, View targetView) {
    	
		// After removing the index, store it in the index map
		if (isStatusField(displayDataSplit[0].split(" ")[0])) {
			ViewIndexMap.add(targetView, Integer.parseInt(displayDataSplit[0].substring(5)));
		} else {
			ViewIndexMap.add(targetView, Integer.parseInt(displayDataSplit[0]));
		}
		
    	// Do a comparison on displayDataSplit to check if the returned data
    	// contains the "todo" or "done" field that requires indicator
    	String statusData = displayData.split(" ")[0];
    	if (statusData.equals(STATUS_DONE)) {
    		return initDataElementWithStatus(
    				displayData, numOfElements, index, isTask, displayDataSplit);
    	} else {
    		return initDataElementNoStatus(
    				displayData, numOfElements, index, isTask, displayDataSplit);
    	}
    }
	
	/**
	 * This method creates a HBox and formats it for an empty result String
	 * ("There are no results to display.")
	 * 
	 * @param displayData
	 * 		      The task/event data to be displayed in the window
	 * @return A HBox with the empty result String formatted for insertion into the
	 * 		   scroll pane
	 */
	private static HBox initNoResultElement(String displayData) {
		Label elementLabel = new Label(displayData);
		HBox labelBox = new HBox(elementLabel);
		HBox elementBox = new HBox(labelBox);
		
		formatLabelElement(elementLabel, labelBox);

		// Apply CSS style for regular data field
		elementBox.getStyleClass().add("element");
		return elementBox;
	}

	/**
	 * This method creates a HBox and formats it for a date or title String
	 * ("FLOAT", "SAT, 10 OCT 2015" etc...)
	 * 
	 * @param displayData
	 * 		      The task/event data to be displayed in the window
	 * @return A HBox with the date or title String formatted for insertion into the
	 * 		   scroll pane
	 */
	private static HBox initTitleOrDateElement(String displayData) {
		Label elementLabel = new Label(displayData.toUpperCase());
		HBox elementBox = new HBox(elementLabel);
		Line elementLine = new Line(0, 0, InterfaceController.WIDTH_DEFAULT, 0);
		elementBox.getChildren().add(elementLine);
		
		formatTitleOrDateElement(elementLabel, elementBox, elementLine);
		
		// CSS
		elementLine.getStyleClass().add("line");
		elementBox.getStyleClass().add("element-title");
		return elementBox;
	}

    /**
     * This method creates a HBox and formats it for a task/event data String
     * 
     * @param displayData
     * 		      The task/event data to be displayed in the window
     * @param numOfElements
     * 		      The total number of tasks/events. Used for formatting the index box
     * @param index
     * 		      The view index of the particular task/event
     * @param isTask
     * 		      A boolean flag indicating whether the data to be input is a task
     * @param displayDataSplit
     * 		      A String[] of displayData split by a period(.). Used in 
     * 			  initDisplayElement() for branching, reused to avoid recomputation
     * @return A HBox with only the task/event data formatted for insertion into the
     * 		   scroll pane
     */
	private static HBox initDataElementNoStatus(String displayData, int numOfElements, 
			int index, boolean isTask, String[] displayDataSplit) {
		
		Label elementIndex = new Label(String.valueOf(index));
		Label elementLabel = new Label(displayData.replaceFirst(displayDataSplit[0] + ".", "").trim());
		HBox indexBox = new HBox(elementIndex);
		HBox labelBox = new HBox(elementLabel);
		HBox elementBox = new HBox(indexBox, labelBox);

		// Component formatting
		formatIndexElement(numOfElements, elementIndex, indexBox);
		formatLabelElement(elementLabel, labelBox);
		
		// CSS
		elementBox.getStyleClass().add("element");
		elementIndex.getStyleClass().add("element-index-label");
		
		if (isTask) {
			indexBox.getStyleClass().add("element-index-task");
		} else {
			indexBox.getStyleClass().add("element-index-event");
		}
		return elementBox;
	}
	
    /**
     * This method creates a HBox and formats it for a task/event data String. This method
     * will also append a tick to the end of the label to indicate completed status
     * 
     * @param displayData
     * 		      The task/event data to be displayed in the window
     * @param numOfElements
     * 		      The total number of tasks/events. Used for formatting the index box
     * @param index
     * 		      The view index of the particular task/event
     * @param isTask
     * 		      A boolean flag indicating whether the data to be input is a task
     * @param displayDataSplit
     * 		      A String[] of displayData split by a period(.). Used in 
     * 			  initDisplayElement() for branching, reused to avoid recomputation
     * @return A HBox with only the task/event data formatted for insertion into the
     * 		   scroll pane
     */
	private static HBox initDataElementWithStatus(String displayData, int numOfElements, 
			int index, boolean isTask, String[] displayDataSplit) {
		
		Label elementIndex = new Label(String.valueOf(index));
		Label elementLabel = new Label(displayData.replaceFirst(displayDataSplit[0] + ".", "").trim());
		ImageView elementTick = new ImageView(PATH_TICK);
		HBox indexBox = new HBox(elementIndex);
		HBox labelBox = new HBox(elementLabel);
		HBox tickBox = new HBox(elementTick);
		HBox elementBox = new HBox(indexBox, labelBox, tickBox);

		// Component formatting
		formatIndexElement(numOfElements, elementIndex, indexBox);
		formatLabelElement(elementLabel, labelBox);
		formatTickElement(elementTick, tickBox);
		
		// CSS
		elementBox.getStyleClass().add("element");
		elementIndex.getStyleClass().add("element-index-label");
		
		if (isTask) {
			indexBox.getStyleClass().add("element-index-task");
		} else {
			indexBox.getStyleClass().add("element-index-event");
		}
		return elementBox;
	}
	
	/**
	 * This method formats the individual components of a HBox containing the
	 * index of the task/event
	 * 
	 * @param numOfElements
	 * 		      The total number of tasks/events. Used for formatting the index box
	 * @param elementIndex
	 * 		      The view index of the particular task/event
	 * @param indexBox
	 * 		      The HBox containing the index to be formatted
	 */
	private static void formatIndexElement(int numOfElements, Label elementIndex, HBox indexBox) {
		setFitToMaxWidth(indexBox, numOfElements);
		indexBox.setAlignment(Pos.CENTER);
		HBox.setMargin(elementIndex, new Insets(
				InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
				InterfaceController.MARGIN_TEXT_ELEMENT, 
				InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
				InterfaceController.MARGIN_TEXT_ELEMENT));
	}
	
	/**
	 * This method formats the individual components of a HBox containing the
	 * task/event data
	 * 
	 * @param elementLabel
	 * 		      The Label containing the task/event data to be formatted
	 * @param labelBox
	 * 		      The HBox containing the elementLabel
	 */
	private static void formatLabelElement(Label elementLabel, HBox labelBox) {
		elementLabel.setWrapText(true);
		HBox.setHgrow(labelBox, Priority.ALWAYS);
		HBox.setMargin(elementLabel, new Insets(
				InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
				InterfaceController.MARGIN_TEXT_ELEMENT, 
				InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
				InterfaceController.MARGIN_TEXT_ELEMENT));
	}

	/**
	 * This method formats the individual components of a HBox containing the 
	 * title or date element of the task/event
	 * 
	 * @param elementLabel
	 * 		      The Label containing the task/event data to be formatted
	 * @param elementBox
	 * 		      The HBox containing the elementLabel
	 * @param elementLine
	 * 		      The Line which is appended to the elementLabel in the 
	 * 		      elementBox as a vertical separator
	 */
	private static void formatTitleOrDateElement(Label elementLabel, HBox elementBox, Line elementLine) {
		setFitLineToWidth(elementLabel, elementBox, elementLine);
		elementBox.setAlignment(Pos.CENTER_LEFT);
		HBox.setMargin(elementLabel, new Insets(
				0, InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
				0, InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT));
	}

	/**
	 * This method formats the individual components of a HBox containing the
	 * tick indicating completed status of a task/event
	 * 
	 * @param elementTick
	 * 		      The ImageView containing the tick image to be formatted
	 * @param tickBox
	 * 			  The HBox containing the elementTick
	 */
	private static void formatTickElement(ImageView elementTick, HBox tickBox) {
		tickBox.setAlignment(Pos.CENTER);
		HBox.setMargin(elementTick, new Insets(0, MARGIN_TICK, 0, MARGIN_TICK));
	}
	
	/**
	 * This method calculates the maximum width used by the current largest index
	 * and sets the indexBox to that width to ensure consistency
	 * 
	 * @param indexBox
	 * 		      The HBox containing the index to be formatted
	 * @param numOfElements
	 * 		      The total number of tasks/events. Used for formatting the index box
	 */
	private static void setFitToMaxWidth(HBox indexBox, int numOfElements) {
		Text text = new Text(String.valueOf(numOfElements));
		Scene s = new Scene(new Group(text));
		// Override the CSS style to calculate the text width
		text.setStyle("-fx-font-family: \"Myriad Pro\"; "
				+ "-fx-font-size: 16; ");
		text.applyCss();
		double textWidth = Math.ceil(text.getLayoutBounds().getWidth());
		indexBox.setMinWidth(textWidth + 2 * InterfaceController.MARGIN_TEXT_ELEMENT);
	}
	
	/**
	 * This method calculates the correct length of the separator line elementLine
	 * to fit the line length to width of the scroll pane
	 * 
	 * @param elementLabel
	 * 		      The Label containing the task/event data to be formatted
	 * @param elementBox
	 * 		      The HBox containing the elementLabel
	 * @param elementLine
	 * 		      The Line which is appended to the elementLabel in the 
	 * 		      elementBox as a vertical separator
	 */
	private static void setFitLineToWidth(Label elementLabel, HBox elementBox, Line elementLine) {
		Text text = new Text(elementLabel.getText());
		Scene s = new Scene(new Group(text));
		text.setStyle("-fx-font-family: \"Myriad Pro\"; "
				+ "-fx-font-size: 16; "
				+ "-fx-font-weight: bold;");
		text.applyCss();
		// The arbitrary margin exists because text in a container is not perfectly 
		// aligned to the dimensions of its container
		double textWidth = Math.ceil(text.getLayoutBounds().getWidth());
		elementLine.endXProperty().bind(elementBox.widthProperty().subtract(
				textWidth + InterfaceController.MARGIN_ARBITRARY));
	}
	
	/**
	 * This method returns whether an input string contains a status field in the
	 * data ("todo" or "done")
	 * 
	 * @param data
	 * 		      The String to check if it matches either status field
	 * @return true if data equals to either "done" or "todo", false otherwise
	 */
	private static boolean isStatusField(String data) {
		return data.equals(STATUS_DONE) || data.equals(STATUS_TODO);
	}
	
    // ======================================================================
    // Private methods for initializing components of the main interface
	// Used in initMainInterface()
    // ======================================================================

	/**
	 * Initializes all the views by calling the relevant initializers in all
	 * the view controllers
	 */
	private static void initViewComponents() {
		DefaultViewController.initDefView();
        SummaryViewController.initSummaryView();
        AllViewController.initAllView();
        HistoryViewController.initHistView();
        UnresolvedViewController.initUnresView();
        DoneViewController.initDoneView();
        SearchViewController.initSearchView();
	}

	/**
	 * Initializes sub components of the main interface
	 * (filepathBar, sideBar, feedbackBar, textField)
	 */
	private static void initSubComponents() {
		initFilePathBar();
        initSideBar();
        initFeedbackBar();
        initTextField();
	}
	
    private static void initFilePathBar() {
        filepathLabel = new Label(logicControl.getFilePath());
        filepathLabelBox = new HBox(filepathLabel);
        filepathLine = new Line(0, 0, WIDTH_DEFAULT, 0);
        filepathConfig = new ImageView(PATH_CONFIG);
        filepathConfigBox = new HBox(filepathConfig);
        Region filepathBuffer = new Region();
        filepathBox = new HBox(filepathBuffer, filepathLabelBox, filepathConfigBox);
        filepathBoxWithLine = new VBox(filepathBox, filepathLine);
        
        // Component formatting
        filepathBuffer.setMaxSize(HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE, 
        		HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE);
        filepathBuffer.setMinSize(HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE, 
        		HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE);
        
        HBox.setHgrow(filepathLabelBox, Priority.ALWAYS);
        
        HBox.setMargin(filepathLabel, new Insets(
        		0, MARGIN_TEXT_BAR, 
        		0, MARGIN_TEXT_BAR));
        
        filepathLabelBox.setAlignment(Pos.CENTER);

        filepathBox.setMaxHeight(HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE);
        filepathBox.setMinHeight(HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE);

        filepathBoxWithLine.setMaxHeight(HEIGHT_FILEPATH);
        filepathBoxWithLine.setMinHeight(HEIGHT_FILEPATH);
        
        // Event handlers for mouse interactions
        filepathLabel.addEventHandler(
        		MouseEvent.MOUSE_ENTERED, 
        		logicControl.getPathHoverHandler(filepathLabel));
        filepathLabel.addEventHandler(
        		MouseEvent.MOUSE_EXITED, 
        		logicControl.getPathHoverHandler(filepathLabel));
        filepathLabel.addEventHandler(
        		MouseEvent.MOUSE_CLICKED, 
        		logicControl.getPathClickHandler());
        filepathConfigBox.addEventHandler(
        		MouseEvent.MOUSE_CLICKED, 
        		logicControl.getConfigClickHandler());

        // CSS
        filepathLine.getStyleClass().add("line");
        filepathBox.getStyleClass().add("display-bar");
        filepathBox.getStyleClass().add("gradient-regular");
    }
    
    private static void initTextField() {
        textField = new TextField();
        textField.requestFocus();
        textBox = new VBox(textField);
        
        // Component formatting
        textBox.setAlignment(Pos.CENTER);
        VBox.setMargin(textField, new Insets(0, MARGIN_TEXT_FIELD, 0, MARGIN_TEXT_FIELD));

        textBox.setMaxHeight(HEIGHT_TEXT_BOX);
        textBox.setMinHeight(HEIGHT_TEXT_BOX);
        
        textField.setMinHeight(HEIGHT_TEXT_FIELD);
        textField.setMaxHeight(HEIGHT_TEXT_FIELD);
        
        // Event handling for operations
        textField.setOnAction(logicControl.getTextInputHandler());
        textField.addEventFilter(KeyEvent.KEY_PRESSED, logicControl.getKeyPressHandler());
        
        // Initialize the popup for the first time
        AutoComplete.initPopup();
        logicControl.toggleAutoComplete();

        // CSS
        textBox.getStyleClass().add("gradient-regular");
        textField.getStyleClass().add("text-field");
    }

    private static void initFeedbackBar() {
        feedbackLabel = new Label("No commands entered yet.");
        feedbackLine = new Line(0, 0, WIDTH_DEFAULT, 0);

        feedbackBox = new VBox(feedbackLabel);
        feedbackBoxWithLine = new VBox(feedbackBox, feedbackLine);

        // Component formatting
        VBox.setMargin(feedbackLabel, new Insets(
        		0, MARGIN_TEXT_BAR, 
        		0, MARGIN_TEXT_BAR));
        
        feedbackBox.setAlignment(Pos.CENTER);

        feedbackBox.setMaxHeight(HEIGHT_FEEDBACK - HEIGHT_HORIZ_LINE);
        feedbackBox.setMinHeight(HEIGHT_FEEDBACK - HEIGHT_HORIZ_LINE);

        feedbackBoxWithLine.setMaxHeight(HEIGHT_FEEDBACK);
        feedbackBoxWithLine.setMinHeight(HEIGHT_FEEDBACK);

        // CSS
        feedbackLine.getStyleClass().add("line");
        feedbackBox.getStyleClass().add("display-bar");
        feedbackBox.getStyleClass().add("gradient-regular");
    }
    
    private static void initSideBar() {
        initAllSideBarButtons();
        Region sbSpacer = new Region();
        initSideBarACIndicator();
        
        changeButtonToSelected(View.DEFAULT);

        sbBox = new VBox(sbDefBox, 
        		sbAllBox, 
        		sbUnresBox,
        		sbDoneBox, 
        		sbSearchBox,
        		sbHistBox,
        		sbHelpBox, 
        		sbSpacer, 
        		sbACBox);
        sbLine = new Line(0, 0, 0, WIDTH_DEFAULT_BUTTON);
        sbBoxWithLine = new HBox(sbBox, sbLine);

        // Component formatting
        VBox.setVgrow(sbSpacer, Priority.ALWAYS);
        sbBoxWithLine.setMaxWidth(WIDTH_SIDEBAR);
        sbBoxWithLine.setMinWidth(WIDTH_SIDEBAR);

        // CSS
        sbLine.getStyleClass().add("line");
        sbBoxWithLine.getStyleClass().add("sidebar");
    }
    
    // ======================================================================
    // Private methods for initializing buttons in the sidebar
    // ======================================================================
    
    /** 
     * Initializes all the buttons in the sidebar by calling all the private
     * button initializers
     */
	private static void initAllSideBarButtons() {
		initSideBarDefButton();
        initSideBarAllButton();
        initSideBarUnresButton();
        initSideBarDoneButton();
        initSideBarSearchButton();
        initSideBarHistoryButton();
        initSideBarHelpButton();
	}
	
	
	private static void initSideBarDefButton() {
        sbDefImage = new ImageView(PATH_DEFAULT);
        sbDefBox = new VBox(sbDefImage);

        setButtonDimensions(sbDefBox);
        setButtonHandler(sbDefBox, View.DEFAULT);
        
        VBox.setMargin(sbDefBox, new Insets(
        		HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE, 
        		MARGIN_COMPONENT, 
        		MARGIN_BUTTON, 
        		MARGIN_COMPONENT));
    }
    
    private static void initSideBarAllButton() {
    	sbAllImage = new ImageView(PATH_ALL);
    	sbAllBox = new VBox(sbAllImage);

        setButtonDimensions(sbAllBox);
        setButtonHandler(sbAllBox, View.ALL);
        VBox.setMargin(sbAllBox, new Insets(
        		0, MARGIN_COMPONENT, MARGIN_BUTTON, MARGIN_COMPONENT));
    }
    
    private static void initSideBarUnresButton() {
    	sbUnresImage = new ImageView(PATH_UNRESOLVED);
    	sbUnresBox = new VBox(sbUnresImage);

        setButtonDimensions(sbUnresBox);
        setButtonHandler(sbUnresBox, View.UNRESOLVED);
        VBox.setMargin(sbUnresBox, new Insets(
        		0, MARGIN_COMPONENT, MARGIN_BUTTON, MARGIN_COMPONENT));
    }
    
    private static void initSideBarDoneButton() {
    	sbDoneImage = new ImageView(PATH_DONE);
    	sbDoneBox = new VBox(sbDoneImage);

        setButtonDimensions(sbDoneBox);
        setButtonHandler(sbDoneBox, View.DONE);
        VBox.setMargin(sbDoneBox, new Insets(
        		0, MARGIN_COMPONENT, MARGIN_BUTTON, MARGIN_COMPONENT));
    }
    
    private static void initSideBarSearchButton() {
    	sbSearchImage = new ImageView(PATH_SEARCH);
    	sbSearchBox = new VBox(sbSearchImage);

        setButtonDimensions(sbSearchBox);
        setButtonHandler(sbSearchBox, View.SEARCH);
        VBox.setMargin(sbSearchBox, new Insets(
        		0, MARGIN_COMPONENT, MARGIN_BUTTON, MARGIN_COMPONENT));
    }
    
    
    private static void initSideBarHistoryButton() {
    	sbHistImage = new ImageView(PATH_HIST);
    	sbHistBox = new VBox(sbHistImage);
    	
        setButtonDimensions(sbHistBox);
        setButtonHandler(sbHistBox, View.HISTORY);
        VBox.setMargin(sbHistBox, new Insets(
        		0, MARGIN_COMPONENT, MARGIN_BUTTON, MARGIN_COMPONENT));
    }
    
    private static void initSideBarHelpButton() {
    	sbHelpImage = new ImageView(PATH_HELP);
    	sbHelpBox = new VBox(sbHelpImage);

        setButtonDimensions(sbHelpBox);
        setButtonHandler(sbHelpBox, View.HELP);
        VBox.setMargin(sbHelpBox, new Insets(
        		0, MARGIN_COMPONENT, MARGIN_BUTTON, MARGIN_COMPONENT));
    }
    
    private static void initSideBarACIndicator() {
    	sbACImage = new ImageView(PATH_AC);
    	sbACBox = new VBox(sbACImage);
    	
    	sbACBox.setAlignment(Pos.CENTER);
        VBox.setMargin(sbACBox, new Insets(
        		0, 0, MARGIN_AC_INDICATOR, 0));
    }
    
    /**
     * This method sets the button dimensions to the constant defined 
     * for buttons
     * 
     * @param buttonBox
     * 		      The VBox containing the button
     */	
	private static void setButtonDimensions(VBox buttonBox) {
		buttonBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
		buttonBox.setMinWidth(WIDTH_DEFAULT_BUTTON);
		buttonBox.setMaxHeight(WIDTH_DEFAULT_BUTTON);
		buttonBox.setMinHeight(WIDTH_DEFAULT_BUTTON);
	}
	
	/**
	 * This method attaches EventHandlers to the buttons to be able to
	 * handle mouse clicks and hover events
	 * 
	 * @param buttonBox
	 * 		      The VBox containing the button
	 * @param view
	 * 		      The View of which to associate the button operation with
	 */
	private static void setButtonHandler(VBox buttonBox, View view) {
		buttonBox.addEventHandler(
        		MouseEvent.MOUSE_ENTERED, 
        		logicControl.getButtonHoverHandler(view));
		buttonBox.addEventHandler(
        		MouseEvent.MOUSE_EXITED, 
        		logicControl.getButtonHoverHandler(view));
		buttonBox.addEventHandler(
        		MouseEvent.MOUSE_PRESSED, 
        		logicControl.getButtonClickHandler(view));
	}
}
