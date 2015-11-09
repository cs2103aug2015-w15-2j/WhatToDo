/**
 * This class initializes the done view of the application and provides methods that 
 * update the view when the file has been edited by the user.
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

public class DoneViewController {

	// ================================================================================
    // JavaFX controls used in the all interface
    // ================================================================================
	
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
    
    private static final String HEADER_DONE_TASKS = "COMPLETED TASKS";
    private static final String HEADER_DONE_EVENTS = "COMPLETED EVENTS";
    
    /**
     * This method initializes all the interface components for the done view,
     * primarily the task window and the event window
     */
    protected static void initDoneView() {
        initDoneTaskView(InterfaceController.getLogic().getDoneTasks());
        initDoneEventView(InterfaceController.getLogic().getDoneEvents());
        
        doneScrollLine = new Line(0, 0, 0, InterfaceController.WIDTH_DEFAULT_BUTTON);
        InterfaceController.doneBox = new HBox(doneTaskBox, doneScrollLine, doneEventBox);
        
        // Component formatting
        doneTaskScroll.prefViewportWidthProperty().bind(
        		InterfaceController.doneBox.widthProperty().divide(2));
        doneEventScroll.prefViewportWidthProperty().bind(
        		InterfaceController.doneBox.widthProperty().divide(2));
        
        doneTaskScroll.maxWidthProperty().bind(
        		InterfaceController.doneBox.widthProperty().divide(2));
        doneEventScroll.maxWidthProperty().bind(
        		InterfaceController.doneBox.widthProperty().divide(2));
        
        doneScrollLine.endYProperty().bind(DefaultViewController.getDefScrollLine().endYProperty());
        
        // CSS
        doneScrollLine.getStyleClass().add("line");
    }
    
    /**
     * This method updates the done view with data from the text file
     * 
     * Called by:
     * 	1. 	runCommand() in LogicController to update the view every time an 
     * 		operation is performed
     * 	2. 	updateMainInterface() in InterfaceController to update the view when
     * 		a view change command is issued (button/hotkey/text command)
     */
    protected static void updateDoneView() {
    	// Clear the previous content already displayed
        doneTaskContentBox.getChildren().clear();
        doneEventContentBox.getChildren().clear();
        ViewIndexMap.resetDoneMap();
        
        String[] tasks = InterfaceController.getLogic().getDoneTasks();
        String[] events = InterfaceController.getLogic().getDoneEvents();
    	int numOfElements = InterfaceController.getLogic().getDoneElementsCount();
    	
    	int numOfResults = 1;
    	numOfResults = InterfaceController.updateTasks(
    			tasks, numOfElements, numOfResults, View.DONE, doneTaskContentBox);
    	numOfResults = InterfaceController.updateEvents(
    			events, numOfElements, numOfResults, View.DONE, doneEventContentBox);
    }
    
    // ================================================================================
    // Private methods, used to initialize various sub components of the interface
    // ================================================================================
    
    /**
     * This method initializes the task view for the done view
     * 
     * @param tasks
     * 		      A String[] of tasks returned from LogicController's
     * 			  getDoneTasks()
     */
    private static void initDoneTaskView(String[] tasks) {
    	Label doneTaskHeader = new Label(HEADER_DONE_TASKS);
        doneTaskHeaderBox = new HBox(doneTaskHeader);
        doneTaskContentBox = new VBox();
        
        for (int i = 0; i < tasks.length; i++) {
        	// Use a temporary component for formatting
        	int numOfElements = InterfaceController.getLogic().getDoneElementsCount();
        	HBox tempBox = InterfaceController.initDisplayElement(
        			tasks[i], numOfElements, 1, true, View.DONE);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            doneTaskContentBox.getChildren().add(tempBox);
        }
        
        doneTaskScroll = new ScrollPane(doneTaskContentBox);
        doneTaskBox = new VBox(doneTaskHeaderBox, doneTaskScroll);
        
        // Component formatting
        doneTaskHeaderBox.setAlignment(Pos.CENTER);
        doneTaskScroll.setFitToWidth(true);
        VBox.setVgrow(doneTaskScroll, Priority.ALWAYS);
        doneTaskBox.setAlignment(Pos.CENTER);
        
        HBox.setMargin(doneTaskHeader, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        VBox.setMargin(doneTaskHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        VBox.setMargin(doneTaskScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));
        
        doneTaskScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // CSS
        doneTaskHeader.getStyleClass().add("box-title-label");
        doneTaskHeaderBox.getStyleClass().add("box-title-all-task");
    }

    /**
     * This method initializes the event view for the done view
     * 
     * @param tasks
     * 		      A String[] of tasks returned from LogicController's
     * 			  getDoneEvents()
     */
    private static void initDoneEventView(String[] events) {
    	Label doneEventHeader = new Label(HEADER_DONE_EVENTS);
        doneEventHeaderBox = new HBox(doneEventHeader);
        doneEventContentBox = new VBox();
        
        for (int i = 0; i < events.length; i++) {
        	// Use a temporary component for formatting
        	int numOfElements = InterfaceController.getLogic().getDoneElementsCount();
        	HBox tempBox = InterfaceController.initDisplayElement(
        			events[i], numOfElements, 1, false, View.DONE);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
            doneEventContentBox.getChildren().add(tempBox);
        }
        
        doneEventScroll = new ScrollPane(doneEventContentBox);
        doneEventBox = new VBox(doneEventHeaderBox, doneEventScroll);
        
        // Component formatting
        doneEventHeaderBox.setAlignment(Pos.CENTER);
        doneEventScroll.setFitToWidth(true);
        VBox.setVgrow(doneEventScroll, Priority.ALWAYS);
        doneEventBox.setAlignment(Pos.CENTER);
        
        HBox.setMargin(doneEventHeader, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        VBox.setMargin(doneEventHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        VBox.setMargin(doneEventScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));
        
        doneEventScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // CSS
        doneEventHeader.getStyleClass().add("box-title-label");
        doneEventHeaderBox.getStyleClass().add("box-title-all-event");
    }

}
