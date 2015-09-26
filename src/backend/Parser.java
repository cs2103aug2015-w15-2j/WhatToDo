package backend;

import struct.Date;
import struct.Event;
import struct.Input;
import struct.Task;

public class Parser {

    public Input getCommandType(String inputText) {

        // Function stub, emulating a Task
        Task task = new Task();
        task.setTaskName(inputText);
        task.setTaskDeadline(new Date("Monday", "20150921"));
        task.setTaskFloating(false);

        return new Input(task);
    }
}
