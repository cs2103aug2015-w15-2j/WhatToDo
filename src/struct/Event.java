/**
 * This class defines the Event object and its methods used in the application
 *
 * @author Adrian
 */

package struct;


public class Event extends Data implements Comparable<Event>{

	private static final String SEMICOLON = ";";
	
	private static final String FORMAT_TO_STRING = "event;%s;%s;%s;%s;%s";
	
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
    
    public Event(String line){ 
    	line.trim();
    	String[] lineComponents = line.split(SEMICOLON);
    	
    	//assert correct no. of components 
    	//assert first word is event
    	this.name = lineComponents[1]; 
    	this.isDone = false; 
    	this.eventStartDate = new Date(lineComponents[2]);
    	this.eventEndDate = new Date(lineComponents[4]);
    	this.eventStartTime = lineComponents[3];
    	this.eventEndTime = lineComponents[5];
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
		
		if(compareStartDate != 0){ 
			return compareStartDate; 
		}else if (compareStartTime != 0){ 
			return compareStartTime; 
		}else if (compareEndDate != 0){ 
			return compareEndDate; 
		}else if (compareEndTime != 0){ 
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
		return String.format(FORMAT_TO_STRING, this.name, this.eventStartDate.getFullDate(), 
				this.eventStartTime, this.eventEndDate.getFullDate(), this.eventEndTime);
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
