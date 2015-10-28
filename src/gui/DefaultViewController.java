package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class DefaultViewController {

	 /* ================================================================================
     * JavaFX controls used in the general interface
     * ================================================================================
     */
	
	// Used for initDefTaskView
    private static VBox defTaskBox, defTaskContentBox;
    private static HBox defTaskHeaderBox;
    private static ScrollPane defTaskScroll;

    // Used for initDefEventView
    private static VBox defEventBox, defEventContentBox;
    private static HBox defEventHeaderBox;
    private static ScrollPane defEventScroll;

    // Used for initDefView
    private static Line defScrollLine;
    
    protected static final String HEADER_DEF_TASKS = "UPCOMING TASKS: SOON";
    protected static final String HEADER_DEF_EVENTS = "UPCOMING EVENTS: SOON";
    
    private static HBox initDisplayElement(String displayData) {
    	// Apply different CSS styles and formatting depending on whether it 
    	// contains a data field or a title field
    	if (LogicController.isTitle(displayData)) {
    		
    		Label elementLabel = new Label(displayData);
        	HBox elementBox = new HBox(elementLabel);
    		
    		// Create a divider line and add it to the elementBox
    		Line elementLine = new Line(0, 0, InterfaceController.WIDTH_DEFAULT, 0);
    		elementBox.getChildren().add(elementLine);
    		
    		// Get the width of label and resize the line
    		Text text = new Text(elementLabel.getText());
    		Scene s = new Scene(new Group(text));
    		// Override the CSS style to calculate the text width
    		text.setStyle("-fx-font-family: \"Myriad Pro\"; "
    				+ "-fx-font-size: 14; "
    				+ "-fx-font-weight: bold;");
    		text.applyCss();
    		
    		// Apply the binding to (element box width - text width - arbitrary margin)
    		// The arbitrary margin exists because text in a container is not perfectly 
    		// aligned to the dimensions of its container
    		double textWidth = Math.ceil(text.getLayoutBounds().getWidth());
    		elementLine.endXProperty().bind(elementBox.widthProperty().subtract(
    				textWidth + InterfaceController.MARGIN_ARBITRARY));
    		
    		// Align the elements in the HBox
    		elementBox.setAlignment(Pos.CENTER_LEFT);
    		
    		// Set the margins of the element node label within the HBox
        	HBox.setMargin(elementLabel, new Insets(
        			0, InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
        			0, InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT));
        	
        	// Apply CSS style for titles
        	elementLine.getStyleClass().add("line");
    		elementBox.getStyleClass().add("element-title");
    		
    		return elementBox;
    		
    	} else {
    		// Determine whether the element data is an element or a null response
    		String[] splitDisplayData = displayData.split(Pattern.quote("."));
    		
    		Label elementLabel, elementIndex;
    		HBox elementBox;

    		// For a null response (There are no items to display)
    		if (splitDisplayData.length == 1) {
    			
    			elementLabel = new Label(displayData);
    			elementBox = new HBox(elementLabel);
    			
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

    			return elementBox;
    		} else {
    			
    			elementIndex = new Label(splitDisplayData[0]);
    			elementLabel = new Label(displayData.replaceFirst(splitDisplayData[0] + ".", ""));
    			HBox indexBox = new HBox(elementIndex);
    			indexBox.setAlignment(Pos.CENTER);
    			elementBox = new HBox(indexBox, elementLabel);
    			
    			// Set text wrapping for the display data
    			elementLabel.setWrapText(true);

    			// Set the margins of the element index label within the HBox
    			HBox.setMargin(elementIndex, new Insets(
    					InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
    					InterfaceController.MARGIN_TEXT_ELEMENT, 
    					InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
    					InterfaceController.MARGIN_TEXT_ELEMENT));
    			
    			// Set the margins of the element node label within the HBox
    			HBox.setMargin(elementLabel, new Insets(
    					InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
    					InterfaceController.MARGIN_TEXT_ELEMENT, 
    					InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
    					InterfaceController.MARGIN_TEXT_ELEMENT));
    			
    			// Apply CSS style for regular data field
    			elementBox.getStyleClass().add("element");
    			indexBox.getStyleClass().add("element-index");
    			elementIndex.getStyleClass().add("element-index-label");
    			
    			return elementBox;
    		}
    	}
    }

    private static void initDefTaskView(ArrayList<String> tasks) {

    	Label defTaskHeader = new Label(HEADER_DEF_TASKS);
        defTaskHeaderBox = new HBox(defTaskHeader);
        defTaskHeaderBox.setAlignment(Pos.CENTER);

        defTaskContentBox = new VBox();
        
        // Run the loop through the entire task list
        for (int i = 0; i < tasks.size(); i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = initDisplayElement(tasks.get(i));
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            defTaskContentBox.getChildren().add(tempBox);
        }
        
        defTaskScroll = new ScrollPane(defTaskContentBox);
        defTaskScroll.setFitToWidth(true);
        
        defTaskBox = new VBox(defTaskHeaderBox, defTaskScroll);
        
        // Set margins for the header label
        HBox.setMargin(defTaskHeader, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        // Set margins for the header
        VBox.setMargin(defTaskHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        // Set margins for the scroll pane
        VBox.setMargin(defTaskScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));
        
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

    	Label defEventHeader = new Label(HEADER_DEF_EVENTS);
        defEventHeaderBox = new HBox(defEventHeader);
        defEventHeaderBox.setAlignment(Pos.CENTER);

        defEventContentBox = new VBox();
        
        // Run the loop through the entire task list
        for (int i = 0; i < events.size(); i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = initDisplayElement(events.get(i));
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            defEventContentBox.getChildren().add(tempBox);
        }
        
        
        defEventScroll = new ScrollPane(defEventContentBox);
        defEventScroll.setFitToWidth(true);
        
        defEventBox = new VBox(defEventHeaderBox, defEventScroll);
        
        // Set margins for the header label
        HBox.setMargin(defEventHeader, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        // Set margins for the header
        VBox.setMargin(defEventHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        // Set margins for the scroll pane
        VBox.setMargin(defEventScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));
        
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

    public static void initDefView() {

        initDefTaskView(InterfaceController.logicControl.getDefTasks());
        initDefEventView(InterfaceController.logicControl.getDefEvents());
        
        defScrollLine = new Line(0, 0, 0, InterfaceController.WIDTH_DEFAULT_BUTTON);
        
        InterfaceController.defBox = new HBox(defTaskBox, defScrollLine, defEventBox);
        
        // Set the preferred viewport width of the two scroll panes to be half
        // of the entire view pane
        defTaskScroll.prefViewportWidthProperty().bind(
        		InterfaceController.defBox.widthProperty().divide(2));
        defEventScroll.prefViewportWidthProperty().bind(
        		InterfaceController.defBox.widthProperty().divide(2));
        
        // CSS
        defScrollLine.getStyleClass().add("line");
    }
    
    public static void updateDefView() {
    	
    	// Clear the previous content already displayed
        defTaskContentBox.getChildren().clear();
        defEventContentBox.getChildren().clear();
        
        // Get the results of the file from logic
        ArrayList<String> tasks = InterfaceController.logicControl.getDefTasks();
        ArrayList<String> events = InterfaceController.logicControl.getDefEvents();
        
        // Run the loop through the entire task list
        for (int i = 0; i < tasks.size(); i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = initDisplayElement(tasks.get(i));
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            defTaskContentBox.getChildren().add(tempBox);
        }
        
        // Run the loop through the entire task list
        for (int i = 0; i < events.size(); i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = initDisplayElement(events.get(i));
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            defEventContentBox.getChildren().add(tempBox);
        }
    }
    
    /* ================================================================================
     * Getters for LogicController to access required JavaFX components
     * ================================================================================
     */
    
    public static Line getDefScrollLine() {
    	return defScrollLine;
    }
}
