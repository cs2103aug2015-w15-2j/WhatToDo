package backend;

import backend.NameParser;
import backend.DateParser;
import backend.TimeParser;
import backend.CommandParser;

import java.util.ArrayList;
import java.util.Hashtable;

import struct.Command;
import struct.Date;

public class AddParser {
	
	private static final int POSITION_FIRST_INDEX = 0;
	private static final int POSITION_PLUS_ONE = 1;
	private static final int POSITION_PLUS_TWO = 2;
	private static final int POSITION_DIFFERENCE_TWO = 2;
	private static final int POSITION_DIFFERENCE_THREE = 3;
	private static final int POSITION_DIFFERENCE_FOUR = 4;
	
	private static final String STRING_VERIFIED = "verified";
	
	private static final String KEYWORD_EVENT_TO = "to";
	private static final String KEYWORD_EVENT_FROM = "from";
	
	private static final String ERROR_TASK_NAME = "Task name required.";
	private static final String ERROR_DEADLINE = "Invalid deadline.";
	private static final String ERROR_EVENT_FORMAT = "Invalid format for adding event.";
	private static final String ERROR_START_DATE = "Invalid start date.";
	private static final String ERROR_START_TIME = "Invalid start time.";
	private static final String ERROR_END_DATE = "Invalid end date.";
	private static final String ERROR_END_TIME = "Invalid end time.";
	private static final String ERROR_START_END_TIME = "Start time later than or equal to "
			                                          + "end time for single-day event.";
	private static final String ERROR_START_END_DATE = "Start date later than end date.";
	private static final String ERROR_EVENT_NAME = "Event name required.";
	
	private static final DateParser dateParser = new DateParser();
	private static final TimeParser timeParser = new TimeParser();
	private static final NameParser nameParser = new NameParser();
	private static final CommandParser parser = new CommandParser(new Hashtable<String, String>());
	
	protected boolean containsToAndFrom(ArrayList<String> argumentsLowerCase) {
		return argumentsLowerCase.contains(KEYWORD_EVENT_TO) 
			   && argumentsLowerCase.contains(KEYWORD_EVENT_FROM);
	}

	protected Command addTask(ArrayList<String> arguments, int keywordByIndex) {
		Date date = null;
		String name = nameParser.getName(arguments, POSITION_FIRST_INDEX, keywordByIndex);
		if (keywordByIndex == arguments.size() - 2) {
			date = dateParser.getDate(arguments.get(arguments.size() - 1).toLowerCase());
		}
		
		String verificationMsg = verifyTaskAttributes(date, name);
		if (!verificationMsg.equals(STRING_VERIFIED)) {
			return parser.initInvalidCommand(verificationMsg);
		}
		
		Command command = new Command(Command.CommandType.ADD);
		command.setDataType(Command.DataType.TASK);
		command.setName(name);
		command.setDueDate(date);
		return command;
	}

	private String verifyTaskAttributes(Date date, String name) {
		if (date == null) {
			return ERROR_DEADLINE;
		}
		if (name == null) {
			return ERROR_TASK_NAME;
		}
		return STRING_VERIFIED;
	}

	protected Command addEvent(ArrayList<String> arguments, int keywordFromIndex, int keywordToIndex) {

		if (!isEventInCorrectPosition(arguments, keywordFromIndex, keywordToIndex)) {
			return parser.initInvalidCommand(ERROR_EVENT_FORMAT);
		}

		Date startDate = dateParser.getDate(arguments.get(
				         keywordFromIndex + POSITION_PLUS_ONE).toLowerCase());
		String startTime = timeParser.getTime(arguments.get(keywordFromIndex + POSITION_PLUS_TWO));
		Date endDate = dateParser.getDate(arguments.get(
				       keywordToIndex + POSITION_PLUS_ONE).toLowerCase());
		String endTime = timeParser.getTime(arguments.get(keywordToIndex + POSITION_PLUS_TWO));
		String name = nameParser.getEventName(arguments, keywordToIndex, keywordFromIndex);

		String verificationMsg = verifyEvent(startDate, startTime, endDate, endTime, name);
		if (!verificationMsg.equals(STRING_VERIFIED)) {
			return parser.initInvalidCommand(verificationMsg);
		}

		Command command = new Command(Command.CommandType.ADD);
		command.setDataType(Command.DataType.EVENT);
		command.setName(name);
		command.setStartDate(startDate);
		command.setStartTime(startTime);
		command.setEndDate(endDate);
		command.setEndTime(endTime);
		return command;
	}

	private boolean isEventInCorrectPosition(ArrayList<String> arguments, int keywordFromIndex, 
			                                 int keywordToIndex) {
		return Math.abs(keywordToIndex - keywordFromIndex) == POSITION_DIFFERENCE_THREE
				        && (arguments.size() - keywordToIndex == POSITION_DIFFERENCE_THREE
						|| arguments.size() - keywordFromIndex == POSITION_DIFFERENCE_THREE);
	}
	
	private String verifyEvent(Date startDate, String startTime, Date endDate, String endTime, String name) {
		if (startDate == null) {
			return ERROR_START_DATE;
		}
		if (startTime == null) {
			return ERROR_START_TIME;
		}
		if (endDate == null) {
			return ERROR_END_DATE;
		}
		if (endTime == null) {
			return ERROR_END_TIME;
		}
		if (startDate.compareTo(endDate) == 0) {
			if (!timeParser.areValidTimes(startTime, endTime)) {
				return ERROR_START_END_TIME;
			}
		}
		if (startDate.compareTo(endDate) == 1) {
			return ERROR_START_END_DATE;
		}
		if (name == null) {
			return ERROR_EVENT_NAME;
		}
		return STRING_VERIFIED;
	}

	protected Command addDayEvent(ArrayList<String> arguments, int keywordOnIndex, int keywordFromIndex,
			                      int keywordToIndex) {
		int minIndex = Math.min(keywordToIndex, Math.min(keywordFromIndex, keywordOnIndex));
		int maxIndex = Math.max(keywordToIndex, Math.max(keywordFromIndex, keywordOnIndex));

		if (isDayEventInCorrectPosition(arguments, minIndex, maxIndex)) {
			return parser.initInvalidCommand(ERROR_EVENT_FORMAT);
		}

		Date date = dateParser.getDate(arguments.get(keywordOnIndex + POSITION_PLUS_ONE).toLowerCase());
		String startTime = timeParser.getTime(arguments.get(keywordFromIndex + POSITION_PLUS_ONE));
		String endTime = timeParser.getTime(arguments.get(keywordToIndex + POSITION_PLUS_ONE));
		String name = nameParser.getName(arguments, POSITION_FIRST_INDEX, minIndex);

		String verificationMsg = verifyEvent(date, startTime, date, endTime, name);
		if (!verificationMsg.equals(STRING_VERIFIED)) {
			return parser.initInvalidCommand(verificationMsg);
		}
		
		Command command = new Command(Command.CommandType.ADD);
		command.setDataType(Command.DataType.EVENT);
		command.setName(name);
		command.setStartDate(date);
		command.setStartTime(startTime);
		command.setEndDate(date);
		command.setEndTime(endTime);
		return command;
	}

	private boolean isDayEventInCorrectPosition(ArrayList<String> arguments, int minIndex, int maxIndex) {
		return maxIndex - minIndex != POSITION_DIFFERENCE_FOUR 
			   || arguments.size() - maxIndex != POSITION_DIFFERENCE_TWO;
	}

	protected Command addFloatingTask(ArrayList<String> arguments) {
		Command command = new Command(Command.CommandType.ADD);
		command.setDataType(Command.DataType.FLOATING_TASK);
		String name = nameParser.getName(arguments, POSITION_FIRST_INDEX, arguments.size());
		command.setName(name);
		return command;
	}

}
