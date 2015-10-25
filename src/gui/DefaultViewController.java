package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class DefaultViewController {

	 /* ================================================================================
     * JavaFX controls used in the general interface
     * ================================================================================
     */

	// Used for initSummaryHeader
	private static VBox summaryHeaderBox;
	private static Label summaryHeaderLabel, summaryYouLabel;
	
	// Used for initSummaryContent
	private static VBox summaryContentBox;
	private static Label soonTasksLabel, floatTasksLabel, soonEventsLabel, ongoingEventsLabel;
	
	// Used for initContent
	private static ScrollPane contentScroll;
	private static VBox contentBox;
	
	// Used for initDefaultView
	private static VBox defView;
	
	private static void initSummaryHeader() {
		
		summaryHeaderLabel = new Label("A summary of your upcoming tasks and events");
		summaryHeaderLabel.setStyle("-fx-font-family: \"Roboto LT\";");
		summaryYouLabel = new Label("You have:");
		summaryYouLabel.setStyle("-fx-font-family: \"Roboto LT\";");
		
		summaryHeaderBox = new VBox(summaryHeaderLabel, summaryYouLabel);
		summaryHeaderBox.setAlignment(Pos.CENTER);
		
		VBox.setMargin(summaryHeaderLabel, new Insets(10, 0, 0, 0));
		VBox.setMargin(summaryYouLabel, new Insets(10, 0, 10, 0));
		
		// CSS
		summaryHeaderLabel.getStyleClass().add("def-summary-header");
		summaryYouLabel.getStyleClass().add("def-summary-you");
	}
	
	private static void initSummaryContent() {
		
		int soonTasks = 3;
		int floatTasks = 3;
		int soonEvents = 3;
		int ongoingEvents = 3;
		
		floatTasksLabel = new Label(floatTasks + " tasks without deadlines.");
		soonTasksLabel = new Label(soonTasks + " tasks due within the next two days.");
		ongoingEventsLabel = new Label(ongoingEvents + " ongoing events.");
		soonEventsLabel = new Label(soonEvents + " events starting within the next two days.");
		
		summaryContentBox = new VBox(floatTasksLabel, soonTasksLabel, ongoingEventsLabel, soonEventsLabel);
		summaryContentBox.setAlignment(Pos.CENTER);
		
		VBox.setMargin(floatTasksLabel, new Insets(5, 0, 0, 0));
		VBox.setMargin(soonEventsLabel, new Insets(0, 0, 5, 0));
		
		// CSS
		floatTasksLabel.getStyleClass().add("def-summary-content-label");
		soonTasksLabel.getStyleClass().add("def-summary-content-label");
		ongoingEventsLabel.getStyleClass().add("def-summary-content-label");
		soonEventsLabel.getStyleClass().add("def-summary-content-label");
		
		summaryContentBox.getStyleClass().add("def-summary-content");
	}
	
    private static boolean isTitle(String displayData) {
    	
    	String firstWord = displayData.split(" ")[0];
    	return firstWord.equals("FLOAT") || firstWord.equals("TODAY") || 
    			firstWord.equals("TOMORROW") || firstWord.equals("ONGOING");
    }
    
    private static String formatDeadline(String deadline) {
    	
    	String[] splitDeadline = deadline.split(" ");
    	
    	switch(splitDeadline[0]) {
    	case "FLOAT":
    		return "No deadline set.";
    	case "TODAY":
    		return "Due today, " + deadline.replaceFirst("TODAY - ", "");
    	case "TOMORROW":
    		return "Due tomorrow, " + deadline.replaceFirst("TOMORROW - ", "");
    	case "ONGOING":
    		return "Currently ongoing";
    	default:
    		// Do nothing
    		return "";
    	}
    }
    
    private static HBox initContentElement(String displayData, String deadline) {
    	
    	String[] splitDisplayData = displayData.split(Pattern.quote("."));
    	
    	// Extract the element index
    	String elementIndex = splitDisplayData[0];
    	
    	Label elementIndexLabel = new Label(elementIndex);
    	HBox elementIndexBox = new HBox(elementIndexLabel);
    	elementIndexBox.setAlignment(Pos.CENTER);
    	elementIndexBox.setMinWidth(75);
    	elementIndexBox.setMaxWidth(75);
    	
    	Label elementLabel = new Label(splitDisplayData[1].trim());
    	elementLabel.setWrapText(true);
    	
    	Label elementDeadline = new Label(formatDeadline(deadline));
    	
    	HBox elementBox = new HBox(elementIndexBox, elementLabel, elementDeadline);
    	elementBox.setAlignment(Pos.CENTER_LEFT);
    	
    	elementLabel.prefWidthProperty().bind(elementBox.widthProperty().divide(2));
    	HBox.setHgrow(elementDeadline, Priority.ALWAYS);
    	HBox.setMargin(elementDeadline, new Insets(0, 0, 0, 20));
    	HBox.setMargin(elementLabel, new Insets(5, 0, 5, 0));
    	
    	// CSS
    	elementIndexLabel.getStyleClass().add("def-content");
    	elementLabel.getStyleClass().add("def-content");
    	elementDeadline.getStyleClass().add("def-content");
    	
    	return elementBox;
    }
    
	private static void initContent() {
		
		ArrayList<String> defTasks = InterfaceController.logicControl.getDefTasks();
		ArrayList<String> defEvents = InterfaceController.logicControl.getDefEvents();
		
		contentBox = new VBox();
		
		String deadline = null;
		int altCount = 0;
		for (int i = 0; i < defTasks.size(); i++) {
			if (isTitle(defTasks.get(i))) {
				deadline = defTasks.get(i);
			} else {
				if (defTasks.get(i).split(Pattern.quote(".")).length > 1) {
					HBox temp = initContentElement(defTasks.get(i), deadline);
					if (altCount % 2 == 0) {
						temp.getStyleClass().add("def-content-alt");
					}
					altCount++;
					contentBox.getChildren().add(temp);
				}
			}
		}
		
		deadline = null;
		for (int i = 0; i < defEvents.size(); i++) {
			if (isTitle(defEvents.get(i))) {
				deadline = defEvents.get(i);
			} else {
				if (defEvents.get(i).split(Pattern.quote(".")).length > 1) {
					HBox temp = initContentElement(defEvents.get(i), deadline);
					contentBox.getChildren().add(temp);
				}
			}
		}
		
		contentScroll = new ScrollPane(contentBox);
		contentScroll.setVbarPolicy(ScrollBarPolicy.NEVER);
		contentScroll.setFitToWidth(true);
	}
	
	public static void initDefView() {
		
		initSummaryHeader();
		initSummaryContent();
		initContent();
		
		defView = new VBox(summaryHeaderBox, summaryContentBox, contentScroll);
		defView.setAlignment(Pos.TOP_CENTER);
		HBox.setHgrow(defView, Priority.ALWAYS);
		VBox.setMargin(summaryContentBox, new Insets(0, 0, 20, 0));
		
		InterfaceController.defBox = new HBox(defView);
		//summaryContentBox.maxWidthProperty().bind(defView.widthProperty().multiply(0.7));
	}
}