/**
 * This class initializes the default view of the application and provides methods 
 * that update the view when the file has been edited by the user.
 * 
 * @@author A0124123Y
 */

package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import struct.View;

public class DefaultViewController {

	// ================================================================================
    // JavaFX controls used in the default interface
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
    
    private static final String HEADER_DEF_TASKS = "UPCOMING TASKS: SOON";
    private static final String HEADER_DEF_EVENTS = "UPCOMING EVENTS: SOON";
    
    /**
     * This method initializes all the interface components for the default view,
     * primarily the task window and the event window
     */
    protected static void initDefView() {
        initDefTaskView(InterfaceController.getLogic().getDefTasks());
        initDefEventView(InterfaceController.getLogic().getDefEvents());
        
        defScrollLine = new Line(0, 0, 0, InterfaceController.WIDTH_DEFAULT_BUTTON);
        
        InterfaceController.defBox = new HBox(defTaskBox, defScrollLine, defEventBox);
        
        // Component formatting
        defTaskScroll.prefViewportWidthProperty().bind(
        		InterfaceController.defBox.widthProperty().divide(2));
        defEventScroll.prefViewportWidthProperty().bind(
        		InterfaceController.defBox.widthProperty().divide(2));
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
    protected static void updateDefView() {
    	// Clear the previous content already displayed
        defTaskContentBox.getChildren().clear();
        defEventContentBox.getChildren().clear();
        ViewIndexMap.resetDefMap();
        
        // Get the results of the file from logic
        String[] tasks = InterfaceController.getLogic().getDefTasks();
        String[] events = InterfaceController.getLogic().getDefEvents();
        
        int numOfElements = InterfaceController.getLogic().getDefElementsCount();
        int numOfResults = 1;
        
        numOfResults = updateDefTasks(tasks, numOfElements, numOfResults);
        numOfResults = updateDefEvents(events, numOfElements, numOfResults);
    }

    // ================================================================================
    // Getters for logicControl to access required JavaFX components
    // ================================================================================
    
    /**
     * This method returns the line separator between the task and event views 
     * 
     * @return The Line object separating the task and event views.
     */
    protected static Line getDefScrollLine() {
    	return defScrollLine;
    }
    
    // ================================================================================
    // Private methods, used to initialize various sub components of the interface
    // ================================================================================
    
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
        defTaskContentBox = new VBox();
        int numOfElements = InterfaceController.getLogic().getDefElementsCount();
        
        initDefTasks(tasks, numOfElements);
        defTaskScroll = new ScrollPane(defTaskContentBox);
        defTaskBox = new VBox(defTaskHeaderBox, defTaskScroll);
        
        // Component formatting
        defTaskHeaderBox.setAlignment(Pos.CENTER);
        defTaskScroll.setFitToWidth(true);
        VBox.setVgrow(defTaskScroll, Priority.ALWAYS);
        defTaskBox.setAlignment(Pos.CENTER);
        
        HBox.setMargin(defTaskHeader, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        VBox.setMargin(defTaskHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        VBox.setMargin(defTaskScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));
        
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
        defEventContentBox = new VBox();
        int numOfElements = InterfaceController.getLogic().getDefElementsCount();
        
        initDefEvents(events, numOfElements);
        defEventScroll = new ScrollPane(defEventContentBox);
        defEventBox = new VBox(defEventHeaderBox, defEventScroll);
        
        // Component formatting
        defEventHeaderBox.setAlignment(Pos.CENTER);
        defEventScroll.setFitToWidth(true);
        VBox.setVgrow(defEventScroll, Priority.ALWAYS);
        defEventBox.setAlignment(Pos.CENTER);
        
        HBox.setMargin(defEventHeader, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        VBox.setMargin(defEventHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        VBox.setMargin(defEventScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));
        
        defEventScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        defEventScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // CSS
        defEventHeader.getStyleClass().add("box-title-label");
        defEventHeaderBox.getStyleClass().add("box-title");
    }

	/**
	 * This method initializes the task content of the default view with data
	 * 
	 * @param tasks
	 * 			  The array of task data to be displayed in the view
	 * @param numOfElements
	 * 			  The total number of tasks/events. Used for formatting the index box
	 * @return The index of the last element to be added
	 */
	private static void initDefTasks(String[] tasks, int numOfElements) {
		for (int i = 0; i < tasks.length; i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = InterfaceController.initDisplayElement(
        			tasks[i], numOfElements, 1, true, View.DEFAULT);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            defTaskContentBox.getChildren().add(tempBox);
        }
	}
	
	/**
	 * This method initializes the event content of the default view with data
	 * 
	 * @param events
	 * 			  The array of event data to be displayed in the view
	 * @param numOfElements
	 * 			  The total number of tasks/events. Used for formatting the index box
	 * @return The index of the last element to be added
	 */
	private static void initDefEvents(String[] events, int numOfElements) {
		for (int i = 0; i < events.length; i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = InterfaceController.initDisplayElement(
        			events[i], numOfElements, 1, false, View.DEFAULT);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            defEventContentBox.getChildren().add(tempBox);
        }
	}
	
	/**
	 * This method updates the task content of the default view with the
     * updated data
     * 
	 * @param tasks
	 * 			  The updated array of task data to be displayed in the view
	 * @param numOfElements
	 * 			  The total number of tasks/events. Used for formatting the index box
	 * @param index
	 * 			  The view index of the particular task/event
	 * @return The index of the last element to be added
	 */
	private static int updateDefTasks(String[] tasks, int numOfElements, int index) {
		for (int i = 0; i < tasks.length; i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = InterfaceController.initDisplayElement(
        			tasks[i], numOfElements, index, true, View.DEFAULT);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            defTaskContentBox.getChildren().add(tempBox);
			// Only increment the counter if an element is added
			if (InterfaceController.getLogic().isNonEmptyElement(tasks[i])) {
				index++;
			}
        }
		return index;
	}
	
	/**
	 * This method updates the event content of the default view with the
     * updated data
     * 
	 * @param events
	 * 			  The updated array of event data to be displayed in the view
	 * @param numOfElements
	 * 			  The total number of tasks/events. Used for formatting the index box
	 * @param index
	 * 			  The view index of the particular task/event
	 * @return The index of the last element to be added
	 */
	private static int updateDefEvents(String[] events, int numOfElements, int index) {
		// Run the loop through the entire event list
        for (int i = 0; i < events.length; i++) {
        	// Use a temporary component for formatting
        	HBox tempBox = InterfaceController.initDisplayElement(
        			events[i], numOfElements, index, false, View.DEFAULT);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            defEventContentBox.getChildren().add(tempBox);
			// Only increment the counter if an element is added
			if (InterfaceController.getLogic().isNonEmptyElement(events[i])) {
				index++;
			}
        }
		return index;
	}
}
