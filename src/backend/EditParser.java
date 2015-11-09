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

//@@author A0124099B
public class EditParser {
	
	private static final int POSITION_FIRST_INDEX = 0;
	
	private static final String STRING_VERIFIED = "verified";

	private static final int NUMBER_REPEATED_EDIT_KEYWORD = -2;
	private static final int NUMBER_NO_EDIT_KEYWORD = -1;

	private static final String KEYWORD_EDIT_NAME = "name";
	private static final String KEYWORD_EDIT_DEADLINE = "date";
	private static final String KEYWORD_EDIT_START_DATE = "startd";
	private static final String KEYWORD_EDIT_START_TIME = "startt";
	private static final String KEYWORD_EDIT_END_DATE = "endd";
	private static final String KEYWORD_EDIT_END_TIME = "endt";

	private static final String ERROR_NAME = "New task/event name required.";
	private static final String ERROR_DEADLINE = "Invalid deadline.";
	private static final String ERROR_START_DATE = "Invalid start date.";
	private static final String ERROR_START_TIME = "Invalid start time.";
	private static final String ERROR_END_DATE = "Invalid end date.";
	private static final String ERROR_END_TIME = "Invalid end time.";
	private static final String ERROR_EDIT = "Index required.";
	private static final String ERROR_INDEX = "Invalid index.";
	private static final String ERROR_DOUBLE_EDIT_KEYWORD = "Keyword %1$s has been entered twice.";
	private static final String ERROR_EDIT_FORMAT = "Invalid edit format.";

	private static final DateParser dateParser = new DateParser();
	private static final TimeParser timeParser = new TimeParser();
	private static final NameParser nameParser = new NameParser();

	public EditParser() {

	}
	
	protected Command parse(ArrayList<String> arguments) {

		ArrayList<String> argumentsLowerCase = CommandParser.toLowerCase(arguments);
		int nameIndex = getKeywordIndex(argumentsLowerCase, KEYWORD_EDIT_NAME);
		int deadlineIndex = getKeywordIndex(argumentsLowerCase, KEYWORD_EDIT_DEADLINE);
		int startDateIndex = getKeywordIndex(argumentsLowerCase, KEYWORD_EDIT_START_DATE);
		int startTimeIndex = getKeywordIndex(argumentsLowerCase, KEYWORD_EDIT_START_TIME);
		int endDateIndex = getKeywordIndex(argumentsLowerCase, KEYWORD_EDIT_END_DATE);
		int endTimeIndex = getKeywordIndex(argumentsLowerCase, KEYWORD_EDIT_END_TIME);

		String repeatedKeyword = repeatedKeywords(nameIndex, deadlineIndex, startDateIndex, 
				                                             startTimeIndex, endDateIndex, endTimeIndex);
		ArrayList<Integer> indexArrayList = getIndexArrayList(nameIndex, deadlineIndex, 
		        startDateIndex, startTimeIndex, endDateIndex, endTimeIndex);
		
		String verificationMsg = verifyEdit(arguments, deadlineIndex, startDateIndex, 
		        startTimeIndex, endDateIndex, endTimeIndex, repeatedKeyword, indexArrayList);
		if (!verificationMsg.equals(STRING_VERIFIED)) {
			return CommandParser.initInvalidCommand(verificationMsg);
		}
		int index = Integer.parseInt(arguments.get(POSITION_FIRST_INDEX));

		Hashtable<Integer, String> indexKeywordHash = getHashtable(nameIndex, deadlineIndex, 
		        startDateIndex, startTimeIndex, endDateIndex, endTimeIndex);
		ArrayList<String> editList = getEditList(nameIndex, deadlineIndex, startDateIndex, 
				                                            startTimeIndex, endDateIndex, endTimeIndex);

		Command command = new Command();
		command.setCommandType(Command.CommandType.EDIT);
		command.setEditList(editList);
		command.setIndex(index);

		for (int i = 0; i < indexArrayList.size(); i++) {
			int keywordIndex = indexArrayList.get(i);
			int nextKeywordIndex;
			if (i == indexArrayList.size() - 1) {
				nextKeywordIndex = arguments.size();
			} else {
				nextKeywordIndex = indexArrayList.get(i + 1);
			}
			String keyword = indexKeywordHash.get(keywordIndex);
			String argument = nameParser.getName(arguments, keywordIndex + 1, nextKeywordIndex);
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
			if (command.getCommandType() == Command.CommandType.INVALID) {
				i = indexArrayList.size();
			}
		}
		return command;
	}

	private int getKeywordIndex(ArrayList<String> arguments, String keyword) {
		if (arguments.contains(keyword)) {
			int keywordIndex = arguments.indexOf(keyword);
			List<String> subList = arguments.subList(keywordIndex + 1, arguments.size());
			if (subList.contains(keyword)) {
				return NUMBER_REPEATED_EDIT_KEYWORD;
			}
			return keywordIndex;
		}
		return NUMBER_NO_EDIT_KEYWORD;
	}

	private String repeatedKeywords(int nameIndex, int deadlineIndex, int startDateIndex, int startTimeIndex,
			int endDateIndex, int endTimeIndex) {
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
			return ERROR_EDIT;
		}
		String indexString = arguments.get(POSITION_FIRST_INDEX);
		if (!indexString.matches(CommandParser.REGEX_POSITIVE_INTEGER)) {
			return ERROR_INDEX;
		}
		if (repeatedKeyword != null) {
			return String.format(ERROR_DOUBLE_EDIT_KEYWORD, repeatedKeyword);
		}
		if (!(deadlineIndex < 0)) {
			if (!(startDateIndex < 0) || !(startTimeIndex < 0) || !(endDateIndex < 0) || !(endTimeIndex < 0)) {
				return ERROR_EDIT_FORMAT;
			}
		}
		if (indexArrayList.isEmpty()) {
			return ERROR_EDIT_FORMAT;
		}
		return STRING_VERIFIED;
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

	private ArrayList<String> getEditList(int nameIndex, int deadlineIndex, int startDateIndex, int startTimeIndex,
			int endDateIndex, int endTimeIndex) {
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

	private Command editName(Command command, String argument) {
		if (argument == null) {
			return CommandParser.initInvalidCommand(ERROR_NAME);
		}
		command.setName(argument);
		return command;
	}

	private Command editDeadline(Command command, String argument) {
		Date deadline = dateParser.getDate(argument);
		if (deadline == null) {
			return CommandParser.initInvalidCommand(ERROR_DEADLINE);
		}
		command.setDueDate(deadline);
		return command;
	}

	private Command editStartDate(Command command, String argument) {
		Date startDate = dateParser.getDate(argument);
		if (startDate == null) {
			return CommandParser.initInvalidCommand(ERROR_START_DATE);
		}
		command.setStartDate(startDate);
		return command;
	}

	private Command editStartTime(Command command, String argument) {
		String startTime = timeParser.getTime(argument);
		if (startTime == null) {
			return CommandParser.initInvalidCommand(ERROR_START_TIME);
		}
		command.setStartTime(startTime);
		return command;
	}

	private Command editEndDate(Command command, String argument) {
		Date endDate = dateParser.getDate(argument);
		if (endDate == null) {
			return CommandParser.initInvalidCommand(ERROR_END_DATE);
		}
		command.setEndDate(endDate);
		return command;
	}

	private Command editEndTime(Command command, String argument) {
		String endTime = timeParser.getTime(argument);
		if (endTime == null) {
			return CommandParser.initInvalidCommand(ERROR_END_TIME);
		}
		command.setEndTime(endTime);
		return command;
	}
}