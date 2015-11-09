/**
 * This class defines the Event object and its methods used in the application
 *
 * @@author A0127051U
 */

package struct;


public class Event extends TodoItem implements Comparable<Event>{

	private static final String SEMICOLON = ";";
	private static final String EMPTY_STRING = "";
	
	private static final String FORMAT_TO_STRING = "event;%s;%s;%s;%s;%s;%s";
	
	private static final String STRING_EVENT = "event";
	private static final String STRING_DONE = "done";
	private static final String STRING_NOT_DONE = "todo";
	
	private static final int PARAM_TYPE = 0;
	private static final int PARAM_NAME = 1;
	private static final int PARAM_DONE = 2;
	private static final int PARAM_START_DATE = 3;
	private static final int PARAM_START_TIME = 4;
	private static final int PARAM_END_DATE = 5;
	private static final int PARAM_END_TIME = 6;
	private static final int NUM_PARAM_EVENTS = 7;
	
	private static final int SAME_DATE = 0;
	private static final int SAME_TIME = 0;
	
    private Date eventStartDate, eventEndDate;
    private String eventStartTime, eventEndTime;
    
	//============================================
	// Constructors
	//============================================
    
    public Event() {
    	super();
        this.eventStartDate = new Date();
        this.eventEndDate = new Date();
        this.eventStartTime = EMPTY_STRING;
        this.eventEndTime = EMPTY_STRING;
    }
    
    public Event(String line){ 
    	line.trim();
    	String[] lineComponents = line.split(SEMICOLON);
    	
    	assert(lineComponents[PARAM_TYPE].equals(STRING_EVENT));
    	assert(lineComponents.length == NUM_PARAM_EVENTS);
    	
    	this.name = lineComponents[PARAM_NAME]; 
    	this.isDone = lineComponents[PARAM_DONE].equals(STRING_DONE); 
    	this.eventStartDate = new Date(lineComponents[PARAM_START_DATE]);
    	this.eventEndDate = new Date(lineComponents[PARAM_END_DATE]);
    	this.eventStartTime = lineComponents[PARAM_START_TIME];
    	this.eventEndTime = lineComponents[PARAM_END_TIME];
    }

	public Event(String name, boolean isDone, Date eventStartDate, Date eventEndDate, String eventStartTime, String eventEndTime) {
		super(name, isDone);
		this.eventStartDate = eventStartDate;
		this.eventEndDate = eventEndDate;
		this.eventStartTime = eventStartTime;
		this.eventEndTime = eventEndTime;
	}
	
	//============================================
	// Public Methods
	//============================================
	
	/**
	 * compareTo
	 * @return negative number if this < other 
	 * 		   positive number if this > other 
	 * 		   zero if this == other         
	 */
	public int compareTo(Event other){ 
		int compareStartDate = this.getEventStartDate().compareTo(other.getEventStartDate());
		int compareEndDate = this.getEventEndDate().compareTo(other.getEventEndDate());
		int compareStartTime = this.getEventStartTime().compareTo(other.getEventStartTime()); 
		int compareEndTime = this.getEventEndTime().compareTo(other.getEventEndTime());
		int compareName = this.getName().compareTo(other.getName());
		
		if(compareStartDate != SAME_DATE){ 
			return compareStartDate; 
		}else if (compareStartTime != SAME_TIME){ 
			return compareStartTime; 
		}else if (compareEndDate != SAME_DATE){ 
			return compareEndDate; 
		}else if (compareEndTime != SAME_TIME){ 
			return compareEndTime; 
		}else{
			return compareName; 
		}
	}
	
	/**
	 * toString 
	 * @return formatted string to write into txt file
	 */
	public String toString(){
		String status;
		if (this.isDone) {
			status = STRING_DONE;
		} else {
			status = STRING_NOT_DONE;
		}
		return String.format(FORMAT_TO_STRING, this.name, status, this.eventStartDate.formatDateShort(), 
				this.eventStartTime, this.eventEndDate.formatDateShort(), this.eventEndTime);
	}

	//============================================
	// Getters 
	//============================================
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

	//============================================
	// Setters 
	//============================================
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
