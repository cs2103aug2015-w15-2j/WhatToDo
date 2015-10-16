package backend;

import java.nio.file.FileSystemException;

import struct.Command;
import struct.Date;
import struct.Event;
import struct.FloatingTask;
import struct.State;
import struct.Task;

public class Logic {

	private static final int INDEX_TYPE = 0; 
	private static final int INDEX_NAME = 1; 
	private static final int INDEX_ISDONE = 2; 
	private static final int INDEX_DUEDATE = 3;
	private static final int INDEX_STARTDATE = 3; 
	private static final int INDEX_STARTTIME = 4; 
	private static final int INDEX_ENDDATE = 5; 
	private static final int INDEX_ENDTIME = 6; 

	private static final String MESSAGE_ADD_TASK = "Added \"%s\" to list. Due on %s, %s.";
	private static final String MESSAGE_ADD_FLOAT_TASK = "Added \"%s\" to list.";
	private static final String MESSAGE_ADD_EVENT = "Added \"%s\" to list. Event Start: %s, %s, %s Event End: %s, %s, %s.";
	private static final String MESSAGE_DELETE_LINE = "Deleted \"%s\" from list.";
	private static final String MESSAGE_REDO = "Redid command: \"%s\"."; 
	private static final String MESSAGE_NO_REDO = "There are no commands to redo.";
	private static final String MESSAGE_UNDO = "Undid command: \"%s\"."; 
	private static final String MESSAGE_NO_UNDO = "There are no commands to undo.";
	private static final String MESSAGE_EXIT = "Exit";
	
	private static final String MESSAGE_ERROR_UNKNOWN = "Unknown error encountered."; 
    private static final String MESSAGE_ERROR_INVALID_COMMAND = " \"%s\" is an invalid command."; 
    private static final String MESSAGE_ERROR_ADD = "Error encountered when adding item. The item's data type is unrecognized."; 
    private static final String MESSAGE_ERROR_UNDO = "Error encountered in memory. Undo will be unavailable for all commands before this.";
    
    
    private static final String DISPLAY_NO_ITEMS = "There are no items to display.\n"; 
    private static final String DISPLAY_FORMAT_FLOAT_OR_TASK = "%d. %s\n"; 
    private static final String DISPLAY_FORMAT_EVENT = "%d. [%s %s - %s %s]\t%s\n"; 
    private static final String DISPLAY_LAYOUT_TASK = "FLOAT\n%s\n\nTODAY - %s \n%s\n\nTOMORROW - %s \n%s";
    private static final String DISPLAY_LAYOUT_EVENT = "ONGOING\n%s\n\nTODAY - %s \n%s\n\nTOMORROW - %s \n%s";
    
    private static final String TYPE_FLOAT = "float";
    private static final String TYPE_TASK = "task";
    private static final String TYPE_EVENT = "event";
    
    private static final String TODO = "todo"; 
    
    private static final String NEWLINE = "\n";
    private static final String SEMICOLON = ";";
    
    private CommandParser commandParser; 
    private Storage storage;
    private Memory memory; 
    private Command prevCommand; 
 
	//============================================
	// Constructor
	//============================================
    
    public Logic() throws FileSystemException {
			commandParser = new CommandParser();
			storage = new Storage();
			memory = new Memory();
			prevCommand = null; 
	}
    
	//============================================
	// Public methods
	//============================================
    
    /**
     * executeCommand
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
    		case UNDO : 
    			return executeUndo(); 
    		case REDO : 
    			return executeRedo();
    		case EXIT :
    			return executeExit(); 
    		case INVALID :
            default :
            	return handleInvalid(userInput);
    	}	
    }
    
    public String taskDefaultView(){
    	try{
    		String[] linesInFile = getLinesInFile();
        	String floatContent = getFloatContent(linesInFile); 
            String todayContent = getTaskContent(linesInFile, Date.todayDateShort()); 
            String tomorrowContent = getTaskContent(linesInFile, Date.tomorrowDateShort()); 
           
            return String.format(DISPLAY_LAYOUT_TASK, floatContent, Date.todayDateLong(), todayContent, 
            		Date.tomorrowDateLong(), tomorrowContent).trim();
    	}
    	catch(FileSystemException e){
    		return String.format(DISPLAY_LAYOUT_TASK, e.getMessage(), Date.todayDateLong(), e.getMessage(), 
            		Date.tomorrowDateLong(), e.getMessage()).trim();
    	}
    	catch(Exception e){
    		return String.format(DISPLAY_LAYOUT_TASK, MESSAGE_ERROR_UNKNOWN, Date.todayDateLong(), MESSAGE_ERROR_UNKNOWN, 
            		Date.tomorrowDateLong(), MESSAGE_ERROR_UNKNOWN).trim();
    	}
    }
    
    public String eventDefaultView(){
    	try{
    		String[] linesInFile = getLinesInFile();
        	String onGoingContent = getOngoingEventContent(linesInFile); 
            String todayContent = getEventContent(linesInFile, Date.todayDateShort()); 
            String tomorrowContent = getEventContent(linesInFile, Date.tomorrowDateShort()); 
            	
            return String.format(DISPLAY_LAYOUT_EVENT, onGoingContent, Date.todayDateLong(), todayContent, 
            		Date.tomorrowDateLong(), tomorrowContent).trim();
    	}
    	catch(FileSystemException e){
    		return String.format(DISPLAY_LAYOUT_EVENT, e.getMessage(), Date.todayDateLong(), e.getMessage(), 
            		Date.tomorrowDateLong(), e.getMessage()).trim();
    	}
    	catch (Exception e) {
    		return String.format(DISPLAY_LAYOUT_EVENT, MESSAGE_ERROR_UNKNOWN, Date.todayDateLong(), MESSAGE_ERROR_UNKNOWN, 
            		Date.tomorrowDateLong(), MESSAGE_ERROR_UNKNOWN).trim();
		}
    }
    
    public String getFilepath() {
    	try{
    		return storage.getFilePath();
    	}
    	catch(Exception e){
    		return MESSAGE_ERROR_UNKNOWN;
    	}
    }
    
	//============================================
	// Private methods for executeCommand 
	//============================================
    
    private String executeAdd(Command command){
    	try{
    		switch (command.getDataType()) {
			case FLOATING_TASK : 
				return executeAddFloatingTask(command); 
    		case TASK :
    			return executeAddTask(command); 
    		case EVENT :
    			return executeAddEvent(command); 
    		default: 
    			return MESSAGE_ERROR_ADD;
    		}
    	}
    	catch(FileSystemException e){
    		return e.getMessage();
    	}
    	catch (Exception e) {
			return MESSAGE_ERROR_UNKNOWN; 
		}
    }
    
    private String executeAddFloatingTask(Command command) throws FileSystemException{
    	State stateBeforeExecutingCommand = getState(command);
    		
    	String taskName = command.getName(); 
        FloatingTask floatingTask = new FloatingTask(taskName, false);
        storage.addFloatingTask(floatingTask); 
        String addFeedback = String.format(MESSAGE_ADD_FLOAT_TASK, taskName); 
        
        boolean isSaved = saveState(stateBeforeExecutingCommand);
        return formFeedbackMsg(addFeedback, isSaved);
    }
    
    private String executeAddTask(Command command) throws FileSystemException{
    	State stateBeforeExecutingCommand = getState(command);
    		
    	String taskName = command.getName(); 
        Date taskDeadline = command.getDueDate();
        Task task = new Task(taskName, false, taskDeadline);
        storage.addTask(task);
        String addFeedback = String.format(MESSAGE_ADD_TASK, taskName, 
        		taskDeadline.getDayString(),taskDeadline.getFullDate()); 

        boolean isSaved = saveState(stateBeforeExecutingCommand);
        return formFeedbackMsg(addFeedback, isSaved);
    }
    
    private String executeAddEvent(Command command) throws FileSystemException{
    	State stateBeforeExecutingCommand = getState(command);
    		
    	String eventName = command.getName(); 
        Date eventStartDate = command.getStartDate();
        Date eventEndDate = command.getEndDate();
        String eventStartTime = command.getStartTime(); 
        String eventEndTime = command.getEndTime();
        Event event = new Event(eventName, false, eventStartDate, eventEndDate, eventStartTime, eventEndTime);
        storage.addEvent(event); 
        String addFeedback = String.format(MESSAGE_ADD_EVENT, eventName, 
        		eventStartDate.getDayString(), eventStartDate.getFullDate(), eventStartTime, 
        		eventEndDate.getDayString(), eventEndDate.getFullDate(), eventEndTime);
        
        boolean isSaved = saveState(stateBeforeExecutingCommand);
        return formFeedbackMsg(addFeedback, isSaved);
    }
   
    private String executeDelete(Command command){
    	try{
    		State stateBeforeExecutingCommand = getState(command);
    		
    		int lineNumber = command.getIndex(); 
        	String deletedLine = storage.deleteLine(lineNumber); 
        	//TODO format feedback message for delete
        	String deleteFeedback = String.format(MESSAGE_DELETE_LINE, deletedLine); 
        	
        	boolean isSaved = saveState(stateBeforeExecutingCommand);
        	return formFeedbackMsg(deleteFeedback, isSaved);
    	}
    	catch(FileSystemException e){
    		return e.getMessage();
    	}
    	catch(Exception e){
    		return MESSAGE_ERROR_UNKNOWN;
    	}
    }
        
    //TODO edit now only edit name of task - need to add code to determine 
    //whether user wants to edit date, 
    private String executeEdit(Command command){
    	try{ 
    		int lineNumber = command.getIndex(); 
        	String newName = command.getName();  
        	//TODO format feedback msg for edit 
        	return storage.editName(lineNumber, newName); 
    	}
    	//TODO exception for edit
    	catch(Exception e){
    		return e.getMessage(); 
    	}
    }
    
    //TODO search
    private String executeSearch(Command command){
    	return "sth"; 
    }
    
    private String executeUndo(){ 
    	State stateAfterUndo = null;
    	try{
    		String currFileContents = storage.display();
        	stateAfterUndo = memory.getUndoState(currFileContents);
    	}
    	catch(Exception e){
    		
    	}
    	
    	if(stateAfterUndo == null){
    		return MESSAGE_NO_UNDO; 
    	}
    	
    	try{ 
    		storage.overwriteFile(stateAfterUndo.getFileContents());
        	return String.format(MESSAGE_UNDO, stateAfterUndo.getUserCommand());
    	}
    	//TODO exception for undo
    	catch(Exception e){
    		return e.getMessage();
    	}
    }
    
    //TODO redo 
    private String executeRedo(){ 
    	State stateAfterRedo = null;
    	try{
    		String currFileContents = storage.display();
        	stateAfterRedo = memory.getRedoState(currFileContents);
    	}
    	catch(Exception e){
    		
    	}
    	
    	if(stateAfterRedo == null){
    		return MESSAGE_NO_REDO; 
    	}
    	
    	try{ 
    		storage.overwriteFile(stateAfterRedo.getFileContents());
        	return String.format(MESSAGE_REDO, stateAfterRedo.getUserCommand());
    	}
    	//TODO exception for redo
    	catch(Exception e){
    		return e.getMessage();
    	}
    }
    
   	private State getState(Command command) {
   		try{
   			String prevFileContents = storage.display(); 
   	   		String userCommand = command.getUserInput();
   	   		State prevState = new State(prevFileContents, userCommand);
   	   		return prevState;
   		}
   		catch(Exception e){
   			return null; 
   		}
   	}
    
	private boolean saveState(State prevState) {
		if(prevState != null){
        	memory.savePrevState(prevState);
        	return true;
        }
		else{
        	memory.clearUndoStack();
        	return false;
        }
	}
   	
	private String formFeedbackMsg(String addFeedback, boolean isSaved) {
		if(isSaved){
        	return addFeedback;
        }
        else{
        	return addFeedback + MESSAGE_ERROR_UNDO;
        }
	}
	
    private String executeExit(){
    	return MESSAGE_EXIT; 
    }
    
    private String handleInvalid(String userInput){ 
    	return String.format(MESSAGE_ERROR_INVALID_COMMAND, userInput); 
    }
    
	//============================================
	// Private methods for defaultView 
	//============================================    
      
    private String[] getLinesInFile() throws FileSystemException{
    	String fileContents = storage.display();
    	if(fileContents.isEmpty()){ 
    		return new String[0];
    	}else{
    		return fileContents.split(NEWLINE);
    	}
    }
    
    private String getFloatContent(String[] linesInFile){ 
    	StringBuffer floatContentBuffer = new StringBuffer();
    	
    	for(int index = 0; index < linesInFile.length; index++){
    		String line = linesInFile[index].trim(); 
    		String[] lineComponents = line.split(SEMICOLON);
    		if(isUncompleted(TYPE_FLOAT, lineComponents)){
    			String formatted = String.format(DISPLAY_FORMAT_FLOAT_OR_TASK, index+1, lineComponents[INDEX_NAME]);
    			floatContentBuffer.append(formatted);
    		}
    	}
    	return addMsgIfEmpty(floatContentBuffer);
    }
    
    private String getTaskContent(String[] linesInFile, String date){  
    	StringBuffer contentBuffer = new StringBuffer();
    			
    	for(int index = 0; index < linesInFile.length; index++){
    		String line = linesInFile[index].trim(); 
    		String[] lineComponents = line.split(SEMICOLON);
    		if(isUncompleted(TYPE_TASK, lineComponents) && lineComponents[INDEX_DUEDATE].equals(date)){
    			String formatted = String.format(DISPLAY_FORMAT_FLOAT_OR_TASK, index+1, lineComponents[1]);
    			contentBuffer.append(formatted);
    		}
    	}
    	return addMsgIfEmpty(contentBuffer);
    }
    
    private String getOngoingEventContent(String[] linesInFile){ 
    	StringBuffer contentBuffer = new StringBuffer();
    	
    	for(int index = 0; index < linesInFile.length; index++){
    		String line = linesInFile[index].trim(); 
    		String[] lineComponents = line.split(SEMICOLON);
    		if(isOngoingEvent(line)){
    			String formatted = String.format(DISPLAY_FORMAT_EVENT, index+1,
    					lineComponents[INDEX_STARTDATE], lineComponents[INDEX_STARTTIME],
    					lineComponents[INDEX_ENDDATE], lineComponents[INDEX_ENDTIME], lineComponents[INDEX_NAME]);
    			contentBuffer.append(formatted);
    		}
    	}
    	return addMsgIfEmpty(contentBuffer);
    }
    
    private String getEventContent(String[] linesInFile, String date){ 
    	StringBuffer contentBuffer = new StringBuffer();
    	
    	for(int index = 0; index < linesInFile.length; index++){
    		String line = linesInFile[index].trim(); 
    		String[] lineComponents = line.split(SEMICOLON);
    		if(isUncompleted(TYPE_EVENT, lineComponents) && lineComponents[INDEX_STARTDATE].equals(date)){
    			String formatted = String.format(DISPLAY_FORMAT_EVENT, index+1,
    					lineComponents[INDEX_STARTDATE], lineComponents[INDEX_STARTTIME],
    					lineComponents[INDEX_ENDDATE], lineComponents[INDEX_ENDTIME], lineComponents[INDEX_NAME]);
    			contentBuffer.append(formatted);
    		}
    	}
    	return addMsgIfEmpty(contentBuffer);
    }

    private boolean isUncompleted(String type, String[] lineComponents){
    	return lineComponents[INDEX_ISDONE].equals(TODO) && lineComponents[INDEX_TYPE].equals(type); 
    }
     
    private boolean isOngoingEvent(String lineInFile){
    	String line = lineInFile.trim(); 
    	String[] lineComponents = line.split(SEMICOLON);
    	if(lineComponents.length < 6){
    		return false; 
    	}else{
    		String type = lineComponents[INDEX_TYPE];
    		String isDone = lineComponents[INDEX_ISDONE];
        	Date startDate = new Date(lineComponents[INDEX_STARTDATE]);
        	Date endDate = new Date(lineComponents[INDEX_ENDDATE]);
        	Date todayDate = Date.todayDate();
        	
        	return (type.equals(TYPE_EVENT) && isDone.equals(TODO) 
        			&& startDate.compareTo(todayDate) < 0 && endDate.compareTo(todayDate) > 0); 
    	}
    }
    
    private String addMsgIfEmpty(StringBuffer buffer){
    	if(buffer.length() == 0){ 
    		buffer.append(DISPLAY_NO_ITEMS);
    	}
    	return buffer.toString().trim();
    }
}
