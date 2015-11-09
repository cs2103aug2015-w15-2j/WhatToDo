/**
 * This class defines the Task object and its methods used in the application
 * 
 * @@author A0124238L
 */

package struct;

public class FloatingTask extends TodoItem implements Comparable<FloatingTask>{
	
	private static final String SEMICOLON = ";";
	
	private static final String FORMAT_TO_STRING = "float;%s;%s";
	
	private static final String STRING_FLOAT = "float";
	private static final String STRING_DONE = "done";
	private static final String STRING_NOT_DONE = "todo";
	
	private static final int PARAM_TYPE = 0;
	private static final int PARAM_NAME = 1;
	private static final int PARAM_DONE = 2;
	private static final int NUM_PARAM_FLOAT_TASK = 3;

	//============================================
	// Constructors
	//============================================
	
	public FloatingTask() {
		super(); 
	}
	
	public FloatingTask(String line){ 
		line.trim();
    	String[] lineComponents = line.split(SEMICOLON);
    	
    	assert(lineComponents[PARAM_TYPE].equals(STRING_FLOAT));
    	assert(lineComponents.length == NUM_PARAM_FLOAT_TASK);
    	
    	this.name = lineComponents[PARAM_NAME]; 
    	this.isDone = lineComponents[PARAM_DONE].equals(STRING_DONE); 
	}
	
	public FloatingTask(String name, boolean isDone){
		super(name, isDone);
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
	public int compareTo(FloatingTask other){ 
		return this.getName().compareTo(other.getName());
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
		return String.format(FORMAT_TO_STRING, this.name, status); 
	}	
}
