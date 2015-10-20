package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;

import java.nio.file.FileSystemException;

public class InterfaceController {

    /* ================================================================================
     * JavaFX controls used in the general interface
     * ================================================================================
     */

    // Used for initFilePathBar
    private static HBox filepathBox;
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
    
    // Used for initSideBarDoneButton
    private static VBox sbDoneBox;
    private static ImageView sbDoneImage;
    
    // Used for initSideBarSearchButton
    private static VBox sbSearchBox;
    private static ImageView sbSearchImage;

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
    protected static HBox defBox, allBox, histBox, doneBox, searchBox;

    protected static String currentView;
    protected static LogicController logicControl;
    
    // Values for currentView
    protected static final String VIEW_DEFAULT = "def";
    protected static final String VIEW_ALL = "all";
    protected static final String VIEW_HIST = "hist";
    protected static final String VIEW_DONE = "done";
    protected static final String VIEW_SEARCH = "search";
    
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
    protected static final String PATH_DONE = "gui/resources/done.png";
    protected static final String PATH_DONE_SELECTED = "gui/resources/done_selected.png";
    protected static final String PATH_DONE_HOVER = "gui/resources/done_hover.png";
    protected static final String PATH_SEARCH = "gui/resources/search.png";
    protected static final String PATH_SEARCH_SELECTED = "gui/resources/search_selected.png";
    protected static final String PATH_SEARCH_HOVER = "gui/resources/search_hover.png";
    
    // Dimension variables used for sizing JavaFX components
    protected static final double WIDTH_DEFAULT = 100;
    protected static final double WIDTH_DEFAULT_BUTTON = 50;
    protected static final double WIDTH_SIDEBAR = 71;
    
    protected static final double HEIGHT_FILEPATH = 31;
    protected static final double HEIGHT_FEEDBACK = 31;
    protected static final double HEIGHT_TEXTFIELD = 40;
    
    protected static final double WIDTH_VERT_LINE = 1;
    protected static final double HEIGHT_HORIZ_LINE = 1;
    
    protected static final double MARGIN_TEXT_BAR = 20;
    protected static final double MARGIN_TEXT_ELEMENT = 10;
    protected static final double MARGIN_TEXT_ELEMENT_HEIGHT = 3;
    protected static final double MARGIN_TEXT_ELEMENT_SEPARATOR = 10;
    protected static final double MARGIN_TEXT_FIELD = 7;
    protected static final double MARGIN_COMPONENT = 10;
    protected static final double MARGIN_SCROLL = 30;
    protected static final double MARGIN_ARBITRARY = 6;

    private static void initFilePathBar() {

        filepathLabel = new Label(logicControl.getFilePath());
        filepathLine = new Line(0, 0, WIDTH_DEFAULT, 0);

        filepathBox = new HBox(filepathLabel);
        filepathBoxWithLine = new VBox(filepathBox, filepathLine);

        // Set margins for the filepath label
        HBox.setMargin(filepathLabel, new Insets(
        		0, MARGIN_TEXT_BAR, 
        		0, MARGIN_TEXT_BAR));
        
        // Set alignment of the filepath label
        filepathBox.setAlignment(Pos.CENTER);

        // Fix height for the filepath bar without lines
        filepathBox.setMaxHeight(HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE);
        filepathBox.setMinHeight(HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE);

        // Fix height for the filepath bar
        // +1 for line widths
        filepathBoxWithLine.setMaxHeight(HEIGHT_FILEPATH);
        filepathBoxWithLine.setMinHeight(HEIGHT_FILEPATH);

        // CSS
        filepathLine.getStyleClass().add("line");
        filepathBox.getStyleClass().add("display-bar");
        filepathBox.getStyleClass().add("gradient-regular");
    }

    private static void initSideBarDefButton() {

        sbDefImage = new ImageView(PATH_DEFAULT);
        sbDefBox = new VBox(sbDefImage);

        // Fix width and height for the button
        sbDefBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
        sbDefBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

        sbDefBox.setMaxHeight(WIDTH_SIDEBAR);
        sbDefBox.setMinHeight(WIDTH_SIDEBAR);
        
        // Set listener for mouse interactions
        sbDefBox.addEventHandler(
        		MouseEvent.MOUSE_ENTERED, 
        		logicControl.getButtonHoverHandler(VIEW_DEFAULT));
        sbDefBox.addEventHandler(
        		MouseEvent.MOUSE_EXITED, 
        		logicControl.getButtonHoverHandler(VIEW_DEFAULT));
    }
    
    private static void initSideBarAllButton() {

    	sbAllImage = new ImageView(PATH_ALL);
    	sbAllBox = new VBox(sbAllImage);

    	// Fix width and height for the button
    	sbAllBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
    	sbAllBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

    	sbAllBox.setMaxHeight(WIDTH_SIDEBAR);
    	sbAllBox.setMinHeight(WIDTH_SIDEBAR);
    	
        // Set listener for mouse interactions
        sbAllBox.addEventHandler(
        		MouseEvent.MOUSE_ENTERED, 
        		logicControl.getButtonHoverHandler(VIEW_ALL));
        sbAllBox.addEventHandler(
        		MouseEvent.MOUSE_EXITED, 
        		logicControl.getButtonHoverHandler(VIEW_ALL));
    }

    private static void initSideBarHistoryButton() {
    	
    	sbHistImage = new ImageView(PATH_HIST);
    	sbHistBox = new VBox(sbHistImage);
    	
    	// Fix width and height for the button
    	sbHistBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
    	sbHistBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

    	sbHistBox.setMaxHeight(WIDTH_SIDEBAR);
    	sbHistBox.setMinHeight(WIDTH_SIDEBAR);
    	
        // Set listener for mouse interactions
    	sbHistBox.addEventHandler(
        		MouseEvent.MOUSE_ENTERED, 
        		logicControl.getButtonHoverHandler(VIEW_HIST));
    	sbHistBox.addEventHandler(
        		MouseEvent.MOUSE_EXITED, 
        		logicControl.getButtonHoverHandler(VIEW_HIST));
    }
    
    private static void initSideBarDoneButton() {

    	sbDoneImage = new ImageView(PATH_DONE);
    	sbDoneBox = new VBox(sbDoneImage);

    	// Fix width and height for the button
    	sbDoneBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
    	sbDoneBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

    	sbDoneBox.setMaxHeight(WIDTH_SIDEBAR);
    	sbDoneBox.setMinHeight(WIDTH_SIDEBAR);
    	
        // Set listener for mouse interactions
    	sbDoneBox.addEventHandler(
        		MouseEvent.MOUSE_ENTERED, 
        		logicControl.getButtonHoverHandler(VIEW_DONE));
    	sbDoneBox.addEventHandler(
        		MouseEvent.MOUSE_EXITED, 
        		logicControl.getButtonHoverHandler(VIEW_DONE));
    }
    
    private static void initSideBarSearchButton() {
    	
    	sbSearchImage = new ImageView(PATH_SEARCH);
    	sbSearchBox = new VBox(sbSearchImage);

    	// Fix width and height for the button
    	sbSearchBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
    	sbSearchBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

    	sbSearchBox.setMaxHeight(WIDTH_SIDEBAR);
    	sbSearchBox.setMinHeight(WIDTH_SIDEBAR);
    	
        // Set listener for mouse interactions
    	sbSearchBox.addEventHandler(
        		MouseEvent.MOUSE_ENTERED, 
        		logicControl.getButtonHoverHandler(VIEW_SEARCH));
    	sbSearchBox.addEventHandler(
        		MouseEvent.MOUSE_EXITED, 
        		logicControl.getButtonHoverHandler(VIEW_SEARCH));
    }
    
    private static void changeButtonToSelected(String view) {
    	switch (view) {
    	case VIEW_DEFAULT:
    		sbDefImage = new ImageView(PATH_DEFAULT_SELECTED);
    		sbDefBox.getChildren().clear();
    		sbDefBox.getChildren().add(sbDefImage);
    		break;
    	case VIEW_ALL:
    		sbAllImage = new ImageView(PATH_ALL_SELECTED);
    		sbAllBox.getChildren().clear();
    		sbAllBox.getChildren().add(sbAllImage);
    		break;
    	case VIEW_HIST:
    		sbHistImage = new ImageView(PATH_HIST_SELECTED);
    		sbHistBox.getChildren().clear();
    		sbHistBox.getChildren().add(sbHistImage);
    		break;
    	case VIEW_DONE:
    		sbDoneImage = new ImageView(PATH_DONE_SELECTED);
    		sbDoneBox.getChildren().clear();
    		sbDoneBox.getChildren().add(sbDoneImage);
    		break;
    	case VIEW_SEARCH:
    		sbSearchImage = new ImageView(PATH_SEARCH_SELECTED);
    		sbSearchBox.getChildren().clear();
    		sbSearchBox.getChildren().add(sbSearchImage);
    	default:
    		// Do nothing
    		break;
    	}
    }
    
    private static void changeButtonToUnselected(String view) {
    	switch (view) {
    	case VIEW_DEFAULT:
    		sbDefImage = new ImageView(PATH_DEFAULT);
    		sbDefBox.getChildren().clear();
    		sbDefBox.getChildren().add(sbDefImage);
    		break;
    	case VIEW_ALL:
    		sbAllImage = new ImageView(PATH_ALL);
    		sbAllBox.getChildren().clear();
    		sbAllBox.getChildren().add(sbAllImage);
    		break;
    	case VIEW_HIST:
    		sbHistImage = new ImageView(PATH_HIST);
    		sbHistBox.getChildren().clear();
    		sbHistBox.getChildren().add(sbHistImage);
    		break;
    	case VIEW_DONE:
    		sbDoneImage = new ImageView(PATH_DONE);
    		sbDoneBox.getChildren().clear();
    		sbDoneBox.getChildren().add(sbDoneImage);
    		break;
    	case VIEW_SEARCH:
    		sbSearchImage = new ImageView(PATH_SEARCH);
    		sbSearchBox.getChildren().clear();
    		sbSearchBox.getChildren().add(sbSearchImage);
    	default:
    		// Do nothing
    		break;
    	}
    }

    private static void initSideBar() {

        initSideBarDefButton();
        initSideBarAllButton();
        initSideBarHistoryButton();
        initSideBarDoneButton();
        initSideBarSearchButton();
        
        changeButtonToSelected(VIEW_DEFAULT);

        sbBox = new VBox(sbDefBox, sbAllBox, sbHistBox, sbDoneBox, sbSearchBox);        
        sbLine = new Line(0, 0, 0, WIDTH_DEFAULT_BUTTON);

        sbBoxWithLine = new HBox(sbBox, sbLine);

        // Set margins for the buttons
        VBox.setMargin(sbDefBox, new Insets(
        		HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE, 
        		MARGIN_COMPONENT, 0, MARGIN_COMPONENT));
        
        VBox.setMargin(sbAllBox, new Insets(0, MARGIN_COMPONENT, 0, MARGIN_COMPONENT));
        VBox.setMargin(sbHistBox, new Insets(0, MARGIN_COMPONENT, 0, MARGIN_COMPONENT));
        VBox.setMargin(sbDoneBox, new Insets(0, MARGIN_COMPONENT, 0, MARGIN_COMPONENT));
        VBox.setMargin(sbSearchBox, new Insets(0, MARGIN_COMPONENT, 0, MARGIN_COMPONENT));
        
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
        textField.addEventFilter(KeyEvent.KEY_PRESSED, logicControl.getUpKeyHandler());
        textField.addEventFilter(KeyEvent.KEY_PRESSED, logicControl.getDownKeyHandler());
        
        textBox = new VBox(textField);

        // Set the margins for the text field
        VBox.setMargin(textField, new Insets(MARGIN_TEXT_FIELD));

        // Fix the height of the text field
        textBox.setMaxHeight(HEIGHT_TEXTFIELD);
        textBox.setMinHeight(HEIGHT_TEXTFIELD);

        // CSS
        textBox.getStyleClass().add("gradient-regular");
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
    	
    	// Initial view is defined to be default
    	currentView = VIEW_DEFAULT;
    	
    	// Initialize a LogicController
        logicControl = new LogicController();

        initFilePathBar();
        initSideBar();
        
        // Initialize all the views
        DefaultViewController.initDefView();
        AllViewController.initAllView();
        HistoryViewController.initHistView();
        //DoneViewController.initDoneView();
        //SearchViewController.initSearchView();
        
        // Initial view will be default
        viewBox = new HBox(defBox);
        initFeedbackBar();
        initTextField();
        
        // Set default box to grow with view box
        HBox.setHgrow(defBox, Priority.ALWAYS);
        
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
        MainApp.defaultView = mainScene;
    }
    
    public static void updateMainInterface(String view) {
    	
    	// Clear the old content
    	viewBox.getChildren().clear();
    	
    	switch (view) {
    	
    	case VIEW_DEFAULT:
    		DefaultViewController.updateDefView();
    		viewBox.getChildren().add(defBox);
    		
    		// Set default box to grow with view box
            HBox.setHgrow(defBox, Priority.ALWAYS);
            
            // Change buttons
            changeButtonToSelected(VIEW_DEFAULT);
            changeButtonToUnselected(currentView);
            
            // Update currentView
            currentView = InterfaceController.VIEW_DEFAULT;
    		break;
    		
    	case VIEW_ALL:
    		AllViewController.updateAllView();
    		viewBox.getChildren().add(allBox);
    		
    		// Set all box to grow with view box
            HBox.setHgrow(allBox, Priority.ALWAYS);
            
            // Change buttons
            changeButtonToSelected(VIEW_ALL);
            changeButtonToUnselected(currentView);
            
            // Update currentView
            currentView = InterfaceController.VIEW_ALL;
    		break;
    		
    	case VIEW_HIST:
    		viewBox.getChildren().add(histBox);
    		
    		// Set history box to grow with view box
            HBox.setHgrow(histBox, Priority.ALWAYS);
    		
            // Change buttons
            changeButtonToSelected(VIEW_HIST);
            changeButtonToUnselected(currentView);
            
            // Update currentView
            currentView = InterfaceController.VIEW_HIST;
    		break;
    		
    	case VIEW_DONE:
    		//DoneViewController.updateDoneView();
    		viewBox.getChildren().add(doneBox);
    		
    		// Set done box to grow with view box
            //HBox.setHgrow(doneBox, Priority.ALWAYS);
    		
            // Change buttons
            changeButtonToSelected(VIEW_DONE);
            changeButtonToUnselected(currentView);
            
            // Update currentView
            currentView = InterfaceController.VIEW_DONE;
    		break;
    		
    	case VIEW_SEARCH:
    		//DoneViewController.updateDoneView();
    		viewBox.getChildren().add(searchBox);
    		
    		// Set done box to grow with view box
            //HBox.setHgrow(doneBox, Priority.ALWAYS);
    		
            // Change buttons
            changeButtonToSelected(VIEW_SEARCH);
            changeButtonToUnselected(currentView);
            
            // Update currentView
            currentView = InterfaceController.VIEW_SEARCH;
    		break;
    		
    	default: //ignore
    		break;
    	}
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
    
    public static VBox getDoneButton() {
    	return sbDoneBox;
    }
    
    public static VBox getSearchButton() {
    	return sbSearchBox;
    }
}
