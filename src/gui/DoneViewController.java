package gui;

import java.util.regex.Pattern;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
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
    
    private static void initDoneTaskView(String[] tasks) {

    	Label doneTaskHeader = new Label(HEADER_DONE_TASKS);
        doneTaskHeaderBox = new HBox(doneTaskHeader);
        doneTaskHeaderBox.setAlignment(Pos.CENTER);

        doneTaskContentBox = new VBox();
        
        // Run the loop through the entire task list
        for (int i = 0; i < tasks.length; i++) {
        	// Use a temporary component for formatting
        	int numOfElements = InterfaceController.getLogic().getDoneElementsCount();
        	HBox tempBox = InterfaceController.initDisplayElement(tasks[i], numOfElements, 1, true);
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
        	int numOfElements = InterfaceController.getLogic().getDoneElementsCount();
        	HBox tempBox = InterfaceController.initDisplayElement(events[i], numOfElements, 1, false);
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

        initDoneTaskView(InterfaceController.getLogic().getDoneTasks());
        initDoneEventView(InterfaceController.getLogic().getDoneEvents());
        
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
        String[] tasks = InterfaceController.getLogic().getDoneTasks();
        String[] events = InterfaceController.getLogic().getDoneEvents();
        
    	int numOfElements = InterfaceController.getLogic().getDoneElementsCount();
    	
    	// Print the task results
    	// Only print the empty message if there are zero results
    	int numOfResults = 1;
    	if (tasks.length == 3 && InterfaceController.getLogic().isEmpty(tasks[2])) {
    		HBox tempBox = InterfaceController.initDisplayElement(tasks[2], numOfElements, numOfResults, true);
    		VBox.setMargin(tempBox, new Insets(
    				0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
    		doneTaskContentBox.getChildren().add(tempBox);
    	} else {
    		// If there are no results for floating tasks
    		if (InterfaceController.getLogic().isEmpty(tasks[tasks.length - 1])) {
    			for (int i = 0; i < tasks.length; i++) {
    				HBox tempBox = InterfaceController.initDisplayElement(tasks[i], numOfElements, numOfResults, true);
    				VBox.setMargin(tempBox, new Insets(
    						0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
    				doneTaskContentBox.getChildren().add(tempBox);
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
    					doneTaskContentBox.getChildren().add(tempBox);
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
    		doneEventContentBox.getChildren().add(tempBox);
    		if (InterfaceController.getLogic().isNonEmptyElement(events[i])) {
    			numOfResults++;
    		}
    	}
    }
}
