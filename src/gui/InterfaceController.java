package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;

import struct.View;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;

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
    
    // Used for initSideBarHistoryButton
    private static VBox sbHistBox;
    private static ImageView sbHistImage;
    
    // Used for initSideBarUnresButton
    private static VBox sbUnresBox;
    private static ImageView sbUnresImage;
    
    // Used for initSideBarDoneButton
    private static VBox sbDoneBox;
    private static ImageView sbDoneImage;
    
    // Used for initSideBarSearchButton
    private static VBox sbSearchBox;
    private static ImageView sbSearchImage;
    
    // Used for initSideBarHelpButton
    private static VBox sbHelpBox;
    private static ImageView sbHelpImage;
    
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
    
    // file paths for button images
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
    protected static final String PATH_CONFIG = "gui/resources/config.png";
    protected static final String PATH_TICK = "gui/resources/tick.png";
    
	protected static final String PATH_CONFIG_FILE = "config" + File.separator + "config.txt";
    
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

        initFilePathBar();
        initSideBar();
        initFeedbackBar();
        initTextField();
        
        DefaultViewController.initDefView();
        SummaryViewController.initSummaryView();
        AllViewController.initAllView();
        HistoryViewController.initHistView();
        UnresolvedViewController.initUnresView();
        DoneViewController.initDoneView();
        SearchViewController.initSearchView();
        
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
    
    /**
     * This method opens the text file currently set by the application for writing.
     * This text file is opened in the user's default associated application
     */
    protected static void openFileLocation() {
		try {
			HistoryViewController.updateHistView("Opening file...");
			InterfaceController.getFeedbackLabel().setText("Opening file...");
			Desktop.getDesktop().open(
					new File(logicControl.getFilePath()));
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
    
    /**
     * This method closes the application window
     */
    protected static void closeMainInterface() {
    	currentView = View.EXIT;
    	MainApp.stage.close();
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
    
    // Getters for buttons for mouse event handlers
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
    // Private methods, used for initialization of individual components
    // ======================================================================
    
    private static void initFilePathBar() {
        filepathLabel = new Label(logicControl.getFilePath());
        filepathLabelBox = new HBox(filepathLabel);
        
        filepathLine = new Line(0, 0, WIDTH_DEFAULT, 0);
        filepathConfig = new ImageView(PATH_CONFIG);
        filepathConfigBox = new HBox(filepathConfig);
        
        Region filepathBuffer = new Region();
        filepathBuffer.setMaxSize(HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE, 
        		HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE);
        filepathBuffer.setMinSize(HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE, 
        		HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE);
        
        filepathBox = new HBox(filepathBuffer, filepathLabelBox, filepathConfigBox);
        filepathBoxWithLine = new VBox(filepathBox, filepathLine);
        
        // Component formatting
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
        		MouseEvent.MOUSE_ENTERED, logicControl.getPathHoverHandler(filepathLabel));
        filepathLabel.addEventHandler(
        		MouseEvent.MOUSE_EXITED, logicControl.getPathHoverHandler(filepathLabel));
        filepathLabel.addEventHandler(
        		MouseEvent.MOUSE_CLICKED, logicControl.getPathClickHandler());
        filepathConfigBox.addEventHandler(
        		MouseEvent.MOUSE_CLICKED, logicControl.getConfigClickHandler());

        // CSS
        filepathLine.getStyleClass().add("line");
        filepathBox.getStyleClass().add("display-bar");
        filepathBox.getStyleClass().add("gradient-regular");
    }

    private static void initSideBarDefButton() {
        sbDefImage = new ImageView(PATH_DEFAULT);
        sbDefBox = new VBox(sbDefImage);

        sbDefBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
        sbDefBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

        sbDefBox.setMaxHeight(WIDTH_DEFAULT_BUTTON);
        sbDefBox.setMinHeight(WIDTH_DEFAULT_BUTTON);
        
        // Event handlers for mouse interactions
        sbDefBox.addEventHandler(
        		MouseEvent.MOUSE_ENTERED, 
        		logicControl.getButtonHoverHandler(View.DEFAULT));
        sbDefBox.addEventHandler(
        		MouseEvent.MOUSE_EXITED, 
        		logicControl.getButtonHoverHandler(View.DEFAULT));
        sbDefBox.addEventHandler(
        		MouseEvent.MOUSE_PRESSED, 
        		logicControl.getButtonClickHandler(View.DEFAULT));
    }
    
    private static void initSideBarAllButton() {
    	sbAllImage = new ImageView(PATH_ALL);
    	sbAllBox = new VBox(sbAllImage);

    	sbAllBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
    	sbAllBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

    	sbAllBox.setMaxHeight(WIDTH_DEFAULT_BUTTON);
    	sbAllBox.setMinHeight(WIDTH_DEFAULT_BUTTON);
    	
        // Event handlers for mouse interactions
        sbAllBox.addEventHandler(
        		MouseEvent.MOUSE_ENTERED, 
        		logicControl.getButtonHoverHandler(View.ALL));
        sbAllBox.addEventHandler(
        		MouseEvent.MOUSE_EXITED, 
        		logicControl.getButtonHoverHandler(View.ALL));
        sbAllBox.addEventHandler(
        		MouseEvent.MOUSE_PRESSED, 
        		logicControl.getButtonClickHandler(View.ALL));
    }

    private static void initSideBarHistoryButton() {
    	sbHistImage = new ImageView(PATH_HIST);
    	sbHistBox = new VBox(sbHistImage);
    	
    	sbHistBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
    	sbHistBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

    	sbHistBox.setMaxHeight(WIDTH_DEFAULT_BUTTON);
    	sbHistBox.setMinHeight(WIDTH_DEFAULT_BUTTON);
    	
        // Event handlers for mouse interactions
    	sbHistBox.addEventHandler(
        		MouseEvent.MOUSE_ENTERED, 
        		logicControl.getButtonHoverHandler(View.HISTORY));
    	sbHistBox.addEventHandler(
        		MouseEvent.MOUSE_EXITED, 
        		logicControl.getButtonHoverHandler(View.HISTORY));
    	sbHistBox.addEventHandler(
        		MouseEvent.MOUSE_PRESSED, 
        		logicControl.getButtonClickHandler(View.HISTORY));
    }
    
    private static void initSideBarUnresButton() {
    	sbUnresImage = new ImageView(PATH_UNRESOLVED);
    	sbUnresBox = new VBox(sbUnresImage);

    	sbUnresBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
    	sbUnresBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

    	sbUnresBox.setMaxHeight(WIDTH_DEFAULT_BUTTON);
    	sbUnresBox.setMinHeight(WIDTH_DEFAULT_BUTTON);
    	
        // Event handlers for mouse interactions
    	sbUnresBox.addEventHandler(
        		MouseEvent.MOUSE_ENTERED, 
        		logicControl.getButtonHoverHandler(View.UNRESOLVED));
    	sbUnresBox.addEventHandler(
        		MouseEvent.MOUSE_EXITED, 
        		logicControl.getButtonHoverHandler(View.UNRESOLVED));
    	sbUnresBox.addEventHandler(
        		MouseEvent.MOUSE_PRESSED, 
        		logicControl.getButtonClickHandler(View.UNRESOLVED));
    }
    
    private static void initSideBarDoneButton() {
    	sbDoneImage = new ImageView(PATH_DONE);
    	sbDoneBox = new VBox(sbDoneImage);

    	sbDoneBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
    	sbDoneBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

    	sbDoneBox.setMaxHeight(WIDTH_DEFAULT_BUTTON);
    	sbDoneBox.setMinHeight(WIDTH_DEFAULT_BUTTON);
    	
        // Event handlers for mouse interactions
    	sbDoneBox.addEventHandler(
        		MouseEvent.MOUSE_ENTERED, 
        		logicControl.getButtonHoverHandler(View.DONE));
    	sbDoneBox.addEventHandler(
        		MouseEvent.MOUSE_EXITED, 
        		logicControl.getButtonHoverHandler(View.DONE));
    	sbDoneBox.addEventHandler(
        		MouseEvent.MOUSE_PRESSED, 
        		logicControl.getButtonClickHandler(View.DONE));
    }
    
    private static void initSideBarSearchButton() {
    	sbSearchImage = new ImageView(PATH_SEARCH);
    	sbSearchBox = new VBox(sbSearchImage);

    	sbSearchBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
    	sbSearchBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

    	sbSearchBox.setMaxHeight(WIDTH_DEFAULT_BUTTON);
    	sbSearchBox.setMinHeight(WIDTH_DEFAULT_BUTTON);
    	
        // Event handlers for mouse interactions
    	sbSearchBox.addEventHandler(
        		MouseEvent.MOUSE_ENTERED, 
        		logicControl.getButtonHoverHandler(View.SEARCH));
    	sbSearchBox.addEventHandler(
        		MouseEvent.MOUSE_EXITED, 
        		logicControl.getButtonHoverHandler(View.SEARCH));
    	sbSearchBox.addEventHandler(
        		MouseEvent.MOUSE_PRESSED, 
        		logicControl.getButtonClickHandler(View.SEARCH));
    }
    
    private static void initSideBarHelpButton() {
    	sbHelpImage = new ImageView(PATH_HELP);
    	sbHelpBox = new VBox(sbHelpImage);

    	sbHelpBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
    	sbHelpBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

    	sbHelpBox.setMaxHeight(WIDTH_DEFAULT_BUTTON);
    	sbHelpBox.setMinHeight(WIDTH_DEFAULT_BUTTON);
    	
        // Event handlers for mouse interactions
    	sbHelpBox.addEventHandler(
        		MouseEvent.MOUSE_ENTERED, 
        		logicControl.getButtonHoverHandler(View.HELP));
    	sbHelpBox.addEventHandler(
        		MouseEvent.MOUSE_EXITED, 
        		logicControl.getButtonHoverHandler(View.HELP));
    	sbHelpBox.addEventHandler(
        		MouseEvent.MOUSE_PRESSED, 
        		logicControl.getButtonClickHandler(View.HELP));
    }

    private static void initSideBar() {
        initSideBarDefButton();
        initSideBarAllButton();
        initSideBarHistoryButton();
        initSideBarUnresButton();
        initSideBarDoneButton();
        initSideBarSearchButton();
        initSideBarHelpButton();
        
        changeButtonToSelected(View.DEFAULT);

        sbBox = new VBox(sbDefBox, 
        		sbAllBox, 
        		sbHistBox, 
        		sbUnresBox,
        		sbDoneBox, 
        		sbSearchBox, 
        		sbHelpBox);
        
        sbLine = new Line(0, 0, 0, WIDTH_DEFAULT_BUTTON);

        sbBoxWithLine = new HBox(sbBox, sbLine);

        // Component formatting
        VBox.setMargin(sbDefBox, new Insets(
        		HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE, 
        		MARGIN_COMPONENT, 
        		MARGIN_BUTTON, 
        		MARGIN_COMPONENT));
        
        VBox.setMargin(sbAllBox, new Insets(
        		0, MARGIN_COMPONENT, MARGIN_BUTTON, MARGIN_COMPONENT));
        VBox.setMargin(sbHistBox, new Insets(
        		0, MARGIN_COMPONENT, MARGIN_BUTTON, MARGIN_COMPONENT));
        VBox.setMargin(sbUnresBox, new Insets(
        		0, MARGIN_COMPONENT, MARGIN_BUTTON, MARGIN_COMPONENT));
        VBox.setMargin(sbDoneBox, new Insets(
        		0, MARGIN_COMPONENT, MARGIN_BUTTON, MARGIN_COMPONENT));
        VBox.setMargin(sbSearchBox, new Insets(
        		0, MARGIN_COMPONENT, MARGIN_BUTTON, MARGIN_COMPONENT));
        VBox.setMargin(sbHelpBox, new Insets(
        		0, MARGIN_COMPONENT, MARGIN_BUTTON, MARGIN_COMPONENT));
        
        sbBoxWithLine.setMaxWidth(WIDTH_SIDEBAR);
        sbBoxWithLine.setMinWidth(WIDTH_SIDEBAR);

        // CSS
        sbLine.getStyleClass().add("line");
        sbBoxWithLine.getStyleClass().add("sidebar");
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
        
        // Initialize autocomplete
        AutoComplete.initPopup();
        textField.textProperty().addListener(logicControl.getAutoCompleteListener());

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
}
