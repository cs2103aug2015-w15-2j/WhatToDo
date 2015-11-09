/**
 * This class defines the Date object and its methods used in the application
 *
 * @@author A0124238L
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
        this.year = 2000 + Integer.parseInt(fullDate.substring(4));
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
    	SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy"); 
    	return sdf.format(cal.getTime());
    }
    
    public static String tomorrowDateLong(){
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE,1); 
    	SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy"); 
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
    
    public String getDayString() {
        Calendar cal = Calendar.getInstance(); 
        cal.set(this.year, this.month-1, this.day);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        return sdf.format(cal.getTime());
    }
    
    /**
     * Returns a Date object corresponding to daysToAdd days after 
     * this Date. Note: This Date object is not updated.
     * 
     * Example:
     * Date newDate = new Date("290915");
     * Date oneDayLater = newDate.plusDay(1);
     * 
     * newDate will be still at "290915" whereas oneDayLater is at "300915"
     * 
     * @param daysToAdd   number of days to add to this Date.
     * @return            Date object after daysToAdd days from this Date.
     */
    public Date plusDay(int daysToAdd) {
    	
        Calendar cal = Calendar.getInstance(); 
        cal.set(this.year, this.month-1, this.day);
        cal.add(Calendar.DATE, daysToAdd);
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
        return new Date(sdf.format(cal.getTime()));

    }

    public String formatDateShort() {
        return fullDate;
    }
    
    public String formatDateMedium(){ 
    	Calendar cal = Calendar.getInstance(); 
    	int todayYear = cal.get(Calendar.YEAR);
    	cal.set(this.year, this.month-1, this.day);
    	SimpleDateFormat sdf = getDateFormat(todayYear);
    	return sdf.format(cal.getTime());
    }
    
    public String formatDateLong(){ 
    	Calendar cal = Calendar.getInstance(); 
    	cal.set(this.year, this.month-1, this.day);
    	SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy"); 
    	return sdf.format(cal.getTime());
    }

	//============================================
	// Private methods 
	//============================================
    
    private int getIntReverseDate(Date date){
    	String dateString = date.formatDateShort(); 
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
    
	private SimpleDateFormat getDateFormat(int todayYear) {
    	if(todayYear == this.year){
    		return new SimpleDateFormat("EEE, dd MMM"); 
    	}
    	else{ 
    		return new SimpleDateFormat("EEE, dd MMM yy"); 
    	}
	}
}
