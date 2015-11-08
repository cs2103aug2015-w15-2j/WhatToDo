package backend;

import backend.EditParser;
import backend.DateParser;
import backend.TimeParser;
import backend.SetParser;
import backend.DeleteParser;
import backend.NameParser;

import struct.Date;
import struct.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class CommandParser {
	
    private static final int POSITION_PARAM_COMMAND = 0;
    private static final int POSITION_FIRST_PARAM_ARGUMENT = 1;
    private static final int POSITION_FIRST_INDEX = 0;
    private static final int POSITION_PLUS_ONE = 1;
    private static final int POSITION_PLUS_TWO = 2;
    private static final int POSITION_DIFFERENCE_TWO = 2;
    private static final int POSITION_DIFFERENCE_THREE = 3;
    private static final int POSITION_DIFFERENCE_FOUR = 4;
	
    private static final String REGEX_WHITESPACES = "[\\s;]+";
    private static final String REGEX_POSITIVE_INTEGER = "^0*[1-9][0-9]*";
    
    private static final String STRING_VERIFIED = "verified";
	
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
    private static final String KEYWORD_EVENT_TO = "to";
    private static final String KEYWORD_EVENT_FROM = "from";
    private static final String KEYWORD_EVENT_ON = "on";
    private static final String KEYWORD_SET = "as";
    
    private static final String KEYWORD_EDIT_NAME = "name";
    private static final String KEYWORD_EDIT_DEADLINE = "date";
    private static final String KEYWORD_EDIT_START_DATE = "startd";
    private static final String KEYWORD_EDIT_START_TIME = "startt";
    private static final String KEYWORD_EDIT_END_DATE = "endd";
    private static final String KEYWORD_EDIT_END_TIME = "endt";
    
    private static final String ERROR_USER_COMMAND = "Invalid user command.";
    private static final String ERROR_TASK_NAME = "Task name required.";
    private static final String ERROR_DEADLINE = "Invalid deadline.";
    private static final String ERROR_EVENT_FORMAT = "Invalid format for adding event.";
    private static final String ERROR_START_DATE = "Invalid start date.";
    private static final String ERROR_START_TIME = "Invalid start time.";
    private static final String ERROR_END_DATE = "Invalid end date.";
    private static final String ERROR_END_TIME = "Invalid end time.";
    private static final String ERROR_START_END_TIME = "Start time later than or equal to end time for single-day event.";
    private static final String ERROR_START_END_DATE = "Start date later than end date.";    
    private static final String ERROR_EVENT_NAME = "Event name required.";
    private static final String ERROR_EVENT_DATE = "Invalid date.";
    private static final String ERROR_DELETE = "Index/alias command required.";
    private static final String ERROR_DELETE_INDEX_ALIAS = "Invalid index or alias.";
    private static final String ERROR_EDIT = "Index required.";
    private static final String ERROR_INDEX = "Invalid index.";
    private static final String ERROR_DOUBLE_EDIT_KEYWORD = "Keyword %1$s has been entered twice.";
    private static final String ERROR_EDIT_FORMAT = "Invalid edit format.";
    private static final String ERROR_SET = "Command and alias required.";
    private static final String ERROR_SET_FORMAT = "Invalid set format.";
    private static final String ERROR_SAVE = "Directory required.";
    private static final String ERROR_NO_ARGUMENTS = "This command does not expect arguments.";
    
    private Hashtable<String, String> commandAliases = new Hashtable<String, String>();

    private static final EditParser editParser = new EditParser();
    private static final DateParser dateParser = new DateParser();
    private static final TimeParser timeParser = new TimeParser();
    private static final SetParser setParser = new SetParser();
    private static final NameParser nameParser = new NameParser();
    
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
        
        switch (userCommand.toLowerCase()) {

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
	
    
    // ================================================================
    // Create add command methods
    // ================================================================
	
	private Command initAddCommand(ArrayList<String> arguments) {
		Command command;
		ArrayList<String> argumentsLowerCase = toLowerCase(arguments);
		if (argumentsLowerCase.isEmpty()) {
			return initInvalidCommand(ERROR_TASK_NAME);
		} else if (argumentsLowerCase.contains(KEYWORD_DEADLINE)) {
			int keywordByIndex = argumentsLowerCase.indexOf(KEYWORD_DEADLINE);
			command = addTask(arguments, keywordByIndex);
		} else if (argumentsLowerCase.contains(KEYWORD_EVENT_TO) && 
				   argumentsLowerCase.contains(KEYWORD_EVENT_FROM)) {
			int keywordFromIndex = argumentsLowerCase.indexOf(KEYWORD_EVENT_FROM);
			int keywordToIndex = argumentsLowerCase.indexOf(KEYWORD_EVENT_TO);
			if (argumentsLowerCase.contains(KEYWORD_EVENT_ON)) {
				int keywordOnIndex = argumentsLowerCase.indexOf(KEYWORD_EVENT_ON);
				command = addDayEvent(arguments, keywordOnIndex, keywordFromIndex, keywordToIndex);
			} else {
				command = addEvent(arguments, keywordFromIndex, keywordToIndex);
			} 
		} else {
			command = addFloatingTask(arguments);
		}
		return command;
	}
	
	private Command addTask(ArrayList<String> arguments, int keywordByIndex) {
		Date date = null;
		if (keywordByIndex == arguments.size() - 2) {
			date = dateParser.getDate(arguments.get(arguments.size() - 1).toLowerCase());
		}
		if (date == null) {
			return initInvalidCommand(ERROR_DEADLINE);
		}
		List<String> nameList = arguments.subList(POSITION_FIRST_INDEX, keywordByIndex);
		String name = nameParser.getName(nameList);
		if (name == null) {
			return initInvalidCommand(ERROR_TASK_NAME);
		}
		
		Command command = new Command(Command.CommandType.ADD);
		command.setDataType(Command.DataType.TASK);
		command.setName(name);
		command.setDueDate(date);
		return command;
	}
	
	private Command addEvent(ArrayList<String> arguments, int keywordFromIndex, int keywordToIndex) {
		
		if (Math.abs(keywordToIndex - keywordFromIndex) != POSITION_DIFFERENCE_THREE 
			|| (arguments.size() - keywordToIndex != POSITION_DIFFERENCE_THREE 
			&& arguments.size() - keywordFromIndex != POSITION_DIFFERENCE_THREE)) {
			return initInvalidCommand(ERROR_EVENT_FORMAT);
		}
		
		Date startDate = dateParser.getDate(arguments.get(keywordFromIndex + POSITION_PLUS_ONE).toLowerCase());
		String startTime = timeParser.getTime(arguments.get(keywordFromIndex + POSITION_PLUS_TWO));
		Date endDate = dateParser.getDate(arguments.get(keywordToIndex + POSITION_PLUS_ONE).toLowerCase());
		String endTime = timeParser.getTime(arguments.get(keywordToIndex + POSITION_PLUS_TWO));
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
			if (!timeParser.areValidTimes(startTime, endTime)) {
				return initInvalidCommand(ERROR_START_END_TIME);
			}
		}
		if (startDate.compareTo(endDate) == 1) {
			return initInvalidCommand(ERROR_START_END_DATE);
		}
		
		if (keywordToIndex > keywordFromIndex) {
			List<String> nameList = arguments.subList(POSITION_FIRST_INDEX, keywordFromIndex);
			name = nameParser.getName(nameList);
		} else {
			List<String> nameList = arguments.subList(POSITION_FIRST_INDEX, keywordToIndex);
			name = nameParser.getName(nameList);
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
	
	private Command addDayEvent(ArrayList<String> arguments, int keywordOnIndex, int keywordFromIndex, int keywordToIndex) {
		int minIndex = Math.min(keywordToIndex, Math.min(keywordFromIndex, keywordOnIndex));
		int maxIndex = Math.max(keywordToIndex, Math.max(keywordFromIndex, keywordOnIndex));
		
		if (maxIndex - minIndex != POSITION_DIFFERENCE_FOUR 
			|| arguments.size() - maxIndex != POSITION_DIFFERENCE_TWO) {
			return initInvalidCommand(ERROR_EVENT_FORMAT);
		}
		
		Date date = dateParser.getDate(arguments.get(keywordOnIndex + POSITION_PLUS_ONE).toLowerCase());
		String startTime = timeParser.getTime(arguments.get(keywordFromIndex + POSITION_PLUS_ONE));
		String endTime = timeParser.getTime(arguments.get(keywordToIndex + POSITION_PLUS_ONE));
		List<String> nameList = arguments.subList(POSITION_FIRST_INDEX, minIndex);
		String name = nameParser.getName(nameList);
		
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
		if (!timeParser.areValidTimes(startTime, endTime)) {
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
		String name = nameParser.getName(nameList);
		command.setName(name);
		return command;
	}
	
	private ArrayList<String> toLowerCase(ArrayList<String> arguments) {
		ArrayList<String> argumentsLowerCase = new ArrayList<String>();
		for (int i=0; i < arguments.size(); i++) {
			String parameter = arguments.get(i);
			String newParameter = parameter.toLowerCase();
			argumentsLowerCase.add(newParameter);
		}
		return argumentsLowerCase;
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
		DeleteParser deleteParser = new DeleteParser(commandAliases);
		if (argument.matches(REGEX_POSITIVE_INTEGER)) {
			return deleteParser.deleteIndex(Integer.parseInt(argument));
		} else {
			return deleteParser.deleteAlias(argument);
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
		
		String indexString = arguments.get(0);
		int index;
		if (indexString.matches(REGEX_POSITIVE_INTEGER)) {
			index = Integer.parseInt(indexString);
		} else {
			return initInvalidCommand(ERROR_INDEX);
		}

		ArrayList<String> argumentsLowerCase = toLowerCase(arguments);
		int nameIndex = editParser.getKeywordIndex(argumentsLowerCase, KEYWORD_EDIT_NAME);
		int deadlineIndex = editParser.getKeywordIndex(argumentsLowerCase, KEYWORD_EDIT_DEADLINE);
		int startDateIndex = editParser.getKeywordIndex(argumentsLowerCase, KEYWORD_EDIT_START_DATE);
		int startTimeIndex = editParser.getKeywordIndex(argumentsLowerCase, KEYWORD_EDIT_START_TIME);
		int endDateIndex = editParser.getKeywordIndex(argumentsLowerCase, KEYWORD_EDIT_END_DATE);
		int endTimeIndex = editParser.getKeywordIndex(argumentsLowerCase, KEYWORD_EDIT_END_TIME);
		
		String repeatedKeyword = editParser.repeatedKeywords(nameIndex, deadlineIndex, startDateIndex, 
	                                                         startTimeIndex, endDateIndex, endTimeIndex);
		if (repeatedKeyword != null) {
			return initInvalidCommand(String.format(ERROR_DOUBLE_EDIT_KEYWORD, repeatedKeyword));
		}
		if (!(deadlineIndex < 0)) {
			if (!(startDateIndex < 0) || !(startTimeIndex < 0) || !(endDateIndex < 0) || !(endTimeIndex < 0)) {
				return initInvalidCommand(ERROR_EDIT_FORMAT);
			}
		}
		
		ArrayList<Integer> indexArrayList = editParser.getIndexArrayList(nameIndex, deadlineIndex, startDateIndex, 
				                                              startTimeIndex, endDateIndex, endTimeIndex);
		
		if (indexArrayList.isEmpty()) {
			return initInvalidCommand(ERROR_EDIT_FORMAT);
		}
		
		Hashtable<Integer, String> indexKeywordHash = editParser.getHashtable(nameIndex, deadlineIndex, startDateIndex, 
				                                                   startTimeIndex, endDateIndex, endTimeIndex);
		ArrayList<String> editList = editParser.getEditList(nameIndex, deadlineIndex, startDateIndex, 
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
			String argument = nameParser.getName(argumentsSublist);
			String argumentLowerCase = argument;
			if (argument != null) {
				argumentLowerCase = argument.toLowerCase();
			}
			
			 switch (keyword) {
			 
			 	case KEYWORD_EDIT_NAME :
			 		command = editParser.editName(command, argument);
			 		break;

	            case KEYWORD_EDIT_DEADLINE :
	                command = editParser.editDeadline(command, argumentLowerCase);
	                break;
	                
	            case KEYWORD_EDIT_START_DATE :
	            	command = editParser.editStartDate(command, argumentLowerCase);
	            	break;
	            
	            case KEYWORD_EDIT_START_TIME :
	            	command = editParser.editStartTime(command, argumentLowerCase);
	            	break;
	            	
	            case KEYWORD_EDIT_END_DATE :
	            	command = editParser.editEndDate(command, argumentLowerCase);
	            	break;
	            	
	            case KEYWORD_EDIT_END_TIME :
	            	command = editParser.editEndTime(command, argumentLowerCase);
	            	break;
			 }
			 if (command.getCommandType() == Command.CommandType.INVALID) {
				 i = indexArrayList.size();
			 }
		}
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
		String searchWords = nameParser.getName(searchList);
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
		if (arguments.isEmpty()) {
			return initInvalidCommand(ERROR_SET);
		}
		if (arguments.size() != 3 || !arguments.get(1).equals(KEYWORD_SET)) {
			return initInvalidCommand(ERROR_SET_FORMAT);
		}
		String commandKeyword = arguments.get(2);
		String alias = arguments.get(0);
		String commandKeywordVerified = setParser.verifyCommandKeyword(commandKeyword);
		String aliasVerified = setParser.verifyAlias(alias);
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
		String directory = nameParser.getName(directoryList);
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
	
	protected Command initInvalidCommand(String errorMsg) {
		Command command = new Command(Command.CommandType.INVALID);
		command.setName(errorMsg);
		return command;
	}

}
