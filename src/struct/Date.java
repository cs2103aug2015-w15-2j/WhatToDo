/**
 * This class defines the Date object and its methods used in the application
 *
 * @@author A0124238L
 */

package struct;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Date implements Comparable<Date>{
	private static final String EMPTY_STRING = "";
	private static final String DAY_MONTH_YEAR = "ddMMyy";
	private static final String DATE_LONG_FORMAT = "EEE, dd MMM yyyy";
	private static final String DATE_LONG_SHORT_YEAR_FORMAT = "EEE, dd MMM yy";
	private static final String DATE_LONG_NO_YEAR_FORMAT = "EEE, dd MMM";
	
	private static final String DAY_STRING = "EEEE";
	
	private static final int INVALID = -1;
	private static final int FIRST_DATE = 1;
	private static final int YEAR_2000 = 2000;
	private static final int PARAM_FIRST_CHAR = 0;
	private static final int PARAM_THIRD_CHAR = 2;
	private static final int PARAM_FIFTH_CHAR = 4;
	
	private static final int SAME_DATE = 0;
	private static final int EARLIER_DATE = -1;
	private static final int LATER_DATE = 1;
	
	private static final int OFFSET = 1;
	private static final int NUM_PARAM_DATE = 6;
	
    // fullDate is in the format ddMMyy
    private String fullDate;
    private int day, month, year;
    
	//============================================
	// Constructors
	//============================================

    public Date() {
        this.fullDate = EMPTY_STRING;
        this.day = INVALID;
        this.month = INVALID;
        this.year = INVALID;
    }
    
    public Date(String fullDate){
    	this.fullDate = fullDate;
        this.day = Integer.parseInt(fullDate.substring(PARAM_FIRST_CHAR, PARAM_THIRD_CHAR));
        this.month = Integer.parseInt(fullDate.substring(PARAM_THIRD_CHAR, PARAM_FIFTH_CHAR));
        this.year = YEAR_2000 + Integer.parseInt(fullDate.substring(PARAM_FIFTH_CHAR));
    }
    
    //============================================
    // Static methods 
    //============================================
    
    public static String todayDateShort(){
    	Calendar cal = Calendar.getInstance(); 
    	
    	SimpleDateFormat sdf = new SimpleDateFormat(DAY_MONTH_YEAR); 
    	return sdf.format(cal.getTime());
    }
    
    public static String tomorrowDateShort(){
    	Calendar cal = Calendar.getInstance(); 
    	cal.add(Calendar.DATE, FIRST_DATE); 
    	
    	SimpleDateFormat sdf = new SimpleDateFormat(DAY_MONTH_YEAR); 
    	return sdf.format(cal.getTime());
    }
    
    public static String todayDateLong(){
    	Calendar cal = Calendar.getInstance(); 
    	
    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_LONG_FORMAT); 
    	return sdf.format(cal.getTime());
    }
    
    public static String tomorrowDateLong(){
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE, FIRST_DATE); 
    	
    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_LONG_FORMAT); 
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
    		return EARLIER_DATE; 
    	}
    	else if(thisDateInt > otherDateInt){
    		return LATER_DATE; 
    	}
    	else{
    		return SAME_DATE; 
    	}
    }
    
    public String getDayString() {
        Calendar cal = Calendar.getInstance(); 
        cal.set(this.year, this.month - OFFSET, this.day);
        
        SimpleDateFormat sdf = new SimpleDateFormat(DAY_STRING);
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
        cal.set(this.year, this.month - OFFSET, this.day);
        cal.add(Calendar.DATE, daysToAdd);
        
        SimpleDateFormat sdf = new SimpleDateFormat(DAY_MONTH_YEAR);
        return new Date(sdf.format(cal.getTime()));
    }

    public String formatDateShort() {
        return fullDate;
    }
    
    public String formatDateMedium(){ 
    	Calendar cal = Calendar.getInstance(); 
    	int todayYear = cal.get(Calendar.YEAR);
    	cal.set(this.year, this.month - OFFSET, this.day);
    	
    	SimpleDateFormat sdf = getDateFormat(todayYear);
    	return sdf.format(cal.getTime());
    }
    
    public String formatDateLong(){ 
    	Calendar cal = Calendar.getInstance(); 
    	cal.set(this.year, this.month - OFFSET, this.day);
    	
    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_LONG_FORMAT); 
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
        return date.substring(PARAM_FIFTH_CHAR) + date.substring(PARAM_THIRD_CHAR, PARAM_FIFTH_CHAR)
        		+ date.substring(PARAM_FIRST_CHAR, PARAM_THIRD_CHAR);
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
    	assert(yymmdd.length() == NUM_PARAM_DATE);
    	
        return Integer.parseInt(yymmdd);
    }
    
	private SimpleDateFormat getDateFormat(int todayYear) {
    	if(todayYear == this.year){
    		return new SimpleDateFormat(DATE_LONG_NO_YEAR_FORMAT); 
    	}
    	else{ 
    		return new SimpleDateFormat(DATE_LONG_SHORT_YEAR_FORMAT); 
    	}
	}
}
