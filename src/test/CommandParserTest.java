package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
	private String todayDayString = todayDate.getDayString().toLowerCase();
	private Date tmrDate = Date.tomorrowDate();
	private String tmrDateString = tmrDate.formatDateShort();
	private static final ArrayList<String> DAYS_ARRAY_LIST = new ArrayList<String>();
	
	private void initDaysArrayList() {
		DAYS_ARRAY_LIST.add("mon");
		DAYS_ARRAY_LIST.add("tue");
		DAYS_ARRAY_LIST.add("wed");
		DAYS_ARRAY_LIST.add("thu");
		DAYS_ARRAY_LIST.add("fri");
		DAYS_ARRAY_LIST.add("sat");
		DAYS_ARRAY_LIST.add("sun");
	}
	
	private Date getDate(String day) {
		initDaysArrayList();
		String daySubstring;
		boolean n = false;
		if (day.substring(0, 1).equals("n")) {
			n = true;
			daySubstring = day.substring(1, 4);
		} else {
			daySubstring = day.substring(0, 3);
		}
		int difference = DAYS_ARRAY_LIST.indexOf(daySubstring) - 
				         DAYS_ARRAY_LIST.indexOf(todayDayString.substring(0, 3));
		if (difference < 0) {
			difference += 7;
		}
		if (n) {
			difference += 7;
		}
		return todayDate.plusDay(difference);
	}
	
	
	// *******************************************************************
	// *******************************************************************
	// FOR ADD COMMAND
	// *******************************************************************
	// *******************************************************************
	
	// Floating task
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
	
	// Tasks with deadline
	@Test
	public void testAddTask() {
		String userInput = "add something by 3112";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		Command.DataType dataType = command.getDataType();
		String name = command.getName();
		Date endDate = command.getDueDate();
		String endDateString = endDate.formatDateShort();
		Date date = new Date("311215");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.TASK, dataType);
		assertEquals("something", name);
		assertEquals(dateString, endDateString);
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
	
	// Single day events
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
	
	@Test
	public void testAddSingleDayEventJumbled1() {
		String userInput = "add event on today to 3pm from 2pm";
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
	
	@Test
	public void testAddSingleDayEventJumbled2() {
		String userInput = "add event from 2pm on today to 3pm";
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
	
	@Test
	public void testAddSingleDayEventJumbled3() {
		String userInput = "add event from 2pm to 3pm on today";
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
	
	@Test
	public void testAddSingleDayEventJumbled4() {
		String userInput = "add event to 3pm from 2pm on today";
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
	
	@Test
	public void testAddSingleDayEventJumbled5() {
		String userInput = "add event to 3pm on today from 2pm";
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
	
	@Test
	public void testInvalidAddSingleDayEventFormat1() {
		String userInput = "add event on today from today 2pm to 3pm";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid format for adding event.", name);
	}
	
	@Test
	public void testInvalidAddSingleDayEventFormat2() {
		String userInput = "add event on today from 2pm to 3pm tmr";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid format for adding event.", name);
	}
	
	@Test
	public void testInvalidAddSingleDayEventEmpty() {
		String userInput = "add on today from 2pm to 3pm";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Event name required.", name);
	}
	
	@Test
	public void testInvalidAddSingleDayEventDate() {
		String userInput = "add event on todayy from 2pm to 3pm";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid date.", name);
	}
	
	@Test
	public void testInvalidAddSingleDayEventStartTime() {
		String userInput = "add event on today from 2401 to 3pm";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid start time.", name);
	}
	
	@Test
	public void testInvalidAddSingleDayEventEndTime() {
		String userInput = "add event on today from 2pm to 2401";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid end time.", name);
	}
	
	@Test
	public void testInvalidAddSingleDayEventTimes1() {
		String userInput = "add event on today from 3pm to 3pm";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Start time later than or equal to end time for single-day event.", name);
	}
	
	@Test
	public void testInvalidAddSingleDayEventTimes2() {
		String userInput = "add event on today from 3pm to 2pm";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Start time later than or equal to end time for single-day event.", name);
	}
	
	// Multi-days event
	@Test
	public void testAddEvent() {
		String userInput = "add event from sun 2pm to sun 3pm";
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
		Date date = getDate("sun");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testAddEventJumbled() {
		String userInput = "add event to tomorrow 3pm from today 2pm";
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
		assertEquals(tmrDateString, endDateString);
		assertEquals(todayDateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testInvalidAddEventFormat1() {
		String userInput = "add event from sun 2 pm to sun 3pm";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid format for adding event.", name);
	}
	
	@Test
	public void testInvalidAddEventFormat2() {
		String userInput = "add event from sun 2pm to sun 3 pm";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid format for adding event.", name);
	}
	
	@Test
	public void testInvalidAddEventStartDate() {
		String userInput = "add event from sund 2pm to sun 3pm";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid start date.", name);
	}
	
	@Test
	public void testInvalidAddEventStartTime() {
		String userInput = "add event from sun 12345 to sun 3pm";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid start time.", name);
	}
	
	@Test
	public void testInvalidAddEventEndDate() {
		String userInput = "add event from sun 2pm to sund 3pm";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid end date.", name);
	}
	
	@Test
	public void testInvalidAddEventEndTime() {
		String userInput = "add event from sun 2pm to sun 12345";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid end time.", name);
	}
	
	@Test
	public void testInvalidAddEventTimes() {
		String userInput = "add event from sun 3pm to sun 2pm";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Start time later than or equal to end time for single-day event.", name);
	}
	
	@Test
	public void testInvalidAddEventDates() {
		String userInput = "add event from tomorrow 2pm to today 3pm";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Start date later than end date.", name);
	}
	
	@Test
	public void testInvalidAddEventEmpty() {
		String userInput = "add from sun 2pm to sun 3pm";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Event name required.", name);
	}
	
	// *******************************************************************
	// *******************************************************************
	// FOR DATE FORMATS
	// *******************************************************************
	// *******************************************************************
	
	@Test
	public void testDateTomorrow1() {
		String userInput = "add event on tomorrow from 2pm to 3pm";
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
		assertEquals(tmrDateString, endDateString);
		assertEquals(tmrDateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateTomorrow2() {
		String userInput = "add event on tmr from 2pm to 3pm";
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
		assertEquals(tmrDateString, endDateString);
		assertEquals(tmrDateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateTomorrow3() {
		String userInput = "add event on tomo from 2pm to 3pm";
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
		assertEquals(tmrDateString, endDateString);
		assertEquals(tmrDateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateMondayShort() {
		String userInput = "add event on mon from 2pm to 3pm";
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
		Date date = getDate("mon");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateMondayLong() {
		String userInput = "add event on monday from 2pm to 3pm";
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
		Date date = getDate("monday");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateTuesdayShort() {
		String userInput = "add event on tue from 2pm to 3pm";
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
		Date date = getDate("tue");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateTuesdayLong() {
		String userInput = "add event on tuesday from 2pm to 3pm";
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
		Date date = getDate("tuesday");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateWednesdayShort() {
		String userInput = "add event on wed from 2pm to 3pm";
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
		Date date = getDate("wed");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateWednesdayLong() {
		String userInput = "add event on wednesday from 2pm to 3pm";
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
		Date date = getDate("wednesday");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateThursdayShort() {
		String userInput = "add event on thu from 2pm to 3pm";
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
		Date date = getDate("thu");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateThursdayLong() {
		String userInput = "add event on thursday from 2pm to 3pm";
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
		Date date = getDate("thursday");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateFridayShort() {
		String userInput = "add event on fri from 2pm to 3pm";
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
		Date date = getDate("fri");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateFridayLong() {
		String userInput = "add event on friday from 2pm to 3pm";
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
		Date date = getDate("friday");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateSaturdayShort() {
		String userInput = "add event on sat from 2pm to 3pm";
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
		Date date = getDate("sat");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateSaturdayLong() {
		String userInput = "add event on saturday from 2pm to 3pm";
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
		Date date = getDate("saturday");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateSundayShort() {
		String userInput = "add event on sun from 2pm to 3pm";
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
		Date date = getDate("sun");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateSundayLong() {
		String userInput = "add event on sunday from 2pm to 3pm";
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
		Date date = getDate("sunday");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateNextMondayShort() {
		String userInput = "add event on nmon from 2pm to 3pm";
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
		Date date = getDate("nmon");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateNextMondayLong() {
		String userInput = "add event on nmonday from 2pm to 3pm";
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
		Date date = getDate("nmonday");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateNextTuesdayShort() {
		String userInput = "add event on ntue from 2pm to 3pm";
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
		Date date = getDate("ntue");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateNextTuesdayLong() {
		String userInput = "add event on ntuesday from 2pm to 3pm";
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
		Date date = getDate("ntuesday");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateNextWednesdayShort() {
		String userInput = "add event on nwed from 2pm to 3pm";
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
		Date date = getDate("nwed");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateNextWednesdayLong() {
		String userInput = "add event on nwednesday from 2pm to 3pm";
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
		Date date = getDate("nwednesday");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateNextThursdayShort() {
		String userInput = "add event on nthu from 2pm to 3pm";
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
		Date date = getDate("nthu");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateNextThursdayLong() {
		String userInput = "add event on nthursday from 2pm to 3pm";
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
		Date date = getDate("nthursday");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateNextFridayShort() {
		String userInput = "add event on nfri from 2pm to 3pm";
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
		Date date = getDate("nfri");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateNextFridayLong() {
		String userInput = "add event on nfriday from 2pm to 3pm";
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
		Date date = getDate("nfriday");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateNextSaturdayShort() {
		String userInput = "add event on nsat from 2pm to 3pm";
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
		Date date = getDate("nsat");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateNextSaturdayLong() {
		String userInput = "add event on nsaturday from 2pm to 3pm";
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
		Date date = getDate("nsaturday");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateNextSundayShort() {
		String userInput = "add event on nsun from 2pm to 3pm";
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
		Date date = getDate("nsun");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateNextSundayLong() {
		String userInput = "add event on nsunday from 2pm to 3pm";
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
		Date date = getDate("nsunday");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateFutureShort() {
		String userInput = "add event on 3112 from 2pm to 3pm";
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
		Date date = new Date("311215");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateFutureLong() {
		String userInput = "add event on 311215 from 2pm to 3pm";
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
		Date date = new Date("311215");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testDateLeapYear() {
		String userInput = "add event on 290216 from 2pm to 3pm";
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
		Date date = new Date("290216");
		String dateString = date.formatDateShort();
		assertEquals(Command.CommandType.ADD, type);
		assertEquals(Command.DataType.EVENT, dataType);
		assertEquals("event", name);
		assertEquals(dateString, endDateString);
		assertEquals(dateString, startDateString);
		assertEquals("1400", startTime);
		assertEquals("1500", endTime);
	}
	
	@Test
	public void testInvalidDatePastShort() {
		String userInput = "add event on 1210 from 2pm to 3pm";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid date.", name);
	}
	
	@Test
	public void testInvalidDatePastLong() {
		String userInput = "add event on 121015 from 2pm to 3pm";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid date.", name);
	}
	
	@Test
	public void testInvalidDatePastLeapYear() {
		String userInput = "add event on 290200 from 2pm to 3pm";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid date.", name);
	}
	
	@Test
	public void testInvalidDateMonth() {
		String userInput = "add event on 121315 from 2pm to 3pm";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid date.", name);
	}
	
	@Test
	public void testInvalidDateDay() {
		String userInput = "add event on 321215 from 2pm to 3pm";
		Command command = parser.parse(userInput);
		Command.CommandType type = command.getCommandType();
		String name = command.getName();
		assertEquals(Command.CommandType.INVALID, type);
		assertEquals("Invalid date.", name);
	}
	
	// *******************************************************************
	// *******************************************************************
	// FOR TIME FORMATS
	// *******************************************************************
	// *******************************************************************
	
	@Test
	public void testTime1() {
		String userInput = "add event on today from 1am to 2am";
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
		assertEquals("0100", startTime);
		assertEquals("0200", endTime);
	}
	
	@Test
	public void testTime2() {
		String userInput = "add event on today from 1pm to 2pm";
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
		assertEquals("1300", startTime);
		assertEquals("1400", endTime);
	}
	
	@Test
	public void testTime3() {
		String userInput = "add event on today from 10am to 11am";
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
		assertEquals("1000", startTime);
		assertEquals("1100", endTime);
	}
	
	@Test
	public void testTime4() {
		String userInput = "add event on today from 10pm to 11pm";
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
		assertEquals("2200", startTime);
		assertEquals("2300", endTime);
	}
	
	@Test
	public void testTime5() {
		String userInput = "add event on today from 10am to 12pm";
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
		assertEquals("1000", startTime);
		assertEquals("1200", endTime);
	}
	
	@Test
	public void testTime6() {
		String userInput = "add event on today from 12am to 10am";
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
		assertEquals("0000", startTime);
		assertEquals("1000", endTime);
	}
	
	@Test
	public void testTime7() {
		String userInput = "add event on today from 1:30am to 2:30am";
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
		assertEquals("0130", startTime);
		assertEquals("0230", endTime);
	}
	
	@Test
	public void testTime8() {
		String userInput = "add event on today from 01:30am to 02:30am";
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
		assertEquals("0130", startTime);
		assertEquals("0230", endTime);
	}
	
	@Test
	public void testTime9() {
		String userInput = "add event on today from 1.30am to 2.30am";
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
		assertEquals("0130", startTime);
		assertEquals("0230", endTime);
	}
	
	@Test
	public void testTime10() {
		String userInput = "add event on today from 01.30am to 02.30am";
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
		assertEquals("0130", startTime);
		assertEquals("0230", endTime);
	}
	
	@Test
	public void testTime11() {
		String userInput = "add event on today from 130am to 0230am";
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
		assertEquals("0130", startTime);
		assertEquals("0230", endTime);
	}
	
	@Test
	public void testTime12() {
		String userInput = "add event on today from 2 to 23";
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
		assertEquals("0200", startTime);
		assertEquals("2300", endTime);
	}
	
	@Test
	public void testTime() {
		String userInput = "add event on today from 2:30 to 23.30";
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
		assertEquals("0230", startTime);
		assertEquals("2330", endTime);
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
