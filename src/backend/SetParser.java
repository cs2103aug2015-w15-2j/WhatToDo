package backend;

import java.util.ArrayList;

import struct.Command;

//@@author A0124099B
public class SetParser {

	private static final String STRING_VERIFIED = "verified";
	
	private static final String KEYWORD_SET = "as";

	private static final String ERROR_SET_COMMAND = "%1$s is not a registered command.";
	private static final String ERROR_SET_ALIAS = "Input alias is a either a registered command and cannot be used or an alias-in-use.";
	private static final String ERROR_SET_NUMBER = "Positive integers cannot be used as aliases.";
	private static final String ERROR_SET = "Command and alias required.";
	private static final String ERROR_SET_FORMAT = "Invalid set format.";

	private static final ArrayList<String> COMMANDS_ARRAY_LIST = new ArrayList<String>();

	public SetParser() {
		initCommandsArrayList();
	}
	
	protected Command parse(ArrayList<String> arguments) {
		if (arguments.isEmpty()) {
			return CommandParser.initInvalidCommand(ERROR_SET);
		}
		if (arguments.size() != 3 || !arguments.get(1).equals(KEYWORD_SET)) {
			return CommandParser.initInvalidCommand(ERROR_SET_FORMAT);
		}
		String commandKeyword = arguments.get(2);
		String alias = arguments.get(0);
		String commandKeywordVerified = verifyCommandKeyword(commandKeyword);
		String aliasVerified = verifyAlias(alias);
		if (!commandKeywordVerified.equals(STRING_VERIFIED)) {
			return CommandParser.initInvalidCommand(commandKeywordVerified);
		}
		if (!aliasVerified.equals(STRING_VERIFIED)) {
			return CommandParser.initInvalidCommand(aliasVerified);
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
			return String.format(ERROR_SET_COMMAND, commandKeyword);
		}
	}

	private String verifyAlias(String alias) {
		if (COMMANDS_ARRAY_LIST.contains(alias)) {
			return ERROR_SET_ALIAS;
		} else if (alias.matches(CommandParser.REGEX_POSITIVE_INTEGER)) {
			return ERROR_SET_NUMBER;
		} else {
			return STRING_VERIFIED;
		}
	}

	private void initCommandsArrayList() {
		COMMANDS_ARRAY_LIST.add(CommandParser.USER_COMMAND_ADD);
		COMMANDS_ARRAY_LIST.add(CommandParser.USER_COMMAND_DELETE);
		COMMANDS_ARRAY_LIST.add(CommandParser.USER_COMMAND_EDIT);
		COMMANDS_ARRAY_LIST.add(CommandParser.USER_COMMAND_SEARCH);
		COMMANDS_ARRAY_LIST.add(CommandParser.USER_COMMAND_DONE);
		COMMANDS_ARRAY_LIST.add(CommandParser.USER_COMMAND_SET);
		COMMANDS_ARRAY_LIST.add(CommandParser.USER_COMMAND_SAVE);
		COMMANDS_ARRAY_LIST.add(CommandParser.USER_COMMAND_UNDO);
		COMMANDS_ARRAY_LIST.add(CommandParser.USER_COMMAND_REDO);
		COMMANDS_ARRAY_LIST.add(CommandParser.USER_COMMAND_VIEW_ALL);
		COMMANDS_ARRAY_LIST.add(CommandParser.USER_COMMAND_VIEW_DEF);
		COMMANDS_ARRAY_LIST.add(CommandParser.USER_COMMAND_VIEW_HIST);
		COMMANDS_ARRAY_LIST.add(CommandParser.USER_COMMAND_VIEW_UNRES);
		COMMANDS_ARRAY_LIST.add(CommandParser.USER_COMMAND_VIEW_HELP);
		COMMANDS_ARRAY_LIST.add(CommandParser.USER_COMMAND_VIEW_OPEN_FILE);
		COMMANDS_ARRAY_LIST.add(CommandParser.USER_COMMAND_VIEW_CONFIG);
		COMMANDS_ARRAY_LIST.add(CommandParser.USER_COMMAND_EXIT);
	}
}
