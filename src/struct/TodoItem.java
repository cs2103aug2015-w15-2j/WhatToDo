/**
 * This abstract class encpasulates todo-items 
 * 
 * @@author A0124238L
 */

package struct;

public abstract class TodoItem {
	
	protected String name; 
	protected boolean isDone;
	
	//============================================
	// Constructor
	//============================================
	
	public TodoItem(){
		this.name = ""; 
		this.isDone = false;
	}
	
	public TodoItem(String name, boolean isDone) {
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
