package backend;

import backend.EditParser;
import backend.SetParser;
import backend.DeleteParser;
import backend.NameParser;
import backend.AddParser;

import struct.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;


//@@author A0124099B
public class CommandParser {

	private static final int POSITION_PARAM_COMMAND = 0;
	private static final int POSITION_FIRST_PARAM_ARGUMENT = 1;
	private static final int POSITION_FIRST_INDEX = 0;

	private static final String REGEX_WHITESPACES = "[\\s;]+";
	protected static final String REGEX_POSITIVE_INTEGER = "^0*[1-9][0-9]*";

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

	private static final String ERROR_USER_COMMAND = "Invalid user command.";
	private static final String ERROR_INDEX = "Invalid index.";
	private static final String ERROR_SAVE = "Directory required.";
	private static final String ERROR_NO_ARGUMENTS = "This command does not expect arguments.";

	private Hashtable<String, String> commandAliases = new Hashtable<String, String>();

	private static final EditParser editParser = new EditParser();
	private static final SetParser setParser = new SetParser();
	private static final NameParser nameParser = new NameParser();
	private static final AddParser addParser = new AddParser();

	public CommandParser(Hashtable<String, String> commandAliases) {
		this.commandAliases = commandAliases;
	}

	public Command parse(String userInput) {
		Command command;
		ArrayList<String> originalParameters = splitString(userInput);
		ArrayList<String> originalArguments = getUserArguments(originalParameters);
		ArrayList<String> parameters = splitString(userInput.toLowerCase());
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
		if (commandAliases.containsKey(parameters.get(0))) {
			String parameter = parameters.get(0);
			String newParameter = commandAliases.get(parameter);
			parameters.remove(0);
			parameters.add(0, newParameter);
		}
		if (parameters.get(0).equals(USER_COMMAND_SET)) {
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
	// Create add command methods
	// ================================================================

	private Command initAddCommand(ArrayList<String> arguments) {
		return addParser.parse(arguments);
	}

	// ================================================================
	// Create delete command methods
	// ================================================================

	private Command initDeleteCommand(ArrayList<String> arguments) {
		DeleteParser deleteParser = new DeleteParser(commandAliases);
		return deleteParser.parse(arguments);
	}

	public void deleteAliasFromHash(String command) {
		commandAliases.remove(command);
	}

	// ================================================================
	// Create edit command methods
	// ================================================================

	private Command initEditCommand(ArrayList<String> arguments) {
		return editParser.parse(arguments);
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
		String searchWords = nameParser.getName(arguments, POSITION_FIRST_INDEX, arguments.size());
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
		String index = arguments.get(0);

		if (arguments.size() > 1 || !index.matches(REGEX_POSITIVE_INTEGER)) {
			return initInvalidCommand(ERROR_INDEX);
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
		String directory = nameParser.getName(arguments, POSITION_FIRST_INDEX, arguments.size());
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

	protected static Command initInvalidCommand(String errorMsg) {
		Command command = new Command(Command.CommandType.INVALID);
		command.setName(errorMsg);
		return command;
	}

}
