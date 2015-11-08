package test;

import static org.junit.Assert.*;

import org.junit.Test;

import backend.CommandParser;
import struct.Command;
import struct.Date;

public class CommandParserTest {
	
	
	// *******************************************************************
	// *******************************************************************
	// FOR ADD COMMAND
	// *******************************************************************
	// *******************************************************************
	
	@Test
	public void testAddFloatingTask() {
		String userInput = "add something";
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		String name = command.getName();
		assertEquals("something", name);
	}
	
	@Test
	public void testAddTask() {
		String userInput = "add something by 2311";
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		String name = command.getName();
		Date endDate = command.getDueDate();
		String endDateString = endDate.formatDateShort();
		Date testDate = new Date("231115");
		String testDateString = testDate.formatDateShort();
		assertEquals("something", name);
		assertEquals(testDateString, endDateString);
	}
	
	/**@Test
	public void testAddEvent() {
		String userInput = "add event from nfri 10pm to nsun 23";
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		String name = command.getName();
		Date endDate = command.getEndDate();
		System.out.println(name);
		String endDateString = endDate.formatDateShort();
		Date testEndDate = new Date("011115");
		String testEndDateString = testEndDate.formatDateShort();
		Date startDate = command.getStartDate();
		String startDateString = startDate.formatDateShort();
		Date testStartDate = new Date("301015");
		String testStartDateString = testStartDate.formatDateShort();
		String startTime = command.getStartTime();
		String endTime = command.getEndTime();
		assertEquals("event", name);
		assertEquals(testEndDateString, endDateString);
		assertEquals(testStartDateString, startDateString);
		assertEquals("2200", startTime);
		assertEquals("2300", endTime);
	}**/
	
	
	// *******************************************************************
	// *******************************************************************
	// FOR DELETE COMMAND
	// *******************************************************************
	// *******************************************************************
	
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
	
	// *******************************************************************
	// *******************************************************************
	// FOR DONE COMMAND
	// *******************************************************************
	// *******************************************************************
	
	// *******************************************************************
	// *******************************************************************
	// FOR SET COMMAND
	// *******************************************************************
	// *******************************************************************
	
	
	// *******************************************************************
	// *******************************************************************
	// FOR SAVE COMMAND
	// *******************************************************************
	// *******************************************************************
	
	@Test
	public void testSave() {
		String userInput = "save C:/Dropbox";
		CommandParser parser = new CommandParser();
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
		CommandParser parser = new CommandParser();
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
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		assertEquals(Command.CommandType.UNDO, type);
	}
	
	@Test
	public void testInvalidUndo() {
		String userInput = "undo 1";
		CommandParser parser = new CommandParser();
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
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		assertEquals(Command.CommandType.REDO, type);
	}
	
	@Test
	public void testInvalidRedo() {
		String userInput = "redo 1";
		CommandParser parser = new CommandParser();
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
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.ViewType viewType = command.getViewType();
		assertEquals(Command.CommandType.VIEW, type);
		assertEquals(Command.ViewType.ALL, viewType);
	}
	
	@Test
	public void testInvalidViewAll() {
		String userInput = "all something";
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("This command does not expect arguments.", errorMsg);
	}
	
	@Test
	public void testViewDef() {
		String userInput = "def";
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.ViewType viewType = command.getViewType();
		assertEquals(Command.CommandType.VIEW, type);
		assertEquals(Command.ViewType.DEF, viewType);
	}
	
	@Test
	public void testInvalidViewDef() {
		String userInput = "def something";
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("This command does not expect arguments.", errorMsg);
	}
	
	@Test
	public void testViewHist() {
		String userInput = "hist";
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.ViewType viewType = command.getViewType();
		assertEquals(Command.CommandType.VIEW, type);
		assertEquals(Command.ViewType.HIST, viewType);
	}
	
	@Test
	public void testInvalidViewHist() {
		String userInput = "hist something";
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("This command does not expect arguments.", errorMsg);
	}
	
	@Test
	public void testViewUnres() {
		String userInput = "unres";
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.ViewType viewType = command.getViewType();
		assertEquals(Command.CommandType.VIEW, type);
		assertEquals(Command.ViewType.UNRES, viewType);
	}
	
	@Test
	public void testInvalidViewUnres() {
		String userInput = "unres something";
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("This command does not expect arguments.", errorMsg);
	}
	
	@Test
	public void testViewSearch() {
		String userInput = "search";
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.ViewType viewType = command.getViewType();
		assertEquals(Command.CommandType.VIEW, type);
		assertEquals(Command.ViewType.SEARCH, viewType);
	}
	
	@Test
	public void testViewHelp() {
		String userInput = "help";
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.ViewType viewType = command.getViewType();
		assertEquals(Command.CommandType.VIEW, type);
		assertEquals(Command.ViewType.HELP, viewType);
	}
	
	@Test
	public void testInvalidViewHelp() {
		String userInput = "help something";
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("This command does not expect arguments.", errorMsg);
	}
	
	@Test
	public void testViewDone() {
		String userInput = "done";
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.ViewType viewType = command.getViewType();
		assertEquals(Command.CommandType.VIEW, type);
		assertEquals(Command.ViewType.DONE, viewType);
	}
	
	@Test
	public void testViewOpenfile() {
		String userInput = "openfile";
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.ViewType viewType = command.getViewType();
		assertEquals(Command.CommandType.VIEW, type);
		assertEquals(Command.ViewType.OPENFILE, viewType);
	}
	
	@Test
	public void testInvalidViewOpenfile() {
		String userInput = "openfile something";
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String errorMsg = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("This command does not expect arguments.", errorMsg);
	}
	
	@Test
	public void testViewConfig() {
		String userInput = "config";
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.ViewType viewType = command.getViewType();
		assertEquals(Command.CommandType.VIEW, type);
		assertEquals(Command.ViewType.CONFIG, viewType);
	}
	
	@Test
	public void testInvalidViewConfig() {
		String userInput = "config something";
		CommandParser parser = new CommandParser();
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
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		assertEquals(Command.CommandType.EXIT, type);
	}
	
	@Test
	public void testInvalidExit() {
		String userInput = "exit software";
		CommandParser parser = new CommandParser();
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
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String input = command.getUserInput();
		assertEquals(userInput, input);
		assertEquals(Command.CommandType.INVALID, type);
	}

}
