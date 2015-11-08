package backend;

import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;

import struct.Command;
import struct.Command.CommandType;
import struct.Command.DataType;
import struct.Command.ViewType;
import struct.Date;
import struct.Event;
import struct.FloatingTask;
import struct.State;
import struct.Task;

//@@author A0127051U
public class Logic {
	
    private static final int CONVERSION_NOT_REQ = 0; 
    private static final int CONVERSION_INVALID = -1; 
    private static final int CONVERSION_TO_TASK = 1; 
    private static final int CONVERSION_TO_EVENT = 2; 
	
	private static final int INDEX_COMMAND = 0;
	private static final int INDEX_ALIAS = 0; 
	private static final int INDEX_MEANING = 1; 

	private static final int INDEX_TYPE = 0; 
	private static final int INDEX_NAME = 1; 
	private static final int INDEX_ISDONE = 2; 
	private static final int INDEX_DUEDATE = 3;
	private static final int INDEX_STARTDATE = 3; 
	private static final int INDEX_STARTTIME = 4; 
	private static final int INDEX_ENDDATE = 5; 
	private static final int INDEX_ENDTIME = 6; 
		
    private static final String COMMAND_FORMAT_ADD_TASK = "add %s by %s"; 
    private static final String COMMAND_FORMAT_ADD_EVENT = "add %s from %s %s to %s %s";

	private static final String MESSAGE_ADD_TASK = "Added task \"%s\" to list. Due on %s.";
	private static final String MESSAGE_ADD_FLOAT_TASK = "Added float \"%s\" to list.";
	private static final String MESSAGE_ADD_EVENT = "Added event \"%s\" to list. Start: %s at %s End: %s at %s.";
	private static final String MESSAGE_DELETE_ITEM = "Deleted %s from list.";
	private static final String MESSAGE_EDIT = "Edited %s \"%s\".";
	private static final String MESSAGE_EDIT_CONVERSION = "Converted float \"%s\" to %s \"%s\".";
	private static final String MESSAGE_MARK_DONE = "Done %s.";
	private static final String MESSAGE_REDO = "Redid a \"%s\" command."; 
	private static final String MESSAGE_NO_REDO = "There are no commands to redo.";
	private static final String MESSAGE_UNDO = "Undid a \"%s\" command."; 
	private static final String MESSAGE_NO_UNDO = "There are no commands to undo.";
	private static final String MESSAGE_SET = "Set new alias \"%s\" for \"%s\"."; 
	private static final String MESSAGE_DELETE_ALIAS = "Deleted alias \"%s\"."; 
	
	private static final String MESSAGE_ERROR_UNKNOWN = "Unknown error encountered."; 
    private static final String MESSAGE_ERROR_INVALID_COMMAND = "\"%s\" is an invalid command. %s"; 
    private static final String MESSAGE_ERROR_ADD = "Error encountered when adding item. The item's data type is unrecognized."; 
    private static final String MESSAGE_ERROR_EDIT = "Error encountered when editing item."; 
	private static final String MESSAGE_ERROR_EDIT_INSUFFICIENT_ARGS = "Not enough arguments for conversion."; 
    private static final String MESSAGE_ERROR_EDIT_INVALID_CONVERSION = "A %s cannot be converted to a %s.";
    private static final String MESSAGE_ERROR_EDIT_INVALID_EDIT = "Invalid edit. %s"; 
    private static final String MESSAGE_ERROR_UNDO = "Error encountered in memory. Undo will be unavailable for all commands before this.";
    
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

    private static final ArrayList<String> ATTRIBUTE_LIST_FLOAT_TO_TASK = 
    		new ArrayList<String>(Arrays.asList(KEYWORD_EDIT_DEADLINE));
    private static final ArrayList<String> ATTRIBUTE_LIST_FLOAT_TO_EVENT = 
    		new ArrayList<String>(Arrays.asList(KEYWORD_EDIT_START_DATE, KEYWORD_EDIT_START_TIME, 
    				                            KEYWORD_EDIT_END_DATE, KEYWORD_EDIT_END_TIME));
       
    private static final String TYPE_FLOAT = "float";
    private static final String TYPE_TASK = "task";
    private static final String TYPE_EVENT = "event";
    
    private static final String DONE = "done"; 
    
    private static final String NEWLINE = "\n";
    private static final String SEMICOLON = ";";
    private static final String EMPTYSTRING = "";
    private static final String REGEX_WHITESPACES = "[\\s;]+"; 
    
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
		//storage must be initialized before createAliasHashtable()
    	storage = new Storage();
   		Hashtable<String, String> aliasHashtable = createAliasHashtable(); 
		commandParser = new CommandParser(aliasHashtable);
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
    		case SET : 
    			return executeSet(command); 
    		case DELETEALIAS : 
    			return executeDeleteAlias(command); 
    		case SAVE : 
    			return executeSave(command);
    		case INVALID :
            default :
            	return handleInvalid(command);
    	}	
    }
 
    public String taskDefaultView(){
    	try{
    		String[] linesInFile = getLinesInFile();
        	String floatContent = getAllStatus(linesInFile, TYPE_FLOAT, false); 
            String todayContent = getDateContent(linesInFile, TYPE_TASK, Date.todayDate()); 
            String tomorrowContent = getDateContent(linesInFile, TYPE_TASK, Date.tomorrowDate());
           
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
            String todayContent = getDateContent(linesInFile, TYPE_EVENT, Date.todayDate());
            String tomorrowContent = getDateContent(linesInFile, TYPE_EVENT, Date.tomorrowDate()); 
            	
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
    		String formattedContent = formatter.formatTaskWithHeaders(linesInFile, pastTaskIndexes, false); 
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
    		String formattedContent = formatter.formatEventWithHeaders(linesInFile, pastEventIndexes, false); 
    		return formattedContent; 
    	}
    	catch(FileSystemException e){ 
    		return e.getMessage();
    	}catch (Exception e) {
			return e.getMessage();
		} 
    }
    
    public String getAliasFileContents() throws FileSystemException{
    	return storage.readAliasFile(); 
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
	// Private methods for constructor 
	//============================================
    
    //TODO - consider returning a message if there is prob reading alias.txt 
    private Hashtable<String, String> createAliasHashtable(){
    	Hashtable<String, String> aliasHashtable = new Hashtable<String, String>();
    	
    	try{ 
    		String aliasFileContents = storage.readAliasFile();
    		String[] lineInAliasFile = aliasFileContents.split(NEWLINE); 
    		for(int i = 0; i < lineInAliasFile.length; i++){
    			String[] lineFields = lineInAliasFile[i].split(SEMICOLON);
    			String alias = lineFields[INDEX_ALIAS];
    			String meaning = lineFields[INDEX_MEANING];
    			
    			aliasHashtable.put(alias, meaning); 
    		}
    	}
    	catch(Exception e){
    		
    	}
    	
    	return aliasHashtable; 
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
        	String deleteFeedback = String.format(MESSAGE_DELETE_ITEM, formatLine(deletedLine)); 
        	
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

    private String executeEdit(Command command){
    	try{     		
    		switch(getConversionStatus(command)){  
    			case CONVERSION_TO_TASK : 
    				return executeEditConvertToTask(command);  
    			case CONVERSION_TO_EVENT : 
    				return executeEditConvertToEvent(command);  
    			case CONVERSION_NOT_REQ : 
    				return executeEditNoConversion(command); 
    			case CONVERSION_INVALID :
    			default : 
    				return MESSAGE_ERROR_EDIT_INSUFFICIENT_ARGS;  
    		}
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
    	else if(lineType.equals(TYPE_FLOAT) && editList.containsAll(ATTRIBUTE_LIST_FLOAT_TO_TASK)){ 
    		return CONVERSION_TO_TASK; 
    	}
    	else if(lineType.equals(TYPE_FLOAT) && editList.containsAll(ATTRIBUTE_LIST_FLOAT_TO_EVENT)){
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
       
    private String executeEditConvertToTask(Command command) throws FileSystemException{
    	State stateBeforeExecutingCommand = getState(command);
    	
    	ArrayList<String> editList = command.getEditList(); 
    	int lineNumber = command.getIndex(); 
    	String oldName = storage.getAttribute(lineNumber, INDEX_NAME);
    	String name = (editList.contains(KEYWORD_EDIT_NAME)) ? command.getName() : oldName;  
    	Date deadline = command.getDueDate(); 
    	String deadlineStr = deadline.formatDateShort(); 
    	String isDoneStr = storage.getAttribute(lineNumber, INDEX_ISDONE);
    	Boolean isDone = (isDoneStr.equals(DONE)) ? true : false; 
    
    	String addTaskCmdString = String.format(COMMAND_FORMAT_ADD_TASK, name, deadlineStr);    	
    	Command addTaskCmd = commandParser.parse(addTaskCmdString); 
    	if(addTaskCmd.getCommandType() == CommandType.ADD && addTaskCmd.getDataType() == DataType.TASK){ 
    		storage.deleteLine(lineNumber); 
    		Task convertedTask = new Task(name, isDone, deadline); 
    		storage.addTask(convertedTask);
    		
    		String editFeedback = String.format(MESSAGE_EDIT_CONVERSION, oldName, TYPE_TASK, name);
    		
    		boolean isSaved = saveState(stateBeforeExecutingCommand);
            clearRedo(command);
            return formFeedbackMsg(editFeedback, isSaved);
    	}else{ 
    		//assert cmdtype == invalid 
    		String reason = (addTaskCmd.getName() != null)? addTaskCmd.getName() : EMPTYSTRING;
    		return String.format(MESSAGE_ERROR_EDIT_INVALID_EDIT, reason);  
    	}	  
    }
    
    private String executeEditConvertToEvent(Command command) throws FileSystemException{ 
    	State stateBeforeExecutingCommand = getState(command);
    	
    	ArrayList<String> editList = command.getEditList(); 
    	int lineNumber = command.getIndex(); 
    	String oldName = storage.getAttribute(lineNumber, INDEX_NAME); 
    	String name = (editList.contains(KEYWORD_EDIT_NAME)) ? command.getName() : oldName; 
    	Date startDate = command.getStartDate(); 
    	String startDateStr = startDate.formatDateShort(); 
    	Date endDate = command.getEndDate(); 
    	String endDateStr = endDate.formatDateShort(); 
    	String startTime = command.getStartTime(); 
    	String endTime = command.getEndTime(); 
    	String isDoneStr = storage.getAttribute(lineNumber, INDEX_ISDONE);
    	Boolean isDone = (isDoneStr.equals(DONE)) ? true : false; 
    	
    	String addEventCmdString = String.format(COMMAND_FORMAT_ADD_EVENT, name, startDateStr, startTime, endDateStr, endTime);  
    	Command addEventCmd = commandParser.parse(addEventCmdString); 
    	if(addEventCmd.getCommandType() == CommandType.ADD && addEventCmd.getDataType() == DataType.EVENT){ 
    		storage.deleteLine(lineNumber); 
    		Event convertedEvent = new Event(name, isDone, startDate, endDate, startTime, endTime); 
    		storage.addEvent(convertedEvent);
    		
    		String editFeedback = String.format(MESSAGE_EDIT_CONVERSION, oldName, TYPE_EVENT, name);
    		
    		boolean isSaved = saveState(stateBeforeExecutingCommand);
            clearRedo(command);
            return formFeedbackMsg(editFeedback, isSaved);
    	}else{ 
    		//assert cmdtype == invalid 
    		String reason = (addEventCmd.getName() != null)? addEventCmd.getName() : EMPTYSTRING;
    		return String.format(MESSAGE_ERROR_EDIT_INVALID_EDIT, reason);  
    	}	
    }

	private String executeEditNoConversion(Command command)	throws FileSystemException {
		int lineNumber = command.getIndex(); 
		String lineType = storage.getAttribute(lineNumber, INDEX_TYPE); 
		switch(lineType){ 
			case TYPE_FLOAT :
				return executeEditFloat(command); 
			case TYPE_TASK : 
				return executeEditTask(command); 
			case TYPE_EVENT : 
				return executeEditEvent(command); 
			default:
				return MESSAGE_ERROR_EDIT; 
		}
	}

	private String executeEditFloat(Command command) throws FileSystemException{
		State stateBeforeExecutingCommand = getState(command);
		
		int lineNumber = command.getIndex();
		String newName = command.getName(); 
		String isDoneStr = storage.getAttribute(lineNumber, INDEX_ISDONE); 
		boolean isDone = (isDoneStr.equals(DONE))? true : false;  
		FloatingTask newFloatingTask = new FloatingTask(newName,isDone); 
		
		storage.deleteLine(lineNumber); 
		storage.addFloatingTask(newFloatingTask);
		String editFeedback = String.format(MESSAGE_EDIT, TYPE_FLOAT, newName);
	
		boolean isSaved = saveState(stateBeforeExecutingCommand);
        clearRedo(command);
        return formFeedbackMsg(editFeedback, isSaved);
	}
	
	private String executeEditTask(Command command) throws FileSystemException {
		State stateBeforeExecutingCommand = getState(command);
		
		ArrayList<String> editList = command.getEditList(); 
		
		//assert editList is not null 
		if(Collections.disjoint(editList, ATTRIBUTE_LIST_FLOAT_TO_EVENT)){ 
			int lineNumber = command.getIndex();
			//TODO refactor this shit 
			String newName = (editList.contains(KEYWORD_EDIT_NAME)) ? command.getName() : storage.getAttribute(lineNumber, INDEX_NAME); 
			Date newDeadline =  (editList.contains(KEYWORD_EDIT_DEADLINE)) ? command.getDueDate() : new Date(storage.getAttribute(lineNumber, INDEX_DUEDATE));
			String newDeadlineStr = newDeadline.formatDateShort(); 
			String isDoneStr = storage.getAttribute(lineNumber, INDEX_ISDONE); 
			boolean isDone = (isDoneStr.equals(DONE))? true : false;  
			
			String addEditedTaskCmdStr = String.format(COMMAND_FORMAT_ADD_TASK, newName, newDeadlineStr);    	
	    	Command addEditedTaskCmd = commandParser.parse(addEditedTaskCmdStr); 
	    	if(addEditedTaskCmd.getCommandType() == CommandType.ADD && addEditedTaskCmd.getDataType() == DataType.TASK){ 
	    		storage.deleteLine(lineNumber); 
	    		Task editedTask = new Task(newName, isDone, newDeadline); 
	    		storage.addTask(editedTask);
	    		String editFeedback = String.format(MESSAGE_EDIT, TYPE_TASK, newName);
	    		
	    		boolean isSaved = saveState(stateBeforeExecutingCommand);
	            clearRedo(command);
	            return formFeedbackMsg(editFeedback, isSaved);
	    	}else{ 
        		//assert cmdtype == invalid 
        		String reason = (addEditedTaskCmd.getName() != null)? addEditedTaskCmd.getName() : EMPTYSTRING;
        		return String.format(MESSAGE_ERROR_EDIT_INVALID_EDIT, reason); 
	    	}
		}
		else{ 
			return String.format(MESSAGE_ERROR_EDIT_INVALID_CONVERSION, TYPE_TASK, TYPE_EVENT); 
		}
	}
	
    private String executeEditEvent(Command command) throws FileSystemException {
    	State stateBeforeExecutingCommand = getState(command);
    	
    	ArrayList<String> editList = command.getEditList(); 
    	
    	//assert editList is not null 
    	if(Collections.disjoint(editList, ATTRIBUTE_LIST_FLOAT_TO_TASK)){ 
    		int lineNumber = command.getIndex();
    		//TODO refactor this shit 
    		String newName = (editList.contains(KEYWORD_EDIT_NAME)) ? command.getName() : storage.getAttribute(lineNumber, INDEX_NAME); 
    		Date newStartDate = (editList.contains(KEYWORD_EDIT_START_DATE)) ? command.getStartDate() : new Date(storage.getAttribute(lineNumber, INDEX_STARTDATE));
    		String newStartDateStr = newStartDate.formatDateShort();
    		Date newEndDate = (editList.contains(KEYWORD_EDIT_END_DATE)) ? command.getEndDate() : new Date(storage.getAttribute(lineNumber, INDEX_ENDDATE));
    		String newEndDateStr = newEndDate.formatDateShort(); 
    		String newStartTime = (editList.contains(KEYWORD_EDIT_START_TIME)) ? command.getStartTime() : storage.getAttribute(lineNumber, INDEX_STARTTIME);
    		String newEndTime = (editList.contains(KEYWORD_EDIT_END_TIME)) ? command.getEndTime() : storage.getAttribute(lineNumber, INDEX_ENDTIME);
    		String isDoneStr = storage.getAttribute(lineNumber, INDEX_ISDONE); 
			boolean isDone = (isDoneStr.equals(DONE))? true : false;  
    		
    		String addEditedEventCmdString = String.format(COMMAND_FORMAT_ADD_EVENT, newName, newStartDateStr, newStartTime, newEndDateStr, newEndTime);  
        	Command addEditedEventCmd = commandParser.parse(addEditedEventCmdString); 
        	if(addEditedEventCmd.getCommandType() == CommandType.ADD && addEditedEventCmd.getDataType() == DataType.EVENT){ 
        		storage.deleteLine(lineNumber); 
        		Event editedEvent = new Event(newName, isDone, newStartDate, newEndDate, newStartTime, newEndTime); 
        		storage.addEvent(editedEvent);
        		String editFeedback = String.format(MESSAGE_EDIT, TYPE_EVENT, newName);
        		
        		boolean isSaved = saveState(stateBeforeExecutingCommand);
                clearRedo(command);
                return formFeedbackMsg(editFeedback, isSaved);
        	}else{ 
        		//assert cmdtype == invalid 
        		String reason = (addEditedEventCmd.getName() != null)? addEditedEventCmd.getName() : EMPTYSTRING;
        		return String.format(MESSAGE_ERROR_EDIT_INVALID_EDIT, reason); 
        	}	
    	}
    	else{ 
			return String.format(MESSAGE_ERROR_EDIT_INVALID_CONVERSION, TYPE_EVENT, TYPE_TASK);
    	}
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
    			return formatter.formatTaskWithHeaders(linesInFile, result, true);
    		case TYPE_EVENT : 
    			return formatter.formatEventWithHeaders(linesInFile, result, true); 
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
    		String userCmdUndid = stateAfterUndo.getUserCommand(); 
        	return String.format(MESSAGE_UNDO, getCommandStr(userCmdUndid));
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
        	String userCmdRedid = stateAfterRedo.getUserCommand(); 
        	return String.format(MESSAGE_REDO, getCommandStr(userCmdRedid));
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
   	
	private String formFeedbackMsg(String cmdFeedback, boolean isSaved) {
		if(isSaved){
        	return cmdFeedback;
        }
        else{
        	return cmdFeedback + MESSAGE_ERROR_UNDO;
        }
	}

	private String getCommandStr(String userString){
		String[] lineComponents = userString.split(REGEX_WHITESPACES);
		return lineComponents[INDEX_COMMAND]; 
	}
	
	private String formatLine(String line){
		String[] lineComponents = line.split(SEMICOLON);
		String type = lineComponents[INDEX_TYPE]; 
		String name = lineComponents[INDEX_NAME];
		return String.format(DISPLAY_FORMAT_DELETED_OR_MARKDONE, type, name); 
	}
	
	private String executeSet(Command command){ 
		try{
			String newAlias = command.getName(); 
			String oldAlias = command.getOriginalCommand();
			storage.addToAliasFile(newAlias, oldAlias);
			commandParser.setAlias(newAlias, oldAlias);
			return String.format(MESSAGE_SET, newAlias, oldAlias);
		}
		catch(FileSystemException e){
			return e.getMessage(); 
		}
		catch (Exception e) {
			return MESSAGE_ERROR_UNKNOWN; 
		} 
	}
	
	private String executeDeleteAlias(Command command){ 
		try{
			String alias = command.getName(); 
			storage.deleteFromAliasFile(alias);
			commandParser.deleteAliasFromHash(alias);
			return String.format(MESSAGE_DELETE_ALIAS, alias);
		}
		catch(FileSystemException e){
			return e.getMessage(); 
		}
		catch (Exception e) {
			return MESSAGE_ERROR_UNKNOWN; 
		}
		 
	}
	
	private String executeSave(Command command) {
		String newFilePath = command.getName(); 
		return storage.changeFileStorageLocation(newFilePath); 
	}
    
    private String handleInvalid(Command command){ 
    	String userInput = command.getUserInput(); 
    	String reason = (command.getName() != null)? command.getName() : EMPTYSTRING;
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
    
    private String getDateContent(String[] linesInFile ,String type, Date date){
    	ArrayList<Integer> result = filter.filterDate(linesInFile, type, date); 
    	//TODO REFACTOR THIS SHIT
    	switch(type){  
    		case TYPE_TASK : 
    			return formatter.formatFloatOrTaskWithoutHeaders(linesInFile, result, false); 
    		case TYPE_EVENT : 
    			return formatter.formatEventWithoutHeaders(linesInFile, result); 
    		default : 
    			return ""; 
    	}
    }
    
    private String getOngoingEventContent(String[] linesInFile){ 
    	ArrayList<Integer> ongoingEventIndexes = filter.filterOngoingEvents(linesInFile);
    	String formattedList = formatter.formatEventWithoutHeaders(linesInFile, ongoingEventIndexes);
    	
    	return formattedList; 
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
    			return formatter.formatTaskWithHeaders(linesInFile, result, false);
    		case TYPE_EVENT : 
    			return formatter.formatEventWithHeaders(linesInFile, result, false); 
    		default : 
    			return ""; 
    	}
    }
    
    //============================================
  	// Public method used in testing only
  	//============================================
    
    public void overwriteFile(String textToWrite) throws FileSystemException{ 
    	storage.overwriteFile(textToWrite);
    }
       
}
