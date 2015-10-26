package gui;

import java.util.ArrayList;

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
import struct.View;

public class HistoryViewController {

	/* ================================================================================
     * JavaFX controls used in the general interface
     * ================================================================================
     */
	
	// Used for initAllTaskView
    private static VBox histBox, histContentBox;
    private static HBox histHeaderBox;
    private static ScrollPane histScroll;
    
    private static final String HEADER_HISTORY = "OPERATION HISTORY";
    private static final String MESSAGE_EMPTY_HIST = "No operations performed yet.";
    
    private static int messageIndex = 0;
    
    private static HBox initDisplayElement(String index, String feedbackMessage) {

    	Label feedbackIndex = new Label(index);
    	HBox indexBox = new HBox(feedbackIndex);
    	
    	Label feedbackLabel = new Label(feedbackMessage);
    	HBox feedbackBox = new HBox(indexBox, feedbackLabel);

    	// Set text wrapping for the feedback message
    	feedbackLabel.setWrapText(true);

    	// If is default and no commands entered, do not display background
    	if (index != "") {
    		// Set the margins of the index label within the HBox
    		HBox.setMargin(feedbackIndex, new Insets(
    				InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
    				InterfaceController.MARGIN_TEXT_ELEMENT, 
    				InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
    				InterfaceController.MARGIN_TEXT_ELEMENT));
    	}
    	
    	// Set the margins of the feedback label within the HBox
    	HBox.setMargin(feedbackLabel, new Insets(
    			InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
    			InterfaceController.MARGIN_TEXT_ELEMENT, 
    			InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
    			InterfaceController.MARGIN_TEXT_ELEMENT));

    	// Apply CSS style for regular data field
    	feedbackBox.getStyleClass().add("element");
    	indexBox.getStyleClass().add("element-index-history");
    	feedbackIndex.getStyleClass().add("element-index-label");
    	
    	return feedbackBox;
    }

    public static void initHistView() {
    	
    	Label histHeader = new Label(HEADER_HISTORY);
        histHeaderBox = new HBox(histHeader);
        histHeaderBox.setAlignment(Pos.CENTER);
        
        // History view is empty when first initialized,
        // add one message for no operations performed yet
        HBox initialBox = initDisplayElement("", MESSAGE_EMPTY_HIST);
        VBox.setMargin(initialBox, new Insets(
        		0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
        
        histContentBox = new VBox(initialBox);
        
        // Set the scroll pane to automatically scroll to the end
        histContentBox.heightProperty().addListener(
        		InterfaceController.logicControl.
        		getScrollListener(View.HISTORY));
        
        histScroll = new ScrollPane(histContentBox);
        histScroll.setFitToWidth(true);
        
        histBox = new VBox(histHeaderBox, histScroll);
        
        histBox.setAlignment(Pos.CENTER);
        
        // Set margins for the header label
        HBox.setMargin(histHeader, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        // Set margins for the header
        // Reducing the margin by line width to compensate for the added vertical
        // separator in the default and all views
        VBox.setMargin(histHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL - InterfaceController.WIDTH_VERT_LINE, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        // Set margins for the scroll pane
        VBox.setMargin(histScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL - InterfaceController.WIDTH_VERT_LINE, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));
        
        // Set the alignment of the header image to be in the center
        histBox.setAlignment(Pos.CENTER);
        
        // Set the height of the scroll pane to grow with window height
        VBox.setVgrow(histScroll, Priority.ALWAYS);
        
        // Set the scrollbar policy of the scroll pane
        histScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // Update the history view in InterfaceController
        InterfaceController.histBox = new HBox(histBox);
        
        // Set the preferred viewport width for the history scroll pane
        histScroll.prefViewportWidthProperty().bind(InterfaceController.histBox.widthProperty());
        
        // CSS
        histHeader.getStyleClass().add("box-title-label");
        histHeaderBox.getStyleClass().add("box-title-history");
    }
    
    public static void updateHistView(String feedbackMessage) {
    	
        // Increment the message index
        messageIndex++;
        
        // Use a temporary component for formatting
        HBox tempBox = initDisplayElement(String.valueOf(messageIndex), feedbackMessage);
        VBox.setMargin(tempBox, new Insets(
        		0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
        
        // Replace the initial message if this message is the first to be added
        if (messageIndex == 1) {
        	histContentBox.getChildren().clear();
        }
        
        histContentBox.getChildren().add(tempBox);
    }
    
    /* ================================================================================
     * Getters for LogicController to access required JavaFX components
     * ================================================================================
     */
    
    public static ScrollPane getHistScroll() {
    	return histScroll;
    }
}
