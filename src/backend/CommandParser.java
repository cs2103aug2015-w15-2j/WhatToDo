package backend;

import struct.Date;
import struct.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class CommandParser {
	
    private static final int POSITION_PARAM_COMMAND = 0;
    private static final int POSITION_FIRST_PARAM_ARGUMENT = 1;
    private static final int POSITION_FIRST_INDEX = 0;
    private static final int POSITION_THIRD_INDEX = 2;
    private static final int POSITION_FOURTH_INDEX = 3;
    private static final int POSITION_PLUS_ONE = 1;
    private static final int POSITION_PLUS_TWO = 2;
    private static final int POSITION_DIFFERENCE_TWO = 2;
    private static final int POSITION_DIFFERENCE_THREE = 3;
    private static final int POSITION_DIFFERENCE_FOUR = 4;
    
    private static final int DIFFERENCE_ZERO = 0;
    
    private static final int FULL_DATE_LENGTH = 6;
    private static final int SMALL_DATE_LENGTH = 4;
    
    private static final int NUMBER_OF_DAYS_IN_A_WEEK = 7;
    private static final int NUMBER_OF_DAYS_IN_TWO_WEEKS = 14;
    
    private static final int NUMBER_REPEATED_EDIT_KEYWORD = -2;
    private static final int NUMBER_NO_EDIT_KEYWORD = -1;
	
    private static final String REGEX_WHITESPACES = "[\\s;]+";
    private static final String REGEX_POSITIVE_INTEGER = "^\\d+$";
    private static final String REGEX_12_HOUR_SIMPLE_TIME = "(1[012]|[1-9]|0[1-9])(\\s)?(?i)(am|pm)";
    private static final String REGEX_12_HOUR_TIME = "(1[012]|[1-9]|0[1-9])(:|.)?" 
                                                    + "[0-5][0-9](\\s)?(?i)(am|pm)";
    private static final String REGEX_24_HOUR_SIMPLE_TIME = "([01]?[0-9]|2[0-3])";
    private static final String REGEX_24_HOUR_TIME = "([01]?[0-9]|2[0-3])(:|.)?[0-5][0-9]";
    
    private static final String STRING_ONE_SPACE = " ";
    private static final String STRING_ONE_DOT = ".";
    private static final String STRING_ONE_COLON = ":";
    private static final String STRING_EMPTY = "";
    private static final String STRING_VERIFIED = "verified";
    private static final String ESCAPE_CHARACTER = "\\";
    
    private static final String TIME_MIDNIGHT = "0000";
    private static final String TIME_ZERO_HOUR = "00";
    private static final String TIME_ZERO_MINUTE = "00";
    private static final int TIME_MAXIMUM = 2400;
    private static final int TIME_ADD_TWELVE = 12;
	
    private static final String USER_COMMAND_ADD = "add";
    private static final String USER_COMMAND_DELETE = "delete";
    private static final String USER_COMMAND_EDIT = "edit";
    private static final String USER_COMMAND_SEARCH = "search";
    private static final String USER_COMMAND_DONE = "done";
    private static final String USER_COMMAND_SET = "set";
    private static final String USER_COMMAND_SAVE = "save";
    private static final String USER_COMMAND_UNDO = "undo";
    private static final String USER_COMMAND_REDO = "redo";
    private static final String USER_COMMAND_VIEW_ALL = "all";
    private static final String USER_COMMAND_VIEW_DEF = "def";
    private static final String USER_COMMAND_VIEW_HIST = "hist";
    private static final String USER_COMMAND_VIEW_UNRES = "unres";
    private static final String USER_COMMAND_VIEW_HELP = "help";
    private static final String USER_COMMAND_VIEW_OPEN_FILE = "openfile";
    private static final String USER_COMMAND_VIEW_CONFIG = "config";
    private static final String USER_COMMAND_EXIT = "exit";
    
    private static final String KEYWORD_DEADLINE = "by";
    private static final String KEYWORD_TODAY = "today";
    private static final String KEYWORD_TOMORROW_ONE = "tomorrow";
    private static final String KEYWORD_TOMORROW_TWO = "tmr";
    private static final String KEYWORD_TOMORROW_THREE = "tomo";
    private static final String KEYWORD_EVENT_TO = "to";
    private static final String KEYWORD_EVENT_FROM = "from";
    private static final String KEYWORD_EVENT_ON = "on";
    private static final String KEYWORD_NEXT = "n";
    private static final String KEYWORD_SET = "as";
    
    private static final String KEYWORD_EDIT_NAME = "name";
    private static final String KEYWORD_EDIT_DEADLINE = "date";
    private static final String KEYWORD_EDIT_START_DATE = "startd";
    private static final String KEYWORD_EDIT_START_TIME = "startt";
    private static final String KEYWORD_EDIT_END_DATE = "endd";
    private static final String KEYWORD_EDIT_END_TIME = "endt";
    
    private static final String ERROR_USER_COMMAND = "Invalid user command!";
    private static final String ERROR_TASK_NAME = "Task name required!";
    private static final String ERROR_DEADLINE = "Invalid deadline!";
    private static final String ERROR_EVENT_FORMAT = "Invalid format for adding event!";
    private static final String ERROR_START_DATE = "Invalid start date!";
    private static final String ERROR_START_TIME = "Invalid start time!";
    private static final String ERROR_END_DATE = "Invalid end date!";
    private static final String ERROR_END_TIME = "Invalid end time!";
    private static final String ERROR_START_END_TIME = "Start time later than or equal to end time for single-day event!";
    private static final String ERROR_START_END_DATE = "Start date later than end date!";    
    private static final String ERROR_EVENT_NAME = "Event name required!";
    private static final String ERROR_EVENT_DATE = "Invalid date!";
    private static final String ERROR_DELETE = "Index/alias command required!";
    private static final String ERROR_DELETE_INDEX_ALIAS = "Invalid index or alias!";
    private static final String ERROR_DELETE_ALIAS = "No such alias has been set!";
    private static final String ERROR_EDIT = "Index required!";
    private static final String ERROR_INDEX = "Invalid index!";
    private static final String ERROR_DOUBLE_EDIT_KEYWORD = "Double keywords not accepted!";
    private static final String ERROR_EDIT_FORMAT = "Invalid edit format!";
    private static final String ERROR_NAME = "New task/event name required!";
    private static final String ERROR_SET = "Command and alias required!";
    private static final String ERROR_SET_FORMAT = "Invalid set format!";
    private static final String ERROR_SET_COMMAND = "Command specified is not a registered command/keyword!";
    private static final String ERROR_SET_ALIAS = "Specified alias is a either a registered command/keyword and cannot be used or an alias-in-use!";
    private static final String ERROR_SET_NUMBER = "Positive integers cannot be used as aliases!";
    private static final String ERROR_SAVE = "Directory required!";
    private static final String ERROR_NO_ARGUMENTS = "This command does not expect arguments!";
    
    private static final ArrayList<String> DAYS_ARRAY_LIST = new ArrayList<String>();
    private static final ArrayList<String> DAYS_FULL_ARRAY_LIST = new ArrayList<String>();
    
    private static final ArrayList<String> COMMANDS_ARRAY_LIST = new ArrayList<String>();
    private Hashtable<String, String> commandAliases = new Hashtable<String, String>();
    
	public CommandParser() {
        initDaysArrayList();
        initFullDaysArrayList();
        initCommandsArrayList();
    }
	
	public CommandParser(Hashtable<String, String> commandAliases) {
		this.commandAliases = commandAliases;
		initDaysArrayList();
		initFullDaysArrayList();
		initCommandsArrayList();
	}

    public Command parse(String userInput) {
        Command command;
        ArrayList<String> parameters = splitString(userInput);
        ArrayList<String> convertedParameters = convertParameters(parameters);
        String userCommand = getUserCommand(convertedParameters);
        ArrayList<String> arguments = getUserArguments(parameters);
        ArrayList<String> convertedArguments = getUserArguments(convertedParameters);

        switch (userCommand.toLowerCase()) {

            case USER_COMMAND_ADD :
                command = initAddCommand(convertedArguments);
                break;

            case USER_COMMAND_DELETE :
                command = initDeleteCommand(arguments);
                break;

            case USER_COMMAND_EDIT :
                command = initEditCommand(convertedArguments);
                break;
                
            case USER_COMMAND_SEARCH :
            	command = initSearchCommand(convertedArguments);
            	break;
            	
            case USER_COMMAND_DONE :
            	command = initDoneCommand(convertedArguments);
            	break;
            	
            case USER_COMMAND_SET :
            	command = initSetCommand(convertedArguments);
            	break;
            	
            case USER_COMMAND_SAVE :
            	command = initSaveCommand(convertedArguments);
            	break;
            	
            case USER_COMMAND_UNDO :
            	command = initUndoCommand(convertedArguments);
            	break;
            	
            case USER_COMMAND_REDO :
            	command = initRedoCommand(convertedArguments);
            	break; 
            	
            case USER_COMMAND_VIEW_ALL :
            	command = initViewAllCommand(convertedArguments);
            	break;
            	
            case USER_COMMAND_VIEW_DEF :
            	command = initViewDefCommand(convertedArguments);
            	break;
            	
            case USER_COMMAND_VIEW_HIST :
            	command = initViewHistCommand(convertedArguments);
            	break;
            	
            case USER_COMMAND_VIEW_UNRES :
            	command = initViewUnresCommand(convertedArguments);
            	break;
            	
            case USER_COMMAND_VIEW_HELP :
            	command = initViewHelpCommand(convertedArguments);
            	break;
            	
            case USER_COMMAND_VIEW_OPEN_FILE :
            	command = initViewOpenFileCommand(convertedArguments);
            	break;
            	
            case USER_COMMAND_VIEW_CONFIG :
            	command = initViewConfigCommand(convertedArguments);
            	break;
                
            case USER_COMMAND_EXIT :
                command = initExitCommand(convertedArguments);
                break;

            default :
                command = initInvalidCommand(ERROR_USER_COMMAND);
        }
        command.setUserInput(userInput);
        return command;
    }
    
    private ArrayList<String> splitString(String arguments) {
        String[] strArray = arguments.trim().split(REGEX_WHITESPACES);
        return new ArrayList<String>(Arrays.asList(strArray));
    }
    
    private ArrayList<String> convertParameters(ArrayList<String> parameters) {
    	for (int i = 0; i < parameters.size(); i++) {
    		String parameter = parameters.get(i);
    		if (commandAliases.containsKey(parameter)) {
    			String newParameter = commandAliases.get(parameter);
    			parameters.remove(i);
    			parameters.add(i, newParameter);
    		}
    	}
    	return parameters;
    }

    private String getUserCommand(ArrayList<String> parameters) {
        return parameters.get(POSITION_PARAM_COMMAND);
    }

    private ArrayList<String> getUserArguments(ArrayList<String> parameters) {
        return new ArrayList<String>(parameters.subList(POSITION_FIRST_PARAM_ARGUMENT,
                                                        parameters.size()));
    }
	
    
    // ================================================================
    // Create add command methods
    // ================================================================
	
	private Command initAddCommand(ArrayList<String> arguments) {
		Command command;
		
		if (arguments.isEmpty()) {
			return initInvalidCommand(ERROR_TASK_NAME);
		} else if (arguments.contains(KEYWORD_DEADLINE)) {
			command = addTask(arguments);
		} else if (arguments.contains(KEYWORD_EVENT_TO) && 
				   arguments.contains(KEYWORD_EVENT_FROM)) {
			if (arguments.contains(KEYWORD_EVENT_ON)) {
				command = addDayEvent(arguments);
			} else {
				command = addEvent(arguments);
			} 
		} else {
			command = addFloatingTask(arguments);
		}
		return command;
	}
	
	private Command addTask(ArrayList<String> arguments) {
		int keywordIndex = arguments.indexOf(KEYWORD_DEADLINE);
		int index = keywordIndex + POSITION_PLUS_ONE;
		String dateString = getDateString(arguments, index);
		Date date = getDate(dateString);
		List<String> nameList = arguments.subList(POSITION_FIRST_INDEX, keywordIndex);
		String name = getName(nameList);
		
		if (date == null) {
			return initInvalidCommand(ERROR_DEADLINE);
		}
		if (name == null) {
			return initInvalidCommand(ERROR_TASK_NAME);
		}
		
		Command command = new Command(Command.CommandType.ADD);
		command.setDataType(Command.DataType.TASK);
		command.setName(name);
		command.setDueDate(date);
		return command;
	}

	private String getDateString(ArrayList<String> arguments, int index) {
		String dateString = STRING_EMPTY;
		while (index < arguments.size()) {
			dateString += arguments.get(index);
			index++;
		}
		return dateString;
	}
	
	private Command addEvent(ArrayList<String> arguments) {
		int keywordToIndex = arguments.indexOf(KEYWORD_EVENT_TO);
		int keywordFromIndex = arguments.indexOf(KEYWORD_EVENT_FROM);
		
		if (Math.abs(keywordToIndex - keywordFromIndex) != POSITION_DIFFERENCE_THREE 
			|| (arguments.size() - keywordToIndex != POSITION_DIFFERENCE_THREE 
			&& arguments.size() - keywordFromIndex != POSITION_DIFFERENCE_THREE)) {
			return initInvalidCommand(ERROR_EVENT_FORMAT);
		}
		
		Date startDate = getDate(arguments.get(keywordFromIndex + POSITION_PLUS_ONE));
		String startTime = getTime(arguments.get(keywordFromIndex + POSITION_PLUS_TWO));
		Date endDate = getDate(arguments.get(keywordToIndex + POSITION_PLUS_ONE));
		String endTime = getTime(arguments.get(keywordToIndex + POSITION_PLUS_TWO));
		String name = null;
		
		if (startDate == null) {
			return initInvalidCommand(ERROR_START_DATE);
		}
		if (startTime == null) {
			return initInvalidCommand(ERROR_START_TIME);
		}
		if (endDate == null) {
			return initInvalidCommand(ERROR_END_DATE);
		}
		if (endTime == null) {
			return initInvalidCommand(ERROR_END_TIME);
		}
		if (startDate.compareTo(endDate) == 0) {
			if (!areValidTimes(startTime, endTime)) {
				return initInvalidCommand(ERROR_START_END_TIME);
			}
		}
		if (startDate.compareTo(endDate) == 1) {
			return initInvalidCommand(ERROR_START_END_DATE);
		}
		
		if (keywordToIndex > keywordFromIndex) {
			List<String> nameList = arguments.subList(POSITION_FIRST_INDEX, keywordFromIndex);
			name = getName(nameList);
		} else {
			List<String> nameList = arguments.subList(POSITION_FIRST_INDEX, keywordToIndex);
			name = getName(nameList);
		}
		if (name == null) {
			return initInvalidCommand(ERROR_EVENT_NAME);
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
	
	private Command addDayEvent(ArrayList<String> arguments) {
		int keywordToIndex = arguments.indexOf(KEYWORD_EVENT_TO);
		int keywordFromIndex = arguments.indexOf(KEYWORD_EVENT_FROM);
		int keywordOnIndex = arguments.indexOf(KEYWORD_EVENT_ON);
		int minIndex = Math.min(keywordToIndex, Math.min(keywordFromIndex, keywordOnIndex));
		int maxIndex = Math.max(keywordToIndex, Math.max(keywordFromIndex, keywordOnIndex));
		
		if (maxIndex - minIndex != POSITION_DIFFERENCE_FOUR 
			|| arguments.size() - maxIndex != POSITION_DIFFERENCE_TWO) {
			return initInvalidCommand(ERROR_EVENT_FORMAT);
		}
		
		Date date = getDate(arguments.get(keywordOnIndex + POSITION_PLUS_ONE));
		String startTime = getTime(arguments.get(keywordFromIndex + POSITION_PLUS_ONE));
		String endTime = getTime(arguments.get(keywordToIndex + POSITION_PLUS_ONE));
		List<String> nameList = arguments.subList(POSITION_FIRST_INDEX, minIndex);
		String name = getName(nameList);
		
		if (name == null) {
			return initInvalidCommand(ERROR_EVENT_NAME);
		}
		if (date == null) {
			return initInvalidCommand(ERROR_EVENT_DATE);
		}
		if (startTime == null) {
			return initInvalidCommand(ERROR_START_TIME);
		}
		if (endTime == null) {
			return initInvalidCommand(ERROR_END_TIME);
		}
		if (!areValidTimes(startTime, endTime)) {
			return initInvalidCommand(ERROR_START_END_TIME);
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
	
	private Command addFloatingTask(ArrayList<String> arguments) {
		Command command = new Command(Command.CommandType.ADD);
		command.setDataType(Command.DataType.FLOATING_TASK);
		List<String> nameList = arguments.subList(POSITION_FIRST_INDEX, arguments.size());
		String name = getName(nameList);
		command.setName(name);
		return command;
	}
	
	private String getName(List<String> arguments) {
		String name = "";
		if (arguments.isEmpty()) {
			return null;
		}
		for (int i = 0; i < arguments.size(); i++) {
			String currArgument = arguments.get(i);
			if (currArgument.startsWith(ESCAPE_CHARACTER)) {
				currArgument = currArgument.substring(1);
			}
			name += currArgument + STRING_ONE_SPACE;
		}
		return name.trim();
	}
	
	private Date getDate(String date) {
		Date todayDate = Date.todayDate();
		if (date.equals(KEYWORD_TODAY)){
			return todayDate;
		} else if (date.equals(KEYWORD_TOMORROW_ONE) || date.equals(KEYWORD_TOMORROW_TWO) 
				   || date.equals(KEYWORD_TOMORROW_THREE)) {
			return Date.tomorrowDate();
		} else if (DAYS_FULL_ARRAY_LIST.contains(date.toLowerCase())) {
			String daySubstring = date.substring(POSITION_FIRST_INDEX, POSITION_FOURTH_INDEX).toLowerCase();
			int day = DAYS_ARRAY_LIST.indexOf(daySubstring);
			String todayString = todayDate.getDayString();
			String todaySubstring = todayString.substring(POSITION_FIRST_INDEX, POSITION_FOURTH_INDEX).toLowerCase();
			int today = DAYS_ARRAY_LIST.indexOf(todaySubstring);
			int difference = day - today;
			if (difference >= DIFFERENCE_ZERO) {
				return todayDate.plusDay(difference);
			} else {
				return todayDate.plusDay(difference + NUMBER_OF_DAYS_IN_A_WEEK);
			}
		} else if (date.substring(0,1).equals(KEYWORD_NEXT) 
				&& DAYS_FULL_ARRAY_LIST.contains(date.substring(1).toLowerCase())) {
			int day = DAYS_ARRAY_LIST.indexOf(date.substring(1,4).toLowerCase());
			int today = DAYS_ARRAY_LIST.indexOf(todayDate.getDayString().substring(0,3).toLowerCase());
			int difference = day - today;
			if (difference >= DIFFERENCE_ZERO) {
				return todayDate.plusDay(difference + NUMBER_OF_DAYS_IN_A_WEEK);
			} else {
				return todayDate.plusDay(difference + NUMBER_OF_DAYS_IN_TWO_WEEKS);
			}
		} else if (date.matches(REGEX_POSITIVE_INTEGER) && String.valueOf(date).length() == SMALL_DATE_LENGTH) {
			String year = todayDate.formatDateShort().substring(4);
			date = date + year;
			Date currDate = new Date(date);
			if (isValidDate(date) && todayDate.compareTo(currDate) <= 0) {
				return currDate;
			}
		} else if (date.matches(REGEX_POSITIVE_INTEGER) 
				&& String.valueOf(date).length() == FULL_DATE_LENGTH && isValidDate(date)) {
			Date currDate = new Date(date);
			if (todayDate.compareTo(currDate) <= 0) {
				return currDate;
			}
		}
		return null;
	}
	
	private boolean isValidDate(String date) {
		int[] daysInEachMonth = {0,31,28,31,30,31,30,31,31,30,31,30,31};
		int day = Integer.parseInt(date.substring(0,2));
		int month = Integer.parseInt(date.substring(2,4));
		int year = 2000 + Integer.parseInt(date.substring(4));
		if (year % 4 == 0) {
			if (year % 100 != 0) {
				daysInEachMonth[2] = 29;
			} else {
				if (year % 400 == 0) {
					daysInEachMonth[2] = 29;
				}
			}
		}
		if (month < 13 && day <= daysInEachMonth[month]) {
			return true;
		} else {
			return false;
		}
	}
	
	private void initDaysArrayList() {
		DAYS_ARRAY_LIST.add("mon");
		DAYS_ARRAY_LIST.add("tue");
		DAYS_ARRAY_LIST.add("wed");
		DAYS_ARRAY_LIST.add("thu");
		DAYS_ARRAY_LIST.add("fri");
		DAYS_ARRAY_LIST.add("sat");
		DAYS_ARRAY_LIST.add("sun");
	}
	
	private void initFullDaysArrayList() {
		DAYS_FULL_ARRAY_LIST.add("mon");
		DAYS_FULL_ARRAY_LIST.add("tue");
		DAYS_FULL_ARRAY_LIST.add("wed");
		DAYS_FULL_ARRAY_LIST.add("thu");
		DAYS_FULL_ARRAY_LIST.add("fri");
		DAYS_FULL_ARRAY_LIST.add("sat");
		DAYS_FULL_ARRAY_LIST.add("sun");
		DAYS_FULL_ARRAY_LIST.add("monday");
		DAYS_FULL_ARRAY_LIST.add("tuesday");
		DAYS_FULL_ARRAY_LIST.add("wednesday");
		DAYS_FULL_ARRAY_LIST.add("thursday");
		DAYS_FULL_ARRAY_LIST.add("friday");
		DAYS_FULL_ARRAY_LIST.add("saturday");
		DAYS_FULL_ARRAY_LIST.add("sunday");
	}
	
	private String getTime(String time) {
		String timeString;
		if (time.matches(REGEX_12_HOUR_SIMPLE_TIME)) {
			String period = time.substring(time.length() - 2).toLowerCase();
			String hourString = time.substring(0, time.length() - 2).trim();
			int hourInt = Integer.parseInt(hourString);
			if (period.equals("pm") && hourInt != 12) {
				hourInt += TIME_ADD_TWELVE;
			}
			hourString = Integer.toString(hourInt);
			if (period.equals("am") && hourInt == 12) {
				timeString = TIME_MIDNIGHT;
			} else if (hourString.length() == 1) {
				timeString = "0" + hourString + TIME_ZERO_MINUTE;
			} else {
				timeString = hourString + TIME_ZERO_MINUTE;
			}
			return timeString;
		} else if (time.matches(REGEX_12_HOUR_TIME)) {
			String period = time.substring(time.length() - 2).toLowerCase();
			timeString = time.substring(0, time.length() - 2).trim();
			timeString = timeString.replace(STRING_ONE_DOT, STRING_EMPTY);
			timeString = timeString.replace(STRING_ONE_COLON, STRING_EMPTY);
			String minuteString = timeString.substring(timeString.length() - 2);
			String hourString = timeString.substring(0, timeString.length() - 2);
			int hourInt = Integer.parseInt(hourString);
			if (period.equals("pm") && hourInt != 12) {
				hourInt += TIME_ADD_TWELVE;
			}
			if (period.equals("am") && hourInt == 12) {
				return TIME_ZERO_HOUR + minuteString;
			}
			hourString = Integer.toString(hourInt);
			if (hourString.length() == 1) {
				timeString = "0" + hourString + minuteString;
			} else {
				timeString = hourString + minuteString;
			}
			if (Integer.parseInt(timeString) < TIME_MAXIMUM) {
				return timeString;
			}
		} else if (time.matches(REGEX_24_HOUR_SIMPLE_TIME)){
			timeString = time;
			if (time.length() == 1) {
				timeString = "0" + time + TIME_ZERO_MINUTE;
			} else if (time.length() == 2) {
				timeString = time + TIME_ZERO_MINUTE;
			}
			return timeString;
		} else if (time.matches(REGEX_24_HOUR_TIME)) {
			timeString = time;
			timeString = timeString.replace(".","");
			timeString = timeString.replace(":","");
			if (timeString.length() == 3) {
				timeString = "0" + timeString;
			}
			if (Integer.parseInt(timeString) < TIME_MAXIMUM) {
				return timeString;
			}
		}
		return null;
	}
	
	private boolean areValidTimes(String startTime, String endTime) {
		int intStartTime = Integer.parseInt(startTime);
		int intEndTime = Integer.parseInt(endTime);
		if (intEndTime > intStartTime) {
			return true;
		} else {
			return false;
		}
	}

	
    // ================================================================
    // Create delete command methods
    // ================================================================
	
	private Command initDeleteCommand(ArrayList<String> arguments) {
		if (arguments.isEmpty()) {
			return initInvalidCommand(ERROR_DELETE);
		}
		
		if (arguments.size() > 1) {
			return initInvalidCommand(ERROR_DELETE_INDEX_ALIAS);
		}
		
		String argument = arguments.get(0);
		
		if (argument.matches(REGEX_POSITIVE_INTEGER)) {
			return deleteIndex(Integer.parseInt(argument));
		} else {
			return deleteAlias(argument);
		}
	}
	
	private Command deleteAlias(String alias) {
		 if (!commandAliases.containsKey(alias)) {
			 return initInvalidCommand(ERROR_DELETE_ALIAS);
		 } else {
			 String originalCommand = commandAliases.get(alias);
			 Command command = new Command(Command.CommandType.DELETEALIAS);
			 command.setName(alias);
			 command.setOriginalCommand(originalCommand);
			 return command;
		 }
	}
	
	private Command deleteIndex(int index) {
		Command command = new Command(Command.CommandType.DELETE);
		command.setIndex(index);
		return command;
	}
	
	private int getIndex(ArrayList<String> arguments) {
		try {
			int index = Integer.parseInt(arguments.get(0));
			return index;
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	public void deleteAliasFromHash(String command) {
		commandAliases.remove(command);
	}
	
	
    // ================================================================
    // Create edit command methods
    // ================================================================
	
	private Command initEditCommand(ArrayList<String> arguments) {
		
		if (arguments.isEmpty()) {
			return initInvalidCommand(ERROR_EDIT);
		}
		
		
		int index = Integer.parseInt(arguments.get(0));
		if (index < 1) {
			return initInvalidCommand(ERROR_INDEX);
		}
		
		int nameIndex = getKeywordIndex(arguments, KEYWORD_EDIT_NAME);
		int deadlineIndex = getKeywordIndex(arguments, KEYWORD_EDIT_DEADLINE);
		int startDateIndex = getKeywordIndex(arguments, KEYWORD_EDIT_START_DATE);
		int startTimeIndex = getKeywordIndex(arguments, KEYWORD_EDIT_START_TIME);
		int endDateIndex = getKeywordIndex(arguments, KEYWORD_EDIT_END_DATE);
		int endTimeIndex = getKeywordIndex(arguments, KEYWORD_EDIT_END_TIME);
		
		if (repeatedKeywords(nameIndex, deadlineIndex, startDateIndex, 
				             startTimeIndex, endDateIndex, endTimeIndex) != null) {
			String repeatedKeyword = repeatedKeywords(nameIndex, deadlineIndex, startDateIndex, 
					                                  startTimeIndex, endDateIndex, endTimeIndex);
			String errorMsg = "Double keywords " + repeatedKeyword + " not accepted!";
			return initInvalidCommand(ERROR_DOUBLE_EDIT_KEYWORD);
		}
		if (!(deadlineIndex < 0)) {
			if (!(startDateIndex < 0) || !(startTimeIndex < 0) || !(endDateIndex < 0) || !(endTimeIndex < 0)) {
				return initInvalidCommand(ERROR_EDIT_FORMAT);
			}
		}
		
		ArrayList<Integer> indexArrayList = getIndexArrayList(nameIndex, deadlineIndex, startDateIndex, 
				                                              startTimeIndex, endDateIndex, endTimeIndex);
		
		if (indexArrayList.isEmpty()) {
			return initInvalidCommand(ERROR_EDIT_FORMAT);
		}
		
		Hashtable<Integer, String> indexKeywordHash = getHashtable(nameIndex, deadlineIndex, startDateIndex, 
				                                                   startTimeIndex, endDateIndex, endTimeIndex);
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
			List<String> argumentsSublist = arguments.subList(keywordIndex + 1, nextKeywordIndex);
			String argument = getName(argumentsSublist);
			
			 switch (keyword) {
			 
			 	case KEYWORD_EDIT_NAME :
			 		command = editName(command, argument);
			 		break;

	            case KEYWORD_EDIT_DEADLINE :
	                command = editDeadline(command, argument);
	                break;
	                
	            case KEYWORD_EDIT_START_DATE :
	            	command = editStartDate(command, argument);
	            	break;
	            
	            case KEYWORD_EDIT_START_TIME :
	            	command = editStartTime(command, argument);
	            	break;
	            	
	            case KEYWORD_EDIT_END_DATE :
	            	command = editEndDate(command, argument);
	            	break;
	            	
	            case KEYWORD_EDIT_END_TIME :
	            	command = editEndTime(command, argument);
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
	
	private Command editName (Command command, String argument) {
		if (argument == null) {
			return initInvalidCommand(ERROR_NAME);
		}
		command.setName(argument);
		return command;
	}
	
	private Command editDeadline(Command command, String argument) {
		Date deadline = getDate(argument);
		if (deadline == null) {
			return initInvalidCommand(ERROR_DEADLINE);
		}
		command.setDueDate(deadline);
		return command;
	}
	
	private Command editStartDate(Command command, String argument) {
		Date startDate = getDate(argument);
		if (startDate == null) {
			return initInvalidCommand(ERROR_START_DATE);
		}
		command.setStartDate(startDate);
		return command;
	}
	
	private Command editStartTime(Command command, String argument) {
		String startTime = getTime(argument);
		if (startTime == null) {
			return initInvalidCommand(ERROR_START_TIME);
		}
		command.setStartTime(startTime);
		return command;
	}
	
	private Command editEndDate(Command command, String argument) {
		Date endDate = getDate(argument);
		if (endDate == null) {
			return initInvalidCommand(ERROR_END_DATE);
		}
		command.setEndDate(endDate);
		return command;
	}
	
	private Command editEndTime(Command command, String argument) {
		String endTime = getTime(argument);
		if (endTime == null) {
			return initInvalidCommand(ERROR_END_TIME);
		}
		command.setEndTime(endTime);
		return command;
	}
	
	
    // ================================================================
    // Create search command method
    // ================================================================
	
	private Command initSearchCommand(ArrayList<String> arguments) {
		if (arguments.isEmpty()) {
			Command command = new Command(Command.CommandType.VIEW);
			command.setViewType(Command.ViewType.SEARCH);
			return command;
		}
		List<String> searchList = arguments.subList(0, arguments.size());
		String searchWords = getName(searchList);
		Command command = new Command(Command.CommandType.SEARCH);
		command.setName(searchWords);
		return command;
	}
	
	
    // ================================================================
    // Create done command method
    // ================================================================
	
	private Command initDoneCommand(ArrayList<String> arguments) {
		if (arguments.isEmpty()) {
			Command command = new Command(Command.CommandType.VIEW);
			command.setViewType(Command.ViewType.DONE);
			return command;
		}
		int index = getIndex(arguments);
		
		if (arguments.size() > 1 || index <= 0) {
			return initInvalidCommand(ERROR_INDEX);
		}
		
		Command command = new Command(Command.CommandType.DONE);
		command.setIndex(index);
		return command;
	}
	
	
    // ================================================================
    // Create set command methods
    // ================================================================
	
	private Command initSetCommand(ArrayList<String> arguments) {
		if (arguments.isEmpty()) {
			return initInvalidCommand(ERROR_SET);
		}
		if (arguments.get(1) != KEYWORD_SET || arguments.size() != 3) {
			return initInvalidCommand(ERROR_SET_FORMAT);
		}
		String commandKeyword = arguments.get(0);
		String alias = arguments.get(2);
		String commandKeywordVerified = verifyCommandKeyword(commandKeyword);
		String aliasVerified = verifyAlias(alias);
		if (!commandKeywordVerified.equals(STRING_VERIFIED)) {
			return initInvalidCommand(commandKeywordVerified);
		}
		if (!aliasVerified.equals(STRING_VERIFIED)) {
			return initInvalidCommand(aliasVerified);
		}
		
		Command command = new Command(Command.CommandType.SET);
		command.setName(alias);
		command.setOriginalCommand(commandKeyword);
		return command;
	}
	
	private String verifyCommandKeyword(String commandKeyword) {
		if (COMMANDS_ARRAY_LIST.contains(commandKeyword)) {
			return STRING_VERIFIED;
		} else {
			String errorMsg = commandKeyword + " is not a registered command/keyword";
			return ERROR_SET_COMMAND;
		}
	}
	
	private String verifyAlias(String alias) {
		if (COMMANDS_ARRAY_LIST.contains(alias)) {
			return ERROR_SET_ALIAS;
		} else if (alias.matches(REGEX_POSITIVE_INTEGER)) {
			return ERROR_SET_NUMBER;
		} else {
			return STRING_VERIFIED;
		}
	}
	
	private void initCommandsArrayList() {
		COMMANDS_ARRAY_LIST.add(USER_COMMAND_ADD);
		COMMANDS_ARRAY_LIST.add(USER_COMMAND_DELETE);
		COMMANDS_ARRAY_LIST.add(USER_COMMAND_EDIT);
		COMMANDS_ARRAY_LIST.add(USER_COMMAND_SEARCH);
		COMMANDS_ARRAY_LIST.add(USER_COMMAND_DONE);
		COMMANDS_ARRAY_LIST.add(USER_COMMAND_SET);
		COMMANDS_ARRAY_LIST.add(USER_COMMAND_SAVE);
		COMMANDS_ARRAY_LIST.add(USER_COMMAND_UNDO);
		COMMANDS_ARRAY_LIST.add(USER_COMMAND_REDO);
		COMMANDS_ARRAY_LIST.add(USER_COMMAND_VIEW_ALL);
		COMMANDS_ARRAY_LIST.add(USER_COMMAND_VIEW_DEF);
		COMMANDS_ARRAY_LIST.add(USER_COMMAND_VIEW_HIST);
		COMMANDS_ARRAY_LIST.add(USER_COMMAND_VIEW_UNRES);
		COMMANDS_ARRAY_LIST.add(USER_COMMAND_VIEW_HELP);
		COMMANDS_ARRAY_LIST.add(USER_COMMAND_VIEW_OPEN_FILE);
		COMMANDS_ARRAY_LIST.add(USER_COMMAND_VIEW_CONFIG);
		COMMANDS_ARRAY_LIST.add(USER_COMMAND_EXIT);
		COMMANDS_ARRAY_LIST.add(KEYWORD_DEADLINE);
		COMMANDS_ARRAY_LIST.add(KEYWORD_TODAY);
		COMMANDS_ARRAY_LIST.add(KEYWORD_TOMORROW_ONE);
		COMMANDS_ARRAY_LIST.add(KEYWORD_TOMORROW_TWO);
		COMMANDS_ARRAY_LIST.add(KEYWORD_TOMORROW_THREE);
		COMMANDS_ARRAY_LIST.add(KEYWORD_EVENT_TO);
		COMMANDS_ARRAY_LIST.add(KEYWORD_EVENT_FROM);
		COMMANDS_ARRAY_LIST.add(KEYWORD_EVENT_ON);
		COMMANDS_ARRAY_LIST.add(KEYWORD_SET);
		COMMANDS_ARRAY_LIST.add(KEYWORD_EDIT_NAME);
		COMMANDS_ARRAY_LIST.add(KEYWORD_EDIT_DEADLINE);
		COMMANDS_ARRAY_LIST.add(KEYWORD_EDIT_START_DATE);
		COMMANDS_ARRAY_LIST.add(KEYWORD_EDIT_START_TIME);
		COMMANDS_ARRAY_LIST.add(KEYWORD_EDIT_END_DATE);
		COMMANDS_ARRAY_LIST.add(KEYWORD_EDIT_END_TIME);
	}
	
	public void setAlias(String newCommand, String originalCommand) {
		commandAliases.put(newCommand, originalCommand);
	}
	
	
    // ================================================================
    // Create save command method
    // ================================================================
	
	private Command initSaveCommand(ArrayList<String> arguments) {
		if (arguments.isEmpty()) {
			return initInvalidCommand(ERROR_SAVE);
		}
		Command command = new Command(Command.CommandType.SAVE);
		List<String> directoryList = arguments.subList(0, arguments.size());
		String directory = getName(directoryList);
		command.setName(directory);
		return command;
	}
	
	
    // ================================================================
    // Create undo command method
    // ================================================================
	
	private Command initUndoCommand(ArrayList<String> arguments) {
		if (!arguments.isEmpty()) {
			return initInvalidCommand(ERROR_NO_ARGUMENTS);
		}
		Command command = new Command(Command.CommandType.UNDO);
		return command;
	}
	
	
    // ================================================================
    // Create redo command method
    // ================================================================
	
	private Command initRedoCommand(ArrayList<String> arguments) {
		if (!arguments.isEmpty()) {
			return initInvalidCommand(ERROR_NO_ARGUMENTS);
		}
		Command command = new Command(Command.CommandType.REDO);
		return command;
	}
	
	
    // ================================================================
    // Create view all command method
    // ================================================================
	
	private Command initViewAllCommand(ArrayList<String> arguments) {
		if (!arguments.isEmpty()) {
			return initInvalidCommand(ERROR_NO_ARGUMENTS);
		}
		Command command = new Command(Command.CommandType.VIEW);
		command.setViewType(Command.ViewType.ALL);
		return command;
	}
	
	
    // ================================================================
    // Create view def command method
    // ================================================================
	
	private Command initViewDefCommand(ArrayList<String> arguments) {
		if (!arguments.isEmpty()) {
			return initInvalidCommand(ERROR_NO_ARGUMENTS);
		}
		Command command = new Command(Command.CommandType.VIEW);
		command.setViewType(Command.ViewType.DEF);
		return command;
	}
	
	
    // ================================================================
    // Create view hist command method
    // ================================================================
	
	private Command initViewHistCommand(ArrayList<String> arguments) {
		if (!arguments.isEmpty()) {
			return initInvalidCommand(ERROR_NO_ARGUMENTS);
		}
		Command command = new Command(Command.CommandType.VIEW);
		command.setViewType(Command.ViewType.HIST);
		return command;
	}
	
	
    // ================================================================
    // Create view unres command method
    // ================================================================
	
	private Command initViewUnresCommand(ArrayList<String> arguments) {
		if (!arguments.isEmpty()) {
			return initInvalidCommand(ERROR_NO_ARGUMENTS);
		}
		Command command = new Command(Command.CommandType.VIEW);
		command.setViewType(Command.ViewType.UNRES);
		return command;
	}
	
	
    // ================================================================
    // Create view help command method
    // ================================================================
	
	private Command initViewHelpCommand(ArrayList<String> arguments) {
		if (!arguments.isEmpty()) {
			return initInvalidCommand(ERROR_NO_ARGUMENTS);
		}
		Command command = new Command(Command.CommandType.VIEW);
		command.setViewType(Command.ViewType.HELP);
		return command;
	}
	
	
    // ================================================================
    // Create view open file command method
    // ================================================================
	
	private Command initViewOpenFileCommand(ArrayList<String> arguments) {
		if (!arguments.isEmpty()) {
			return initInvalidCommand(ERROR_NO_ARGUMENTS);
		}
		Command command = new Command(Command.CommandType.VIEW);
		command.setViewType(Command.ViewType.OPENFILE);
		return command;
	}
	
	
    // ================================================================
    // Create view config command method
    // ================================================================
	
	private Command initViewConfigCommand(ArrayList<String> arguments) {
		if (!arguments.isEmpty()) {
			return initInvalidCommand(ERROR_NO_ARGUMENTS);
		}
		Command command = new Command(Command.CommandType.VIEW);
		command.setViewType(Command.ViewType.CONFIG);
		return command;
	}
	
	
    // ================================================================
    // Create exit command method
    // ================================================================
	
	private Command initExitCommand(ArrayList<String> arguments) {
		if (!arguments.isEmpty()) {
			return initInvalidCommand(ERROR_NO_ARGUMENTS);
		}
		return new Command(Command.CommandType.EXIT);
	}
	
	
    // ================================================================
    // Create invalid command methods
    // ================================================================
	
	private Command initInvalidCommand(String errorMsg) {
		Command command = new Command(Command.CommandType.INVALID);
		command.setName(errorMsg);
		return command;
	}

}
