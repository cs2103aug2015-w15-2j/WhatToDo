package backend;

import java.util.ArrayList;
import java.util.List;

//@@author A0124099B
public class NameParser {
	
	private static final int POSITION_FIRST_INDEX = 0;

	private static final String STRING_ONE_SPACE = " ";
	private static final String ESCAPE_CHARACTER = "\\";

	public NameParser() {

	}

	private String getName(List<String> arguments) {
		String name = "";
		if (arguments.isEmpty()) {
			return null;
		}
		for (int i = 0; i < arguments.size(); i++) {
			String currArgument = arguments.get(i);
			if (currArgument.startsWith(ESCAPE_CHARACTER)) {
				currArgument = currArgument.substring(1);
			}
			name += currArgument + STRING_ONE_SPACE;
		}
		return name.trim();
	}
	
	protected String getName(ArrayList<String> arguments, int startIndex, int endIndex) {
		List<String> nameList = arguments.subList(startIndex, endIndex);
		return getName(nameList);
	}
	
	protected String getEventName(ArrayList<String> arguments, int keywordToIndex, int keywordFromIndex) {
		String name = null;
		if (keywordToIndex > keywordFromIndex) {
			name = getName(arguments, POSITION_FIRST_INDEX, keywordFromIndex);
		} else {
			name = getName(arguments, POSITION_FIRST_INDEX, keywordToIndex);
		}
		return name;
	}

}
