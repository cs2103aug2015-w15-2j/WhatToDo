package todo;

import struct.*;

public class Logic {

    private final String CHAR_TAB = "\t";
    private final String CHAR_NEWLINE = "\n";

    public String runOperation(Input input) {
        // Function stub, assumed add operation for a task
        Task task = input.getTask();
        Date taskDeadline = task.getTaskDeadline();
        return CHAR_TAB + "Added " + task.getTaskName() + " to list." + CHAR_NEWLINE +
                CHAR_TAB + "Due on " + taskDeadline.getDayString() + ", " +
                taskDeadline.getFormatDate() + "." + CHAR_NEWLINE;
    }
}
