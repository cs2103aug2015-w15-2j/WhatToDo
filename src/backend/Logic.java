package backend;

import java.nio.file.FileSystemException;

import struct.Date;
import struct.Input;
import struct.Task;

public class Logic {

    private Storage storage;

    public String runOperation(String userInput) {
        // Function stub, assumed add operation for a task
    	
    	// stub to test storage.
    	try {
    		storage = new Storage();
    	} catch (FileSystemException fileException) {
    		return fileException.getMessage();
    	}

        // Creates a Parser object to send userInput
        Parser parser = new Parser();
        Input input = parser.getCommandType(userInput);

        Task task = input.getTask();
        String taskName = task.getTaskName();
        Date taskDeadline = task.getTaskDeadline();
        
        String feedback = storage.addTask(taskName, taskDeadline);

        return feedback;
    }
}
