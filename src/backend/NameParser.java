package backend;

import java.util.List;

public class NameParser {
	
    private static final String STRING_ONE_SPACE = " ";
    private static final String ESCAPE_CHARACTER = "\\";
	
	public NameParser() {
		
	}
	
	protected String getName(List<String> arguments) {
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

}
