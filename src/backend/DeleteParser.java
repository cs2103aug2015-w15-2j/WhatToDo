/**
 * This class parses user input string for delete commands.
 * 
 * @@author A0124099B
 */

package backend;

import java.util.ArrayList;
import java.util.Hashtable;

import struct.Command;

public class DeleteParser {
	
	private static final String MESSAGE_ERROR_DELETE = "Index/alias command required.";
	private static final String MESSAGE_ERROR_DELETE_INDEX_ALIAS = "Invalid index or alias.";
	private static final String MESSAGE_ERROR_DELETE_ALIAS = "Alias %1$s has not been set.";

	private Hashtable<String, String> commandAliases = new Hashtable<String, String>();

	public DeleteParser(Hashtable<String, String> commandAliases) {
		assert(commandAliases != null);
		this.commandAliases = commandAliases;
	}
	
	protected Command parse(ArrayList<String> arguments) {
		// String verificationMsg will only be STRING_VERIFIED if it passes
		// the checks in verification method
		String verificationMsg = verifyDelete(arguments);
		if (!verificationMsg.equals(CommandParser.STRING_VERIFIED)) {
			return CommandParser.initInvalidCommand(verificationMsg);
		}
		String argument = arguments.get(CommandParser.POSITION_FIRST_INDEX);
		
		// Delete command can be used to delete both index and alias
		if (argument.matches(CommandParser.REGEX_POSITIVE_INTEGER)) {
			return deleteIndex(Integer.parseInt(argument));
		} else {
			return deleteAlias(argument);
		}
	}

	private String verifyDelete(ArrayList<String> arguments) {
		if (arguments.isEmpty()) {
			return MESSAGE_ERROR_DELETE;
		}
		if (arguments.size() > 1) {
			return MESSAGE_ERROR_DELETE_INDEX_ALIAS;
		}
		return CommandParser.STRING_VERIFIED;
	}

	private Command deleteAlias(String alias) {
		if (!commandAliases.containsKey(alias)) {
			return CommandParser.initInvalidCommand(String.format(MESSAGE_ERROR_DELETE_ALIAS, alias));
		} else {
			String originalCommand = commandAliases.get(alias);
			Command command = new Command(Command.CommandType.DELETEALIAS);
			command.setName(alias);
			command.setOriginalCommand(originalCommand);
			return command;
		}
	}

	private Command deleteIndex(int index) {
		Command command = new Command(Command.CommandType.DELETE);
		command.setIndex(index);
		return command;
	}

}
