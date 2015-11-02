package gui;

import java.util.ArrayList;
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

    // Used for initAllView
    private static Line allScrollLine;
    
    protected static final String HEADER_ALL_TASKS = "UPCOMING TASKS: ALL";
    protected static final String HEADER_ALL_EVENTS = "UPCOMING EVENTS: ALL";
    
    private static HBox initDisplayElement(String displayData, int numOfElements, int index, boolean isTask) {
    	// Apply different CSS styles and formatting depending on whether it 
    	// contains a data field or a title field
    	if (InterfaceController.getLogic().isTitleOrDate(displayData)) {
    		
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
				ViewIndexMap.addToAllMap(Integer.parseInt(displayDataSplit[0]));
				
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

    private static void initAllTaskView(String[] tasks) {

    	Label allTaskHeader = new Label(HEADER_ALL_TASKS);
        allTaskHeaderBox = new HBox(allTaskHeader);
        allTaskHeaderBox.setAlignment(Pos.CENTER);

        allTaskContentBox = new VBox();
        
        // Run the loop through the entire task list
        for (int i = 0; i < tasks.length; i++) {
        	// Use a temporary component for formatting
        	int numOfElements = InterfaceController.getLogic().getAllElementsCount();
        	HBox tempBox = initDisplayElement(tasks[i], numOfElements, 1, true);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            allTaskContentBox.getChildren().add(tempBox);
        }
        
        allTaskScroll = new ScrollPane(allTaskContentBox);
        allTaskScroll.setFitToWidth(true);
        
        allTaskBox = new VBox(allTaskHeaderBox, allTaskScroll);
        
        // Set margins for the header label
        HBox.setMargin(allTaskHeader, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
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
        allTaskHeader.getStyleClass().add("box-title-label");
        allTaskHeaderBox.getStyleClass().add("box-title-all-task");
    }

    private static void initAllEventView(String[] events) {

    	Label allEventHeader = new Label(HEADER_ALL_EVENTS);
        allEventHeaderBox = new HBox(allEventHeader);
        allEventHeaderBox.setAlignment(Pos.CENTER);

        allEventContentBox = new VBox();
        
        // Run the loop through the entire task list
        for (int i = 0; i < events.length; i++) {
        	// Use a temporary component for formatting
        	int numOfElements = InterfaceController.getLogic().getAllElementsCount();
        	HBox tempBox = initDisplayElement(events[i], numOfElements, 1, false);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            allEventContentBox.getChildren().add(tempBox);
        }
        
        
        allEventScroll = new ScrollPane(allEventContentBox);
        allEventScroll.setFitToWidth(true);
        
        allEventBox = new VBox(allEventHeaderBox, allEventScroll);
        
        // Set margins for the header label
        HBox.setMargin(allEventHeader, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
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
        allEventHeader.getStyleClass().add("box-title-label");
        allEventHeaderBox.getStyleClass().add("box-title-all-event");
    }

    public static void initAllView() {

        initAllTaskView(InterfaceController.getLogic().getAllTasks());
        initAllEventView(InterfaceController.getLogic().getAllEvents());
        
        allScrollLine = new Line(0, 0, 0, InterfaceController.WIDTH_DEFAULT_BUTTON);
        
        InterfaceController.allBox = new HBox(allTaskBox, allScrollLine, allEventBox);
        
        // Set the preferred viewport width of the two scroll panes to be half
        // of the entire view pane
        allTaskScroll.prefViewportWidthProperty().bind(
        		InterfaceController.allBox.widthProperty().divide(2));
        allEventScroll.prefViewportWidthProperty().bind(
        		InterfaceController.allBox.widthProperty().divide(2));
        
        // Fix the width of the scroll panes to prevent resize of the inner labels
        allTaskScroll.maxWidthProperty().bind(
        		InterfaceController.allBox.widthProperty().divide(2));
        allEventScroll.maxWidthProperty().bind(
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
        ViewIndexMap.resetAllMap();
        
        // Get the results of the file from logic
        String[] tasks = InterfaceController.getLogic().getAllTasks();
        String[] events = InterfaceController.getLogic().getAllEvents();
        
    	int numOfElements = InterfaceController.getLogic().getAllElementsCount();

    	// Print the task results
    	// Only print the empty message if there are zero results
    	int numOfResults = 1;
    	if (tasks.length == 3 && InterfaceController.getLogic().isEmpty(tasks[2])) {
    		HBox tempBox = initDisplayElement(tasks[2], numOfElements, numOfResults, true);
    		VBox.setMargin(tempBox, new Insets(
    				0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
    		allTaskContentBox.getChildren().add(tempBox);
    	} else {
    		// If there are no results for floating tasks
    		if (InterfaceController.getLogic().isEmpty(tasks[tasks.length - 1])) {
    			for (int i = 0; i < tasks.length; i++) {
    				HBox tempBox = initDisplayElement(tasks[i], numOfElements, numOfResults, true);
    				VBox.setMargin(tempBox, new Insets(
    						0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
    				allTaskContentBox.getChildren().add(tempBox);
    				// Only increment the counter if an element is added
    				if (InterfaceController.getLogic().isNonEmptyElement(tasks[i])) {
    					numOfResults++;
    				}
    			}
    		} else {
    			for (int i = 0; i < tasks.length; i++) {
    				if (!InterfaceController.getLogic().isEmpty(tasks[i])) {
    					HBox tempBox = initDisplayElement(tasks[i], numOfElements, numOfResults, true);
    					VBox.setMargin(tempBox, new Insets(
    							0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
    					allTaskContentBox.getChildren().add(tempBox);
    					// Only increment the counter if an element is added
    					if (InterfaceController.getLogic().isNonEmptyElement(tasks[i])) {
    						numOfResults++;
    					}
    				}
    			}
    		}
    	}
    	// Print the event results
    	for (int i = 0; i < events.length; i++) {
    		HBox tempBox = initDisplayElement(events[i], numOfElements, numOfResults, false);
    		VBox.setMargin(tempBox, new Insets(
    				0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
    		allEventContentBox.getChildren().add(tempBox);
    		if (InterfaceController.getLogic().isNonEmptyElement(events[i])) {
    			numOfResults++;
    		}
    	}
    }
}
