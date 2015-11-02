/**
 * This class defines the Task object and its methods used in the application
 *
 * @author Adrian
 */

package struct;

public class Task extends Data implements Comparable<Task>{

	private static final String SEMICOLON = ";";
	
	private static final String FORMAT_TO_STRING = "task;%s;%s;%s";
	
	private static final String STRING_TASK = "task";
	private static final String STRING_DONE = "done";
	private static final String STRING_NOT_DONE = "todo";
	
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
    	
    	assert(lineComponents[0].equals(STRING_TASK));
    	assert(lineComponents.length == 4);

    	this.name = lineComponents[1]; 
    	this.isDone = lineComponents[2].equals(STRING_DONE); 
    	this.deadline = new Date(lineComponents[3]);
    	
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
		if(compareDate != 0){
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

