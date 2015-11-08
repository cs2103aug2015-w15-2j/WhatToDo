/**
 * This class reads, writes and manipulates the text data in the text file which
 * stores the to-do list of the user.
 * 
 * @@author A0124238L
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
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import struct.FloatingTask;
import struct.Event;
import struct.Task;

public class Storage {
	private static final String FILE_NAME = "whattodo.txt";
	private static final String COLLATED_FILE_PATH_FORMAT = "%s"
			+ File.separator + "%s";

	private static final String MESSAGE_CHANGE_STORAGE_SUCCESS = "You are now writing to \"%s\"";
	private static final String MESSAGE_SAME_FILE = "Your file location remains unchanged.";

	private static final String MESSAGE_ERROR_CREATE_FILE = "Error encountered when creating file.";
	private static final String MESSAGE_ERROR_READ_FILE = "Error encountered when reading file.";
	private static final String MESSAGE_ERROR_WRITE_FILE = "Error encountered when writing to file.";
	private static final String MESSAGE_ERROR_INVALID_LINE_ACCESS = "Cannot find line %d in text file.";
	private static final String MESSAGE_ERROR_ALREADY_DONE = "Error encountered: the %s \"%s\" has already been completed.";

	private static final String MESSAGE_ERROR_CHANGING_FILE_PATH = "File location specified is invalid.";
	private static final String MESSAGE_ERROR_CHANGING_FILE_PATH_UNKNOWN = "Unknown error encountered when changing file path.";
	private static final String MESSAGE_ERROR_CHANGING_FILE_PATH_CONFLICT = "Conflicting text files found in"
			+ " both old and new file paths. Please delete either one.";
	
	private static final String MESSAGE_LOG_UNKNOWN_TYPE = "Unknown type found in line %d";
	private static final String MESSAGE_LOG_CHANGE_PATH = "Unknown Exception found";
	private static final String MESSAGE_LOG_UNKNOWN_SITUATION = "Unknown situation: %d";
	private static final String MESSAGE_LOG_LINE_VALIDATION = "Line Number: %d, file lines: %d";

	private static final String TEXT_FILE_DIVIDER = ";";

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
	private static final int PARAM_COMPARE_TO = 0;
	
	// For Logging.
	private static final Level LEVEL_TO_SHOW = Level.ALL;

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
	
	// For handling operations within config folder.
	private ConfigHandler configHandler;
	
	// For Logging
	private Logger logger;

	/**
	 * Constructor
	 * 
	 * @throws FileSystemException
	 *             when error in creating file.
	 */
	public Storage() throws FileSystemException {
		initialiseHandler();
		
		filePath = configHandler.getPathFromConfig();

		createToDoListFile();
		
		configHandler.createAliasFile();
		
		initialiseLogger();
	}

	public String getFilePath() {
		return getAbsoluteFilePath();
	}

	/**
	 * Changes location to store text file to given newLocation string.
	 * Directories/Folders will be created if missing.	 * 
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
		assert(newTask.getName() != EMPTY_STRING);
		assert(newTask.getDeadline().formatDateShort() != EMPTY_STRING);
		
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
	 * @param newFloatingTask
	 *            the FloatingTask object to be added to file.
	 * @throws FileSystemException
	 *             when encounter error in reading file.
	 */
	public void addFloatingTask(FloatingTask newFloatingTask)
			throws FileSystemException {
		assert(newFloatingTask.getName() != EMPTY_STRING);
		
		try {
			addFloatTaskToFile(newFloatingTask);
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
		assert(newEvent.getName() != EMPTY_STRING);
		assert(newEvent.getEventStartDate().formatDateShort() != EMPTY_STRING);
		assert(newEvent.getEventEndDate().formatDateShort() != EMPTY_STRING);
		assert(newEvent.getEventStartTime() != EMPTY_STRING);
		assert(newEvent.getEventEndTime() != EMPTY_STRING);
		
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
	
	/**
	 * Adds a line alias->actual command mapping to the alias file in config folder.
	 * 
	 * @param aliasLine              user-specified alias.
	 * @param actualCommandType      actual command type to be mapped to.
	 * @throws FileSystemException   when error in writing to file.
	 */
	public void addToAliasFile(String aliasLine, String actualCommandType) throws FileSystemException {
		assert(aliasLine.indexOf(TEXT_FILE_DIVIDER) == -1);
		assert(actualCommandType.indexOf(TEXT_FILE_DIVIDER) == -1);
		
		try {
			configHandler.updateAliasFile(aliasLine, actualCommandType); 
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_WRITE_FILE);
		}
	}
	
	/**
	 * Deletes a line with specified alias, removing the mapping too from alias file in
	 * config folder. This method will not do anything if specified alias is not found in
	 * file.
	 * 
	 * @param aliasLine              user-specified alias.
	 * @throws FileSystemException   when error in writing to file.
	 */
	public void deleteFromAliasFile(String aliasLine) throws FileSystemException {
		assert(aliasLine.indexOf(TEXT_FILE_DIVIDER) == -1);
		
		try {
			configHandler.removeFromAliasFile(aliasLine); 
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_WRITE_FILE);
		}
	}
	
	/**
	 * Displays the entire content of the alias file.
	 * 
	 * @return                        the contents of alias file as a String.
	 * @throws FileSystemException    when error in reading file.
	 */
	public String readAliasFile() throws FileSystemException {
		try {
			String fileContents = configHandler.displayAliasFile(); 
			
			return fileContents;
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_READ_FILE);
		}
	}
	
	/**
	 * Clears the entire content of the alias file.
	 * 
	 * @throws FileSystemException    when error in writing file.
	 */
	public void clearAliasFile() throws FileSystemException {
		try {
			configHandler.clearAliasFile();
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_WRITE_FILE);
		}
	}
	
	/**
	 * Overwrites the entire content of the alias file with given text.
	 * 
	 * @param  textToOverwrite        text to overwrite alias file with.
	 * @throws FileSystemException    when error in writing file.
	 */
	public void overwriteAliasFile(String textToOverwrite) throws FileSystemException {
		try {
			configHandler.overwriteAliasFile(textToOverwrite);
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_WRITE_FILE);
		}
	}

	// Private Methods Start Here.
	
	private void initialiseLogger() {
		logger = Logger.getLogger(Storage.class.getName());
		
		logger.setLevel(LEVEL_TO_SHOW);
		logger.setUseParentHandlers(false);
		
		ConsoleHandler handler = new ConsoleHandler();
	    handler.setLevel(LEVEL_TO_SHOW);
	    
	    logger.addHandler(handler);
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
				hasAddedLine = processAddLineForFloat(newFloatTask, fileContents, lineToAdd, lineRead);
			}
		}
	}

	private boolean processAddLineForFloat(FloatingTask newFloatTask,
			ArrayList<String> fileContents, String lineToAdd, String lineRead) {
		boolean hasAddedLine = false;
		String typeOfLineRead = getFirstWord(lineRead);

		if (!typeOfLineRead.equals(STRING_FLOAT_TASK)) {
			insertLine(fileContents, lineToAdd, lineRead);
			hasAddedLine = true;
		} else {
			FloatingTask currentReadTask = new FloatingTask(lineRead);
			if (currentReadTask.compareTo(newFloatTask) < PARAM_COMPARE_TO) {
				fileContents.add(lineRead);
			} else {
				insertLine(fileContents, lineToAdd, lineRead);
				hasAddedLine = true;
			}
		}
		return hasAddedLine;
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
				hasAddedLine = processAddLineForTask(newTask, fileContents, lineToAdd, lineRead);
			}
		}
	}

	private boolean processAddLineForTask(Task newTask,
			ArrayList<String> fileContents, String lineToAdd, String lineRead) {
		boolean hasAddedLine = false;
		String typeOfLineRead = getFirstWord(lineRead);
		
		if (typeOfLineRead.equals(STRING_FLOAT_TASK)) {
			fileContents.add(lineRead);
		} else if (typeOfLineRead.equals(STRING_TASK)) {
			Task currentReadTask = new Task(lineRead);
			if (newTask.compareTo(currentReadTask) > PARAM_COMPARE_TO) {
				fileContents.add(lineRead);
			} else {
				insertLine(fileContents, lineToAdd, lineRead);
				hasAddedLine = true;
			}
		} else {
			insertLine(fileContents, lineToAdd, lineRead);
			hasAddedLine = true;
		}
		return hasAddedLine;
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
				hasAddedLine = processAddLineForEvent(newEvent, fileContents, lineToAdd, lineRead);
			}
		}
	}

	private boolean processAddLineForEvent(Event newEvent,
			ArrayList<String> fileContents, String lineToAdd, String lineRead) {
		boolean hasAddedLine = false;
		String typeOfLineRead = getFirstWord(lineRead);
		
		if (!typeOfLineRead.equals(STRING_EVENT)) {
			fileContents.add(lineRead);
		} else {
			Event currentReadEvent = new Event(lineRead);
			if (newEvent.compareTo(currentReadEvent) > PARAM_COMPARE_TO) {
				fileContents.add(lineRead);
			} else {
				insertLine(fileContents, lineToAdd, lineRead);
				hasAddedLine = true;
			}
		}
		return hasAddedLine;
	}
	
	private void insertLine(ArrayList<String> fileContents, String lineToAdd,
			String lineRead) {
		fileContents.add(lineToAdd);
		fileContents.add(lineRead);
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
	
	private String findAttribute(int lineNumber, int type) throws IOException, IllegalArgumentException {
		ArrayList<String> fileContents = new ArrayList<String>();
		
		addFileContentsToArrayList(fileContents);
		
		throwIllegalArgExceptionIfInvalid(lineNumber, fileContents);
		
		String lineToFind = fileContents.get(lineNumber - PARAM_OFFSET);
		
		String attribute = getAttributeFromLine(type, lineToFind);
		
		return attribute;
	}

	private String getAttributeFromLine(int type, String lineToFind) {
		String attribute;
		int numParameters = countParameters(lineToFind);
		
		if (hasValidType(type, numParameters)) {
			attribute = getSpecificWord(type, lineToFind);
		} else {
			attribute = null;
		}
		return attribute;
	}

	private boolean hasValidType(int type, int numParameters) {
		if (numParameters > type && type >= PARAM_FIRST_WORD) {
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
			logger.log(Level.FINE, String.format(MESSAGE_LOG_LINE_VALIDATION, lineNumber, fileContents.size()));
			return false;
		} else if (lineNumber > fileContents.size()) {
			logger.log(Level.FINE, String.format(MESSAGE_LOG_LINE_VALIDATION, lineNumber, fileContents.size()));
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

	private String markAsCompleted(int lineNumber) throws IOException,
			IllegalArgumentException {
		ArrayList<String> fileContents = new ArrayList<String>();

		addFileContentsToArrayList(fileContents);

		throwIllegalArgExceptionIfInvalid(lineNumber, fileContents);

		String lineToMarkDone = fileContents.get(lineNumber - PARAM_OFFSET);
		String typeToMarkDone = getFirstWord(lineToMarkDone);

		if (typeToMarkDone.equals(STRING_TASK)) {
			markTaskAsComplete(lineNumber, lineToMarkDone,
					typeToMarkDone);
		} else if (typeToMarkDone.equals(STRING_EVENT)) {
			markEventAsComplete(lineNumber, lineToMarkDone,
					typeToMarkDone);
		} else if (typeToMarkDone.equals(STRING_FLOAT_TASK)) {
			markFloatAsComplete(lineNumber, lineToMarkDone,
					typeToMarkDone);
		} else {
			logger.log(Level.WARNING, String.format(MESSAGE_LOG_UNKNOWN_TYPE, lineNumber));
			throw new IOException();
		}
		
		return lineToMarkDone;
	}

	private void markFloatAsComplete(int lineNumber, String lineToMarkDone,
			String typeToMarkDone) throws IOException {
		FloatingTask choosenFloat = new FloatingTask(lineToMarkDone);

		if (choosenFloat.isDone()) {
			String errorMessage = String.format(MESSAGE_ERROR_ALREADY_DONE,
					typeToMarkDone, choosenFloat.getName());
			throw new IllegalArgumentException(errorMessage);
		}

		choosenFloat.setDone(true);

		deleteLineFromFile(lineNumber);
		addFloatTaskToFile(choosenFloat);
	}

	private void markTaskAsComplete(int lineNumber, String lineToMarkDone,
			String typeToMarkDone) throws IOException {
		Task choosenTask = new Task(lineToMarkDone);

		if (choosenTask.isDone()) {
			String errorMessage = String.format(MESSAGE_ERROR_ALREADY_DONE,
					typeToMarkDone, choosenTask.getName());
			throw new IllegalArgumentException(errorMessage);
		}

		choosenTask.setDone(true);

		deleteLineFromFile(lineNumber);
		addTaskToFile(choosenTask);
	}
	
	private void markEventAsComplete(int lineNumber, String lineToMarkDone,
			String typeToMarkDone) throws IOException {
		Event choosenEvent = new Event(lineToMarkDone);

		if (choosenEvent.isDone()) {
			String errorMessage = String.format(MESSAGE_ERROR_ALREADY_DONE,
					typeToMarkDone, choosenEvent.getName());
			throw new IllegalArgumentException(errorMessage);
		}

		choosenEvent.setDone(true);

		deleteLineFromFile(lineNumber);
		addEventToFile(choosenEvent);
	}

	private boolean isEndOfFile(String lineRead) {
		return lineRead == null;
	}
	
	private String changeFilePath(String newLocation) {
		int changePathSituation;

		try {
			closeStreamsIfOpen();
			changePathSituation = checkChangePathSituation(newLocation);
		} catch (IOException exception) {
			logger.log(Level.WARNING, MESSAGE_LOG_CHANGE_PATH);
			return MESSAGE_ERROR_CHANGING_FILE_PATH_UNKNOWN;
		}

		String feedback;

		switch (changePathSituation) {
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
				logger.log(Level.WARNING, String.format(MESSAGE_LOG_UNKNOWN_SITUATION,changePathSituation));
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
			configHandler.updateConfigFile(newLocation);
		} catch (FileNotFoundException exception) {
			logger.log(Level.WARNING, MESSAGE_LOG_CHANGE_PATH);
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

	private int checkChangePathSituation(String newPathLocation) throws IOException {
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
		createToDoListFile();
		writeContentsToFile(fileContents);
		configHandler.updateConfigFile(newLocation);
	}
	
	private String[] splitParameters(String line) {
		return line.split(TEXT_FILE_DIVIDER);
	}

	private String getFirstWord(String text) {
		return getSpecificWord(PARAM_FIRST_WORD, text);
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
	
	private void initialiseHandler() throws FileSystemException {
		assert(configHandler == null);
		
		configHandler = new ConfigHandler();
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
		BufferedReader customFileReader = new BufferedReader(fileToBeRead);

		String lineRead = customFileReader.readLine();
		customFileReader.close();

		return lineRead;
	}

	private void createToDoListFile() throws FileSystemException {
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

	private void createNewFileIfFileDoesNotExist(File file)
			throws FileSystemException {
		try {
			file.createNewFile();
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_CREATE_FILE);
		}
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
			fileWriter.print(EMPTY_STRING);
			fileWriter.close();
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

		for (int i = PARAM_START_LOOP_ZERO; i < lastLine; i++) {
			fileWriter.println(linesToWrite[i]);
		}
		fileWriter.print(linesToWrite[lastLine]);

		fileWriter.close();
	}
}