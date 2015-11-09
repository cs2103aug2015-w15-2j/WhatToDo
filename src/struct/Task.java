/**
 * This class defines the Task object and its methods used in the application
 *
 * @@author A0127051U
 */

package struct;

public class Task extends TodoItem implements Comparable<Task>{

	private static final String SEMICOLON = ";";
	
	private static final String FORMAT_TO_STRING = "task;%s;%s;%s";
	
	private static final String STRING_TASK = "task";
	private static final String STRING_DONE = "done";
	private static final String STRING_NOT_DONE = "todo";
	
	private static final int PARAM_TYPE = 0;
	private static final int PARAM_NAME = 1;
	private static final int PARAM_TODO = 2;
	private static final int PARAM_DEADLINE = 3;
	private static final int NUM_PARAM_TASK = 4;
	
	private static final int SAME_DATE = 0;
	
    private Date deadline;
    
	//============================================
	// Constructors
	//============================================

    public Task() {
        super(); 
        this.deadline = new Date();
    }
    
    public Task(String line) {
    	line.trim();
    	String[] lineComponents = line.split(SEMICOLON);
    	
    	assert(lineComponents[PARAM_TYPE].equals(STRING_TASK));
    	assert(lineComponents.length == NUM_PARAM_TASK);

    	this.name = lineComponents[PARAM_NAME]; 
    	this.isDone = lineComponents[PARAM_TODO].equals(STRING_DONE); 
    	this.deadline = new Date(lineComponents[PARAM_DEADLINE]);
    	
    }
   
	public Task(String name, boolean isDone, Date deadline) {
		super(name, isDone);
		this.deadline = deadline;
	}
	
	//============================================
	// Public Methods
	//============================================
	
	/**
	 * compareTo
	 * @return negative number if this < other 
	 * 		   positive number if this > other 
	 * 		   zero if this == other         
	 */
	public int compareTo(Task other){
		int compareDate = this.getDeadline().compareTo(other.getDeadline());
		int compareName = this.getName().compareTo(other.getName());
		if(compareDate != SAME_DATE){
			return compareDate; 
		}else{
			return compareName; 
		}
	}
	
	/**
	 * toString 
	 * @return formatted string to write into txt file
	 */
	public String toString(){
		String status;
		if (this.isDone) {
			status = STRING_DONE;
		} else {
			status = STRING_NOT_DONE;
		}
		return String.format(FORMAT_TO_STRING, this.name, status, this.deadline.formatDateShort());
	}

	//============================================
	// Getters
	//============================================
    public Date getDeadline() {
        return deadline;
    }

	//============================================
	// Setters
	//============================================
    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
}

