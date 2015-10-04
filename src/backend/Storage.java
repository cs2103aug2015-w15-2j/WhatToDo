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
import java.util.Collections;

import struct.Date;

public class Storage {

	private static final String FILENAME = "whattodo.txt";
	private static final String COLLATED_FILE_PATH_FORMAT = "%s\\%s";

	private static final String MESSAGE_ADD_TASK = "Added \"%s\" to list.\nDue on %s, %s.";
	private static final String MESSAGE_ADD_FLOAT_TASK = "Added \"%s\" to list.";
	private static final String MESSAGE_ADD_EVENT = "Added \"%s\" to list.\nEvent Start: %s, %s, %s\nEvent End: %s, %s, %s";
	private static final String MESSAGE_DELETE_LINE = "Deleted \"%s\" from list.";
	private static final String MESSAGE_EDIT_NAME = "Edited %s name from \"%s\" to \"%s\".";
	private static final String MESSAGE_EMPTY_FILE = "The list is empty.";

	private static final String MESSAGE_ERROR_CREATE_FILE = "Error encountered when creating file.";
	private static final String MESSAGE_ERROR_READ_FILE = "Error encountered when reading file.";
	private static final String MESSAGE_ERROR_INVALID_LINE_ACCESS = "Line %d is invalid.";
	private static final String MESSAGE_ERROR_ADD_TASK = "Error encountered when adding task to file.";
	private static final String MESSAGE_ERROR_ADD_EVENT = "Error encountered when adding event to file.";
	private static final String MESSAGE_ERROR_DELETE_LINE = "Error encountered when deleting from file.";
	private static final String MESSAGE_ERROR_REPLACE_NAME = "Error encountered when renaming.";

	private static final String TEXT_FILE_FORMAT_TASK = "task;%s;%s";
	private static final String TEXT_FILE_FORMAT_FLOAT_TASK = "float;%s";
	private static final String TEXT_FILE_FORMAT_EVENT = "event;%s;%s;%s;%s;%s";

	private static final String TEXT_FILE_DIVIDER = ";";

	private static final String STRING_TASK = "task";
	private static final String STRING_FLOAT_TASK = "float";
	private static final String STRING_EVENT = "event";

	private static final String NEWLINE = "\n";

	// This string stores the whole file name with directory.
	private String filePath;

	// For reading from file.
	private BufferedReader fileReader;

	// For writing into file.
	private PrintWriter fileWriter;

	/**
	 * Constructors
	 * 
	 * @throws FileSystemException
	 *             when error in creating file.
	 * @throws FileNotFoundException
	 *             when error in locating file.
	 */
	public Storage() throws FileSystemException {
		filePath = FILENAME;

		createFileWithoutDirectory();
	}

	public Storage(String directory) throws FileSystemException {
		filePath = String
				.format(COLLATED_FILE_PATH_FORMAT, directory, FILENAME);

		createFileWithDirectory();
	}

	// Accessor
	public String getFilePath() {
		return filePath;
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
	public String addTask(String taskName, Date taskDeadline) {
		try {
			addTaskToFile(taskName, taskDeadline);
		} catch (IOException exception) {
			return MESSAGE_ERROR_ADD_TASK;
		}

		return String.format(MESSAGE_ADD_TASK, taskName,
				taskDeadline.getDayString(), taskDeadline.getFormatDate());
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
	public String addFloatingTask(String taskName) {
		try {
			addFloatTaskToFile(taskName);
		} catch (IOException exception) {
			return MESSAGE_ERROR_ADD_TASK;
		}

		return String.format(MESSAGE_ADD_FLOAT_TASK, taskName);
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
	public String addEvent(String eventName, Date eventStartDate,
			String eventStartTime, Date eventEndDate, String eventEndTime) {
		try {
			addEventToFile(eventName, eventStartDate, eventStartTime,
					eventEndDate, eventEndTime);
		} catch (IOException exception) {
			return MESSAGE_ERROR_ADD_EVENT;
		}

		return String.format(MESSAGE_ADD_EVENT, eventName,
				eventStartDate.getDayString(), eventStartDate.getFormatDate(),
				eventStartTime, eventEndDate.getDayString(),
				eventEndDate.getFormatDate(), eventEndTime);
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
	
	// TODO: stubs
	public String editTaskDeadline(int lineNumber, Date newDeadline) {
		return "stub";
	}
	public String editEventStartDate(int lineNumber, Date newDate) {
		return "stub";
	}
	public String editEventEndDate(int lineNumber, Date newDate) {
		return "stub";
	}
	public String editEventStartTime(int lineNumber, String newTime) {
		return "stub";
	}
	public String editEventEndTime(int lineNumber, String newTime) {
		return "stub";
	}

	/*
	 * Private Methods Start Here.
	 */

	// Refactor and improve
	private String deleteLineFromFile(int lineNumber) throws IOException {
		String lineToDelete = "dummy";

		initialiseReader();
		if (lineNumber <= 0) {
			return String.format(MESSAGE_ERROR_INVALID_LINE_ACCESS, lineNumber);
		}

		ArrayList<String> fileContents = new ArrayList<String>();

		for (int i = 0; i < lineNumber - 1; i++) {
			String lineRead = fileReader.readLine();

			if (lineRead == null) {
				return String.format(MESSAGE_ERROR_INVALID_LINE_ACCESS,
						lineNumber);
			}

			fileContents.add(lineRead);
		}

		lineToDelete = fileReader.readLine();

		while (true) {
			String lineRead = fileReader.readLine();

			if (lineRead == null) {
				break;
			} else {
				fileContents.add(lineRead);
			}
		}

		writeContentsToFile(fileContents);
		fileReader.close();

		return String.format(MESSAGE_DELETE_LINE, lineToDelete);
	}

	// To refactor and improve
	private String replaceName(int lineNumber, String newName)
			throws IOException {
		if (lineNumber <= 0) {
			return String.format(MESSAGE_ERROR_INVALID_LINE_ACCESS, lineNumber);
		}

		ArrayList<String> fileContents = new ArrayList<String>();

		initialiseReader();
		while (true) {
			String lineRead = fileReader.readLine();

			if (lineRead == null) {
				break;
			} else {
				fileContents.add(lineRead);
			}
		}

		if (lineNumber > fileContents.size()) {
			return String.format(MESSAGE_ERROR_INVALID_LINE_ACCESS, lineNumber);
		}

		String[] params = fileContents.get(lineNumber - 1).split(
				TEXT_FILE_DIVIDER);
		String type = params[0].equals(STRING_EVENT) ? STRING_EVENT
				: STRING_TASK;
		String oldName = params[1];

		params[1] = newName;

		String editedLine = params[0];
		for (int i = 1; i < params.length; i++) {
			editedLine += TEXT_FILE_DIVIDER + params[i];
		}

		fileContents.set(lineNumber - 1, editedLine);

		// sort the floating task
		if (params[0].equals(STRING_FLOAT_TASK)) {
			for (int i = lineNumber - 2; i >= 0; i--) {
				if (fileContents.get(i).compareTo(fileContents.get(i + 1)) > 0) {
					Collections.swap(fileContents, i, i + 1);
				} else {
					break;
				}
			}
		}

		writeContentsToFile(fileContents);
		fileReader.close();

		return String.format(MESSAGE_EDIT_NAME, type, oldName, newName);
	}

	private String showAllFileContents() throws IOException {
		String fileContents = readContentFromFile();

		return fileContents;
	}

	// To refactor and improve
	private String readContentFromFile() throws IOException {
		StringBuilder fileContents = new StringBuilder();
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

		if (fileContents.length() == 0) {
			return MESSAGE_EMPTY_FILE;
		}
		
		fileReader.close();

		return fileContents.toString();
	}

	// To refactor and improve.
	private void addEventToFile(String eventName, Date eventStartDate,
			String eventStartTime, Date eventEndDate, String eventEndTime)
			throws IOException {

		ArrayList<String> fileContents = new ArrayList<String>();

		boolean hasAddedLine = false;

		initialiseReader();

		String lineToAdd = String.format(TEXT_FILE_FORMAT_EVENT, eventName,
				eventStartDate.getFullDate(), eventStartTime,
				eventEndDate.getFullDate(), eventEndTime);

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
				String[] parameters = lineRead.split(TEXT_FILE_DIVIDER);
				if (parameters[0].equals(STRING_TASK)) {
					fileContents.add(lineRead);
				} else if (parameters[0].equals(STRING_EVENT)) {
					if (eventIsLater(eventStartDate, eventStartTime,
							eventEndDate, eventEndTime, parameters)) {
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

	// To refactor and improve.
	private boolean eventIsLater(Date eventStartDate, String eventStartTime,
			Date eventEndDate, String eventEndTime, String[] parameters) {
		if (eventStartDate.isLaterThan(parameters[parameters.length - 4])) {
			return true;
		} else if (eventStartDate.isSameDate(parameters[parameters.length - 4])) {
			if (Integer.parseInt(eventStartTime) > Integer
					.parseInt(parameters[parameters.length - 3])) {
				return true;
			} else if (Integer.parseInt(eventStartTime) == Integer
					.parseInt(parameters[parameters.length - 3])) {
				if (eventEndDate.isLaterThan(parameters[parameters.length - 2])) {
					return true;
				} else if (eventEndDate
						.isSameDate(parameters[parameters.length - 2])) {
					if (Integer.parseInt(eventEndTime) > Integer
							.parseInt(parameters[parameters.length - 1])) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// To refactor and improve.
	private void addTaskToFile(String taskName, Date taskDeadline)
			throws IOException {

		ArrayList<String> fileContents = new ArrayList<String>();

		boolean hasAddedLine = false;

		initialiseReader();

		String lineToAdd = String.format(TEXT_FILE_FORMAT_TASK, taskName,
				taskDeadline.getFullDate());

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
				String[] parameters = lineRead.split(TEXT_FILE_DIVIDER);
				if (parameters[0].equals(STRING_TASK)) {
					if (taskDeadline
							.isLaterThan(parameters[parameters.length - 1])) {
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

	// To refactor and improve.
	private void addFloatTaskToFile(String taskName) throws IOException {
		ArrayList<String> fileContents = new ArrayList<String>();

		boolean hasAddedLine = false;

		initialiseReader();

		String lineToAdd = String.format(TEXT_FILE_FORMAT_FLOAT_TASK, taskName);

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
				String[] parameters = lineRead.split(TEXT_FILE_DIVIDER);
				if (!parameters[0].equals(STRING_FLOAT_TASK)
						|| parameters[parameters.length - 1]
								.compareTo(taskName) < 0) {
					fileContents.add(lineRead);
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

	private void createFileWithoutDirectory() throws FileSystemException {
		File file = new File(filePath);

		createNewFile(file);
	}

	private void createFileWithDirectory() throws FileSystemException {
		File file = new File(filePath);

		createDirectoryIfMissing(file);

		createNewFile(file);
	}

	private void createDirectoryIfMissing(File file) {
		file.getParentFile().mkdirs();
	}

	private static void createNewFile(File file) throws FileSystemException {
		try {
			file.createNewFile();
		} catch (IOException exception) {
			throw new FileSystemException(MESSAGE_ERROR_CREATE_FILE);
		}
	}

	private void initialiseReader() throws FileNotFoundException {
		FileReader fileToBeRead = new FileReader(filePath);
		fileReader = new BufferedReader(fileToBeRead);
	}

	private void initialiseWriter() throws FileNotFoundException {
		fileWriter = new PrintWriter(filePath);
	}
}
