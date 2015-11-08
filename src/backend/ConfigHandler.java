/**
 * This class handles operations with regards to the Config folder.
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

public class ConfigHandler {
	private static final String TEXT_FILE_DIVIDER = ";";

	private static final String FILE_NAME = "whattodo.txt";
	private static final String COLLATED_FILE_PATH_FORMAT = "%s"
			+ File.separator + "%s";

	private static final String CONFIG_FILE_PATH = "config" + File.separator
			+ "config.txt";

	private static final String ALIAS_FILE_PATH = "config" + File.separator
			+ "alias.txt";
	private static final String FORMAT_ALIAS = "%s" + TEXT_FILE_DIVIDER + "%s";

	private static final String MESSAGE_ERROR_CREATE_FILE = "Error encountered when creating file.";

	private static final String EMPTY_STRING = "";
	private static final String NEWLINE = "\n";

	private static final int PARAM_START_LOOP_ZERO = 0;
	private static final int PARAM_FIRST_WORD = 0;
	private static final int PARAM_LESS_ONE = 1;
	private static final int PARAM_DOES_NOT_EXIST = -1;

	// For reading files.
	private BufferedReader configFileReader;
	private BufferedReader aliasFileReader;

	// For writing files.
	private PrintWriter configFileWriter;
	private PrintWriter aliasFileWriter;

	public String getPathFromConfig() throws FileSystemException {
		createConfigFileIfMissing();

		String readFilePath;

		try {
			readFilePath = readFromConfigFile();
		} catch (IOException exception) {
			return FILE_NAME;
		}

		if (!hasDirectorySpecifiedInPath(readFilePath)) {
			return FILE_NAME;
		}

		return String
				.format(COLLATED_FILE_PATH_FORMAT, readFilePath, FILE_NAME);
	}

	public void updateConfigFile(String newLocation)
			throws FileNotFoundException {
		configFileWriter = new PrintWriter(CONFIG_FILE_PATH);

		configFileWriter.print(newLocation);

		configFileWriter.close();
	}

	public void createAliasFile() throws FileSystemException {
		File file = new File(ALIAS_FILE_PATH);

		createNewFileIfFileDoesNotExist(file);
	}

	public void updateAliasFile(String aliasLine, String actualCommandType)
			throws IOException {
		ArrayList<String> fileContents = readAliasFileToArrayList();

		String lineToAdd = String.format(FORMAT_ALIAS, aliasLine,
				actualCommandType);
		fileContents.add(lineToAdd);

		writeContentsToAliasFile(fileContents);
	}

	public void removeFromAliasFile(String aliasLine) throws IOException {
		ArrayList<String> fileContents = readAliasFileToArrayList();

		removeAliasFromArrayList(aliasLine, fileContents);

		writeContentsToAliasFile(fileContents);
	}

	public String displayAliasFile() throws IOException {
		initializeAliasReader();

		StringBuilder fileContents = new StringBuilder();

		formatAliasContentsAsString(fileContents);

		return fileContents.toString();
	}

	public void clearAliasFile() throws IOException {
		emptyAliasFile();
	}

	public void overwriteAliasFile(String textToOverwrite) throws IOException {
		String[] linesToReplace = textToOverwrite.split(NEWLINE);

		ArrayList<String> fileContents = convertToArrayList(linesToReplace);

		writeContentsToAliasFile(fileContents);
	}

	/*
	 * Private Methods start here
	 */

	private void initialiseAliasWriter() throws FileNotFoundException {
		aliasFileWriter = new PrintWriter(ALIAS_FILE_PATH);
	}

	private ArrayList<String> convertToArrayList(String[] linesToConvert) {
		ArrayList<String> convertedContents = new ArrayList<String>();

		for (int i = PARAM_START_LOOP_ZERO; i < linesToConvert.length; i++) {
			convertedContents.add(linesToConvert[i]);
		}

		return convertedContents;
	}

	private void createConfigFileIfMissing() throws FileSystemException {
		File file = new File(CONFIG_FILE_PATH);

		createDirectoryIfMissing(file);

		createNewFileIfFileDoesNotExist(file);
	}

	private void formatAliasContentsAsString(StringBuilder fileContents)
			throws IOException {
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
	}

	private ArrayList<String> readAliasFileToArrayList() throws IOException {
		initializeAliasReader();

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

	private void writeContentsToAliasFile(ArrayList<String> fileContents)
			throws FileNotFoundException {
		initialiseAliasWriter();

		int lastLine = fileContents.size() - PARAM_LESS_ONE;

		if (lastLine == PARAM_DOES_NOT_EXIST) {
			aliasFileWriter.print(EMPTY_STRING);
			aliasFileWriter.close();
			return;
		}

		for (int i = PARAM_START_LOOP_ZERO; i < lastLine; i++) {
			aliasFileWriter.println(fileContents.get(i));
		}
		aliasFileWriter.print(fileContents.get(lastLine));

		aliasFileWriter.close();
	}

	private void emptyAliasFile() throws FileNotFoundException {
		aliasFileWriter = new PrintWriter(ALIAS_FILE_PATH);

		aliasFileWriter.print(EMPTY_STRING);

		aliasFileWriter.close();
	}

	private void removeAliasFromArrayList(String aliasLine,
			ArrayList<String> fileContents) {
		for (int i = PARAM_START_LOOP_ZERO; i < fileContents.size(); i++) {
			String aliasFromFile = getFirstWord(fileContents.get(i));
			if (aliasFromFile.equals(aliasLine)) {
				fileContents.remove(i);
				break;
			}
		}
	}

	private void initializeAliasReader() throws FileNotFoundException {
		FileReader fileToBeRead = new FileReader(ALIAS_FILE_PATH);
		aliasFileReader = new BufferedReader(fileToBeRead);
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

	private String readFromConfigFile() throws IOException {
		FileReader fileToBeRead = new FileReader(CONFIG_FILE_PATH);
		configFileReader = new BufferedReader(fileToBeRead);

		String lineRead = configFileReader.readLine();
		configFileReader.close();

		return lineRead;
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

	private boolean isEndOfFile(String lineRead) {
		return lineRead == null;
	}

	private String getFirstWord(String text) {
		String parameters[] = splitParameters(text);
		String firstWord = parameters[PARAM_FIRST_WORD];

		return firstWord;
	}

	private String[] splitParameters(String line) {
		return line.split(TEXT_FILE_DIVIDER);
	}
}
