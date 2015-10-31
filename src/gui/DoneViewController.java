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

public class DoneViewController {

	/* ================================================================================
     * JavaFX controls used in the general interface
     * ================================================================================
     */
	
	// Used for initDoneTaskView
    private static VBox doneTaskBox, doneTaskContentBox;
    private static HBox doneTaskHeaderBox;
    private static ScrollPane doneTaskScroll;

    // Used for initDoneEventView
    private static VBox doneEventBox, doneEventContentBox;
    private static HBox doneEventHeaderBox;
    private static ScrollPane doneEventScroll;

    // Used for initDoneView
    private static Line doneScrollLine;
    
    protected static final String HEADER_DONE_TASKS = "COMPLETED TASKS";
    protected static final String HEADER_DONE_EVENTS = "COMPLETED EVENTS";
    
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
				ViewIndexMap.addToDoneMap(Integer.parseInt(displayDataSplit[0]));
				
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

    private static void initDoneTaskView(String[] tasks) {

    	Label doneTaskHeader = new Label(HEADER_DONE_TASKS);
        doneTaskHeaderBox = new HBox(doneTaskHeader);
        doneTaskHeaderBox.setAlignment(Pos.CENTER);

        doneTaskContentBox = new VBox();
        
        // Run the loop through the entire task list
        for (int i = 0; i < tasks.length; i++) {
        	// Use a temporary component for formatting
        	int numOfElements = InterfaceController.logicControl.getDoneElementsCount();
        	HBox tempBox = initDisplayElement(tasks[i], numOfElements, 1, true);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            doneTaskContentBox.getChildren().add(tempBox);
        }
        
        doneTaskScroll = new ScrollPane(doneTaskContentBox);
        doneTaskScroll.setFitToWidth(true);
        
        doneTaskBox = new VBox(doneTaskHeaderBox, doneTaskScroll);
        
        // Set margins for the header label
        HBox.setMargin(doneTaskHeader, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        // Set margins for the header
        VBox.setMargin(doneTaskHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        // Set margins for the scroll pane
        VBox.setMargin(doneTaskScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));
        
        // Set the alignment of the header image to be in the center
        doneTaskBox.setAlignment(Pos.CENTER);
        
        // Set the height of the scroll pane to grow with window height
        VBox.setVgrow(doneTaskScroll, Priority.ALWAYS);
        
        // Set the scrollbar policy of the scroll pane
        doneTaskScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // CSS
        doneTaskHeader.getStyleClass().add("box-title-label");
        doneTaskHeaderBox.getStyleClass().add("box-title-all-task");
    }

    private static void initDoneEventView(String[] events) {

    	Label doneEventHeader = new Label(HEADER_DONE_EVENTS);
        doneEventHeaderBox = new HBox(doneEventHeader);
        doneEventHeaderBox.setAlignment(Pos.CENTER);

        doneEventContentBox = new VBox();
        
        // Run the loop through the entire task list
        for (int i = 0; i < events.length; i++) {
        	// Use a temporary component for formatting
        	int numOfElements = InterfaceController.logicControl.getDoneElementsCount();
        	HBox tempBox = initDisplayElement(events[i], numOfElements, 1, false);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            doneEventContentBox.getChildren().add(tempBox);
        }
        
        
        doneEventScroll = new ScrollPane(doneEventContentBox);
        doneEventScroll.setFitToWidth(true);
        
        doneEventBox = new VBox(doneEventHeaderBox, doneEventScroll);
        
        // Set margins for the header label
        HBox.setMargin(doneEventHeader, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        // Set margins for the header
        VBox.setMargin(doneEventHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        // Set margins for the scroll pane
        VBox.setMargin(doneEventScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));
        
        // Set the alignment of the header image to be in the center
        doneEventBox.setAlignment(Pos.CENTER);

        // Set the height of the scroll pane to grow with the window height
        VBox.setVgrow(doneEventScroll, Priority.ALWAYS);
        
        // Set the scrollbar policy of the scroll pane
        doneEventScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // CSS
        doneEventHeader.getStyleClass().add("box-title-label");
        doneEventHeaderBox.getStyleClass().add("box-title-all-event");
    }

    public static void initDoneView() {

        initDoneTaskView(InterfaceController.logicControl.getDoneTasks());
        initDoneEventView(InterfaceController.logicControl.getDoneEvents());
        
        doneScrollLine = new Line(0, 0, 0, InterfaceController.WIDTH_DEFAULT_BUTTON);
        
        InterfaceController.doneBox = new HBox(doneTaskBox, doneScrollLine, doneEventBox);
        
        // Set the preferred viewport width of the two scroll panes to be half
        // of the entire view pane
        doneTaskScroll.prefViewportWidthProperty().bind(
        		InterfaceController.doneBox.widthProperty().divide(2));
        doneEventScroll.prefViewportWidthProperty().bind(
        		InterfaceController.doneBox.widthProperty().divide(2));
        
        // Fix the width of the scroll panes to prevent resize of the inner labels
        doneTaskScroll.maxWidthProperty().bind(
        		InterfaceController.doneBox.widthProperty().divide(2));
        doneEventScroll.maxWidthProperty().bind(
        		InterfaceController.doneBox.widthProperty().divide(2));
        
        // Set the scroll separator to bind with the same line in DefaultViewController
        doneScrollLine.endYProperty().bind(DefaultViewController.getDefScrollLine().endYProperty());
        
        // CSS
        doneScrollLine.getStyleClass().add("line");
    }
    
    public static void updateDoneView() {
    	
    	// Clear the previous content already displayed
        doneTaskContentBox.getChildren().clear();
        doneEventContentBox.getChildren().clear();
        ViewIndexMap.resetDoneMap();
        
        // Get the results of the file from logic
        String[] tasks = InterfaceController.logicControl.getDoneTasks();
        String[] events = InterfaceController.logicControl.getDoneEvents();
        
    	int numOfElements = InterfaceController.logicControl.getDoneElementsCount();
    	
        // Run the loop through the entire task list
    	int numOfResults = 1;
        for (int i = 0; i < tasks.length; i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = initDisplayElement(tasks[i], numOfElements, numOfResults, true);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            doneTaskContentBox.getChildren().add(tempBox);
			// Only increment the counter if an element is added
			if (InterfaceController.logicControl.isNonEmptyElement(tasks[i])) {
				numOfResults++;
			}
        }
        
        // Run the loop through the entire task list
        for (int i = 0; i < events.length; i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = initDisplayElement(events[i], numOfElements, numOfResults, false);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            doneEventContentBox.getChildren().add(tempBox);
			// Only increment the counter if an element is added
			if (InterfaceController.logicControl.isNonEmptyElement(events[i])) {
				numOfResults++;
			}
        }
    }
}
