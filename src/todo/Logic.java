package todo;

import struct.*;

public class Logic {

    private final char CHAR_NEWLINE = '\n';

    public String runOperation(Input input) {
        // Function stub, assumed add operation for a task
        Task task = input.getTask();
        Date taskDeadline = task.getTaskDeadline();
        return "Added " + task.getTaskName() + " to list." + CHAR_NEWLINE +
                "Due on " + taskDeadline.getDayString() + ", " +
                taskDeadline.getFormatDate() + ".";
    }
}
