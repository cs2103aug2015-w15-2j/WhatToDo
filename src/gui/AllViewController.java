package gui;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class AllViewController {

	/* ================================================================================
     * JavaFX controls used in the general interface
     * ================================================================================
     */
	
	// Used for initAllTaskView
    private static VBox allTaskBox, allTaskContentBox;
    private static HBox allTaskHeaderBox;
    private static ScrollPane allTaskScroll;

    // Used for initAllEventView
    private static VBox allEventBox, allEventContentBox;
    private static HBox allEventHeaderBox;
    private static ScrollPane allEventScroll;

    // Used for initDefView
    private static Line allScrollLine;
    
    protected static final String HEADER_ALL_TASKS = "UPCOMING TASKS: ALL";
    protected static final String HEADER_ALL_EVENTS = "UPCOMING EVENTS: ALL";
    
    private static boolean isTitle(String displayData) {
    	
    	// TODO: replace this with whatever format is required
    	String firstWord = displayData.split(" ")[0];
    	return firstWord.equals("FLOAT") || firstWord.equals("TODAY") || 
    			firstWord.equals("TOMORROW") || firstWord.equals("ONGOING");
    }
    
    private static HBox initDisplayElement(String displayData) {
    	
    	Label elementLabel = new Label(displayData);
    	HBox elementBox = new HBox(elementLabel);
    	
    	// Apply different CSS styles and formatting depending on whether it 
    	// contains a data field or a title field
    	if (isTitle(displayData)) {
    		
    		// Create a divider line and add it to the elementBox
    		Line elementLine = new Line(0, 0, InterfaceController.WIDTH_DEFAULT, 0);
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
    		elementLine.endXProperty().bind(elementBox.widthProperty().subtract(textWidth + InterfaceController.MARGIN_ARBITRARY));
    		
    		// Align the elements in the HBox
    		elementBox.setAlignment(Pos.CENTER_LEFT);
    		
    		// Set the margins of the element node label within the HBox
        	HBox.setMargin(elementLabel, new Insets(
        			0, InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
        			0, InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT));
        	
        	// Apply CSS style for titles
        	elementLine.getStyleClass().add("line");
    		elementBox.getStyleClass().add("element-title");
    	} else {
    		// Set text wrapping for the display data
        	elementLabel.setWrapText(true);
        	
    		// Set the margins of the element node label within the HBox
        	HBox.setMargin(elementLabel, new Insets(
        			InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
        			InterfaceController.MARGIN_TEXT_ELEMENT, 
        			InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
        			InterfaceController.MARGIN_TEXT_ELEMENT));
        	
        	// Apply CSS style for regular data field
    		elementBox.getStyleClass().add("element");
    	}

    	return elementBox;
    }

    private static void initAllTaskView(ArrayList<String> tasks) {

    	Label defTaskHeader = new Label(HEADER_ALL_TASKS);
        allTaskHeaderBox = new HBox(defTaskHeader);
        allTaskHeaderBox.setAlignment(Pos.CENTER_LEFT);

        allTaskContentBox = new VBox();
        
        // Run the loop through the entire task list
        for (int i = 0; i < tasks.size(); i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = initDisplayElement(tasks.get(i));
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            allTaskContentBox.getChildren().add(tempBox);
        }
        
        allTaskScroll = new ScrollPane(allTaskContentBox);
        allTaskScroll.setFitToWidth(true);
        
        allTaskBox = new VBox(allTaskHeaderBox, allTaskScroll);
        
        // Set margins for the header label
        HBox.setMargin(defTaskHeader, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        allTaskHeaderBox.setAlignment(Pos.CENTER);
        
        // Set margins for the header
        VBox.setMargin(allTaskHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        // Set margins for the scroll pane
        VBox.setMargin(allTaskScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));
        
        // Set the alignment of the header image to be in the center
        allTaskBox.setAlignment(Pos.CENTER);
        
        // Set the height of the scroll pane to grow with window height
        VBox.setVgrow(allTaskScroll, Priority.ALWAYS);
        
        // Set the scrollbar policy of the scroll pane
        allTaskScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // CSS
        defTaskHeader.getStyleClass().add("box-title-label");
        allTaskHeaderBox.getStyleClass().add("box-title-all-task");
    }

    private static void initAllEventView(ArrayList<String> events) {

    	Label defEventHeader = new Label(HEADER_ALL_EVENTS);
        allEventHeaderBox = new HBox(defEventHeader);

        allEventContentBox = new VBox();
        
        // Run the loop through the entire task list
        for (int i = 0; i < events.size(); i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = initDisplayElement(events.get(i));
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            allEventContentBox.getChildren().add(tempBox);
        }
        
        
        allEventScroll = new ScrollPane(allEventContentBox);
        allEventScroll.setFitToWidth(true);
        
        allEventBox = new VBox(allEventHeaderBox, allEventScroll);
        
        // Set margins for the header label
        HBox.setMargin(defEventHeader, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        allEventHeaderBox.setAlignment(Pos.CENTER);
        
        // Set margins for the header
        VBox.setMargin(allEventHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        // Set margins for the scroll pane
        VBox.setMargin(allEventScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));
        
        // Set the alignment of the header image to be in the center
        allEventBox.setAlignment(Pos.CENTER);

        // Set the height of the scroll pane to grow with the window height
        VBox.setVgrow(allEventScroll, Priority.ALWAYS);
        
        // Set the scrollbar policy of the scroll pane
        allEventScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // CSS
        defEventHeader.getStyleClass().add("box-title-label");
        allEventHeaderBox.getStyleClass().add("box-title-all-event");
    }

    public static void initAllView() {

        initAllTaskView(InterfaceController.logicControl.getDefTasks());
        initAllEventView(InterfaceController.logicControl.getDefEvents());
        
        allScrollLine = new Line(0, 0, 0, InterfaceController.WIDTH_DEFAULT_BUTTON);
        
        InterfaceController.allBox = new HBox(allTaskBox, allScrollLine, allEventBox);
        
        // Set the preferred viewport width of the two scroll panes to be half
        // of the entire view pane
        allTaskScroll.prefViewportWidthProperty().bind(
        		InterfaceController.allBox.widthProperty().divide(2));
        allEventScroll.prefViewportWidthProperty().bind(
        		InterfaceController.allBox.widthProperty().divide(2));
        
        // Set the scroll separator to bind with the same line in DefaultViewController
        allScrollLine.endYProperty().bind(DefaultViewController.getDefScrollLine().endYProperty());
        
        // CSS
        allScrollLine.getStyleClass().add("line");
    }
    
    public static void updateAllView() {
    	
    	// Clear the previous content already displayed
        allTaskContentBox.getChildren().clear();
        allEventContentBox.getChildren().clear();
        
        // Get the results of the file from logic
        ArrayList<String> tasks = InterfaceController.logicControl.getDefTasks();
        ArrayList<String> events = InterfaceController.logicControl.getDefEvents();
        
        // Run the loop through the entire task list
        for (int i = 0; i < tasks.size(); i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = initDisplayElement(tasks.get(i));
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            allTaskContentBox.getChildren().add(tempBox);
        }
        
        // Run the loop through the entire task list
        for (int i = 0; i < events.size(); i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = initDisplayElement(events.get(i));
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            allEventContentBox.getChildren().add(tempBox);
        }
    }
    
    /* ================================================================================
     * Getters for LogicController to access required JavaFX components
     * ================================================================================
     */
    
    public static Line getAllScrollLine() {
    	return allScrollLine;
    }
}
