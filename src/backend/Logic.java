package backend;

import java.nio.file.FileSystemException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import struct.Command;
import struct.CommandStub;
import struct.Date;
import struct.Event;
import struct.Task;

public class Logic {

    private static final String CHAR_NEWLINE = "\n";
    
    private static final String MESSAGE_ERROR_INVALID_COMMAND = " \"%s\" is an invalid command."; 
    private static final String MESSAGE_ERROR_ADD = "Error encountered when adding item. The item's data type is unrecognized."; 
    private static final String MESSAGE_ERROR_READ_FILE = "Error encountered when reading file.";
    
    private static final String MESSAGE_EXIT = "exit"; 
    
    private CommandParser commandParser; 
    private Storage storage;
 
	//============================================
	// Constructor
	//============================================
    
    public Logic() throws FileSystemException {
			commandParser = new CommandParser();
			storage = new Storage();
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
    	Command command = commandParser.parse(userInput);
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
    
    //TODO task default view
    public String taskDefaultView(){
    	String todayDate = getTodayDate(); 
    	String fileContents = storage.display();

    	return "sth";
    }
    
    //TODO event default view
    public String eventDefaultView(){
    	
    	return "sth";
    }
    
	//============================================
	// Private methods for executeCommand 
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
    	return storage.addTask(taskName, taskDeadline);
    }
    
    private String executeAddFloatingTask(Command command){
    	String taskName = command.getName(); 
    	return storage.addFloatingTask(taskName); 
    }
    
    private String executeAddEvent(Command command){
    	String eventName = command.getName(); 
    	Date eventStartDate = command.getStartDate();
    	Date eventEndDate = command.getEndDate();
    	String eventStartTime = command.getStartTime(); 
    	String eventEndTime = command.getEndTime();
    	return storage.addEvent(eventName, eventStartDate, eventStartTime, eventEndDate, eventEndTime); 
    }
   
    private String executeDelete(Command command){
    	int lineNumber = command.getIndex(); 
    	return storage.deleteLine(lineNumber); 
    }
    
    //TODO edit now only edit name of task - need to add code to determine 
    //whether user wants to edit date, 
    private String executeEdit(Command command){
    	int lineNumber = command.getIndex(); 
    	String newName = command.getName();  
    	return storage.editName(lineNumber, newName); 
    }
    
    //TODO search
    private String executeSearch(Command command){
    	return "sth"; 
    }
    
    
    private String executeExit(Command command){
    	return MESSAGE_EXIT; 
    }
    
    private String handleInvalid(String userInput){ 
    	return String.format(MESSAGE_ERROR_INVALID_COMMAND, userInput); 
    }
    
	//============================================
	// Private methods for defaultView 
	//============================================    
    
    private String getTodayDate(){
    	Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
		return sdf.format(cal.getTime());
    }
    
    
	//============================================
	// Stub methods for testing 
	//============================================
    
    /**
     * runOperation - stub of executeCommand 
     * @param userInput
     * @return
     */
    public String runOperation(String userInput) {
        // Function stub, assumed add operation for a task

        String returnMessages = "";
    	
    	// stub to test storage.
    	try {
    		storage = new Storage();
    	} catch (FileSystemException fileException) {
    		returnMessages = fileException.getMessage();
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
                returnMessages = storage.addTask(taskAdd.getTaskName(), taskAdd.getTaskDeadline());
                break;
            case SEARCH:
                // Add a task and an event to return list
                Task taskSearch = new Task();
                taskSearch.setTaskName("Some task name");
                taskSearch.setTaskDeadline(new Date("Wednesday", "300915"));
                taskSearch.setTaskFloating(false);
                returnMessages = "Task: " + taskSearch.getTaskName() + CHAR_NEWLINE +
                        "Due on " + taskSearch.getTaskDeadline().getDayString() + ", " +
                        taskSearch.getTaskDeadline().getFormatDate() + ".";

                Event event = new Event();
                event.setEventName("Some event name");
                event.setEventStartDate(new Date("Thursday", "011015"));
                event.setEventEndDate(new Date("Friday", "021015"));
                event.setEventStartTime("0800");
                event.setEventEndTime("1800");
                returnMessages = "Event: " + event.getEventName() + CHAR_NEWLINE +
                        "Start Date: " + event.getEventStartDate().getFormatDate() + CHAR_NEWLINE +
                        "End Date: " + event.getEventEndDate().getFormatDate() + CHAR_NEWLINE +
                        "Start Time: " + event.getEventStartTime() + CHAR_NEWLINE +
                        "End Time: " + event.getEventEndTime();
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

    public String readTasks() {
        return "stub";
    }

    public String readEvents() {
        return "stub";
    }

    public String getFilepath() {
        return "C:/Users/todo.txt";
    }
}
