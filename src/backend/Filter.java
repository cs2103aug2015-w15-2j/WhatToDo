package backend;

import java.util.ArrayList;

import struct.Date;

public class Filter {
	
	private static final int INDEX_TYPE = 0; 
	private static final int INDEX_NAME = 1; 
	private static final int INDEX_ISDONE = 2; 
	private static final int INDEX_DUEDATE = 3;
	private static final int INDEX_ENDDATE = 5; 
	
//    private static final String TYPE_FLOAT = "float";
    private static final String TYPE_TASK = "task";
//    private static final String TYPE_EVENT = "event";
	
	private static final String DONE = "done"; 
	
	private static final String REGEX_WHITESPACE = "\\s+"; 
	
	private static final String SEMICOLON = ";";
	
	//============================================
	// Public methods
	//============================================
		
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
	
	public ArrayList<Integer> filterPastUncompleted(String[] linesInFile, String type){ 
		//assert type is not float 
		ArrayList<Integer> resultList = new ArrayList<Integer>(); 
		
		for(int i = 0; i < linesInFile.length; i++){ 
			 String line = linesInFile[i];
			 if(isType(type, line) && isPast(type, line) &&isReqStatus(false, line)){
				 resultList.add(i); 
			 }
		 }

		return resultList; 
	}
		
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
	
	//TODO match query with quotes 
	public String matchQuery(String query){
		
		//remove trailing quotes at the start and end 
		//use .contains
		
		return "stringmatches"; 
	}
	
	//============================================
	// Private methods 
	//============================================
	
	private boolean isType(String type, String line){
		String[] lineFields = line.split(SEMICOLON);
		String lineType = lineFields[INDEX_TYPE];
		return lineType.equals(type); 
	}
	
	private boolean isReqStatus(boolean isDone, String line){ 
		if(isDone){ 
			return isCompleted(line); 
		}
		else{
			return !isCompleted(line); 
		}
	}
	
	private boolean isCompleted(String line){ 
		String[] lineFields = line.split(SEMICOLON);
		String lineIsDone = lineFields[INDEX_ISDONE];
		return lineIsDone.equals(DONE); 
	}
	
	private boolean isPast(String type, String line){ 
		//assert type is not float 
		String[] lineFields = line.split(SEMICOLON);
		String lineDateStr = (type.equals(TYPE_TASK)) ? lineFields[INDEX_DUEDATE] : lineFields[INDEX_ENDDATE]; 
		Date lineDate = new Date(lineDateStr); 
		Date todayDate = Date.todayDate(); 
		
		if(lineDate.compareTo(todayDate) < 0){
			return true; 
		}
		else{ 
			return false; 
		}
	}
	
	private String[] parseQuery(String rawQuery){ 
		String query = rawQuery.trim().toLowerCase();
		String[] tokens = query.split(REGEX_WHITESPACE);
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
