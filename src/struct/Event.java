/**
 * This class defines the Event object and its methods used in the application
 *
 * @author Adrian
 */

package struct;

public class Event extends Data{

    private Date eventStartDate, eventEndDate;
    private String eventStartTime, eventEndTime;
    
	//============================================
	// Constructors
	//============================================
    
    public Event() {
    	super();
        this.eventStartDate = new Date();
        this.eventEndDate = new Date();
        this.eventStartTime = "";
        this.eventEndTime = "";
    }

	public Event(String name, boolean isDone, Date eventStartDate, Date eventEndDate, String eventStartTime, String eventEndTime) {
		super(name, isDone);
		this.eventStartDate = eventStartDate;
		this.eventEndDate = eventEndDate;
		this.eventStartTime = eventStartTime;
		this.eventEndTime = eventEndTime;
	}

	// ------------------------Getters------------------------
    public Date getEventStartDate() {
        return eventStartDate;
    }

    public Date getEventEndDate() {
        return eventEndDate;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    // ------------------------Setters------------------------
    public void setEventStartDate(Date eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public void setEventEndDate(Date eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }
    
}
