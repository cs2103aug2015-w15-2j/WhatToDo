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

    /* ================================================================================
     * JavaFX controls used in the general interface
     * ================================================================================
     */

    // Used for initFilePathBar
	private static ImageView filepathConfig;
    private static HBox filepathBox, filepathLabelBox;
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

    protected static LogicController logicControl;
    
    private static View currentView;
    
    // Values for button images
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
    
	protected static final String PATH_CONFIG_FILE = "config" + File.separator + "config";
    
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

    private static void initFilePathBar() {

        filepathLabel = new Label(logicControl.getFilePath());
        filepathLabelBox = new HBox(filepathLabel);
        
        filepathLine = new Line(0, 0, WIDTH_DEFAULT, 0);
        filepathConfig = new ImageView(PATH_CONFIG);
        
        Region filepathBuffer = new Region();
        filepathBuffer.setMaxSize(HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE, 
        		HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE);
        filepathBuffer.setMinSize(HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE, 
        		HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE);
        
        HBox.setHgrow(filepathLabelBox, Priority.ALWAYS);
        
        filepathBox = new HBox(filepathBuffer, filepathLabelBox, filepathConfig);
        filepathBoxWithLine = new VBox(filepathBox, filepathLine);

        // Set margins for the filepath label
        HBox.setMargin(filepathLabel, new Insets(
        		0, MARGIN_TEXT_BAR, 
        		0, MARGIN_TEXT_BAR));
        
        // Set alignment of the filepath label
        filepathLabelBox.setAlignment(Pos.CENTER);

        // Fix height for the filepath bar without lines
        filepathBox.setMaxHeight(HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE);
        filepathBox.setMinHeight(HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE);

        // Fix height for the filepath bar
        // +1 for line widths
        filepathBoxWithLine.setMaxHeight(HEIGHT_FILEPATH);
        filepathBoxWithLine.setMinHeight(HEIGHT_FILEPATH);
        
        filepathLabel.addEventHandler(
        		MouseEvent.MOUSE_ENTERED, logicControl.getPathHoverHandler(filepathLabel));
        filepathLabel.addEventHandler(
        		MouseEvent.MOUSE_EXITED, logicControl.getPathHoverHandler(filepathLabel));
        filepathLabel.addEventHandler(
        		MouseEvent.MOUSE_CLICKED, logicControl.getPathClickHandler());
        filepathConfig.addEventHandler(
        		MouseEvent.MOUSE_CLICKED, logicControl.getConfigClickHandler());

        // CSS
        filepathLine.getStyleClass().add("line");
        filepathBox.getStyleClass().add("display-bar");
        filepathBox.getStyleClass().add("gradient-regular");
    }
    
    protected static void updateFilePathBar() {
    	
    	filepathLabel = new Label(logicControl.getFilePath());
    	filepathLabelBox.getChildren().clear();
    	filepathLabelBox.getChildren().add(filepathLabel);
        
    	HBox.setHgrow(filepathLabelBox, Priority.ALWAYS);
    	
        filepathLabel.addEventHandler(
        		MouseEvent.MOUSE_ENTERED, logicControl.getPathHoverHandler(filepathLabel));
        filepathLabel.addEventHandler(
        		MouseEvent.MOUSE_EXITED, logicControl.getPathHoverHandler(filepathLabel));
        filepathLabel.addEventHandler(
        		MouseEvent.MOUSE_CLICKED, logicControl.getPathClickHandler());
    }

    private static void initSideBarDefButton() {

        sbDefImage = new ImageView(PATH_DEFAULT);
        sbDefBox = new VBox(sbDefImage);

        // Fix width and height for the button
        sbDefBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
        sbDefBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

        sbDefBox.setMaxHeight(WIDTH_DEFAULT_BUTTON);
        sbDefBox.setMinHeight(WIDTH_DEFAULT_BUTTON);
        
        // Set listener for mouse interactions
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

    	// Fix width and height for the button
    	sbAllBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
    	sbAllBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

    	sbAllBox.setMaxHeight(WIDTH_DEFAULT_BUTTON);
    	sbAllBox.setMinHeight(WIDTH_DEFAULT_BUTTON);
    	
        // Set listener for mouse interactions
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
    	
    	// Fix width and height for the button
    	sbHistBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
    	sbHistBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

    	sbHistBox.setMaxHeight(WIDTH_DEFAULT_BUTTON);
    	sbHistBox.setMinHeight(WIDTH_DEFAULT_BUTTON);
    	
        // Set listener for mouse interactions
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

    	// Fix width and height for the button
    	sbUnresBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
    	sbUnresBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

    	sbUnresBox.setMaxHeight(WIDTH_DEFAULT_BUTTON);
    	sbUnresBox.setMinHeight(WIDTH_DEFAULT_BUTTON);
    	
        // Set listener for mouse interactions
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

    	// Fix width and height for the button
    	sbDoneBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
    	sbDoneBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

    	sbDoneBox.setMaxHeight(WIDTH_DEFAULT_BUTTON);
    	sbDoneBox.setMinHeight(WIDTH_DEFAULT_BUTTON);
    	
        // Set listener for mouse interactions
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

    	// Fix width and height for the button
    	sbSearchBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
    	sbSearchBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

    	sbSearchBox.setMaxHeight(WIDTH_DEFAULT_BUTTON);
    	sbSearchBox.setMinHeight(WIDTH_DEFAULT_BUTTON);
    	
        // Set listener for mouse interactions
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

    	// Fix width and height for the button
    	sbHelpBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
    	sbHelpBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

    	sbHelpBox.setMaxHeight(WIDTH_DEFAULT_BUTTON);
    	sbHelpBox.setMinHeight(WIDTH_DEFAULT_BUTTON);
    	
        // Set listener for mouse interactions
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

        // Set margins for the buttons
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
        
        // Fix the width for the sidebar
        // +1 for line width
        sbBoxWithLine.setMaxWidth(WIDTH_SIDEBAR);
        sbBoxWithLine.setMinWidth(WIDTH_SIDEBAR);

        // CSS
        sbLine.getStyleClass().add("line");
        sbBoxWithLine.getStyleClass().add("sidebar");
    }

    private static void initTextField() {

        textField = new TextField();
        textField.requestFocus();
        
        // Assign text handlers to the text field
        textField.setOnAction(logicControl.getTextInputHandler());
        textField.addEventFilter(KeyEvent.KEY_PRESSED, logicControl.getKeyPressHandler());
        
        textBox = new VBox(textField);

        // Set the text field to always center in the area
        textBox.setAlignment(Pos.CENTER);
        VBox.setMargin(textField, new Insets(0, MARGIN_TEXT_FIELD, 0, MARGIN_TEXT_FIELD));

        // Fix the height of the text field
        textBox.setMaxHeight(HEIGHT_TEXT_BOX);
        textBox.setMinHeight(HEIGHT_TEXT_BOX);
        
        // Set the height of the text field
        textField.setMinHeight(HEIGHT_TEXT_FIELD);
        textField.setMaxHeight(HEIGHT_TEXT_FIELD);

        // CSS
        textBox.getStyleClass().add("gradient-regular");
        textField.getStyleClass().add("text-field");
    }

    private static void initFeedbackBar() {

        feedbackLabel = new Label("No commands entered yet.");
        feedbackLine = new Line(0, 0, WIDTH_DEFAULT, 0);

        feedbackBox = new VBox(feedbackLabel);
        feedbackBoxWithLine = new VBox(feedbackBox, feedbackLine);

        // Set margins for the feedback label
        VBox.setMargin(feedbackLabel, new Insets(
        		0, MARGIN_TEXT_BAR, 
        		0, MARGIN_TEXT_BAR));
        
        // Set alignment of the feedback label
        feedbackBox.setAlignment(Pos.CENTER);

        // Fix the height of the feedback label
        feedbackBox.setMaxHeight(HEIGHT_FEEDBACK - HEIGHT_HORIZ_LINE);
        feedbackBox.setMinHeight(HEIGHT_FEEDBACK - HEIGHT_HORIZ_LINE);

        // Fix the height of the feedback bar
        // +1 for line width
        feedbackBoxWithLine.setMaxHeight(HEIGHT_FEEDBACK);
        feedbackBoxWithLine.setMinHeight(HEIGHT_FEEDBACK);

        // CSS
        feedbackLine.getStyleClass().add("line");
        feedbackBox.getStyleClass().add("display-bar");
        feedbackBox.getStyleClass().add("gradient-regular");
    }

    public static void initMainInterface() {
    	
    	// Initial view set to ALL, 
    	// just a dummy state other than DEFAULT
    	currentView = View.ALL;
    	
    	// Initialize a LogicController
        logicControl = new LogicController();
        
        // Initialize the ViewIndexMap
        ViewIndexMap.initAllMaps();

        initFilePathBar();
        initSideBar();
        
        // Initialize all the views
        DefaultViewController.initDefView();
        SummaryViewController.initSummaryView();
        AllViewController.initAllView();
        HistoryViewController.initHistView();
        UnresolvedViewController.initUnresView();
        //DoneViewController.initDoneView();
        SearchViewController.initSearchView();
        
        // Initial view will be empty
        viewBox = new HBox();
        initFeedbackBar();
        initTextField();
        
        // Create the line separator for defBox
        viewLine = new Line(0, 0, WIDTH_DEFAULT, 0);

        // Create the region excluding the sidebar
        contentBoxNoSideBar = new VBox(
        		filepathBoxWithLine, 
        		viewBox, 
        		viewLine, 
        		feedbackBoxWithLine, 
        		textBox);
        
        // Set the height of defBox to grow with the window
        VBox.setVgrow(viewBox, Priority.ALWAYS);

        // Create the region including the sidebar
        contentBoxWithSideBar = new HBox(sbBoxWithLine, contentBoxNoSideBar);

        // Set the width of contentBoxNoSideBar to grow with window
        HBox.setHgrow(contentBoxNoSideBar, Priority.ALWAYS);

        // Create the main VBox containing everything
        mainBox = new VBox(contentBoxWithSideBar);

        // Set the height of contentBoxWithSideBar to grow with window
        VBox.setVgrow(contentBoxWithSideBar, Priority.ALWAYS);

        mainScene = new Scene(mainBox);
        
        // Set resize listeners for the main scene
        mainScene.heightProperty().addListener(logicControl.getHeightListener());
        mainScene.widthProperty().addListener(logicControl.getWidthListener());

        // Set CSS styling
        viewLine.getStyleClass().add("line");
        mainScene.getStylesheets().add(InterfaceController.class.getResource(
                "/gui/stylesheets/Interface.css").toExternalForm());

        // Set the scene in MainApp
        MainApp.scene = mainScene;
    }
    
    public static void updateMainInterface(View view) {
    	
    	// Clear the old content
    	viewBox.getChildren().clear();
    	
    	switch (view) {
    	
    	case DEFAULT:
    		DefaultViewController.updateDefView();
    		viewBox.getChildren().add(defBox);
    		
    		// Set default box to grow with view box
            HBox.setHgrow(defBox, Priority.ALWAYS);
            
            // Change buttons
            changeButtonToSelected(View.DEFAULT);
            changeButtonToUnselected(currentView);
            
            // Update currentView
            currentView = View.DEFAULT;
    		break;
    		
    	case ALL:
    		AllViewController.updateAllView();
    		viewBox.getChildren().add(allBox);
    		
    		// Set all box to grow with view box
            HBox.setHgrow(allBox, Priority.ALWAYS);
            
            // Change buttons
            changeButtonToSelected(View.ALL);
            changeButtonToUnselected(currentView);
            
            // Update currentView
            currentView = View.ALL;
    		break;
    		
    	case HISTORY:
    		viewBox.getChildren().add(histBox);
    		
    		// Set history box to grow with view box
            HBox.setHgrow(histBox, Priority.ALWAYS);
    		
            // Change buttons
            changeButtonToSelected(View.HISTORY);
            changeButtonToUnselected(currentView);
            
            // Update currentView
            currentView = View.HISTORY;
    		break;
    		
    	case UNRESOLVED:
    		UnresolvedViewController.updateUnresView();
    		viewBox.getChildren().add(unresBox);
    		
    		HBox.setHgrow(unresBox, Priority.ALWAYS);
    		
            // Change buttons
            changeButtonToSelected(View.UNRESOLVED);
            changeButtonToUnselected(currentView);
            
            // Update currentView
            currentView = View.UNRESOLVED;
    		break;
    		
    	case DONE:
    		//DoneViewController.updateDoneView();
    		viewBox.getChildren().add(doneBox);
    		
    		HBox.setHgrow(doneBox, Priority.ALWAYS);
    		
            // Change buttons
            changeButtonToSelected(View.DONE);
            changeButtonToUnselected(currentView);
            
            // Update currentView
            currentView = View.DONE;
    		break;
    		
    	case SEARCH:
    		viewBox.getChildren().add(searchBox);
    		
    		// Set done box to grow with view box
            HBox.setHgrow(searchBox, Priority.ALWAYS);
    		
            // Change buttons
            changeButtonToSelected(View.SEARCH);
            changeButtonToUnselected(currentView);
            
            // Update currentView
            currentView = View.SEARCH;
    		break;
    		
    	case SUMMARY:
    		SummaryViewController.updateSummaryView();
    		viewBox.getChildren().add(summaryBox);
    		
    		// Set summary box to grow with view box
            HBox.setHgrow(summaryBox, Priority.ALWAYS);
            
            // Update currentView
            currentView = View.SUMMARY;
    		break;
    		
    	default: //ignore
    		break;
    	}
    }

    /* ================================================================================
     * Public methods to perform changes to the interface
     * ================================================================================
     */
    
    public static void openFileLocation() {
		try {
			HistoryViewController.updateHistView("Opening file...");
			InterfaceController.getFeedbackLabel().setText("Opening file...");
			Desktop.getDesktop().open(
					new File(InterfaceController.logicControl.getFilePath()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void openConfigLocation() {
    	try {
    		HistoryViewController.updateHistView("Opening config...");
    		InterfaceController.getFeedbackLabel().setText("Opening config...");
    		Desktop.getDesktop().open(new File(PATH_CONFIG_FILE));
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public static void closeMainInterface() {
    	
    	currentView = View.EXIT;
    	MainApp.stage.close();
    }
    
    
    /* ================================================================================
     * Getters for LogicController to access required JavaFX components
     * ================================================================================
     */
    
    public static TextField getTextField() {
    	return textField;
    }
    
    public static Label getFeedbackLabel() {
    	return feedbackLabel;
    }
    
    // Return lines so as to dynamically adjust line height and width
    public static Line getSbLine() {
    	return sbLine;
    }
    
    public static Line getFeedbackLine() {
    	return feedbackLine;
    }
    
    public static Line getViewLine() {
    	return viewLine;
    }
    
    public static Line getFilepathLine() {
    	return filepathLine;
    }
    
    // Return buttons for mouse event handlers
    public static VBox getHomeButton() {
    	return sbDefBox;
    }
    
    public static VBox getAllButton() {
    	return sbAllBox;
    }
    
    public static VBox getHistButton() {
    	return sbHistBox;
    }
    
    public static VBox getUnresButton() {
    	return sbUnresBox;
    }
    
    public static VBox getDoneButton() {
    	return sbDoneBox;
    }
    
    public static VBox getSearchButton() {
    	return sbSearchBox;
    }
    
    public static VBox getHelpButton() {
    	return sbHelpBox;
    }
    
    /* ================================================================================
     * Getters for other components to access and modify the current view
     * ================================================================================
     */
    
    public static View getCurrentView() {
    	return currentView;
    }
    
    public static void setCurrentView(View newView) {
    	currentView = newView;
    }
}
