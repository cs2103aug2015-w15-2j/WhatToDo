/**
 * This class parses user input string for edit commands.
 * 
 * @@author A0124099B
 */

package backend;

import java.util.ArrayList;

import struct.Command;

public class SetParser {
	
	private static final int POSITION_THIRD_INDEX = 2;
	
	private static final String KEYWORD_SET = "as";

	private static final String MESSAGE_ERROR_SET_COMMAND = "%1$s is not a registered command.";
	private static final String MESSAGE_ERROR_SET_ALIAS = "Input alias is a either a registered command and cannot be used or an alias-in-use.";
	private static final String MESSAGE_ERROR_SET_NUMBER = "Positive integers cannot be used as aliases.";
	private static final String MESSAGE_ERROR_SET = "Command and alias required.";
	private static final String MESSAGE_ERROR_SET_FORMAT = "Invalid set format.";
	private static final String MESSAGE_ERROR_SET_AS = "Keyword as cannot be used as alias.";

	private static final ArrayList<String> COMMANDS_ARRAY_LIST = new ArrayList<String>();

	public SetParser() {
		initCommandsArrayList();
	}
	
	protected Command parse(ArrayList<String> arguments) {
		// String verificationMsg will only be STRING_VERIFIED if it passes
		// the checks in verification method
		String verificationMsg = verifySet(arguments);
		if (!verificationMsg.equals(CommandParser.STRING_VERIFIED)) {
			return CommandParser.initInvalidCommand(verificationMsg);
		}
		
		String commandKeyword = arguments.get(POSITION_THIRD_INDEX);
		String alias = arguments.get(CommandParser.POSITION_FIRST_INDEX);
		String commandKeywordVerified = verifyCommandKeyword(commandKeyword);
		String aliasVerified = verifyAlias(alias);
		
		// String commandKeywordVerified will only be STRING_VERIFIED if commandKeyword
		// is a alias for or is a registered command in COMMANDS_ARRAY_LIST
		if (!commandKeywordVerified.equals(CommandParser.STRING_VERIFIED)) {
			return CommandParser.initInvalidCommand(commandKeywordVerified);
		}
		
		// String aliasVerified will only be STRING_VERIFIED if alias is valid.
		// This means it cannot be a registered alias or command,
		// KEYWORD_SET_AS, or positive integer.
		if (!aliasVerified.equals(CommandParser.STRING_VERIFIED)) {
			return CommandParser.initInvalidCommand(aliasVerified);
		}

		Command command = new Command(Command.CommandType.SET);
		command.setName(alias);
		command.setOriginalCommand(commandKeyword);
		return command;
	}

	private String verifySet(ArrayList<String> arguments) {
		if (arguments.isEmpty()) {
			return MESSAGE_ERROR_SET;
		}
		if (arguments.size() != 3 || !arguments.get(1).equals(KEYWORD_SET)) {
			return MESSAGE_ERROR_SET_FORMAT;
		}
		return CommandParser.STRING_VERIFIED;
	}

	private String verifyCommandKeyword(String commandKeyword) {
		if (COMMANDS_ARRAY_LIST.contains(commandKeyword)) {
			return CommandParser.STRING_VERIFIED;
		} else {
			return String.format(MESSAGE_ERROR_SET_COMMAND, commandKeyword);
		}
	}

	private String verifyAlias(String alias) {
		if (COMMANDS_ARRAY_LIST.contains(alias)) {
			return MESSAGE_ERROR_SET_ALIAS;
		} else if (KEYWORD_SET.equals(alias)) {
			return MESSAGE_ERROR_SET_AS;
		} else if (alias.matches(CommandParser.REGEX_POSITIVE_INTEGER)) {
			return MESSAGE_ERROR_SET_NUMBER;
		} else {
			return CommandParser.STRING_VERIFIED;
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
