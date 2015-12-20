/**
 * This class initializes the summary view of the application and provides methods 
 * that update the view when the file has been edited by the user.
 * 
 * @@author A0124123Y
 */

package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SummaryViewController {
	
	// Used for initTaskTwoDays
	private static Label taskTwoDaysCount, taskTwoDaysLabel;
	private static HBox taskTwoDaysCountBox, taskTwoDaysLabelBox, taskTwoDaysBox;
	
	// Used for initEventTwoDays
	private static Label eventTwoDaysCount, eventTwoDaysLabel;
	private static HBox eventTwoDaysCountBox, eventTwoDaysLabelBox, eventTwoDaysBox;
	
	// Used for initTaskFloat
	private static Label taskFloatCount, taskFloatLabel;
	private static HBox taskFloatCountBox, taskFloatLabelBox, taskFloatBox;
	
	// Used for initEventOngoing
	private static Label eventOngoingCount, eventOngoingLabel;
	private static HBox eventOngoingCountBox, eventOngoingLabelBox, eventOngoingBox;
	
	// Used for initAllUnres
	private static Label allUnresCount, allUnresLabel, allUnresAttention, allUnresClear;
	private static ImageView allUnresIcon;
	private static HBox allUnresCountBox, allUnresLabelBox, allUnresNotifyBox,
	allUnresIconBox, allUnresBox;
	
	// Used for initSummaryView
	private static VBox summaryBox;
	
	private static final String PATH_UNRES_TICK = "gui/resources/unres_tick.png";
	private static final String PATH_UNRES_ALERT = "gui/resources/unres_alert.png";
	
	private static final String HEADER_TASK_THREE_DAYS = "Tasks due within three days";
	private static final String HEADER_EVENT_THREE_DAYS = "Events starting within three days";
	private static final String HEADER_TASK_FLOAT = "Tasks with no deadline";
	private static final String HEADER_EVENT_ONGOING = "Ongoing events";
	private static final String HEADER_ALL_UNRES = "Unresolved tasks/events past deadline";
	private static final String HEADER_ALL_UNRES_ATTENTION = "Some items require attention";
	private static final String HEADER_ALL_UNRES_CLEAR = "All items clear";
	
	private static final double MARGIN_SUMMARY_BOX = 80;
	private static final double MARGIN_SUMMARY_LABEL = 30;
	private static final double MARGIN_SUMMARY_COUNT_HORIZ = 20;
	private static final double MARGIN_SUMMARY_COUNT_VERT = 5;
	
	private static final int INDEX_TASK_TWO_DAYS = 0;
	private static final int INDEX_EVENT_TWO_DAYS = 1;
	private static final int INDEX_TASK_FLOAT = 2;
	private static final int INDEX_EVENT_ONGOING = 3;
	private static final int INDEX_UNRES = 4;
	private static final int ARRAY_LENGTH = 5;
	
	private static boolean isShowing = false;
	
    /**
     * This method initializes all the interface components for the default view,
     * primarily the five display bars
     */
	protected static void initSummaryView() {
		int[] summary = InterfaceController.getLogic().getSummaryCount();
		assert summary.length == ARRAY_LENGTH;
		double maxWidth = getCountMaxWidth(summary);
		
		initTaskTwoDays(summary[INDEX_TASK_TWO_DAYS], maxWidth);
		initEventTwoDays(summary[INDEX_EVENT_TWO_DAYS], maxWidth);
		initTaskFloat(summary[INDEX_TASK_FLOAT], maxWidth);
		initEventOngoing(summary[INDEX_EVENT_ONGOING], maxWidth);
		initAllUnres(summary[INDEX_UNRES], maxWidth);
		
		Region space1 = new Region();
		Region space2 = new Region();
		Region space3 = new Region();
		Region space4 = new Region();
		Region space5 = new Region();
		Region space6 = new Region();
		
		summaryBox = new VBox(space1, taskTwoDaysBox, 
				space2, eventTwoDaysBox, 
				space3, taskFloatBox, 
				space4, eventOngoingBox, 
				space5, allUnresBox, 
				space6);
		InterfaceController.summaryBox = summaryBox;
		
		// Component formatting
		formatSummaryBox();
		
		VBox.setVgrow(space1, Priority.ALWAYS);
		VBox.setVgrow(space2, Priority.ALWAYS);
		VBox.setVgrow(space3, Priority.ALWAYS);
		VBox.setVgrow(space4, Priority.ALWAYS);
		VBox.setVgrow(space5, Priority.ALWAYS);
		VBox.setVgrow(space6, Priority.ALWAYS);
	}
	
    /**
     * This method updates the summary view with data from the text file
     * 
     * Called by:
     * 	1. 	runCommand() in LogicController to update the view every time an 
     * 		operation is performed
     * 	2. 	updateMainInterface() in InterfaceController to update the view when
     * 		a view change command is issued (button/hotkey/text command)
     */
	protected static void updateSummaryView() {
		int[] summary = InterfaceController.getLogic().getSummaryCount();
		assert summary.length == ARRAY_LENGTH;
		
		// Clear the old data
		taskTwoDaysCountBox.getChildren().clear();
		eventTwoDaysCountBox.getChildren().clear();
		taskFloatCountBox.getChildren().clear();
		eventOngoingCountBox.getChildren().clear();
		allUnresCountBox.getChildren().clear();
		
		taskTwoDaysCount = new Label(String.valueOf(summary[INDEX_TASK_TWO_DAYS]));
		eventTwoDaysCount = new Label(String.valueOf(summary[INDEX_EVENT_TWO_DAYS]));
		taskFloatCount = new Label(String.valueOf(summary[INDEX_TASK_FLOAT]));
		eventOngoingCount = new Label(String.valueOf(summary[INDEX_EVENT_ONGOING]));
		allUnresCount = new Label(String.valueOf(summary[INDEX_UNRES]));
		
		// Update the icon depending on the new value of unresolved tasks
		initUnresIcon(summary[INDEX_UNRES]);
		allUnresIconBox.getChildren().clear();
		allUnresIconBox.getChildren().add(allUnresIcon);
		HBox.setMargin(allUnresIcon, new Insets(0, MARGIN_SUMMARY_COUNT_HORIZ, 0, 0));
		
		updateUnresNotifyBox(summary[INDEX_UNRES]);
		
		// Insert the new data
		taskTwoDaysCountBox.getChildren().add(taskTwoDaysCount);
		eventTwoDaysCountBox.getChildren().add(eventTwoDaysCount);
		taskFloatCountBox.getChildren().add(taskFloatCount);
		eventOngoingCountBox.getChildren().add(eventOngoingCount);
		allUnresCountBox.getChildren().add(allUnresCount);
		
		// Component formatting
		HBox.setMargin(taskTwoDaysCount, new Insets(
				MARGIN_SUMMARY_COUNT_VERT, 0, MARGIN_SUMMARY_COUNT_VERT, 0));
		HBox.setMargin(eventTwoDaysCount, new Insets(
				MARGIN_SUMMARY_COUNT_VERT, 0, MARGIN_SUMMARY_COUNT_VERT, 0));
		HBox.setMargin(taskFloatCount, new Insets(
				MARGIN_SUMMARY_COUNT_VERT, 0, MARGIN_SUMMARY_COUNT_VERT, 0));
		HBox.setMargin(eventOngoingCount, new Insets(
				MARGIN_SUMMARY_COUNT_VERT, 0, MARGIN_SUMMARY_COUNT_VERT, 0));
		HBox.setMargin(allUnresCount, new Insets(
				MARGIN_SUMMARY_COUNT_VERT, 0, MARGIN_SUMMARY_COUNT_VERT, 0));
		
		// CSS
		taskTwoDaysCount.getStyleClass().add("summary-box-count");
		eventTwoDaysCount.getStyleClass().add("summary-box-count");
		taskFloatCount.getStyleClass().add("summary-box-count");
		eventOngoingCount.getStyleClass().add("summary-box-count");
		allUnresCount.getStyleClass().add("summary-box-count");
	}
	
	// ========================================
	// Getters and setters for isShowing
	// ========================================
	
	protected static boolean isShowing() {
		return isShowing;
	}
	
	protected static void startShowing() {
		isShowing = true;
	}
	
	protected static void stopShowing() {
		isShowing = false;
	}
	
    // ================================================================================
    // Private methods, used to initialize various sub components of the interface
    // ================================================================================
	
	private static void initTaskTwoDays(int count, double maxWidth) {
		taskTwoDaysCount = new Label(String.valueOf(count));
		taskTwoDaysLabel = new Label(HEADER_TASK_THREE_DAYS);
		
		taskTwoDaysCountBox = new HBox(taskTwoDaysCount);
		taskTwoDaysLabelBox = new HBox(taskTwoDaysLabel);
		taskTwoDaysCountBox.setAlignment(Pos.CENTER);
		taskTwoDaysLabelBox.setAlignment(Pos.CENTER_LEFT);
		
		taskTwoDaysCountBox.setMinWidth(maxWidth);
		HBox.setMargin(taskTwoDaysLabel, new Insets(0, 0, 0, MARGIN_SUMMARY_LABEL));
		HBox.setHgrow(taskTwoDaysLabelBox, Priority.ALWAYS);
		
		taskTwoDaysBox = new HBox(taskTwoDaysLabelBox, taskTwoDaysCountBox);
		
		// CSS
		taskTwoDaysBox.getStyleClass().add("summary-box");
		taskTwoDaysCount.getStyleClass().add("summary-box-count");
		taskTwoDaysLabel.getStyleClass().add("summary-box-label");
		taskTwoDaysLabel.setStyle("-fx-font-family: \"Myriad Pro Light\";");
		
		taskTwoDaysLabelBox.getStyleClass().add("summary-box-label-box");
		taskTwoDaysCountBox.getStyleClass().add("summary-box-count-box");
	}
	
	private static void initEventTwoDays(int count, double maxWidth) {
		eventTwoDaysCount = new Label(String.valueOf(count));
		eventTwoDaysLabel = new Label(HEADER_EVENT_THREE_DAYS);
		
		eventTwoDaysCountBox = new HBox(eventTwoDaysCount);
		eventTwoDaysLabelBox = new HBox(eventTwoDaysLabel);
		eventTwoDaysCountBox.setAlignment(Pos.CENTER);
		eventTwoDaysLabelBox.setAlignment(Pos.CENTER_LEFT);
		
		eventTwoDaysCountBox.setMinWidth(maxWidth);
		HBox.setMargin(eventTwoDaysLabel, new Insets(0, 0, 0, MARGIN_SUMMARY_LABEL));
		HBox.setHgrow(eventTwoDaysLabelBox, Priority.ALWAYS);
		
		eventTwoDaysBox = new HBox(eventTwoDaysLabelBox, eventTwoDaysCountBox);

		// CSS
		eventTwoDaysBox.getStyleClass().add("summary-box");
		eventTwoDaysCount.getStyleClass().add("summary-box-count");
		eventTwoDaysLabel.getStyleClass().add("summary-box-label");
		eventTwoDaysLabel.setStyle("-fx-font-family: \"Myriad Pro Light\";");
		
		eventTwoDaysLabelBox.getStyleClass().add("summary-box-label-box");
		eventTwoDaysCountBox.getStyleClass().add("summary-box-count-box");
	}
	
	private static void initTaskFloat(int count, double maxWidth) {
		taskFloatCount = new Label(String.valueOf(count));
		taskFloatLabel = new Label(HEADER_TASK_FLOAT);
		
		taskFloatCountBox = new HBox(taskFloatCount);
		taskFloatLabelBox = new HBox(taskFloatLabel);
		taskFloatCountBox.setAlignment(Pos.CENTER);
		taskFloatLabelBox.setAlignment(Pos.CENTER_LEFT);
		
		taskFloatCountBox.setMinWidth(maxWidth);
		HBox.setMargin(taskFloatLabel, new Insets(0, 0, 0, MARGIN_SUMMARY_LABEL));
		HBox.setHgrow(taskFloatLabelBox, Priority.ALWAYS);
		
		taskFloatBox = new HBox(taskFloatLabelBox, taskFloatCountBox);
		
		// CSS
		taskFloatBox.getStyleClass().add("summary-box");
		taskFloatCount.getStyleClass().add("summary-box-count");
		taskFloatLabel.getStyleClass().add("summary-box-label");
		taskFloatLabel.setStyle("-fx-font-family: \"Myriad Pro Light\";");
		
		taskFloatLabelBox.getStyleClass().add("summary-box-label-box");
		taskFloatCountBox.getStyleClass().add("summary-box-count-box");
	}
	
	private static void initEventOngoing(int count, double maxWidth) {
		eventOngoingCount = new Label(String.valueOf(count));
		eventOngoingLabel = new Label(HEADER_EVENT_ONGOING);
		
		eventOngoingCountBox = new HBox(eventOngoingCount);
		eventOngoingLabelBox = new HBox(eventOngoingLabel);
		eventOngoingCountBox.setAlignment(Pos.CENTER);
		eventOngoingLabelBox.setAlignment(Pos.CENTER_LEFT);
		
		eventOngoingCountBox.setMinWidth(maxWidth);
		HBox.setMargin(eventOngoingLabel, new Insets(0, 0, 0, MARGIN_SUMMARY_LABEL));
		HBox.setHgrow(eventOngoingLabelBox, Priority.ALWAYS);
		
		eventOngoingBox = new HBox(eventOngoingLabelBox, eventOngoingCountBox);
		
		// CSS
		eventOngoingBox.getStyleClass().add("summary-box");
		eventOngoingCount.getStyleClass().add("summary-box-count");
		eventOngoingLabel.getStyleClass().add("summary-box-label");
		eventOngoingLabel.setStyle("-fx-font-family: \"Myriad Pro Light\";");
		
		eventOngoingLabelBox.getStyleClass().add("summary-box-label-box");
		eventOngoingCountBox.getStyleClass().add("summary-box-count-box");
	}
	
	private static void initAllUnres(int count, double maxWidth) {
		allUnresCount = new Label(String.valueOf(count));
		allUnresLabel = new Label(HEADER_ALL_UNRES);
		allUnresAttention = new Label(HEADER_ALL_UNRES_ATTENTION);
		allUnresClear = new Label(HEADER_ALL_UNRES_CLEAR);
		initUnresIcon(count);
		
		allUnresCountBox = new HBox(allUnresCount);
		allUnresLabelBox = new HBox(allUnresLabel);
		allUnresIconBox = new HBox(allUnresIcon);
		initUnresNotifyBox(count);
		
		allUnresBox = new HBox(allUnresLabelBox, allUnresNotifyBox, 
				allUnresIconBox, allUnresCountBox);
		
		// Add event handling for mouse clicks
		allUnresAttention.addEventHandler(MouseEvent.MOUSE_ENTERED, 
				Handlers.getUnresHoverHandler(allUnresAttention));
		allUnresAttention.addEventHandler(MouseEvent.MOUSE_EXITED, 
				Handlers.getUnresHoverHandler(allUnresAttention));
		allUnresAttention.addEventHandler(MouseEvent.MOUSE_CLICKED, 
				Handlers.getUnresClickHandler());
		
		// Component formatting
		allUnresCountBox.setAlignment(Pos.CENTER);
		allUnresLabelBox.setAlignment(Pos.CENTER_LEFT);
		allUnresIconBox.setAlignment(Pos.CENTER);
		allUnresNotifyBox.setAlignment(Pos.CENTER_LEFT);
		
		allUnresCountBox.setMinWidth(maxWidth);
		HBox.setMargin(allUnresLabel, new Insets(0, 0, 0, MARGIN_SUMMARY_LABEL));
		HBox.setMargin(allUnresAttention, new Insets(0, 0, 0, MARGIN_SUMMARY_COUNT_HORIZ));
		HBox.setMargin(allUnresClear, new Insets(0, 0, 0, MARGIN_SUMMARY_COUNT_HORIZ));
		HBox.setMargin(allUnresIcon, new Insets(0, MARGIN_SUMMARY_COUNT_HORIZ, 0, 0));
		HBox.setHgrow(allUnresNotifyBox, Priority.ALWAYS);
		
		// CSS
		allUnresBox.getStyleClass().add("summary-box");
		allUnresCount.getStyleClass().add("summary-box-count");
		allUnresLabel.getStyleClass().add("summary-box-label");
		allUnresAttention.getStyleClass().add("summary-box-notify");
		allUnresClear.getStyleClass().add("summary-box-notify");
		allUnresLabel.setStyle("-fx-font-family: \"Myriad Pro Light\";");
		allUnresAttention.setStyle("-fx-font-family: \"Myriad Pro Light\"; "
				+ "-fx-font-style: italic;");
		allUnresClear.setStyle("-fx-font-family: \"Myriad Pro Light\"; "
				+ "-fx-font-style: italic;");
		
		allUnresLabelBox.getStyleClass().add("summary-box-label-box");
		allUnresIconBox.getStyleClass().add("summary-box-label-box");
		allUnresCountBox.getStyleClass().add("summary-box-count-box");
		allUnresNotifyBox.getStyleClass().add("summary-box-notify-box");
	}
	
    // ================================================================================
    // Private methods, used to initialize and update the unresolved bar
    // ================================================================================
	
	private static void initUnresIcon(int count) {
		if (count == 0) {
			allUnresIcon = new ImageView(PATH_UNRES_TICK);
		} else {
			allUnresIcon = new ImageView(PATH_UNRES_ALERT);
		}
	}
	
	private static void initUnresNotifyBox(int count) {
		if (count == 0) {
			allUnresNotifyBox = new HBox(allUnresClear);
		} else {
			allUnresNotifyBox = new HBox(allUnresAttention);
		}
	}
	
	private static void updateUnresNotifyBox(int count) {
		allUnresNotifyBox.getChildren().clear();
		if (count == 0) {
			allUnresNotifyBox.getChildren().add(allUnresClear);
		} else {
			allUnresNotifyBox.getChildren().add(allUnresAttention);
		}
	}
	
    // ================================================================================
    // Misc private methods used to perform calculations and formatting
    // ================================================================================
	
	/**
	 * This method returns the width of the largest number that will be displayed 
	 * in the summary view
	 * 
	 * @param summary
	 * 			  The array of values to be used in the summary view
	 * @return The width of the largest number to be used in the summary view
	 */
	private static double getCountMaxWidth(int[] summary) {
		// Obtain the largest value within summary
		int maxValue = summary[0];
		for (int i = 1; i < 5; i++) {
			maxValue = Math.max(maxValue, summary[i]);
		}
		
		Text text = new Text(String.valueOf(maxValue));
		new Scene(new Group(text));
		text.setStyle("-fx-font-family: \"Myriad Pro Light\";"
				+ "-fx-font-size: 48;");
		text.applyCss();
		double maxWidth = text.getLayoutBounds().getWidth() + 2 * MARGIN_SUMMARY_COUNT_HORIZ;
		return maxWidth;
	}

	/**
	 * This method formats all five HBoxes used in the summary view during initialization
	 */
	private static void formatSummaryBox() {
		HBox.setHgrow(taskTwoDaysBox, Priority.ALWAYS);
		HBox.setHgrow(eventTwoDaysBox, Priority.ALWAYS);
		HBox.setHgrow(taskFloatBox, Priority.ALWAYS);
		HBox.setHgrow(eventOngoingBox, Priority.ALWAYS);
		HBox.setHgrow(allUnresBox, Priority.ALWAYS);
		
		VBox.setMargin(taskTwoDaysBox, new Insets(0, MARGIN_SUMMARY_BOX, 0, MARGIN_SUMMARY_BOX));
		VBox.setMargin(eventTwoDaysBox, new Insets(0, MARGIN_SUMMARY_BOX, 0, MARGIN_SUMMARY_BOX));
		VBox.setMargin(taskFloatBox, new Insets(0, MARGIN_SUMMARY_BOX, 0, MARGIN_SUMMARY_BOX));
		VBox.setMargin(eventOngoingBox, new Insets(0, MARGIN_SUMMARY_BOX, 0, MARGIN_SUMMARY_BOX));
		VBox.setMargin(allUnresBox, new Insets(0, MARGIN_SUMMARY_BOX, 0, MARGIN_SUMMARY_BOX));
	}
}
