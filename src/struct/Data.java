package struct;

public class Data {
	
	protected String name; 
	protected boolean isDone;
	
	//============================================
	// Constructor
	//============================================
	
	public Data(){
		this.name = ""; 
		this.isDone = false;
	}
	
	public Data(String name, boolean isDone) {
		this.name = name;
		this.isDone = isDone;
	}
	
	//============================================
	// Getters
	//============================================	

	public String getName() {
		return name;
	}

	public boolean isDone() {
		return isDone;
	}

	//============================================
	// Setters
	//============================================

	public void setName(String name) {
		this.name = name;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

}
