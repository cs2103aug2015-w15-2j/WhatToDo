package gui;

import java.util.ArrayList;
import java.util.regex.Pattern;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class SearchViewController {


	/* ================================================================================
	 * JavaFX controls used in the general interface
	 * ================================================================================
	 */

	// Used for initSearchHeader
	private static HBox searchHeaderBox;

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

		searchHeaderBox = new HBox(searchHeaderLabel, searchHeaderTerm);
	}

	private static boolean isDate(String displayData) {

		String day = displayData.split(",")[0];
		return day.equals("Monday") || 
				day.equals("Tuesday") || 
				day.equals("Wednesday") || 
				day.equals("Thursday") || 
				day.equals("Friday") || 
				day.equals("Saturday") || 
				day.equals("Sunday");
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

	private static HBox initDisplayElement(String displayData, int index) {

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

			// Apply the binding to (element box width - text width - arbitrary margin)
			// The arbitrary margin exists because text in a container is not perfectly 
			// aligned to the dimensions of its container
			double textWidth = Math.ceil(text.getLayoutBounds().getWidth());
			elementLine.endXProperty().bind(elementBox.widthProperty().subtract(
					textWidth + InterfaceController.MARGIN_ARBITRARY));

			return elementBox;

		} else {
			// If there are no results
			if (isEmpty(displayData)) {
				Label elementLabel = new Label(displayData);
				HBox elementBox = new HBox(elementLabel);

				return elementBox;

			} else {
				Label elementIndex = new Label(String.valueOf(index));
				HBox indexBox = new HBox(elementIndex);

				String[] displayDataSplit = displayData.split(Pattern.quote("."));
				String elementString = displayDataSplit[1];
				for (int i = 2; i < displayDataSplit.length; i++) {
					elementString += displayDataSplit[i] + ".";
				}
				// Remove the last period in the string
				elementString = elementString.substring(0, elementString.length() - 1);
				Label elementLabel = new Label(elementString);

				HBox elementBox = new HBox(indexBox, elementLabel);

				return elementBox;
			}

		}
	}

	private static void initSearchTaskView(ArrayList<String> taskResults) {

		Label searchTaskHeaderLabel = new Label(HEADER_SEARCH_TASKS);
		searchTaskHeaderBox = new HBox(searchTaskHeaderLabel);

		searchTaskContentBox = new VBox();

		// Loop for the floats
		for (int i = 0; i < taskResults.size(); i++) {
			HBox tempBox = initDisplayElement(taskResults.get(i), i + 1);
			searchTaskContentBox.getChildren().add(tempBox);
		}

		searchTaskScroll = new ScrollPane(searchTaskContentBox);

		searchTaskBox = new VBox(searchTaskHeaderBox, searchTaskContentBox);
	}

	private static void initSearchEventView(ArrayList<String> eventResults) {

		Label searchEventHeaderLabel = new Label(HEADER_SEARCH_EVENTS);
		searchEventHeaderBox = new HBox(searchEventHeaderLabel);

		searchEventContentBox = new VBox();

		// Loop for the floats
		for (int i = 0; i < eventResults.size(); i++) {
			HBox tempBox = initDisplayElement(eventResults.get(i), i + 1);
			searchEventContentBox.getChildren().add(tempBox);
		}

		searchEventScroll = new ScrollPane(searchEventContentBox);

		searchEventBox = new VBox(searchEventHeaderBox, searchEventContentBox);
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

		InterfaceController.searchBox = new VBox(searchHeaderBox, searchBoxNoHeader);
	}

	public static void updateSearchView(String results) {

		ArrayList<String> taskResults = getTaskResults(results);
		ArrayList<String> eventResults = getEventResults(results);

		searchTaskContentBox.getChildren().clear();
		searchEventContentBox.getChildren().clear();
		
		for (int i = 0; i < taskResults.size(); i++) {
			HBox tempBox = initDisplayElement(taskResults.get(i), i + 1);
			searchTaskContentBox.getChildren().add(tempBox);
		}
		
		for (int i = 0; i < eventResults.size(); i++) {
			HBox tempBox = initDisplayElement(eventResults.get(i), i + 1);
			searchEventContentBox.getChildren().add(tempBox);
		}
	}
}
