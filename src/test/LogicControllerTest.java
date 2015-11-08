package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import gui.InterfaceController;
import gui.LogicController;

public class LogicControllerTest {

	LogicController lc = new LogicController();
	
	// Ignoring all tests for default tasks and events
	// Given that the test cases need to be altered every day
	
	@Test
	// Test for getAllElementsCount(), and by extension getAllTasks() and getAllEvents()
	public void testCase1() {
		InterfaceController.initLogicControl();
		
		int expected = 25;
		int actual = lc.getAllElementsCount();
		
		assertEquals(expected, actual);
	}
	
	@Test
	// Test for getSearchElementsCount()
	public void testCase2() {
		
		int expected = 17;
		
		ArrayList<String> taskResultsTest = new ArrayList<String>();
		taskResultsTest.add("FLOAT");
		taskResultsTest.add("There are no items to display.");
		taskResultsTest.add("TUE, 08 NOV 2016");
		taskResultsTest.add("1. match");
		taskResultsTest.add("2. match");
		taskResultsTest.add("3. match");
		taskResultsTest.add("4. match");
		taskResultsTest.add("THU, 10 NOV 2016");
		taskResultsTest.add("5. match");
		taskResultsTest.add("6. match");
		taskResultsTest.add("7. match");
		taskResultsTest.add("FRI, 11 NOV 2016");
		taskResultsTest.add("8. match");
		taskResultsTest.add("9. match");
		
		ArrayList<String> eventResultsTest = new ArrayList<String>();
		eventResultsTest.add("ONGOING");
		eventResultsTest.add("10. match");
		eventResultsTest.add("11. match");
		eventResultsTest.add("WED, 09 NOV 2016");
		eventResultsTest.add("12. match");
		eventResultsTest.add("13. match");
		eventResultsTest.add("14. match");
		eventResultsTest.add("15. match");
		eventResultsTest.add("FRI, 11 NOV 2016");
		eventResultsTest.add("16. match");
		eventResultsTest.add("17. match");
		
		int actual = lc.getSearchElementsCount(taskResultsTest, eventResultsTest);
		
		assertEquals(expected, actual);
	}
	
	@Test
	// Test for getUnresElementsCount(), and by extension getUnresTasks() and getUnresEvents()
	public void testCase3() {
		
		int expected = 8;
		int actual = lc.getUnresElementsCount();
		
		assertEquals(expected, actual);
	}
	
	@Test
	// Test for getDoneElementsCount(), and by extension getDoneTasks() and getDoneEvents()
	public void testCase4() {
		
		int expected = 10;
		int actual = lc.getDoneElementsCount();
		
		assertEquals(expected, actual);
	}

	@Test
	// Test for getSummaryCount(), and by extension getDefTasks() and getDefEvents()
	public void testCase5() {
		
		int[] expected = {0, 0, 5, 1, 8};
		
		int[] actual = new int[5];
		actual = lc.getSummaryCount();
		
		assertEquals(expected[0], actual[0]);
		assertEquals(expected[1], actual[1]);
		assertEquals(expected[2], actual[2]);
		assertEquals(expected[3], actual[3]);
		assertEquals(expected[4], actual[4]);
	}
}
