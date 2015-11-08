package backend;

import java.util.ArrayList;

import struct.Date;

public class DateParser {
	
    private static final int POSITION_FIRST_INDEX = 0;
    private static final int POSITION_FOURTH_INDEX = 3;
    
    private static final int DIFFERENCE_ZERO = 0;
    
    private static final int FULL_DATE_LENGTH = 6;
    private static final int SMALL_DATE_LENGTH = 4;
    
    private static final int NUMBER_OF_DAYS_IN_A_WEEK = 7;
    private static final int NUMBER_OF_DAYS_IN_TWO_WEEKS = 14;
    
    private static final String REGEX_POSITIVE_INTEGER = "^0*[1-9][0-9]*";
	
    private static final String KEYWORD_TODAY = "today";
    private static final String KEYWORD_TOMORROW_ONE = "tomorrow";
    private static final String KEYWORD_TOMORROW_TWO = "tmr";
    private static final String KEYWORD_TOMORROW_THREE = "tomo";
    private static final String KEYWORD_NEXT = "n";
	
    private static final ArrayList<String> DAYS_ARRAY_LIST = new ArrayList<String>();
    private static final ArrayList<String> DAYS_FULL_ARRAY_LIST = new ArrayList<String>();
	
	public DateParser() {
		initDaysArrayList();
		initFullDaysArrayList();
	}
	
	protected Date getDate(String date) {
		Date todayDate = Date.todayDate();
		if (date.equals(KEYWORD_TODAY)){
			return todayDate;
		} else if (date.equals(KEYWORD_TOMORROW_ONE) || date.equals(KEYWORD_TOMORROW_TWO) 
				   || date.equals(KEYWORD_TOMORROW_THREE)) {
			return Date.tomorrowDate();
		} else if (DAYS_FULL_ARRAY_LIST.contains(date)) {
			String daySubstring = date.substring(POSITION_FIRST_INDEX, POSITION_FOURTH_INDEX);
			int day = DAYS_ARRAY_LIST.indexOf(daySubstring);
			String todayString = todayDate.getDayString().toLowerCase();
			String todaySubstring = todayString.substring(POSITION_FIRST_INDEX, POSITION_FOURTH_INDEX);
			int today = DAYS_ARRAY_LIST.indexOf(todaySubstring);
			int difference = day - today;
			if (difference >= DIFFERENCE_ZERO) {
				return todayDate.plusDay(difference);
			} else {
				return todayDate.plusDay(difference + NUMBER_OF_DAYS_IN_A_WEEK);
			}
		} else if (date.substring(0,1).equals(KEYWORD_NEXT) 
				&& DAYS_FULL_ARRAY_LIST.contains(date.substring(1))) {
			int day = DAYS_ARRAY_LIST.indexOf(date.substring(1,4));
			int today = DAYS_ARRAY_LIST.indexOf(todayDate.getDayString().substring(0,3).toLowerCase());
			int difference = day - today;
			if (difference >= DIFFERENCE_ZERO) {
				return todayDate.plusDay(difference + NUMBER_OF_DAYS_IN_A_WEEK);
			} else {
				return todayDate.plusDay(difference + NUMBER_OF_DAYS_IN_TWO_WEEKS);
			}
		} else if (date.matches(REGEX_POSITIVE_INTEGER) && String.valueOf(date).length() == SMALL_DATE_LENGTH) {
			String year = todayDate.formatDateShort().substring(4);
			date = date + year;
			Date currDate = new Date(date);
			if (isValidDate(date) && todayDate.compareTo(currDate) <= 0) {
				return currDate;
			}
		} else if (date.matches(REGEX_POSITIVE_INTEGER) 
				&& String.valueOf(date).length() == FULL_DATE_LENGTH && isValidDate(date)) {
			Date currDate = new Date(date);
			if (todayDate.compareTo(currDate) <= 0) {
				return currDate;
			}
		}
		return null;
	}
	
	protected boolean isValidDate(String date) {
		int[] daysInEachMonth = {0,31,28,31,30,31,30,31,31,30,31,30,31};
		int day = Integer.parseInt(date.substring(0,2));
		int month = Integer.parseInt(date.substring(2,4));
		int year = 2000 + Integer.parseInt(date.substring(4));
		if (year % 4 == 0) {
			if (year % 100 != 0) {
				daysInEachMonth[2] = 29;
			} else {
				if (year % 400 == 0) {
					daysInEachMonth[2] = 29;
				}
			}
		}
		if (month < 13 && day <= daysInEachMonth[month]) {
			return true;
		} else {
			return false;
		}
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
	}

}
