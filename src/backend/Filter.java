/**
 * This class filters the contents of file so that each view in the application 
 * can display the correct information 
 * 
 * @@author A0127051U
 */

package backend;

import java.util.ArrayList;

import struct.Date;

public class Filter {
	
	private static final int INDEX_TYPE = 0; 
	private static final int INDEX_NAME = 1; 
	private static final int INDEX_ISDONE = 2; 
	private static final int INDEX_DUEDATE = 3;
	private static final int INDEX_STARTDATE = 3; 
	private static final int INDEX_STARTTIME = 4;
	private static final int INDEX_ENDDATE = 5; 
	private static final int INDEX_ENDTIME = 6;
	
    private static final String TYPE_TASK = "task";
    private static final String TYPE_EVENT = "event";
	
	private static final String DONE = "done";
	private static final String SEMICOLON = ";";
	private static final String REGEX_WHITESPACES = "[\\s,]+"; 
	
	//============================================
	// Public methods
	//============================================
	
	/**
	 * filters tasks and events only
	 * @param linesInFile
	 * @param type
	 * @param date
	 * @return the arraylist of indexes of tasks due on date if type is task 
	 *         the arraylist indexes of events starting on date if type is event
	 */
	public ArrayList<Integer> filterDate(String[] linesInFile, String type, Date date){ 
		 assert(type.equals(TYPE_TASK) || type.equals(TYPE_EVENT));
		
		 ArrayList<Integer> resultList = new ArrayList<Integer>(); 
		 
		 for(int i = 0; i < linesInFile.length; i++){ 
			 String line = linesInFile[i]; 
			 if(isType(type, line) && isReqStatus(false, line) && isOnDate(date, line)){
				 resultList.add(i); 
			 }
		 } 
		 
		 return resultList; 
	}
	
	public ArrayList<Integer> filterDateAndTime(String[] linesInFile, Date date, String time){
		ArrayList<Integer> resultList = new ArrayList<Integer>();
		 
		 for(int i = 0; i < linesInFile.length; i++){ 
			 String line = linesInFile[i]; 
			 if (isType(TYPE_EVENT, line) && isReqStatus(false, line) && isOnDate(date, line) &&
					 !isOngoing(line) && !isPastTime(line)){
				 resultList.add(i); 
			 }
		 } 
		 
		 return resultList;
    }
	
	/**
	 * filters for ongoing events - events starting earlier than today 
	 * and ending later than today 
	 * @param linesInFile
	 * @return the arraylist indexes of ongoing events 
	 */
	public ArrayList<Integer> filterOngoingEvents(String[] linesInFile){ 
		ArrayList<Integer> resultList = new ArrayList<Integer>(); 
		
		for(int i = 0; i < linesInFile.length; i++){ 
			String line = linesInFile[i];
			if(isType(TYPE_EVENT, line) && isReqStatus(false, line) && isOngoing(line)){
				resultList.add(i);
			}
		}
		
		return resultList; 
	}
	
	/**
	 * filters any type of to-do item
	 * @param linesInFile
	 * @param type
	 * @param isDone
	 * @return arraylist of indexes items of type 'type' that is done if isDone is true 
	 *         arraylist of indexes items of type 'type' that is uncompleted, otherwise
	 */
	public ArrayList<Integer> filterStatus(String[] linesInFile, String type, boolean isDone){ 
		 ArrayList<Integer> resultList = new ArrayList<Integer>(); 
		 
		 for(int i = 0; i < linesInFile.length; i++){ 
			 String line = linesInFile[i];
			 if(isType(type, line) && isReqStatus(isDone, line)){
				 resultList.add(i); 
			 }
		 }
		 
		 return resultList; 
	}
	
	/**
	 * filters tasks and events only
	 * @param linesInFile
	 * @param type
	 * @param date
	 * @return the arraylist of indexes of tasks that is uncompleted and past deadline 
	 *          or arraylist of indexed of events that is uncompleted and past enddate
	 */
	public ArrayList<Integer> filterPastUncompleted(String[] linesInFile, String type){ 
		assert(type.equals(TYPE_TASK) || type.equals(TYPE_EVENT));
		
		ArrayList<Integer> resultList = new ArrayList<Integer>(); 
		
		for(int i = 0; i < linesInFile.length; i++){ 
			 String line = linesInFile[i];
			 if(isType(type, line) && isPast(type, line) &&isReqStatus(false, line)){
				 resultList.add(i); 
			 }
		 }

		return resultList; 
	}
	
	public ArrayList<Integer> filterPastUncompletedEvent(String[] linesInFile) { 		
		ArrayList<Integer> resultList = new ArrayList<Integer>(); 
		
		for (int i = 0; i < linesInFile.length; i++) { 
			 String line = linesInFile[i];
			 if (isType(TYPE_EVENT, line) && isPastDateAndTime(line) && isReqStatus(false, line)) {
				 resultList.add(i); 
			 }
		 }

		return resultList; 
	}
	
	/**
	 * filter method for search
	 * @param linesInFile
	 * @param type
	 * @param query
	 * @return arraylist of indexes of items that match at least one of the tokens in query 
	 */
	public ArrayList<Integer> matchTokensInQuery(String[] linesInFile, String type, String query){ 
		String[] tokens = parseQuery(query); 
		ArrayList<Integer> resultList = new ArrayList<Integer>(); 
		
		for(int i = 0; i < linesInFile.length; i++){ 
			String line = linesInFile[i];
			if(isType(type, line) && containToken(tokens, line)){
				resultList.add(i); 
			}
		}
		
		return resultList; 
	}
		
	//============================================
	// Private methods 
	//============================================
	
	private boolean isType(String type, String line){
		String[] lineFields = line.split(SEMICOLON);
		String lineType = lineFields[INDEX_TYPE];
		return lineType.equals(type); 
	}
	
	private boolean isOnDate(Date date, String line){
		String[] lineFields = line.split(SEMICOLON);
		Date lineDate = new Date(lineFields[INDEX_DUEDATE]);
		if(lineDate.compareTo(date) == 0){ 
			return true; 
		}
		else{ 
			return false; 
		}
	}
	
	private boolean isReqStatus(boolean isDone, String line){ 
		if(isDone){ 
			return isCompleted(line); 
		}
		else{
			return !isCompleted(line); 
		}
	}
	
	private boolean isOngoing(String line){
		String[] lineFields = line.split(SEMICOLON);
		
		assert(lineFields[INDEX_TYPE].equals(TYPE_EVENT));
		
		Date todayDate = Date.todayDate(); 
		Date startDate = new Date(lineFields[INDEX_STARTDATE]);
		Date endDate = new Date(lineFields[INDEX_ENDDATE]);
		
		String currentTime = Date.currentTime();
		String startTime = lineFields[INDEX_STARTTIME];
		String endTime = lineFields[INDEX_ENDTIME];
		
		return isTodayAfterStartPeriod(todayDate, startDate, currentTime, startTime) &&
				isTodayBeforeEndPeriod(todayDate, endDate, currentTime, endTime);
	}
	
	private boolean isTodayAfterStartPeriod(Date todayDate, Date startDate, String currentTime, String startTime) {
		if (startDate.compareTo(todayDate) > 0) {
			return false;
		} else if (startDate.compareTo(todayDate) == 0) {
			if (currentTime.compareTo(startTime) >= 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
	
	private boolean isTodayBeforeEndPeriod(Date todayDate, Date endDate, String currentTime, String endTime) {
		if (endDate.compareTo(todayDate) > 0) {
			return true;
		} else if (endDate.compareTo(todayDate) == 0) {
			if (currentTime.compareTo(endTime) <= 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private boolean isCompleted(String line){ 
		String[] lineFields = line.split(SEMICOLON);
		String lineIsDone = lineFields[INDEX_ISDONE];
		return lineIsDone.equals(DONE); 
	}
	
	private boolean isPast(String type, String line){
		assert(type.equals(TYPE_TASK) || type.equals(TYPE_EVENT));
		
		String[] lineFields = line.split(SEMICOLON);
		String lineDateStr = getPastDate(type, lineFields); 
		Date lineDate = new Date(lineDateStr); 
		Date todayDate = Date.todayDate(); 
		
		if(lineDate.compareTo(todayDate) < 0){
			return true; 
		}
		else{ 
			return false; 
		}
	}
	
	private boolean isPastTime(String line) {
		String[] lineFields = line.split(SEMICOLON);
		String lineTimeStr = lineFields[INDEX_ENDTIME];
		String currentTime = Date.currentTime();
		
		if (lineTimeStr.compareTo(currentTime) < 0) {
			return true; 
		}
		else{ 
			return false; 
		}
	}
	
	private boolean isPastDateAndTime(String line) {
		String[] lineFields = line.split(SEMICOLON);
		String lineDateStr = getPastDate(TYPE_EVENT, lineFields); 
		Date lineDate = new Date(lineDateStr); 
		Date todayDate = Date.todayDate(); 
		String endTime = lineFields[INDEX_ENDTIME];
		String currentTime = Date.currentTime();
		
		if (lineDate.compareTo(todayDate) < 0) {
			return true; 
		}
		else if ((lineDate.compareTo(todayDate) == 0) && (endTime.compareTo(currentTime) < 0)) { 
			return true; 
		} else {
			return false;
		}
	}
	
	/**
	 * get date to compare to today's date to determine if item is past 
	 * @param type
	 * @param lineFields
	 * @return deadline if type is task
	 *         end date if type id event 
	 */
	private String getPastDate(String type, String[] lineFields) {
		assert(type.equals(TYPE_TASK) || type.equals(TYPE_EVENT));
		if(type.equals(TYPE_TASK)){ 
			return lineFields[INDEX_DUEDATE]; 
		}
		else{
			return lineFields[INDEX_ENDDATE];
		}
	}
	
	private String[] parseQuery(String rawQuery){ 
		String query = rawQuery.trim().toLowerCase();
		String[] tokens = query.split(REGEX_WHITESPACES);
		return tokens; 
	}
	
	private boolean containToken(String[] tokens, String line){
		String[] lineFields = line.split(SEMICOLON);
		String lineName = lineFields[INDEX_NAME].toLowerCase();
		boolean result = false; 
		int i = 0; 
		
		while(!result && i < tokens.length){
			result = lineName.contains(tokens[i]);
			i++; 
		}
		
		return result; 
	}
}
