package backend;

import struct.CommandStub;

public class CommandParser {

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