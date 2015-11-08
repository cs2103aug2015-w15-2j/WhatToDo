package test;

import static org.junit.Assert.*;

import java.nio.file.FileSystemException;

import org.junit.Test;

import backend.Logic;

import struct.Command;
import struct.Command.CommandType;
import struct.Command.ViewType;

//@@author A0127051U
public class LogicTest {
	
	@Test 
	/** Test getCommandType and getViewType**/
	public void testGet() throws FileSystemException{ 
		Logic logic = new Logic();
		
		//test getCommandType
		Command.CommandType commandType = logic.getCommandType("add sth"); 
		assertEquals(CommandType.ADD, commandType);
		
		//test getViewType 
		Command.ViewType viewType = logic.getViewType("all"); 
		assertEquals(ViewType.ALL, viewType);
	}
	
	@Test 
	//TODO MOVE TO ADD TEST 
	/* Test adding of float with keyword 'by' */
	public void testAddFloatWithBy() throws FileSystemException{ 
		Logic logic = new Logic();
		String feedback = logic.executeCommand("add sth \\by tomorrow");
		assertEquals("Added float \"sth by tomorrow\" to list.", feedback);
	}
	
	@Test 
	/** Test valid add commands **/
	public void testAdd() throws FileSystemException{ 
		Logic logic = new Logic();
		
		// add float 
		String feedback = logic.executeCommand("add sth");
		assertEquals("Added float \"sth\" to list.", feedback);
		
		// add task with deadline 
		feedback = logic.executeCommand("add deadline by 111116");
		assertEquals("Added task \"deadline\" to list. Due on Fri, 11 Nov 2016.", feedback);
		
		// add single-day event 
		feedback = logic.executeCommand("add single on 111116 from 1pm to 6pm"); 
		assertEquals("Added event \"single\" to list. Start: Fri, 11 Nov 2016 at 1300 End: Fri, 11 Nov 2016 at 1800.", feedback);
		
		// add event multiple day event
		feedback = logic.executeCommand("add cooking from 231016 7pm to 241016 8pm");
		assertEquals("Added event \"cooking\" to list. Start: Sun, 23 Oct 2016 at 1900 End: Mon, 24 Oct 2016 at 2000.", feedback);
	}
	
	@Test 
	/** Test invalid add commands **/
	public void testInvalidAdd() throws FileSystemException{
		Logic logic = new Logic();
		
		// Invalid deadline
		String feedback = logic.executeCommand("add hello by 3002");
		assertEquals("\"add hello by 3002\" is an invalid command. Invalid deadline.", feedback);
		
		// Start time later than end time for single-day event
		feedback = logic.executeCommand("add hello on 301216 from 5pm to 1am");
		assertEquals("\"add hello on 301216 from 5pm to 1am\" is an invalid command. "
				+ "Start time later than or equal to end time for single-day event.", feedback);
		
		// Start time equal to end time for single-day event
		feedback = logic.executeCommand("add hello on 301216 from 5pm to 5pm");
		assertEquals("\"add hello on 301216 from 5pm to 5pm\" is an invalid command. "
				+ "Start time later than or equal to end time for single-day event.", feedback);
		
		// Start date later than end date
		feedback = logic.executeCommand("add hello from tmr 1am to today 3am"); 
		assertEquals("\"add hello from tmr 1am to today 3am\" is an invalid command. "
				+ "Start date later than end date.", feedback);
		
		// Missing task name 
	} 
	
	@Test
	/** Test valid delete commands **/
	public void testDelete() throws FileSystemException{ 
		Logic logic = new Logic(); 
		logic.overwriteFile("float;hello;todo\n"
				+ "task;hellotask;todo;111116\n"
				+ "event;helloevent;todo;111116;1500;111116;2000");
		String feedback = logic.executeCommand("delete 2");
		assertEquals("Deleted task \"hellotask\" from list.", feedback);

	}
	
	@Test 
	/** Test invalid delete commands **/
	public void testInvalidDelete() throws FileSystemException{
		Logic logic = new Logic();
		
		// No such alias has been set 
		String feedback = logic.executeCommand("delete -1");
		assertEquals("\"delete -1\" is an invalid command. Alias -1 has not been set.", feedback);
		
		// Invalid index - index does not exist
		feedback = logic.executeCommand("delete 100000");
		assertEquals("Cannot find line 100000 in text file.", feedback);
		
		// Invalid index - missing index 
		feedback = logic.executeCommand("delete");
		assertEquals("\"delete\" is an invalid command. Index/alias command required.", feedback);
		
	}
	
	@Test
	/** Test valid edit commands **/
	public void testEdit() throws FileSystemException{ 
		Logic logic = new Logic(); 
		logic.overwriteFile("float;hello;todo\n"
				+ "float;hello2;done\n"
				+ "task;hellotask;todo;111116\n"
				+ "event;helloevent;todo;111116;1500;111116;2000");
		
		/** edit task **/
		// edit name of task 
		String feedback = logic.executeCommand("edit 3 name newtaskname"); 
		assertEquals("Edited task \"newtaskname\".", feedback);
		
		// edit date of task 
		feedback = logic.executeCommand("edit 3 date 101116"); 
		assertEquals("Edited task \"newtaskname\".", feedback);
		
		// edit all fields of task together 
		feedback = logic.executeCommand("edit 3 name editalltask date 101116"); 
		assertEquals("Edited task \"editalltask\".", feedback);
		
		/** edit event **/ 
		// edit name of event 
		feedback = logic.executeCommand("edit 4 name neweventname"); 
		assertEquals("Edited event \"neweventname\".", feedback);
		
		// edit all fields of event together 
		feedback = logic.executeCommand("edit 4 name editallevent startd 121116 startt 3 endd 131116 endt 4"); 
		assertEquals("Edited event \"editallevent\".", feedback);
		
		/** edit float **/ 
		// edit name of float
		feedback = logic.executeCommand("edit 1 name anewname"); 
		assertEquals("Edited float \"anewname\".", feedback);
		
		// convert float to task 
		feedback = logic.executeCommand("edit 1 name convertedtask date 101116"); 
		assertEquals("Converted float \"anewname\" to task \"convertedtask\".", feedback);
		
		// convert float to event
		feedback = logic.executeCommand("edit 1 name convertedevent startd 101116 startt 3 endd 111116 endt 4"); 
		assertEquals("Converted float \"hello2\" to event \"convertedevent\".", feedback);
	}
	
	@Test 
	/** Test invalid edit commands **/
	public void testInvalidEdit() throws FileSystemException{
		Logic logic = new Logic(); 
		logic.overwriteFile("float;hello;todo\n"
				+ "task;hellotask;done;111116\n"
				+ "event;helloevent;todo;111116;1500;111116;2000");
		
		/** invalid conversions **/
		// invalid - convert from task to event 
		String feedback = logic.executeCommand("edit 2 startd 101116 startt 3 endd 111116 endt 4"); 
		assertEquals("A task cannot be converted to a event.", feedback);
		
		// invalid - convert from event to task 
		feedback = logic.executeCommand("edit 3 date 101116"); 
		assertEquals("A event cannot be converted to a task.", feedback);
		
		// invalid - insufficient args to convert from float to event 
		feedback = logic.executeCommand("edit 1 startd 101116 endd 101116"); 
		assertEquals("Not enough arguments for conversion.", feedback);
		
		/** illogical edits **/ 
		// edit deadline to a date that has past 
		feedback = logic.executeCommand("edit 2 date 111106"); 
		assertEquals("\"edit 2 date 111106\" is an invalid command. Invalid deadline.", feedback);
							
		// edit start date to become later than end date
		feedback = logic.executeCommand("edit 3 startd 141116"); 
		assertEquals("Invalid edit. Start date later than end date.", feedback);
	
		// edit start time to become later than end time for single day events 
		feedback = logic.executeCommand("edit 3 startt 2100"); 
		assertEquals("Invalid edit. Start time later than or equal to end time for single-day event.", feedback);
		
	}
	
	@Test
	/** Test valid done commands **/
	public void testDone() throws FileSystemException{ 
		Logic logic = new Logic(); 
		logic.overwriteFile("float;hello;todo\n"
				+ "task;hellotask;todo;111116\n"
				+ "event;helloevent;todo;111116;1500;111116;2000");
		
		// valid - lower boundary case 
		String feedback = logic.executeCommand("done 1"); 
		assertEquals("Done float \"hello\".", feedback);
		
		// valid - upper boundary case 
		feedback = logic.executeCommand("done 3"); 
		assertEquals("Done event \"helloevent\".", feedback);
	}
	
	@Test
	/** Test invalid done commands **/
	public void testInvalidtDone() throws FileSystemException{ 
		Logic logic = new Logic(); 
		logic.overwriteFile("float;hello;done\n"
				+ "task;hellotask;todo;111116\n"
				+ "event;helloevent;todo;111116;1500;111116;2000");
		
		// invalid - mark done a task that is already done
		String feedback = logic.executeCommand("done 1"); 
		assertEquals("Error encountered: the float \"hello\" has already been completed.", feedback);
		
		// invalid index - less than lower boundary 
		feedback = logic.executeCommand("done -1"); 
		assertEquals("\"done -1\" is an invalid command. Invalid index.", feedback);
		
		// invalid index - greater than upper boundary 
		feedback = logic.executeCommand("done 4"); 
		assertEquals("Cannot find line 4 in text file.", feedback);
	}
	
	@Test
	/** Test valid search commands **/
	public void testSearch() throws FileSystemException{ 
		Logic logic = new Logic();
		logic.overwriteFile("float;hello;done\n"
				+ "float;goodbye;todo\n"
				+ "task;task hello;todo;111116\n"
				+ "event;helloevent;todo;111116;1500;111116;2000");
		
		// search with results 
		String feedback = logic.executeCommand("search hello");
		assertEquals("Showing results for \"hello\"\n"
				+ "TASK\nFri, 11 Nov 2016\ntodo 3. task hello\n"
				+ "FLOAT\ndone 1. hello\n"
				+ "EVENT\nFri, 11 Nov 2016\ntodo 4. helloevent;Start: 3:00 PM         End: Fri, 11 Nov 8:00 PM", feedback);
		
		// search no results 
		feedback = logic.executeCommand("search xxxxx"); 
		assertEquals("Showing results for \"xxxxx\"\n"
				+ "TASK\nThere are no items to display.\n"
				+ "FLOAT\nThere are no items to display.\n"
				+ "EVENT\nThere are no items to display.", feedback);
		
		// search empty file 
		logic.overwriteFile(""); 
		feedback = logic.executeCommand("search hello"); 
		assertEquals("Showing results for \"hello\"\n"
				+ "TASK\nThere are no items to display.\n"
				+ "FLOAT\nThere are no items to display.\n"
				+ "EVENT\nThere are no items to display.", feedback);
	}
	
	@Test
	/** Test undo and redo commands **/
	public void testUndoandRedo() throws FileSystemException{ 
		Logic logic = new Logic();
		
		// no commands to undo
		String feedback = logic.executeCommand("undo"); 
		assertEquals("There are no commands to undo.", feedback); 
		
		// undo and redo multiple commands
		logic.overwriteFile("");
		logic.executeCommand("add sth"); 
		logic.executeCommand("add task by 111216"); 
		logic.executeCommand("delete 1");
		
		feedback = logic.executeCommand("undo"); 
		assertEquals("Undid a \"delete\" command.", feedback); 
		feedback = logic.executeCommand("undo"); 
		assertEquals("Undid a \"add\" command.", feedback); 
		feedback = logic.executeCommand("redo"); 
		assertEquals("Redid a \"add\" command.", feedback); 
		feedback = logic.executeCommand("redo"); 
		assertEquals("Redid a \"delete\" command.", feedback); 
		
		// check if redo stack is cleared correctly 
		logic.executeCommand("add 1"); 
		logic.executeCommand("add 2"); 
		logic.executeCommand("delete 1");
		logic.executeCommand("undo"); 
		logic.executeCommand("undo"); 
		logic.executeCommand("add 3"); 
		feedback = logic.executeCommand("redo"); 
		assertEquals("There are no commands to redo.", feedback); 
	}
	
	@Test
	/** Test set commands **/
	public void testSet() throws FileSystemException{ 
		Logic logic = new Logic();
		logic.clearAliasFile(); 
		
		// valid set command
		String feedback = logic.executeCommand("set + as add"); 
		assertEquals("Set new alias \"+\" for \"add\".", feedback); 
	}
	
	@Test
	/** Test invalid set commands **/
	public void testInvalidSet() throws FileSystemException{ 
		Logic logic = new Logic();
		logic.clearAliasFile();
		
		// invalid alias - set alias that is already a command keyword
		String feedback = logic.executeCommand("set add as delete"); 
		assertEquals("\"set add as delete\" is an invalid command. "
				+ "Input alias is a either a registered command and cannot be used or an alias-in-use.", feedback); 
		
		// invalid alias - set alias that already exists 
		logic.executeCommand("set create as add"); 
		feedback = logic.executeCommand("set create as delete"); 
		assertEquals("\"set create as delete\" is an invalid command. "
				+ "Input alias is a either a registered command and cannot be used or an alias-in-use.", feedback); 
	}
	
	@Test
	/** Test delete alias commands **/
	public void testDeleteAlias() throws FileSystemException{ 
		Logic logic = new Logic();
		logic.clearAliasFile();
		
		// valid delete alias command 
		logic.executeCommand("set del as delete");
		String feedback = logic.executeCommand("delete del"); 
		assertEquals("Deleted alias \"del\".", feedback); 
	}
	
	@Test
	/** Test invalid delete alias commands **/
	public void testInvalidDeleteAlias() throws FileSystemException{ 
		Logic logic = new Logic();
		logic.clearAliasFile();
		
		// invalid delete alias - delete alias that was not set 
		String feedback = logic.executeCommand("delete insert"); 
		assertEquals("\"delete insert\" is an invalid command. Alias insert has not been set.", feedback); 
	}
	
	@Test 
	/** Test save command **/
	public void testSave() throws FileSystemException{ 
		Logic logic = new Logic();
		
		//get original filepath
		String originalUrl = logic.getFilepath(); 
		int fileNameIndex = originalUrl.lastIndexOf("\\whattodo.txt");
		String originalBaseUrl = originalUrl.substring(0, fileNameIndex); 
		
		// valid - save to same location
		String feedback = logic.executeCommand("save " + originalBaseUrl); 
		assertEquals("Your file location remains unchanged.", feedback); 
		
		// valid - save to new location 
		feedback = logic.executeCommand("save c:\\test"); 
		assertEquals("You are now writing to \"c:\\test\\whattodo.txt\"", feedback); 
		
		//save to original location after testing 
		logic.executeCommand("save " + originalBaseUrl); 
	}
		
	@Test
	public void testInvalidUserCommand() throws FileSystemException{
		Logic logic = new Logic();
		
		String feedback = logic.executeCommand("zzz");
		assertEquals("\"zzz\" is an invalid command. Invalid user command.", feedback);
	}
	
	@Test
	public void testCorruptedFile() throws FileSystemException{
		Logic logic = new Logic();
		
		logic.overwriteFile(";;");
		String feedback = logic.executeCommand("add zzz");
		assertEquals("Unknown error encountered. whattodo.txt may be corrupted.", feedback);
		
		//ensure the file is not corrupted at the end of tests
		logic.overwriteFile("");
	}
	
}
