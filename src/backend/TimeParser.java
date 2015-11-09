package backend;

//@@author A0124099B
public class TimeParser {

	private static final String REGEX_12_HOUR_SIMPLE_TIME = "(1[012]|[1-9]|0[1-9])(\\s)?(?i)(am|pm)";
	private static final String REGEX_12_HOUR_TIME = "(1[012]|[1-9]|0[1-9])(:|.)?" + "[0-5][0-9](\\s)?(?i)(am|pm)";
	private static final String REGEX_24_HOUR_SIMPLE_TIME = "([01]?[0-9]|2[0-3])";
	private static final String REGEX_24_HOUR_TIME = "([01]?[0-9]|2[0-3])(:|.)?[0-5][0-9]";

	private static final String STRING_ONE_DOT = ".";
	private static final String STRING_ONE_COLON = ":";
	private static final String STRING_EMPTY = "";

	private static final String TIME_MIDNIGHT = "0000";
	private static final String TIME_ZERO_HOUR = "00";
	private static final String TIME_ZERO_MINUTE = "00";
	private static final int TIME_MAXIMUM = 2400;
	private static final int TIME_ADD_TWELVE = 12;

	public TimeParser() {

	}

	protected String getTime(String time) {
		String timeString;
		if (time.matches(REGEX_12_HOUR_SIMPLE_TIME)) {
			String period = time.substring(time.length() - 2).toLowerCase();
			String hourString = time.substring(0, time.length() - 2).trim();
			int hourInt = Integer.parseInt(hourString);
			if (period.equals("pm") && hourInt != 12) {
				hourInt += TIME_ADD_TWELVE;
			}
			hourString = Integer.toString(hourInt);
			if (period.equals("am") && hourInt == 12) {
				timeString = TIME_MIDNIGHT;
			} else if (hourString.length() == 1) {
				timeString = "0" + hourString + TIME_ZERO_MINUTE;
			} else {
				timeString = hourString + TIME_ZERO_MINUTE;
			}
			return timeString;
		} else if (time.matches(REGEX_12_HOUR_TIME)) {
			String period = time.substring(time.length() - 2).toLowerCase();
			timeString = time.substring(0, time.length() - 2).trim();
			timeString = timeString.replace(STRING_ONE_DOT, STRING_EMPTY);
			timeString = timeString.replace(STRING_ONE_COLON, STRING_EMPTY);
			String minuteString = timeString.substring(timeString.length() - 2);
			String hourString = timeString.substring(0, timeString.length() - 2);
			int hourInt = Integer.parseInt(hourString);
			if (period.equals("pm") && hourInt != 12) {
				hourInt += TIME_ADD_TWELVE;
			}
			if (period.equals("am") && hourInt == 12) {
				return TIME_ZERO_HOUR + minuteString;
			}
			hourString = Integer.toString(hourInt);
			if (hourString.length() == 1) {
				timeString = "0" + hourString + minuteString;
			} else {
				timeString = hourString + minuteString;
			}
			if (Integer.parseInt(timeString) < TIME_MAXIMUM) {
				return timeString;
			}
		} else if (time.matches(REGEX_24_HOUR_SIMPLE_TIME)) {
			timeString = time;
			if (time.length() == 1) {
				timeString = "0" + time + TIME_ZERO_MINUTE;
			} else if (time.length() == 2) {
				timeString = time + TIME_ZERO_MINUTE;
			}
			return timeString;
		} else if (time.matches(REGEX_24_HOUR_TIME)) {
			timeString = time;
			timeString = timeString.replace(".", "");
			timeString = timeString.replace(":", "");
			if (timeString.length() == 3) {
				timeString = "0" + timeString;
			}
			if (Integer.parseInt(timeString) < TIME_MAXIMUM) {
				return timeString;
			}
		}
		return null;
	}

	protected boolean areValidTimes(String startTime, String endTime) {
		int intStartTime = Integer.parseInt(startTime);
		int intEndTime = Integer.parseInt(endTime);
		if (intEndTime > intStartTime) {
			return true;
		} else {
			return false;
		}
	}

}
