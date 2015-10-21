package test;

import static org.junit.Assert.*;

import org.junit.Test;

import gui.CommandHistory;

public class CommandHistoryTest {

	// Create an instance of command history
	CommandHistory ch = new CommandHistory();
	
	@Test
	// Average case test for previous, 10 commands
	public void testCase1() {
		
		// Correct result
		String expected = "10 9 8 7 6 5 4 3 2 1 ";
		
		// Test result
		ch.add("1");
		ch.resetIndex();
		ch.add("2");
		ch.resetIndex();
		ch.add("3");
		ch.resetIndex();
		ch.add("4");
		ch.resetIndex();
		ch.add("5");
		ch.resetIndex();
		ch.add("6");
		ch.resetIndex();
		ch.add("7");
		ch.resetIndex();
		ch.add("8");
		ch.resetIndex();
		ch.add("9");
		ch.resetIndex();
		ch.add("10");
		ch.resetIndex();
		
		String actual = "";
		
		for (int i = 0; i < 10; i++) {
			String prev = ch.getPreviousTest();
			ch.updateField(prev);
			actual += ch.getText() + " ";
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	// Average test case for next, 10 commands
	public void testCase2() {
		
		// Correct result
		String expected = "2 3 4 5 6 7 8 9 10 10 ";

		// Test result
		ch.add("1");
		ch.resetIndex();
		ch.add("2");
		ch.resetIndex();
		ch.add("3");
		ch.resetIndex();
		ch.add("4");
		ch.resetIndex();
		ch.add("5");
		ch.resetIndex();
		ch.add("6");
		ch.resetIndex();
		ch.add("7");
		ch.resetIndex();
		ch.add("8");
		ch.resetIndex();
		ch.add("9");
		ch.resetIndex();
		ch.add("10");
		ch.resetIndex();
		
		String actual = "";
		
		for (int i = 0; i < 10; i++) {
			String prev = ch.getPreviousTest();
			ch.updateField(prev);
		}
		
		for (int i = 0; i < 10; i++) {
			String next = ch.getNextTest();
			ch.updateField(next);
			actual += ch.getText() + " ";
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	// Average test case for alternate previous and next, 10 commands
	public void testCase3() {
		
		// Correct result
		String expected = "9 10 9 10 9 10 9 10 9 10 9 10 9 10 9 10 9 10 9 10 ";

		// Test result
		ch.add("1");
		ch.resetIndex();
		ch.add("2");
		ch.resetIndex();
		ch.add("3");
		ch.resetIndex();
		ch.add("4");
		ch.resetIndex();
		ch.add("5");
		ch.resetIndex();
		ch.add("6");
		ch.resetIndex();
		ch.add("7");
		ch.resetIndex();
		ch.add("8");
		ch.resetIndex();
		ch.add("9");
		ch.resetIndex();
		ch.add("10");
		ch.resetIndex();

		String actual = "";
		
		String first = ch.getPreviousTest();
		ch.updateField(first);
		
		for (int i = 0; i < 10; i++) {
			String prev = ch.getPreviousTest();
			ch.updateField(prev);
			actual += ch.getText() + " ";
			String next = ch.getNextTest();
			ch.updateField(next);
			actual += ch.getText() + " ";
		}

		assertEquals(expected, actual);
	}
	
	@Test
	/* Small test case for previous and next, 2 commands
	 * Testing for ArrayIndexOutOfBoundsException since getPrevious() and
	 * getNext() have consecutive double increments(++) and decrements(--)
	 */
	public void testCase4() {
		
		// Correct result
		String expected = "1 2 ";

		// Test result
		ch.add("1");
		ch.resetIndex();
		ch.add("2");
		ch.resetIndex();

		String actual = "";
		String prev, next;

		prev = ch.getPreviousTest();
		ch.updateField(prev);
		prev = ch.getPreviousTest();
		ch.updateField(prev);
		prev = ch.getPreviousTest();
		ch.updateField(prev);
		prev = ch.getPreviousTest();
		ch.updateField(prev);
		prev = ch.getPreviousTest();
		ch.updateField(prev);
		
		actual += ch.getText() + " ";
		
		next = ch.getNextTest();
		ch.updateField(next);
		next = ch.getNextTest();
		ch.updateField(next);
		next = ch.getNextTest();
		ch.updateField(next);
		next = ch.getNextTest();
		ch.updateField(next);
		next = ch.getNextTest();
		ch.updateField(next);
		
		actual += ch.getText() + " ";

		assertEquals(expected, actual);
	}
	
	@Test
	// Average test case for previous and next for n = 3 waves, 
	// n+2 getPrevious() calls and n getNext() calls, 15 commands
	public void testCase5() {
		
		// Correct result
		String expected = "11 14 9 12 7 10 ";

		// Test result
		ch.add("1");
		ch.resetIndex();
		ch.add("2");
		ch.resetIndex();
		ch.add("3");
		ch.resetIndex();
		ch.add("4");
		ch.resetIndex();
		ch.add("5");
		ch.resetIndex();
		ch.add("6");
		ch.resetIndex();
		ch.add("7");
		ch.resetIndex();
		ch.add("8");
		ch.resetIndex();
		ch.add("9");
		ch.resetIndex();
		ch.add("10");
		ch.resetIndex();
		ch.add("11");
		ch.resetIndex();
		ch.add("12");
		ch.resetIndex();
		ch.add("13");
		ch.resetIndex();
		ch.add("14");
		ch.resetIndex();
		ch.add("15");
		ch.resetIndex();

		String actual = "";

		for (int n = 0; n < 3; n++) {
			for (int i = 0; i < 5; i++) {
				String prev = ch.getPreviousTest();
				ch.updateField(prev);
			}
			actual += ch.getText() + " ";
			
			for (int j = 0; j < 3; j++) {
				String next = ch.getNextTest();
				ch.updateField(next);
			}
			actual += ch.getText() + " ";
		}

		assertEquals(expected, actual);
	}
	
	@Test
	// Average test case with duplicate values, 10 commands
	public void testCase6() {
		
		// Correct result
		String expected = "3 3 3 3 3 2 3 3 3 2 2 2 2 1 ";

		// Test result
		ch.add("1");
		ch.resetIndex();
		ch.add("2");
		ch.resetIndex();
		ch.add("2");
		ch.resetIndex();
		ch.add("2");
		ch.resetIndex();
		ch.add("2");
		ch.resetIndex();
		ch.add("3");
		ch.resetIndex();
		ch.add("3");
		ch.resetIndex();
		ch.add("3");
		ch.resetIndex();
		ch.add("3");
		ch.resetIndex();
		ch.add("3");

		String actual = "";

		for (int i = 0; i < 6; i++) {
			String prev = ch.getPreviousTest();
			ch.updateField(prev);
			actual += ch.getText() + " ";
		}
		
		for (int j = 0; j < 2; j++) {
			String next = ch.getNextTest();
			ch.updateField(next);
			actual += ch.getText() + " ";
		}
		
		for (int i = 0; i < 6; i++) {
			String prev = ch.getPreviousTest();
			ch.updateField(prev);
			actual += ch.getText() + " ";
		}

		assertEquals(expected, actual);
	}
	
	@Test
	/* Small test case for edge cases on getPrevious(), 5 commands
	 * Even if getPrevious() is called > 5 times only the first command 
	 * should be returned. Right after, calling getNext() should return
	 * the second command and not cycle through extra copies of the first
	 * command.
	 */
	public void testCase7() {
		
		// Correct result
		String expected = "1 2 ";

		// Test result
		ch.add("1");
		ch.resetIndex();
		ch.add("2");
		ch.resetIndex();
		ch.add("3");
		ch.resetIndex();
		ch.add("4");
		ch.resetIndex();
		ch.add("5");
		ch.resetIndex();

		String actual = "";

		for (int i = 0; i < 10; i++) {
			String prev = ch.getPreviousTest();
			ch.updateField(prev);
		}
		actual += ch.getText() + " ";
		
		String next = ch.getNextTest();
		ch.updateField(next);
		actual += ch.getText() + " ";

		assertEquals(expected, actual);
	}
	
	@Test
	/* Small test case for edge cases on getNext(), 5 commands
	 * Even if getNext() is called > 5 times only the last command should
	 * be returned. Right after, calling getPrevious() should return the 
	 * second last command and not cycle through extra copies of the last
	 * command.
	 */
	public void testCase8() {
		
		// Correct result
		String expected = "5 4 ";

		// Test result
		ch.add("1");
		ch.resetIndex();
		ch.add("2");
		ch.resetIndex();
		ch.add("3");
		ch.resetIndex();
		ch.add("4");
		ch.resetIndex();
		ch.add("5");
		ch.resetIndex();

		String actual = "";

		for (int i = 0; i < 10; i++) {
			String next = ch.getNextTest();
			ch.updateField(next);
		}
		actual += ch.getText() + " ";
		
		String prev = ch.getPreviousTest();
		ch.updateField(prev);
		actual += ch.getText() + " ";

		assertEquals(expected, actual);
	}
	
	@Test
	/* Small test case for edge cases on both getNext() and getPrevious().
	 * Only the first and last commands should be returned even for excessive
	 * calls to either method.
	 */
	public void testCase9() {
		
		// Correct result
		String expected = "1 5 ";

		// Test result
		ch.add("1");
		ch.resetIndex();
		ch.add("2");
		ch.resetIndex();
		ch.add("3");
		ch.resetIndex();
		ch.add("4");
		ch.resetIndex();
		ch.add("5");
		ch.resetIndex();

		String actual = "";

		for (int i = 0; i < 10; i++) {
			String prev = ch.getPreviousTest();
			ch.updateField(prev);
		}
		actual += ch.getText() + " ";
		
		for (int i = 0; i < 10; i++) {
			String next = ch.getNextTest();
			ch.updateField(next);
		}
		actual += ch.getText() + " ";

		assertEquals(expected, actual);
	}
}
