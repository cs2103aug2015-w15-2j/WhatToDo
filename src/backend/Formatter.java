package backend;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import struct.Date;

public class Formatter {

//	private static final int INDEX_TYPE = 0; 
	private static final int INDEX_NAME = 1; 
	private static final int INDEX_ISDONE = 2; 
	private static final int INDEX_DUEDATE = 3;
	private static final int INDEX_STARTDATE = 3; 
	private static final int INDEX_STARTTIME = 4; 
	private static final int INDEX_ENDDATE = 5; 
	private static final int INDEX_ENDTIME = 6; 
	
	private static final String DISPLAY_NO_ITEMS = "There are no items to display.\n"; 
    private static final String DISPLAY_RESULTS_FLOAT_OR_TASK = "%s%d. %s\n"; 
    private static final String DISPLAY_FORMAT_EVENT = "%s%d. %s;Start: %s         End: %s %s\n"; 
    
	private static final String SEMICOLON = ";";
	private static final String NEWLINE = "\n";
	private static final String EMPTYSTRING = ""; 
	private static final String SPACE = " "; 
	
	//============================================
	// Public methods
	//============================================
    
	public String formatFloatOrTaskWithoutHeaders(String[] linesInFile, ArrayList<Integer> result, boolean includeStatus){
		StringBuffer contentBuffer = new StringBuffer();
		for(int i : result){ 
			String line = linesInFile[i]; 
			String[] lineFields = line.split(SEMICOLON);
//			String lineType = lineFields[INDEX_TYPE];
//			assert items is float or task  
			String lineName = lineFields[INDEX_NAME];
			String lineIsDone = (includeStatus) ? lineFields[INDEX_ISDONE] + SPACE : EMPTYSTRING;
			
			String formattedLine = String.format(DISPLAY_RESULTS_FLOAT_OR_TASK, lineIsDone, i+1, lineName);
			contentBuffer.append(formattedLine); 
		}
		
		return addMsgIfEmpty(contentBuffer); 
	}
	
	public String formatTaskWithHeaders(String[] linesInFile, ArrayList<Integer> result, boolean includeStatus){
		StringBuffer contentBuffer = new StringBuffer();
		Date prevDeadline = null; 
		for(int i : result){ 
			String line = linesInFile[i]; 
			String[] lineFields = line.split(SEMICOLON);
//			String lineType = lineFields[INDEX_TYPE];
//			assert items is task  
			String lineName = lineFields[INDEX_NAME];
			String lineIsDone = (includeStatus) ? lineFields[INDEX_ISDONE] + SPACE : EMPTYSTRING;
			
			Date currDeadline = new Date(lineFields[INDEX_DUEDATE]);
			prevDeadline = addDateHeader(contentBuffer, currDeadline, prevDeadline);
			
			String formattedLine = String.format(DISPLAY_RESULTS_FLOAT_OR_TASK, lineIsDone, i+1, lineName);
			contentBuffer.append(formattedLine); 
		}
		
		return addMsgIfEmpty(contentBuffer); 
	}

	public String formatEventWithoutHeaders(String[] linesInFile, ArrayList<Integer> result){ 
		StringBuffer contentBuffer = new StringBuffer(); 
		for(int i : result){ 
			String line = linesInFile[i]; 
			String[] lineFields = line.split(SEMICOLON);
//			String lineType = lineFields[INDEX_TYPE];
//			assert items is event  
			String lineName = lineFields[INDEX_NAME];
			String lineIsDone = EMPTYSTRING;
			String lineStartTime = formatTime(lineFields[INDEX_STARTTIME]); 
			String lineEndTime = formatTime(lineFields[INDEX_ENDTIME]); 
			Date currEndDate = new Date(lineFields[INDEX_ENDDATE]);
			
			String formattedLine = String.format(DISPLAY_FORMAT_EVENT, lineIsDone, i+1, 
					lineName, lineStartTime, currEndDate.formatDateMedium(), lineEndTime);
			contentBuffer.append(formattedLine); 	
		}
		
		return addMsgIfEmpty(contentBuffer); 
	}
		
	public String formatEventWithHeaders(String[] linesInFile, ArrayList<Integer> result, boolean includeStatus){ 
		StringBuffer contentBuffer = new StringBuffer();
		Date prevStartDate = null; 
		for(int i : result){ 
			String line = linesInFile[i]; 
			String[] lineFields = line.split(SEMICOLON);
//			String lineType = lineFields[INDEX_TYPE];
//			assert items is event  
			String lineName = lineFields[INDEX_NAME];
			String lineIsDone = (includeStatus) ? lineFields[INDEX_ISDONE] + SPACE : EMPTYSTRING;
			String lineStartTime = formatTime(lineFields[INDEX_STARTTIME]); 
			String lineEndTime = formatTime(lineFields[INDEX_ENDTIME]); 
			Date currEndDate = new Date(lineFields[INDEX_ENDDATE]);
			
			Date currStartDate = new Date(lineFields[INDEX_STARTDATE]);
			prevStartDate = addDateHeader(contentBuffer, currStartDate, prevStartDate); 
			
			String formattedLine = String.format(DISPLAY_FORMAT_EVENT, lineIsDone, i+1, 
					lineName, lineStartTime, currEndDate.formatDateMedium(), lineEndTime);
			contentBuffer.append(formattedLine); 
		}
		
		return addMsgIfEmpty(contentBuffer); 
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
    
    private Date addDateHeader(StringBuffer sb, Date currDate, Date prevDate){
    	//add date header only if the currDate is different from the prevDate
    	if(prevDate == null || currDate.compareTo(prevDate)!= 0){
    		String dateHeader = currDate.formatDateLong(); 
    		sb.append(dateHeader + NEWLINE); 
    	}
    	return currDate;
    }
    
    private String formatTime(String time){
    	//assert time string is numeric
    	Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0,2)));
		cal.set(Calendar.MINUTE, Integer.parseInt(time.substring(2)));
    	SimpleDateFormat sdf = new SimpleDateFormat("h:mm a"); 
    	return sdf.format(cal.getTime());
    }
}
