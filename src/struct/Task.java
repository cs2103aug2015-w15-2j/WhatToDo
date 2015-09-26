package struct;

public class Task {

    // Class variables for tasks
    private Date taskDeadline;
    private String taskName, taskFlag, taskDescription, taskTag;
    private boolean taskFloating, taskDone;

    public Task() {
        // Initialize task variables
        taskDeadline = new Date();
        taskName = "";
        taskFlag = "";
        taskDescription = "";
        taskTag = "";
        taskFloating = true;
        taskDone = false;
    }

    // ------------------------Getters------------------------
    public Date getTaskDeadline() {
        return taskDeadline;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskFlag() {
        return taskFlag;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getTaskTag() {
        return taskTag;
    }

    public boolean isTaskFloating() {
        return taskFloating;
    }

    public boolean isTaskDone() {
        return taskDone;
    }

    // ------------------------Setters------------------------
    public void setTaskDeadline(Date taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskFlag(String taskFlag) {
        this.taskFlag = taskFlag;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setTaskTag(String taskTag) {
        this.taskTag = taskTag;
    }

    public void setTaskFloating(boolean taskFloating) {
        this.taskFloating = taskFloating;
    }

    public void setTaskDone(boolean taskDone) {
        this.taskDone = taskDone;
    }
}

