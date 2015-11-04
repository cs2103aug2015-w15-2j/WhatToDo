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

	 // ================================================================================
     // JavaFX controls used in the general interface
     // ================================================================================
	
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
    
    /**
     * This method initializes all the interface components for the default view,
     * primary the task window and the event window
     */
    public static void initDefView() {

        initDefTaskView(InterfaceController.getLogic().getDefTasks());
        initDefEventView(InterfaceController.getLogic().getDefEvents());
        
        defScrollLine = new Line(0, 0, 0, InterfaceController.WIDTH_DEFAULT_BUTTON);
        
        InterfaceController.defBox = new HBox(defTaskBox, defScrollLine, defEventBox);
        
        // Set the preferred viewport width of the two scroll panes to be half
        // of the entire view pane
        defTaskScroll.prefViewportWidthProperty().bind(
        		InterfaceController.defBox.widthProperty().divide(2));
        defEventScroll.prefViewportWidthProperty().bind(
        		InterfaceController.defBox.widthProperty().divide(2));
        
        // Fix the width of the scroll panes to prevent resize of the inner labels
        defTaskScroll.maxWidthProperty().bind(
        		InterfaceController.defBox.widthProperty().divide(2));
        defEventScroll.maxWidthProperty().bind(
        		InterfaceController.defBox.widthProperty().divide(2));
        
        // CSS
        defScrollLine.getStyleClass().add("line");
    }
    
    /**
     * This method updates the default view with data from the text file
     * 
     * Called by:
     * 	1. 	runCommand() in LogicController to update the view every time an 
     * 		operation is performed
     * 	2. 	updateMainInterface() in InterfaceController to update the view when
     * 		a view change command is issued (button/hotkey/text command)
     */
    public static void updateDefView() {
    	
    	// Clear the previous content already displayed
        defTaskContentBox.getChildren().clear();
        defEventContentBox.getChildren().clear();
        ViewIndexMap.resetDefMap();
        
        // Get the results of the file from logic
        String[] tasks = InterfaceController.getLogic().getDefTasks();
        String[] events = InterfaceController.getLogic().getDefEvents();
        
        int numOfElements = InterfaceController.getLogic().getDefElementsCount();
        
        // Run the loop through the entire task list
        int numOfResults = 1;
        for (int i = 0; i < tasks.length; i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = initDisplayElement(tasks[i], numOfElements, numOfResults);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            defTaskContentBox.getChildren().add(tempBox);
			// Only increment the counter if an element is added
			if (InterfaceController.getLogic().isNonEmptyElement(tasks[i])) {
				numOfResults++;
			}
        }
        
        // Run the loop through the entire task list
        for (int i = 0; i < events.length; i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = initDisplayElement(events[i], numOfElements, numOfResults);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            defEventContentBox.getChildren().add(tempBox);
			// Only increment the counter if an element is added
			if (InterfaceController.getLogic().isNonEmptyElement(events[i])) {
				numOfResults++;
			}
        }
    }
    
    // ================================================================================
    // Getters for getLogic() to access required JavaFX components
    // ================================================================================
    
    /**
     * This method returns the line separator between the task and event views 
     * 
     * @return The Line object separating the task and event views.
     */
    public static Line getDefScrollLine() {
    	return defScrollLine;
    }
    
    // ================================================================================
    // Private methods, used to initialize various sub components of the interface
    // ================================================================================
    
    /**
     * This method takes in an input String and creates a HBox with all the required
     * data formatted correctly to be inserted into one of the task/event windows.
     * 
     * @param displayData
     * 		      The task/event data to be displayed in the window
     * @param numOfElements
     * 		      The total number of tasks/events. Used for formatting the index box
     * @param index
     * 		      The index of the particular task/event
     * @return A HBox with all the task/event data formatted for insertion into the 
     * 		   scroll pane
     */
    private static HBox initDisplayElement(String displayData, int numOfElements, int index) {
    	// Apply different CSS styles and formatting depending on whether it 
    	// contains a data field or a title field
    	if (InterfaceController.getLogic().isTitle(displayData)) {
    		
    		Label elementLabel = new Label(displayData.toUpperCase());
        	HBox elementBox = new HBox(elementLabel);
    		
    		// Create a divider line and add it to the elementBox
    		Line elementLine = new Line(0, 0, InterfaceController.WIDTH_DEFAULT, 0);
    		elementBox.getChildren().add(elementLine);
    		
    		// Get the width of label and resize the line
    		Text text = new Text(elementLabel.getText());
    		Scene s = new Scene(new Group(text));
    		// Override the CSS style to calculate the text width
    		text.setStyle("-fx-font-family: \"Myriad Pro\"; "
    				+ "-fx-font-size: 16; "
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
    		String[] displayDataSplit = displayData.split(Pattern.quote("."));
    		
    		Label elementLabel, elementIndex;
    		HBox elementBox;

    		// For a null response (There are no items to display)
    		if (displayDataSplit.length == 1) {
    			
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
    			
    			elementIndex = new Label(String.valueOf(index));
    			elementLabel = new Label(displayData.replaceFirst(displayDataSplit[0] + ".", "").trim());
    			
    			HBox indexBox = new HBox(elementIndex);
    			indexBox.setAlignment(Pos.CENTER);
    			
				// After removing the index, store it in the index map
				ViewIndexMap.addToDefMap(Integer.parseInt(displayDataSplit[0]));
    			
        		// Get the width of label of the largest number in the list
    			// In this case the largest number will be the number of elements in the list
        		Text text = new Text(String.valueOf(numOfElements));
        		Scene s = new Scene(new Group(text));
        		// Override the CSS style to calculate the text width
        		text.setStyle("-fx-font-family: \"Myriad Pro\"; "
        				+ "-fx-font-size: 16; ");
        		text.applyCss();
        		double textWidth = Math.ceil(text.getLayoutBounds().getWidth());
    			indexBox.setMinWidth(textWidth + 2 * InterfaceController.MARGIN_TEXT_ELEMENT);
    			
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

    /**
     * This method initializes the task view for the default view
     * 
     * @param tasks
     * 		      A String[] of tasks returned from LogicController's
     * 			  getDefTasks()
     */
    private static void initDefTaskView(String[] tasks) {

    	Label defTaskHeader = new Label(HEADER_DEF_TASKS);
        defTaskHeaderBox = new HBox(defTaskHeader);
        defTaskHeaderBox.setAlignment(Pos.CENTER);

        defTaskContentBox = new VBox();
        
        // Run the loop through the entire task list
        int numOfElements = InterfaceController.getLogic().getDefElementsCount();
        for (int i = 0; i < tasks.length; i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = initDisplayElement(tasks[i], numOfElements, 1);
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
        defTaskScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // CSS
        defTaskHeader.getStyleClass().add("box-title-label");
        defTaskHeaderBox.getStyleClass().add("box-title");
    }

    /**
     * This method initializes the event view for the default view
     * 
     * @param events
     * 		      A String[] of tasks returned from LogicController's
     * 			  getDefEvents()
     */
    private static void initDefEventView(String[] events) {

    	Label defEventHeader = new Label(HEADER_DEF_EVENTS);
        defEventHeaderBox = new HBox(defEventHeader);
        defEventHeaderBox.setAlignment(Pos.CENTER);

        defEventContentBox = new VBox();
        
        // Run the loop through the entire task list
        int numOfElements = InterfaceController.getLogic().getDefElementsCount();
        for (int i = 0; i < events.length; i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = initDisplayElement(events[i], numOfElements, 1);
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
        defEventScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // CSS
        defEventHeader.getStyleClass().add("box-title-label");
        defEventHeaderBox.getStyleClass().add("box-title");
    }
}
