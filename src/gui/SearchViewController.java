package gui;

import java.util.ArrayList;
import java.util.regex.Pattern;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import struct.View;

public class SearchViewController {


	/* ================================================================================
	 * JavaFX controls used in the general interface
	 * ================================================================================
	 */

	// Used for initSearchTaskView
	private static VBox searchTaskBox, searchTaskContentBox;
	private static HBox searchTaskHeaderBox;
	private static ScrollPane searchTaskScroll;

	// Used for initSearchEventView
	private static VBox searchEventBox, searchEventContentBox;
	private static HBox searchEventHeaderBox;
	private static ScrollPane searchEventScroll;

	// Used for initSearchView
	private static Line searchScrollLine;

	protected static final String HEADER_SEARCH = "RESULTS FOR:";
	protected static final String HEADER_SEARCH_TASKS = "TASKS";
	protected static final String HEADER_SEARCH_EVENTS = "EVENTS";

	private static ArrayList<String> getTaskResults(String displayData) {

		String[] resultsSplit = displayData.split("\n");
		ArrayList<String> taskResults = new ArrayList<String>();
		boolean startRead = false;

		for (int i = 0; i < resultsSplit.length; i++) {
			if (resultsSplit[i].equals("TASK")) {
				startRead = true;
			} else if (resultsSplit[i].equals("EVENT")) {
				startRead = false;
				break;
			} else {
				if (startRead) {
					taskResults.add(resultsSplit[i]);
				}
			}
		}
		return taskResults;
	}

	private static ArrayList<String> getEventResults(String displayData) {

		String[] resultsSplit = displayData.split("\n");
		ArrayList<String> eventResults = new ArrayList<String>();
		boolean startRead = false;

		for (int i = 0; i < resultsSplit.length; i++) {
			if (resultsSplit[i].equals("EVENT")) {
				startRead = true;
			} else {
				if (startRead) {
					// Filter through the array and reformat the data
					for (int j = 0; j < resultsSplit.length; j++) {
						resultsSplit[i] = resultsSplit[i].replace(';', '\n');
					}
					eventResults.add(resultsSplit[i]);
				}
			}
		}
		return eventResults;
	}

	private static void initSearchTaskView(ArrayList<String> taskResults, int numOfElements) {

		Label searchTaskHeaderLabel = new Label(HEADER_SEARCH_TASKS);
		searchTaskHeaderBox = new HBox(searchTaskHeaderLabel);
		searchTaskHeaderBox.setAlignment(Pos.CENTER);

		searchTaskContentBox = new VBox();

		// Loop for the floats
		for (int i = 0; i < taskResults.size(); i++) {
			HBox tempBox = InterfaceController.initDisplayElement(taskResults.get(i), numOfElements, i + 1, true, View.SEARCH);
			VBox.setMargin(tempBox, new Insets(
					0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
			searchTaskContentBox.getChildren().add(tempBox);
		}

		searchTaskScroll = new ScrollPane(searchTaskContentBox);
		searchTaskScroll.setFitToWidth(true);
		
        // Set the scrollbar policy of the scroll pane
		searchTaskScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		searchTaskScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		searchTaskBox = new VBox(searchTaskHeaderBox, searchTaskScroll);
		
        // Set margins for the header label
        HBox.setMargin(searchTaskHeaderLabel, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        // Set margins for the header
        VBox.setMargin(searchTaskHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        // Set margins for the scroll pane
        VBox.setMargin(searchTaskScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));
        
        // Set the alignment of the header image to be in the center
        searchTaskBox.setAlignment(Pos.CENTER);
        
        // Set the height of the scroll pane to grow with window height
        VBox.setVgrow(searchTaskScroll, Priority.ALWAYS);
        
        // CSS
        searchTaskHeaderLabel.getStyleClass().add("box-title-label");
        searchTaskHeaderBox.getStyleClass().add("box-title-all-task");
	}

	private static void initSearchEventView(ArrayList<String> eventResults, int numOfElements) {

		Label searchEventHeaderLabel = new Label(HEADER_SEARCH_EVENTS);
		searchEventHeaderBox = new HBox(searchEventHeaderLabel);
		searchEventHeaderBox.setAlignment(Pos.CENTER);

		searchEventContentBox = new VBox();

		// Loop for the floats
		for (int i = 0; i < eventResults.size(); i++) {
			HBox tempBox = InterfaceController.initDisplayElement(eventResults.get(i), numOfElements, i + 1, false, View.SEARCH);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
			searchEventContentBox.getChildren().add(tempBox);
		}

		searchEventScroll = new ScrollPane(searchEventContentBox);
		searchEventScroll.setFitToWidth(true);
		
        // Set the scrollbar policy of the scroll pane
		searchEventScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		searchEventScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		searchEventBox = new VBox(searchEventHeaderBox, searchEventScroll);
		
        // Set margins for the header label
        HBox.setMargin(searchEventHeaderLabel, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        // Set margins for the header
        VBox.setMargin(searchEventHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        // Set margins for the scroll pane
        VBox.setMargin(searchEventScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));
        
        // Set the alignment of the header image to be in the center
        searchEventBox.setAlignment(Pos.CENTER);
        
        // Set the height of the scroll pane to grow with window height
        VBox.setVgrow(searchEventScroll, Priority.ALWAYS);
        
        // CSS
        searchEventHeaderLabel.getStyleClass().add("box-title-label");
        searchEventHeaderBox.getStyleClass().add("box-title-all-event");
	}

	public static void initSearchView() {
		ArrayList<String> initialArray = new ArrayList<String>();

		initSearchTaskView(initialArray, 0);
		initSearchEventView(initialArray, 0);

		searchScrollLine = new Line(0, 0, 0, InterfaceController.WIDTH_DEFAULT_BUTTON);

		HBox searchBoxNoHeader = new HBox(searchTaskBox, searchScrollLine, searchEventBox);
        
		InterfaceController.searchBox = new VBox(searchBoxNoHeader);
		
        // Set the preferred viewport width of the two scroll panes to be half
        // of the entire view pane
		searchTaskScroll.prefViewportWidthProperty().bind(
        		InterfaceController.searchBox.widthProperty().divide(2));
        searchEventScroll.prefViewportWidthProperty().bind(
        		InterfaceController.searchBox.widthProperty().divide(2));
        
        // Fix the width of the scroll panes to prevent resize of the inner labels
        searchTaskScroll.maxWidthProperty().bind(
        		InterfaceController.searchBox.widthProperty().divide(2));
        searchEventScroll.maxWidthProperty().bind(
        		InterfaceController.searchBox.widthProperty().divide(2));
        
        // Bind the height of the separator line to the default line height minus the height
        // of the header
        searchScrollLine.endYProperty().bind(
        		DefaultViewController.getDefScrollLine().endYProperty());
        
        // CSS
        searchScrollLine.getStyleClass().add("line");
	}

	public static void updateSearchView(String results) {
		ArrayList<String> taskResults = getTaskResults(results);
		ArrayList<String> eventResults = getEventResults(results);

		searchTaskContentBox.getChildren().clear();
		searchEventContentBox.getChildren().clear();
		ViewIndexMap.resetSearchMap();
		
		int numOfElements = InterfaceController.getLogic().getSearchElementsCount(taskResults, eventResults);
		
		// Print the task results
		// Only print the empty message if there are zero results
		int numOfResults = 1;
		if (taskResults.size() == 3 && InterfaceController.getLogic().isEmpty(taskResults.get(2))) {
			HBox tempBox = InterfaceController.initDisplayElement(taskResults.get(2), numOfElements, numOfResults, true, View.SEARCH);
			VBox.setMargin(tempBox, new Insets(
					0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
			searchTaskContentBox.getChildren().add(tempBox);
		} else {
			// If there are no results for floating tasks
			if (InterfaceController.getLogic().isEmpty(taskResults.get(taskResults.size() - 1))) {
				for (int i = 0; i < taskResults.size(); i++) {
					HBox tempBox = InterfaceController.initDisplayElement(taskResults.get(i), numOfElements, numOfResults, true, View.SEARCH);
					VBox.setMargin(tempBox, new Insets(
							0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
					searchTaskContentBox.getChildren().add(tempBox);
					// Only increment the counter if an element is added
					if (InterfaceController.getLogic().isNonEmptyElement(taskResults.get(i))) {
						numOfResults++;
					}
				}
			} else {
				for (int i = 0; i < taskResults.size(); i++) {
					if (!InterfaceController.getLogic().isEmpty(taskResults.get(i))) {
						HBox tempBox = InterfaceController.initDisplayElement(taskResults.get(i), numOfElements, numOfResults, true, View.SEARCH);
						VBox.setMargin(tempBox, new Insets(
								0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
						searchTaskContentBox.getChildren().add(tempBox);
						// Only increment the counter if an element is added
						if (InterfaceController.getLogic().isNonEmptyElement(taskResults.get(i))) {
							numOfResults++;
						}
					}
				}
			}
		}
		// Print the event results
		for (int i = 0; i < eventResults.size(); i++) {
			HBox tempBox = InterfaceController.initDisplayElement(eventResults.get(i), numOfElements, numOfResults, false, View.SEARCH);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
        	searchEventContentBox.getChildren().add(tempBox);
			if (InterfaceController.getLogic().isNonEmptyElement(eventResults.get(i))) {
				numOfResults++;
			}
		}
	}
}
