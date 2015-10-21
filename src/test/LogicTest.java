package test;

import static org.junit.Assert.*;

import java.nio.file.FileSystemException;

import org.junit.Test;

import backend.Logic;

public class LogicTest {
		
	@Test
	public void testInvalid() throws FileSystemException{
		Logic logic = new Logic();
		String feedback = logic.executeCommand("zzz");
		assertEquals("\"zzz\" is an invalid command. ", feedback);
	}
	
	@Test 
	public void testAddFloat() throws FileSystemException{ 
		Logic logic = new Logic();
		String feedback = logic.executeCommand("add sth");
		assertEquals("Added float \"sth\" to list.", feedback);
	}
	
	@Test 
	/* Test adding of float with keyword 'by' */
	public void testAddFloatWithBy() throws FileSystemException{ 
		Logic logic = new Logic();
		String feedback = logic.executeCommand("add sth \\by tomorrow");
		assertEquals("Added float \"sth by tomorrow\" to list.", feedback);
	}
	
	@Test
	public void testAddTask() throws FileSystemException{ 
		Logic logic = new Logic();
		String feedback = logic.executeCommand("add deadline by 221015");
		assertEquals("Added task \"deadline\" to list. Due on Thursday, 22 Oct 2015.", feedback);
	}
	
	@Test
	public void testAddEvent() throws FileSystemException{ 
		Logic logic = new Logic();
		String feedback = logic.executeCommand("add cooking from 231015 7pm to 241015 8pm");
		assertEquals("Added event \"cooking\" to list. Start: Friday, 23 Oct 2015 at 1900 End: Saturday, 24 Oct 2015 at 2000.", feedback);
	}
	
	@Test
	/* Invalid */
	public void testDeleteInvalid() throws FileSystemException{
		Logic logic = new Logic();
		String feedback = logic.executeCommand("delete -1");
		assertEquals("\"delete -1\" is an invalid command. ", feedback);
	}
	
	@Test
	/* Lower Boundary */ 
	public void testDeleteOne() throws FileSystemException{ 
		Logic logic = new Logic(); 
		logic.executeCommand("add !"); 
		String feedback = logic.executeCommand("delete 1");
		assertEquals("Deleted float \"!\" from list.", feedback);
	}
	
}
