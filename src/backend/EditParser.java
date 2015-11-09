package backend;

import backend.CommandParser;
import backend.DateParser;
import backend.TimeParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import struct.Command;
import struct.Date;

//@@author A0124099B
public class EditParser {
	
	private static final int POSITION_FIRST_INDEX = 0;
	private static final String REGEX_POSITIVE_INTEGER = "^0*[1-9][0-9]*";
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
	private static final CommandParser parser = new CommandParser(new Hashtable<String, String>());

	public EditParser() {

	}

	protected int getKeywordIndex(ArrayList<String> arguments, String keyword) {
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

	protected String repeatedKeywords(int nameIndex, int deadlineIndex, int startDateIndex, int startTimeIndex,
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
	
	protected String verifyEdit(ArrayList<String> arguments, int deadlineIndex, int startDateIndex, 
	        int startTimeIndex, int endDateIndex, int endTimeIndex, String repeatedKeyword, 
	        ArrayList<Integer> indexArrayList) {
		if (arguments.isEmpty()) {
			return ERROR_EDIT;
		}
		String indexString = arguments.get(POSITION_FIRST_INDEX);
		if (!indexString.matches(REGEX_POSITIVE_INTEGER)) {
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

	protected ArrayList<Integer> getIndexArrayList(int nameIndex, int deadlineIndex, int startDateIndex,
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

	protected Hashtable<Integer, String> getHashtable(int nameIndex, int deadlineIndex, int startDateIndex,
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

	protected ArrayList<String> getEditList(int nameIndex, int deadlineIndex, int startDateIndex, int startTimeIndex,
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

	protected Command editName(Command command, String argument) {
		if (argument == null) {
			return parser.initInvalidCommand(ERROR_NAME);
		}
		command.setName(argument);
		return command;
	}

	protected Command editDeadline(Command command, String argument) {
		Date deadline = dateParser.getDate(argument);
		if (deadline == null) {
			return parser.initInvalidCommand(ERROR_DEADLINE);
		}
		command.setDueDate(deadline);
		return command;
	}

	protected Command editStartDate(Command command, String argument) {
		Date startDate = dateParser.getDate(argument);
		if (startDate == null) {
			return parser.initInvalidCommand(ERROR_START_DATE);
		}
		command.setStartDate(startDate);
		return command;
	}

	protected Command editStartTime(Command command, String argument) {
		String startTime = timeParser.getTime(argument);
		if (startTime == null) {
			return parser.initInvalidCommand(ERROR_START_TIME);
		}
		command.setStartTime(startTime);
		return command;
	}

	protected Command editEndDate(Command command, String argument) {
		Date endDate = dateParser.getDate(argument);
		if (endDate == null) {
			return parser.initInvalidCommand(ERROR_END_DATE);
		}
		command.setEndDate(endDate);
		return command;
	}

	protected Command editEndTime(Command command, String argument) {
		String endTime = timeParser.getTime(argument);
		if (endTime == null) {
			return parser.initInvalidCommand(ERROR_END_TIME);
		}
		command.setEndTime(endTime);
		return command;
	}
}