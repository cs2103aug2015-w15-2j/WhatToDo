package gui;

import backend.Logic;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.nio.file.FileSystemException;
import java.util.ArrayList;

public class InterfaceController {

    /* ================================================================================
     * JavaFX controls used in the general interface
     * ================================================================================
     */

    // Used for initFilePathBar
    private static HBox filepathBox;
    private static VBox filepathBoxWithLine;
    private static Label filepathLabel;
    private static Region leftSpace, rightSpace;
    private static Line filepathLine;

    // Used for initSideBarHomeButton
    private static VBox sbHomeBox;
    private static ImageView sbHomeImage;
    
    // Used for initSideBarAllButton
    private static VBox sbAllBox;
    private static ImageView sbAllImage;
    
    // Used for initSideBarHistoryButton
    private static VBox sbHistoryBox;
    private static ImageView sbHistoryImage;
    
    // Used for initSideBarDoneButton
    private static VBox sbDoneBox;
    private static ImageView sbDoneImage;

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
    
    // Used for initDefTaskView
    private static VBox defTaskBox, defTaskContentBox;
    private static HBox defTaskHeaderBox;
    private static ScrollPane defTaskScroll;

    // Used for initDefEventView
    private static VBox defEventBox, defEventContentBox;
    private static HBox defEventHeaderBox;
    private static ScrollPane defEventScroll;

    // Used for initDefView
    private static HBox defBox, tempBox;
    private static Line defScrollLine;

    // Used for initMainInterface
    private static Scene mainScene;
    private static VBox mainBox;
    private static Line defLine;

    private static LogicController logicControl;

    // Dimension variables used for sizing JavaFX components
    protected static final double WIDTH_DEFAULT = 100;
    protected static final double WIDTH_DEFAULT_BUTTON = 50;
    protected static final double WIDTH_SIDEBAR = 71;
    
    protected static final double HEIGHT_FILEPATH = 36;
    protected static final double HEIGHT_FEEDBACK = 36;
    protected static final double HEIGHT_TEXTFIELD = 45;
    
    protected static final double WIDTH_VERT_LINE = 1;
    protected static final double HEIGHT_HORIZ_LINE = 1;
    
    protected static final double MARGIN_TEXT_BAR = 20;
    protected static final double MARGIN_TEXT_ELEMENT = 10;
    protected static final double MARGIN_TEXT_ELEMENT_HEIGHT = 3;
    protected static final double MARGIN_TEXT_ELEMENT_SEPARATOR = 10;
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

    private static void initSideBarHomeButton(String imagePath) {

        sbHomeImage = new ImageView(imagePath);
        sbHomeBox = new VBox(sbHomeImage);

        // Fix width and height for the button
        sbHomeBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
        sbHomeBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

        sbHomeBox.setMaxHeight(WIDTH_SIDEBAR);
        sbHomeBox.setMinHeight(WIDTH_SIDEBAR);
    }
    
    private static void initSideBarAllButton(String imagePath) {

    	sbAllImage = new ImageView(imagePath);
    	sbAllBox = new VBox(sbAllImage);

    	// Fix width and height for the button
    	sbAllBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
    	sbAllBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

    	sbAllBox.setMaxHeight(WIDTH_SIDEBAR);
    	sbAllBox.setMinHeight(WIDTH_SIDEBAR);
    }

    private static void initSideBarHistoryButton(String imagePath) {
    	
    	sbHistoryImage = new ImageView(imagePath);
    	sbHistoryBox = new VBox(sbHistoryImage);
    	
    	// Fix width and height for the button
    	sbHistoryBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
    	sbHistoryBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

    	sbHistoryBox.setMaxHeight(WIDTH_SIDEBAR);
    	sbHistoryBox.setMinHeight(WIDTH_SIDEBAR);
    }
    
    private static void initSideBarDoneButton(String imagePath) {

    	sbDoneImage = new ImageView(imagePath);
    	sbDoneBox = new VBox(sbDoneImage);

    	// Fix width and height for the button
    	sbDoneBox.setMaxWidth(WIDTH_DEFAULT_BUTTON);
    	sbDoneBox.setMinWidth(WIDTH_DEFAULT_BUTTON);

    	sbDoneBox.setMaxHeight(WIDTH_SIDEBAR);
    	sbDoneBox.setMinHeight(WIDTH_SIDEBAR);
    }

    private static void initSideBar() {

        initSideBarHomeButton("gui/resources/home_selected.png");
        initSideBarAllButton("gui/resources/all.png");
        initSideBarHistoryButton("gui/resources/history.png");
        initSideBarDoneButton("gui/resources/done.png");

        sbBox = new VBox(sbHomeBox, sbAllBox, sbHistoryBox, sbDoneBox);        
        sbLine = new Line(0, 0, 0, WIDTH_DEFAULT_BUTTON);

        sbBoxWithLine = new HBox(sbBox, sbLine);

        // Set margins for the buttons
        VBox.setMargin(sbHomeBox, new Insets(
        		HEIGHT_FILEPATH - HEIGHT_HORIZ_LINE, 
        		MARGIN_COMPONENT, 0, MARGIN_COMPONENT));
        
        VBox.setMargin(sbAllBox, new Insets(0, MARGIN_COMPONENT, 0, MARGIN_COMPONENT));
        VBox.setMargin(sbHistoryBox, new Insets(0, MARGIN_COMPONENT, 0, MARGIN_COMPONENT));
        VBox.setMargin(sbDoneBox, new Insets(0, MARGIN_COMPONENT, 0, MARGIN_COMPONENT));
        
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
        VBox.setMargin(textField, new Insets(MARGIN_COMPONENT));

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
    
    private static boolean isTitle(String displayData) {
    	
    	String firstWord = displayData.split(" ")[0];
    	return firstWord.equals("FLOAT") || firstWord.equals("TODAY") || 
    			firstWord.equals("TOMORROW") || firstWord.equals("ONGOING");
    }
    
    private static HBox initDisplayElement(String displayData) {
    	
    	Label elementLabel = new Label(displayData);
    	//HBox labelBox = new HBox(elementLabel);
    	HBox elementBox = new HBox(elementLabel);
    	
    	// Apply different CSS styles and formatting depending on whether it 
    	// contains a data field or a title field
    	if (isTitle(displayData)) {
    		
    		// Create a divider line and add it to the elementBox
    		Line elementLine = new Line(0, 0, WIDTH_DEFAULT, 0);
    		elementBox.getChildren().add(elementLine);
    		
    		// Get the width of label and resize the line
    		Text text = new Text(elementLabel.getText());
    		Scene s = new Scene(new Group(text));
    		// Override the CSS style to calculate the text width
    		text.setStyle("-fx-font-family: \"PT Sans\"; "
    				+ "-fx-font-size: 14; "
    				+ "-fx-font-weight: bold;");
    		text.applyCss();
    		
    		// Apply the binding to (element box width - text width - arbitrary margin)
    		// The arbitrary margin exists because text in a container is not perfectly 
    		// aligned to the dimensions of its container
    		double textWidth = Math.ceil(text.getLayoutBounds().getWidth());
    		elementLine.endXProperty().bind(elementBox.widthProperty().subtract(textWidth + MARGIN_ARBITRARY));
    		
    		// Align the elements in the HBox
    		elementBox.setAlignment(Pos.CENTER_LEFT);
    		
    		// Set the margins of the element node label within the HBox
        	HBox.setMargin(elementLabel, new Insets(
        			0, MARGIN_TEXT_ELEMENT_HEIGHT, 
        			0, MARGIN_TEXT_ELEMENT_HEIGHT));
        	
        	// Apply CSS style for titles
        	elementLine.getStyleClass().add("line");
    		elementBox.getStyleClass().add("element-title");
    	} else {
    		// Set text wrapping for the display data
        	elementLabel.setWrapText(true);
        	
    		// Set the margins of the element node label within the HBox
        	HBox.setMargin(elementLabel, new Insets(
        			MARGIN_TEXT_ELEMENT_HEIGHT, MARGIN_TEXT_ELEMENT, 
        			MARGIN_TEXT_ELEMENT_HEIGHT, MARGIN_TEXT_ELEMENT));
        	
        	// Apply CSS style for regular data field
    		elementBox.getStyleClass().add("element");
    	}

    	return elementBox;
    }

    private static void initDefTaskView(ArrayList<String> tasks) {

    	Label defTaskHeader = new Label("UPCOMING TASKS");
        defTaskHeaderBox = new HBox(defTaskHeader);
        defTaskHeaderBox.setAlignment(Pos.CENTER_LEFT);

        defTaskContentBox = new VBox();
        
        // Run the loop through the entire task list
        for (int i = 0; i < tasks.size(); i++) {
        	// Use a temporary component for formatting
        	tempBox = initDisplayElement(tasks.get(i));
        	VBox.setMargin(tempBox, new Insets(0, 0, MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            defTaskContentBox.getChildren().add(tempBox);
        }
        
        defTaskScroll = new ScrollPane(defTaskContentBox);
        defTaskScroll.setFitToWidth(true);
        
        defTaskBox = new VBox(defTaskHeaderBox, defTaskScroll);
        
        // Set margins for the header label
        HBox.setMargin(defTaskHeader, new Insets(
        		MARGIN_TEXT_ELEMENT_HEIGHT, 0, MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        defTaskHeaderBox.setAlignment(Pos.CENTER);
        
        // Set margins for the header
        VBox.setMargin(defTaskHeaderBox, new Insets(0, MARGIN_SCROLL, 0, MARGIN_SCROLL));
        
        // Set margins for the scroll pane
        VBox.setMargin(defTaskScroll, new Insets(
        		MARGIN_COMPONENT, MARGIN_SCROLL, 
        		0, MARGIN_SCROLL));
        
        // Set the alignment of the header image to be in the center
        defTaskBox.setAlignment(Pos.CENTER);
        
        // Set the height of the scroll pane to grow with window height
        VBox.setVgrow(defTaskScroll, Priority.ALWAYS);
        
        // Set the scrollbar policy of the scroll pane
        defTaskScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // CSS
        defTaskHeader.getStyleClass().add("box-title-label");
        defTaskHeaderBox.getStyleClass().add("box-title");
    }

    private static void initDefEventView(ArrayList<String> events) {

    	Label defEventHeader = new Label("UPCOMING EVENTS");
        defEventHeaderBox = new HBox(defEventHeader);

        defEventContentBox = new VBox();
        
        // Run the loop through the entire task list
        for (int i = 0; i < events.size(); i++) {
        	// Use a temporary component for formatting
        	tempBox = initDisplayElement(events.get(i));
        	VBox.setMargin(tempBox, new Insets(0, 0, MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            defEventContentBox.getChildren().add(tempBox);
        }
        
        
        defEventScroll = new ScrollPane(defEventContentBox);
        defEventScroll.setFitToWidth(true);
        
        defEventBox = new VBox(defEventHeaderBox, defEventScroll);
        
        // Set margins for the header label
        HBox.setMargin(defEventHeader, new Insets(
        		MARGIN_TEXT_ELEMENT_HEIGHT, 0, MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        defEventHeaderBox.setAlignment(Pos.CENTER);
        
        // Set margins for the header
        VBox.setMargin(defEventHeaderBox, new Insets(0, MARGIN_SCROLL, 0, MARGIN_SCROLL));
        
        // Set margins for the scroll pane
        VBox.setMargin(defEventScroll, new Insets(
        		MARGIN_COMPONENT, MARGIN_SCROLL, 
        		0, MARGIN_SCROLL));
        
        // Set the alignment of the header image to be in the center
        defEventBox.setAlignment(Pos.CENTER);

        // Set the height of the scroll pane to grow with the window height
        VBox.setVgrow(defEventScroll, Priority.ALWAYS);
        
        // Set the scrollbar policy of the scroll pane
        defEventScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // CSS
        defEventHeader.getStyleClass().add("box-title-label");
        defEventHeaderBox.getStyleClass().add("box-title");
    }

    private static void initDefView() {

        initDefTaskView(logicControl.getDefTasks());
        initDefEventView(logicControl.getDefEvents());
        
        defScrollLine = new Line(0, 0, 0, WIDTH_DEFAULT_BUTTON);
        
        defBox = new HBox(defTaskBox, defScrollLine, defEventBox);
        
        // Set the preferred viewport width of the two scroll panes to be half
        // of the entire view pane
        defTaskScroll.prefViewportWidthProperty().bind(defBox.widthProperty().divide(2));
        defEventScroll.prefViewportWidthProperty().bind(defBox.widthProperty().divide(2));
        
        // CSS
        defScrollLine.getStyleClass().add("line");
    }
    
    public static void updateDefView() {
    	
    	// Clear the previous content already displayed
        defTaskContentBox.getChildren().clear();
        defEventContentBox.getChildren().clear();
        
        // Get the results of the file from logic
        ArrayList<String> tasks = logicControl.getDefTasks();
        ArrayList<String> events = logicControl.getDefEvents();
        
        // Run the loop through the entire task list
        for (int i = 0; i < tasks.size(); i++) {
        	// Use a temporary component for formatting
        	tempBox = initDisplayElement(tasks.get(i));
        	VBox.setMargin(tempBox, new Insets(0, 0, MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            defTaskContentBox.getChildren().add(tempBox);
        }
        
        // Run the loop through the entire task list
        for (int i = 0; i < events.size(); i++) {
        	// Use a temporary component for formatting
        	tempBox = initDisplayElement(events.get(i));
        	VBox.setMargin(tempBox, new Insets(0, 0, MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            defEventContentBox.getChildren().add(tempBox);
        }
    }

    public static void initMainInterface() {
    	
    	// Initialize a LogicController
        logicControl = new LogicController();

        initFilePathBar();
        initSideBar();
        initDefView();
        initFeedbackBar();
        initTextField();
        
        // Create the line separator for defBox
        defLine = new Line(0, 0, WIDTH_DEFAULT, 0);

        // Create the region below the filepath bar excluding the sidebar
        VBox contentBoxNoSideBar = new VBox(
        		filepathBoxWithLine, 
        		defBox, 
        		defLine, 
        		feedbackBoxWithLine, 
        		textBox);
        
        // Set the height of defBox to grow with the window
        VBox.setVgrow(defBox, Priority.ALWAYS);

        // Create the region below the filepath bar including the sidebar
        HBox contentBoxWithSideBar = new HBox(sbBoxWithLine, contentBoxNoSideBar);

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
        defLine.getStyleClass().add("line");
        mainScene.getStylesheets().add(InterfaceController.class.getResource(
                "/gui/stylesheets/Interface.css").toExternalForm());

        // Set the scene in MainApp
        MainApp.defaultView = mainScene;
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
    
    // Return lines so as to dynamically adjust line height and width within the
    // ChangeListener
    public static Line getSbLine() {
    	return sbLine;
    }
    
    public static Line getDefScrollLine() {
    	return defScrollLine;
    }
    
    public static Line getFeedbackLine() {
    	return feedbackLine;
    }
    
    public static Line getDefLine() {
    	return defLine;
    }
    
    public static Line getFilepathLine() {
    	return filepathLine;
    }
}
