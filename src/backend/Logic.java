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

    private static final String MESSAGE_ERROR_INVALID_COMMAND = " \"%s\" is an invalid command."; 
    private static final String MESSAGE_ERROR_ADD = "Error encountered when adding item. The item's data type is unrecognized."; 
    private static final String MESSAGE_ERROR_READ_FILE = "Error encountered when reading file.";
    private static final String MESSAGE_EXIT = "exit";
    
    private static final String DISPLAY_NO_TASK_TODAY = "There are no tasks due today.\n"; 
    private static final String DISPLAY_NO_TASK_TOMMORROW = "There are no tasks due tommorrow.\n"; 
    private static final String DISPLAY_NO_EVENT_TODAY = "There are no events today.\n"; 
    private static final String DISPLAY_NO_EVENT_TOMMORROW = "There are no events tommorrow.\n"; 
    private static final String DISPLAY_FORMAT_TASK = "%d. %s\n"; 
    private static final String DISPLAY_LAYOUT = "TODAY, %s \n%s\nTOMMORROW, %s \n%s";
    
    
    private static final String TYPE_FLOAT = "float";
    private static final String TYPE_TASK = "task";
    private static final String TYPE_EVENT = "event";
    
    private static final String NEWLINE = "\n";
    private static final String SEMICOLON = ";";
    
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
    
    //TODO refactor task default view
    public String taskDefaultView(){
    	StringBuffer todayContent = new StringBuffer(); 
    	StringBuffer tomorrowContent = new StringBuffer(); 
    	String todayDate = getTodayDate(); 
    	String tomorrowDate = getTomorrowDate();
    	String[] lines = getLinesInFile(); 
    	
    	for(int index = 0; index < lines.length; index++){
    		String line = lines[index].trim(); 
    		String[] lineComponents = line.split(SEMICOLON);
    		if(lineComponents[0].equals(TYPE_TASK)){
    			//TODO assert lineComponent.length == 3 
    			if(lineComponents[2].equals(todayDate)){
    				String formatted = String.format(DISPLAY_FORMAT_TASK, index+1, lineComponents[1]);
    				todayContent.append(formatted);
    			}
    			else if(lineComponents[2].equals(tomorrowDate)){
    				String formatted = String.format(DISPLAY_FORMAT_TASK, index+1, lineComponents[1]);
    				tomorrowContent.append(formatted);
    			}
    		}
    	}
    	
    	if(todayContent.length() == 0){
    		todayContent.append(DISPLAY_NO_TASK_TODAY);
    	}
    	
    	if(tomorrowContent.length() == 0){
    		tomorrowContent.append(DISPLAY_NO_TASK_TOMMORROW);
    	}
    	
    	return String.format(DISPLAY_LAYOUT, todayDate, todayContent.toString(), tomorrowDate, tomorrowContent.toString()).trim();
    }
    
    //TODO event default view - need to think abt how to get and display events that are currently going on 
    // on top of events that start today
    public String eventDefaultView(){
    	StringBuffer todayContent = new StringBuffer(); 
    	StringBuffer tomorrowContent = new StringBuffer(); 
    	String todayDate = getTodayDate(); 
    	String tomorrowDate = getTomorrowDate();
    	String[] lines = getLinesInFile(); 
    	
//    	for(int index = 0; index < lines.length; index++){
//    		String line = lines[index].trim(); 
//    		String[] lineComponents = line.split(SEMICOLON);
//    		if(lineComponents[0].equals(TYPE_)){
//    			if(lineComponents[2].equals(todayDate)){
//    				String formatted = String.format(DISPLAY_FORMAT_TASK, index+1, lineComponents[1]);
//    				todayContent.append(formatted);
//    			}
//    			else if(lineComponents[2].equals(tomorrowDate)){
//    				String formatted = String.format(DISPLAY_FORMAT_TASK, index+1, lineComponents[1]);
//    				tomorrowContent.append(formatted);
//    			}
//    		}
//    	}
    	
    	if(todayContent.length() == 0){
    		todayContent.append(DISPLAY_NO_EVENT_TODAY);
    	}
    	
    	if(tomorrowContent.length() == 0){
    		tomorrowContent.append(DISPLAY_NO_EVENT_TOMMORROW);
    	}
    	
    	return String.format(DISPLAY_LAYOUT, todayDate, todayContent.toString(), tomorrowDate, tomorrowContent.toString()).trim();
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
    
    private String getTomorrowDate(){
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
		return sdf.format(cal.getTime());
    }
    
    private String[] getLinesInFile(){
    	String fileContents = storage.display();
    	return fileContents.split(NEWLINE);
    }
    
    
	//============================================
	// Stub methods for testing 
	//============================================

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
