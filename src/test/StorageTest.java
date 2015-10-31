package test;

import static org.junit.Assert.*;

import org.junit.Test;

import java.nio.file.FileSystemException;

import backend.Storage;
import struct.Date;
import struct.FloatingTask;
import struct.Event;
import struct.Task;

public class StorageTest {

	@Test
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
		
		// Test if tasks are sorted properly.
		assertEquals("task;animal;todo;010115\ntask;avocado;todo;010115\ntask;apple;todo;251115\n"
				+ "task;orange;todo;261115\ntask;guava;todo;101215\ntask;pear;todo;241215\n"
				+ "task;banana;todo;251215\ntask;kiwi;todo;251215\ntask;watermelon;todo;010116\n"
				+ "task;wintermelon;todo;010116\n",storage.display());
		
		// Clear File for next test.
		storage.overwriteFile("");
	}
	
	@Test
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
		
		assertEquals("float;catch a dreamcatcher;todo\nfloat;dancing at the bar;todo\n"
				+ "float;eat something nice;todo\nfloat;fly a kite;todo\nfloat;kill a monster;todo\n"
				+ "float;train a dragon;todo\nfloat;uniqlo shopping;todo\n", storage.display());
		
		storage.addFloatingTask(new FloatingTask("catch a catcher", false));
		storage.addFloatingTask(new FloatingTask("catch a dreamer", false));
		
		assertEquals("float;catch a catcher;todo\nfloat;catch a dreamcatcher;todo\n"
				+ "float;catch a dreamer;todo\nfloat;dancing at the bar;todo\n"
				+ "float;eat something nice;todo\nfloat;fly a kite;todo\nfloat;kill a monster;todo\n"
				+ "float;train a dragon;todo\nfloat;uniqlo shopping;todo\n", storage.display());
		
		// Clear File for next test.
		storage.overwriteFile("");
	}

	@Test
	public void testAddEvent() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");
		
		// Test different start time.
		storage.addEvent(new Event ("prom night", false, new Date("101015"), new Date("101015"), "1700", "2359"));
		storage.addEvent(new Event ("class chalet", false, new Date("101015"), new Date("101015"), "0800", "2359"));
		
		assertEquals("event;class chalet;todo;101015;0800;101015;2359\n"
				+ "event;prom night;todo;101015;1700;101015;2359\n", storage.display());
		
		// Test different start date.
		storage.addEvent(new Event ("preparation event", false, new Date("091015"), new Date("101015"), "0800", "2359"));
		
		assertEquals("event;preparation event;todo;091015;0800;101015;2359\n"
				+ "event;class chalet;todo;101015;0800;101015;2359\n"
				+ "event;prom night;todo;101015;1700;101015;2359\n", storage.display());
		
		// Test different end time.
		storage.addEvent(new Event ("just weird", false, new Date("091015"), new Date("101015"), "0800", "2358"));
		
		assertEquals("event;just weird;todo;091015;0800;101015;2358\n"
				+ "event;preparation event;todo;091015;0800;101015;2359\n"
				+ "event;class chalet;todo;101015;0800;101015;2359\n"
				+ "event;prom night;todo;101015;1700;101015;2359\n", storage.display());
		
		// Test different end date.
		storage.addEvent(new Event ("stoning", false, new Date("091015"), new Date("121015"), "0800", "2359"));
		
		assertEquals("event;just weird;todo;091015;0800;101015;2358\n"
				+ "event;preparation event;todo;091015;0800;101015;2359\n"
				+ "event;stoning;todo;091015;0800;121015;2359\n"
				+ "event;class chalet;todo;101015;0800;101015;2359\n"
				+ "event;prom night;todo;101015;1700;101015;2359\n", storage.display());
		
		// Clear File for next test.
		storage.overwriteFile("");
	}

	@Test
	public void testDeleteLine() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");
		
		storage.addFloatingTask(new FloatingTask("float task", false));
		storage.addTask(new Task("i am a task", false, new Date("121212")));
		storage.addEvent(new Event ("event time", false, new Date("101015"), new Date("101015"), "1700", "2359"));
		
		// This is a boundary case for line 1.
		assertEquals("float;float task;todo", storage.deleteLine(1));
		assertEquals("task;i am a task;todo;121212\nevent;event time;todo;101015;1700;101015;2359\n", storage.display());
		
		// This is a boundary case for last line.
		assertEquals("event;event time;todo;101015;1700;101015;2359", storage.deleteLine(2));
		assertEquals("task;i am a task;todo;121212\n", storage.display());
		
		// This is a boundary case for delete only item left.
		assertEquals("task;i am a task;todo;121212", storage.deleteLine(1));
		// This is a boundary case for display nothing.
		assertEquals("", storage.display());
			
		// Clear File for next test.
		storage.overwriteFile("");
	}
	
	@Test(expected=FileSystemException.class)
	public void testDeleteZero() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");
		
		// For test only.
		storage.addFloatingTask(new FloatingTask("dummy", false));
		
		// This is a boundary case for '0' partition.
		storage.deleteLine(0);
	}

	@Test(expected=FileSystemException.class)
	public void testDeleteNegative() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");
		
		// For test only.
		storage.addFloatingTask(new FloatingTask("dummy2", false));
		
		// This is a boundary case for negative partition.
		storage.deleteLine(-1);
	}
	
	@Test(expected=FileSystemException.class)
	public void testDeleteTooLarge() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");
		
		// For test only.
		storage.addFloatingTask(new FloatingTask("dummy", false));
		
		// This is a boundary case for line overflow partition.
		storage.deleteLine(2);
	}
	
	@Test
	public void testEditName() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");

		storage.addTask(new Task("i am a task", false, new Date("121212")));
		storage.addEvent(new Event ("event time", false, new Date("101015"), new Date("101015"), "1700", "2359"));
		storage.addFloatingTask(new FloatingTask("float task", false));
		
		// This is a boundary case for line 1.
		assertEquals("float;float task;todo", storage.editName(1, "still floating"));
		assertEquals("float;still floating;todo\n"
				+ "task;i am a task;todo;121212\nevent;event time;todo;101015;1700;101015;2359\n", storage.display());
		
		// This is a boundary case for last line.
		assertEquals("event;event time;todo;101015;1700;101015;2359", storage.editName(3, "best event in town"));
		assertEquals("float;still floating;todo\n"
				+ "task;i am a task;todo;121212\nevent;best event in town;todo;101015;1700;101015;2359\n", storage.display());
		
		// This is a general case.
		assertEquals("task;i am a task;todo;121212", storage.editName(2, "tasking"));
		assertEquals("float;still floating;todo\n"
				+ "task;tasking;todo;121212\nevent;best event in town;todo;101015;1700;101015;2359\n", storage.display());
			
		// Clear File for next test.
		storage.overwriteFile("");
	}

	@Test(expected=FileSystemException.class)
	public void testEditNameZero() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");

		storage.addFloatingTask(new FloatingTask("dummy", false));
		
		// This is a boundary case for '0' partition.
		storage.editName(0, "you can't see me");
	}

	@Test(expected=FileSystemException.class)
	public void testEditNameNegative() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");
		
		storage.addFloatingTask(new FloatingTask("dummy2", false));
		
		// This is a boundary case for negative partition.
		storage.editName(-1, "you won't see me");
	}
	
	@Test(expected=FileSystemException.class)
	public void testEditNameTooLarge() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");
		
		// For test only.
		storage.addFloatingTask(new FloatingTask("dummy", false));
		
		// This is a boundary case for line overflow partition.
		storage.editName(2, "overflow");
	}
	
	@Test
	public void testFindTypeInLine() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");
		
		// Setup for testing
		storage.addTask(new Task("arrange meeting", false, new Date("020116")));
		storage.addEvent(new Event ("Company D&D", false, new Date("211215"), new Date("211215"), "1800", "2300"));
		storage.addFloatingTask(new FloatingTask("buy a painting", false));
		
		// This is a boundary case for line 1.
		assertEquals("float", storage.findTypeInLine(1));
		
		// This is a boundary case for last line.
		assertEquals("event", storage.findTypeInLine(3));
		
		// This is a general case.
		assertEquals("task", storage.findTypeInLine(2));
		
		// Checks file remains unchanged.
		assertEquals("float;buy a painting;todo\n"
				+ "task;arrange meeting;todo;020116\nevent;Company D&D;todo;211215;1800;211215;2300\n", storage.display());
			
		// Clear File for next test.
		storage.overwriteFile("");
	}
	
	@Test
	public void testConvertFloatToTask() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");
		
		// Setup for testing
		storage.addTask(new Task("arrange meeting", false, new Date("020116")));
		storage.addEvent(new Event ("Company D&D", false, new Date("211215"), new Date("211215"), "1800", "2300"));
		storage.addFloatingTask(new FloatingTask("buy a painting", false));
				
		storage.convertFloatToTask(1, new Date("030116"));

		assertEquals("task;arrange meeting;todo;020116\ntask;buy a painting;todo;030116\n"
				+ "event;Company D&D;todo;211215;1800;211215;2300\n", storage.display());
			
		// Clear File for next test.
		storage.overwriteFile("");
	}
	
	@Test
	public void testConvertFloatToEvent() throws FileSystemException {
		Storage storage = new Storage();
		storage.overwriteFile("");
		
		// Setup for testing
		storage.addTask(new Task("arrange meeting", false, new Date("020116")));
		storage.addEvent(new Event ("Company D&D", false, new Date("211215"), new Date("211215"), "1800", "2300"));
		storage.addFloatingTask(new FloatingTask("buy a painting", false));
				
		storage.convertFloatToEvent(1, new Date("030116"), "1300", new Date("030116"), "1500");

		assertEquals("task;arrange meeting;todo;020116\nevent;Company D&D;todo;211215;1800;211215;2300\n"
				+ "event;buy a painting;todo;030116;1300;030116;1500\n", storage.display());
			
		// Clear File for next test.
		storage.overwriteFile("");
	}
	
	/*
	@Test
	public void testEditTaskDeadline() {
		fail("Not yet implemented");
	}

	@Test
	public void testEditEventStartDate() {
		fail("Not yet implemented");
	}

	@Test
	public void testEditEventEndDate() {
		fail("Not yet implemented");
	}

	@Test
	public void testEditEventStartTime() {
		fail("Not yet implemented");
	}

	@Test
	public void testEditEventEndTime() {
		fail("Not yet implemented");
	}

	@Test
	public void testMarkAsDone() {
		fail("Not yet implemented");
	}
*/
}
