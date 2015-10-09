package gui;

import backend.Logic;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

import java.nio.file.FileSystemException;
import java.util.ArrayList;

public class InterfaceController {

    /* ================================================================================
     * JavaFX controls used in the general interface
     * ================================================================================
     */

    // Used for design
    private static LinearGradient background, backgroundSelected;

    // Used for initFilePathBar
    private static HBox filepathBox;
    private static VBox filepathBoxWithLine;
    private static Label filepathLabel;
    private static Region leftSpace, rightSpace;
    private static Line filepathLine;

    // Used for initSideBarHomeButton
    private static VBox sbHomeBox;
    private static ImageView sbHomeImage;
    private static Line sbHomeLine;

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
    private static ScrollPane defTaskScroll;
    private static ImageView defTaskImage;

    // Used for initDefEventView
    private static VBox defEventBox, defEventContentBox;
    private static ScrollPane defEventScroll;
    private static ImageView defEventImage;

    // Used for initDefView
    private static HBox defBox, tempBox;
    private static Line defScrollLine;

    // Used for initMainInterface
    private static Scene mainScene;
    private static VBox mainBox;
    private static Line defLine;

    private static LogicController logicControl;

    private static final double DEFAULT_WIDTH = 100;
    private static final double DEFAULT_SIZE_BUTTON = 50;

    /*
    private static void initGradientBG() {

        // Define the start and end colors
        Stop stops[] = new Stop[] {
                new Stop(0, Color.rgb(250, 250, 250)),  // End color (top half)
                new Stop(1, Color.rgb(240, 240, 240))   // Start color (bottom half)
        };

        // Create the gradient
        background = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
    }

    private static void initGradientBGSelect() {

        // Define the start and end colors
        Stop stops[] = new Stop[] {
                new Stop(0, Color.rgb(220, 220, 220)),  // End color (top half)
                new Stop(1, Color.rgb(240, 240, 240))   // Start color (bottom half)
        };

        // Create the gradient
        backgroundSelected = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
    }
    */

    private static void initFilePathBar() {

        filepathLabel = new Label(logicControl.getFilePath());
        filepathLine = new Line(0, 0, DEFAULT_WIDTH, 0);

        filepathBox = new HBox(filepathLabel);
        filepathBoxWithLine = new VBox(filepathBox, filepathLine);

        // Set margins for the filepath label
        HBox.setMargin(filepathLabel, new Insets(8, 71, 0, 71));

        // Fix height for the filepath bar without lines
        filepathBox.setMaxHeight(35);
        filepathBox.setMinHeight(35);

        // Fix height for the filepath bar
        // +1 for line widths
        filepathBoxWithLine.setMaxHeight(36);
        filepathBoxWithLine.setMinHeight(36);

        // CSS
        filepathLine.getStyleClass().add("line");
        filepathBox.getStyleClass().add("display-bar");
        filepathBox.getStyleClass().add("gradient-regular");
    }

    private static void initSideBarHomeButton(String imagePath) {

        sbHomeImage = new ImageView(imagePath);
        sbHomeLine = new Line(0, 0, DEFAULT_SIZE_BUTTON, 0);

        sbHomeBox = new VBox(sbHomeImage, sbHomeLine);

        // Set margins for the home button
        VBox.setMargin(sbHomeImage, new Insets(10));

        // Fix width and height for the button
        // +1 for line width
        sbHomeBox.setMaxWidth(50);
        sbHomeBox.setMinWidth(50);

        sbHomeBox.setMaxHeight(51);
        sbHomeBox.setMinHeight(51);

        // CSS
        sbHomeLine.getStyleClass().add("line");
        sbHomeBox.getStyleClass().add("gradient-selected");
    }

    private static void initSideBar() {

        initSideBarHomeButton("gui/resources/homeButton.png");

        sbBox = new VBox(sbHomeBox);
        sbLine = new Line(0, 0, 0, DEFAULT_SIZE_BUTTON);

        sbBoxWithLine = new HBox(sbBox, sbLine);

        // Fix the width for the sidebar
        // +1 for line width
        sbBoxWithLine.setMaxWidth(51);
        sbBoxWithLine.setMinWidth(51);

        // CSS
        sbLine.getStyleClass().add("line");
        sbBoxWithLine.getStyleClass().add("gradient-sidebar");
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
        VBox.setMargin(textField, new Insets(10));

        // Fix the height of the text field
        textBox.setMaxHeight(45);
        textBox.setMinHeight(45);

        // CSS
        textBox.getStyleClass().add("gradient-regular");
    }

    private static void initFeedbackBar() {

        feedbackLabel = new Label("No commands entered yet.");
        feedbackLine = new Line(0, 0, DEFAULT_WIDTH, 0);

        feedbackBox = new VBox(feedbackLabel);
        feedbackBoxWithLine = new VBox(feedbackBox, feedbackLine);

        // Set margins for the feedback label
        VBox.setMargin(feedbackLabel, new Insets(8, 20, 0, 20));

        // Fix the height of the feedback label
        feedbackBox.setMaxHeight(35);
        feedbackBox.setMinHeight(35);

        // Fix the height of the feedback bar
        // +1 for line width
        feedbackBoxWithLine.setMaxHeight(36);
        feedbackBoxWithLine.setMinHeight(36);

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
    	
    	// Define a containing HBox that will contain a label with the 
    	// formatted data and a background
    	HBox elementBox;
    	
    	Label elementLabel = new Label(displayData);
    	elementBox = new HBox(elementLabel);
    	
    	// Set the text wrap for the label to true
    	elementLabel.setWrapText(true);
    	
    	// Apply different CSS styles and formatting depending on whether it 
    	// contains a data field or a title field
    	if (isTitle(displayData)) {
    		
    		// Set the margins of the element node label within the HBox
        	HBox.setMargin(elementLabel, new Insets(0, 10, 0, 10));
        	
        	// Apply CSS style for titles
    		elementBox.getStyleClass().add("element-title");
    	} else {
    		
    		// Set the margins of the element node label within the HBox
        	HBox.setMargin(elementLabel, new Insets(10));
        	
        	// Apply CSS style for regular data field
    		elementBox.getStyleClass().add("element");
    	}

    	return elementBox;
    }

    private static void initDefTaskView(ArrayList<String> tasks) {

        defTaskImage = new ImageView("gui/resources/taskHeader.png");

        defTaskContentBox = new VBox();
        
        // Run the loop through the entire task list
        for (int i = 0; i < tasks.size(); i++) {
        	// Use a temporary component for formatting
        	tempBox = initDisplayElement(tasks.get(i));
        	VBox.setMargin(tempBox, new Insets(0, 0, 20, 0));
            defTaskContentBox.getChildren().add(tempBox);
        }
        
        defTaskScroll = new ScrollPane(defTaskContentBox);
        defTaskScroll.setFitToWidth(true);
        
        defTaskBox = new VBox(defTaskImage, defTaskScroll);
        
        // Set margins for the image
        VBox.setMargin(defTaskImage, new Insets(0, 0, 10, 0));
        
        // Set margins for the scroll pane
        VBox.setMargin(defTaskScroll, new Insets(10, 30, 0, 30));
        
        // Set the alignment of the header image to be in the center
        defTaskBox.setAlignment(Pos.CENTER);
        
        // Set the height of the scroll pane to grow with window height
        VBox.setVgrow(defTaskScroll, Priority.ALWAYS);
        
        // Set the scrollbar policy of the scroll pane
        defTaskScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // CSS
        defTaskImage.getStyleClass().add("image");
    }

    private static void initDefEventView(ArrayList<String> events) {

        defEventImage = new ImageView("gui/resources/eventHeader.png");

        defEventContentBox = new VBox();
        
        // Run the loop through the entire task list
        for (int i = 0; i < events.size(); i++) {
        	// Use a temporary component for formatting
        	tempBox = initDisplayElement(events.get(i));
        	VBox.setMargin(tempBox, new Insets(0, 0, 20, 0));
            defEventContentBox.getChildren().add(tempBox);
        }
        
        
        defEventScroll = new ScrollPane(defEventContentBox);
        defEventScroll.setFitToWidth(true);
        
        defEventBox = new VBox(defEventImage, defEventScroll);
        
        // Set margins for the image
        VBox.setMargin(defEventImage, new Insets(0, 0, 10, 0));
        
        // Set margins for the scroll pane
        VBox.setMargin(defEventScroll, new Insets(10, 30, 0, 30));
        
        // Set the alignment of the header image to be in the center
        defEventBox.setAlignment(Pos.CENTER);

        // Set the height of the scroll pane to grow with the window height
        VBox.setVgrow(defEventScroll, Priority.ALWAYS);
        
        // Set the scrollbar policy of the scroll pane
        defEventScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // CSS
        defEventImage.getStyleClass().add("image");
    }

    private static void initDefView() {

        initDefTaskView(logicControl.getDefTasks());
        initDefEventView(logicControl.getDefEvents());
        
        defScrollLine = new Line(0, 0, 0, DEFAULT_SIZE_BUTTON);
        
        defBox = new HBox(defTaskBox, defScrollLine, defEventBox);
        
        // Set the preferred viewport width of the two scroll panes to be half
        // of the entire view pane
        defTaskScroll.prefViewportWidthProperty().bind(defBox.widthProperty().divide(2));
        defEventScroll.prefViewportWidthProperty().bind(defBox.widthProperty().divide(2));
        
        // CSS
        defScrollLine.getStyleClass().add("line");
    }
    
    public static void updateView() {
    	
    	initDefView();
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
        defLine = new Line(0, 0, DEFAULT_WIDTH, 0);

        // Create the region below the filepath bar excluding the sidebar
        VBox contentBoxNoSideBar = new VBox(defBox, defLine, feedbackBoxWithLine, textBox);
        
        // Set the height of defBox to grow with the window
        VBox.setVgrow(defBox, Priority.ALWAYS);

        // Create the region below the filepath bar including the sidebar
        HBox contentBoxWithSideBar = new HBox(sbBoxWithLine, contentBoxNoSideBar);

        // Set the width of contentBoxNoSideBar to grow with window
        HBox.setHgrow(contentBoxNoSideBar, Priority.ALWAYS);

        // Create the main VBox containing everything
        mainBox = new VBox(filepathBoxWithLine, contentBoxWithSideBar);

        // Set the height of contentBoxWithSideBar to grow with window
        VBox.setVgrow(contentBoxWithSideBar, Priority.ALWAYS);

        mainScene = new Scene(mainBox);

        // Set resize listeners for the main scene
        mainScene.heightProperty().addListener(logicControl.getHeightListener());
        mainScene.widthProperty().addListener(logicControl.getWidthListener());

        // Set CSS styling
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
