/**
 * This class initializes the unresolved view of the application and provides methods 
 * that update the view when the file has been edited by the user.
 * 
 * @@author A0124123Y
 */

package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import struct.View;

public class UnresolvedViewController {

	// ================================================================================
    // JavaFX controls used in the unresolved interface
    // ================================================================================
	
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
    
    private static final String HEADER_UNRESOLVED_TASKS = "UNRESOLVED TASKS";
    private static final String HEADER_UNRESOLVED_EVENTS = "UNRESOLVED EVENTS";
    
    /**
     * This method initializes all the interface components for the unresolved view,
     * primarily the task window and the event window
     */
    protected static void initUnresView() {
        initUnresTaskView(InterfaceController.getLogic().getUnresTasks());
        initUnresEventView(InterfaceController.getLogic().getUnresEvents());
        
        unresScrollLine = new Line(0, 0, 0, InterfaceController.WIDTH_DEFAULT_BUTTON);
        InterfaceController.unresBox = new HBox(unresTaskBox, unresScrollLine, unresEventBox);
        
        // Component formatting
        unresTaskScroll.prefViewportWidthProperty().bind(
        		InterfaceController.unresBox.widthProperty().divide(2));
        unresEventScroll.prefViewportWidthProperty().bind(
        		InterfaceController.unresBox.widthProperty().divide(2));
        
        unresTaskScroll.maxWidthProperty().bind(
        		InterfaceController.unresBox.widthProperty().divide(2));
        unresEventScroll.maxWidthProperty().bind(
        		InterfaceController.unresBox.widthProperty().divide(2));
        
        unresScrollLine.endYProperty().bind(DefaultViewController.getDefScrollLine().endYProperty());
        
        // CSS
        unresScrollLine.getStyleClass().add("line");
    }
    
    /**
     * This method updates the unresolved view with data from the text file
     * 
     * Called by:
     * 	1. 	runCommand() in LogicController to update the view every time an 
     * 		operation is performed
     * 	2. 	updateMainInterface() in InterfaceController to update the view when
     * 		a view change command is issued (button/hotkey/text command)
     */
    protected static void updateUnresView() {
    	// Clear the previous content already displayed
        unresTaskContentBox.getChildren().clear();
        unresEventContentBox.getChildren().clear();
        ViewIndexMap.resetUnresMap();
        
        String[] tasks = InterfaceController.getLogic().getUnresTasks();
        String[] events = InterfaceController.getLogic().getUnresEvents();
    	int numOfElements = InterfaceController.getLogic().getUnresElementsCount();
    	int numOfResults = 1;
    	
        // Run the loop through the entire task list
        for (int i = 0; i < tasks.length; i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = InterfaceController.initDisplayElement(
        			tasks[i], numOfElements, numOfResults, true, View.UNRESOLVED);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            unresTaskContentBox.getChildren().add(tempBox);
			// Only increment the counter if an element is added
			if (InterfaceController.getLogic().isNonEmptyElement(tasks[i])) {
				numOfResults++;
			}
        }
        // Run the loop through the entire event list
        for (int i = 0; i < events.length; i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = InterfaceController.initDisplayElement(
        			events[i], numOfElements, numOfResults, false, View.UNRESOLVED);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            unresEventContentBox.getChildren().add(tempBox);
			// Only increment the counter if an element is added
			if (InterfaceController.getLogic().isNonEmptyElement(events[i])) {
				numOfResults++;
			}
        }
    }
    
    // ================================================================================
    // Private methods, used to initialize various sub components of the interface
    // ================================================================================
    
    /**
     * This method initializes the task view for the unresolved view
     * 
     * @param tasks
     * 		      A String[] of tasks returned from LogicController's
     * 			  getUnresTasks()
     */
    private static void initUnresTaskView(String[] tasks) {
    	Label unresTaskHeader = new Label(HEADER_UNRESOLVED_TASKS);
        unresTaskHeaderBox = new HBox(unresTaskHeader);
        unresTaskContentBox = new VBox();
    	int numOfElements = InterfaceController.getLogic().getUnresElementsCount();
    	
        initUnresTasks(tasks, numOfElements);
        unresTaskScroll = new ScrollPane(unresTaskContentBox);
        unresTaskBox = new VBox(unresTaskHeaderBox, unresTaskScroll);
        
        // Component formatting
        unresTaskHeaderBox.setAlignment(Pos.CENTER);
        unresTaskScroll.setFitToWidth(true);
        VBox.setVgrow(unresTaskScroll, Priority.ALWAYS);
        unresTaskBox.setAlignment(Pos.CENTER);
        
        HBox.setMargin(unresTaskHeader, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        VBox.setMargin(unresTaskHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        VBox.setMargin(unresTaskScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));
        
        unresTaskScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // CSS
        unresTaskHeader.getStyleClass().add("box-title-label");
        unresTaskHeaderBox.getStyleClass().add("box-title-all-task");
    }

    /**
     * This method initializes the event view for the unresolved view
     * 
     * @param tasks
     * 		      A String[] of tasks returned from LogicController's
     * 			  getUnresEvents()
     */
    private static void initUnresEventView(String[] events) {
    	Label unresEventHeader = new Label(HEADER_UNRESOLVED_EVENTS);
        unresEventHeaderBox = new HBox(unresEventHeader);
        unresEventContentBox = new VBox();
    	int numOfElements = InterfaceController.getLogic().getUnresElementsCount();
    	
        initUnresEvents(events, numOfElements);
        unresEventScroll = new ScrollPane(unresEventContentBox);
        unresEventBox = new VBox(unresEventHeaderBox, unresEventScroll);
        
        // Component formatting
        unresEventHeaderBox.setAlignment(Pos.CENTER);
        unresEventScroll.setFitToWidth(true);
        VBox.setVgrow(unresEventScroll, Priority.ALWAYS);
        unresEventBox.setAlignment(Pos.CENTER);

        HBox.setMargin(unresEventHeader, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        VBox.setMargin(unresEventHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        VBox.setMargin(unresEventScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));
        
        unresEventScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // CSS
        unresEventHeader.getStyleClass().add("box-title-label");
        unresEventHeaderBox.getStyleClass().add("box-title-all-event");
    }

	/**
	 * This method initializes the task content of the unresolved view with data
	 * 
	 * @param tasks
	 * 			  The array of task data to be displayed in the view
	 * @param numOfElements
	 * 			  The total number of tasks/events. Used for formatting the index box
	 * @return The index of the last element to be added
	 */
	private static void initUnresTasks(String[] tasks, int numOfElements) {
		for (int i = 0; i < tasks.length; i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = InterfaceController.initDisplayElement(
        			tasks[i], numOfElements, 1, true, View.UNRESOLVED);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            unresTaskContentBox.getChildren().add(tempBox);
        }
	}
	
	/**
	 * This method initializes the event content of the unresolved view with data
	 * 
	 * @param events
	 * 			  The array of event data to be displayed in the view
	 * @param numOfElements
	 * 			  The total number of tasks/events. Used for formatting the index box
	 * @return The index of the last element to be added
	 */
	private static void initUnresEvents(String[] events, int numOfElements) {
		for (int i = 0; i < events.length; i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = InterfaceController.initDisplayElement(
        			events[i], numOfElements, 1, false, View.UNRESOLVED);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            unresEventContentBox.getChildren().add(tempBox);
        }
	}
}
