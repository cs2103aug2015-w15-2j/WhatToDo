package backend;

import java.util.ArrayList;

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
    private static final String SEARCH_RESULTS_FLOAT_OR_TASK = "%s %d. %s\n"; 
    private static final String SEARCH_RESULTS_EVENT = "%s %d. [%s %s - %s %s] %s\n"; 
    
	private static final String SEMICOLON = ";";
	private static final String NEWLINE = "\n";
    
	public Formatter(){
		
	}
	
	public String formatFloatResults(String[] linesInFile, ArrayList<Integer> result){
		StringBuffer contentBuffer = new StringBuffer();
		for(int i : result){ 
			String line = linesInFile[i]; 
			String[] lineFields = line.split(SEMICOLON);
//			String lineType = lineFields[INDEX_TYPE];
//			assert items is floating task  
			String lineName = lineFields[INDEX_NAME];
			String lineIsDone = lineFields[INDEX_ISDONE];
			
			String formattedLine = String.format(SEARCH_RESULTS_FLOAT_OR_TASK, lineIsDone, i+1, lineName);
			contentBuffer.append(formattedLine); 
		}
		
		return addMsgIfEmpty(contentBuffer); 
	}
	
	public String formatTaskResults(String[] linesInFile, ArrayList<Integer> result){ 
		StringBuffer contentBuffer = new StringBuffer();
		Date prevlineDate = null; 
		for(int i : result){ 
			String line = linesInFile[i]; 
			String[] lineFields = line.split(SEMICOLON);
//			String lineType = lineFields[INDEX_TYPE];
//			assert items is task  
			String lineName = lineFields[INDEX_NAME];
			String lineIsDone = lineFields[INDEX_ISDONE];
			
			Date lineDate = new Date(lineFields[INDEX_DUEDATE]);
			addDateHeader(contentBuffer, lineDate, prevlineDate); 
			
			String formattedLine = String.format(SEARCH_RESULTS_FLOAT_OR_TASK, lineIsDone, i+1, lineName);
			contentBuffer.append(formattedLine); 
		}
		
		return addMsgIfEmpty(contentBuffer); 
	}
	
	public String formatEventResults(String[] linesInFile, ArrayList<Integer> result){ 
		StringBuffer contentBuffer = new StringBuffer();
		Date prevlineDate = null; 
		for(int i : result){ 
			String line = linesInFile[i]; 
			String[] lineFields = line.split(SEMICOLON);
//			String lineType = lineFields[INDEX_TYPE];
//			assert items is event  
			String lineName = lineFields[INDEX_NAME];
			String lineIsDone = lineFields[INDEX_ISDONE];
			String lineStartDate = lineFields[INDEX_STARTDATE]; 
			String lineEndDate = lineFields[INDEX_ENDDATE];
			String lineStartTime = lineFields[INDEX_STARTTIME]; 
			String lineEndTime = lineFields[INDEX_ENDTIME]; 
			
			Date lineDate = new Date(lineFields[INDEX_STARTDATE]);
			addDateHeader(contentBuffer, lineDate, prevlineDate); 
			
			String formattedLine = String.format(SEARCH_RESULTS_EVENT, lineIsDone, i+1, 
					lineStartDate, lineStartTime, lineEndDate, lineEndTime, lineName);
			contentBuffer.append(formattedLine); 
		}
		
		return addMsgIfEmpty(contentBuffer); 
	}
	
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
}
