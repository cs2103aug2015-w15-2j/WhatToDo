package backend;

import struct.Date;
import struct.Input;
import struct.Task;

public class Logic {

    private final char CHAR_NEWLINE = '\n';

    public String runOperation(String userInput) {
        // Function stub, assumed add operation for a task

        // Creates a Parser object to send userInput
        Parser parser = new Parser();
        Input input = parser.getCommandType(userInput);

        Task task = input.getTask();
        Date taskDeadline = task.getTaskDeadline();

        return "Added " + task.getTaskName() + " to list." + CHAR_NEWLINE +
                "Due on " + taskDeadline.getDayString() + ", " +
                taskDeadline.getFormatDate() + ".";
    }
}
