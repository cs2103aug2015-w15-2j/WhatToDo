/**
 * This class parses user input string for time.
 * 
 * @@author A0124099B
 */

package backend;

public class TimeParser {
	
	private static final int POSITION_DIFFERENCE_TWO = 2;

	private static final String REGEX_TWELVE_HOUR_SIMPLE_TIME = "(1[012]|[1-9]|0[1-9])(\\s)?(?i)(am|pm)";
	private static final String REGEX_TWELVE_HOUR_TIME = "(1[012]|[1-9]|0[1-9])(:|.)?" + 
	                                                     "[0-5][0-9](\\s)?(?i)(am|pm)";
	private static final String REGEX_TWENTY_FOUR_HOUR_SIMPLE_TIME = "([01]?[0-9]|2[0-3])";
	private static final String REGEX_TWENTY_FOUR_HOUR_TIME = "([01]?[0-9]|2[0-3])(:|.)?[0-5][0-9]";

	private static final String STRING_ONE_DOT = ".";
	private static final String STRING_ONE_COLON = ":";
	private static final String STRING_EMPTY = "";

	private static final String TIME_AM = "am";
	private static final String TIME_PM = "pm";
	private static final String TIME_MIDNIGHT = "0000";
	private static final String TIME_ZERO_HOUR = "00";
	private static final String TIME_ZERO_MINUTE = "00";
	private static final String TIME_ZERO = "0";
	private static final int TIME_HOUR_TWELVE = 12;
	private static final int TIME_MAXIMUM = 2400;
	private static final int TIME_ADD_TWELVE = 12;
	private static final int TIME_LENGTH_ONE = 1;
	private static final int TIME_LENGTH_TWO = 2;
	private static final int TIME_LENGTH_THREE = 3;

	/**
	 * Depending on which REGEX the time String matches to, we will
	 * call the appropriate method to do the appropriate removal of 
	 * STRING_ONE_DOT and STRING_ONE_COLON, followed by the appropriate
	 * padding of zeroes and accounting for the time period (am/pm) as
	 * well as the special cases
	 * 
	 * @param time
	 * @return String
	 */
	protected String getTime(String time) {
		if (time.matches(REGEX_TWELVE_HOUR_SIMPLE_TIME)) {
			return getTwelveHourSimpleTime(time);
		} else if (time.matches(REGEX_TWELVE_HOUR_TIME)) {
			return getTwelveHourTime(time);
		} else if (time.matches(REGEX_TWENTY_FOUR_HOUR_SIMPLE_TIME)) {
			return getTwentyFourHourSimpleTime(time);
		} else if (time.matches(REGEX_TWENTY_FOUR_HOUR_TIME)) {
			return getTwentyFourHourTime(time);
		} else {
			return null;
		}
	}

	// To check if endTime is larger than startTime
	protected boolean areValidTimes(String startTime, String endTime) {
		int intStartTime = Integer.parseInt(startTime);
		int intEndTime = Integer.parseInt(endTime);
		if (intEndTime > intStartTime) {
			return true;
		} else {
			return false;
		}
	}

	private String getTwelveHourSimpleTime(String time) {
		String period = time.substring(time.length() - POSITION_DIFFERENCE_TWO).toLowerCase();
		String hourString = time.substring(CommandParser.POSITION_FIRST_INDEX, 
				                           time.length() - POSITION_DIFFERENCE_TWO).trim();
		int hourInt = Integer.parseInt(hourString);
		
		if (period.equals(TIME_PM) && hourInt != TIME_HOUR_TWELVE) {
			hourInt += TIME_ADD_TWELVE;
		}
		
		hourString = Integer.toString(hourInt);
		if (period.equals(TIME_AM) && hourInt == TIME_HOUR_TWELVE) {
			time = TIME_MIDNIGHT;
		} else if (hourString.length() == TIME_LENGTH_ONE) {
			time = TIME_ZERO + hourString + TIME_ZERO_MINUTE;
		} else {
			time = hourString + TIME_ZERO_MINUTE;
		}
		return time;
	}
	
	private String getTwelveHourTime(String time) {
		String period = time.substring(time.length() - POSITION_DIFFERENCE_TWO).toLowerCase();
		time = time.substring(CommandParser.POSITION_FIRST_INDEX, 
				              time.length() - POSITION_DIFFERENCE_TWO).trim();
		time = time.replace(STRING_ONE_DOT, STRING_EMPTY);
		time = time.replace(STRING_ONE_COLON, STRING_EMPTY);
		String minuteString = time.substring(time.length() - POSITION_DIFFERENCE_TWO);
		String hourString = time.substring(CommandParser.POSITION_FIRST_INDEX, 
				                           time.length() - POSITION_DIFFERENCE_TWO);
		
		int hourInt = Integer.parseInt(hourString);
		if (period.equals(TIME_PM) && hourInt != TIME_HOUR_TWELVE) {
			hourInt += TIME_ADD_TWELVE;
		}
		if (period.equals(TIME_AM) && hourInt == TIME_HOUR_TWELVE) {
			return TIME_ZERO_HOUR + minuteString;
		}
		
		hourString = Integer.toString(hourInt);
		if (hourString.length() == TIME_LENGTH_ONE) {
			time = TIME_ZERO + hourString + minuteString;
		} else {
			time = hourString + minuteString;
		}
		if (Integer.parseInt(time) < TIME_MAXIMUM) {
			return time;
		} else {
			return null;
		}
	}
	
	private String getTwentyFourHourSimpleTime(String time) {
		if (time.length() == TIME_LENGTH_ONE) {
			time = TIME_ZERO + time + TIME_ZERO_MINUTE;
		} else if (time.length() == TIME_LENGTH_TWO) {
			time = time + TIME_ZERO_MINUTE;
		}
		return time;
	}
	
	private String getTwentyFourHourTime(String time) {
		time = time.replace(STRING_ONE_DOT, STRING_EMPTY);
		time = time.replace(STRING_ONE_COLON, STRING_EMPTY);
		
		if (time.length() == TIME_LENGTH_THREE) {
			time = TIME_ZERO + time;
		}
		if (Integer.parseInt(time) < TIME_MAXIMUM) {
			return time;
		} else {
			return null;
		}
	}

}
