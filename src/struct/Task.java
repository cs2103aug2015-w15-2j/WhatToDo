/**
 * This class defines the Task object and its methods used in the application
 *
 * @author Adrian
 */

package struct;

public class Task extends Data implements Comparable<Task>{

	private static final String SEMICOLON = ";";
	private static final String TYPE_FLOAT = "float";
	private static final String TYPE_TASK = "task";
	
    private Date deadline;
    private boolean isFloating;
    
	//============================================
	// Constructors
	//============================================

    public Task() {
        super(); 
        this.deadline = new Date();
        this.isFloating = true;
    }
    
    public Task(String line) {
    	line.trim();
    	String[] lineComponents = line.split(SEMICOLON);
    	
    	this.name = lineComponents[1]; 
    	this.isDone = false; 
    	
    	//assert correct no. of components 2 or 3
    	//assert first word is float or task 
    	if(lineComponents[0].equals(TYPE_FLOAT)){
    		this.isFloating = true; 
    		this.deadline = null; 
    	}
    	else{
    		this.isFloating = false; 
    		this.deadline = new Date(lineComponents[2]);
    	}
    }
   
	public Task(String name, boolean isDone, Date deadline, boolean isFloating) {
		super(name, isDone);
		this.deadline = deadline;
		this.isFloating = isFloating;
	}
	
	//============================================
	// Public Methods
	//============================================
	//TODO compareTo
	public int compareTo(Task other){
		return 0; 
	}

	//============================================
	// Getters
	//============================================
    public Date getDeadline() {
        return deadline;
    }

    public boolean getIsFloating() {
        return isFloating;
    }

	//============================================
	// Setters
	//============================================
    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setIsFloating(boolean isFloating) {
        this.isFloating = isFloating;
    }
}

