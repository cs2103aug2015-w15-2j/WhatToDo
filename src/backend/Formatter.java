/**
 * This class formats items to display on the application 
 * 
 * @@author A0127051U
 */

package backend;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import struct.Date;

public class Formatter {

	private static final int INDEX_TYPE = 0; 
	private static final int INDEX_NAME = 1; 
	private static final int INDEX_ISDONE = 2; 
	private static final int INDEX_DUEDATE = 3;
	private static final int INDEX_STARTDATE = 3; 
	private static final int INDEX_STARTTIME = 4; 
	private static final int INDEX_ENDDATE = 5; 
	private static final int INDEX_ENDTIME = 6; 
	
	private static final String DISPLAY_NO_ITEMS = "There are no items to display.\n"; 
    private static final String DISPLAY_FORMAT_FLOAT_OR_TASK = "%s%d. %s\n"; 
    private static final String DISPLAY_FORMAT_EVENT = "%s%d. %s;Start: %s%s;End: %s %s\n"; 
    private static final String DISPLAY_FORMAT_DELETED_OR_MARKDONE = "%s \"%s\"";
    
    private static final String DISPLAY_LAYOUT_ALL_TASK = "%s\nFLOAT\n%s"; 
    private static final String DISPLAY_LAYOUT_DEFAULT_TASK = "TODAY - %s \n%s\nTOMORROW - %s \n%s\nDAY AFTER - %s \n%s\nFLOAT\n%s";
    private static final String DISPLAY_LAYOUT_DEFAULT_EVENT = "ONGOING\n%s\nTODAY - %s \n%s\nTOMORROW - %s \n%s\nDAY AFTER - %s \n%s";
    private static final String DISPLAY_LAYOUT_SEARCH_RESULTS = "Showing results for \"%s\"\nTASK\n%s\nFLOAT\n%s\nEVENT\n%s"; 
    
    private static final String TYPE_FLOAT = "float";
    private static final String TYPE_TASK = "task";
    private static final String TYPE_EVENT = "event";
    
	private static final String SEMICOLON = ";";
	private static final String NEWLINE = "\n";
	private static final String EMPTYSTRING = ""; 
	private static final String SPACE = " "; 
	
	private static final String REGEX_24_HOUR_TIME = "([01]?[0-9]|2[0-3])[0-5][0-9]";
	
	//============================================
	// Public methods 
	//============================================
	
	/**
	 * formats content to display in events in default view 
	 * @param linesInFile
	 * @param eventOngoingIndexList
	 * @param eventTodayIndexList
	 * @param eventTmrIndexList
	 * @return formatted string
	 */
	public String formatDefEventView(String[] linesInFile, ArrayList<Integer> eventOngoingIndexList, 
			ArrayList<Integer> eventTodayIndexList, ArrayList<Integer> eventTmrIndexList,
			ArrayList<Integer> event2DaysIndexList){ 
		
		String ongoingContent = formatEventWithoutHeaders(linesInFile, eventOngoingIndexList, true); 
		String todayContent = formatEventWithoutHeaders(linesInFile, eventTodayIndexList, false); 
		String tmrContent = formatEventWithoutHeaders(linesInFile, eventTmrIndexList, false); 
		String twoDaysContent = formatEventWithoutHeaders(linesInFile, event2DaysIndexList, false); 
		String todayDate = Date.todayDateLong(); 
		String tmrDate = Date.tomorrowDateLong();
		String twoDaysDate = Date.twoDaysDateLong();
		
		return String.format(DISPLAY_LAYOUT_DEFAULT_EVENT, ongoingContent, todayDate, todayContent, 
        		tmrDate, tmrContent, twoDaysDate, twoDaysContent).trim();
	}

	/**
	 * formats content to display in tasks in default view 
	 * @param linesInFile
	 * @param taskTodayIndexList
	 * @param taskTmrIndexList
	 * @param floatIndexList
	 * @return formatted string
	 */
	public String formatDefTaskView(String[] linesInFile, ArrayList<Integer> taskTodayIndexList, 
			ArrayList<Integer> taskTmrIndexList, ArrayList<Integer> task2DaysIndexList, 
			ArrayList<Integer> floatIndexList){ 
		
		String taskTodayContent = formatFloatOrTaskWithoutHeaders(linesInFile, taskTodayIndexList, false); 
		String taskTmrContent = formatFloatOrTaskWithoutHeaders(linesInFile, taskTmrIndexList, false); 
		String task2DaysContent = formatFloatOrTaskWithoutHeaders(linesInFile, task2DaysIndexList, false);
		String floatContent = formatFloatOrTaskWithoutHeaders(linesInFile, floatIndexList, false); 
		String todayDate = Date.todayDateLong(); 
		String tmrDate = Date.tomorrowDateLong(); 
		String twoDaysDate = Date.twoDaysDateLong();
		
        return String.format(DISPLAY_LAYOUT_DEFAULT_TASK, todayDate, taskTodayContent, 
        		tmrDate, taskTmrContent, twoDaysDate, task2DaysContent,floatContent).trim();
	}
	
	/**
	 * formats content to display tasks in all view 
	 * @param linesInFile
	 * @param taskIndexList
	 * @param floatIndexList
	 * @return formatted string
	 */
	public String formatAllTaskView(String[] linesInFile, 
			ArrayList<Integer> taskIndexList, ArrayList<Integer> floatIndexList){ 
		
		String taskContent = formatTaskWithHeaders(linesInFile, taskIndexList, false); 
		String floatContent = formatFloatOrTaskWithoutHeaders(linesInFile, floatIndexList, false);
		
		return String.format(DISPLAY_LAYOUT_ALL_TASK, taskContent, floatContent).trim(); 
	}
	
	/**
	 * formats content to display in search view 
	 * @param query
	 * @param linesInFile
	 * @param taskResults
	 * @param floatResults
	 * @param eventResults
	 * @return formatted string
	 */
	public String formatSearchResults(String query, String[] linesInFile, ArrayList<Integer> taskResults,
			ArrayList<Integer> floatResults, ArrayList<Integer> eventResults){ 
		
		String taskContent = formatTaskWithHeaders(linesInFile, taskResults, true);
		String floatContent = formatFloatOrTaskWithoutHeaders(linesInFile, floatResults, true);
		String eventContent = formatEventWithHeaders(linesInFile, eventResults, true); 
		
		return String.format(DISPLAY_LAYOUT_SEARCH_RESULTS, query, taskContent, floatContent, eventContent);
		
	}
	
	/**
	 * formats search results if there is error 
	 * @param query
	 * @param errorMsg
	 * @return 
	 */
	public String formatSearchError(String query, String errorMsg){ 		
		return String.format(DISPLAY_LAYOUT_SEARCH_RESULTS, query, errorMsg, errorMsg, errorMsg);
	}

	/**
	 * formats float or tasks into list with no date headers 
	 * @param linesInFile
	 * @param result
	 * @param includeStatus - true if isDoneStr is required, false if not required 
	 * @return formatted string 
	 */
	public String formatFloatOrTaskWithoutHeaders(String[] linesInFile, 
			ArrayList<Integer> result, boolean includeStatus){
		
		StringBuffer contentBuffer = new StringBuffer();
		for(int i : result){ 
			String line = linesInFile[i]; 
			String[] lineFields = line.split(SEMICOLON);

			assert(lineFields[INDEX_TYPE].equals(TYPE_FLOAT) ||
					lineFields[INDEX_TYPE].equals(TYPE_TASK)); 
			
			String lineName = lineFields[INDEX_NAME];
			String lineIsDone = (includeStatus) ? lineFields[INDEX_ISDONE] + SPACE : EMPTYSTRING;
			
			String formattedLine = String.format(DISPLAY_FORMAT_FLOAT_OR_TASK, lineIsDone, i+1, lineName);
			contentBuffer.append(formattedLine); 
		}
		
		return addMsgIfEmpty(contentBuffer); 
	}
	
	/**
	 * formats tasks into list with date headers 
	 * @param linesInFile
	 * @param result
	 * @param includeStatus - true if isDoneStr is required, false if not required 
	 * @return formatted string 
	 */
	public String formatTaskWithHeaders(String[] linesInFile, ArrayList<Integer> result, boolean includeStatus){
		StringBuffer contentBuffer = new StringBuffer();
		Date prevDeadline = null; 
		for(int i : result){ 
			String line = linesInFile[i]; 
			String[] lineFields = line.split(SEMICOLON);

			assert(lineFields[INDEX_TYPE].equals(TYPE_TASK)); 
			
			String lineName = lineFields[INDEX_NAME];
			String lineIsDone = (includeStatus) ? lineFields[INDEX_ISDONE] + SPACE : EMPTYSTRING;
			Date currDeadline = new Date(lineFields[INDEX_DUEDATE]);
			
			prevDeadline = addDateHeader(contentBuffer, currDeadline, prevDeadline);
			String formattedLine = String.format(DISPLAY_FORMAT_FLOAT_OR_TASK, lineIsDone, i+1, lineName);
			contentBuffer.append(formattedLine); 
		}
	
		return addMsgIfEmpty(contentBuffer); 
	}
	
	/**
	 * formatting of events into a list that with no date headers
	 * @param linesInFile
	 * @param result
	 * @param includeStartDate - true if startDate is required; false if startDate is not required 
	 * @return formatted string 
	 */
	public String formatEventWithoutHeaders(String[] linesInFile, ArrayList<Integer> result, boolean includeStartDate){ 
		StringBuffer contentBuffer = new StringBuffer(); 
		for(int i : result){ 
			String line = linesInFile[i]; 
			String[] lineFields = line.split(SEMICOLON);

			assert(lineFields[INDEX_TYPE].equals(TYPE_EVENT)); 
			
			String lineName = lineFields[INDEX_NAME];
			String lineIsDone = EMPTYSTRING;
			String lineStartTime = formatTime(lineFields[INDEX_STARTTIME]); 
			String lineEndTime = formatTime(lineFields[INDEX_ENDTIME]); 
			Date startDate = new Date(lineFields[INDEX_STARTDATE]); 
			String currStartDate = (includeStartDate)? startDate.formatDateMedium() + SPACE : EMPTYSTRING; 
			Date currEndDate = new Date(lineFields[INDEX_ENDDATE]);
			
			String formattedLine = String.format(DISPLAY_FORMAT_EVENT, lineIsDone, i+1, 
					lineName, currStartDate, lineStartTime, currEndDate.formatDateMedium(), lineEndTime);
			contentBuffer.append(formattedLine); 	
		}
		
		return addMsgIfEmpty(contentBuffer); 
	}
	
	/**
	 * formatting of events into a list that include date headers
	 * @param linesInFile
	 * @param result 
	 * @param includeStatus - true if isDoneStr require, false if not required
	 * @return
	 */
	public String formatEventWithHeaders(String[] linesInFile, ArrayList<Integer> result, boolean includeStatus){ 
		StringBuffer contentBuffer = new StringBuffer();
		Date prevStartDate = null; 
		for(int i : result){ 
			String line = linesInFile[i]; 
			String[] lineFields = line.split(SEMICOLON);

			assert(lineFields[INDEX_TYPE].equals(TYPE_EVENT)); 
			
			String lineName = lineFields[INDEX_NAME];
			String lineIsDone = (includeStatus) ? lineFields[INDEX_ISDONE] + SPACE : EMPTYSTRING;
			String lineStartTime = formatTime(lineFields[INDEX_STARTTIME]); 
			String lineEndTime = formatTime(lineFields[INDEX_ENDTIME]); 
			Date currEndDate = new Date(lineFields[INDEX_ENDDATE]);
			Date currStartDate = new Date(lineFields[INDEX_STARTDATE]);
			
			prevStartDate = addDateHeader(contentBuffer, currStartDate, prevStartDate); 
			String formattedLine = String.format(DISPLAY_FORMAT_EVENT, lineIsDone, i+1, 
					lineName, EMPTYSTRING, lineStartTime, currEndDate.formatDateMedium(), lineEndTime);
			contentBuffer.append(formattedLine); 
		}
		
		return addMsgIfEmpty(contentBuffer); 
	}
	
	/**
	 * extracts the type and name of the line 
	 * @param line
	 * @return formatted string in this format: <type> "<name>"
	 */
	public String formatDeleteOrDoneLine(String line){
		String[] lineComponents = line.split(SEMICOLON);
		String type = lineComponents[INDEX_TYPE]; 
		String name = lineComponents[INDEX_NAME];
		return String.format(DISPLAY_FORMAT_DELETED_OR_MARKDONE, type, name); 
	}
		
	//============================================
	// Private methods 
	//============================================
	
    private String addMsgIfEmpty(StringBuffer buffer){
    	if(buffer.length() == 0){ 
    		buffer.append(DISPLAY_NO_ITEMS);
    	}
    	return buffer.toString().trim();
    }
    
    /**
     * add date headers to sb if the currDate is different from prevDate
     * @param sb
     * @param currDate
     * @param prevDate
     * @return currDate
     */
    private Date addDateHeader(StringBuffer sb, Date currDate, Date prevDate){
    	if(prevDate == null || currDate.compareTo(prevDate)!= 0){
    		String dateHeader = currDate.formatDateLong(); 
    		sb.append(dateHeader + NEWLINE); 
    	}
    	return currDate;
    }
    
    /**
     * format time to 12h format
     * @param time in 24h format
     * @return formatted time string
     */
    private String formatTime(String time){
    	assert(time.matches(REGEX_24_HOUR_TIME)); 
    	
    	Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0,2)));
		cal.set(Calendar.MINUTE, Integer.parseInt(time.substring(2)));
    	SimpleDateFormat sdf = new SimpleDateFormat("h:mm a"); 
    	return sdf.format(cal.getTime());
    }
}
