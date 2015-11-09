package backend;

import java.util.ArrayList;
import java.util.Hashtable;

import struct.Command;

//@@author A0124099B
public class DeleteParser {
	
	private static final String ERROR_DELETE = "Index/alias command required.";
	private static final String ERROR_DELETE_INDEX_ALIAS = "Invalid index or alias.";
	private static final String ERROR_DELETE_ALIAS = "Alias %1$s has not been set.";

	private Hashtable<String, String> commandAliases = new Hashtable<String, String>();

	public DeleteParser(Hashtable<String, String> commandAliases) {
		this.commandAliases = commandAliases;
	}
	
	protected Command parse(ArrayList<String> arguments) {
		if (arguments.isEmpty()) {
			return CommandParser.initInvalidCommand(ERROR_DELETE);
		}
		if (arguments.size() > 1) {
			return CommandParser.initInvalidCommand(ERROR_DELETE_INDEX_ALIAS);
		}

		String argument = arguments.get(0);
		DeleteParser deleteParser = new DeleteParser(commandAliases);
		if (argument.matches(CommandParser.REGEX_POSITIVE_INTEGER)) {
			return deleteParser.deleteIndex(Integer.parseInt(argument));
		} else {
			return deleteParser.deleteAlias(argument);
		}
	}

	private Command deleteAlias(String alias) {
		if (!commandAliases.containsKey(alias)) {
			return CommandParser.initInvalidCommand(String.format(ERROR_DELETE_ALIAS, alias));
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
