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

    private static void initAllTaskView(String[] tasks) {

    	Label allTaskHeader = new Label(HEADER_ALL_TASKS);
        allTaskHeaderBox = new HBox(allTaskHeader);
        allTaskHeaderBox.setAlignment(Pos.CENTER);

        allTaskContentBox = new VBox();
        
        // Run the loop through the entire task list
        for (int i = 0; i < tasks.length; i++) {
        	// Use a temporary component for formatting
        	int numOfElements = InterfaceController.getLogic().getAllElementsCount();
        	HBox tempBox = InterfaceController.initDisplayElement(tasks[i], numOfElements, 1, true);
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
        	HBox tempBox = InterfaceController.initDisplayElement(events[i], numOfElements, 1, false);
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
    		HBox tempBox = InterfaceController.initDisplayElement(tasks[2], numOfElements, numOfResults, true);
    		VBox.setMargin(tempBox, new Insets(
    				0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
    		allTaskContentBox.getChildren().add(tempBox);
    	} else {
    		// If there are no results for floating tasks
    		if (InterfaceController.getLogic().isEmpty(tasks[tasks.length - 1])) {
    			for (int i = 0; i < tasks.length; i++) {
    				HBox tempBox = InterfaceController.initDisplayElement(tasks[i], numOfElements, numOfResults, true);
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
    					HBox tempBox = InterfaceController.initDisplayElement(tasks[i], numOfElements, numOfResults, true);
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
    		HBox tempBox = InterfaceController.initDisplayElement(events[i], numOfElements, numOfResults, false);
    		VBox.setMargin(tempBox, new Insets(
    				0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
    		allEventContentBox.getChildren().add(tempBox);
    		if (InterfaceController.getLogic().isNonEmptyElement(events[i])) {
    			numOfResults++;
    		}
    	}
    }
}
