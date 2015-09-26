package struct;

public class Date {

    // Class variables
    // fullDate is in the format YYYYMMDD
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
        day = Integer.parseInt(fullDate.substring(6));
        month = Integer.parseInt(fullDate.substring(4, 6));
        year = Integer.parseInt(fullDate.substring(0, 4));
    }

    // Accessors
    public String getDayString() {
        return dayString;
    }

    public String getFullDate() {
        return fullDate;
    }

    // For comparison purposes
    public int getIntFullDate() {
        return Integer.parseInt(fullDate);
    }

    // For display purposes
    public String getFormatDate() {
        return "" + day + "/" + month + "/" + year;
    }
}
