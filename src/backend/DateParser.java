/**
 * This class parses user input string for date.
 * 
 * @@author A0124099B
 */

package backend;

import java.util.ArrayList;

import struct.Date;

public class DateParser {

	private static final int POSITION_SECOND_INDEX = 1;
	private static final int POSITION_THIRD_INDEX = 2;
	private static final int POSITION_FOURTH_INDEX = 3;
	private static final int POSITION_FIFTH_INDEX = 4;
	
	private static final int SAME_DATE = 0;
	
	private static final int DIFFERENCE_ZERO = 0;

	private static final int SMALL_DATE_LENGTH = 4;

	private static final int NUMBER_OF_DAYS_IN_A_WEEK = 7;

	private static final String KEYWORD_NEXT = "n";
	private static final String KEYWORD_TODAY = "today";
	private static final ArrayList<String> KEYWORD_TOMORROWS = new ArrayList<String>();

	private static final ArrayList<String> DAYS_ARRAY_LIST = new ArrayList<String>();
	private static final ArrayList<String> DAYS_FULL_ARRAY_LIST = new ArrayList<String>();

	public DateParser() {
		initTmrArrayList();
		initDaysArrayList();
		initFullDaysArrayList();
	}

	protected Date getDate(String date) {
		Date todayDate = Date.todayDate();
		if (date.equals(KEYWORD_TODAY)) {
			return todayDate;
		} else if (KEYWORD_TOMORROWS.contains(date)) {
			return Date.tomorrowDate();
		} else if (DAYS_FULL_ARRAY_LIST.contains(date)) {
			return getDateForDay(date, todayDate);
		} else if (date.matches(CommandParser.REGEX_POSITIVE_INTEGER)) {
			return getDateForDdmm(date, todayDate);
		} else {
			return null;
		}
	}

	private Date getDateForDdmm(String date, Date todayDate) {
		if (String.valueOf(date).length() == SMALL_DATE_LENGTH) {
			String year = todayDate.formatDateShort().substring(POSITION_FIFTH_INDEX);
			date = date + year;
		}
		Date currDate = new Date(date);
		if (isValidDate(date) && todayDate.compareTo(currDate) <= SAME_DATE) {
			return currDate;
		} else {
			return null;
		}
	}

	private Date getDateForDay(String date, Date todayDate) {
		boolean hasN = false;
		
		// If the first character is KEYWORD_NEXT, we set hasN to true
		// and remove the first chracter
		if (date.substring(CommandParser.POSITION_FIRST_INDEX, POSITION_SECOND_INDEX).equals(KEYWORD_NEXT)) {
			hasN = true;
			date = date.substring(POSITION_SECOND_INDEX);
		}
		
		String daySubstring = date.substring(CommandParser.POSITION_FIRST_INDEX, POSITION_FOURTH_INDEX);
		String todayString = todayDate.getDayString().toLowerCase();
		String todaySubstring = todayString.substring(CommandParser.POSITION_FIRST_INDEX, 
				                                      POSITION_FOURTH_INDEX);
		int day = DAYS_ARRAY_LIST.indexOf(daySubstring);
		int today = DAYS_ARRAY_LIST.indexOf(todaySubstring);
		int difference = day - today;
		
		// If difference is negative, we add NUMBER_OF_DAYS_IN_A_WEEK
		// to the difference to know the number of days we need to add
		// to today
		if (difference < DIFFERENCE_ZERO) {
			difference += NUMBER_OF_DAYS_IN_A_WEEK;
		}
		
		// If hasN has been set to true, we will need to add another
		// NUMBER_OF_DAYS_IN_A_WEEK to get the actual difference
		// in days from today
		if (hasN) {
			difference += NUMBER_OF_DAYS_IN_A_WEEK;
		}
		
		return todayDate.plusDay(difference);
	}

	protected boolean isValidDate(String date) {
		// To account for the offset in index referencing, index 0 is initialiased to 0,
		// while the rest of the indexes are initialised to the number of days in that
		// index's month
		int[] daysInEachMonth = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int day = Integer.parseInt(date.substring(CommandParser.POSITION_FIRST_INDEX, POSITION_THIRD_INDEX));
		int month = Integer.parseInt(date.substring(POSITION_THIRD_INDEX, POSITION_FIFTH_INDEX));
		int year = 2000 + Integer.parseInt(date.substring(POSITION_FIFTH_INDEX));
		
		// Adjust number of days in February if the year is a leap year
		adjustForLeapYear(daysInEachMonth, year);
		if (month < 13 && day <= daysInEachMonth[month]) {
			return true;
		} else {
			return false;
		}
	}

	private void adjustForLeapYear(int[] daysInEachMonth, int year) {
		if (year % 4 == 0) {
			if (year % 100 != 0) {
				daysInEachMonth[2] = 29;
			} else {
				if (year % 400 == 0) {
					daysInEachMonth[2] = 29;
				}
			}
		}
	}
	
	private void initTmrArrayList() {
		KEYWORD_TOMORROWS.add("tomorrow");
		KEYWORD_TOMORROWS.add("tmr");
		KEYWORD_TOMORROWS.add("tomo");
	}

	private void initDaysArrayList() {
		DAYS_ARRAY_LIST.add("mon");
		DAYS_ARRAY_LIST.add("tue");
		DAYS_ARRAY_LIST.add("wed");
		DAYS_ARRAY_LIST.add("thu");
		DAYS_ARRAY_LIST.add("fri");
		DAYS_ARRAY_LIST.add("sat");
		DAYS_ARRAY_LIST.add("sun");
	}

	private void initFullDaysArrayList() {
		DAYS_FULL_ARRAY_LIST.add("mon");
		DAYS_FULL_ARRAY_LIST.add("tue");
		DAYS_FULL_ARRAY_LIST.add("wed");
		DAYS_FULL_ARRAY_LIST.add("thu");
		DAYS_FULL_ARRAY_LIST.add("fri");
		DAYS_FULL_ARRAY_LIST.add("sat");
		DAYS_FULL_ARRAY_LIST.add("sun");
		DAYS_FULL_ARRAY_LIST.add("monday");
		DAYS_FULL_ARRAY_LIST.add("tuesday");
		DAYS_FULL_ARRAY_LIST.add("wednesday");
		DAYS_FULL_ARRAY_LIST.add("thursday");
		DAYS_FULL_ARRAY_LIST.add("friday");
		DAYS_FULL_ARRAY_LIST.add("saturday");
		DAYS_FULL_ARRAY_LIST.add("sunday");
		DAYS_FULL_ARRAY_LIST.add("nmon");
		DAYS_FULL_ARRAY_LIST.add("ntue");
		DAYS_FULL_ARRAY_LIST.add("nwed");
		DAYS_FULL_ARRAY_LIST.add("nthu");
		DAYS_FULL_ARRAY_LIST.add("nfri");
		DAYS_FULL_ARRAY_LIST.add("nsat");
		DAYS_FULL_ARRAY_LIST.add("nsun");
		DAYS_FULL_ARRAY_LIST.add("nmonday");
		DAYS_FULL_ARRAY_LIST.add("ntuesday");
		DAYS_FULL_ARRAY_LIST.add("nwednesday");
		DAYS_FULL_ARRAY_LIST.add("nthursday");
		DAYS_FULL_ARRAY_LIST.add("nfriday");
		DAYS_FULL_ARRAY_LIST.add("nsaturday");
		DAYS_FULL_ARRAY_LIST.add("nsunday");
	}

}
