/**
 * This class initializes the history view of the application and provides methods 
 * that update the view when the user has entered a new command and a new feedback
 * message has been generated.
 * 
 * @@author A0124123Y
 */

package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import struct.View;

public class HistoryViewController {

	// ================================================================================
    // JavaFX controls used in the default interface
    // ================================================================================
	
	// Used for initAllTaskView
    private static VBox histBox, histContentBox;
    private static HBox histHeaderBox;
    private static ScrollPane histScroll;
    
    private static final String HEADER_HISTORY = "OPERATION HISTORY";
    private static final String MESSAGE_EMPTY_HIST = "No operations performed yet.";
    
    private static int messageIndex = 0;

    /**
     * This method initializes all the interface components for the history view
     */
    protected static void initHistView() {
    	Label histHeader = new Label(HEADER_HISTORY);
        histHeaderBox = new HBox(histHeader);
        histHeaderBox.setAlignment(Pos.CENTER);
        
        // History view is empty when first initialized,
        // add one message for no operations performed yet
        HBox initialBox = initHistoryElement("", MESSAGE_EMPTY_HIST);
        VBox.setMargin(initialBox, new Insets(
        		0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
        
        histContentBox = new VBox(initialBox);
        histScroll = new ScrollPane(histContentBox);
        histBox = new VBox(histHeaderBox, histScroll);
        InterfaceController.histBox = new HBox(histBox);
        
        histContentBox.heightProperty().addListener(
        		Listeners.getScrollListener(View.HISTORY));
        
        // Component formatting
        HBox.setMargin(histHeader, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        // Reducing the margin by line width to compensate for the added vertical
        // separator in the default and all views
        VBox.setMargin(histHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL - InterfaceController.WIDTH_VERT_LINE, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        VBox.setMargin(histScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL - InterfaceController.WIDTH_VERT_LINE, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));
        
        histBox.setAlignment(Pos.CENTER);
        histBox.setAlignment(Pos.CENTER);
        VBox.setVgrow(histScroll, Priority.ALWAYS);
        histScroll.setFitToWidth(true);
        histScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        histScroll.prefViewportWidthProperty().bind(InterfaceController.histBox.widthProperty());
        
        // CSS
        histHeader.getStyleClass().add("box-title-label");
        histHeaderBox.getStyleClass().add("box-title-history");
    }
    
    /**
     * This method updates the history view by adding the feedback message to the view
     * 
     * @param feedbackMessage
     * 			  The feedback message returned from executeCommand()
     */
    protected static void updateHistView(String feedbackMessage) {
        messageIndex++;
        
        // Use a temporary component for formatting
        HBox tempBox = initHistoryElement(String.valueOf(messageIndex), feedbackMessage);
        if (messageIndex == 1) {
        	histContentBox.getChildren().clear();
        }
        histContentBox.getChildren().add(tempBox);
        
        // Component formatting
        VBox.setMargin(tempBox, new Insets(
        		0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
    }
    
    // ================================================================================
    // Getters for logicControl to access required JavaFX components
    // ================================================================================
    
    protected static ScrollPane getHistScroll() {
    	return histScroll;
    }
    
    // ================================================================================
    // Private methods, used to initialize various sub components of the interface
    // ================================================================================
    
    /**
     * This method creates a HBox containing a history view element
     * 
     * @param index
     * 		      The index of the element to be created
     * @param feedbackMessage
     * 			  The feedback message returned from Logic's executeCommand() method
     * 			  to be added into the history
     * @return A formatted HBox containing the history data
     */
    private static HBox initHistoryElement(String index, String feedbackMessage) {
    	Label feedbackIndex = new Label(index);
    	HBox indexBox = new HBox(feedbackIndex);
    	Label feedbackLabel = new Label(feedbackMessage);
    	HBox feedbackBox = new HBox(indexBox, feedbackLabel);

    	// Component formatting
    	if (index != "") {
        	// If is default and no commands entered, do not display index background
        	setToMaxWidth(index, indexBox);
    	}
    	
    	feedbackLabel.setWrapText(true);
    	indexBox.setAlignment(Pos.CENTER);
    	
    	HBox.setMargin(feedbackLabel, new Insets(
    			InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
    			InterfaceController.MARGIN_TEXT_ELEMENT, 
    			InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
    			InterfaceController.MARGIN_TEXT_ELEMENT));

    	// CSS
    	feedbackBox.getStyleClass().add("element");
    	indexBox.getStyleClass().add("element-index-history");
    	feedbackIndex.getStyleClass().add("element-index-label");
    	
    	return feedbackBox;
    }

	/**
	 * This method calculates the maximum width used by the current largest index
	 * and sets the indexBox to that width to ensure consistency
	 * 
	 * @param index
	 * 			  The view index to be inserted into indexBox
	 * @param indexBox
	 * 		      The HBox containing the index to be formatted
	 */
	private static void setToMaxWidth(String index, HBox indexBox) {
		// Get the width of label and resize the line
		Text text = new Text(String.valueOf(index));
		Scene s = new Scene(new Group(text));
		// Override the CSS style to calculate the text width
		text.setStyle("-fx-font-family: \"Myriad Pro\"; "
				+ "-fx-font-size: 16; ");
		text.applyCss();
		double textWidth = Math.ceil(text.getLayoutBounds().getWidth());
		indexBox.setMinWidth(textWidth + 2 * InterfaceController.MARGIN_TEXT_ELEMENT);
	}
}
