package struct;

public class FloatingTask extends Data implements Comparable<FloatingTask>{
	
	private static final String SEMICOLON = ";";
	
	private static final String FORMAT_TO_STRING = "float;%s;%s";
	
	private static final String STRING_FLOAT = "float";
	private static final String STRING_DONE = "done";
	private static final String STRING_NOT_DONE = "todo";

	//============================================
	// Constructors
	//============================================
	
	public FloatingTask() {
		super(); 
	}
	
	public FloatingTask(String line){ 
		line.trim();
    	String[] lineComponents = line.split(SEMICOLON);
    	
    	assert(lineComponents[0].equals(STRING_FLOAT));
    	assert(lineComponents.length == 3);
    	
    	this.name = lineComponents[1]; 
    	this.isDone = lineComponents[2].equals(STRING_DONE); 
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
