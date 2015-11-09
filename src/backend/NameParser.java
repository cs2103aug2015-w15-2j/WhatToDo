/**
 * This class parses user input string for name.
 * 
 * @@author A0124099B
 */

package backend;

import java.util.ArrayList;
import java.util.List;

public class NameParser {
	
	private static final int POSITION_SECOND_INDEX = 1;

	private static final String STRING_ONE_SPACE = " ";
	private static final String ESCAPE_CHARACTER = "\\";
	
	protected String getName(ArrayList<String> arguments, int startIndex, int endIndex) {
		List<String> nameList = arguments.subList(startIndex, endIndex);
		return getName(nameList);
	}
	
	protected String getEventName(ArrayList<String> arguments, int keywordToIndex, int keywordFromIndex) {
		String name = null;
		if (keywordToIndex > keywordFromIndex) {
			name = getName(arguments, CommandParser.POSITION_FIRST_INDEX, keywordFromIndex);
		} else {
			name = getName(arguments, CommandParser.POSITION_FIRST_INDEX, keywordToIndex);
		}
		return name;
	}
	
	private String getName(List<String> arguments) {
		String name = "";
		if (arguments.isEmpty()) {
			return null;
		}
		for (int i = 0; i < arguments.size(); i++) {
			String currArgument = arguments.get(i);
			// getName method will remove any ESCAPE_CHARACTERS in each String
			if (currArgument.startsWith(ESCAPE_CHARACTER)) {
				currArgument = currArgument.substring(POSITION_SECOND_INDEX);
			}
			name += currArgument + STRING_ONE_SPACE;
		}
		return name.trim();
	}

}
