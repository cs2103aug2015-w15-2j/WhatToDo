package struct;

public class FloatingTask extends Data implements Comparable<FloatingTask>{
	
	private static final String SEMICOLON = ";";
	
	private static final String FORMAT_TO_STRING = "float;%s";

	//============================================
	// Constructors
	//============================================
	
	public FloatingTask() {
		super(); 
	}
	
	public FloatingTask(String line){ 
		line.trim();
    	String[] lineComponents = line.split(SEMICOLON);
    	
    	//assert first word is float 
    	//assert no. of components == 2 
    	this.name = lineComponents[1]; 
    	this.isDone = false; 
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
		return String.format(FORMAT_TO_STRING, this.name); 
	}	
}
