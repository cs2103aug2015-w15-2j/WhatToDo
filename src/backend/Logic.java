package backend;

import java.nio.file.FileSystemException;

import struct.Date;
import struct.Event;
import struct.Task;
import struct.Command;

import java.util.ArrayList;

public class Logic {

    private static final String CHAR_NEWLINE = "\n";
    private Storage storage;

    public ArrayList<String> runOperation(String userInput) {
        // Function stub, assumed add operation for a task

        ArrayList<String> returnMessages = new ArrayList<String>();
    	
    	// stub to test storage.
    	try {
    		storage = new Storage();
    	} catch (FileSystemException fileException) {
    		returnMessages.add(fileException.getMessage());
    	}

        // Creates a Parser object to determine command type
        Parser parser = new Parser();
        Command command = parser.getCommandType(userInput);

        // Stub FOR TESTING ONLY
        switch(command) {
            case ADD:
                // Add just a task to return list
                String textToAdd = userInput.split(" ")[1];
                Task taskAdd = new Task();
                taskAdd.setTaskName(textToAdd);
                taskAdd.setTaskDeadline(new Date("Wednesday", "300915"));
                taskAdd.setTaskFloating(false);
                returnMessages.add(
                        storage.addTask(taskAdd.getTaskName(), taskAdd.getTaskDeadline()));
                break;
            case SEARCH:
                // Add a task and an event to return list
                Task taskSearch = new Task();
                taskSearch.setTaskName("Some task name");
                taskSearch.setTaskDeadline(new Date("Wednesday", "300915"));
                taskSearch.setTaskFloating(false);
                returnMessages.add("Task: " + taskSearch.getTaskName() + CHAR_NEWLINE +
                        "Due on " + taskSearch.getTaskDeadline().getDayString() + ", " +
                        taskSearch.getTaskDeadline().getFormatDate() + ".");

                Event event = new Event();
                event.setEventName("Some event name");
                event.setEventStartDate(new Date("Thursday", "011015"));
                event.setEventEndDate(new Date("Friday", "021015"));
                event.setEventStartTime("0800");
                event.setEventEndTime("1800");
                returnMessages.add("Event: " + event.getEventName() + CHAR_NEWLINE +
                        "Start Date: " + event.getEventStartDate().getFormatDate() + CHAR_NEWLINE +
                        "End Date: " + event.getEventEndDate().getFormatDate() + CHAR_NEWLINE +
                        "Start Time: " + event.getEventStartTime() + CHAR_NEWLINE +
                        "End Time: " + event.getEventEndTime());
                break;
            default:
                // Do nothing
        }

        return returnMessages;
    }

    public boolean isSwapCommand(String currentState, String userInput) {
        // Create a parser object to parse
        Parser parser = new Parser();
        return parser.isSwapCommand(currentState, userInput);
    }
}
