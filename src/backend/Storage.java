/**
 * This class reads, writes and manipulates the text data in the text file which
 * stores the to-do list of the user.
 * 
 * @author Lam Zhen Zong, Nicholas
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

public class Storage {

	private static final String FILENAME = "whattodo.txt";
	private static final String COLLATED_FILE_PATH_FORMAT = "%s\\%s";

	private static final String MESSAGE_ADD_TASK = "Added \"%s\" to list.\nDue on %s, %s.";
	private static final String MESSAGE_ADD_FLOAT_TASK = "Added \"%s\" to list.";
	private static final String MESSAGE_ADD_EVENT = "Added \"%s\" to list.\nEvent Start: %s, %s, %s\nEvent End: %s, %s, %s";
	private static final String MESSAGE_ERROR_CREATE_FILE = "Error encountered when creating file.";

	private static final String TEXT_FILE_FORMAT_TASK = "task;%s;%s";
	private static final String TEXT_FILE_FORMAT_FLOAT_TASK = "float;%s";
	private static final String TEXT_FILE_FORMAT_EVENT = "event;%s;%s;%s;%s;%s";

	private static final String TEXT_FILE_DIVIDER = ";";

	private static final String STRING_TASK = "task";
	private static final String STRING_FLOAT_TASK = "float";
	private static final String STRING_EVENT = "event";

	// This string stores the whole file name with directory.
	private String filePath;

	public Storage() throws FileSystemException {
		filePath = FILENAME;

		createFileWithoutDirectory();
	}

	public Storage(String directory) throws FileSystemException {
		filePath = String
				.format(COLLATED_FILE_PATH_FORMAT, directory, FILENAME);

		createFileWithDirectory();
	}

	public String getFilePath() {
		return filePath;
	}

	/**
	 * Adds a task with deadline to the text file (to-do list). Tasks in the
	 * file will be sorted by deadlines.
	 * 
	 * @param taskName
	 *            the name of the task to be added
	 * @param taskDeadline
	 *            the deadline of task to be added
	 * @return feedback message based on task to be added
	 */
	public String addTask(String taskName, Date taskDeadline) {
		addTaskToFile(taskName, taskDeadline);

		return String.format(MESSAGE_ADD_TASK, taskName,
				taskDeadline.getDayString(), taskDeadline.getFormatDate());
	}

	/**
	 * Adds a floating task to the text file (to-do list). Floating tasks in the
	 * file will be sorted in alphabetical order.
	 * 
	 * @param taskName
	 *            the name of the task to be added
	 * @return feedback message based on task to be added
	 */
	public String addFloatingTask(String taskName) {
		addFloatTaskToFile(taskName);

		return String.format(MESSAGE_ADD_FLOAT_TASK, taskName);
	}

	/**
	 * Adds an event to the text file (to-do-list). Events in the file will be
	 * sorted by start date/time, followed by end date/time.
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
		addEventToFile(eventName, eventStartDate, eventStartTime, eventEndDate,
				eventEndTime);

		return String.format(MESSAGE_ADD_EVENT, eventName,
				eventStartDate.getDayString(), eventStartDate.getFormatDate(),
				eventStartTime, eventEndDate.getDayString(),
				eventEndDate.getFormatDate(), eventEndTime);
	}

	// To refactor and improve.
	private void addEventToFile(String eventName, Date eventStartDate,
			String eventStartTime, Date eventEndDate, String eventEndTime) {
		try {
			ArrayList<String> fileContents = new ArrayList<String>();
			FileReader fileToBeRead = new FileReader(filePath);
			BufferedReader fileReader = new BufferedReader(fileToBeRead);
			boolean hasAddedLine = false;

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
						if (eventIsLater(eventStartDate, eventStartTime, eventEndDate, eventEndTime, parameters)) {
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

			fileReader.close();

			PrintWriter writer = new PrintWriter(filePath);
			for (int i = 0; i < fileContents.size(); i++) {
				writer.println(fileContents.get(i));
			}
			writer.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}

	}

	// To refactor and improve.
	private boolean eventIsLater(Date eventStartDate, String eventStartTime,
			Date eventEndDate, String eventEndTime, String[] parameters) {
		if (eventStartDate.isLaterThan(parameters[parameters.length-4])) {
			return true;
		} else if (eventStartDate.isSameDate(parameters[parameters.length-4])) {
			if (Integer.parseInt(eventStartTime) > Integer.parseInt(parameters[parameters.length-3])) {
				return true;
			} else if (Integer.parseInt(eventStartTime) == Integer.parseInt(parameters[parameters.length-3])) {
				if (eventEndDate.isLaterThan(parameters[parameters.length-2])) {
					return true;
				} else if (eventEndDate.isSameDate(parameters[parameters.length-2])) {
					if (Integer.parseInt(eventEndTime) > Integer.parseInt(parameters[parameters.length-1])) {
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
	private void addTaskToFile(String taskName, Date taskDeadline) {
		try {
			ArrayList<String> fileContents = new ArrayList<String>();
			FileReader fileToBeRead = new FileReader(filePath);
			BufferedReader fileReader = new BufferedReader(fileToBeRead);
			boolean hasAddedLine = false;

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

			fileReader.close();

			PrintWriter writer = new PrintWriter(filePath);
			for (int i = 0; i < fileContents.size(); i++) {
				writer.println(fileContents.get(i));
			}
			writer.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}

	}

	// To refactor and improve.
	private void addFloatTaskToFile(String taskName) {
		try {
			ArrayList<String> fileContents = new ArrayList<String>();
			FileReader fileToBeRead = new FileReader(filePath);
			BufferedReader fileReader = new BufferedReader(fileToBeRead);
			boolean hasAddedLine = false;

			String lineToAdd = String.format(TEXT_FILE_FORMAT_FLOAT_TASK,
					taskName);

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

			fileReader.close();

			PrintWriter writer = new PrintWriter(filePath);
			for (int i = 0; i < fileContents.size(); i++) {
				writer.println(fileContents.get(i));
			}
			writer.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}

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

}
