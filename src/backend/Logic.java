package backend;

import java.nio.file.FileSystemException;

import struct.Command;
import struct.CommandStub;
import struct.Date;
import struct.Event;
import struct.Task;

import java.util.ArrayList;

public class Logic {

    private static final String CHAR_NEWLINE = "\n";
    
    private static final String MESSAGE_ERROR_INVALID_COMMAND = " \"%s\" is an invalid command."; 
    private static final String MESSAGE_ERROR_ADD = "Failed to add item to file."; 
    
    private CommandParser commandParser; 
    private Storage storage;
 
	//============================================
	// Constructor
	//============================================
    
    public Logic() {
		try{
			commandParser = new CommandParser(); 
			storage = new Storage(); 
		}
		catch(FileSystemException e){
			
		}
	}
    
	//============================================
	// Public method
	//============================================
    
    /**
     * 
     * @param userInput
     * @return
     */
    public String executeCommand(String userInput) {
    	//TODO - call commandParser when it is ready 
    	//Command command = commandParser.parse(userInput);
    	Command command = new Command();
    	switch (command.getCommandType()) {
    		case ADD : 
    			return executeAdd(command);
    		case DELETE : 
    			return executeDelete(command); 
    		case EDIT : 
    			return executeEdit(command); 
    		case SEARCH :
    			return executeSearch(command); 
    		case EXIT :
    			return executeExit(command); 
    		case INVALID :
            default :
            	return handleInvalid(userInput);
    	}
    	
    }
    
	//============================================
	// Private methods 
	//============================================

    private String executeAdd(Command command){
    	switch (command.getDataType()) {
    		case TASK :
    			return executeAddTask(command); 
    		case FLOATING_TASK : 
    			return executeAddFloatingTask(command); 
    		case EVENT :
    			return executeAddEvent(command); 
    		default: 
    			return MESSAGE_ERROR_ADD;
    	}
    }
    
    private String executeAddTask(Command command){
    	String taskName = command.getName(); 
    	Date taskDeadline = command.getDueDate();
    	try{
    		String feedback = storage.addTask(taskName, taskDeadline);
    		return feedback; 
    	}
    	catch(Exception e){ 
    		return MESSAGE_ERROR_ADD; 
    	}
    }
    
    private String executeAddFloatingTask(Command command){
    	String taskName = command.getName(); 
    	try{
    		String feedback = storage.addFloatingTask(taskName); 
    		return feedback; 
    	}
    	catch(Exception e){ 
    		return MESSAGE_ERROR_ADD; 
    	}
    }
    
    private String executeAddEvent(Command command){
    	String eventName = command.getName(); 
    	Date eventStartDate = command.getStartDate();
    	Date eventEndDate = command.getEndDate();
    	String eventStartTime = command.getStartTime(); 
    	String eventEndTime = command.getEndTime();
    	try{
    		String feedback = storage.addEvent(eventName, eventStartDate, eventStartTime, eventEndDate, eventEndTime); 
    		return feedback; 
    	}
    	catch(Exception e){ 
    		return MESSAGE_ERROR_ADD; 
    	}
    }
   
    //TODO delete 
    private String executeDelete(Command command){
    	return "sth"; 
    }
    
    //TODO edit
    private String executeEdit(Command command){
    	return "sth"; 
    }
    
    //TODO search
    private String executeSearch(Command command){
    	return "sth"; 
    }
    
    //TODO exit
    private String executeExit(Command command){
    	return "sth"; 
    }
    
    private String handleInvalid(String userInput){ 
    	return String.format(MESSAGE_ERROR_INVALID_COMMAND, userInput); 
    }
    
    
    /**
     * runOperation - stub of executeCommand 
     * @param userInput
     * @return
     */
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
        CommandParser parser = new CommandParser();
        CommandStub command = parser.getCommandType(userInput);

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
        CommandParser parser = new CommandParser();
        return parser.isSwapCommand(currentState, userInput);
    }
}
