/**
 * This class reads, writes and manipulates the text data in the text file which
 * stores the to-do list of the user.
 * 
 * @@author Lam Zhen Zong, Nicholas
 */

package backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystemException;
import java.util.ArrayList;

import struct.Date;
import struct.FloatingTask;
import struct.Event;
import struct.Task;

public class Storage {

	private static final String FILE_NAME = "whattodo.txt";
	private static final String COLLATED_FILE_PATH_FORMAT = "%s\\%s";
	private static final String CONFIG_FILE_PATH = "config\\config";

	private static final String MESSAGE_ADD_TASK = "Added \"%s\" to list.\nDue on %s, %s.";
	private static final String MESSAGE_ADD_FLOAT_TASK = "Added \"%s\" to list.";
	private static final String MESSAGE_ADD_EVENT = "Added \"%s\" to list.\nEvent Start: %s, %s, %s\nEvent End: %s, %s, %s";
	private static final String MESSAGE_DELETE_LINE = "Deleted \"%s\" from list.";
	private static final String MESSAGE_EDIT_NAME = "Edited %s name from \"%s\" to \"%s\".";
	private static final String MESSAGE_EDIT_TASK_DEADLINE = "Edited task \"%s\" deadline from \"%s\" to \"%s\".";
	private static final String MESSAGE_EDIT_EVENT_START_DATE = "Edited event \"%s\" start date from \"%s\" to \"%s\".";
	private static final String MESSAGE_EDIT_EVENT_END_DATE = "Edited event \"%s\" end date from \"%s\" to \"%s\".";
	private static final String MESSAGE_EDIT_EVENT_START_TIME = "Edited event \"%s\" start time from \"%s\" to \"%s\".";
	private static final String MESSAGE_EDIT_EVENT_END_TIME = "Edited event \"%s\" end time from \"%s\" to \"%s\".";
	private static final String MESSAGE_MARK_DONE = "The %s \"%s\" has been marked as done.";
	private static final String MESSAGE_EMPTY_FILE = "The list is empty.";
	private static final String MESSAGE_CHANGE_STORAGE_SUCCESS = "You are now writing to \"%s\"";
	private static final String MESSAGE_SAME_FILE = "Your file location remains unchanged.";

	private static final String MESSAGE_ERROR_CREATE_FILE = "Error encountered when creating file.";
	private static final String MESSAGE_ERROR_READ_FILE = "Error encountered when reading file.";
	private static final String MESSAGE_ERROR_CHANGING_FILE_PATH = "Error encountered when changing file location.";
	private static final String MESSAGE_ERROR_INVALID_LINE_ACCESS = "Cannot find line %d in text file.";
	private static final String MESSAGE_ERROR_INVALID_LINE_ACCESS_TASK = "Line %d is not a task!";
	private static final String MESSAGE_ERROR_INVALID_LINE_ACCESS_EVENT = "Line %d is not an event!";
	private static final String MESSAGE_ERROR_INVALID_EVENT_DATE_RANGE = "Event start date/time is not earlier than end date/time.";
	private static final String MESSAGE_ERROR_ADD_TASK = "Error encountered when adding task to file.";
	private static final String MESSAGE_ERROR_ADD_EVENT = "Error encountered when adding event to file.";
	private static final String MESSAGE_ERROR_DELETE_LINE = "Error encountered when deleting from file.";
	private static final String MESSAGE_ERROR_REPLACE_NAME = "Error encountered when renaming.";
	private static final String MESSAGE_ERROR_REPLACE_TASK_DEADLINE = "Error encountered when editing deadline.";
	private static final String MESSAGE_ERROR_REPLACE_EVENT_START_DATE = "Error encountered when editing event start date.";
	private static final String MESSAGE_ERROR_REPLACE_EVENT_END_DATE = "Error encountered when editing event end date.";
	private static final String MESSAGE_ERROR_REPLACE_EVENT_START_TIME = "Error encountered when editing event start time.";
	private static final String MESSAGE_ERROR_REPLACE_EVENT_END_TIME = "Error encountered when editing event end time.";
	private static final String MESSAGE_ERROR_ALREADY_DONE = "Error encountered: the %s \"%s\" has already been completed.";
	private static final String MESSAGE_ERROR_MARK_DONE = "Error encountered when marking line as done.";

	private static final String TEXT_FILE_DIVIDER = ";";

	private static final String STRING_TASK = "task";
	private static final String STRING_FLOAT_TASK = "float";
	private static final String STRING_EVENT = "event";

	private static final String NEWLINE = "\n";
	private static final String EMPTY_STRING = "";

	private static final int PARAM_EMPTY_STRING_BUILDER = 0;
	private static final int PARAM_LINE_NUMBER_ZERO = 0;

	// This string stores the whole file name with directory.
	private String filePath;

	// For reading from file.
	private BufferedReader fileReader;

	// For writing into file.
	private PrintWriter fileWriter;

	/**
	 * Constructor
	 * 
	 * @throws FileSystemException  when error in creating file.
	 */
	public Storage() throws FileSystemException {
		filePath = getPathFromConfig();

		createFile();
	}

	public String getFilePath() {
		return filePath;
	}
	
	/**
	 * Changes location to store text file to given newLocation string.
	 * Directories/Folders will be created if missing.
	 * 
	 * Here are some examples of what newLocation parameter can be:
	 * " "                    - store text file at default location.
	 * "TaskList"             - store text file in folder called TaskList. TaskList created at default location.
	 * "One\Two"              - store text file in folder Two which is nested in folder One (at default location)
	 * "C:\Users\Jim\DropBox" - store text file in DropBox.
	 * 
	 * Returns error message when given newLocation is invalid name for directory or file.
	 * 
	 * @param newLocation   file path of location to change to
	 * @return              status message to indicate if changing is a success.
	 */
	public String changeFileStorageLocation(String newLocation) {
		String feedback = changeFilePath(newLocation);
		
		return feedback;
	}

	/**
	 * Adds a task with deadline to the text file (to-do list). Tasks in the
	 * file will be sorted by deadlines. Returns an error message if adding task
	 * failed.
	 * 
	 * @param taskName
	 *            the name of the task to be added
	 * @param taskDeadline
	 *            the deadline of task to be added
	 * @return feedback message based on task to be added
	 */
	// deprecated method, use the one below.
	public String addTask(String taskName, Date taskDeadline) {
		return addTask(new Task(taskName, false, taskDeadline));
	}

	public String addTask(Task newTask) {
		try {
			addTaskToFile(newTask);
		} catch (IOException exception) {
			return MESSAGE_ERROR_ADD_TASK;
		}

		return String.format(MESSAGE_ADD_TASK, newTask.getName(), newTask
				.getDeadline().getDayString(), newTask.getDeadline()
				.getFormatDate());
	}

	/**
	 * Adds a floating task to the text file (to-do list). Floating tasks in the
	 * file will be sorted in alphabetical order. Returns an error message if
	 * adding task failed.
	 * 
	 * @param taskName
	 *            the name of the task to be added
	 * @return feedback message based on task to be added
	 */
	// deprecated method, use the one below.
	public String addFloatingTask(String taskName) {
		return addFloatingTask(new FloatingTask(taskName, false));
	}

	public String addFloatingTask(FloatingTask taskToAdd) {
		try {
			addFloatTaskToFile(taskToAdd);
		} catch (IOException exception) {
			return MESSAGE_ERROR_ADD_TASK;
		}

		return String.format(MESSAGE_ADD_FLOAT_TASK, taskToAdd.getName());
	}

	/**
	 * Adds an event to the text file (to-do-list). Events in the file will be
	 * sorted by start date/time, followed by end date/time. Returns an error
	 * message if adding event failed.
	 * 
	 * @param eventName
	 *            the name of event to be added
	 * @param eventStartDate
	 *            start date of event to be added
	 * @param eventStartTime
	 *            start time of event to be added
	 * @param eventEndDate
	 *            end date of event to be added
	 * @param eventEndTime
	 *            end time of event to be added
	 * @return
	 */
	// deprecated method, use the one below.
	public String addEvent(String eventName, Date eventStartDate,
			String eventStartTime, Date eventEndDate, String eventEndTime) {
		return addEvent(new Event(eventName, false, eventStartDate,
				eventEndDate, eventStartTime, eventEndTime));
	}

	public String addEvent(Event newEvent) {
		try {
			addEventToFile(newEvent);
		} catch (IOException exception) {
			return MESSAGE_ERROR_ADD_EVENT;
		}

		return String.format(MESSAGE_ADD_EVENT, newEvent.getName(), newEvent
				.getEventStartDate().getDayString(), newEvent
				.getEventStartDate().getFormatDate(), newEvent
				.getEventStartTime(),
				newEvent.getEventEndDate().getDayString(), newEvent
						.getEventEndDate().getFormatDate(), newEvent
						.getEventEndTime());
	}

	/**
	 * Deletes a line in the text file with specified line number. (1-based
	 * counting) An error message will be returned when line number is less than
	 * 0 or greater than the number of lines present in text file, or when
	 * deleting failed.
	 * 
	 * @param lineNumber
	 *            line number in text file to be deleted.
	 * @return feedback based on line number.
	 */
	public String deleteLine(int lineNumber) {
		try {
			String feedback = deleteLineFromFile(lineNumber);

			return feedback;
		} catch (IOException exception) {
			return MESSAGE_ERROR_DELETE_LINE;
		}
	}

	/**
	 * Displays the contents in the text file (to-do list). Returns an error
	 * message if reading file contents failed.
	 * 
	 * @return the contents of text file in a String.
	 */
	public String display() {
		try {
			return showAllFileContents();
		} catch (IOException exception) {
			return MESSAGE_ERROR_READ_FILE;
		}
	}

	/**
	 * Replaces task/event name in text file of given line number. (1-based
	 * counting) An error message will be returned when line number is less than
	 * 0 or greater than the number of lines present in text file.
	 * 
	 * @param lineNumber
	 *            line number in text file to be replaced.
	 * @param newName
	 *            new event/task name to replace old name with.
	 * @return feedback based on line number.
	 */
	public String editName(int lineNumber, String newName) {
		try {
			String feedback = replaceName(lineNumber, newName);

			return feedback;
		} catch (IOException exception) {
			return MESSAGE_ERROR_REPLACE_NAME;
		}
	}

	/**
	 * Replaces task deadline in text file of given line number. (1-based
	 * counting) An error message will be returned when line number is less than
	 * 0 or greater than the number of lines present in text file, or when the
	 * object at line number is not a task.
	 * 
	 * @param lineNumber
	 *            line number in text file to be replaced.
	 * @param newDeadline
	 *            new task deadline to replace old deadline with.
	 * @return feedback based on line number.
	 */
	public String editTaskDeadline(int lineNumber, Date newDeadline) {
		try {
			String feedback = replaceTaskDeadline(lineNumber, newDeadline);

			return feedback;
		} catch (IOException exception) {
			return MESSAGE_ERROR_REPLACE_TASK_DEADLINE;
		}
	}

	/**
	 * Replaces event start date in text file of given line number. (1-based
	 * counting) An error message will be returned when line number is less than
	 * 0 or greater than the number of lines present in text file, or when the
	 * object at line number is not an event, or when event start later than
	 * event end.
	 * 
	 * @param lineNumber
	 *            line number in text file to be replaced.
	 * @param newDate
	 *            new event start date to replace old start date with.
	 * @return feedback based on line number.
	 */
	public String editEventStartDate(int lineNumber, Date newDate) {
		try {
			String feedback = replaceEventStartDate(lineNumber, newDate);

			return feedback;
		} catch (IOException exception) {
			return MESSAGE_ERROR_REPLACE_EVENT_START_DATE;
		}
	}

	/**
	 * Replaces event end date in text file of given line number. (1-based
	 * counting) An error message will be returned when line number is less than
	 * 0 or greater than the number of lines present in text file, or when the
	 * object at line number is not an event, or when event start later than
	 * event end.
	 * 
	 * @param lineNumber
	 *            line number in text file to be replaced.
	 * @param newDate
	 *            new event end date to replace old end date with.
	 * @return feedback based on line number.
	 */
	public String editEventEndDate(int lineNumber, Date newDate) {
		try {
			String feedback = replaceEventEndDate(lineNumber, newDate);

			return feedback;
		} catch (IOException exception) {
			return MESSAGE_ERROR_REPLACE_EVENT_END_DATE;
		}
	}

	/**
	 * Replaces event start time in text file of given line number. (1-based
	 * counting) An error message will be returned when line number is less than
	 * 0 or greater than the number of lines present in text file, or when the
	 * object at line number is not an event, or when event start later than
	 * event end.
	 * 
	 * @param lineNumber
	 *            line number in text file to be replaced.
	 * @param newTime
	 *            new event start time to replace old start time with.
	 * @return feedback based on line number.
	 */
	public String editEventStartTime(int lineNumber, String newTime) {
		try {
			String feedback = replaceEventStartTime(lineNumber, newTime);

			return feedback;
		} catch (IOException exception) {
			return MESSAGE_ERROR_REPLACE_EVENT_START_TIME;
		}
	}

	/**
	 * Replaces event end time in text file of given line number. (1-based
	 * counting) An error message will be returned when line number is less than
	 * 0 or greater than the number of lines present in text file, or when the
	 * object at line number is not an event, or when event start later than
	 * event end.
	 * 
	 * @param lineNumber
	 *            line number in text file to be replaced.
	 * @param newTime
	 *            new event end time to replace old end time with.
	 * @return feedback based on line number.
	 */
	public String editEventEndTime(int lineNumber, String newTime) {
		try {
			String feedback = replaceEventEndTime(lineNumber, newTime);

			return feedback;
		} catch (IOException exception) {
			return MESSAGE_ERROR_REPLACE_EVENT_END_TIME;
		}
	}

	/**
	 * Overwrites the entire text file with given string input.
	 * 
	 * @param textToWrite
	 *            text used to overwrite the text file with.
	 * @throws FileNotFoundException
	 *             when unable to find file to write to.
	 */
	public void overwriteFile(String textToWrite) throws FileNotFoundException {
		writeContentsToFile(textToWrite);
	}

	/**
	 * Marks the task/event/float at given line number in text file as done.
	 * (1-based counting)
	 * 
	 * @param lineNumber
	 *            line number in text file to be marked as done.
	 * @return feedback based on line number
	 */
	public String markAsDone(int lineNumber) {
		try {
			String feedback = markAsCompleted(lineNumber);

			return feedback;
		} catch (IOException exception) {
			return MESSAGE_ERROR_MARK_DONE;
		}
	}

	/*
	 * Private Methods Start Here.
	 */

	// To refactor and improve
	private String replaceEventStartTime(int lineNumber, String newStartTime)
			throws IOException {
		ArrayList<String> fileContents = new ArrayList<String>();
		String oldStartTime;
		String eventName;

		addFileContentsToArrayList(fileContents);

		if (!isValidLineNumber(lineNumber, fileContents)) {
			return String.format(MESSAGE_ERROR_INVALID_LINE_ACCESS, lineNumber);
		}

		String lineToBeEdited = fileContents.get(lineNumber - 1);
		String typeToBeEdited = getFirstWord(lineToBeEdited);

		if (typeToBeEdited.equals(STRING_EVENT)) {
			Event editedEvent = new Event(lineToBeEdited);

			if (!isValidStartAndEnd(editedEvent.getEventStartDate(),
					newStartTime, editedEvent.getEventEndDate(),
					editedEvent.getEventEndTime())) {
				return MESSAGE_ERROR_INVALID_EVENT_DATE_RANGE;
			}

			oldStartTime = editedEvent.getEventStartTime();
			editedEvent.setEventStartTime(newStartTime);
			eventName = editedEvent.getName();

			deleteLineFromFile(lineNumber);
			addEventToFile(editedEvent);
		} else {
			return String.format(MESSAGE_ERROR_INVALID_LINE_ACCESS_EVENT,
					lineNumber);
		}

		return String.format(MESSAGE_EDIT_EVENT_START_TIME, eventName,
				oldStartTime, newStartTime);
	}

	// To refactor and improve
	private String replaceEventStartDate(int lineNumber, Date newStartDate)
			throws IOException {
		ArrayList<String> fileContents = new ArrayList<String>();
		String oldStartDate;
		String eventName;

		addFileContentsToArrayList(fileContents);

		if (!isValidLineNumber(lineNumber, fileContents)) {
			return String.format(MESSAGE_ERROR_INVALID_LINE_ACCESS, lineNumber);
		}

		String lineToBeEdited = fileContents.get(lineNumber - 1);
		String typeToBeEdited = getFirstWord(lineToBeEdited);

		if (typeToBeEdited.equals(STRING_EVENT)) {
			Event editedEvent = new Event(lineToBeEdited);

			if (!isValidStartAndEnd(newStartDate,
					editedEvent.getEventStartTime(),
					editedEvent.getEventEndDate(),
					editedEvent.getEventEndTime())) {
				return MESSAGE_ERROR_INVALID_EVENT_DATE_RANGE;
			}

			oldStartDate = editedEvent.getEventStartDate().getFormatDate();
			editedEvent.setEventStartDate(newStartDate);
			eventName = editedEvent.getName();

			deleteLineFromFile(lineNumber);
			addEventToFile(editedEvent);
		} else {
			return String.format(MESSAGE_ERROR_INVALID_LINE_ACCESS_EVENT,
					lineNumber);
		}

		return String.format(MESSAGE_EDIT_EVENT_START_DATE, eventName,
				oldStartDate, newStartDate.getFormatDate());
	}

	// To refactor and improve
	private String replaceEventEndDate(int lineNumber, Date newEndDate)
			throws IOException {
		ArrayList<String> fileContents = new ArrayList<String>();
		String oldEndDate;
		String eventName;

		addFileContentsToArrayList(fileContents);

		if (!isValidLineNumber(lineNumber, fileContents)) {
			return String.format(MESSAGE_ERROR_INVALID_LINE_ACCESS, lineNumber);
		}

		String lineToBeEdited = fileContents.get(lineNumber - 1);
		String typeToBeEdited = getFirstWord(lineToBeEdited);

		if (typeToBeEdited.equals(STRING_EVENT)) {
			Event editedEvent = new Event(lineToBeEdited);

			if (!isValidStartAndEnd(editedEvent.getEventStartDate(),
					editedEvent.getEventStartTime(), newEndDate,
					editedEvent.getEventEndTime())) {
				return MESSAGE_ERROR_INVALID_EVENT_DATE_RANGE;
			}

			oldEndDate = editedEvent.getEventEndDate().getFormatDate();
			editedEvent.setEventEndDate(newEndDate);
			eventName = editedEvent.getName();

			deleteLineFromFile(lineNumber);
			addEventToFile(editedEvent);
		} else {
			return String.format(MESSAGE_ERROR_INVALID_LINE_ACCESS_EVENT,
					lineNumber);
		}

		return String.format(MESSAGE_EDIT_EVENT_END_DATE, eventName,
				oldEndDate, newEndDate.getFormatDate());
	}

	// To refactor and improve
	private String replaceEventEndTime(int lineNumber, String newEndTime)
			throws IOException {
		ArrayList<String> fileContents = new ArrayList<String>();
		String oldEndTime;
		String eventName;

		addFileContentsToArrayList(fileContents);

		if (!isValidLineNumber(lineNumber, fileContents)) {
			return String.format(MESSAGE_ERROR_INVALID_LINE_ACCESS, lineNumber);
		}

		String lineToBeEdited = fileContents.get(lineNumber - 1);
		String typeToBeEdited = getFirstWord(lineToBeEdited);

		if (typeToBeEdited.equals(STRING_EVENT)) {
			Event editedEvent = new Event(lineToBeEdited);

			if (!isValidStartAndEnd(editedEvent.getEventStartDate(),
					editedEvent.getEventStartTime(),
					editedEvent.getEventEndDate(), newEndTime)) {
				return MESSAGE_ERROR_INVALID_EVENT_DATE_RANGE;
			}

			oldEndTime = editedEvent.getEventEndTime();
			editedEvent.setEventEndTime(newEndTime);
			eventName = editedEvent.getName();

			deleteLineFromFile(lineNumber);
			addEventToFile(editedEvent);
		} else {
			return String.format(MESSAGE_ERROR_INVALID_LINE_ACCESS_EVENT,
					lineNumber);
		}

		return String.format(MESSAGE_EDIT_EVENT_END_TIME, eventName,
				oldEndTime, newEndTime);
	}

	// To refactor and improve
	private String replaceTaskDeadline(int lineNumber, Date newDeadline)
			throws IOException {
		ArrayList<String> fileContents = new ArrayList<String>();
		String oldDeadline;
		String taskName;

		addFileContentsToArrayList(fileContents);

		if (!isValidLineNumber(lineNumber, fileContents)) {
			return String.format(MESSAGE_ERROR_INVALID_LINE_ACCESS, lineNumber);
		}

		String lineToBeEdited = fileContents.get(lineNumber - 1);
		String typeToBeEdited = getFirstWord(lineToBeEdited);

		if (typeToBeEdited.equals(STRING_TASK)) {
			Task editedTask = new Task(lineToBeEdited);

			oldDeadline = editedTask.getDeadline().getFormatDate();
			editedTask.setDeadline(newDeadline);
			taskName = editedTask.getName();

			deleteLineFromFile(lineNumber);
			addTaskToFile(editedTask);
		} else {
			return String.format(MESSAGE_ERROR_INVALID_LINE_ACCESS_TASK,
					lineNumber);
		}

		return String.format(MESSAGE_EDIT_TASK_DEADLINE, taskName, oldDeadline,
				newDeadline.getFormatDate());
	}

	// Refactor
	private boolean isValidStartAndEnd(Date startDate, String startTime,
			Date endDate, String endTime) {
		if (startDate.compareTo(endDate) > 0) {
			return false;
		} else if (startDate.compareTo(endDate) == 0) {
			return (Integer.parseInt(endTime) > Integer.parseInt(startTime));
		} else {
			return true;
		}
	}

	private void addFileContentsToArrayList(ArrayList<String> fileContents)
			throws IOException {
		initialiseReader();

		while (true) {
			String lineRead = fileReader.readLine();

			if (lineRead == null) {
				break;
			} else {
				fileContents.add(lineRead);
			}
		}

		fileReader.close();
	}

	// Refactor and improve
	private String deleteLineFromFile(int lineNumber) throws IOException {
		ArrayList<String> fileContents = new ArrayList<String>();

		addFileContentsToArrayList(fileContents);

		if (!isValidLineNumber(lineNumber, fileContents)) {
			return String.format(MESSAGE_ERROR_INVALID_LINE_ACCESS, lineNumber);
		}

		String lineToDelete = fileContents.remove(lineNumber - 1);

		writeContentsToFile(fileContents);

		return String.format(MESSAGE_DELETE_LINE, lineToDelete);
	}

	// To refactor and improve
	private String replaceName(int lineNumber, String newName)
			throws IOException {
		ArrayList<String> fileContents = new ArrayList<String>();
		String oldName;

		addFileContentsToArrayList(fileContents);

		if (!isValidLineNumber(lineNumber, fileContents)) {
			return String.format(MESSAGE_ERROR_INVALID_LINE_ACCESS, lineNumber);
		}

		String lineToBeEdited = fileContents.get(lineNumber - 1);
		String typeToBeEdited = getFirstWord(lineToBeEdited);

		if (typeToBeEdited.equals(STRING_TASK)) {
			Task editedTask = new Task(lineToBeEdited);

			oldName = editedTask.getName();
			editedTask.setName(newName);

			deleteLineFromFile(lineNumber);
			addTaskToFile(editedTask);
		} else if (typeToBeEdited.equals(STRING_EVENT)) {
			Event editedEvent = new Event(lineToBeEdited);

			oldName = editedEvent.getName();
			editedEvent.setName(newName);

			deleteLineFromFile(lineNumber);
			addEventToFile(editedEvent);
		} else if (typeToBeEdited.equals(STRING_FLOAT_TASK)) {
			FloatingTask editedFloat = new FloatingTask(lineToBeEdited);

			oldName = editedFloat.getName();
			editedFloat.setName(newName);

			deleteLineFromFile(lineNumber);
			addFloatTaskToFile(editedFloat);
		} else {
			return MESSAGE_ERROR_REPLACE_NAME;
		}

		return String.format(MESSAGE_EDIT_NAME, typeToBeEdited, oldName,
				newName);
	}

	private boolean isValidLineNumber(int lineNumber,
			ArrayList<String> fileContents) {
		if (lineNumber <= PARAM_LINE_NUMBER_ZERO) {
			return false;
		} else if (lineNumber > fileContents.size()) {
			return false;
		} else {
			return true;
		}
	}

	private String showAllFileContents() throws IOException {
		String fileContents = readContentFromFile();

		return fileContents;
	}

	private String readContentFromFile() throws IOException {
		StringBuilder fileContents = new StringBuilder();

		readLineByLine(fileContents);

		if (fileContents.length() == PARAM_EMPTY_STRING_BUILDER) {
			return MESSAGE_EMPTY_FILE;
		}

		return fileContents.toString();
	}

	// To refactor and improve
	private String markAsCompleted(int lineNumber) throws IOException {
		ArrayList<String> fileContents = new ArrayList<String>();

		addFileContentsToArrayList(fileContents);

		if (!isValidLineNumber(lineNumber, fileContents)) {
			return String.format(MESSAGE_ERROR_INVALID_LINE_ACCESS, lineNumber);
		}

		String lineToMarkDone = fileContents.get(lineNumber - 1);
		String typeToMarkDone = getFirstWord(lineToMarkDone);

		if (typeToMarkDone.equals(STRING_TASK)) {
			Task choosenTask = new Task(lineToMarkDone);

			if (choosenTask.isDone()) {
				return String.format(MESSAGE_ERROR_ALREADY_DONE,
						typeToMarkDone, choosenTask.getName());
			}

			choosenTask.setDone(true);

			deleteLineFromFile(lineNumber);
			addTaskToFile(choosenTask);

			return String.format(MESSAGE_MARK_DONE, typeToMarkDone,
					choosenTask.getName());
		} else if (typeToMarkDone.equals(STRING_EVENT)) {
			Event choosenEvent = new Event(lineToMarkDone);

			if (choosenEvent.isDone()) {
				return String.format(MESSAGE_ERROR_ALREADY_DONE,
						typeToMarkDone, choosenEvent.getName());
			}

			choosenEvent.setDone(true);

			deleteLineFromFile(lineNumber);
			addEventToFile(choosenEvent);

			return String.format(MESSAGE_MARK_DONE, typeToMarkDone,
					choosenEvent.getName());
		} else if (typeToMarkDone.equals(STRING_FLOAT_TASK)) {
			FloatingTask choosenFloat = new FloatingTask(lineToMarkDone);

			if (choosenFloat.isDone()) {
				return String.format(MESSAGE_ERROR_ALREADY_DONE,
						typeToMarkDone, choosenFloat.getName());
			}

			choosenFloat.setDone(true);

			deleteLineFromFile(lineNumber);
			addFloatTaskToFile(choosenFloat);

			return String.format(MESSAGE_MARK_DONE, typeToMarkDone,
					choosenFloat.getName());
		} else {
			return MESSAGE_ERROR_MARK_DONE;
		}
	}

	private void readLineByLine(StringBuilder fileContents) throws IOException {
		initialiseReader();

		while (true) {
			String lineRead = fileReader.readLine();

			if (lineRead == null) {
				break;
			} else {
				fileContents.append(lineRead);
				fileContents.append(NEWLINE);
			}
		}

		fileReader.close();
	}

	// Use this instead of the one below.
	private void addEventToFile(Event newEvent) throws IOException {
		ArrayList<String> fileContents = new ArrayList<String>();

		boolean hasAddedLine = false;

		String lineToAdd = newEvent.toString();

		initialiseReader();

		while (true) {
			String lineRead = fileReader.readLine();
			if (lineRead == null) {
				if (!hasAddedLine) {
					fileContents.add(lineToAdd);
				}
				break;
			} else if (hasAddedLine) {
				fileContents.add(lineRead);
			} else {
				String typeOfLineRead = getFirstWord(lineRead);
				if (!typeOfLineRead.equals(STRING_EVENT)) {
					fileContents.add(lineRead);
				} else {
					Event currentReadEvent = new Event(lineRead);
					if (newEvent.compareTo(currentReadEvent) > 0) {
						fileContents.add(lineRead);
					} else {
						fileContents.add(lineToAdd);
						fileContents.add(lineRead);
						hasAddedLine = true;
					}
				}
			}
		}

		writeContentsToFile(fileContents);
		fileReader.close();
	}

	// Use this to add task instead the other one which takes in string.
	private void addTaskToFile(Task newTask) throws IOException {
		ArrayList<String> fileContents = new ArrayList<String>();

		boolean hasAddedLine = false;

		String lineToAdd = newTask.toString();

		initialiseReader();

		while (true) {
			String lineRead = fileReader.readLine();
			if (lineRead == null) {
				if (!hasAddedLine) {
					fileContents.add(lineToAdd);
				}
				break;
			} else if (hasAddedLine) {
				fileContents.add(lineRead);
			} else {
				String typeOfLineRead = getFirstWord(lineRead);
				if (typeOfLineRead.equals(STRING_FLOAT_TASK)) {
					fileContents.add(lineRead);
				} else if (typeOfLineRead.equals(STRING_TASK)) {
					Task currentReadTask = new Task(lineRead);
					if (newTask.compareTo(currentReadTask) > 0) {
						fileContents.add(lineRead);
					} else {
						fileContents.add(lineToAdd);
						fileContents.add(lineRead);
						hasAddedLine = true;
					}
				} else {
					fileContents.add(lineToAdd);
					fileContents.add(lineRead);
					hasAddedLine = true;
				}
			}
		}

		writeContentsToFile(fileContents);
		fileReader.close();
	}

	private void writeContentsToFile(ArrayList<String> fileContents)
			throws FileNotFoundException {
		initialiseWriter();

		for (int i = 0; i < fileContents.size(); i++) {
			fileWriter.println(fileContents.get(i));
		}
		fileWriter.close();
	}

	private void writeContentsToFile(String textToWrite)
			throws FileNotFoundException {
		initialiseWriter();

		fileWriter.println(textToWrite);

		fileWriter.close();
	}

	// Use this to add float task instead of the other one which takes in
	// String.
	private void addFloatTaskToFile(FloatingTask newFloatTask)
			throws IOException {
		ArrayList<String> fileContents = new ArrayList<String>();

		boolean hasAddedLine = false;

		String lineToAdd = newFloatTask.toString();

		initialiseReader();

		while (true) {
			String lineRead = fileReader.readLine();
			if (lineRead == null) {
				if (!hasAddedLine) {
					fileContents.add(lineToAdd);
				}
				break;
			} else if (hasAddedLine) {
				fileContents.add(lineRead);
			} else {
				String typeOfLineRead = getFirstWord(lineRead);

				if (!typeOfLineRead.equals(STRING_FLOAT_TASK)) {
					fileContents.add(lineToAdd);
					fileContents.add(lineRead);
					hasAddedLine = true;
				} else {
					FloatingTask currentReadTask = new FloatingTask(lineRead);
					if (currentReadTask.compareTo(newFloatTask) < 0) {
						fileContents.add(lineRead);
					} else {
						fileContents.add(lineToAdd);
						fileContents.add(lineRead);
						hasAddedLine = true;
					}
				}
			}
		}

		writeContentsToFile(fileContents);
		fileReader.close();

	}
	
	private String changeFilePath(String newLocation) {
		File toBeReplaced = new File(filePath);
		ArrayList<String> fileContents = new ArrayList<String>();
		
		try {
			addFileContentsToArrayList(fileContents);
			
			updateFilePath(newLocation);
			
			if (filePath.equals(toBeReplaced.getPath())) {
				return MESSAGE_SAME_FILE;
			}

			createFile();		
			writeContentsToFile(fileContents);
			updateConfigFile(newLocation);
		} catch (IOException exception) {
			filePath = toBeReplaced.getPath();
			return MESSAGE_ERROR_CHANGING_FILE_PATH;
		}
		
		toBeReplaced.delete();
		
		return String.format(MESSAGE_CHANGE_STORAGE_SUCCESS, getFilePath());
	}

	private void updateFilePath(String newLocation) {
		if (hasDirectorySpecifiedInPath(newLocation)) {
			filePath = String.format(COLLATED_FILE_PATH_FORMAT, newLocation, FILE_NAME);
		} else {
			filePath = FILE_NAME;
		}
	}

	private String getFirstWord(String text) {
		String parameters[] = text.split(TEXT_FILE_DIVIDER);

		return parameters[0];
	}
	
	private String getPathFromConfig() throws FileSystemException {
		File file = new File(CONFIG_FILE_PATH);
		String readFilePath;

		createDirectoryIfMissing(file);

		createNewFileIfFileDoesNotExist(file);
		
		try {
			readFilePath = readFirstLine(CONFIG_FILE_PATH);
		} catch (IOException exception) {
			return FILE_NAME;
		}
		
		if (!hasDirectorySpecifiedInPath(readFilePath)) {
			return FILE_NAME;
		}
		
		return String.format(COLLATED_FILE_PATH_FORMAT, readFilePath, FILE_NAME);
	}

	private boolean hasDirectorySpecifiedInPath(String readFilePath) {
		if (readFilePath == null) {
			return false;
		} else if (readFilePath.trim().equals(EMPTY_STRING)) {
			return false;
		} else {
			return true;
		}
	}
	
	private String readFirstLine(String configFilePath) throws IOException {
		FileReader fileToBeRead = new FileReader(configFilePath);
		BufferedReader configFileReader = new BufferedReader(fileToBeRead);
		
		String lineRead = configFileReader.readLine();
		configFileReader.close();
		
		return lineRead;		
	}

	private void createFile() throws FileSystemException {
		File file = new File(filePath);
		
		if (hasDirectorySpecifiedInPath()) {
			createDirectoryIfMissing(file);
		}

		createNewFileIfFileDoesNotExist(file);
	}

	private boolean hasDirectorySpecifiedInPath() {
		return !filePath.equals(FILE_NAME);
	}

	private void createDirectoryIfMissing(File file) {
		file.getParentFile().mkdirs();
	}

	private void createNewFileIfFileDoesNotExist(File file) throws FileSystemException {
		try {
			file.createNewFile();
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_CREATE_FILE);
		}
	}

	private void updateConfigFile(String newLocation)
			throws FileNotFoundException {
		PrintWriter configWriter = new PrintWriter(CONFIG_FILE_PATH);

		configWriter.println(newLocation);

		configWriter.close();
	}

	private void initialiseReader() throws FileNotFoundException {
		FileReader fileToBeRead = new FileReader(filePath);
		fileReader = new BufferedReader(fileToBeRead);
	}

	private void initialiseWriter() throws FileNotFoundException {
		fileWriter = new PrintWriter(filePath);
	}
}
