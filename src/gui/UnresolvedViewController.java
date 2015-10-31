package gui;

import java.util.regex.Pattern;

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

public class UnresolvedViewController {

	/* ================================================================================
     * JavaFX controls used in the general interface
     * ================================================================================
     */
	
	// Used for initUnresTaskView
    private static VBox unresTaskBox, unresTaskContentBox;
    private static HBox unresTaskHeaderBox;
    private static ScrollPane unresTaskScroll;

    // Used for initUnresEventView
    private static VBox unresEventBox, unresEventContentBox;
    private static HBox unresEventHeaderBox;
    private static ScrollPane unresEventScroll;

    // Used for initUnresView
    private static Line unresScrollLine;
    
    protected static final String HEADER_UNRESOLVED_TASKS = "UNRESOLVED TASKS";
    protected static final String HEADER_UNRESOLVED_EVENTS = "UNRESOLVED EVENTS";
    
    private static HBox initDisplayElement(String displayData, int numOfElements, int index, boolean isTask) {
    	// Apply different CSS styles and formatting depending on whether it 
    	// contains a data field or a title field
    	if (InterfaceController.logicControl.isTitleOrDate(displayData)) {
    		
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
				ViewIndexMap.addToUnresMap(Integer.parseInt(displayDataSplit[0]));
				
        		// Get the width of label and resize the line
        		Text text = new Text(String.valueOf(numOfElements));
        		Scene s = new Scene(new Group(text));
        		// Override the CSS style to calculate the text width
        		text.setStyle("-fx-font-family: \"Myriad Pro\"; "
        				+ "-fx-font-size: 14; ");
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
    			elementIndex.getStyleClass().add("element-index-label");
    			
    			if (isTask) {
        			indexBox.getStyleClass().add("element-index-task");
    			} else {
        			indexBox.getStyleClass().add("element-index-event");
    			}
    			
    			return elementBox;
    		}
    	}
    }

    private static void initUnresTaskView(String[] tasks) {

    	Label unresTaskHeader = new Label(HEADER_UNRESOLVED_TASKS);
        unresTaskHeaderBox = new HBox(unresTaskHeader);
        unresTaskHeaderBox.setAlignment(Pos.CENTER);

        unresTaskContentBox = new VBox();
        
    	int numOfElements = InterfaceController.logicControl.getUnresElementsCount();
    	
        // Run the loop through the entire task list
        for (int i = 0; i < tasks.length; i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = initDisplayElement(tasks[i], numOfElements, 1, true);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            unresTaskContentBox.getChildren().add(tempBox);
        }
        
        unresTaskScroll = new ScrollPane(unresTaskContentBox);
        unresTaskScroll.setFitToWidth(true);
        
        unresTaskBox = new VBox(unresTaskHeaderBox, unresTaskScroll);
        
        // Set margins for the header label
        HBox.setMargin(unresTaskHeader, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        // Set margins for the header
        VBox.setMargin(unresTaskHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        // Set margins for the scroll pane
        VBox.setMargin(unresTaskScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));
        
        // Set the alignment of the header image to be in the center
        unresTaskBox.setAlignment(Pos.CENTER);
        
        // Set the height of the scroll pane to grow with window height
        VBox.setVgrow(unresTaskScroll, Priority.ALWAYS);
        
        // Set the scrollbar policy of the scroll pane
        unresTaskScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // CSS
        unresTaskHeader.getStyleClass().add("box-title-label");
        unresTaskHeaderBox.getStyleClass().add("box-title-all-task");
    }

    private static void initUnresEventView(String[] events) {

    	Label unresEventHeader = new Label(HEADER_UNRESOLVED_EVENTS);
        unresEventHeaderBox = new HBox(unresEventHeader);
        unresEventHeaderBox.setAlignment(Pos.CENTER);

        unresEventContentBox = new VBox();
        
    	int numOfElements = InterfaceController.logicControl.getUnresElementsCount();
    	
        // Run the loop through the entire task list
        for (int i = 0; i < events.length; i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = initDisplayElement(events[i], numOfElements, 1, false);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            unresEventContentBox.getChildren().add(tempBox);
        }
        
        
        unresEventScroll = new ScrollPane(unresEventContentBox);
        unresEventScroll.setFitToWidth(true);
        
        unresEventBox = new VBox(unresEventHeaderBox, unresEventScroll);
        
        // Set margins for the header label
        HBox.setMargin(unresEventHeader, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        // Set margins for the header
        VBox.setMargin(unresEventHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        // Set margins for the scroll pane
        VBox.setMargin(unresEventScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));
        
        // Set the alignment of the header image to be in the center
        unresEventBox.setAlignment(Pos.CENTER);

        // Set the height of the scroll pane to grow with the window height
        VBox.setVgrow(unresEventScroll, Priority.ALWAYS);
        
        // Set the scrollbar policy of the scroll pane
        unresEventScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // CSS
        unresEventHeader.getStyleClass().add("box-title-label");
        unresEventHeaderBox.getStyleClass().add("box-title-all-event");
    }

    public static void initUnresView() {

        initUnresTaskView(InterfaceController.logicControl.getUnresTasks());
        initUnresEventView(InterfaceController.logicControl.getUnresEvents());
        
        unresScrollLine = new Line(0, 0, 0, InterfaceController.WIDTH_DEFAULT_BUTTON);
        
        InterfaceController.unresBox = new HBox(unresTaskBox, unresScrollLine, unresEventBox);
        
        // Set the preferred viewport width of the two scroll panes to be half
        // of the entire view pane
        unresTaskScroll.prefViewportWidthProperty().bind(
        		InterfaceController.unresBox.widthProperty().divide(2));
        unresEventScroll.prefViewportWidthProperty().bind(
        		InterfaceController.unresBox.widthProperty().divide(2));
        
        // Fix the width of the scroll panes to prevent resize of the inner labels
        unresTaskScroll.maxWidthProperty().bind(
        		InterfaceController.unresBox.widthProperty().divide(2));
        unresEventScroll.maxWidthProperty().bind(
        		InterfaceController.unresBox.widthProperty().divide(2));
        
        // Set the scroll separator to bind with the same line in DefaultViewController
        unresScrollLine.endYProperty().bind(DefaultViewController.getDefScrollLine().endYProperty());
        
        // CSS
        unresScrollLine.getStyleClass().add("line");
    }
    
    public static void updateUnresView() {
    	
    	// Clear the previous content already displayed
        unresTaskContentBox.getChildren().clear();
        unresEventContentBox.getChildren().clear();
        ViewIndexMap.resetUnresMap();
        
        // Get the results of the file from logic
        String[] tasks = InterfaceController.logicControl.getUnresTasks();
        String[] events = InterfaceController.logicControl.getUnresEvents();
        
    	int numOfElements = InterfaceController.logicControl.getUnresElementsCount();
    	
        // Run the loop through the entire task list
    	int numOfResults = 1;
        for (int i = 0; i < tasks.length; i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = initDisplayElement(tasks[i], numOfElements, numOfResults, true);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            unresTaskContentBox.getChildren().add(tempBox);
			// Only increment the counter if an element is added
			if (!InterfaceController.logicControl.isTitleOrDate(tasks[i])) {
				numOfResults++;
			}
        }
        
        // Run the loop through the entire task list
        for (int i = 0; i < events.length; i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = initDisplayElement(events[i], numOfElements, numOfResults, false);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            unresEventContentBox.getChildren().add(tempBox);
			// Only increment the counter if an element is added
			if (!InterfaceController.logicControl.isTitleOrDate(events[i])) {
				numOfResults++;
			}
        }
    }
}
