/**
 * This class defines the Date object and its methods used in the application
 *
 * @author Adrian
 */

package struct;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Date implements Comparable<Date>{
	
    // fullDate is in the format ddMMyy
    private String fullDate;
    private int day, month, year;
    
	//============================================
	// Constructors
	//============================================

    public Date() {
        this.fullDate = "";
        this.day = -1;
        this.month = -1;
        this.year = -1;
    }
    
    public Date(String fullDate){
    	this.fullDate = fullDate;
        this.day = Integer.parseInt(fullDate.substring(0, 2));
        this.month = Integer.parseInt(fullDate.substring(2, 4));
        this.year = Integer.parseInt(fullDate.substring(4));
    }
    
    //============================================
    // Static methods 
    //============================================
    
    public static String todayDateShort(){
    	Calendar cal = Calendar.getInstance(); 
    	SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy"); 
    	return sdf.format(cal.getTime());
    }
    
    public static String tomorrowDateShort(){
    	Calendar cal = Calendar.getInstance(); 
    	cal.add(Calendar.DATE,1); 
    	SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy"); 
    	return sdf.format(cal.getTime());
    }
    
    public static String todayDateLong(){
    	Calendar cal = Calendar.getInstance(); 
    	SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM yyyy"); 
    	return sdf.format(cal.getTime());
    }
    
    public static String tomorrowDateLong(){
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE,1); 
    	SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM yyyy"); 
    	return sdf.format(cal.getTime());
    }
    
    public static Date todayDate(){
    	 return new Date(Date.todayDateShort()); 
    }
    
    public static Date tomorrowDate() {
    	return new Date(Date.tomorrowDateShort());
    }

	//============================================
	// Public methods
	//============================================    
    
    /**
	 * compareTo
	 * @return -1 if this < other 
	 * 		    1 if this > other 
	 * 		    0 if this == other         
	 */
    public int compareTo(Date other){
    	int thisDateInt = getIntReverseDate(this); 
    	int otherDateInt = getIntReverseDate(other); 
    	if(thisDateInt < otherDateInt){
    		return -1; 
    	}
    	else if(thisDateInt > otherDateInt){
    		return 1; 
    	}
    	else{
    		return 0; 
    	}
    }
    
    //TODO isLaterThan is to be removed do not use
    /**
     * Checks if the date is later than another date specified by user
     *
     * @param date
     *            The date to be compared against
     * @return true if this.fullDate is later than date, false otherwise
     */
    public boolean isLaterThan(String date) {
        return getIntDate(getReverseDate(fullDate)) > getIntDate(getReverseDate(date));
    }
    
    //TODO isSameDate is to be removed do not use
    /**
     * Checks if the date is the same as another date specified by user
     * 
     * @param date  The date to be compared against
     * @return      true if this.fullDate is the same as date, false otherwise.
     */
    public boolean isSameDate(String date) {
        return fullDate.equals(date);
    }
    
    public String getDayString() {
    	
        Calendar cal = Calendar.getInstance(); 
        cal.set(this.year, this.month, this.day);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        return sdf.format(cal.getTime());
    }

    public String getFullDate() {
        return fullDate;
    }

    // For display purposes
    public String getFormatDate() {
        return "" + day + "/" + month + "/" + year;
    }

    
	//============================================
	// Private methods 
	//============================================
    
    private int getIntReverseDate(Date date){
    	String dateString = date.getFullDate(); 
    	return getIntDate(getReverseDate(dateString));
    }
    
    /**
     * Converts a DDMMYY formatted date into a YYMMDD formatted date for easier
     * integer comparison.
     *
     * @param date
     *            The date entered by the user in DDMMYY format
     * @return date in YYMMDD format
     */
    private String getReverseDate(String date) {
        return date.substring(4) + date.substring(2, 4) + date.substring(0, 2);
    }
    
    /**
     * Converts a date string into an integer for direct comparison. Works only
     * with YYMMDD format.
     *
     * @param yymmdd
     *            The date to be converted into an integer
     * @return yymmdd as an integer
     */
    private int getIntDate(String yymmdd) {
        return Integer.parseInt(yymmdd);
    }
}
