/**
 * This class parses user input string for add commands.
 * 
 * @@author A0124099B
 */

package backend;

import backend.NameParser;
import backend.DateParser;
import backend.TimeParser;

import java.util.ArrayList;

import struct.Command;
import struct.Date;

public class AddParser {
	
	private static final int POSITION_PLUS_ONE = 1;
	private static final int POSITION_PLUS_TWO = 2;
	private static final int POSITION_DIFFERENCE_TWO = 2;
	private static final int POSITION_DIFFERENCE_THREE = 3;
	private static final int POSITION_DIFFERENCE_FOUR = 4;
	
	private static final int SAME_DATE = 0;
	private static final int THIS_DATE_LATER_THAN_OTHER_DATE = 1;
	
	private static final int OFFSET = 1;
	
	private static final String KEYWORD_DEADLINE = "by";
	private static final String KEYWORD_EVENT_TO = "to";
	private static final String KEYWORD_EVENT_FROM = "from";
	private static final String KEYWORD_EVENT_ON = "on";
	
	private static final String MESSAGE_ERROR_TASK_NAME = "Task name required.";
	private static final String MESSAGE_ERROR_DEADLINE = "Invalid deadline.";
	private static final String MESSAGE_ERROR_EVENT_FORMAT = "Invalid format for adding event.";
	private static final String MESSAGE_ERROR_START_DATE = "Invalid start date.";
	private static final String MESSAGE_ERROR_START_TIME = "Invalid start time.";
	private static final String MESSAGE_ERROR_END_DATE = "Invalid end date.";
	private static final String MESSAGE_ERROR_END_TIME = "Invalid end time.";
	private static final String MESSAGE_ERROR_START_END_TIME = "Start time later than or equal to "
			                                          + "end time for single-day event.";
	private static final String MESSAGE_ERROR_START_END_DATE = "Start date later than end date.";
	private static final String MESSAGE_ERROR_EVENT_NAME = "Event name required.";
	
	private static final DateParser dateParser = new DateParser();
	private static final TimeParser timeParser = new TimeParser();
	private static final NameParser nameParser = new NameParser();
	
	/**
	 * This method parses add commands depending on the keywords that
	 * the input arguments have.
	 * 
	 * @param arguments
	 * @return command
	 */
	protected Command parse(ArrayList<String> arguments) {
		ArrayList<String> argumentsLowerCase = CommandParser.toLowerCase(arguments);
		
		if (arguments.isEmpty()) {
			return CommandParser.initInvalidCommand(MESSAGE_ERROR_TASK_NAME);
		} else if (argumentsLowerCase.contains(KEYWORD_DEADLINE)) {
			int keywordByIndex = argumentsLowerCase.indexOf(KEYWORD_DEADLINE);
			return addTask(arguments, keywordByIndex);
		} else if (containsToAndFrom(argumentsLowerCase)) {
			return parseEvent(arguments, argumentsLowerCase);
		} else {
			return addFloatingTask(arguments);
		}
	}
	
	private boolean containsToAndFrom(ArrayList<String> argumentsLowerCase) {
		return argumentsLowerCase.contains(KEYWORD_EVENT_TO) 
			   && argumentsLowerCase.contains(KEYWORD_EVENT_FROM);
	}

	private Command parseEvent(ArrayList<String> arguments, ArrayList<String> argumentsLowerCase) {
		int keywordFromIndex = argumentsLowerCase.indexOf(KEYWORD_EVENT_FROM);
		int keywordToIndex = argumentsLowerCase.indexOf(KEYWORD_EVENT_TO);
		
		// We will call addDayEvent method or addEvent method depending on whether
		// KEYWORD_EVENT_ON is in the user input
		if (argumentsLowerCase.contains(KEYWORD_EVENT_ON)) {
			int keywordOnIndex = argumentsLowerCase.indexOf(KEYWORD_EVENT_ON);
			return addDayEvent(arguments, keywordOnIndex, keywordFromIndex, keywordToIndex);
		} else {
			return addEvent(arguments, keywordFromIndex, keywordToIndex);
		}
	}

	private Command addTask(ArrayList<String> arguments, int keywordByIndex) {
		Date date = null;
		String name = nameParser.getName(arguments, CommandParser.POSITION_FIRST_INDEX, keywordByIndex);
		if (arguments.size() - keywordByIndex == POSITION_DIFFERENCE_TWO) {
			date = dateParser.getDate(arguments.get(arguments.size() - OFFSET).toLowerCase());
		}
		
		// String verificationMsg will only be STRING_VERIFIED if it passes
		// the attribute checks in verification method
		String verificationMsg = verifyTaskAttributes(date, name);
		if (!verificationMsg.equals(CommandParser.STRING_VERIFIED)) {
			return CommandParser.initInvalidCommand(verificationMsg);
		}
		
		Command command = new Command(Command.CommandType.ADD);
		command.setDataType(Command.DataType.TASK);
		command.setName(name);
		command.setDueDate(date);
		return command;
	}

	private String verifyTaskAttributes(Date date, String name) {
		if (date == null) {
			return MESSAGE_ERROR_DEADLINE;
		}
		if (name == null) {
			return MESSAGE_ERROR_TASK_NAME;
		}
		return CommandParser.STRING_VERIFIED;
	}

	private Command addEvent(ArrayList<String> arguments, int keywordFromIndex, int keywordToIndex) {
		// To account for non-strict ordering of keywords To and From
		if (!isEventInCorrectPosition(arguments, keywordFromIndex, keywordToIndex)) {
			return CommandParser.initInvalidCommand(MESSAGE_ERROR_EVENT_FORMAT);
		}

		// date must be first string after from and to, time must be second string after from and to
		Date startDate = dateParser.getDate(arguments.get(
				         keywordFromIndex + POSITION_PLUS_ONE).toLowerCase());
		String startTime = timeParser.getTime(arguments.get(keywordFromIndex + POSITION_PLUS_TWO));
		Date endDate = dateParser.getDate(arguments.get(
				       keywordToIndex + POSITION_PLUS_ONE).toLowerCase());
		String endTime = timeParser.getTime(arguments.get(keywordToIndex + POSITION_PLUS_TWO));
		String name = nameParser.getEventName(arguments, keywordToIndex, keywordFromIndex);

		// String verificationMsg will only be STRING_VERIFIED if it passes
		// the attribute checks in verification method
		String verificationMsg = verifyEvent(startDate, startTime, endDate, endTime, name);
		if (!verificationMsg.equals(CommandParser.STRING_VERIFIED)) {
			return CommandParser.initInvalidCommand(verificationMsg);
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
			return MESSAGE_ERROR_START_DATE;
		}
		if (startTime == null) {
			return MESSAGE_ERROR_START_TIME;
		}
		if (endDate == null) {
			return MESSAGE_ERROR_END_DATE;
		}
		if (endTime == null) {
			return MESSAGE_ERROR_END_TIME;
		}
		if (startDate.compareTo(endDate) == SAME_DATE) {
			if (!timeParser.areValidTimes(startTime, endTime)) {
				return MESSAGE_ERROR_START_END_TIME;
			}
		}
		if (startDate.compareTo(endDate) == THIS_DATE_LATER_THAN_OTHER_DATE) {
			return MESSAGE_ERROR_START_END_DATE;
		}
		if (name == null) {
			return MESSAGE_ERROR_EVENT_NAME;
		}
		return CommandParser.STRING_VERIFIED;
	}

	private Command addDayEvent(ArrayList<String> arguments, int keywordOnIndex, int keywordFromIndex,
			                    int keywordToIndex) {
		// To account for non-strict ordering of keywords on, from and to
		int minIndex = Math.min(keywordToIndex, Math.min(keywordFromIndex, keywordOnIndex));
		int maxIndex = Math.max(keywordToIndex, Math.max(keywordFromIndex, keywordOnIndex));
		if (isDayEventInCorrectPosition(arguments, minIndex, maxIndex)) {
			return CommandParser.initInvalidCommand(MESSAGE_ERROR_EVENT_FORMAT);
		}

		// date must be first string after on, start time must be first string after from,
		// end time must be first string after to
		Date date = dateParser.getDate(arguments.get(keywordOnIndex + POSITION_PLUS_ONE).toLowerCase());
		String startTime = timeParser.getTime(arguments.get(keywordFromIndex + POSITION_PLUS_ONE));
		String endTime = timeParser.getTime(arguments.get(keywordToIndex + POSITION_PLUS_ONE));
		String name = nameParser.getName(arguments, CommandParser.POSITION_FIRST_INDEX, minIndex);
		
		// String verificationMsg will only be STRING_VERIFIED if it passes
		// the attribute checks in verification method
		String verificationMsg = verifyEvent(date, startTime, date, endTime, name);
		if (!verificationMsg.equals(CommandParser.STRING_VERIFIED)) {
			return CommandParser.initInvalidCommand(verificationMsg);
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

	private Command addFloatingTask(ArrayList<String> arguments) {
		Command command = new Command(Command.CommandType.ADD);
		command.setDataType(Command.DataType.FLOATING_TASK);
		String name = nameParser.getName(arguments, CommandParser.POSITION_FIRST_INDEX, arguments.size());
		command.setName(name);
		return command;
	}

}
