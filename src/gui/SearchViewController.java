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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class SearchViewController {


	/* ================================================================================
	 * JavaFX controls used in the general interface
	 * ================================================================================
	 */

	// Used for initSearchHeader
	private static VBox searchHeaderBox;

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

	protected static final String SEMICOLON = ";";

	protected static final String HEADER_SEARCH = "RESULTS FOR:";
	protected static final String HEADER_SEARCH_TASKS = "TASKS";
	protected static final String HEADER_SEARCH_EVENTS = "EVENTS";

	protected static final String MESSAGE_EMPTY = "There are no items to display.";

	private static void initSearchHeader(String searchTerm) {

		Label searchHeaderLabel = new Label(HEADER_SEARCH);
		Label searchHeaderTerm = new Label(searchTerm);
		searchHeaderTerm.setWrapText(true);

		searchHeaderBox = new VBox(searchHeaderLabel, searchHeaderTerm);
		searchHeaderBox.setAlignment(Pos.CENTER);
		
        // Set margins for the header label
        VBox.setMargin(searchHeaderLabel, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		0, 0));
        VBox.setMargin(searchHeaderTerm, new Insets(
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0, 
        		InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 0));
        
        // Set margins for the header
        VBox.setMargin(searchHeaderBox, new Insets(
        		0, InterfaceController.MARGIN_SCROLL - InterfaceController.WIDTH_VERT_LINE, 
        		0, InterfaceController.MARGIN_SCROLL));
        
		// CSS
		searchHeaderLabel.getStyleClass().add("box-title-label");
		searchHeaderTerm.getStyleClass().add("box-title-label");
		searchHeaderBox.getStyleClass().add("box-title");
	}

	private static boolean isDate(String displayData) {

		// To prevent comparisons failing across different OSes
		// Assume a string is a date if it does not start with either
		// "todo" or "done" (if it is an element)
		// Also cannot match the no results found message
		return !(displayData.split(" ")[0].equals("todo") || 
				displayData.split(" ")[0].equals("done") || 
				displayData.split(" ")[0].equals("There"));
	}

	private static boolean isEmpty(String displayData) {

		return displayData.equals(MESSAGE_EMPTY);
	}

	private static ArrayList<String> getTaskResults(String displayData) {

		String[] resultsSplit = displayData.split("\n");
		ArrayList<String> taskResults = new ArrayList<String>();
		boolean startRead = false;

		for (int i = 0; i < resultsSplit.length; i++) {
			if (resultsSplit[i].equals("FLOAT")) {
				startRead = true;
				taskResults.add(resultsSplit[i]);
			} else if (resultsSplit[i].equals("EVENT")) {
				startRead = false;
				break;
			} else {
				if (startRead) {
					if (!resultsSplit[i].equals("TASK")) {
						taskResults.add(resultsSplit[i]);
					}
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
					eventResults.add(resultsSplit[i]);
				}
			}
		}

		return eventResults;
	}

	private static HBox initDisplayElement(String displayData, int index, boolean isTask) {

		if (isDate(displayData)) {

			Label elementLabel = new Label(displayData.toUpperCase());
			HBox elementBox = new HBox(elementLabel);

			// Create a divider line and add it to the elementBox
			Line elementLine = new Line(0, 0, InterfaceController.WIDTH_DEFAULT, 0);
			elementBox.getChildren().add(elementLine);

			// Get the width of label and resize the line
			Text text = new Text(elementLabel.getText());
			Scene s = new Scene(new Group(text));
			// Override the CSS style to calculate the text width
			text.setStyle("-fx-font-family: \"Myriad Pro\"; "
					+ "-fx-font-size: 14; "
					+ "-fx-font-weight: bold;");
			text.applyCss();

    		// Align the elements in the HBox
    		elementBox.setAlignment(Pos.CENTER_LEFT);
    		
    		// Set the margins of the element node label within the HBox
        	HBox.setMargin(elementLabel, new Insets(
        			0, InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
        			0, InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT));
        	
			// Apply the binding to (element box width - text width - arbitrary margin)
			// The arbitrary margin exists because text in a container is not perfectly 
			// aligned to the dimensions of its container
			double textWidth = Math.ceil(text.getLayoutBounds().getWidth());
			elementLine.endXProperty().bind(elementBox.widthProperty().subtract(
					textWidth + InterfaceController.MARGIN_ARBITRARY));

			// Apply CSS style for regular data field
        	elementLine.getStyleClass().add("line");
    		elementBox.getStyleClass().add("element-title");
			
			return elementBox;

		} else {
			// If there are no results
			if (isEmpty(displayData) || displayData.equals("FLOAT")) {
				Label elementLabel = new Label(displayData);
				elementLabel.setWrapText(true);
				
				HBox elementBox = new HBox(elementLabel);

    			HBox.setMargin(elementLabel, new Insets(
    					InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
    					InterfaceController.MARGIN_TEXT_ELEMENT, 
    					InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
    					InterfaceController.MARGIN_TEXT_ELEMENT));
    			
    			// Apply CSS style for regular data field
    			elementBox.getStyleClass().add("element");
    			
				return elementBox;

			} else {
				Label elementIndex = new Label(String.valueOf(index));
				
				HBox indexBox = new HBox(elementIndex);
				indexBox.setAlignment(Pos.CENTER);

				String[] displayDataSplit = displayData.split(Pattern.quote("."));
				String excludedString = displayDataSplit[0] + "."; 
				String elementString = displayData.replaceFirst(excludedString, "").trim();
				
				Label elementLabel = new Label(elementString);
				elementLabel.setWrapText(true);
				
        		// Get the width of label and resize the line
        		Text text = new Text(elementIndex.getText());
        		Scene s = new Scene(new Group(text));
        		// Override the CSS style to calculate the text width
        		text.setStyle("-fx-font-family: \"Myriad Pro\"; "
        				+ "-fx-font-size: 14; ");
        		text.applyCss();
        		double textWidth = Math.ceil(text.getLayoutBounds().getWidth());
    			indexBox.setMinWidth(textWidth + 2 * InterfaceController.MARGIN_TEXT_ELEMENT);

				HBox elementBox = new HBox(indexBox, elementLabel);

    			// Set the margins of the element index label within the HBox
    			HBox.setMargin(elementIndex, new Insets(
    					InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
    					InterfaceController.MARGIN_TEXT_ELEMENT, 
    					InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
    					InterfaceController.MARGIN_TEXT_ELEMENT));
    			
    			// Set the margins of the element node label within the HBox
    			HBox.setMargin(elementLabel, new Insets(
    					InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
    					InterfaceController.MARGIN_TEXT_ELEMENT, 
    					InterfaceController.MARGIN_TEXT_ELEMENT_HEIGHT, 
    					InterfaceController.MARGIN_TEXT_ELEMENT));
    			
    			// Apply CSS style for regular data field
    			elementBox.getStyleClass().add("element");
    			elementIndex.getStyleClass().add("element-index-label");
    			
    			if (isTask) {
        			indexBox.getStyleClass().add("element-index-task");
    			} else {
        			indexBox.getStyleClass().add("element-index-event");
    			}
    			
				return elementBox;
			}

		}
	}

	private static void initSearchTaskView(ArrayList<String> taskResults) {

		Label searchTaskHeaderLabel = new Label(HEADER_SEARCH_TASKS);
		searchTaskHeaderBox = new HBox(searchTaskHeaderLabel);
		searchTaskHeaderBox.setAlignment(Pos.CENTER);

		searchTaskContentBox = new VBox();

		// Loop for the floats
		for (int i = 0; i < taskResults.size(); i++) {
			HBox tempBox = initDisplayElement(taskResults.get(i), i + 1, true);
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

	private static void initSearchEventView(ArrayList<String> eventResults) {

		Label searchEventHeaderLabel = new Label(HEADER_SEARCH_EVENTS);
		searchEventHeaderBox = new HBox(searchEventHeaderLabel);
		searchEventHeaderBox.setAlignment(Pos.CENTER);

		searchEventContentBox = new VBox();

		// Loop for the floats
		for (int i = 0; i < eventResults.size(); i++) {
			HBox tempBox = initDisplayElement(eventResults.get(i), i + 1, false);
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

		// Initialize default empty values for initial view
		String initialTerm = "NO RESULTS";
		ArrayList<String> initialArray = new ArrayList<String>();

		initSearchHeader(initialTerm);
		initSearchTaskView(initialArray);
		initSearchEventView(initialArray);

		searchScrollLine = new Line(0, 0, 0, InterfaceController.WIDTH_DEFAULT_BUTTON);

		HBox searchBoxNoHeader = new HBox(searchTaskBox, searchScrollLine, searchEventBox);

        // Set the scroll separator to bind with the same line in DefaultViewController
        // but minus the height of the header
		Group root = new Group(searchHeaderBox);
        new Scene(root);
        root.applyCss();
        root.layout();
        double headerHeight = searchHeaderBox.getLayoutBounds().getHeight();
        
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
		
		int numOfEmptyMessages = 0;
		// Only print the empty message if there are zero results
		if (taskResults.size() == 3 && isEmpty(taskResults.get(1))) {
			HBox tempBox = initDisplayElement(taskResults.get(1), 2, true);
			VBox.setMargin(tempBox, new Insets(
					0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
			searchTaskContentBox.getChildren().add(tempBox);
		} else {
			boolean withinFloat = false;
			boolean withinTask = false;
			
			// If there are no results for floating tasks
			if (taskResults.get(1).equals(MESSAGE_EMPTY)) {
				for (int i = 0; i < taskResults.size(); i++) {
					HBox tempBox = initDisplayElement(taskResults.get(i), i + 1, true);
					VBox.setMargin(tempBox, new Insets(
							0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
					searchTaskContentBox.getChildren().add(tempBox);
				}
			} else {
				for (int i = 0; i < taskResults.size(); i++) {
					if (!taskResults.get(i).equals(MESSAGE_EMPTY)) {
						HBox tempBox = initDisplayElement(taskResults.get(i), i + 1, true);
						VBox.setMargin(tempBox, new Insets(
								0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
						searchTaskContentBox.getChildren().add(tempBox);
					}
				}
			}
		}
		
		for (int i = 0; i < eventResults.size(); i++) {
			HBox tempBox = initDisplayElement(eventResults.get(i), i + 1, false);
        	VBox.setMargin(tempBox, new Insets(
        			0, 0, InterfaceController.MARGIN_TEXT_ELEMENT_SEPARATOR, 0));
			searchEventContentBox.getChildren().add(tempBox);
		}
	}
}
