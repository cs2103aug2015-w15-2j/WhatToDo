package backend;

import struct.CommandStub;
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
		Command command;
		String dataType = arguments.get(0);
		
		switch (dataType) {
		case USER_COMMAND_ADD_TASK :
			command = addTask(arguments);
		case USER_COMMAND_ADD_FLOATING_TASK :
			command = addFloatingTask(arguments);
		case USER_COMMAND_ADD_EVENT :
			command = addEvent(arguments);
		default :
			command = addDefaultTask(arguments);
		}
		return command;
	}
	
	private Command addTask(ArrayList<String> arguments) {
		Command command = new Command(Command.CommandType.ADD);
		command.setDataType(Command.DataType.TASK);
		arguments.remove(0);
		String name = getName(arguments);
		command.setName(name);
		return command;
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
		return initInvalidCommand();
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
		return initInvalidCommand();
	}
	
	
    // ================================================================
    // Create search command methods
    // ================================================================
	
	private Command initSearchCommand(ArrayList<String> arguments) {
		return initInvalidCommand();
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
