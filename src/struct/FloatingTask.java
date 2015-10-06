package struct;

public class FloatingTask extends Data implements Comparable<FloatingTask>{
	
	private static final String SEMICOLON = ";";

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
	
	public int compareTo(FloatingTask other){ 
		if(this.getName().compareTo(other.getName()) < 0){
			return -1; 
		}
		else if (this.getName().compareTo(other.getName()) > 0 ){
			return 1; 
		}
		else{
			return 0; 
		}
	}
	
}
