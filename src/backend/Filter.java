package backend;

import java.util.ArrayList;

public class Filter {
	
	private static final int INDEX_TYPE = 0; 
	private static final int INDEX_NAME = 1; 
	private static final int INDEX_ISDONE = 2; 
	private static final int INDEX_DUEDATE = 3;
	private static final int INDEX_STARTDATE = 3; 
	private static final int INDEX_STARTTIME = 4; 
	private static final int INDEX_ENDDATE = 5; 
	private static final int INDEX_ENDTIME = 6;
	
	private static final String DONE = "done"; 
	
	private static final String REGEX_WHITESPACE = "\\s+"; 
	
	private static final String SEMICOLON = ";";

	public Filter(){
		
	}
	
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
	
	private boolean isType(String type, String line){
		String[] lineFields = line.split(SEMICOLON);
		String lineType = lineFields[INDEX_TYPE];
		return lineType.equals(type); 
	}
	
	//TODO match query with quotes 
	public String matchQuery(String query){
		
		//remove trailing quotes at the start and end 
		//use .contains
		
		return "stringmatches"; 
	}
	
	//TODO filter for views 
}
