package backend;

import struct.CommandStub;
import struct.Date;
import struct.Command;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandParser {
	
    private static final int POSITION_PARAM_COMMAND = 0;
    private static final int POSITION_FIRST_PARAM_ARGUMENT = 1;
	
	private static final String REGEX_WHITESPACES = "[\\s,]+";
	
    private static final String STRING_ONE_SPACE = " ";
	
    private static final String USER_COMMAND_ADD = "add";
    private static final String USER_COMMAND_ADD_TASK = "d";
    private static final String USER_COMMAND_ADD_FLOATING_TASK = "f";
    private static final String USER_COMMAND_ADD_EVENT = "e";
    private static final String USER_COMMAND_DELETE = "delete";
    private static final String USER_COMMAND_EDIT = "edit";
    private static final String USER_COMMAND_SEARCH = "search";
    private static final String USER_COMMAND_EXIT = "exit";
    private static final String USER_COMMAND_UNDO = "undo";
    private static final String USER_COMMAND_REDO = "redo";
    
	
	public CommandParser() {
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
            	
            case USER_COMMAND_UNDO :
            	command = initUndoCommand();
            	
            case USER_COMMAND_REDO :
            	command = initRedoCommand();
                
            case USER_COMMAND_EXIT :
                command = initExitCommand();
                break;

            default :
                command = initInvalidCommand();
        }

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
		String dataType = arguments.get(0);
		
		switch (dataType) {
		case USER_COMMAND_ADD_TASK :
			return addTask(arguments);
		case USER_COMMAND_ADD_FLOATING_TASK :
			return addFloatingTask(arguments);
		case USER_COMMAND_ADD_EVENT :
			return addEvent(arguments);
		default :
			return addDefaultTask(arguments);
		}
	}
	
	private Command addTask(ArrayList<String> arguments) {
		String dateString = arguments.get(arguments.size() - 1);
		Date date = isValidDate(dateString);
		if (date != null) {
			Command command = new Command(Command.CommandType.ADD);
			command.setDataType(Command.DataType.TASK);
			arguments.remove(0);
			arguments.remove(arguments.size() - 1);
			String name = getName(arguments);
			command.setName(name);
			command.setDueDate(date);
			return command;
		} else {
			return initInvalidCommand();
		}
	}
	
	private Command addFloatingTask(ArrayList<String> arguments) {
		Command command = new Command(Command.CommandType.ADD);
		command.setDataType(Command.DataType.FLOATING_TASK);
		arguments.remove(0);
		String name = getName(arguments);
		command.setName(name);
		return command;
	}
	
	private Command addEvent(ArrayList<String> arguments) {
		if (arguments.size() < 5) {
			return initInvalidCommand();
		}
		boolean hasEndDate = true;
		String endTime = arguments.get(arguments.size() - 1);
		Date endDate = isValidDate(arguments.get(arguments.size() - 2));
		String startTime;
		Date startDate;
		if (endDate != null) {
			startTime = arguments.get(arguments.size() - 3);
			startDate = isValidDate(arguments.get(arguments.size() - 4));
		} else {
			hasEndDate = false;
			startTime = arguments.get(arguments.size() - 2);
			startDate = isValidDate(arguments.get(arguments.size() - 3));
		}
		if (isValidTime(endTime) && isValidTime(startTime) && startDate != null) {
			if (hasEndDate) {
				if (startDate.compareTo(endDate) == -1) {
					Command command = new Command(Command.CommandType.ADD);
					command.setDataType(Command.DataType.EVENT);
					arguments.remove(0);
					arguments.remove(arguments.size() - 1);
					arguments.remove(arguments.size() - 1);
					arguments.remove(arguments.size() - 1);
					arguments.remove(arguments.size() - 1);
					String name = getName(arguments);
					command.setName(name);
					command.setEndDate(endDate);
					command.setEndTime(endTime);
					command.setStartDate(startDate);
					command.setStartTime(startTime);
					return command;
				} else {
					return initInvalidCommand();
				}
			} else {
				if (areValidTimes(startTime, endTime)) {
					Command command = new Command(Command.CommandType.ADD);
					command.setDataType(Command.DataType.EVENT);
					arguments.remove(0);
					arguments.remove(arguments.size() - 1);
					arguments.remove(arguments.size() - 1);
					arguments.remove(arguments.size() - 1);
					String name = getName(arguments);
					command.setName(name);
					command.setEndDate(endDate);
					command.setEndTime(endTime);
					command.setStartTime(startTime);
					System.out.println("i got here");
					return command;
				} else {
					return initInvalidCommand();
				}
			}
		} else {
			return initInvalidCommand();
		}
	}
	
	private Command addDefaultTask(ArrayList<String> arguments) {
		Command command = new Command(Command.CommandType.ADD);
		command.setDataType(Command.DataType.FLOATING_TASK);
		String name = getName(arguments);
		command.setName(name);
		return command;
	}
	
	private String getName(ArrayList<String> arguments) {
		String name = "";
		for (int i = 0; i < arguments.size(); i++) {
			String currentArgument = arguments.get(i);
			name += currentArgument + STRING_ONE_SPACE;
		}
		return name.trim();
	}
	
	private Date isValidDate(String date) {
		Date todayDate = Date.todayDate();
		if (String.valueOf(date).length() == 6) {
			Date currDate = new Date(date);
			if (todayDate.compareTo(currDate) <= 0) {
				return currDate;
			}
		} else if (date.equals("today")){
			return todayDate;
		} else if (date.equals("tomorrow")) {
			return Date.tomorrowDate();
		}
		return null;
	}
	
	private boolean isValidTime(String time) {
		if (String.valueOf(time).length() == 4) {
			int intTime = Integer.parseInt(time);
			if (intTime >= 0 && intTime < 2400) {
				return true;
			}
		}
		return false;
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
