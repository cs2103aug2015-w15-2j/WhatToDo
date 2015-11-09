/**
 * This class parses user input string for edit commands.
 * 
 * @@author A0124099B
 */

package backend;

import backend.DateParser;
import backend.TimeParser;
import backend.NameParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import struct.Command;
import struct.Date;

public class EditParser {
	
	private static final int POSITION_PLUS_ONE = 1;
	
	private static final int OFFSET = 1;
	
	private static final int NUMBER_REPEATED_EDIT_KEYWORD = -2;
	private static final int NUMBER_NO_EDIT_KEYWORD = -1;

	private static final String KEYWORD_EDIT_NAME = "name";
	private static final String KEYWORD_EDIT_DEADLINE = "date";
	private static final String KEYWORD_EDIT_START_DATE = "startd";
	private static final String KEYWORD_EDIT_START_TIME = "startt";
	private static final String KEYWORD_EDIT_END_DATE = "endd";
	private static final String KEYWORD_EDIT_END_TIME = "endt";

	private static final String MESSAGE_ERROR_NAME = "New task/event name required.";
	private static final String MESSAGE_ERROR_DEADLINE = "Invalid deadline.";
	private static final String MESSAGE_ERROR_START_DATE = "Invalid start date.";
	private static final String MESSAGE_ERROR_START_TIME = "Invalid start time.";
	private static final String MESSAGE_ERROR_END_DATE = "Invalid end date.";
	private static final String MESSAGE_ERROR_END_TIME = "Invalid end time.";
	private static final String MESSAGE_ERROR_EDIT = "Index required.";
	private static final String MESSAGE_ERROR_INDEX = "Invalid index.";
	private static final String MESSAGE_ERROR_DOUBLE_EDIT_KEYWORD = "Keyword %1$s has been entered twice.";
	private static final String MESSAGE_ERROR_EDIT_FORMAT = "Invalid edit format.";

	private static final DateParser dateParser = new DateParser();
	private static final TimeParser timeParser = new TimeParser();
	private static final NameParser nameParser = new NameParser();
	
	/**
	 * This method parses edit commands by first getting the indexes of all KEYWORDs
	 * that are used by the command. We then create a sorted ArrayList of the indexes 
	 * (indexArrayList), as well as a Hashtable of these indexes as keys, with the 
	 * corresponding keywords as values (indexKeywordHash). With these two objects, 
	 * we can construct the edit command by calling the setEditFields method.
	 * 
	 * @param arguments
	 * @return command
	 */
	protected Command parse(ArrayList<String> arguments) {

		ArrayList<String> argumentsLowerCase = CommandParser.toLowerCase(arguments);
		int nameIndex = getKeywordIndex(argumentsLowerCase, KEYWORD_EDIT_NAME);
		int deadlineIndex = getKeywordIndex(argumentsLowerCase, KEYWORD_EDIT_DEADLINE);
		int startDateIndex = getKeywordIndex(argumentsLowerCase, KEYWORD_EDIT_START_DATE);
		int startTimeIndex = getKeywordIndex(argumentsLowerCase, KEYWORD_EDIT_START_TIME);
		int endDateIndex = getKeywordIndex(argumentsLowerCase, KEYWORD_EDIT_END_DATE);
		int endTimeIndex = getKeywordIndex(argumentsLowerCase, KEYWORD_EDIT_END_TIME);

		// repeatedKeyword String will be null if no keywords are repeated, or the keyword
		// that is repeated
		String repeatedKeyword = repeatedKeywords(nameIndex, deadlineIndex, startDateIndex, 
				                                  startTimeIndex, endDateIndex, endTimeIndex);
		ArrayList<Integer> indexArrayList = getIndexArrayList(nameIndex, deadlineIndex, 
		        startDateIndex, startTimeIndex, endDateIndex, endTimeIndex);
		
		// String verificationMsg will only be STRING_VERIFIED if it passes
		// the checks in verification method
		String verificationMsg = verifyEdit(arguments, deadlineIndex, startDateIndex, 
		        startTimeIndex, endDateIndex, endTimeIndex, repeatedKeyword, indexArrayList);
		if (!verificationMsg.equals(CommandParser.STRING_VERIFIED)) {
			return CommandParser.initInvalidCommand(verificationMsg);
		}
		
		int index = Integer.parseInt(arguments.get(CommandParser.POSITION_FIRST_INDEX));
		Hashtable<Integer, String> indexKeywordHash = getHashtable(nameIndex, deadlineIndex, 
		        startDateIndex, startTimeIndex, endDateIndex, endTimeIndex);
		
		// editList is a list of keywords that the user has specified in the user input.
		// Given to logic for easier execution of edit command.
		ArrayList<String> editList = getEditList(nameIndex, deadlineIndex, startDateIndex, 
				                                 startTimeIndex, endDateIndex, endTimeIndex);

		Command command = new Command();
		command.setCommandType(Command.CommandType.EDIT);
		command.setEditList(editList);
		command.setIndex(index);
		command = setEditFields(arguments, indexArrayList, indexKeywordHash, command);
		return command;
	}
	
	private int getKeywordIndex(ArrayList<String> arguments, String keyword) {
		if (arguments.contains(keyword)) {
			int keywordIndex = arguments.indexOf(keyword);
			List<String> subList = arguments.subList(keywordIndex + POSITION_PLUS_ONE, arguments.size());
			if (subList.contains(keyword)) {
				return NUMBER_REPEATED_EDIT_KEYWORD;
			}
			return keywordIndex;
		}
		return NUMBER_NO_EDIT_KEYWORD;
	}

	private String repeatedKeywords(int nameIndex, int deadlineIndex, int startDateIndex, 
			int startTimeIndex, int endDateIndex, int endTimeIndex) {
		if (nameIndex == NUMBER_REPEATED_EDIT_KEYWORD) {
			return KEYWORD_EDIT_NAME;
		}
		if (deadlineIndex == NUMBER_REPEATED_EDIT_KEYWORD) {
			return KEYWORD_EDIT_DEADLINE;
		}
		if (startDateIndex == NUMBER_REPEATED_EDIT_KEYWORD) {
			return KEYWORD_EDIT_START_DATE;
		}
		if (startTimeIndex == NUMBER_REPEATED_EDIT_KEYWORD) {
			return KEYWORD_EDIT_START_TIME;
		}
		if (endDateIndex == NUMBER_REPEATED_EDIT_KEYWORD) {
			return KEYWORD_EDIT_END_DATE;
		}
		if (endTimeIndex == NUMBER_REPEATED_EDIT_KEYWORD) {
			return KEYWORD_EDIT_END_TIME;
		}
		return null;
	}
	
	private String verifyEdit(ArrayList<String> arguments, int deadlineIndex, int startDateIndex, 
	        int startTimeIndex, int endDateIndex, int endTimeIndex, String repeatedKeyword, 
	        ArrayList<Integer> indexArrayList) {
		if (arguments.isEmpty()) {
			return MESSAGE_ERROR_EDIT;
		}
		String indexString = arguments.get(CommandParser.POSITION_FIRST_INDEX);
		if (!indexString.matches(CommandParser.REGEX_POSITIVE_INTEGER)) {
			return MESSAGE_ERROR_INDEX;
		}
		if (repeatedKeyword != null) {
			return String.format(MESSAGE_ERROR_DOUBLE_EDIT_KEYWORD, repeatedKeyword);
		}
		if (!(deadlineIndex < 0)) {
			if (!(startDateIndex < 0) || !(startTimeIndex < 0) 
				  || !(endDateIndex < 0) || !(endTimeIndex < 0)) {
				return MESSAGE_ERROR_EDIT_FORMAT;
			}
		}
		if (indexArrayList.isEmpty()) {
			return MESSAGE_ERROR_EDIT_FORMAT;
		}
		return CommandParser.STRING_VERIFIED;
	}

	private ArrayList<Integer> getIndexArrayList(int nameIndex, int deadlineIndex, int startDateIndex,
			int startTimeIndex, int endDateIndex, int endTimeIndex) {
		ArrayList<Integer> indexArrayList = new ArrayList<Integer>();
		if (nameIndex != NUMBER_NO_EDIT_KEYWORD) {
			indexArrayList.add(nameIndex);
		}
		if (deadlineIndex != NUMBER_NO_EDIT_KEYWORD) {
			indexArrayList.add(deadlineIndex);
		}
		if (startDateIndex != NUMBER_NO_EDIT_KEYWORD) {
			indexArrayList.add(startDateIndex);
		}
		if (startTimeIndex != NUMBER_NO_EDIT_KEYWORD) {
			indexArrayList.add(startTimeIndex);
		}
		if (endDateIndex != NUMBER_NO_EDIT_KEYWORD) {
			indexArrayList.add(endDateIndex);
		}
		if (endTimeIndex != NUMBER_NO_EDIT_KEYWORD) {
			indexArrayList.add(endTimeIndex);
		}
		Collections.sort(indexArrayList);
		return indexArrayList;
	}

	private Hashtable<Integer, String> getHashtable(int nameIndex, int deadlineIndex, int startDateIndex,
			int startTimeIndex, int endDateIndex, int endTimeIndex) {
		Hashtable<Integer, String> indexKeywordHash = new Hashtable<Integer, String>();
		if (nameIndex != NUMBER_NO_EDIT_KEYWORD) {
			indexKeywordHash.put(nameIndex, KEYWORD_EDIT_NAME);
		}
		if (deadlineIndex != NUMBER_NO_EDIT_KEYWORD) {
			indexKeywordHash.put(deadlineIndex, KEYWORD_EDIT_DEADLINE);
		}
		if (startDateIndex != NUMBER_NO_EDIT_KEYWORD) {
			indexKeywordHash.put(startDateIndex, KEYWORD_EDIT_START_DATE);
		}
		if (startTimeIndex != NUMBER_NO_EDIT_KEYWORD) {
			indexKeywordHash.put(startTimeIndex, KEYWORD_EDIT_START_TIME);
		}
		if (endDateIndex != NUMBER_NO_EDIT_KEYWORD) {
			indexKeywordHash.put(endDateIndex, KEYWORD_EDIT_END_DATE);
		}
		if (endTimeIndex != NUMBER_NO_EDIT_KEYWORD) {
			indexKeywordHash.put(endTimeIndex, KEYWORD_EDIT_END_TIME);
		}
		return indexKeywordHash;
	}

	private ArrayList<String> getEditList(int nameIndex, int deadlineIndex, int startDateIndex, 
			int startTimeIndex, int endDateIndex, int endTimeIndex) {
		ArrayList<String> editList = new ArrayList<String>();
		if (nameIndex != NUMBER_NO_EDIT_KEYWORD) {
			editList.add(KEYWORD_EDIT_NAME);
		}
		if (deadlineIndex != NUMBER_NO_EDIT_KEYWORD) {
			editList.add(KEYWORD_EDIT_DEADLINE);
		}
		if (startDateIndex != NUMBER_NO_EDIT_KEYWORD) {
			editList.add(KEYWORD_EDIT_START_DATE);
		}
		if (startTimeIndex != NUMBER_NO_EDIT_KEYWORD) {
			editList.add(KEYWORD_EDIT_START_TIME);
		}
		if (endDateIndex != NUMBER_NO_EDIT_KEYWORD) {
			editList.add(KEYWORD_EDIT_END_DATE);
		}
		if (endTimeIndex != NUMBER_NO_EDIT_KEYWORD) {
			editList.add(KEYWORD_EDIT_END_TIME);
		}
		return editList;
	}

	/**
	 * This method sets the different edit fields appropriately. By considering the indexes of
	 * keywords in ascending order, we can set the fields one by one by getting the full string
	 * of the current keyword index to the next keyword index as the current keyword's new
	 * argument. By using the current keyword index as the key to the Hashtable, we will
	 * be able to know what the keyword is and call the appropriate edit method.
	 * 
	 * @param arguments
	 * @param indexArrayList 
	 *             ArrayList of keyword indexes
	 * @param indexKeywordHash 
	 *             Hashtable of indexes as keys and corresponding keywords as values
	 * @param command
	 * @return command
	 */
	private Command setEditFields(ArrayList<String> arguments, ArrayList<Integer> indexArrayList,
			Hashtable<Integer, String> indexKeywordHash, Command command) {
		for (int i = 0; i < indexArrayList.size(); i++) {
			int keywordIndex = indexArrayList.get(i);
			int nextKeywordIndex;
			// If i is the last keywordIndex, we set the nextKeywordIndex as the argument size
			if (i == indexArrayList.size() - OFFSET) {
				nextKeywordIndex = arguments.size();
			} else {
				nextKeywordIndex = indexArrayList.get(i + POSITION_PLUS_ONE);
			}
			String keyword = indexKeywordHash.get(keywordIndex);
			String argument = nameParser.getName(arguments, keywordIndex + POSITION_PLUS_ONE, nextKeywordIndex);
			String argumentLowerCase = argument;
			if (argument != null) {
				argumentLowerCase = argument.toLowerCase();
			}

			switch (keyword) {

			 	case KEYWORD_EDIT_NAME :
			 		command = editName(command, argument);
			 		break;

	            case KEYWORD_EDIT_DEADLINE :
	                command = editDeadline(command, argumentLowerCase);
	                break;
	                
	            case KEYWORD_EDIT_START_DATE :
	            	command = editStartDate(command, argumentLowerCase);
	            	break;
	            
	            case KEYWORD_EDIT_START_TIME :
	            	command = editStartTime(command, argumentLowerCase);
	            	break;
	            	
	            case KEYWORD_EDIT_END_DATE :
	            	command = editEndDate(command, argumentLowerCase);
	            	break;
	            	
	            case KEYWORD_EDIT_END_TIME :
	            	command = editEndTime(command, argumentLowerCase);
	            	break;
			}
			// We terminate the for loop when we get back an Invalid Command
			if (command.getCommandType() == Command.CommandType.INVALID) {
				i = indexArrayList.size();
			}
		}
		return command;
	}

	private Command editName(Command command, String argument) {
		if (argument == null) {
			return CommandParser.initInvalidCommand(MESSAGE_ERROR_NAME);
		}
		command.setName(argument);
		return command;
	}

	private Command editDeadline(Command command, String argument) {
		Date deadline = dateParser.getDate(argument);
		if (deadline == null) {
			return CommandParser.initInvalidCommand(MESSAGE_ERROR_DEADLINE);
		}
		command.setDueDate(deadline);
		return command;
	}

	private Command editStartDate(Command command, String argument) {
		Date startDate = dateParser.getDate(argument);
		if (startDate == null) {
			return CommandParser.initInvalidCommand(MESSAGE_ERROR_START_DATE);
		}
		command.setStartDate(startDate);
		return command;
	}

	private Command editStartTime(Command command, String argument) {
		String startTime = timeParser.getTime(argument);
		if (startTime == null) {
			return CommandParser.initInvalidCommand(MESSAGE_ERROR_START_TIME);
		}
		command.setStartTime(startTime);
		return command;
	}

	private Command editEndDate(Command command, String argument) {
		Date endDate = dateParser.getDate(argument);
		if (endDate == null) {
			return CommandParser.initInvalidCommand(MESSAGE_ERROR_END_DATE);
		}
		command.setEndDate(endDate);
		return command;
	}

	private Command editEndTime(Command command, String argument) {
		String endTime = timeParser.getTime(argument);
		if (endTime == null) {
			return CommandParser.initInvalidCommand(MESSAGE_ERROR_END_TIME);
		}
		command.setEndTime(endTime);
		return command;
	}
}