package struct;

public class Date {

    // Class variables
    // fullDate is in the format DDMMYY
    private String dayString, fullDate;
    private int day, month, year;

    public Date() {
        dayString = "";
        fullDate = "";
        day = -1;
        month = -1;
        year = -1;
    }

    public Date(String dayString, String fullDate) {
        this.dayString = dayString;
        this.fullDate = fullDate;
        day = Integer.parseInt(fullDate.substring(0, 2));
        month = Integer.parseInt(fullDate.substring(2, 4));
        year = Integer.parseInt(fullDate.substring(4));
    }

    // Accessors
    public String getDayString() {
        return dayString;
    }

    public String getFullDate() {
        return fullDate;
    }

    // For display purposes
    public String getFormatDate() {
        return "" + day + "/" + month + "/" + year;
    }

    /**
     * Converts a DDMMYY formatted date into a YYMMDD formatted date for easier
     * integer comparison.
     *
     * @param date
     *            The date entered by the user in DDMMYY format
     * @return date in YYMMDD format
     */
    public static String getReverseDate(String date) {
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
    private static int getIntDate(String yymmdd) {
        return Integer.parseInt(yymmdd);
    }

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
    
    /**
     * Checks if the date is the same as another date specified by user
     * 
     * @param date  The date to be compared against
     * @return      true if this.fullDate is the same as date, false otherwise.
     */
    public boolean isSameDate(String date) {
    	return fullDate.equals(date);
    }
}
