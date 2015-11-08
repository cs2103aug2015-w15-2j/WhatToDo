package test;

import static org.junit.Assert.*;

import org.junit.Test;

import backend.CommandParser;
import struct.Command;
import struct.Date;

public class CommandParserTest {
	
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
	
	@Test
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
	}
	
	@Test
	public void testInvalid() {
		String userInput = "invalid command";
		CommandParser parser = new CommandParser();
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String input = command.getUserInput();
		assertEquals("invalid command", input);
		assertEquals(Command.CommandType.INVALID, type);
	}

}
