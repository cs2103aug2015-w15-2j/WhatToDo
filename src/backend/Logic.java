package backend;

import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.Arrays;

import struct.Command;
import struct.Command.CommandType;
import struct.Command.ViewType;
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

	private static final String MESSAGE_ADD_TASK = "Added task \"%s\" to list. Due on %s.";
	private static final String MESSAGE_ADD_FLOAT_TASK = "Added float \"%s\" to list.";
	private static final String MESSAGE_ADD_EVENT = "Added event \"%s\" to list. Start: %s at %s End: %s at %s.";
	private static final String MESSAGE_DELETE_LINE = "Deleted %s from list.";
	private static final String MESSAGE_EDIT_CONVERSION_INVALID = "Not enough arguments for conversion"; 
	private static final String MESSAGE_MARK_DONE = "Done %s";
	private static final String MESSAGE_REDO = "Redid command: \"%s\"."; 
	private static final String MESSAGE_NO_REDO = "There are no commands to redo.";
	private static final String MESSAGE_UNDO = "Undid command: \"%s\"."; 
	private static final String MESSAGE_NO_UNDO = "There are no commands to undo.";
	private static final String MESSAGE_EXIT = "Exit";
	
	private static final String MESSAGE_ERROR_UNKNOWN = "Unknown error encountered."; 
    private static final String MESSAGE_ERROR_INVALID_COMMAND = "\"%s\" is an invalid command. %s"; 
    private static final String MESSAGE_ERROR_ADD = "Error encountered when adding item. The item's data type is unrecognized."; 
    private static final String MESSAGE_ERROR_UNDO = "Error encountered in memory. Undo will be unavailable for all commands before this.";
    
    private static final String DISPLAY_NO_ITEMS = "There are no items to display.\n"; 
    private static final String DISPLAY_FORMAT_FLOAT_OR_TASK = "%d. %s\n"; 
    private static final String DISPLAY_FORMAT_EVENT = "%d. [%s %s - %s %s] %s\n"; 
    private static final String DISPLAY_FORMAT_DELETED_OR_MARKDONE = "%s \"%s\"";
    private static final String DISPLAY_LAYOUT_DEFAULT_TASK = "TODAY - %s \n%s\nTOMORROW - %s \n%s\nFLOAT\n%s";
    private static final String DISPLAY_LAYOUT_DEFAULT_EVENT = "ONGOING\n%s\nTODAY - %s \n%s\nTOMORROW - %s \n%s";
    private static final String DISPLAY_LAYOUT_ALL_TASK = "%s\nFLOAT\n%s";
    private static final String DISPLAY_LAYOUT_SEARCH_RESULTS = "Showing results for \"%s\"\nTASK\n%s\nFLOAT\n%s\nEVENT\n%s"; 
    
    private static final String KEYWORD_EDIT_NAME = "name";
    private static final String KEYWORD_EDIT_DEADLINE = "date";
    private static final String KEYWORD_EDIT_START_DATE = "startd";
    private static final String KEYWORD_EDIT_START_TIME = "startt";
    private static final String KEYWORD_EDIT_END_DATE = "endd";
    private static final String KEYWORD_EDIT_END_TIME = "endt";

    private static final ArrayList<String> FLOAT_TO_TASK_ATTRIBUTE_LIST = 
    		new ArrayList<String>(Arrays.asList(KEYWORD_EDIT_DEADLINE));
    private static final ArrayList<String> FLOAT_TO_EVENT_ATTRIBUTE_LIST = 
    		new ArrayList<String>(Arrays.asList(KEYWORD_EDIT_START_DATE, KEYWORD_EDIT_START_TIME, 
    				                            KEYWORD_EDIT_END_DATE, KEYWORD_EDIT_END_TIME));
    
    private static final int CONVERSION_NOT_REQ = 0; 
    private static final int CONVERSION_INVALID = -1; 
    private static final int CONVERSION_TO_TASK = 1; 
    private static final int CONVERSION_TO_EVENT = 2; 
    
    private static final String TYPE_FLOAT = "float";
    private static final String TYPE_TASK = "task";
    private static final String TYPE_EVENT = "event";
    
    private static final String TODO = "todo"; 
    
    private static final String NEWLINE = "\n";
    private static final String SEMICOLON = ";";
    
    private CommandParser commandParser; 
    private Storage storage;
    private Memory memory; 
    private Filter filter; 
    private Formatter formatter; 
    //prevCommand refer to the last command that made changes to the file 
    private Command prevCommand; 
 
	//============================================
	// Constructor
	//============================================
    
    public Logic() throws FileSystemException {
			commandParser = new CommandParser();
			storage = new Storage();
			memory = new Memory();
			filter = new Filter(); 
			formatter = new Formatter(); 
			prevCommand = new Command(); 
	}
        
	//============================================
	// Public methods
	//============================================
    
    public CommandType getCommandType(String userInput) {
    	Command command = commandParser.parse(userInput);
    	return command.getCommandType(); 
	}
    
    public ViewType getViewType(String userInput){ 
    	Command command = commandParser.parse(userInput);
    	return command.getViewType();
    }
    
    public String executeCommand(String userInput) {
    	Command command = commandParser.parse(userInput);
    	switch (command.getCommandType()) {
    		case ADD : 
    			return executeAdd(command);
    		case DELETE : 
    			return executeDelete(command); 
    		case EDIT : 
    			return executeEdit(command); 
    		case DONE : 
    			return executeDone(command);
    		case SEARCH :
    			return executeSearch(command); 
    		case UNDO : 
    			return executeUndo(command); 
    		case REDO : 
    			return executeRedo(command);
    		case SAVE : 
    			return executeSave(command);
    		case EXIT :
    			return executeExit(); 
    		case INVALID :
            default :
            	return handleInvalid(command);
    	}	
    }
    
    public String taskDefaultView(){
    	try{
    		String[] linesInFile = getLinesInFile();
        	String floatContent = getAllUncompletedFloat(linesInFile); 
            String todayContent = getTaskContent(linesInFile, Date.todayDateShort()); 
            String tomorrowContent = getTaskContent(linesInFile, Date.tomorrowDateShort()); 
           
            return String.format(DISPLAY_LAYOUT_DEFAULT_TASK, Date.todayDateLong(), todayContent, 
            		Date.tomorrowDateLong(), tomorrowContent, floatContent).trim();
    	}
    	catch(FileSystemException e){
    		return String.format(DISPLAY_LAYOUT_DEFAULT_TASK, e.getMessage(), Date.todayDateLong(), e.getMessage(), 
            		Date.tomorrowDateLong(), e.getMessage()).trim();
    	}
    	catch(Exception e){
    		return String.format(DISPLAY_LAYOUT_DEFAULT_TASK, MESSAGE_ERROR_UNKNOWN, Date.todayDateLong(), MESSAGE_ERROR_UNKNOWN, 
            		Date.tomorrowDateLong(), MESSAGE_ERROR_UNKNOWN).trim();
    	}
    }
    
    public String eventDefaultView(){
    	try{
    		String[] linesInFile = getLinesInFile();
        	String onGoingContent = getOngoingEventContent(linesInFile); 
            String todayContent = getEventContent(linesInFile, Date.todayDateShort()); 
            String tomorrowContent = getEventContent(linesInFile, Date.tomorrowDateShort()); 
            	
            return String.format(DISPLAY_LAYOUT_DEFAULT_EVENT, onGoingContent, Date.todayDateLong(), todayContent, 
            		Date.tomorrowDateLong(), tomorrowContent).trim();
    	}
    	catch(FileSystemException e){
    		return String.format(DISPLAY_LAYOUT_DEFAULT_EVENT, e.getMessage(), Date.todayDateLong(), e.getMessage(), 
            		Date.tomorrowDateLong(), e.getMessage()).trim();
    	}
    	catch (Exception e) {
    		return String.format(DISPLAY_LAYOUT_DEFAULT_EVENT, MESSAGE_ERROR_UNKNOWN, Date.todayDateLong(), MESSAGE_ERROR_UNKNOWN, 
            		Date.tomorrowDateLong(), MESSAGE_ERROR_UNKNOWN).trim();
		}
    }
       
    public String taskAllView(boolean isDone){ 
    	try{ 
    		String[] linesInFile = getLinesInFile();
        	String floatContent = getAllStatus(linesInFile, TYPE_FLOAT, isDone); 
        	String taskContent = getAllStatus(linesInFile,TYPE_TASK, isDone);
        	
        	return String.format(DISPLAY_LAYOUT_ALL_TASK, taskContent, floatContent); 
    	}
    	catch(FileSystemException e){
    		return e.getMessage(); 
    	}
    	catch (Exception e) {
    		return MESSAGE_ERROR_UNKNOWN; 
    	}
    }
    
    public String eventAllView(boolean isDone){ 
    	try{
    		String[] linesInFile = getLinesInFile(); 
    		return getAllStatus(linesInFile, TYPE_EVENT, isDone);
    	}
    	catch(FileSystemException e){
    		return e.getMessage(); 
    	}
    	catch(Exception e){
    		return MESSAGE_ERROR_UNKNOWN;
    	}
    }
        
    public String taskPastUncompletedView(){
    	try{
    		String[] linesInFile = getLinesInFile();
    		ArrayList<Integer> pastTaskIndexes = filter.filterPastUncompleted(linesInFile, TYPE_TASK); 
    		String formattedContent = formatter.formatTask(linesInFile, pastTaskIndexes); 
    		return formattedContent; 
    	}
    	catch(FileSystemException e){ 
    		return e.getMessage();
    	}catch (Exception e) {
			return e.getMessage();
		} 
    }

    public String eventPastUncompletedView(){
    	try{
    		String[] linesInFile = getLinesInFile();
    		ArrayList<Integer> pastEventIndexes = filter.filterPastUncompleted(linesInFile, TYPE_EVENT); 
    		String formattedContent = formatter.formatEvent(linesInFile, pastEventIndexes); 
    		return formattedContent; 
    	}
    	catch(FileSystemException e){ 
    		return e.getMessage();
    	}catch (Exception e) {
			return e.getMessage();
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
        clearRedo(command);
        return formFeedbackMsg(addFeedback, isSaved);
    }
    
    private String executeAddTask(Command command) throws FileSystemException{
    	State stateBeforeExecutingCommand = getState(command);
    		
    	String taskName = command.getName(); 
        Date taskDeadline = command.getDueDate();
        Task task = new Task(taskName, false, taskDeadline);
        storage.addTask(task);
        String addFeedback = String.format(MESSAGE_ADD_TASK, taskName, 
        		taskDeadline.formatDateLong()); 

        boolean isSaved = saveState(stateBeforeExecutingCommand);
        clearRedo(command);
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
        		eventStartDate.formatDateLong(), eventStartTime, 
        		eventEndDate.formatDateLong(), eventEndTime);
        
        boolean isSaved = saveState(stateBeforeExecutingCommand);
        clearRedo(command);
        return formFeedbackMsg(addFeedback, isSaved);
    }
   
    private String executeDelete(Command command){
    	try{
    		State stateBeforeExecutingCommand = getState(command);
    		
    		int lineNumber = command.getIndex(); 
        	String deletedLine = storage.deleteLine(lineNumber); 
        	String deleteFeedback = String.format(MESSAGE_DELETE_LINE, formatLine(deletedLine)); 
        	
        	boolean isSaved = saveState(stateBeforeExecutingCommand);
        	clearRedo(command);
        	return formFeedbackMsg(deleteFeedback, isSaved);
    	}
    	catch(FileSystemException e){
    		return e.getMessage();
    	}
    	catch(Exception e){
    		return MESSAGE_ERROR_UNKNOWN;
    	}
    }
   
    private int getConversionStatus(Command command) throws FileSystemException{ 
		ArrayList<String> editList = command.getEditList();
		int lineNumber = command.getIndex(); 
    	String lineType = storage.findTypeInLine(lineNumber); 
        	
    	if(containNameOnly(editList) || isTaskOrEvent(lineType)){
    		return CONVERSION_NOT_REQ; 
    	}
    	else if(lineType.equals(TYPE_FLOAT) && editList.containsAll(FLOAT_TO_TASK_ATTRIBUTE_LIST)){ 
    		return CONVERSION_TO_TASK; 
    	}
    	else if(lineType.equals(TYPE_FLOAT) && editList.containsAll(FLOAT_TO_EVENT_ATTRIBUTE_LIST)){
    		return CONVERSION_TO_EVENT; 
    	}
    	else{ 
    		return CONVERSION_INVALID;
    	}    	 
    }
    
    private boolean containNameOnly(ArrayList<String> editList){ 
    	return editList.size() == 1 && editList.contains(KEYWORD_EDIT_NAME); 
    }
    
    private boolean isTaskOrEvent(String lineType){ 
    	return lineType.equals(TYPE_TASK) || lineType.equals(TYPE_EVENT); 
    }
        
    //TODO edit 
    private String executeEdit(Command command){
    	try{     		
    		switch(getConversionStatus(command)){  
    			case CONVERSION_TO_TASK : 
    				return executeEditTaskConversion(command);  
    			case CONVERSION_TO_EVENT : 
    				return executeEditEventConversion(command);  
    			case CONVERSION_NOT_REQ : 
    				return executeEditNoConversion(command); 
    			case CONVERSION_INVALID :
    			default : 
    				return MESSAGE_EDIT_CONVERSION_INVALID;  
    		}
    	}
    	catch(FileSystemException e){
    		return e.getMessage();
    	}
    	catch(Exception e){
    		return MESSAGE_ERROR_UNKNOWN; 
    	}
    }
    
    private String executeEditTaskConversion(Command command) throws FileSystemException{ 
    	int lineNumber = command.getIndex(); 
    	Date deadline = command.getDueDate(); 
    	ArrayList<String> editList = command.getEditList(); 
    	
    	//must edit name if required first
    	if(editList.contains(KEYWORD_EDIT_NAME)){ 
    		String newName = command.getName();  
    		storage.editName(lineNumber, newName); 
    	}
    	storage.convertFloatToTask(lineNumber, deadline); 
    	
    	//TODO - fb msg
    	return "converted float to task";  
    }
    
    private String executeEditEventConversion(Command command) throws FileSystemException{ 
    	int lineNumber = command.getIndex(); 
    	Date startDate = command.getStartDate(); 
    	Date endDate = command.getEndDate(); 
    	String startTime = command.getStartTime(); 
    	String endTime = command.getEndTime(); 
    	ArrayList<String> editList = command.getEditList(); 
    	
    	//must edit name if required first 
    	if(editList.contains(KEYWORD_EDIT_NAME)){ 
    		String newName = command.getName();  
    		storage.editName(lineNumber, newName); 
    	}
    	storage.convertFloatToEvent(lineNumber, startDate, startTime, endDate, endTime); 
    	
    	//TODO - fb msg
    	return "converted float to event";  
    }

    //TODO - need to make it work for multiple edits at the same time 
    //     - currently only works for single edit 
	private String executeEditNoConversion(Command command)	throws FileSystemException {
		int lineNumber = command.getIndex(); 
		ArrayList<String> editList = command.getEditList(); 
		
		for(String editItem : editList){ 
			switch (editItem) {
				case KEYWORD_EDIT_NAME :
					String newName = command.getName(); 
					storage.editName(lineNumber, newName); 
					break;
				case KEYWORD_EDIT_DEADLINE :
					Date newDeadline = command.getDueDate(); 
					storage.editTaskDeadline(lineNumber, newDeadline); 
					break;
				case KEYWORD_EDIT_START_DATE : 
					Date newStartDate = command.getStartDate(); 
					storage.editEventStartDate(lineNumber, newStartDate); 
					break; 
				case KEYWORD_EDIT_START_TIME : 
					String newStartTime = command.getStartTime(); 
					storage.editEventStartTime(lineNumber, newStartTime); 
					break; 
				case KEYWORD_EDIT_END_DATE : 
					Date newEndDate = command.getEndDate();
					storage.editEventEndDate(lineNumber, newEndDate); 
					break; 
				case KEYWORD_EDIT_END_TIME : 
					String newEndTime = command.getEndTime(); 
					storage.editEventEndTime(lineNumber, newEndTime); 
					break; 
				default :
					break;
			}
		}
		
		//TODO - fb msg
		return "no conversion - edited fields only"; 
	}
    
    private String executeDone(Command command){
    	try{
    		State stateBeforeExecutingCommand = getState(command);
    		
    		int lineNumber = command.getIndex(); 
        	String doneLine = storage.markAsDone(lineNumber); 
        	String markDoneFeedback = String.format(MESSAGE_MARK_DONE, formatLine(doneLine)); 
        	
        	boolean isSaved = saveState(stateBeforeExecutingCommand);
        	clearRedo(command);
        	return formFeedbackMsg(markDoneFeedback, isSaved);
    	}
    	catch(FileSystemException e){
    		return e.getMessage();
    	}
    	catch(Exception e){
    		return MESSAGE_ERROR_UNKNOWN;
    	}
    }
     
    private String executeSearch(Command command){ 
    	try{
    		String[] linesInFile = getLinesInFile();
    		String query = command.getName(); 
    		
    		String floatResult = formattedSearchResult(linesInFile, TYPE_FLOAT, query); 
    		String taskResult = formattedSearchResult(linesInFile, TYPE_TASK, query); 
    		String eventResult = formattedSearchResult(linesInFile, TYPE_EVENT, query); 
    		
    		return String.format(DISPLAY_LAYOUT_SEARCH_RESULTS, query, taskResult, floatResult, eventResult);
    	}
    	catch(FileSystemException e){
    		//TODO REFACTOR command.getName()
    		return String.format(DISPLAY_LAYOUT_SEARCH_RESULTS, command.getName(), e.getMessage(), e.getMessage(), e.getMessage());
    	}
    	catch(Exception e){ 
    		//TODO REFACTOR command.getName()
    		return String.format(DISPLAY_LAYOUT_SEARCH_RESULTS, command.getName(), MESSAGE_ERROR_UNKNOWN, MESSAGE_ERROR_UNKNOWN, MESSAGE_ERROR_UNKNOWN);
    	}
    	
    }
        
    //TODO REFACTOR - considering putting all the formatting into formatter class 
    private String formattedSearchResult(String[] linesInFile, String type, String query){
    	ArrayList<Integer> result = filter.matchTokensInQuery(linesInFile, type, query); 
    	switch(type){
    		case TYPE_FLOAT : 
    			return formatter.formatFloatOrTaskWithoutHeaders(linesInFile, result, true);
    		case TYPE_TASK : 
    			return formatter.formatTaskResults(linesInFile, result); 
    		case TYPE_EVENT : 
    			return formatter.formatEventResults(linesInFile, result); 
    		default : 
    			return ""; 
    	}
    }
        
    private String executeUndo(Command command){ 
    	try{
    		String currFileContents = storage.display();
    		State stateAfterUndo = memory.getUndoState(currFileContents);
    		
    		if(stateAfterUndo == null){
        		return MESSAGE_NO_UNDO; 
        	}
    		
    		storage.overwriteFile(stateAfterUndo.getFileContents());
    		prevCommand = command; 
        	return String.format(MESSAGE_UNDO, stateAfterUndo.getUserCommand());
    	}
    	catch(FileSystemException e){
    		return e.getMessage();
    	}
    	catch(Exception e){
    		return MESSAGE_ERROR_UNKNOWN;
    	}
    }
    
    private String executeRedo(Command command){ 
    	try{
    		String currFileContents = storage.display();
        	State stateAfterRedo = memory.getRedoState(currFileContents);
        	
        	if(stateAfterRedo == null){
        		return MESSAGE_NO_REDO; 
        	}
        	
        	storage.overwriteFile(stateAfterRedo.getFileContents());
        	prevCommand = command;
        	return String.format(MESSAGE_REDO, stateAfterRedo.getUserCommand());
    	}
    	catch(FileSystemException e){
    		return e.getMessage();
    	}
    	catch(Exception e){
    		return MESSAGE_ERROR_UNKNOWN;
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
    
    private void clearRedo(Command command){
    	if(prevCommand.isUndoOrRedo() && !command.isUndoOrRedo()){
    		memory.clearRedoStack();
    	}
    	prevCommand = command; 
    }
   	
	private String formFeedbackMsg(String addFeedback, boolean isSaved) {
		if(isSaved){
        	return addFeedback;
        }
        else{
        	return addFeedback + MESSAGE_ERROR_UNDO;
        }
	}
	
	private String formatLine(String line){
		String[] lineComponents = line.split(SEMICOLON);
		String type = lineComponents[INDEX_TYPE]; 
		String name = lineComponents[INDEX_NAME];
		return String.format(DISPLAY_FORMAT_DELETED_OR_MARKDONE, type, name); 
	}
	
	private String executeSave(Command command) {
		String newFilePath = command.getName(); 
		return storage.changeFileStorageLocation(newFilePath); 
	}
	
    private String executeExit(){
    	return MESSAGE_EXIT; 
    }
    
    private String handleInvalid(Command command){ 
    	String userInput = command.getUserInput(); 
    	String reason = (command.getName() != null)? command.getName() : "";
    	return String.format(MESSAGE_ERROR_INVALID_COMMAND, userInput, reason); 
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
    
    private String getAllUncompletedFloat(String[] linesInFile){ 
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
    			String formatted = String.format(DISPLAY_FORMAT_FLOAT_OR_TASK, index+1, lineComponents[INDEX_NAME]);
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
    
    //============================================
  	// Private methods for allView
  	//============================================
    
    private String getAllStatus(String[] linesInFile ,String type, boolean isDone){
    	ArrayList<Integer> result = filter.filterStatus(linesInFile, type, isDone); 
    	//TODO REFACTOR THIS SHIT
    	switch(type){ 
    		case TYPE_FLOAT : 
    			return formatter.formatFloatOrTaskWithoutHeaders(linesInFile, result,false); 
    		case TYPE_TASK : 
    			return formatter.formatTask(linesInFile, result); 
    		case TYPE_EVENT : 
    			return formatter.formatEvent(linesInFile, result); 
    		default : 
    			return ""; 
    	}
    }
       
}
