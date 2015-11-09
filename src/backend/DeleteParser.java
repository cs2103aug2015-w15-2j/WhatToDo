package backend;

import backend.CommandParser;

import java.util.Hashtable;

import struct.Command;

//@@author A0124099B
public class DeleteParser {

	private static final String ERROR_DELETE_ALIAS = "Alias %1$s has not been set.";

	private Hashtable<String, String> commandAliases = new Hashtable<String, String>();

	private static final CommandParser parser = new CommandParser(new Hashtable<String, String>());

	public DeleteParser(Hashtable<String, String> commandAliases) {
		this.commandAliases = commandAliases;
	}

	protected Command deleteAlias(String alias) {
		if (!commandAliases.containsKey(alias)) {
			return parser.initInvalidCommand(String.format(ERROR_DELETE_ALIAS, alias));
		} else {
			String originalCommand = commandAliases.get(alias);
			Command command = new Command(Command.CommandType.DELETEALIAS);
			command.setName(alias);
			command.setOriginalCommand(originalCommand);
			return command;
		}
	}

	protected Command deleteIndex(int index) {
		Command command = new Command(Command.CommandType.DELETE);
		command.setIndex(index);
		return command;
	}

}
