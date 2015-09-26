package struct;

public class Input {

    boolean isTask;
    Task task;
    Event event;

    public Input(Task task) {
        isTask = true;
        this.task = task;
        this.event = null;
    }

    public Input(Event event) {
        isTask = false;
        this.event = event;
        this.task = null;
    }

    // ------------------------Getters------------------------
    public Task getTask() {
        return task;
    }

    public Event getEvent() {
        return event;
    }

    // ------------------------Setters------------------------
    /* These work on the assumption that an Input can only be
     * either a Task or an Event, but not both. Either way,
     * there is no real requirement for these setters because
     * Input objects will only instantiated with the Tasks or
     * Events
     */

    public void setTask(Task task) {
        isTask = true;
        this.task = task;
        this.event = null;
    }

    public void setEvent(Event event) {
        isTask = false;
        this.event = event;
        this.task = null;
    }
}
