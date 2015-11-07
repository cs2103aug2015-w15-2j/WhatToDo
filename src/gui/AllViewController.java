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

public class AllViewController {

	// ================================================================================
    // JavaFX controls used in the all interface
    // ================================================================================
	
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
    
    private static final String HEADER_ALL_TASKS = "UPCOMING TASKS: ALL";
    private static final String HEADER_ALL_EVENTS = "UPCOMING EVENTS: ALL";

    /**
     * This method initializes all the interface components for the all view,
     * primarily the task window and the event window
     */
    protected static void initAllView() {
        initAllTaskView(InterfaceController.getLogic().getAllTasks());
        initAllEventView(InterfaceController.getLogic().getAllEvents());
        
        allScrollLine = new Line(0, 0, 0, InterfaceController.WIDTH_DEFAULT_BUTTON);
        InterfaceController.allBox = new HBox(allTaskBox, allScrollLine, allEventBox);
        
        // Component formatting
        allTaskScroll.prefViewportWidthProperty().bind(
        		InterfaceController.allBox.widthProperty().divide(2));
        allEventScroll.prefViewportWidthProperty().bind(
        		InterfaceController.allBox.widthProperty().divide(2));
        
        allTaskScroll.maxWidthProperty().bind(
        		InterfaceController.allBox.widthProperty().divide(2));
        allEventScroll.maxWidthProperty().bind(
        		InterfaceController.allBox.widthProperty().divide(2));
        
        allScrollLine.endYProperty().bind(DefaultViewController.getDefScrollLine().endYProperty());
        
        // CSS
        allScrollLine.getStyleClass().add("line");
    }
    
    /**
     * This method updates the all view with data from the text file
     * 
     * Called by:
     * 	1. 	runCommand() in LogicController to update the view every time an 
     * 		operation is performed
     * 	2. 	updateMainInterface() in InterfaceController to update the view when
     * 		a view change command is issued (button/hotkey/text command)
     */
    protected static void updateAllView() {
    	// Clear the previous content already displayed
        allTaskContentBox.getChildren().clear();
        allEventContentBox.getChildren().clear();
        ViewIndexMap.resetAllMap();
        
        // Get the results of the file from logic
        String[] tasks = InterfaceController.getLogic().getAllTasks();
        String[] events = InterfaceController.getLogic().getAllEvents();
    	int numOfElements = InterfaceController.getLogic().getAllElementsCount();
    	int numOfResults = 1;
    	
    	numOfResults = InterfaceController.updateTasks(
    			tasks, numOfElements, numOfResults, View.ALL, allTaskContentBox);
    	numOfResults = InterfaceController.updateEvents(
    			events, numOfElements, numOfResults, View.ALL, allEventContentBox);
    }
    
    // ================================================================================
    // Private methods, used to initialize various sub components of the interface
    // ================================================================================
    
    /**
     * This method initializes the task view for the all view
     * 
     * @param tasks
     * 		      A String[] of tasks returned from LogicController's
     * 			  getAllTasks()
     */
    private static void initAllTaskView(String[] tasks) {
    	Label allTaskHeader = new Label(HEADER_ALL_TASKS);
        allTaskHeaderBox = new HBox(allTaskHeader);
        allTaskContentBox = new VBox();
        
    	int numOfElements = InterfaceController.getLogic().getAllElementsCount();
        initAllTasks(tasks, numOfElements);
        
        allTaskScroll = new ScrollPane(allTaskContentBox);
        allTaskBox = new VBox(allTaskHeaderBox, allTaskScroll);
        
        // Component formatting
        allTaskHeaderBox.setAlignment(Pos.CENTER);
        allTaskScroll.setFitToWidth(true);
        VBox.setVgrow(allTaskScroll, Priority.ALWAYS);
        allTaskBox.setAlignment(Pos.CENTER);
        
        HBox.setMargin(allTaskHeader, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        VBox.setMargin(allTaskHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        VBox.setMargin(allTaskScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));
        
        allTaskScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // CSS
        allTaskHeader.getStyleClass().add("box-title-label");
        allTaskHeaderBox.getStyleClass().add("box-title-all-task");
    }

    /**
     * This method initializes the event view for the all view
     * 
     * @param tasks
     * 		      A String[] of tasks returned from LogicController's
     * 			  getAllEvents()
     */
    private static void initAllEventView(String[] events) {
    	Label allEventHeader = new Label(HEADER_ALL_EVENTS);
        allEventHeaderBox = new HBox(allEventHeader);
        allEventContentBox = new VBox();
        
    	int numOfElements = InterfaceController.getLogic().getAllElementsCount();
        initAllEvents(events, numOfElements);
        
        allEventScroll = new ScrollPane(allEventContentBox);
        allEventBox = new VBox(allEventHeaderBox, allEventScroll);
        
        // Component formatting
        allEventHeaderBox.setAlignment(Pos.CENTER);
        allEventScroll.setFitToWidth(true);
        VBox.setVgrow(allEventScroll, Priority.ALWAYS);
        allEventBox.setAlignment(Pos.CENTER);

        HBox.setMargin(allEventHeader, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        VBox.setMargin(allEventHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        VBox.setMargin(allEventScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));
        
        allEventScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // CSS
        allEventHeader.getStyleClass().add("box-title-label");
        allEventHeaderBox.getStyleClass().add("box-title-all-event");
    }

	/**
	 * This method initializes the task content of the all view with data
	 * 
	 * @param tasks
	 * 			  The array of task data to be displayed in the view
	 * @param numOfElements
	 * 			  The total number of tasks/events. Used for formatting the index box
	 * @return The index of the last element to be added
	 */
	private static void initAllTasks(String[] tasks, int numOfElements) {
		for (int i = 0; i < tasks.length; i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = InterfaceController.initDisplayElement(
        			tasks[i], numOfElements, 1, true, View.ALL);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            allTaskContentBox.getChildren().add(tempBox);
        }
	}
	
	/**
	 * This method initializes the event content of the all view with data
	 * 
	 * @param events
	 * 			  The array of event data to be displayed in the view
	 * @param numOfElements
	 * 			  The total number of tasks/events. Used for formatting the index box
	 * @return The index of the last element to be added
	 */
	private static void initAllEvents(String[] events, int numOfElements) {
		for (int i = 0; i < events.length; i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = InterfaceController.initDisplayElement(
        			events[i], numOfElements, 1, false, View.ALL);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            allEventContentBox.getChildren().add(tempBox);
        }
	}
}
