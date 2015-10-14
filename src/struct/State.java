package struct;

public class State {
	
	private String fileContents; 
	private String userCommand;
	
	//============================================
	// Constructors
	//============================================
	
	public State(String fileContents, String userCommand) {
		this.fileContents = fileContents;
		this.userCommand = userCommand;
	}
	
	//============================================
	// Getters
	//============================================

	public String getFileContents() {
		return fileContents;
	}

	public String getUserCommand() {
		return userCommand;
	}
}
