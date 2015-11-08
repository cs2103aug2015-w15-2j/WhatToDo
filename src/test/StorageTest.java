/**
 * This class tests the methods in Storage.java
 * 
 * @@author A0124238L
 */

package test;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystemException;

import backend.Storage;
import struct.Date;
import struct.FloatingTask;
import struct.Event;
import struct.Task;

public class StorageTest {

	@Test
	// Tests if tasks are sorted properly.
	public void testAddTask() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");

		storage.addTask(new Task("banana", false, new Date("251215")));
		storage.addTask(new Task("apple", false, new Date("251115")));
		storage.addTask(new Task("guava", false, new Date("101215")));
		storage.addTask(new Task("orange", false, new Date("261115")));
		storage.addTask(new Task("pear", false, new Date("241215")));
		storage.addTask(new Task("avocado", false, new Date("010115")));
		storage.addTask(new Task("watermelon", false, new Date("010116")));
		storage.addTask(new Task("animal", false, new Date("010115")));
		storage.addTask(new Task("wintermelon", false, new Date("010116")));
		storage.addTask(new Task("kiwi", false, new Date("251215")));

		assertEquals("task;animal;todo;010115\ntask;avocado;todo;010115\ntask;apple;todo;251115\n"
						+ "task;orange;todo;261115\ntask;guava;todo;101215\ntask;pear;todo;241215\n"
						+ "task;banana;todo;251215\ntask;kiwi;todo;251215\ntask;watermelon;todo;010116\n"
						+ "task;wintermelon;todo;010116\n", storage.display());

		// Clear File for next test.
		storage.overwriteFile("");
	}

	@Test
	// Tests if Floating Task are sorted properly.
	public void testAddFloatingTask() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");

		storage.addFloatingTask(new FloatingTask("dancing at the bar", false));
		storage.addFloatingTask(new FloatingTask("train a dragon", false));
		storage.addFloatingTask(new FloatingTask("fly a kite", false));
		storage.addFloatingTask(new FloatingTask("eat something nice", false));
		storage.addFloatingTask(new FloatingTask("kill a monster", false));
		storage.addFloatingTask(new FloatingTask("uniqlo shopping", false));
		storage.addFloatingTask(new FloatingTask("catch a dreamcatcher", false));

		assertEquals(
				"float;catch a dreamcatcher;todo\nfloat;dancing at the bar;todo\n"
						+ "float;eat something nice;todo\nfloat;fly a kite;todo\nfloat;kill a monster;todo\n"
						+ "float;train a dragon;todo\nfloat;uniqlo shopping;todo\n",
				storage.display());

		storage.addFloatingTask(new FloatingTask("catch a catcher", false));
		storage.addFloatingTask(new FloatingTask("catch a dreamer", false));

		assertEquals(
				"float;catch a catcher;todo\nfloat;catch a dreamcatcher;todo\n"
						+ "float;catch a dreamer;todo\nfloat;dancing at the bar;todo\n"
						+ "float;eat something nice;todo\nfloat;fly a kite;todo\nfloat;kill a monster;todo\n"
						+ "float;train a dragon;todo\nfloat;uniqlo shopping;todo\n",
				storage.display());

		// Clear File for next test.
		storage.overwriteFile("");
	}

	@Test
	// Tests if events are sorted properly.
	public void testAddEvent() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");

		// Test different start time.
		storage.addEvent(new Event("prom night", false, new Date("101015"),
				new Date("101015"), "1700", "2359"));
		storage.addEvent(new Event("class chalet", false, new Date("101015"),
				new Date("101015"), "0800", "2359"));

		assertEquals("event;class chalet;todo;101015;0800;101015;2359\n"
				+ "event;prom night;todo;101015;1700;101015;2359\n",
				storage.display());

		// Test different start date.
		storage.addEvent(new Event("preparation event", false, new Date(
				"091015"), new Date("101015"), "0800", "2359"));

		assertEquals("event;preparation event;todo;091015;0800;101015;2359\n"
				+ "event;class chalet;todo;101015;0800;101015;2359\n"
				+ "event;prom night;todo;101015;1700;101015;2359\n",
				storage.display());

		// Test different end time.
		storage.addEvent(new Event("just weird", false, new Date("091015"),
				new Date("101015"), "0800", "2358"));

		assertEquals("event;just weird;todo;091015;0800;101015;2358\n"
				+ "event;preparation event;todo;091015;0800;101015;2359\n"
				+ "event;class chalet;todo;101015;0800;101015;2359\n"
				+ "event;prom night;todo;101015;1700;101015;2359\n",
				storage.display());

		// Test different end date.
		storage.addEvent(new Event("stoning", false, new Date("091015"),
				new Date("121015"), "0800", "2359"));

		assertEquals("event;just weird;todo;091015;0800;101015;2358\n"
				+ "event;preparation event;todo;091015;0800;101015;2359\n"
				+ "event;stoning;todo;091015;0800;121015;2359\n"
				+ "event;class chalet;todo;101015;0800;101015;2359\n"
				+ "event;prom night;todo;101015;1700;101015;2359\n",
				storage.display());

		// Clear File for next test.
		storage.overwriteFile("");
	}

	@Test
	public void testDeleteLine() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");

		storage.addFloatingTask(new FloatingTask("float task", false));
		storage.addTask(new Task("i am a task", false, new Date("121212")));
		storage.addEvent(new Event("event time", false, new Date("101015"),
				new Date("101015"), "1700", "2359"));

		// This is a boundary case for deleting first line.
		assertEquals("float;float task;todo", storage.deleteLine(1));
		assertEquals(
				"task;i am a task;todo;121212\nevent;event time;todo;101015;1700;101015;2359\n",
				storage.display());

		// This is a boundary case for last line.
		assertEquals("event;event time;todo;101015;1700;101015;2359",
				storage.deleteLine(2));
		assertEquals("task;i am a task;todo;121212\n", storage.display());

		// This is a boundary case for delete only item left.
		assertEquals("task;i am a task;todo;121212", storage.deleteLine(1));
		
		// This is a boundary case for display nothing.
		assertEquals("", storage.display());

		// Clear File for next test.
		storage.overwriteFile("");
	}

	@Test(expected = FileSystemException.class)
	// Tests if deleting line 0 will throw exception
	public void testDeleteZero() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");

		storage.addFloatingTask(new FloatingTask("dummy", false));

		// This is a boundary case for delete line 0 (invalid)
		storage.deleteLine(0);
	}

	@Test(expected = FileSystemException.class)
	// Tests if deleting negative line numbers will throw exception
	public void testDeleteNegative() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");

		storage.addFloatingTask(new FloatingTask("dummy2", false));

		// This is a boundary case for deleting negative numbers
		storage.deleteLine(-1);
	}

	@Test(expected = FileSystemException.class)
	// Tests if deleting line numbers beyond number of items will throw exception
	public void testDeleteTooLarge() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");

		storage.addFloatingTask(new FloatingTask("dummy", false));

		// This is a boundary case for deleting line numbers beyond number of items.
		storage.deleteLine(2);
	}

	@Test
	public void testFindTypeInLine() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");

		// Setup for testing
		storage.addTask(new Task("arrange meeting", false, new Date("020116")));
		storage.addEvent(new Event("Company D&D", false, new Date("211215"),
				new Date("211215"), "1800", "2300"));
		storage.addFloatingTask(new FloatingTask("buy a painting", false));

		// This is a boundary case for first line.
		assertEquals("float", storage.findTypeInLine(1));

		// This is a boundary case for last line.
		assertEquals("event", storage.findTypeInLine(3));

		// This is a general case.
		assertEquals("task", storage.findTypeInLine(2));

		// Checks file remains unchanged.
		assertEquals(
				"float;buy a painting;todo\n"
						+ "task;arrange meeting;todo;020116\nevent;Company D&D;todo;211215;1800;211215;2300\n",
				storage.display());

		// Clear File for next test.
		storage.overwriteFile("");
	}

	@Test(expected = FileSystemException.class)
	// Tests if finding line numbers beyond number of items will throw exception
	public void testFindTypeInLineTooLarge() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");

		storage.addFloatingTask(new FloatingTask("dummy", false));

		// This is a boundary case for finding line numbers beyond number of items
		storage.findTypeInLine(2);
	}

	@Test(expected = FileSystemException.class)
	// Tests if finding line number 0 will throw exception
	public void testFindTypeInLineZero() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");

		storage.addFloatingTask(new FloatingTask("dummy", false));

		// This is a boundary case for finding line 0
		storage.findTypeInLine(0);
	}

	@Test(expected = FileSystemException.class)
	// Tests if finding negative line numbers will throw exception
	public void testFindTypeInLineNegative() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");

		storage.addFloatingTask(new FloatingTask("dummy", false));

		// This is a boundary case for finding negative numbers
		storage.findTypeInLine(-1);
	}

	@Test
	public void testMarkAsDone() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");

		// Setup for testing
		storage.addTask(new Task("arrange meeting", false, new Date("020116")));
		storage.addEvent(new Event("Company D&D", false, new Date("211215"),
				new Date("211215"), "1800", "2300"));
		storage.addFloatingTask(new FloatingTask("buy a painting", false));

		// Mark each type of item as done.
		storage.markAsDone(1);
		assertEquals(
				"float;buy a painting;done\ntask;arrange meeting;todo;020116\n"
						+ "event;Company D&D;todo;211215;1800;211215;2300\n",
				storage.display());
		storage.markAsDone(3);
		assertEquals(
				"float;buy a painting;done\ntask;arrange meeting;todo;020116\n"
						+ "event;Company D&D;done;211215;1800;211215;2300\n",
				storage.display());
		storage.markAsDone(2);
		assertEquals(
				"float;buy a painting;done\ntask;arrange meeting;done;020116\n"
						+ "event;Company D&D;done;211215;1800;211215;2300\n",
				storage.display());

		// Clear File for next test.
		storage.overwriteFile("");
	}

	@Test
	// Tests if the correct exceptions with corresponding messages are thrown,
	// when marking already completed items as done.
	public void testMarkAsDoneExceptions() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");

		// Setup for testing
		storage.addTask(new Task("arrange meeting", true, new Date("020116")));
		storage.addEvent(new Event("Company D&D", true, new Date("211215"),
				new Date("211215"), "1800", "2300"));
		storage.addFloatingTask(new FloatingTask("buy a painting", true));

		// Mark each type of item as done.
		try {
			storage.markAsDone(1);
		} catch (FileSystemException exception) {
			assertEquals(
					"Error encountered: the float \"buy a painting\" has already been completed.",
					exception.getMessage());
		}
		try {
			storage.markAsDone(2);
		} catch (FileSystemException exception) {
			assertEquals(
					"Error encountered: the task \"arrange meeting\" has already been completed.",
					exception.getMessage());
		}
		try {
			storage.markAsDone(3);
		} catch (FileSystemException exception) {
			assertEquals(
					"Error encountered: the event \"Company D&D\" has already been completed.",
					exception.getMessage());
		}

		// Clear File for next test.
		storage.overwriteFile("");
	}
	
	@Test
	public void testGetAttribute() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");

		// Setup for testing.
		storage.addTask(new Task("i am a task", false, new Date("121212")));
		storage.addEvent(new Event("event time", false, new Date("101015"),
				new Date("111015"), "1700", "2359"));
		storage.addFloatingTask(new FloatingTask("float task", false));

		// General Case for floating task
		assertEquals("float", storage.getAttribute(1, 0));
		assertEquals("float task", storage.getAttribute(1, 1));
		assertEquals("todo", storage.getAttribute(1, 2));
		
		// These are boundary cases where type = 3 and -1 for float task.
		assertEquals(null, storage.getAttribute(1, 3));
		assertEquals(null, storage.getAttribute(1, -1));
		
		// General Case for task
		assertEquals("task", storage.getAttribute(2, 0));
		assertEquals("i am a task", storage.getAttribute(2, 1));
		assertEquals("todo", storage.getAttribute(2, 2));
		assertEquals("121212", storage.getAttribute(2, 3));
		
		// These are boundary cases where type = 4 and -1 for task.
		assertEquals(null, storage.getAttribute(2, 4));
		assertEquals(null, storage.getAttribute(2, -1));
		
		// General Case for event
		assertEquals("event", storage.getAttribute(3, 0));
		assertEquals("event time", storage.getAttribute(3, 1));
		assertEquals("todo", storage.getAttribute(3, 2));
		assertEquals("101015", storage.getAttribute(3, 3));
		assertEquals("1700", storage.getAttribute(3, 4));
		assertEquals("111015", storage.getAttribute(3, 5));
		assertEquals("2359", storage.getAttribute(3, 6));
		
		// These are boundary cases where type = 7 and -1 for event.
		assertEquals(null, storage.getAttribute(3, 7));
		assertEquals(null, storage.getAttribute(3, -1));

		// Clear File for next test.
		storage.overwriteFile("");
	}
	
	@Test
	// Test the methods for Alias File
	public void testAlias() throws FileSystemException {
		Storage storage = new Storage();
		storage.clearAliasFile();
		
		// Test if add and delete works.
		storage.addToAliasFile("insert", "add");  
		storage.addToAliasFile("remove", "delete"); 
		storage.addToAliasFile("plus", "add");
		storage.deleteFromAliasFile("insert"); 
		assertEquals("remove;delete\nplus;add\n",storage.readAliasFile());
		
		// Test if deleting non-existent alias affects file.
		storage.deleteFromAliasFile("flying"); 
		assertEquals("remove;delete\nplus;add\n",storage.readAliasFile());
		
		// Test if boundary case of empty file is displaying correctly.
		storage.deleteFromAliasFile("plus");
		storage.deleteFromAliasFile("remove");
		assertEquals("",storage.readAliasFile());
		
		// Test add and delete 1 item.
		storage.addToAliasFile("wakaka", "done");
		assertEquals("wakaka;done\n",storage.readAliasFile());
		storage.deleteFromAliasFile("wakaka");
		assertEquals("",storage.readAliasFile());
	}
	
	@Test 
	public void testClearAliasFile() throws FileSystemException {
		Storage storage = new Storage();
		storage.clearAliasFile();
		
		storage.addToAliasFile("plus", "add");  
		storage.addToAliasFile("rm", "delete"); 
		storage.addToAliasFile("jia", "add");
		storage.deleteFromAliasFile("plus"); 
		assertEquals("rm;delete\njia;add\n",storage.readAliasFile());
		
		// General Case
		storage.clearAliasFile(); 
		assertEquals("",storage.readAliasFile());
		
		// Clearing empty file.
		storage.clearAliasFile(); 
		assertEquals("",storage.readAliasFile());
	}
	
	@Test
	public void testOverwriteAliasFile() throws FileSystemException {
		Storage storage = new Storage();
		storage.clearAliasFile();
		
		storage.addToAliasFile("plus", "add");  
		
		// This is a boundary case for overwriting with empty string.
		storage.overwriteAliasFile("");
		assertEquals("",storage.readAliasFile());
		
		// General Case
		storage.addToAliasFile("find", "search");
		
		storage.overwriteAliasFile("assign;set\nwhy;help\n");
		assertEquals("assign;set\nwhy;help\n",storage.readAliasFile());
		
		storage.overwriteAliasFile("");
		assertEquals("",storage.readAliasFile());
	}
	
	@Test
	// Tests that changing file path to folder without text file.
	public void testChangeFilePath() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");
		
		storage.addTask(new Task("sample task", false, new Date("121212")));
		storage.changeFileStorageLocation("src" + File.separator + "test" + File.separator);
		
		assertEquals("task;sample task;todo;121212\n", storage.display());
		
		storage.changeFileStorageLocation("");
		assertEquals("task;sample task;todo;121212\n", storage.display());	
		
		// Clear File for next test.
		storage.overwriteFile("");
	}
	
	@Test
	// Tests that changing file path to folder with empty text file.
	public void testChangeFilePathToEmpty() throws IOException {
		Storage storage = new Storage();
		storage.overwriteFile("");
		
		storage.addTask(new Task("sample task", false, new Date("121212")));
		
		// Simulate empty text file
		File file = new File("src" + File.separator+ "test" + File.separator + "whattodo.txt");
		assertEquals(true, file.createNewFile());
		
		storage.changeFileStorageLocation("src" + File.separator + "test");
		assertEquals("task;sample task;todo;121212\n", storage.display());
		
		// Revert back
		storage.changeFileStorageLocation("");
		assertEquals("task;sample task;todo;121212\n", storage.display());	
		
		// Clear File for next test.
		storage.overwriteFile("");		
	}
	
	@Test
	// Tests that changing file path to folder with conflicting text file.
	public void testChangeFilePathConflicting() throws IOException {
		Storage storage = new Storage();
		storage.overwriteFile("");
		
		storage.addTask(new Task("sample task", false, new Date("121212")));
		
		// Simulate conflict file.
		File file = new File("src" + File.separator + "test" + File.separator + "whattodo.txt");
		assertEquals(true, file.createNewFile());
		PrintWriter writer = new PrintWriter("src" + File.separator + "test" + File.separator + "whattodo.txt");
		writer.print("float;koala bear;todo");
		writer.close();
		
		assertEquals("Conflicting text files found in"
				+ " both old and new file paths. Please delete either one.",
				storage.changeFileStorageLocation("src" + File.separator + "test"));
		
		assertEquals("task;sample task;todo;121212\n", storage.display());
		
		// Clear for next tests.
		storage.overwriteFile("");	
		file.delete();
	}
}
