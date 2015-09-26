package struct;

public class Event {

    // Class variables for events
    private Date eventStartDate, eventEndDate;
    private String eventName, eventDescription, eventStartTime, eventEndTime;
    private boolean eventDone;

    public Event() {
        eventStartDate = new Date();
        eventEndDate = new Date();
        eventName = "";
        eventDescription = "";
        eventStartTime = "";
        eventEndTime = "";
        eventDone = false;
    }

    // ------------------------Getters------------------------
    public Date getEventStartDate() {
        return eventStartDate;
    }

    public Date getEventEndDate() {
        return eventEndDate;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public boolean isEventDone() {
        return eventDone;
    }

    // ------------------------Setters------------------------
    public void setEventStartDate(Date eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public void setEventEndDate(Date eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public void setEventDone(boolean eventDone) {
        this.eventDone = eventDone;
    }
}
