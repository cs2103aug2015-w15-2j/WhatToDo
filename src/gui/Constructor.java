package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import struct.View;

public class Constructor {

	// ======================================================================
    // Protected methods for initializing element components of all the views
    // Used in initDisplayElement()
    // ======================================================================
    
    /**
     * This method calls the correct initDataElement() method based on whether
     * the task/event data is marked with a status field or not
     * 
     * @param displayData
     * 		      The task/event data to be displayed in the window
     * @param numOfElements
     * 		      The total number of tasks/events. Used for formatting the index box
     * @param index
     * 		      The view index of the particular task/event
     * @param isTask
     * 		      A boolean flag indicating whether the data to be input is a task
     * @param displayDataSplit
     * 		      A String[] of displayData split by a period(.). Used in 
     * 			  initDisplayElement() for branching, reused to avoid recomputation
     * @return A HBox with only the task/event data formatted for insertion into the
     * 		   scroll pane
     */
    protected static HBox initDataElement(String displayData, int numOfElements, 
			int index, boolean isTask, String[] displayDataSplit, View targetView) {
    	
		// After removing the index, store it in the index map
		if (isStatusField(displayDataSplit[0].split(" ")[0])) {
			ViewIndexMap.add(targetView, Integer.parseInt(displayDataSplit[0].substring(5)));
		} else {
			ViewIndexMap.add(targetView, Integer.parseInt(displayDataSplit[0]));
		}
		
    	// Do a comparison on displayDataSplit to check if the returned data
    	// contains the "todo" or "done" field that requires indicator
    	String statusData = displayData.split(" ")[0];
    	if (statusData.equals(InterfaceController.STATUS_DONE)) {
    		return initDataElementWithStatus(
    				displayData, numOfElements, index, isTask, displayDataSplit, targetView);
    	} else {
    		return initDataElementNoStatus(
    				displayData, numOfElements, index, isTask, displayDataSplit, targetView);
    	}
    }
	
	/**
	 * This method creates a HBox and formats it for an empty result String
	 * ("There are no results to display.")
	 * 
	 * @param displayData
	 * 		      The task/event data to be displayed in the window
	 * @return A HBox with the empty result String formatted for insertion into the
	 * 		   scroll pane
	 */
    protected static HBox initNoResultElement(String displayData) {
		Label elementLabel = new Label(displayData);
		HBox labelBox = new HBox(elementLabel);
		HBox elementBox = new HBox(labelBox);
		
		formatLabelElement(elementLabel, labelBox);

		// Apply CSS style for regular data field
		elementBox.getStyleClass().add("element");
		return elementBox;
	}

	/**
	 * This method creates a HBox and formats it for a date or title String
	 * ("FLOAT", "SAT, 10 OCT 2015" etc...)
	 * 
	 * @param displayData
	 * 		      The task/event data to be displayed in the window
	 * @return A HBox with the date or title String formatted for insertion into the
	 * 		   scroll pane
	 */
    protected static HBox initTitleOrDateElement(String displayData) {
		Label elementLabel = new Label(displayData.toUpperCase());
		HBox elementBox = new HBox(elementLabel);
		Line elementLine = new Line(0, 0, InterfaceController.WIDTH_DEFAULT, 0);
		elementBox.getChildren().add(elementLine);
		
		formatTitleOrDateElement(elementLabel, elementBox, elementLine);
		
		// CSS
		elementLine.getStyleClass().add("line");
		elementBox.getStyleClass().add("element-title");
		return elementBox;
	}

    /**
     * This method creates a HBox and formats it for a task/event data String
     * 
     * @param displayData
     * 		      The task/event data to be displayed in the window
     * @param numOfElements
     * 		      The total number of tasks/events. Used for formatting the index box
     * @param index
     * 		      The view index of the particular task/event
     * @param isTask
     * 		      A boolean flag indicating whether the data to be input is a task
     * @param displayDataSplit
     * 		      A String[] of displayData split by a period(.). Used in 
     * 			  initDisplayElement() for branching, reused to avoid recomputation
     * @return A HBox with only the task/event data formatted for insertion into the
     * 		   scroll pane
     */
    protected static HBox initDataElementNoStatus(String displayData, int numOfElements, 
			int index, boolean isTask, String[] displayDataSplit, View targetView) {
		
		Label elementIndex = new Label(String.valueOf(index));
		Label elementLabel = new Label(displayData.replaceFirst(displayDataSplit[0] + ".", "").trim());
		HBox indexBox = new HBox(elementIndex);
		HBox labelBox = new HBox(elementLabel);
		HBox elementBox = new HBox(indexBox, labelBox);

		// Component formatting
		formatIndexElement(numOfElements, elementIndex, indexBox);
		formatLabelElement(elementLabel, labelBox);
		
		// CSS
		elementBox.getStyleClass().add("element");
		elementIndex.getStyleClass().add("element-index-label");
		
		if (targetView == View.DEFAULT) {
			indexBox.getStyleClass().add("element-index");
		} else if (isTask) {
			indexBox.getStyleClass().add("element-index-task");
		} else {
			indexBox.getStyleClass().add("element-index-event");
		}
		return elementBox;
	}
	
    /**
     * This method creates a HBox and formats it for a task/event data String. This method
     * will also append a tick to the end of the label to indicate completed status
     * 
     * @param displayData
     * 		      The task/event data to be displayed in the window
     * @param numOfElements
     * 		      The total number of tasks/events. Used for formatting the index box
     * @param index
     * 		      The view index of the particular task/event
     * @param isTask
     * 		      A boolean flag indicating whether the data to be input is a task
     * @param displayDataSplit
     * 		      A String[] of displayData split by a period(.). Used in 
     * 			  initDisplayElement() for branching, reused to avoid recomputation
     * @return A HBox with only the task/event data formatted for insertion into the
     * 		   scroll pane
     */
    protected static HBox initDataElementWithStatus(String displayData, int numOfElements, 
			int index, boolean isTask, String[] displayDataSplit, View targetView) {
		
		Label elementIndex = new Label(String.valueOf(index));
		Label elementLabel = new Label(displayData.replaceFirst(displayDataSplit[0] + ".", "").trim());
		ImageView elementTick = new ImageView(InterfaceController.PATH_TICK);
		HBox indexBox = new HBox(elementIndex);
		HBox labelBox = new HBox(elementLabel);
		HBox tickBox = new HBox(elementTick);
		HBox elementBox = new HBox(indexBox, labelBox, tickBox);

		// Component formatting
		formatIndexElement(numOfElements, elementIndex, indexBox);
		formatLabelElement(elementLabel, labelBox);
		formatTickElement(elementTick, tickBox);
		
		// CSS
		elementBox.getStyleClass().add("element");
		elementIndex.getStyleClass().add("element-index-label");
		
		if (targetView == View.DEFAULT) {
			indexBox.getStyleClass().add("element-index");
		} else if (isTask) {
			indexBox.getStyleClass().add("element-index-task");
		} else {
			indexBox.getStyleClass().add("element-index-event");
		}
		return elementBox;
	}
	
	/**
	 * This method formats the individual components of a HBox containing the
	 * index of the task/event
	 * 
	 * @param numOfElements
	 * 		      The total number of tasks/events. Used for formatting the index box
	 * @param elementIndex
	 * 		      The view index of the particular task/event
	 * @param indexBox
	 * 		      The HBox containing the index to be formatted
	 */
    protected static void formatIndexElement(int numOfElements, Label elementIndex, HBox indexBox) {
		setFitToMaxWidth(indexBox, numOfElements);
		indexBox.setAlignment(Pos.CENTER);
		HBox.setMargin(elementIndex, new Insets(
				InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
				InterfaceController.MARGIN_TEXT_ELEMENT, 
				InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
				InterfaceController.MARGIN_TEXT_ELEMENT));
	}
	
	/**
	 * This method formats the individual components of a HBox containing the
	 * task/event data
	 * 
	 * @param elementLabel
	 * 		      The Label containing the task/event data to be formatted
	 * @param labelBox
	 * 		      The HBox containing the elementLabel
	 */
    protected static void formatLabelElement(Label elementLabel, HBox labelBox) {
		elementLabel.setWrapText(true);
		HBox.setHgrow(labelBox, Priority.ALWAYS);
		HBox.setMargin(elementLabel, new Insets(
				InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
				InterfaceController.MARGIN_TEXT_ELEMENT, 
				InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
				InterfaceController.MARGIN_TEXT_ELEMENT));
	}

	/**
	 * This method formats the individual components of a HBox containing the 
	 * title or date element of the task/event
	 * 
	 * @param elementLabel
	 * 		      The Label containing the task/event data to be formatted
	 * @param elementBox
	 * 		      The HBox containing the elementLabel
	 * @param elementLine
	 * 		      The Line which is appended to the elementLabel in the 
	 * 		      elementBox as a vertical separator
	 */
    protected static void formatTitleOrDateElement(Label elementLabel, HBox elementBox, Line elementLine) {
		setFitLineToWidth(elementLabel, elementBox, elementLine);
		elementBox.setAlignment(Pos.CENTER_LEFT);
		HBox.setMargin(elementLabel, new Insets(
				0, InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
				0, InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT));
	}

	/**
	 * This method formats the individual components of a HBox containing the
	 * tick indicating completed status of a task/event
	 * 
	 * @param elementTick
	 * 		      The ImageView containing the tick image to be formatted
	 * @param tickBox
	 * 			  The HBox containing the elementTick
	 */
    protected static void formatTickElement(ImageView elementTick, HBox tickBox) {
		tickBox.setAlignment(Pos.CENTER);
		HBox.setMargin(elementTick, new Insets(0, InterfaceController.MARGIN_TICK, 
				0, InterfaceController.MARGIN_TICK));
	}
	
	/**
	 * This method calculates the maximum width used by the current largest index
	 * and sets the indexBox to that width to ensure consistency
	 * 
	 * @param indexBox
	 * 		      The HBox containing the index to be formatted
	 * @param numOfElements
	 * 		      The total number of tasks/events. Used for formatting the index box
	 */
    protected static void setFitToMaxWidth(HBox indexBox, int numOfElements) {
		Text text = new Text(String.valueOf(numOfElements));
		Scene s = new Scene(new Group(text));
		// Override the CSS style to calculate the text width
		text.setStyle("-fx-font-family: \"Myriad Pro\"; "
				+ "-fx-font-size: 16; ");
		text.applyCss();
		double textWidth = Math.ceil(text.getLayoutBounds().getWidth());
		indexBox.setMinWidth(textWidth + 2 * InterfaceController.MARGIN_TEXT_ELEMENT);
	}
	
	/**
	 * This method calculates the correct length of the separator line elementLine
	 * to fit the line length to width of the scroll pane
	 * 
	 * @param elementLabel
	 * 		      The Label containing the task/event data to be formatted
	 * @param elementBox
	 * 		      The HBox containing the elementLabel
	 * @param elementLine
	 * 		      The Line which is appended to the elementLabel in the 
	 * 		      elementBox as a vertical separator
	 */
    protected static void setFitLineToWidth(Label elementLabel, HBox elementBox, Line elementLine) {
		Text text = new Text(elementLabel.getText());
		Scene s = new Scene(new Group(text));
		text.setStyle("-fx-font-family: \"Myriad Pro\"; "
				+ "-fx-font-size: 16; "
				+ "-fx-font-weight: bold;");
		text.applyCss();
		// The arbitrary margin exists because text in a container is not perfectly 
		// aligned to the dimensions of its container
		double textWidth = Math.ceil(text.getLayoutBounds().getWidth());
		elementLine.endXProperty().bind(elementBox.widthProperty().subtract(
				textWidth + InterfaceController.MARGIN_ARBITRARY));
	}
	
	/**
	 * This method returns whether an input string contains a status field in the
	 * data ("todo" or "done")
	 * 
	 * @param data
	 * 		      The String to check if it matches either status field
	 * @return true if data equals to either "done" or "todo", false otherwise
	 */
    protected static boolean isStatusField(String data) {
		return data.equals(InterfaceController.STATUS_DONE) || 
				data.equals(InterfaceController.STATUS_TODO);
	}
}
