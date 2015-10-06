/**
 * This class defines the Task object and its methods used in the application
 *
 * @author Adrian
 */

package struct;

public class Task extends Data implements Comparable<Task>{

	private static final String SEMICOLON = ";";
	
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
    	
    	//assert correct no. of components == 3
    	//assert first word is task 
    	this.name = lineComponents[1]; 
    	this.isDone = false; 
    	this.deadline = new Date(lineComponents[2]);
    	
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

