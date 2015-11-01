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
	
    private static final String REGEX_WHITESPACES = "[\\s;]+";
    private static final String REGEX_POSITIVE_INTEGER = "^\\d+$";
    private static final String REGEX_12_HOUR_SIMPLE_TIME = "(1[012]|[1-9]|0[1-9])(\\s)?(?i)(am|pm)";
    private static final String REGEX_12_HOUR_TIME = "(1[012]|[1-9]|0[1-9])(:|.)?" 
                                                    + "[0-5][0-9](\\s)?(?i)(am|pm)";
    private static final String REGEX_24_HOUR_SIMPLE_TIME = "([01]?[0-9]|2[0-3])";
    private static final String REGEX_24_HOUR_TIME = "([01]?[0-9]|2[0-3])(:|.)?[0-5][0-9]";

    private static final String STRING_ONE_SPACE = " ";
    private static final String ESCAPE_CHARACTER = "\\";
	
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
    
    private static final ArrayList<String> DAYS_ARRAY_LIST = new ArrayList<String>();
    private static final ArrayList<String> DAYS_FULL_ARRAY_LIST = new ArrayList<String>();
    
    private Hashtable<String, String> commandAliases = new Hashtable<String, String>();
    
	public CommandParser() {
        initDaysArrayList();
        initFullDaysArrayList();
    }

    public Command parse(String userInput) {
        Command command;
        ArrayList<String> parameters = splitString(userInput);
        String userCommand = getUserCommand(parameters);
        ArrayList<String> arguments = getUserArguments(parameters);

        switch (userCommand.toLowerCase()) {

            case USER_COMMAND_ADD :
                command = initAddCommand(arguments);
                break;

            case USER_COMMAND_DELETE :
                command = initDeleteCommand(arguments);
                break;

            case USER_COMMAND_EDIT :
                command = initEditCommand(arguments);
                break;
                
            case USER_COMMAND_SEARCH :
            	command = initSearchCommand(arguments);
            	break;
            	
            case USER_COMMAND_DONE :
            	command = initDoneCommand(arguments);
            	break;
            	
            case USER_COMMAND_SET :
            	command = initSetCommand(arguments);
            	break;
            	
            case USER_COMMAND_SAVE :
            	command = initSaveCommand(arguments);
            	break;
            	
            case USER_COMMAND_UNDO :
            	command = initUndoCommand(arguments);
            	break;
            	
            case USER_COMMAND_REDO :
            	command = initRedoCommand(arguments);
            	break; 
            	
            case USER_COMMAND_VIEW_ALL :
            	command = initViewAllCommand(arguments);
            	break;
            	
            case USER_COMMAND_VIEW_DEF :
            	command = initViewDefCommand(arguments);
            	break;
            	
            case USER_COMMAND_VIEW_HIST :
            	command = initViewHistCommand(arguments);
            	break;
            	
            case USER_COMMAND_VIEW_UNRES :
            	command = initViewUnresCommand(arguments);
            	break;
            	
            case USER_COMMAND_VIEW_HELP :
            	command = initViewHelpCommand(arguments);
            	break;
            	
            case USER_COMMAND_VIEW_OPEN_FILE :
            	command = initViewOpenFileCommand(arguments);
            	break;
            	
            case USER_COMMAND_VIEW_CONFIG :
            	command = initViewConfigCommand(arguments);
            	break;
                
            case USER_COMMAND_EXIT :
                command = initExitCommand(arguments);
                break;

            default :
            	String errorMsg = "User Command not recgonised!";
                command = initInvalidCommand(errorMsg);
        }
        command.setUserInput(userInput);
        return command;
    }
    
    private ArrayList<String> splitString(String arguments) {
        String[] strArray = arguments.trim().split(REGEX_WHITESPACES);
        return new ArrayList<String>(Arrays.asList(strArray));
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
			String errorMsg = "Please input the task name!";
			return initInvalidCommand(errorMsg);
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
			String errorMsg = "Invalid Date";
			return initInvalidCommand(errorMsg);
		}
		if (name == null) {
			String errorMsg = "Invalid Task Name!";
			return initInvalidCommand(errorMsg);
		}
		
		Command command = new Command(Command.CommandType.ADD);
		command.setDataType(Command.DataType.TASK);
		command.setName(name);
		command.setDueDate(date);
		return command;
	}

	private String getDateString(ArrayList<String> arguments, int index) {
		String dateString = "";
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
			String errorMsg = "Invalid format for adding Event";
			return initInvalidCommand(errorMsg);
		}
		
		Date startDate = getDate(arguments.get(keywordFromIndex + POSITION_PLUS_ONE));
		String startTime = getTime(arguments.get(keywordFromIndex + POSITION_PLUS_TWO));
		Date endDate = getDate(arguments.get(keywordToIndex + POSITION_PLUS_ONE));
		String endTime = getTime(arguments.get(keywordToIndex + POSITION_PLUS_TWO));
		String name = null;
		
		if (startDate == null) {
			String errorMsg = "Invalid Start Date";
			return initInvalidCommand(errorMsg);
		}
		if (startTime == null) {
			String errorMsg = "Invalid Start Time";
			return initInvalidCommand(errorMsg);
		}
		if (endDate == null) {
			String errorMsg = "Invalid End Date";
			return initInvalidCommand(errorMsg);
		}
		if (endTime == null) {
			String errorMsg = "Invalid End Time";
			return initInvalidCommand(errorMsg);
		}
		if (startDate.compareTo(endDate) == 0) {
			if (!areValidTimes(startTime, endTime)) {
				String errorMsg = "Start Time cannot be later than or equal to End Time for single day Events";
				return initInvalidCommand(errorMsg);
			}
		}
		if (startDate.compareTo(endDate) == 1) {
			String errorMsg = "Start Date cannot be later than End Date";
			return initInvalidCommand(errorMsg);
		}
		
		if (keywordToIndex > keywordFromIndex) {
			List<String> nameList = arguments.subList(POSITION_FIRST_INDEX, keywordFromIndex);
			name = getName(nameList);
		} else {
			List<String> nameList = arguments.subList(POSITION_FIRST_INDEX, keywordToIndex);
			name = getName(nameList);
		}
		if (name == null) {
			String errorMsg = "Invalid Event name";
			return initInvalidCommand(errorMsg);
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
			String errorMsg = "Invalid format for adding Event";
			return initInvalidCommand(errorMsg);
		}
		
		Date date = getDate(arguments.get(keywordOnIndex + POSITION_PLUS_ONE));
		String startTime = getTime(arguments.get(keywordFromIndex + POSITION_PLUS_ONE));
		String endTime = getTime(arguments.get(keywordToIndex + POSITION_PLUS_ONE));
		List<String> nameList = arguments.subList(POSITION_FIRST_INDEX, minIndex);
		String name = getName(nameList);
		
		if (date == null) {
			String errorMsg = "Invalid Date";
			return initInvalidCommand(errorMsg);
		}
		if (startTime == null) {
			String errorMsg = "Invalid Start Time";
			return initInvalidCommand(errorMsg);
		}
		if (endTime == null) {
			String errorMsg = "Invalid End Time";
			return initInvalidCommand(errorMsg);
		}
		if (!areValidTimes(startTime, endTime)) {
			String errorMsg = "Start Time cannot be later than or equal to End Time";
			return initInvalidCommand(errorMsg);
		}
		if (name == null) {
			String errorMsg = "Invalid Event Name";
			return initInvalidCommand(errorMsg);
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
		} else if (date.matches(REGEX_POSITIVE_INTEGER) && String.valueOf(date).length() == 4) {
			String year = todayDate.getFullDate().substring(4);
			date = date + year;
			Date currDate = new Date(date);
			if (isValidDate(date) && todayDate.compareTo(currDate) <= 0) {
				return currDate;
			}
		} else if (date.matches(REGEX_POSITIVE_INTEGER) 
				&& String.valueOf(date).length() == 6 && isValidDate(date)) {
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
				hourInt += 12;
			}
			if (period.equals("am") && hourInt == 12) {
				timeString = "0000";
			}
			hourString = Integer.toString(hourInt);
			if (hourString.length() == 1) {
				timeString = "0" + hourString + "00";
			} else {
				timeString = hourString + "00";
			}
			return timeString;
		} else if (time.matches(REGEX_12_HOUR_TIME)) {
			String period = time.substring(time.length() - 2).toLowerCase();
			timeString = time.substring(0, time.length() - 2).trim();
			timeString = timeString.replace(".","");
			timeString = timeString.replace(":","");
			String minuteString = timeString.substring(timeString.length() - 2);
			String hourString = timeString.substring(0, timeString.length() - 2);
			int hourInt = Integer.parseInt(hourString);
			if (period.equals("pm") && hourInt != 12) {
				hourInt += 12;
			}
			if (period.equals("am") && hourInt == 12) {
				return "00" + minuteString;
			}
			hourString = Integer.toString(hourInt);
			if (hourString.length() == 1) {
				timeString = "0" + hourString + minuteString;
			} else {
				timeString = hourString + minuteString;
			}
			if (Integer.parseInt(timeString) < 2400) {
				return timeString;
			}
		} else if (time.matches(REGEX_24_HOUR_SIMPLE_TIME)){
			timeString = time;
			if (time.length() == 1) {
				timeString = "0" + time + "00";
			} else if (time.length() == 2) {
				timeString = time + "00";
			}
			return timeString;
		} else if (time.matches(REGEX_24_HOUR_TIME)) {
			timeString = time;
			timeString = timeString.replace(".","");
			timeString = timeString.replace(":","");
			if (timeString.length() == 3) {
				timeString = "0" + timeString;
			}
			if (Integer.parseInt(timeString) < 2400) {
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
		int index = getIndex(arguments);
		
		if (arguments.size() > 1 && index <= 0) {
			String errorMsg = "Invalid index input";
			return initInvalidCommand(errorMsg);
		}
		
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
	
	
    // ================================================================
    // Create edit command methods
    // ================================================================
	
	private Command initEditCommand(ArrayList<String> arguments) {
		
		if (arguments.isEmpty()) {
			String errorMsg = "Please specify something to edit";
			return initInvalidCommand(errorMsg);
		}
		
		
		int index = Integer.parseInt(arguments.get(0));
		if (index < 1) {
			String errorMsg = "Invalid Index";
			return initInvalidCommand(errorMsg);
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
			return initInvalidCommand(errorMsg);
		}
		if (!(deadlineIndex < 0)) {
			if (!(startDateIndex < 0) || !(startTimeIndex < 0) || !(endDateIndex < 0) || !(endTimeIndex < 0)) {
				String errorMsg = "Invalid edit format";
				return initInvalidCommand(errorMsg);
			}
		}
		
		ArrayList<Integer> indexArrayList = getIndexArrayList(nameIndex, deadlineIndex, startDateIndex, 
				                                              startTimeIndex, endDateIndex, endTimeIndex);
		
		if (indexArrayList.isEmpty()) {
			String errorMsg = "Invalid edit command";
			return initInvalidCommand(errorMsg);
		}
		
		Hashtable<Integer, String> indexKeywordHash = getHashtable(nameIndex, deadlineIndex, startDateIndex, 
				                                                   startTimeIndex, endDateIndex, endTimeIndex);
		ArrayList<String> editList = getEditList(nameIndex, deadlineIndex, startDateIndex, 
				                                 startTimeIndex, endDateIndex, endTimeIndex);
		
		if (startDateIndex >= 0 && endDateIndex >= 0) {
			Date startDate = getDate(arguments.get(startDateIndex + 1));
			Date endDate = getDate(arguments.get(endDateIndex + 1));
			if (startDate.compareTo(endDate) == 1) {
				String errorMsg = "Start Date cannot be later than End Date";
				return initInvalidCommand(errorMsg);
			}
			if (startDate.compareTo(endDate) == 0 && startTimeIndex >= 0 && endTimeIndex >= 0) {
				String startTime = arguments.get(startTimeIndex + 1);
				String endTime = arguments.get(endTimeIndex + 1);
				if (!areValidTimes(startTime, endTime)) {
					String errorMsg = "Start Time cannot be later than or equal to End Time for single day Event";
					return initInvalidCommand(errorMsg);
				}
			}
		}
		
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
				return -2;
			}
			return keywordIndex;
		}
		return -1;
	}
	
	private String repeatedKeywords(int nameIndex, int deadlineIndex, int startDateIndex, 
			                        int startTimeIndex, int endDateIndex, int endTimeIndex) {
		if (nameIndex == -2) {
			return KEYWORD_EDIT_NAME;
		}
		if (deadlineIndex == -2) {
			return KEYWORD_EDIT_DEADLINE;
		}
		if (startDateIndex == -2) {
			return KEYWORD_EDIT_START_DATE;
		}
		if (startTimeIndex == -2) {
			return KEYWORD_EDIT_START_TIME;
		}
		if (endDateIndex == -2) {
			return KEYWORD_EDIT_END_DATE;
		}
		if (endTimeIndex == -2) {
			return KEYWORD_EDIT_END_TIME;
		}
		return null;
	}
	
	private ArrayList<Integer> getIndexArrayList(int nameIndex, int deadlineIndex, int startDateIndex, 
			                                     int startTimeIndex, int endDateIndex, int endTimeIndex) {
		ArrayList<Integer> indexArrayList = new ArrayList<Integer>();
		if (nameIndex != -1) {
			indexArrayList.add(nameIndex);
		}
		if (deadlineIndex != -1) {
			indexArrayList.add(deadlineIndex);
		}
		if (startDateIndex != -1) {
			indexArrayList.add(startDateIndex);
		}
		if (startTimeIndex != -1) {
			indexArrayList.add(startTimeIndex);
		}
		if (endDateIndex != -1) {
			indexArrayList.add(endDateIndex);
		}
		if (endTimeIndex != -1) {
			indexArrayList.add(endTimeIndex);
		}
		Collections.sort(indexArrayList);
		return indexArrayList;
	}
	
	private Hashtable<Integer, String> getHashtable(int nameIndex, int deadlineIndex, int startDateIndex, 
			                                        int startTimeIndex, int endDateIndex, int endTimeIndex) {
		Hashtable<Integer, String> indexKeywordHash = new Hashtable<Integer, String>();
		if (nameIndex != -1) {
			indexKeywordHash.put(nameIndex, KEYWORD_EDIT_NAME);
		}
		if (deadlineIndex != -1) {
			indexKeywordHash.put(deadlineIndex, KEYWORD_EDIT_DEADLINE);
		}
		if (startDateIndex != -1) {
			indexKeywordHash.put(startDateIndex, KEYWORD_EDIT_START_DATE);
		}
		if (startTimeIndex != -1) {
			indexKeywordHash.put(startTimeIndex, KEYWORD_EDIT_START_TIME);
		}
		if (endDateIndex != -1) {
			indexKeywordHash.put(endDateIndex, KEYWORD_EDIT_END_DATE);
		}
		if (endTimeIndex != -1) {
			indexKeywordHash.put(endTimeIndex, KEYWORD_EDIT_END_TIME);
		}
		return indexKeywordHash;
	}
	
	private ArrayList<String> getEditList(int nameIndex, int deadlineIndex, int startDateIndex, 
			                              int startTimeIndex, int endDateIndex, int endTimeIndex) {
		ArrayList<String> editList = new ArrayList<String>();
		if (nameIndex != -1) {
			editList.add(KEYWORD_EDIT_NAME);
		}
		if (deadlineIndex != -1) {
			editList.add(KEYWORD_EDIT_DEADLINE);
		}
		if (startDateIndex != -1) {
			editList.add(KEYWORD_EDIT_START_DATE);
		}
		if (startTimeIndex != -1) {
			editList.add(KEYWORD_EDIT_START_TIME);
		}
		if (endDateIndex != -1) {
			editList.add(KEYWORD_EDIT_END_DATE);
		}
		if (endTimeIndex != -1) {
			editList.add(KEYWORD_EDIT_END_TIME);
		}
		return editList;
	}
	
	private Command editName (Command command, String argument) {
		if (argument == null) {
			String errorMsg = "Invalid name";
			return initInvalidCommand(errorMsg);
		}
		command.setName(argument);
		return command;
	}
	
	private Command editDeadline(Command command, String argument) {
		Date deadline = getDate(argument);
		
		if (deadline == null) {
			String errorMsg = "Invalid deadline date";
			return initInvalidCommand(errorMsg);
		}
		command.setDueDate(deadline);
		return command;
	}
	
	private Command editStartDate(Command command, String argument) {
		Date startDate = getDate(argument);
		
		if (startDate == null) {
			String errorMsg = "Invalid start date";
			return initInvalidCommand(errorMsg);
		}
		command.setStartDate(startDate);
		return command;
	}
	
	private Command editStartTime(Command command, String argument) {
		String startTime = getTime(argument);
		
		if (startTime == null) {
			String errorMsg = "Invalid start time";
			return initInvalidCommand(errorMsg);
		}
		command.setStartTime(startTime);
		return command;
	}
	
	private Command editEndDate(Command command, String argument) {
		Date endDate = getDate(argument);
		
		if (endDate == null) {
			String errorMsg = "Invalid end date";
			return initInvalidCommand(errorMsg);
		}
		command.setEndDate(endDate);
		return command;
	}
	
	private Command editEndTime(Command command, String argument) {
		String endTime = getTime(argument);
		
		if (endTime == null) {
			String errorMsg = "Invalid end time";
			return initInvalidCommand(errorMsg);
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
		
		if (arguments.size() > 1 && index <= 0) {
			String errorMsg = "Invalid input of index";
			return initInvalidCommand(errorMsg);
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
			String errorMsg = "Please specify ";
			return initInvalidCommand(errorMsg);
		}
		
		Command command = new Command(Command.CommandType.SET);
		List<String> directoryList = arguments.subList(0, arguments.size());
		String directory = getName(directoryList);
		command.setName(directory);
		return command;
	}
	
	public void setAliasHash(Hashtable<String, String> commandAliases) {
		
		this.commandAliases = commandAliases;
	}
	
	
    // ================================================================
    // Create save command method
    // ================================================================
	
	private Command initSaveCommand(ArrayList<String> arguments) {
		if (arguments.isEmpty()) {
			String errorMsg = "Directory cannot be empty!";
			return initInvalidCommand(errorMsg);
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
			String errorMsg = "This command does not expect arguments!";
			return initInvalidCommand(errorMsg);
		}
		Command command = new Command(Command.CommandType.UNDO);
		return command;
	}
	
	
    // ================================================================
    // Create redo command method
    // ================================================================
	
	private Command initRedoCommand(ArrayList<String> arguments) {
		if (!arguments.isEmpty()) {
			String errorMsg = "This command does not expect arguments!";
			return initInvalidCommand(errorMsg);
		}
		Command command = new Command(Command.CommandType.REDO);
		return command;
	}
	
	
    // ================================================================
    // Create view all command method
    // ================================================================
	
	private Command initViewAllCommand(ArrayList<String> arguments) {
		if (!arguments.isEmpty()) {
			String errorMsg = "This command does not expect arguments!";
			return initInvalidCommand(errorMsg);
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
			String errorMsg = "This command does not expect arguments!";
			return initInvalidCommand(errorMsg);
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
			String errorMsg = "This command does not expect arguments!";
			return initInvalidCommand(errorMsg);
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
			String errorMsg = "This command does not expect arguments!";
			return initInvalidCommand(errorMsg);
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
			String errorMsg = "This command does not expect arguments!";
			return initInvalidCommand(errorMsg);
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
			String errorMsg = "This command does not expect arguments!";
			return initInvalidCommand(errorMsg);
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
			String errorMsg = "This command does not expect arguments!";
			return initInvalidCommand(errorMsg);
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
			String errorMsg = "This command does not expect arguments!";
			return initInvalidCommand(errorMsg);
		}
		return new Command(Command.CommandType.EXIT);
	}
	
	
    // ================================================================
    // Create invalid command methods
    // ================================================================
	
	private Command initInvalidCommand() {
		return new Command(Command.CommandType.INVALID);
	}
	
	private Command initInvalidCommand(String errorMsg) {
		Command command = new Command(Command.CommandType.INVALID);
		command.setName(errorMsg);
		return command;
	}
    
}
