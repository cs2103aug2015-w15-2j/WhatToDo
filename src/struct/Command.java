package struct;

public class Command {
	
	public enum CommandType {
		ADD, DELETE, EDIT, SEARCH, EXIT, INVALID
    }
	
	public enum DataType { 
		TASK, FLOATING_TASK, EVENT, ALL
	}
	
	private CommandType commandType;
	private DataType dataType;
	//name may contain arguments for commands instead of event or task name
	private String name;
	private int index;
	private Date dueDate; 
	private Date startDate;
	private Date endDate; 
	private String startTime; 
	private String endTime;
	
	//============================================
	// Constructors
	//============================================
	
	public Command(CommandType commandType) {
		this.commandType = commandType;
	}
	
	public Command(CommandType commandType, DataType dataType, String name, int index, Date dueDate, Date startDate, 
			Date endDate, String startTime, String endTime) {
		this.commandType = commandType;
		this.dataType = dataType;
		this.name = name;
		this.index = index;
		this.dueDate = dueDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	//============================================
	// Getters
	//============================================

	public CommandType getCommandType() {
		return commandType;
	}

	public DataType getDataType() {
		return dataType;
	}

	public String getName() {
		return name;
	}
	
	public int getIndex(){
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
	
	//============================================
	// Setters
	//============================================

	public void setCommandType(CommandType commandType) {
		this.commandType = commandType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
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
}
