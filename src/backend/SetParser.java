package backend;

import java.util.ArrayList;

public class SetParser {
	
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
	
    private static final String ERROR_SET_COMMAND = "%1$s is not a registered command.";
    private static final String ERROR_SET_ALIAS = "Input alias is a either a registered command and cannot be used or an alias-in-use.";
    private static final String ERROR_SET_NUMBER = "Positive integers cannot be used as aliases.";
	
    private static final ArrayList<String> COMMANDS_ARRAY_LIST = new ArrayList<String>();
	
	public SetParser() {
		initCommandsArrayList();
	}
	
	protected String verifyCommandKeyword(String commandKeyword) {
		if (COMMANDS_ARRAY_LIST.contains(commandKeyword)) {
			return STRING_VERIFIED;
		} else {
			return String.format(ERROR_SET_COMMAND, commandKeyword);
		}
	}
	
	protected String verifyAlias(String alias) {
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
	}
}
