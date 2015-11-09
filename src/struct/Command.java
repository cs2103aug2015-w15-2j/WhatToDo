package struct;

import java.util.ArrayList;

//@@author A0124099B
public class Command {
	
	public enum CommandType {
		ADD, DELETE, DELETEALIAS, EDIT, SEARCH, DONE, SET, SAVE, UNDO, REDO, VIEW, EXIT, INVALID
    }
	
	public enum DataType { 
		TASK, FLOATING_TASK, EVENT
	}
	
	public enum ViewType {
		ALL, DEF, HIST, UNRES, SEARCH, HELP, DONE, OPENFILE, CONFIG
	}
	
	private String userInput; 
	private CommandType commandType;
	private DataType dataType;
	private ViewType viewType;
	//name may contain arguments for commands instead of event or task name
	private String name;
	private int index;
	private Date dueDate; 
	private Date startDate;
	private Date endDate; 
	private String startTime; 
	private String endTime;
	private ArrayList<String> editList;
	private String originalCommand;
	
	//============================================
	// Constructors
	//============================================
	
	public Command() {
		
	}
	
	public Command(CommandType commandType) {
		this.commandType = commandType;
	}
	
	//============================================
	// Public Methods
	//============================================	
	
	public boolean isUndoOrRedo() {
		return this.commandType == CommandType.UNDO || this.commandType == CommandType.REDO;
	}
	
	//============================================
	// Getters
	//============================================
	
	public String getUserInput() {
		return userInput;
	}

	public CommandType getCommandType() {
		return commandType;
	}

	public DataType getDataType() {
		return dataType;
	}
	
	public ViewType getViewType() {
		return viewType;
	}

	public String getName() {
		return name;
	}
	
	public int getIndex() {
		return index;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}
	
	public ArrayList<String> getEditList() {
		return editList;
	}
	
	public String getOriginalCommand() {
		return originalCommand;
	}
	
	//============================================
	// Setters
	//============================================
	
	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}

	public void setCommandType(CommandType commandType) {
		this.commandType = commandType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	
	public void setViewType(ViewType viewType) {
		this.viewType = viewType;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public void setEditList(ArrayList<String> editList) {
		this.editList = editList;
	}
	
	public void setOriginalCommand(String originalCommand) {
		this.originalCommand = originalCommand;
	}
}
