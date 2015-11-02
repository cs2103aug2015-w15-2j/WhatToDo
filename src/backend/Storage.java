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
	private static final String COLLATED_FILE_PATH_FORMAT = "%s"
			+ File.separator + "%s";
	private static final String CONFIG_FILE_PATH = "config" + File.separator
			+ "config.txt";
	private static final String ALIAS_FILE_PATH = "config" + File.separator + "alias.txt";

	private static final String MESSAGE_CHANGE_STORAGE_SUCCESS = "You are now writing to \"%s\"";
	private static final String MESSAGE_SAME_FILE = "Your file location remains unchanged.";

	private static final String MESSAGE_ERROR_CREATE_FILE = "Error encountered when creating file.";
	private static final String MESSAGE_ERROR_READ_FILE = "Error encountered when reading file.";
	private static final String MESSAGE_ERROR_WRITE_FILE = "Error encountered when writing to file.";
	private static final String MESSAGE_ERROR_INVALID_LINE_ACCESS = "Cannot find line %d in text file.";
	private static final String MESSAGE_ERROR_INVALID_LINE_ACCESS_FLOAT = "Line %d is not a floating task!";
	private static final String MESSAGE_ERROR_INVALID_LINE_ACCESS_TASK = "Line %d is not a task!";
	private static final String MESSAGE_ERROR_INVALID_LINE_ACCESS_EVENT = "Line %d is not an event!";
	private static final String MESSAGE_ERROR_INVALID_EVENT_DATE_RANGE = "Event start date/time is not earlier than end date/time.";
	private static final String MESSAGE_ERROR_ALREADY_DONE = "Error encountered: the %s \"%s\" has already been completed.";

	private static final String MESSAGE_ERROR_CHANGING_FILE_PATH = "File location specified is invalid.";
	private static final String MESSAGE_ERROR_CHANGING_FILE_PATH_UNKNOWN = "Unknown error encountered when changing file path.";
	private static final String MESSAGE_ERROR_CHANGING_FILE_PATH_CONFLICT = "Conflicting text files found in"
			+ " both old and new file paths. Please delete either one before continuing.";

	private static final String TEXT_FILE_DIVIDER = ";";
	
	private static final String FORMAT_ALIAS = "%s" + TEXT_FILE_DIVIDER + "%s";

	private static final String STRING_TASK = "task";
	private static final String STRING_FLOAT_TASK = "float";
	private static final String STRING_EVENT = "event";

	private static final String NEWLINE = "\n";
	private static final String EMPTY_STRING = "";

	private static final int PARAM_LINE_NUMBER_ZERO = 0;
	private static final int PARAM_START_LOOP_ZERO = 0;
	private static final int PARAM_LESS_ONE = 1;
	private static final int PARAM_DOES_NOT_EXIST = -1;
	private static final int PARAM_FIRST_WORD = 0;
	private static final int PARAM_SECOND_WORD = 1;
	private static final int PARAM_COMPARE_TO = 0;

	// Used to offset difference in 1-based and 0-based counting.
	private static final int PARAM_OFFSET = 1;

	// Situations for changing file path location.
	private static final int SITUATION_MOVE_FILE = 0;
	private static final int SITUATION_CHANGE_LOCATION = 1;
	private static final int SITUATION_CONFLICT_FILES = 2;
	private static final int SITUATION_SAME_FILE = 3;

	// This string stores the whole file name with directory.
	private String filePath;

	// For reading from file.
	private BufferedReader fileReader;

	// For writing into file.
	private PrintWriter fileWriter;

	/**
	 * Constructor
	 * 
	 * @throws FileSystemException
	 *             when error in creating file.
	 */
	public Storage() throws FileSystemException {
		filePath = getPathFromConfig();

		createFile();
		createAliasFile();
	}

	public String getFilePath() {
		return getAbsoluteFilePath();
	}

	/**
	 * Changes location to store text file to given newLocation string.
	 * Directories/Folders will be created if missing.
	 * 
	 * Here are some examples of what newLocation parameter can be: " " - store
	 * text file at default location. "TaskList" - store text file in folder
	 * called TaskList. TaskList created at default location. "One\Two" - store
	 * text file in folder Two which is nested in folder One (at default
	 * location) "C:\Users\Jim\DropBox" - store text file in DropBox.
	 * 
	 * Returns error message when given newLocation is invalid name for
	 * directory or file.
	 * 
	 * @param newLocation
	 *            file path of location to change to
	 * @return status message to indicate if changing is a success.
	 */
	public String changeFileStorageLocation(String newLocation) {
		String feedback = changeFilePath(newLocation);

		return feedback;
	}

	/**
	 * Adds a task with deadline to the text file (to-do list). Tasks in the
	 * file will be sorted by deadlines.
	 * 
	 * @param newTask
	 *            the Task object to be added to file.
	 * @throws FileSystemException
	 *             when encounter error in reading file.
	 */
	public void addTask(Task newTask) throws FileSystemException {
		try {
			addTaskToFile(newTask);
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_READ_FILE);
		}
	}

	/**
	 * Adds a floating task to the text file (to-do list). Floating tasks in the
	 * file will be sorted in alphabetical order.
	 * 
	 * @param taskToAdd
	 *            the FloatingTask object to be added to file.
	 * @throws FileSystemException
	 *             when encounter error in reading file.
	 */
	public void addFloatingTask(FloatingTask taskToAdd)
			throws FileSystemException {
		try {
			addFloatTaskToFile(taskToAdd);
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_READ_FILE);
		}
	}

	/**
	 * Adds an event to the text file (to-do-list). Events in the file will be
	 * sorted by start date/time, followed by end date/time.
	 * 
	 * @param newEvent
	 *            the Event object to be added to file.
	 * @throws FileSystemException
	 *             when encounter error in reading file.
	 */
	public void addEvent(Event newEvent) throws FileSystemException {
		try {
			addEventToFile(newEvent);
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_READ_FILE);
		}
	}

	/**
	 * Deletes a line in the text file with specified line number. (1-based
	 * counting)
	 * 
	 * @param lineNumber
	 *            line number in text file to be deleted.
	 * @return the text at specified line number.
	 * @throws FileSystemException
	 *             when line number less than 0 or more than number of lines
	 *             present in text file, or when error in reading file.
	 */
	public String deleteLine(int lineNumber) throws FileSystemException {
		try {
			String deletedLine = deleteLineFromFile(lineNumber);

			return deletedLine;
		} catch (IllegalArgumentException exception) {
			throw new FileSystemException(exception.getMessage());
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_READ_FILE);
		}
	}

	/**
	 * Displays the contents in the text file (to-do list).
	 * 
	 * @return the contents of text file in a String.
	 * @throws FileSystemException
	 *             when encounter error in reading file.
	 */
	public String display() throws FileSystemException {
		try {
			return showAllFileContents();
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_READ_FILE);
		}
	}

	/**
	 * Replaces task/event name in text file of given line number. (1-based
	 * counting)
	 * 
	 * @param lineNumber
	 *            line number in text file to be replaced.
	 * @param newName
	 *            new event/task name to replace old name with.
	 * @return the old replaced task/event name at specified line number.
	 * @throws FileSystemException
	 *             when line number less than 0 or more than number of lines
	 *             present in text file, or when error in reading file.
	 */
	public String editName(int lineNumber, String newName)
			throws FileSystemException {
		try {
			String lineToReplace = replaceName(lineNumber, newName);

			return lineToReplace;
		} catch (IllegalArgumentException exception) {
			throw new FileSystemException(exception.getMessage());
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_READ_FILE);
		}
	}

	/**
	 * Replaces task deadline in text file of given line number. (1-based
	 * counting)
	 * 
	 * @param lineNumber
	 *            line number in text file to be replaced.
	 * @param newDeadline
	 *            new task deadline to replace old deadline with.
	 * @return the deadline at specified line number before being replaced.
	 * @throws FileSystemException
	 *             when line number less than 0 or more than number of lines
	 *             present in text file, or when error in reading file, or when
	 *             the object at line number is not a task.
	 */
	public String editTaskDeadline(int lineNumber, Date newDeadline)
			throws FileSystemException {
		try {
			String lineToReplace = replaceTaskDeadline(lineNumber, newDeadline);

			return lineToReplace;
		} catch (IllegalArgumentException exception) {
			throw new FileSystemException(exception.getMessage());
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_READ_FILE);
		}
	}

	/**
	 * Replaces event start date in text file of given line number. (1-based
	 * counting)
	 * 
	 * @param lineNumber
	 *            line number in text file to be replaced.
	 * @param newDate
	 *            new event start date to replace old start date with.
	 * @return event start date at specified line number before replacing.
	 * @throws FileSystemException
	 *             when line number less than 0 or more than number of lines
	 *             present in text file, or when error in reading file, or when
	 *             the object at line number is not an event, or when event
	 *             start later than event end.
	 */
	public String editEventStartDate(int lineNumber, Date newDate)
			throws FileSystemException {
		try {
			String lineToReplace = replaceEventStartDate(lineNumber, newDate);

			return lineToReplace;
		} catch (IllegalArgumentException exception) {
			throw new FileSystemException(exception.getMessage());
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_READ_FILE);
		}
	}

	/**
	 * Replaces event end date in text file of given line number. (1-based
	 * counting)
	 * 
	 * @param lineNumber
	 *            line number in text file to be replaced.
	 * @param newDate
	 *            new event end date to replace old end date with.
	 * @return event end date at specified line number before replacing.
	 * @throws FileSystemException
	 *             when line number less than 0 or more than number of lines
	 *             present in text file, or when error in reading file, or when
	 *             the object at line number is not an event, or when event
	 *             start later than event end.
	 */
	public String editEventEndDate(int lineNumber, Date newDate)
			throws FileSystemException {
		try {
			String lineToReplace = replaceEventEndDate(lineNumber, newDate);

			return lineToReplace;
		} catch (IllegalArgumentException exception) {
			throw new FileSystemException(exception.getMessage());
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_READ_FILE);
		}
	}

	/**
	 * Replaces event start time in text file of given line number. (1-based
	 * counting)
	 * 
	 * @param lineNumber
	 *            line number in text file to be replaced.
	 * @param newTime
	 *            new event start time to replace old start time with.
	 * @return event start time at specified line number before replacing.
	 * @throws FileSystemException
	 *             when line number less than 0 or more than number of lines
	 *             present in text file, or when error in reading file, or when
	 *             the object at line number is not an event, or when event
	 *             start later than event end.
	 */
	public String editEventStartTime(int lineNumber, String newTime)
			throws FileSystemException {
		try {
			String lineToReplace = replaceEventStartTime(lineNumber, newTime);

			return lineToReplace;
		} catch (IllegalArgumentException exception) {
			throw new FileSystemException(exception.getMessage());
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_READ_FILE);
		}
	}

	/**
	 * Replaces event end time in text file of given line number. (1-based
	 * counting)
	 * 
	 * @param lineNumber
	 *            line number in text file to be replaced.
	 * @param newTime
	 *            new event end time to replace old end time with.
	 * @return event end time at specified line number before replacing.
	 * @throws FileSystemException
	 *             when line number less than 0 or more than number of lines
	 *             present in text file, or when error in reading file, or when
	 *             the object at line number is not an event, or when event
	 *             start later than event end.
	 */
	public String editEventEndTime(int lineNumber, String newTime)
			throws FileSystemException {
		try {
			String lineToReplace = replaceEventEndTime(lineNumber, newTime);

			return lineToReplace;
		} catch (IllegalArgumentException exception) {
			throw new FileSystemException(exception.getMessage());
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_READ_FILE);
		}
	}

	/**
	 * Overwrites the entire text file with given string input.
	 * 
	 * @param textToWrite
	 *            text used to overwrite the text file with.
	 * @throws FileSystemException
	 *             when unable to write to file.
	 */
	public void overwriteFile(String textToWrite) throws FileSystemException {
		try {
			writeContentsToFile(textToWrite);
		} catch (FileNotFoundException exception) {
			throw new FileSystemException(MESSAGE_ERROR_WRITE_FILE);
		}
	}

	/**
	 * Marks the task/event/float at given line number in text file as done.
	 * (1-based counting)
	 * 
	 * @param lineNumber
	 *            line number in text file to be marked as done.
	 * @return the text at specified line number before marking as done.
	 * @throws FileSystemException
	 *             when line number less than 0 or more than number of lines
	 *             present in text file, or when error in reading file, or when
	 *             task/event is already done.
	 */
	public String markAsDone(int lineNumber) throws FileSystemException {
		try {
			String markedLine = markAsCompleted(lineNumber);

			return markedLine;
		} catch (IllegalArgumentException exception) {
			throw new FileSystemException(exception.getMessage());
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_READ_FILE);
		}
	}
	
	/**
	 * Retrieves the type of item (float/task/event) in the text file with specified line number. (1-based
	 * counting)
	 * 
	 * @param lineNumber
	 *            line number in text file to be found/queried.
	 * @return the item type at specified line number.
	 * @throws FileSystemException
	 *             when line number less than 0 or more than number of lines
	 *             present in text file, or when error in reading file.
	 */
	public String findTypeInLine(int lineNumber) throws FileSystemException {
		try {
			String itemType = findItemTypeFromFile(lineNumber);

			return itemType;
		} catch (IllegalArgumentException exception) {
			throw new FileSystemException(exception.getMessage());
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_READ_FILE);
		}
	}
	
	/**
	 * Converts floating task at specified line number into a task with specified deadline.
	 * The newly converted task will be considered undone regardless of its status as a floating
	 * task. (1-based counting)
	 * 
	 * @param lineNumber    line number in text file to be converted.
	 * @param deadline      given deadline for converted task.
	 * @return              the text at specified line number before conversion.
	 * @throws FileSystemException
	 *             when line number less than 0 or more than number of lines
	 *             present in text file, or when error in reading file, or when
	 *             the object at line number is not a floating task.
	 */
	public String convertFloatToTask(int lineNumber, Date deadline)
			throws FileSystemException {
		try {
			String lineToConvert = changeFloatToTask(lineNumber, deadline);

			return lineToConvert;
		} catch (IllegalArgumentException exception) {
			throw new FileSystemException(exception.getMessage());
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_READ_FILE);
		}
	}
	
	/**
	 * Converts floating task at specified line number into an event with specified start date and time
	 * and end date and time. The newly converted event will be considered undone regardless of its status
	 * as a floating task. (1-based counting)
	 * 
	 * @param lineNumber    line number in text file to be converted.
	 * @param startDate     given start date for converted event.
	 * @param startTime     given start time for converted event.
	 * @param endDate       given end date for converted event.
	 * @param endTime       given end time for converted event.
	 * @return              the text at specified line number before conversion.
	 * @throws FileSystemException
	 *             when line number less than 0 or more than number of lines
	 *             present in text file, or when error in reading file, or when
	 *             the object at line number is not a floating task.
	 */ 
	public String convertFloatToEvent(int lineNumber, Date startDate, String startTime, 
			Date endDate, String endTime) throws FileSystemException {
		try {
			String lineToConvert = changeFloatToEvent(lineNumber, startDate, startTime, endDate, endTime);

			return lineToConvert;
		} catch (IllegalArgumentException exception) {
			throw new FileSystemException(exception.getMessage());
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_READ_FILE);
		}
	}
	
	/**
	 * Retrieves the attribute at specified line number of specified type.
	 * 
	 * @param lineNumber              line number in text file to be retrieved.
	 * @param type                    type of attribute to get from line.
	 * @return                        the attribute of given type at given line. null if no such
	 *                                attribute exists at given line.
	 * @throws FileSystemException    when line number less than 0 or more than number of lines
	 *                                present in text file, or when error in reading file.
	 */
	public String getAttribute(int lineNumber, int type) throws FileSystemException {
		try {
			String attribute = findAttribute(lineNumber, type);
			
			return attribute;
		} catch (IllegalArgumentException exception) {
			throw new FileSystemException(exception.getMessage());
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_READ_FILE);
		}
	}
	
	public void addToAliasFile(String aliasLine, String actualCommandType) throws FileSystemException {
		try {
			updateAliasFile(aliasLine, actualCommandType); 
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_WRITE_FILE);
		}
	}
	
	public void deleteFromAliasFile(String aliasLine) throws FileSystemException {
		try {
			removeFromAliasFile(aliasLine); 
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_WRITE_FILE);
		}
	}
	
	public String readAliasFile() throws FileSystemException {
		try {
			String fileContents = displayAliasFile(); 
			
			return fileContents;
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_WRITE_FILE);
		}
	}

	/*
	 * Private Methods Start Here.
	 */
	
	private void updateAliasFile(String aliasLine, String actualCommandType)
			throws IOException {
		ArrayList<String> fileContents = readAliasFileToArrayList();
		
		String lineToAdd = String.format(FORMAT_ALIAS, aliasLine, actualCommandType);
		fileContents.add(lineToAdd);

		int lastLine = fileContents.size() - PARAM_LESS_ONE;
		
		PrintWriter configWriter = new PrintWriter(ALIAS_FILE_PATH);

		for (int i = PARAM_START_LOOP_ZERO; i < lastLine; i++) {
			configWriter.println(fileContents.get(i));
		}
		configWriter.print(fileContents.get(lastLine));
		
		configWriter.close();
	}
	
	private void removeFromAliasFile(String aliasLine) throws IOException {
		ArrayList<String> fileContents = readAliasFileToArrayList();
		
		for (int i = PARAM_START_LOOP_ZERO; i < fileContents.size(); i++) {
			String aliasFromFile = getFirstWord(fileContents.get(i));
			if (aliasFromFile.equals(aliasLine)) {
				fileContents.remove(i);
				break;
			}
		}

		int lastLine = fileContents.size() - PARAM_LESS_ONE;
		
		PrintWriter configWriter = new PrintWriter(ALIAS_FILE_PATH);

		if (lastLine == PARAM_DOES_NOT_EXIST) {
			configWriter.print(EMPTY_STRING);
			configWriter.close();
			return;
		}	

		for (int i = PARAM_START_LOOP_ZERO; i < lastLine; i++) {
			configWriter.println(fileContents.get(i));
		}
		configWriter.print(fileContents.get(lastLine));
		
		configWriter.close();
	}
	
	private String displayAliasFile() throws IOException {
		FileReader fileToBeRead = new FileReader(ALIAS_FILE_PATH);
		BufferedReader aliasFileReader = new BufferedReader(fileToBeRead);
		
		StringBuilder fileContents = new StringBuilder();
		
		while (true) {
			String lineRead = aliasFileReader.readLine();

			if (isEndOfFile(lineRead)) {
				break;
			} else {
				fileContents.append(lineRead);
				fileContents.append(NEWLINE);
			}
		}
		
		aliasFileReader.close();
		
		return fileContents.toString();
	}
	
	private ArrayList<String> readAliasFileToArrayList()
			throws IOException {
		FileReader fileToBeRead = new FileReader(ALIAS_FILE_PATH);
		BufferedReader aliasFileReader = new BufferedReader(fileToBeRead);
		
		ArrayList<String> fileContents = new ArrayList<String>();
		
		while (true) {
			String lineRead = aliasFileReader.readLine();

			if (isEndOfFile(lineRead)) {
				break;
			} else {
				fileContents.add(lineRead);
			}
		}
		
		aliasFileReader.close();
		return fileContents;
	}
	
	private String findAttribute(int lineNumber, int type) throws IOException, IllegalArgumentException {
		ArrayList<String> fileContents = new ArrayList<String>();
		
		addFileContentsToArrayList(fileContents);
		
		throwIllegalArgExceptionIfInvalid(lineNumber, fileContents);
		
		String lineToFind = fileContents.get(lineNumber - PARAM_OFFSET);
		
		int numParameters = countParameters(lineToFind);
		
		String attribute;
		if (numParameters > type && type >= PARAM_FIRST_WORD) {
			attribute = getSpecificWord(type, lineToFind);
		} else {
			attribute = null;
		}
		
		return attribute;
	}

	private void addFloatTaskToFile(FloatingTask newFloatTask)
			throws IOException {
		ArrayList<String> fileContents = new ArrayList<String>();

		initialiseReader();

		addFloatToCorrectPlace(newFloatTask, fileContents);

		writeContentsToFile(fileContents);
		fileReader.close();
	}

	private void addFloatToCorrectPlace(FloatingTask newFloatTask,
			ArrayList<String> fileContents) throws IOException {
		boolean hasAddedLine = false;
		String lineToAdd = newFloatTask.toString();

		while (true) {
			String lineRead = fileReader.readLine();
			if (isEndOfFile(lineRead)) {
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
					if (currentReadTask.compareTo(newFloatTask) < PARAM_COMPARE_TO) {
						fileContents.add(lineRead);
					} else {
						fileContents.add(lineToAdd);
						fileContents.add(lineRead);
						hasAddedLine = true;
					}
				}
			}
		}
	}

	private void addTaskToFile(Task newTask) throws IOException {
		ArrayList<String> fileContents = new ArrayList<String>();

		initialiseReader();

		addTaskToCorrectPlace(newTask, fileContents);

		writeContentsToFile(fileContents);
		fileReader.close();
	}

	private void addTaskToCorrectPlace(Task newTask,
			ArrayList<String> fileContents) throws IOException {
		boolean hasAddedLine = false;

		String lineToAdd = newTask.toString();

		while (true) {
			String lineRead = fileReader.readLine();
			if (isEndOfFile(lineRead)) {
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
					if (newTask.compareTo(currentReadTask) > PARAM_COMPARE_TO) {
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
	}

	private void addEventToFile(Event newEvent) throws IOException {
		ArrayList<String> fileContents = new ArrayList<String>();
		initialiseReader();

		addEventToCorrectPlace(newEvent, fileContents);

		writeContentsToFile(fileContents);
		fileReader.close();
	}

	private void addEventToCorrectPlace(Event newEvent,
			ArrayList<String> fileContents) throws IOException {
		boolean hasAddedLine = false;

		String lineToAdd = newEvent.toString();

		while (true) {
			String lineRead = fileReader.readLine();
			if (isEndOfFile(lineRead)) {
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
					if (newEvent.compareTo(currentReadEvent) > PARAM_COMPARE_TO) {
						fileContents.add(lineRead);
					} else {
						fileContents.add(lineToAdd);
						fileContents.add(lineRead);
						hasAddedLine = true;
					}
				}
			}
		}
	}

	private String deleteLineFromFile(int lineNumber) throws IOException,
			IllegalArgumentException {
		ArrayList<String> fileContents = new ArrayList<String>();

		addFileContentsToArrayList(fileContents);

		throwIllegalArgExceptionIfInvalid(lineNumber, fileContents);

		String lineToDelete = fileContents.remove(lineNumber - PARAM_OFFSET);

		writeContentsToFile(fileContents);

		return lineToDelete;
	}

	// To refactor and improve
	private String replaceName(int lineNumber, String newName)
			throws IOException, IllegalArgumentException {
		ArrayList<String> fileContents = new ArrayList<String>();

		addFileContentsToArrayList(fileContents);

		throwIllegalArgExceptionIfInvalid(lineNumber, fileContents);

		String lineToBeEdited = fileContents.get(lineNumber - PARAM_OFFSET);
		String typeToBeEdited = getFirstWord(lineToBeEdited);
		String nameBeforeEdit = getSecondWord(lineToBeEdited);

		if (typeToBeEdited.equals(STRING_TASK)) {
			Task editedTask = new Task(lineToBeEdited);

			editedTask.setName(newName);

			deleteLineFromFile(lineNumber);
			addTaskToFile(editedTask);
		} else if (typeToBeEdited.equals(STRING_EVENT)) {
			Event editedEvent = new Event(lineToBeEdited);

			editedEvent.setName(newName);

			deleteLineFromFile(lineNumber);
			addEventToFile(editedEvent);
		} else if (typeToBeEdited.equals(STRING_FLOAT_TASK)) {
			FloatingTask editedFloat = new FloatingTask(lineToBeEdited);

			editedFloat.setName(newName);

			deleteLineFromFile(lineNumber);
			addFloatTaskToFile(editedFloat);
		} else {
			throw new IOException();
		}

		return nameBeforeEdit;
	}

	// To refactor and improve
	private String replaceTaskDeadline(int lineNumber, Date newDeadline)
			throws IOException, IllegalArgumentException {
		ArrayList<String> fileContents = new ArrayList<String>();

		addFileContentsToArrayList(fileContents);

		throwIllegalArgExceptionIfInvalid(lineNumber, fileContents);

		String lineToBeEdited = fileContents.get(lineNumber - PARAM_OFFSET);
		String typeToBeEdited = getFirstWord(lineToBeEdited);
		String oldDeadline;

		if (typeToBeEdited.equals(STRING_TASK)) {
			Task editedTask = new Task(lineToBeEdited);

			oldDeadline = editedTask.getDeadline().getFullDate();
			editedTask.setDeadline(newDeadline);

			deleteLineFromFile(lineNumber);
			addTaskToFile(editedTask);
		} else {
			String errorMessage = String.format(
					MESSAGE_ERROR_INVALID_LINE_ACCESS_TASK, lineNumber);
			throw new IllegalArgumentException(errorMessage);
		}

		return oldDeadline;
	}

	// To refactor and improve
	private String replaceEventStartDate(int lineNumber, Date newStartDate)
			throws IOException, IllegalArgumentException {
		ArrayList<String> fileContents = new ArrayList<String>();

		addFileContentsToArrayList(fileContents);

		throwIllegalArgExceptionIfInvalid(lineNumber, fileContents);

		String lineToBeEdited = fileContents.get(lineNumber - PARAM_OFFSET);
		String typeToBeEdited = getFirstWord(lineToBeEdited);
		String oldStartDate;

		if (typeToBeEdited.equals(STRING_EVENT)) {
			Event editedEvent = new Event(lineToBeEdited);

			if (!isValidStartAndEnd(newStartDate,
					editedEvent.getEventStartTime(),
					editedEvent.getEventEndDate(),
					editedEvent.getEventEndTime())) {
				throw new IllegalArgumentException(
						MESSAGE_ERROR_INVALID_EVENT_DATE_RANGE);
			}

			oldStartDate = editedEvent.getEventStartDate().getFullDate();
			editedEvent.setEventStartDate(newStartDate);

			deleteLineFromFile(lineNumber);
			addEventToFile(editedEvent);
		} else {
			String errorMessage = String.format(
					MESSAGE_ERROR_INVALID_LINE_ACCESS_EVENT, lineNumber);
			throw new IllegalArgumentException(errorMessage);
		}

		return oldStartDate;
	}

	// To refactor and improve
	private String replaceEventStartTime(int lineNumber, String newStartTime)
			throws IOException, IllegalArgumentException {
		ArrayList<String> fileContents = new ArrayList<String>();

		addFileContentsToArrayList(fileContents);

		throwIllegalArgExceptionIfInvalid(lineNumber, fileContents);

		String lineToBeEdited = fileContents.get(lineNumber - PARAM_OFFSET);
		String typeToBeEdited = getFirstWord(lineToBeEdited);
		String oldStartTime;

		if (typeToBeEdited.equals(STRING_EVENT)) {
			Event editedEvent = new Event(lineToBeEdited);

			if (!isValidStartAndEnd(editedEvent.getEventStartDate(),
					newStartTime, editedEvent.getEventEndDate(),
					editedEvent.getEventEndTime())) {
				throw new IllegalArgumentException(
						MESSAGE_ERROR_INVALID_EVENT_DATE_RANGE);
			}

			oldStartTime = editedEvent.getEventStartTime();
			editedEvent.setEventStartTime(newStartTime);

			deleteLineFromFile(lineNumber);
			addEventToFile(editedEvent);
		} else {
			String errorMessage = String.format(
					MESSAGE_ERROR_INVALID_LINE_ACCESS_EVENT, lineNumber);
			throw new IllegalArgumentException(errorMessage);
		}

		return oldStartTime;
	}

	// To refactor and improve
	private String replaceEventEndDate(int lineNumber, Date newEndDate)
			throws IOException, IllegalArgumentException {
		ArrayList<String> fileContents = new ArrayList<String>();

		addFileContentsToArrayList(fileContents);

		throwIllegalArgExceptionIfInvalid(lineNumber, fileContents);

		String lineToBeEdited = fileContents.get(lineNumber - PARAM_OFFSET);
		String typeToBeEdited = getFirstWord(lineToBeEdited);
		String oldEndDate;

		if (typeToBeEdited.equals(STRING_EVENT)) {
			Event editedEvent = new Event(lineToBeEdited);

			if (!isValidStartAndEnd(editedEvent.getEventStartDate(),
					editedEvent.getEventStartTime(), newEndDate,
					editedEvent.getEventEndTime())) {
				throw new IllegalArgumentException(
						MESSAGE_ERROR_INVALID_EVENT_DATE_RANGE);
			}

			oldEndDate = editedEvent.getEventEndDate().getFullDate();
			editedEvent.setEventEndDate(newEndDate);

			deleteLineFromFile(lineNumber);
			addEventToFile(editedEvent);
		} else {
			String errorMessage = String.format(
					MESSAGE_ERROR_INVALID_LINE_ACCESS_EVENT, lineNumber);
			throw new IllegalArgumentException(errorMessage);
		}

		return oldEndDate;
	}

	// To refactor and improve
	private String replaceEventEndTime(int lineNumber, String newEndTime)
			throws IOException, IllegalArgumentException {
		ArrayList<String> fileContents = new ArrayList<String>();

		addFileContentsToArrayList(fileContents);

		throwIllegalArgExceptionIfInvalid(lineNumber, fileContents);

		String lineToBeEdited = fileContents.get(lineNumber - PARAM_OFFSET);
		String typeToBeEdited = getFirstWord(lineToBeEdited);
		String oldEndTime;

		if (typeToBeEdited.equals(STRING_EVENT)) {
			Event editedEvent = new Event(lineToBeEdited);

			if (!isValidStartAndEnd(editedEvent.getEventStartDate(),
					editedEvent.getEventStartTime(),
					editedEvent.getEventEndDate(), newEndTime)) {
				throw new IllegalArgumentException(
						MESSAGE_ERROR_INVALID_EVENT_DATE_RANGE);
			}

			oldEndTime = editedEvent.getEventEndTime();
			editedEvent.setEventEndTime(newEndTime);

			deleteLineFromFile(lineNumber);
			addEventToFile(editedEvent);
		} else {
			String errorMessage = String.format(
					MESSAGE_ERROR_INVALID_LINE_ACCESS_EVENT, lineNumber);
			throw new IllegalArgumentException(errorMessage);
		}

		return oldEndTime;
	}

	private boolean isValidStartAndEnd(Date startDate, String startTime,
			Date endDate, String endTime) {
		if (startDate.compareTo(endDate) > PARAM_COMPARE_TO) {
			return false;
		} else if (startDate.compareTo(endDate) == PARAM_COMPARE_TO) {
			return isValidTimeRange(startTime, endTime);
		} else {
			return true;
		}
	}
	
	private boolean isValidTimeRange(String startTimeString, String endTimeString) {
		int endTime = Integer.parseInt(endTimeString);
		int startTime = Integer.parseInt(startTimeString);
		
		if (endTime >= startTime) {
			return true;
		} else {
			return false;
		}
	}
	
	private String findItemTypeFromFile(int lineNumber) throws IOException, IllegalArgumentException {
		ArrayList<String> fileContents = new ArrayList<String>();
		
		addFileContentsToArrayList(fileContents);
		
		throwIllegalArgExceptionIfInvalid(lineNumber, fileContents);
		
		String lineToFind = fileContents.get(lineNumber - PARAM_OFFSET);	
		String itemType = getFirstWord(lineToFind);
		
		return itemType;
	}

	private void throwIllegalArgExceptionIfInvalid(int lineNumber,
			ArrayList<String> fileContents) {
		if (!isValidLineNumber(lineNumber, fileContents)) {
			String errorMessage = String.format(
					MESSAGE_ERROR_INVALID_LINE_ACCESS, lineNumber);
			throw new IllegalArgumentException(errorMessage);
		}
	}
	
	private String changeFloatToTask(int lineNumber, Date deadline)
			throws IOException, IllegalArgumentException {
		ArrayList<String> fileContents = new ArrayList<String>();

		addFileContentsToArrayList(fileContents);

		throwIllegalArgExceptionIfInvalid(lineNumber, fileContents);

		String lineToBeChanged = fileContents.get(lineNumber - PARAM_OFFSET);
		String typeToBeChanged = getFirstWord(lineToBeChanged);

		if (typeToBeChanged.equals(STRING_FLOAT_TASK)) {
			FloatingTask taskToBeChanged = new FloatingTask(lineToBeChanged);
			
			String taskName = taskToBeChanged.getName();
			
			Task newTask = new Task(taskName, false, deadline);

			deleteLineFromFile(lineNumber);
			addTaskToFile(newTask);
		} else {
			String errorMessage = String.format(
					MESSAGE_ERROR_INVALID_LINE_ACCESS_FLOAT, lineNumber);
			throw new IllegalArgumentException(errorMessage);
		}

		return lineToBeChanged;
	}
	
	private String changeFloatToEvent(int lineNumber, Date startDate, String startTime,
			Date endDate, String endTime) throws IOException, IllegalArgumentException {
		ArrayList<String> fileContents = new ArrayList<String>();

		addFileContentsToArrayList(fileContents);

		throwIllegalArgExceptionIfInvalid(lineNumber, fileContents);

		String lineToBeChanged = fileContents.get(lineNumber - PARAM_OFFSET);
		String typeToBeChanged = getFirstWord(lineToBeChanged);

		if (typeToBeChanged.equals(STRING_FLOAT_TASK)) {
			FloatingTask taskToBeChanged = new FloatingTask(lineToBeChanged);
			
			String taskName = taskToBeChanged.getName();
			
			Event newEvent = new Event(taskName, false, startDate, endDate, startTime, endTime);

			deleteLineFromFile(lineNumber);
			addEventToFile(newEvent);
		} else {
			String errorMessage = String.format(
					MESSAGE_ERROR_INVALID_LINE_ACCESS_FLOAT, lineNumber);
			throw new IllegalArgumentException(errorMessage);
		}

		return lineToBeChanged;
	}

	private void addFileContentsToArrayList(ArrayList<String> fileContents)
			throws IOException {
		initialiseReader();

		while (true) {
			String lineRead = fileReader.readLine();

			if (isEndOfFile(lineRead)) {
				break;
			} else {
				fileContents.add(lineRead);
			}
		}

		fileReader.close();
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

		return fileContents.toString();
	}

	private void readLineByLine(StringBuilder fileContents) throws IOException {
		initialiseReader();

		while (true) {
			String lineRead = fileReader.readLine();

			if (isEndOfFile(lineRead)) {
				break;
			} else {
				fileContents.append(lineRead);
				fileContents.append(NEWLINE);
			}
		}

		fileReader.close();
	}

	// To refactor and improve
	private String markAsCompleted(int lineNumber) throws IOException,
			IllegalArgumentException {
		ArrayList<String> fileContents = new ArrayList<String>();

		addFileContentsToArrayList(fileContents);

		throwIllegalArgExceptionIfInvalid(lineNumber, fileContents);

		String lineToMarkDone = fileContents.get(lineNumber - PARAM_OFFSET);
		String typeToMarkDone = getFirstWord(lineToMarkDone);

		if (typeToMarkDone.equals(STRING_TASK)) {
			Task choosenTask = new Task(lineToMarkDone);

			if (choosenTask.isDone()) {
				String errorMessage = String.format(MESSAGE_ERROR_ALREADY_DONE,
						typeToMarkDone, choosenTask.getName());
				throw new IllegalArgumentException(errorMessage);
			}

			choosenTask.setDone(true);

			deleteLineFromFile(lineNumber);
			addTaskToFile(choosenTask);

			return lineToMarkDone;
		} else if (typeToMarkDone.equals(STRING_EVENT)) {
			Event choosenEvent = new Event(lineToMarkDone);

			if (choosenEvent.isDone()) {
				String errorMessage = String.format(MESSAGE_ERROR_ALREADY_DONE,
						typeToMarkDone, choosenEvent.getName());
				throw new IllegalArgumentException(errorMessage);
			}

			choosenEvent.setDone(true);

			deleteLineFromFile(lineNumber);
			addEventToFile(choosenEvent);

			return lineToMarkDone;
		} else if (typeToMarkDone.equals(STRING_FLOAT_TASK)) {
			FloatingTask choosenFloat = new FloatingTask(lineToMarkDone);

			if (choosenFloat.isDone()) {
				String errorMessage = String.format(MESSAGE_ERROR_ALREADY_DONE,
						typeToMarkDone, choosenFloat.getName());
				throw new IllegalArgumentException(errorMessage);
			}

			choosenFloat.setDone(true);

			deleteLineFromFile(lineNumber);
			addFloatTaskToFile(choosenFloat);

			return lineToMarkDone;
		} else {
			throw new IOException();
		}
	}

	private boolean isEndOfFile(String lineRead) {
		return lineRead == null;
	}

	private String changeFilePath(String newLocation) {
		int situation;

		try {
			closeStreamsIfOpen();
			situation = checkSituation(newLocation);
		} catch (IOException exception) {
			return MESSAGE_ERROR_CHANGING_FILE_PATH_UNKNOWN;
		}

		String feedback;

		switch (situation) {
			case SITUATION_SAME_FILE:
				return MESSAGE_SAME_FILE;
			case SITUATION_MOVE_FILE:
				feedback = moveFile(newLocation);
				return feedback;
			case SITUATION_CHANGE_LOCATION:
				feedback = changeLocation(newLocation);
				return feedback;
			case SITUATION_CONFLICT_FILES:
				return MESSAGE_ERROR_CHANGING_FILE_PATH_CONFLICT;
			default:
				return MESSAGE_ERROR_CHANGING_FILE_PATH_UNKNOWN;
		}
	}

	private void closeStreamsIfOpen() throws IOException {
		if (fileReader != null) {
			fileReader.close();
		}
		if (fileWriter != null) {
			fileWriter.close();
		}
	}

	private String changeLocation(String newLocation) {
		File oldFilePath = new File(getAbsoluteFilePath());

		try {
			updateConfigFile(newLocation);
		} catch (FileNotFoundException exception) {
			return MESSAGE_ERROR_CHANGING_FILE_PATH_UNKNOWN;
		}

		updateFilePath(newLocation);

		oldFilePath.delete();

		return String.format(MESSAGE_CHANGE_STORAGE_SUCCESS, getFilePath());
	}

	private String moveFile(String newLocation) {
		File oldFilePath = new File(getAbsoluteFilePath());
		ArrayList<String> fileContents = new ArrayList<String>();

		try {
			addFileContentsToArrayList(fileContents);

			updateFilePath(newLocation);

			copyContentsToNewFile(newLocation, fileContents);
		} catch (IOException exception) {
			filePath = oldFilePath.getPath();
			return MESSAGE_ERROR_CHANGING_FILE_PATH;
		}

		oldFilePath.delete();

		return String.format(MESSAGE_CHANGE_STORAGE_SUCCESS, getFilePath());
	}

	private void updateFilePath(String newLocation) {
		if (hasDirectorySpecifiedInPath(newLocation)) {
			filePath = String.format(COLLATED_FILE_PATH_FORMAT, newLocation,
					FILE_NAME);
		} else {
			filePath = FILE_NAME;
		}
	}

	private int checkSituation(String newPathLocation) throws IOException {
		String newCompletePath = String.format(COLLATED_FILE_PATH_FORMAT,
				newPathLocation, FILE_NAME);

		if (isSameFile(newCompletePath)) {
			return SITUATION_SAME_FILE;
		}

		if (isExistingFile(newCompletePath)) {
			if (isEmptyFile(newCompletePath)) {
				return SITUATION_MOVE_FILE;
			} else if (isEmptyFile(filePath)) {
				return SITUATION_CHANGE_LOCATION;
			} else {
				return SITUATION_CONFLICT_FILES;
			}
		} else {
			return SITUATION_MOVE_FILE;
		}
	}

	private boolean isSameFile(String newCompletePath) {
		String nameOfOldFile = getAbsoluteFilePath();
		String nameOfNewFile = new File(newCompletePath).getAbsolutePath();

		return nameOfOldFile.equals(nameOfNewFile);
	}

	private boolean isExistingFile(String filePathLocation) {
		File fileToCheck = new File(filePathLocation);
		if (fileToCheck.exists()) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isEmptyFile(String filePathLocation) throws IOException {
		String lineRead = readFirstLine(filePathLocation);

		if (isEndOfFile(lineRead)) {
			return true;
		} else {
			return false;
		}
	}

	private void copyContentsToNewFile(String newLocation,
			ArrayList<String> fileContents) throws IOException {
		createFile();
		writeContentsToFile(fileContents);
		updateConfigFile(newLocation);
	}
	
	private String[] splitParameters(String line) {
		return line.split(TEXT_FILE_DIVIDER);
	}

	private String getFirstWord(String text) {
		return getSpecificWord(PARAM_FIRST_WORD, text);
	}
	
	private String getSecondWord(String text) {
		return getSpecificWord(PARAM_SECOND_WORD, text);
	}
	
	private String getSpecificWord(int wordNumber, String text) {
		String parameters[] = splitParameters(text);
		String specificWord = parameters[wordNumber];
		
		return specificWord;
	}
	
	private int countParameters(String text) {
		String parameters[] = splitParameters(text);
		int numberParameters = parameters.length;
		
		return numberParameters;
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

		return String
				.format(COLLATED_FILE_PATH_FORMAT, readFilePath, FILE_NAME);
	}

	private boolean hasDirectorySpecifiedInPath(String readFilePath) {
		if (isEndOfFile(readFilePath)) {
			return false;
		} else if (readFilePath.trim().equals(EMPTY_STRING)) {
			return false;
		} else {
			return true;
		}
	}

	private String readFirstLine(String givenFilePath) throws IOException {
		FileReader fileToBeRead = new FileReader(givenFilePath);
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
	
	private void createAliasFile() throws FileSystemException {
		File file = new File(ALIAS_FILE_PATH);
		
		createNewFileIfFileDoesNotExist(file);
	}

	private boolean hasDirectorySpecifiedInPath() {
		return !filePath.equals(FILE_NAME);
	}

	private void createDirectoryIfMissing(File file) {
		file.getParentFile().mkdirs();
	}

	private void createNewFileIfFileDoesNotExist(File file)
			throws FileSystemException {
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

	private String getAbsoluteFilePath() {
		return new File(filePath).getAbsolutePath();
	}

	private void initialiseReader() throws FileNotFoundException {
		FileReader fileToBeRead = new FileReader(filePath);
		fileReader = new BufferedReader(fileToBeRead);
	}

	private void initialiseWriter() throws FileNotFoundException {
		fileWriter = new PrintWriter(filePath);
	}

	private void writeContentsToFile(ArrayList<String> fileContents)
			throws FileNotFoundException {
		initialiseWriter();

		int lastLine = fileContents.size() - PARAM_LESS_ONE;

		if (lastLine == PARAM_DOES_NOT_EXIST) {
			return;
		}

		for (int i = PARAM_START_LOOP_ZERO; i < lastLine; i++) {
			fileWriter.println(fileContents.get(i));
		}
		fileWriter.print(fileContents.get(lastLine));

		fileWriter.close();
	}

	private void writeContentsToFile(String textToWrite)
			throws FileNotFoundException {
		initialiseWriter();

		String[] linesToWrite = textToWrite.split(NEWLINE);

		int lastLine = linesToWrite.length - PARAM_LESS_ONE;

		if (lastLine == PARAM_DOES_NOT_EXIST) {
			return;
		}

		for (int i = PARAM_START_LOOP_ZERO; i < lastLine; i++) {
			fileWriter.println(linesToWrite[i]);
		}
		fileWriter.print(linesToWrite[lastLine]);

		fileWriter.close();
	}
}
