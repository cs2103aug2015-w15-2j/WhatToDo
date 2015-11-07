package gui;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import struct.View;

public class SearchViewController {

	// ================================================================================
    // JavaFX controls used in the search interface
    // ================================================================================

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

	private static final String HEADER_SEARCH = "RESULTS FOR:";
	private static final String HEADER_SEARCH_TASKS = "TASKS";
	private static final String HEADER_SEARCH_EVENTS = "EVENTS";

    /**
     * This method initializes all the interface components for the search view,
     * primarily the task window and the event window
     */
	public static void initSearchView() {
		ArrayList<String> initialArray = new ArrayList<String>();
		initSearchTaskView(initialArray, 0);
		initSearchEventView(initialArray, 0);

		searchScrollLine = new Line(0, 0, 0, InterfaceController.WIDTH_DEFAULT_BUTTON);
		HBox searchBoxNoHeader = new HBox(searchTaskBox, searchScrollLine, searchEventBox);
		InterfaceController.searchBox = new VBox(searchBoxNoHeader);
		
		searchTaskScroll.prefViewportWidthProperty().bind(
        		InterfaceController.searchBox.widthProperty().divide(2));
        searchEventScroll.prefViewportWidthProperty().bind(
        		InterfaceController.searchBox.widthProperty().divide(2));
        
        searchTaskScroll.maxWidthProperty().bind(
        		InterfaceController.searchBox.widthProperty().divide(2));
        searchEventScroll.maxWidthProperty().bind(
        		InterfaceController.searchBox.widthProperty().divide(2));
        
        searchScrollLine.endYProperty().bind(
        		DefaultViewController.getDefScrollLine().endYProperty());
        
        // CSS
        searchScrollLine.getStyleClass().add("line");
	}

    /**
     * This method updates the all view with data from the text file
     * 
     * Called by:
     * 	1. 	runCommand() in LogicController to update the view every time an 
     * 		operation is performed
     * 
     * @param results
     * 			  The search results as a String returned from Logic's
     * 			  executeCommand()
     */
	public static void updateSearchView(String results) {
		ArrayList<String> taskResults = getTaskResults(results);
		ArrayList<String> eventResults = getEventResults(results);

		searchTaskContentBox.getChildren().clear();
		searchEventContentBox.getChildren().clear();
		ViewIndexMap.resetSearchMap();
		
		int numOfElements = InterfaceController.getLogic().getSearchElementsCount(
				taskResults, eventResults);
		int numOfResults = 1;
		
		numOfResults = updateSearchTasks(taskResults, numOfElements, numOfResults);
		numOfResults = updateSearchEvents(eventResults, numOfElements, numOfResults);
	}
	
    // ================================================================================
    // Private methods, used to initialize various sub components of the interface
    // ================================================================================
	
	/**
	 * This method formats the results of a search into only task results
	 * 
	 * @param displayData
	 * 		      The search results passed back from Logic's executeCommand
	 * 			  as a String in the return message 
	 * @return An ArrayList of Strings which contain the task results for 
	 * 		   the search
	 */
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

	/**
	 * This method formats the results of a search into only event results
	 * 
	 * @param displayData
	 * 		      The search results passed back from Logic's executeCommand
	 * 			  as a String in the return message 
	 * @return An ArrayList of Strings which contain the event results for 
	 * 		   the search
	 */
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
	
	/**
	 * This method initializes the task view for the search view
	 * 
	 * @param taskResults
	 * 			  A String[] of tasks returned from getTaskResults()
	 * @param numOfElements
	 * 			  The number of elements in taskResults
	 */
	private static void initSearchTaskView(ArrayList<String> taskResults, int numOfElements) {
		Label searchTaskHeaderLabel = new Label(HEADER_SEARCH_TASKS);
		searchTaskHeaderBox = new HBox(searchTaskHeaderLabel);
		searchTaskContentBox = new VBox();

		initSearchTasks(taskResults, numOfElements);
		searchTaskScroll = new ScrollPane(searchTaskContentBox);
		searchTaskBox = new VBox(searchTaskHeaderBox, searchTaskScroll);
		
		// Component formatting
		searchTaskHeaderBox.setAlignment(Pos.CENTER);
		searchTaskScroll.setFitToWidth(true);
        VBox.setVgrow(searchTaskScroll, Priority.ALWAYS);
        searchTaskBox.setAlignment(Pos.CENTER);
        
        HBox.setMargin(searchTaskHeaderLabel, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        VBox.setMargin(searchTaskHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        VBox.setMargin(searchTaskScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));

		searchTaskScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		searchTaskScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // CSS
        searchTaskHeaderLabel.getStyleClass().add("box-title-label");
        searchTaskHeaderBox.getStyleClass().add("box-title-all-task");
	}

	/**
	 * This method initializes the event view for the search view
	 * 
	 * @param eventResults
	 * 			  A String[] of tasks returned from getEventResults()
	 * @param numOfElements
	 * 			  The number of elements in eventResults
	 */
	private static void initSearchEventView(ArrayList<String> eventResults, int numOfElements) {
		Label searchEventHeaderLabel = new Label(HEADER_SEARCH_EVENTS);
		searchEventHeaderBox = new HBox(searchEventHeaderLabel);
		searchEventContentBox = new VBox();

		initSearchEvents(eventResults, numOfElements);
		searchEventScroll = new ScrollPane(searchEventContentBox);
		searchEventBox = new VBox(searchEventHeaderBox, searchEventScroll);
		
		// Component formatting
		searchEventHeaderBox.setAlignment(Pos.CENTER);
		searchEventScroll.setFitToWidth(true);
        VBox.setVgrow(searchEventScroll, Priority.ALWAYS);
        searchEventBox.setAlignment(Pos.CENTER);
        
        HBox.setMargin(searchEventHeaderLabel, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        VBox.setMargin(searchEventHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL, 
        		0, InterfaceController.MARGIN_SCROLL));
        
        VBox.setMargin(searchEventScroll, new Insets(
        		InterfaceController.MARGIN_COMPONENT, 
        		InterfaceController.MARGIN_SCROLL, 
        		0, 
        		InterfaceController.MARGIN_SCROLL));

		searchEventScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		searchEventScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // CSS
        searchEventHeaderLabel.getStyleClass().add("box-title-label");
        searchEventHeaderBox.getStyleClass().add("box-title-all-event");
	}

	/**
	 * This method initializes the task content of the search view with data
	 * 
	 * @param taskResults
	 * 			  The ArrayList of task data to be displayed in the view
	 * @param numOfElements
	 * 			  The total number of tasks/events. Used for formatting the index box
	 */
	private static void initSearchTasks(ArrayList<String> taskResults, int numOfElements) {
		for (int i = 0; i < taskResults.size(); i++) {
			HBox tempBox = InterfaceController.initDisplayElement(
					taskResults.get(i), numOfElements, i + 1, true, View.SEARCH);
			VBox.setMargin(tempBox, new Insets(
					0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
			searchTaskContentBox.getChildren().add(tempBox);
		}
	}
	
	/**
	 * This method initializes the event content of the search view with data
	 * 
	 * @param eventResults
	 * 			  The ArrayList of event data to be displayed in the view
	 * @param numOfElements
	 * 			  The total number of tasks/events. Used for formatting the index box
	 */
	private static void initSearchEvents(ArrayList<String> eventResults, int numOfElements) {
		for (int i = 0; i < eventResults.size(); i++) {
			HBox tempBox = InterfaceController.initDisplayElement(
					eventResults.get(i), numOfElements, i + 1, false, View.SEARCH);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
			searchEventContentBox.getChildren().add(tempBox);
		}
	}
	
	/**
	 * This method updates the various task content of the search view with the
     * updated data
     * 
	 * @param taskResults
	 * 			  The updated ArrayList of task data to be displayed in the view
	 * @param numOfElements
	 * 			  The total number of tasks/events. Used for formatting the index box
	 * @param index
	 * 			  The view index of the particular task/event
	 * @return The index of the last element to be added
	 */
	private static int updateSearchTasks(ArrayList<String> taskResults, int numOfElements, int index) {
		// Only print the empty message if there are zero results
		if (taskResults.size() == 3 && InterfaceController.getLogic().isEmpty(taskResults.get(2))) {
			HBox tempBox = InterfaceController.initDisplayElement(
					taskResults.get(2), numOfElements, index, true, View.SEARCH);
			VBox.setMargin(tempBox, new Insets(
					0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
			searchTaskContentBox.getChildren().add(tempBox);
		} else {
			// If there are no results for floating tasks
			if (InterfaceController.getLogic().isEmpty(taskResults.get(taskResults.size() - 1))) {
				for (int i = 0; i < taskResults.size(); i++) {
					HBox tempBox = InterfaceController.initDisplayElement(
							taskResults.get(i), numOfElements, index, true, View.SEARCH);
					VBox.setMargin(tempBox, new Insets(
							0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
					searchTaskContentBox.getChildren().add(tempBox);
					// Only increment the counter if an element is added
					if (InterfaceController.getLogic().isNonEmptyElement(taskResults.get(i))) {
						index++;
					}
				}
			} else {
				for (int i = 0; i < taskResults.size(); i++) {
					if (!InterfaceController.getLogic().isEmpty(taskResults.get(i))) {
						HBox tempBox = InterfaceController.initDisplayElement(
								taskResults.get(i), numOfElements, index, true, View.SEARCH);
						VBox.setMargin(tempBox, new Insets(
								0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
						searchTaskContentBox.getChildren().add(tempBox);
						// Only increment the counter if an element is added
						if (InterfaceController.getLogic().isNonEmptyElement(taskResults.get(i))) {
							index++;
						}
					}
				}
			}
		}
		return index;
	}

	/**
	 * This method updates the various event content of the search view with the
     * updated data
     * 
	 * @param eventResults
	 * 			  The updated ArrayList of event data to be displayed in the view
	 * @param numOfElements
	 * 			  The total number of tasks/events. Used for formatting the index box
	 * @param index
	 * 			  The view index of the particular task/event
	 * @return The index of the last element to be added
	 */
	private static int updateSearchEvents(ArrayList<String> eventResults, int numOfElements, int numOfResults) {
		// Print the event results
		for (int i = 0; i < eventResults.size(); i++) {
			HBox tempBox = InterfaceController.initDisplayElement(
					eventResults.get(i), numOfElements, numOfResults, false, View.SEARCH);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
        	searchEventContentBox.getChildren().add(tempBox);
			if (InterfaceController.getLogic().isNonEmptyElement(eventResults.get(i))) {
				numOfResults++;
			}
		}
		return numOfResults;
	}
}
