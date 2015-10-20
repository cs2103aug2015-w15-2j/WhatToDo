package backend;

import struct.CommandStub;
import struct.Date;
import struct.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandParser {
	
    private static final int POSITION_PARAM_COMMAND = 0;
    private static final int POSITION_FIRST_PARAM_ARGUMENT = 1;
	
	private static final String REGEX_WHITESPACES = "[\\s,]+";
	private static final String REGEX_POSITIVE_INTEGER = "^\\d+$";
	private static final String REGEX_12_HOUR_SIMPLE_TIME = "(1[012]|[1-9]|0[1-9])(\\s)?(?i)(am|pm)";
	private static final String REGEX_12_HOUR_TIME = "(1[012]|[1-9]|0[1-9])(:|.)[0-5][0-9](\\s)?(?i)(am|pm)";
	private static final String REGEX_24_HOUR_SIMPLE_TIME = "([01]?[0-9]|2[0-3])";
	private static final String REGEX_24_HOUR_TIME = "([01]?[0-9]|2[0-3])(:|.)?[0-5][0-9]";
	
    private static final String STRING_ONE_SPACE = " ";
    private static final String ESCAPE_CHARACTER = "\\";
	
    private static final String USER_COMMAND_ADD = "add";
    private static final String USER_COMMAND_DELETE = "delete";
    private static final String USER_COMMAND_EDIT = "edit";
    private static final String USER_COMMAND_SEARCH = "search";
    private static final String USER_COMMAND_EXIT = "exit";
    private static final String USER_COMMAND_UNDO = "undo";
    private static final String USER_COMMAND_REDO = "redo";
    
    private static final String[] KEYWORDS = {"by", "to", "from", "on"};
    private static final String KEYWORD_DEADLINE = KEYWORDS[0];
    private static final String KEYWORD_EVENT_1 = KEYWORDS[1];
    private static final String KEYWORD_EVENT_2 = KEYWORDS[2];
    private static final String KEYWORD_EVENT_3 = KEYWORDS[3];
    
    private static final ArrayList<String> nameOfDays = new ArrayList<String>();
    private static final ArrayList<String> fullNameOfDays = new ArrayList<String>();
    
	public CommandParser() {
    }

    public Command parse(String userInput) {
        Command command;
        ArrayList<String> parameters = splitString(userInput);
        String userCommand = getUserCommand(parameters);
        ArrayList<String> arguments = getUserArguments(parameters);
        initNameOfDays();
        initFullNameOfDays();

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
            	
            case USER_COMMAND_UNDO :
            	command = initUndoCommand();
            	break;
            	
            case USER_COMMAND_REDO :
            	command = initRedoCommand();
            	break; 
                
            case USER_COMMAND_EXIT :
                command = initExitCommand();
                break;

            default :
                command = initInvalidCommand();
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
		int size = arguments.size();
		if (size == 0) {
			String errorMsg = "Please input the task name!";
			return initInvalidCommand(errorMsg);
		} else if (arguments.contains(KEYWORD_DEADLINE)) {
			command = addTask(arguments);
		} else if (arguments.contains(KEYWORD_EVENT_1) && arguments.contains(KEYWORD_EVENT_2)) {
			if (arguments.contains(KEYWORD_EVENT_3)) {
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
		int index = keywordIndex + 1;
		String dateString = "";
		while (index < arguments.size()) {
			dateString += arguments.get(index);
			index++;
		}
		Date date = getDate(dateString);
		List<String> nameList = arguments.subList(0, keywordIndex);
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
	
	private Command addEvent(ArrayList<String> arguments) {
		
		int keywordToIndex = arguments.indexOf(KEYWORD_EVENT_1);
		int keywordFromIndex = arguments.indexOf(KEYWORD_EVENT_2);
		
		if (Math.abs(keywordToIndex - keywordFromIndex) != 3 || (arguments.size() - keywordToIndex != 3 && arguments.size() - keywordFromIndex != 3)) {
			String errorMsg = "Invalid format for adding Event";
			return initInvalidCommand(errorMsg);
		}
		
		Date startDate = getDate(arguments.get(keywordFromIndex + 1));
		String startTime = getTime(arguments.get(keywordFromIndex + 2));
		Date endDate = getDate(arguments.get(keywordToIndex + 1));
		String endTime = getTime(arguments.get(keywordToIndex + 2));
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
		if (startDate.compareTo(endDate) != -1) {
			String errorMsg = "Start Date cannot be later than End Date";
			return initInvalidCommand(errorMsg);
		}
		if (keywordToIndex > keywordFromIndex) {
			List<String> nameList = arguments.subList(0, keywordFromIndex);
			name = getName(nameList);
		} else {
			List<String> nameList = arguments.subList(0, keywordToIndex);
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
		
		int keywordToIndex = arguments.indexOf(KEYWORD_EVENT_1);
		int keywordFromIndex = arguments.indexOf(KEYWORD_EVENT_2);
		int keywordOnIndex = arguments.indexOf(KEYWORD_EVENT_3);
		return initInvalidCommand();
	}
	
	private Command addFloatingTask(ArrayList<String> arguments) {
		Command command = new Command(Command.CommandType.ADD);
		command.setDataType(Command.DataType.FLOATING_TASK);
		List<String> nameList = arguments.subList(0, arguments.size());
		String name = getName(nameList);
		command.setName(name);
		return command;
	}
	
	private String getName(List<String> arguments) {
		String name = "";
		if (arguments.size() == 0) {
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
		if (date.equals("today")){
			return todayDate;
		} else if (date.equals("tomorrow")) {
			return Date.tomorrowDate();
		} else if (fullNameOfDays.contains(date.toLowerCase())) {
			int day = nameOfDays.indexOf(date.substring(0,3).toLowerCase());
			int today = nameOfDays.indexOf(todayDate.getDayString().substring(0,3).toLowerCase());
			int difference = day - today;
			if (difference >= 0) {
				return todayDate.plusDay(difference);
			} else {
				return todayDate.plusDay(difference + 7);
			}
		} else if (date.substring(0,1).equals("n") && fullNameOfDays.contains(date.substring(1).toLowerCase())) {
			int day = nameOfDays.indexOf(date.substring(1,4).toLowerCase());
			int today = nameOfDays.indexOf(todayDate.getDayString().substring(0,3).toLowerCase());
			int difference = day - today;
			if (difference >= 0) {
				return todayDate.plusDay(difference + 7);
			} else {
				return todayDate.plusDay(difference + 14);
			}
		} else if (date.matches(REGEX_POSITIVE_INTEGER) && String.valueOf(date).length() == 4) {
			String year = todayDate.getFullDate().substring(4);
			date = date + year;
			Date currDate = new Date(date);
			if (isValidDate(date) && todayDate.compareTo(currDate) <= 0) {
				return currDate;
			}
		} else if (date.matches(REGEX_POSITIVE_INTEGER) && String.valueOf(date).length() == 6 && isValidDate(date)) {
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
	
	private void initNameOfDays() {
		nameOfDays.add("mon");
		nameOfDays.add("tue");
		nameOfDays.add("wed");
		nameOfDays.add("thu");
		nameOfDays.add("fri");
		nameOfDays.add("sat");
		nameOfDays.add("sun");
	}
	
	private void initFullNameOfDays() {
		fullNameOfDays.add("mon");
		fullNameOfDays.add("tue");
		fullNameOfDays.add("wed");
		fullNameOfDays.add("thu");
		fullNameOfDays.add("fri");
		fullNameOfDays.add("sat");
		fullNameOfDays.add("sun");
		fullNameOfDays.add("monday");
		fullNameOfDays.add("tuesday");
		fullNameOfDays.add("wednesday");
		fullNameOfDays.add("thursday");
		fullNameOfDays.add("friday");
		fullNameOfDays.add("saturday");
		fullNameOfDays.add("sunday");
	}
	
	private String getTime(String time) {
		if (time.matches(REGEX_12_HOUR_SIMPLE_TIME)) {
			String period = time.substring(time.length() - 2).toLowerCase();
			String hourString = time.substring(0, time.length() - 2).trim();
			int hourInt = Integer.parseInt(hourString);
			if (period.equals("pm") && hourInt != 12) {
				hourInt += 12;
			}
			hourString = Integer.toString(hourInt);
			if (hourString.length() == 1) {
				hourString = "0" + hourString + "00";
			} else {
				hourString += "00";
			}
			return hourString;
		} else if (time.matches(REGEX_12_HOUR_TIME)) {
			String period = time.substring(time.length() - 2).toLowerCase();
			String timeString = time.substring(0, time.length() - 2).trim();
			timeString = timeString.replace(".","");
			timeString = timeString.replace(":","");
			String minuteString = timeString.substring(timeString.length() - 2);
			String hourString = timeString.substring(0, timeString.length() - 2);
			int hourInt = Integer.parseInt(hourString);
			if (period.equals("pm") && hourInt != 12) {
				hourInt += 12;
			}
			hourString = Integer.toString(hourInt);
			if (hourString.length() == 1) {
				timeString = "0" + hourString + minuteString;
			} else {
				timeString = hourString + minuteString;
			}
			return timeString;
		} else if (time.matches(REGEX_24_HOUR_SIMPLE_TIME)){
			if (time.length() == 1) {
				time = "0" + time + "00";
			} else if (time.length() == 2) {
				time = time + "00";
			}
			return time;
		} else if (time.matches(REGEX_24_HOUR_TIME)) {
			time = time.replace(".","");
			time = time.replace(":","");
			if (time.length() == 3) {
				time = "0" + time;
			}
			return time;
		} else {
			return null;
		}
	}
	
	private boolean areValidTimes(String startTime, String endTime) {
		int intStartTime = Integer.parseInt(startTime);
		int intEndTime = Integer.parseInt(endTime);
		if (intEndTime >= intStartTime) {
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
		
		if (index > 0) {
			Command command = new Command(Command.CommandType.DELETE);
			command.setIndex(index);
			return command;
		} else {
			return initInvalidCommand();
		}
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
		int index = Integer.parseInt(arguments.get(0));
		if (index > 0) {
			arguments.remove(0);
			String name = getName(arguments);
			Command command = new Command(Command.CommandType.EDIT);
			command.setIndex(index);
			command.setName(name);
			return command;
		} else {
			return initInvalidCommand();
		}
		
		
	}
	
	
    // ================================================================
    // Create search command methods
    // ================================================================
	
	private Command initSearchCommand(ArrayList<String> arguments) {
		return initInvalidCommand();
	}
	
	
    // ================================================================
    // Create undo command methods
    // ================================================================
	
	private Command initUndoCommand() {
		Command command = new Command(Command.CommandType.UNDO);
		return command;
	}
	
	
    // ================================================================
    // Create redo command methods
    // ================================================================
	
	private Command initRedoCommand() {
		Command command = new Command(Command.CommandType.REDO);
		return command;
	}
	
	
    // ================================================================
    // Create exit command methods
    // ================================================================
	
	private Command initExitCommand() {
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
	
	
	
	
	
	
	
	
    public CommandStub getCommandType(String inputText) {

        // Function stub, only has add and search commands
        String command = inputText.split(" ")[0];
        if (command.toLowerCase().equals("add")) {
            return CommandStub.ADD;
        } else if (command.toLowerCase().equals("search")) {
            return CommandStub.SEARCH;
        } else {
            // Dummy condition
            return CommandStub.ADD;
        }
    }

    public boolean isSwapCommand(String viewState, String userInput) {
        if (viewState.equals("scroll")) {
            // Returns true if the command entered is "search" and the current state
            // of the program is in scroll view
            return (userInput.split(" ")[0].toLowerCase().equals("search"));
        } else if (viewState.equals("split")) {
            // Returns true if the command entered is "add" and the current state
            // of the program is in split view
            return (userInput.split(" ")[0].toLowerCase().equals("add"));
        } else {
            // Dummy condition, will not enter
            return false;
        }
    }
    
}
