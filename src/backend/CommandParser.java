/**
 * This class parses user input string.
 * 
 * @@author A0124099B
 */

package backend;

import backend.AddParser;
import backend.DeleteParser;
import backend.EditParser;
import backend.SetParser;
import backend.NameParser;

import struct.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class CommandParser {

	private static final int POSITION_PARAM_COMMAND = 0;
	private static final int POSITION_FIRST_PARAM_ARGUMENT = 1;
	protected static final int POSITION_FIRST_INDEX = 0;

	private static final String REGEX_WHITESPACES = "[\\s;]+";
	protected static final String REGEX_POSITIVE_INTEGER = "^0*[1-9][0-9]*";
	
	protected static final String STRING_VERIFIED = "verified";

	protected static final String USER_COMMAND_ADD = "add";
	protected static final String USER_COMMAND_DELETE = "delete";
	protected static final String USER_COMMAND_EDIT = "edit";
	protected static final String USER_COMMAND_SEARCH = "search";
	protected static final String USER_COMMAND_DONE = "done";
	protected static final String USER_COMMAND_SET = "set";
	protected static final String USER_COMMAND_SAVE = "save";
	protected static final String USER_COMMAND_UNDO = "undo";
	protected static final String USER_COMMAND_REDO = "redo";
	protected static final String USER_COMMAND_VIEW_ALL = "all";
	protected static final String USER_COMMAND_VIEW_DEF = "def";
	protected static final String USER_COMMAND_VIEW_HIST = "hist";
	protected static final String USER_COMMAND_VIEW_UNRES = "unres";
	protected static final String USER_COMMAND_VIEW_HELP = "help";
	protected static final String USER_COMMAND_VIEW_OPEN_FILE = "openfile";
	protected static final String USER_COMMAND_VIEW_CONFIG = "config";
	protected static final String USER_COMMAND_EXIT = "exit";

	private static final String MESSAGE_ERROR_USER_COMMAND = "Invalid user command.";
	private static final String MESSAGE_ERROR_INDEX = "Invalid index.";
	private static final String MESSAGE_ERROR_SAVE = "Directory required.";
	private static final String MESSAGE_ERROR_NO_ARGUMENTS = "This command does not expect arguments.";

	private static final AddParser addParser = new AddParser();
	private static final EditParser editParser = new EditParser();
	private static final SetParser setParser = new SetParser();
	private static final NameParser nameParser = new NameParser();
	
	// Hashtable of aliases as keys and commands as values
	private Hashtable<String, String> commandAliases = new Hashtable<String, String>();

	// ================================================================
	// Constructor
	// ================================================================
	
	public CommandParser(Hashtable<String, String> commandAliases) {
		assert(commandAliases != null);
		this.commandAliases = commandAliases;
	}
	
	// ================================================================
	// Public methods
	// ================================================================

	public Command parse(String userInput) {
		Command command;
		ArrayList<String> originalParameters = splitString(userInput);
		// originalArguments are given to add, edit, search and save commands to preserve
		// the original name, keywords or directory given in userInput
		ArrayList<String> originalArguments = getUserArguments(originalParameters);
		ArrayList<String> parameters = splitString(userInput.toLowerCase());
		// convert parameters to account for aliases
		ArrayList<String> convertedParameters = convertParameters(parameters);
		ArrayList<String> arguments = getUserArguments(convertedParameters);
		String userCommand = getUserCommand(convertedParameters);

		switch (userCommand) {

            case USER_COMMAND_ADD :
                command = initAddCommand(originalArguments);
                break;

            case USER_COMMAND_DELETE :
                command = initDeleteCommand(arguments);
                break;

            case USER_COMMAND_EDIT :
                command = initEditCommand(originalArguments);
                break;
                
            case USER_COMMAND_SEARCH :
            	command = initSearchCommand(originalArguments);
            	break;
            	
            case USER_COMMAND_DONE :
            	command = initDoneCommand(arguments);
            	break;
            	
            case USER_COMMAND_SET :
            	command = initSetCommand(arguments);
            	break;
            	
            case USER_COMMAND_SAVE :
            	command = initSaveCommand(originalArguments);
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
                command = initInvalidCommand(MESSAGE_ERROR_USER_COMMAND);
        }
		command.setUserInput(userInput);
		return command;
	}
	
	public void deleteAlias(String command) {
		commandAliases.remove(command);
	}
	
	public void setAlias(String newCommand, String originalCommand) {
		commandAliases.put(newCommand, originalCommand);
	}
	
	// ================================================================
	// Private methods
	// ================================================================

	private ArrayList<String> splitString(String arguments) {
		String[] strArray = arguments.trim().split(REGEX_WHITESPACES);
		return new ArrayList<String>(Arrays.asList(strArray));
	}

	/**
	 * This method first attempts to convert the first string if it is a
	 * registered alias. It then checks if this first string is 
	 * USER_COMMAND_SET. If it is, it converts the rest of the userInput
	 * if any of them is also a registered alias. This is to allow for 
	 * initSetCommand() to know which command the user is referring to.
	 * 
	 * @param parameters
	 * @return convertedParameters
	 */
	private ArrayList<String> convertParameters(ArrayList<String> parameters) {
		if (commandAliases.containsKey(parameters.get(POSITION_FIRST_INDEX))) {
			String parameter = parameters.get(POSITION_FIRST_INDEX);
			String newParameter = commandAliases.get(parameter);
			parameters.remove(POSITION_FIRST_INDEX);
			parameters.add(POSITION_FIRST_INDEX, newParameter);
		}
		if (parameters.get(POSITION_FIRST_INDEX).equals(USER_COMMAND_SET)) {
			for (int i = 1; i < parameters.size(); i++) {
				String currParameter = parameters.get(i);
				if (commandAliases.containsKey(currParameter)) {
					String newParameter = commandAliases.get(currParameter);
					parameters.remove(i);
					parameters.add(i, newParameter);
				}
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
	
	/**
	 * This method converts all the strings in the ArrayList to lower case
	 * @param arguments
	 * @return arguments with its members all in lower case
	 */
	protected static ArrayList<String> toLowerCase(ArrayList<String> arguments) {
		ArrayList<String> argumentsLowerCase = new ArrayList<String>();
		for (int i = 0; i < arguments.size(); i++) {
			String parameter = arguments.get(i);
			String newParameter = parameter.toLowerCase();
			argumentsLowerCase.add(newParameter);
		}
		return argumentsLowerCase;
	}

	// ================================================================
	// Create add command method
	// ================================================================

	private Command initAddCommand(ArrayList<String> arguments) {
		return addParser.parse(arguments);
	}

	// ================================================================
	// Create delete command methods
	// ================================================================

	private Command initDeleteCommand(ArrayList<String> arguments) {
		// deleteParser is created with every delete command in order
		// to pass in the most updated commandAliases Hashtable
		DeleteParser deleteParser = new DeleteParser(commandAliases);
		return deleteParser.parse(arguments);
	}

	// ================================================================
	// Create edit command method
	// ================================================================

	private Command initEditCommand(ArrayList<String> arguments) {
		return editParser.parse(arguments);
	}

	// ================================================================
	// Create search command method
	// ================================================================

	private Command initSearchCommand(ArrayList<String> arguments) {
		// If userInput is simply "search", the arguments here will be empty
		// and it will be recgonised as a command to switch to search view
		if (arguments.isEmpty()) {
			Command command = new Command(Command.CommandType.VIEW);
			command.setViewType(Command.ViewType.SEARCH);
			return command;
		}
		String searchWords = nameParser.getName(arguments, POSITION_FIRST_INDEX, arguments.size());
		Command command = new Command(Command.CommandType.SEARCH);
		command.setName(searchWords);
		return command;
	}

	// ================================================================
	// Create done command method
	// ================================================================

	private Command initDoneCommand(ArrayList<String> arguments) {
		// If userInput is simply "done", the arguments here will be empty
		// and it will be recgonised as a command to switch to search view
		if (arguments.isEmpty()) {
			Command command = new Command(Command.CommandType.VIEW);
			command.setViewType(Command.ViewType.DONE);
			return command;
		}
		String index = arguments.get(POSITION_FIRST_INDEX);

		if (arguments.size() > 1 || !index.matches(REGEX_POSITIVE_INTEGER)) {
			return initInvalidCommand(MESSAGE_ERROR_INDEX);
		}

		Command command = new Command(Command.CommandType.DONE);
		command.setIndex(Integer.parseInt(index));
		return command;
	}

	// ================================================================
	// Create set command methods
	// ================================================================

	private Command initSetCommand(ArrayList<String> arguments) {
		return setParser.parse(arguments);
	}

	// ================================================================
	// Create save command method
	// ================================================================

	private Command initSaveCommand(ArrayList<String> arguments) {
		if (arguments.isEmpty()) {
			return initInvalidCommand(MESSAGE_ERROR_SAVE);
		}
		Command command = new Command(Command.CommandType.SAVE);
		String directory = nameParser.getName(arguments, POSITION_FIRST_INDEX, arguments.size());
		command.setName(directory);
		return command;
	}

	// ================================================================
	// Create undo command method
	// ================================================================

	private Command initUndoCommand(ArrayList<String> arguments) {
		if (!arguments.isEmpty()) {
			return initInvalidCommand(MESSAGE_ERROR_NO_ARGUMENTS);
		}
		Command command = new Command(Command.CommandType.UNDO);
		return command;
	}

	// ================================================================
	// Create redo command method
	// ================================================================

	private Command initRedoCommand(ArrayList<String> arguments) {
		if (!arguments.isEmpty()) {
			return initInvalidCommand(MESSAGE_ERROR_NO_ARGUMENTS);
		}
		Command command = new Command(Command.CommandType.REDO);
		return command;
	}

	// ================================================================
	// Create view all command method
	// ================================================================

	private Command initViewAllCommand(ArrayList<String> arguments) {
		if (!arguments.isEmpty()) {
			return initInvalidCommand(MESSAGE_ERROR_NO_ARGUMENTS);
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
			return initInvalidCommand(MESSAGE_ERROR_NO_ARGUMENTS);
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
			return initInvalidCommand(MESSAGE_ERROR_NO_ARGUMENTS);
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
			return initInvalidCommand(MESSAGE_ERROR_NO_ARGUMENTS);
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
			return initInvalidCommand(MESSAGE_ERROR_NO_ARGUMENTS);
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
			return initInvalidCommand(MESSAGE_ERROR_NO_ARGUMENTS);
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
			return initInvalidCommand(MESSAGE_ERROR_NO_ARGUMENTS);
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
			return initInvalidCommand(MESSAGE_ERROR_NO_ARGUMENTS);
		}
		return new Command(Command.CommandType.EXIT);
	}

	// ================================================================
	// Create invalid command method
	// ================================================================

	protected static Command initInvalidCommand(String errorMsg) {
		Command command = new Command(Command.CommandType.INVALID);
		command.setName(errorMsg);
		return command;
	}

}
