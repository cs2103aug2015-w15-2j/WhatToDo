package test;

import static org.junit.Assert.*;

import java.util.Hashtable;

import org.junit.Test;

import backend.CommandParser;
import struct.Command;
import struct.Date;

public class CommandParserTest {
	
	private Hashtable<String, String> commandAliases = new Hashtable<String, String>();
	private CommandParser parser = new CommandParser(commandAliases);
	private Date todayDate = Date.todayDate();
	private String todayDateString = todayDate.formatDateShort();
	
	
	// *******************************************************************
	// *******************************************************************
	// FOR ADD COMMAND
	// *******************************************************************
	// *******************************************************************
	
	@Test
	public void testAddFloatingTask() {
		String userInput = "add something";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.DataType dataType = command.getDataType();
		String name = command.getName();
		assertEquals("something", name);
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.FLOATING_TASK, dataType);
	}
	
	@Test
	public void testAddTask() {
		String userInput = "add something by 3112";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.DataType dataType = command.getDataType();
		String name = command.getName();
		Date endDate = command.getDueDate();
		String endDateString = endDate.formatDateShort();
		Date testDate = new Date("311215");
		String testDateString = testDate.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.TASK, dataType);
		assertEquals("something", name);
		assertEquals(testDateString, endDateString);
	}
	
	@Test
	public void testInvalidAddTaskDate() {
		String userInput = "add something by 3212";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid deadline.", name);
	}
	
	@Test
	public void testInvalidAddTaskName() {
		String userInput = "add by 3112";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Task name required.", name);
	}
	
	@Test
	public void testAddSingleDayEvent() {
		String userInput = "add event on today from 2pm to 3pm";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.DataType dataType = command.getDataType();
		String name = command.getName();
		Date endDate = command.getEndDate();
		String endDateString = endDate.formatDateShort();
		Date startDate = command.getStartDate();
		String startDateString = startDate.formatDateShort();
		String startTime = command.getStartTime();
		String endTime = command.getEndTime();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(todayDateString, endDateString);
		assertEquals(todayDateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	
	// *******************************************************************
	// *******************************************************************
	// FOR DELETE COMMAND
	// *******************************************************************
	// *******************************************************************
	
	@Test
	public void testDeleteIndex() {
		String userInput = "delete 1";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		int index = command.getIndex();
		assertEquals(Command.CommandType.DELETE, type);
		assertEquals(1, index);
	}
	
	@Test
	public void testInvalidDeleteEmpty() {
		String userInput = "delete";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Index/alias command required.", errorMsg);
	}
	
	@Test
	public void testInvalidDeleteArgument() {
		String userInput = "delete two words";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid index or alias.", errorMsg);
	}
	
	// *******************************************************************
	// *******************************************************************
	// FOR EDIT COMMAND
	// *******************************************************************
	// *******************************************************************
	
	// *******************************************************************
	// *******************************************************************
	// FOR SEARCH COMMAND
	// *******************************************************************
	// *******************************************************************
	
	@Test
	public void testSearchOneKeyword() {
		String userInput = "search something";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String keyword = command.getName();
		assertEquals(Command.CommandType.SEARCH, type);
		assertEquals("something", keyword);
	}
	
	@Test
	public void testSearchTwoKeywords() {
		String userInput = "search two keywords";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String keywords = command.getName();
		assertEquals(Command.CommandType.SEARCH, type);
		assertEquals("two keywords", keywords);
	}
	
	// *******************************************************************
	// *******************************************************************
	// FOR DONE COMMAND
	// *******************************************************************
	// *******************************************************************
	
	@Test
	public void testDone() {
		String userInput = "done 1";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		int index = command.getIndex();
		assertEquals(Command.CommandType.DONE, type);
		assertEquals(1, index);
	}
	
	@Test
	public void testInvalidDoneString() {
		String userInput = "done something";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid index.", errorMsg);
	}
	
	@Test
	public void testInvalidDoneIndex() {
		String userInput = "done -1";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid index.", errorMsg);
	}
	
	@Test
	public void testInvalidDoneArgument() {
		String userInput = "done 1 1";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid index.", errorMsg);
	}
	
	// *******************************************************************
	// *******************************************************************
	// FOR SET COMMAND
	// *******************************************************************
	// *******************************************************************
	
	@Test
	public void testSet() {
		String userInput = "set create as add";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String alias = command.getName();
		String originalCommand = command.getOriginalCommand();
		assertEquals(Command.CommandType.SET, type);
		assertEquals("create", alias);
		assertEquals("add", originalCommand);
	}
	
	@Test
	public void testInvalidSetEmpty() {
		String userInput = "set";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Command and alias required.", errorMsg);
	}
	
	@Test
	public void testInvalidSetCommandFormat() {
		String userInput = "set something as add task";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid set format.", errorMsg);
	}

	@Test
	public void testInvalidSetKeywordAsFormat() {
		String userInput = "set something for add";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid set format.", errorMsg);
	}
	
	@Test
	public void testInvalidSetAlias() {
		String userInput = "set add as add";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Input alias is a either a registered command and cannot be used or an alias-in-use.", errorMsg);
	}
	
	@Test
	public void testInvalidSetPositiveIntegerAlias() {
		String userInput = "set 1 as add";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Positive integers cannot be used as aliases.", errorMsg);
	}
	
	@Test
	public void testInvalidSetCommand() {
		String userInput = "set create as include";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("include is not a registered command.", errorMsg);
	}
	
	@Test
	public void testSetAndDeleteAlias() {
		parser.setAlias("create", "add");
		String userInput = "delete create";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.DELETEALIAS, type);
		assertEquals("create", name);
		parser.deleteAliasFromHash("create");
		Command command2 = parser.parse(userInput);
		Command.CommandType type2 = command2.getCommandType();
		String errorMsg = command2.getName();
		assertEquals(Command.CommandType.INVALID, type2);
		assertEquals("Alias create has not been set.", errorMsg);
	}
	
	// *******************************************************************
	// *******************************************************************
	// FOR SAVE COMMAND
	// *******************************************************************
	// *******************************************************************
	
	@Test
	public void testSave() {
		String userInput = "save C:/Dropbox";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String directory = command.getName();
		assertEquals(Command.CommandType.SAVE, type);
		assertEquals("C:/Dropbox", directory);
	}
	
	// To test empty argument
	@Test
	public void testInvalidSave() {
		String userInput = "save";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Directory required.", errorMsg);
	}
	
	// *******************************************************************
	// *******************************************************************
	// FOR UNDO COMMAND
	// *******************************************************************
	// *******************************************************************
	
	@Test
	public void testUndo() {
		String userInput = "undo";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		assertEquals(Command.CommandType.UNDO, type);
	}
	
	@Test
	public void testInvalidUndo() {
		String userInput = "undo 1";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("This command does not expect arguments.", errorMsg);
	}
	
	// *******************************************************************
	// *******************************************************************
	// FOR REDO COMMAND
	// *******************************************************************
	// *******************************************************************
	
	@Test
	public void testRedo() {
		String userInput = "redo";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		assertEquals(Command.CommandType.REDO, type);
	}
	
	@Test
	public void testInvalidRedo() {
		String userInput = "redo 1";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("This command does not expect arguments.", errorMsg);
	}
	
	// *******************************************************************
	// *******************************************************************
	// FOR VIEW COMMANDS
	// *******************************************************************
	// *******************************************************************
	
	@Test
	public void testViewAll() {
		String userInput = "all";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.ViewType viewType = command.getViewType();
		assertEquals(Command.CommandType.VIEW, type);
		assertEquals(Command.ViewType.ALL, viewType);
	}
	
	@Test
	public void testInvalidViewAll() {
		String userInput = "all something";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("This command does not expect arguments.", errorMsg);
	}
	
	@Test
	public void testViewDef() {
		String userInput = "def";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.ViewType viewType = command.getViewType();
		assertEquals(Command.CommandType.VIEW, type);
		assertEquals(Command.ViewType.DEF, viewType);
	}
	
	@Test
	public void testInvalidViewDef() {
		String userInput = "def something";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("This command does not expect arguments.", errorMsg);
	}
	
	@Test
	public void testViewHist() {
		String userInput = "hist";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.ViewType viewType = command.getViewType();
		assertEquals(Command.CommandType.VIEW, type);
		assertEquals(Command.ViewType.HIST, viewType);
	}
	
	@Test
	public void testInvalidViewHist() {
		String userInput = "hist something";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("This command does not expect arguments.", errorMsg);
	}
	
	@Test
	public void testViewUnres() {
		String userInput = "unres";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.ViewType viewType = command.getViewType();
		assertEquals(Command.CommandType.VIEW, type);
		assertEquals(Command.ViewType.UNRES, viewType);
	}
	
	@Test
	public void testInvalidViewUnres() {
		String userInput = "unres something";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("This command does not expect arguments.", errorMsg);
	}
	
	@Test
	public void testViewSearch() {
		String userInput = "search";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.ViewType viewType = command.getViewType();
		assertEquals(Command.CommandType.VIEW, type);
		assertEquals(Command.ViewType.SEARCH, viewType);
	}
	
	@Test
	public void testViewHelp() {
		String userInput = "help";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.ViewType viewType = command.getViewType();
		assertEquals(Command.CommandType.VIEW, type);
		assertEquals(Command.ViewType.HELP, viewType);
	}
	
	@Test
	public void testInvalidViewHelp() {
		String userInput = "help something";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("This command does not expect arguments.", errorMsg);
	}
	
	@Test
	public void testViewDone() {
		String userInput = "done";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.ViewType viewType = command.getViewType();
		assertEquals(Command.CommandType.VIEW, type);
		assertEquals(Command.ViewType.DONE, viewType);
	}
	
	@Test
	public void testViewOpenfile() {
		String userInput = "openfile";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.ViewType viewType = command.getViewType();
		assertEquals(Command.CommandType.VIEW, type);
		assertEquals(Command.ViewType.OPENFILE, viewType);
	}
	
	@Test
	public void testInvalidViewOpenfile() {
		String userInput = "openfile something";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("This command does not expect arguments.", errorMsg);
	}
	
	@Test
	public void testViewConfig() {
		String userInput = "config";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.ViewType viewType = command.getViewType();
		assertEquals(Command.CommandType.VIEW, type);
		assertEquals(Command.ViewType.CONFIG, viewType);
	}
	
	@Test
	public void testInvalidViewConfig() {
		String userInput = "config something";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("This command does not expect arguments.", errorMsg);
	}
	
	// *******************************************************************
	// *******************************************************************
	// FOR EXIT COMMAND
	// *******************************************************************
	// *******************************************************************
	
	@Test
	public void testExit() {
		String userInput = "exit";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		assertEquals(Command.CommandType.EXIT, type);
	}
	
	@Test
	public void testInvalidExit() {
		String userInput = "exit software";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("This command does not expect arguments.", errorMsg);
	}
	
	// *******************************************************************
	// *******************************************************************
	// FOR INVALID COMMAND
	// *******************************************************************
	// *******************************************************************
	
	@Test
	public void testInvalid() {
		String userInput = "random invalid command";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String input = command.getUserInput();
		assertEquals(userInput, input);
		assertEquals(Command.CommandType.INVALID, type);
	}

}
