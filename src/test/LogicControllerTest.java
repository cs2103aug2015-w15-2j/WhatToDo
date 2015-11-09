package test;

import static org.junit.Assert.*;

import java.nio.file.FileSystemException;
import java.util.ArrayList;

import org.junit.Test;

import gui.InterfaceController;
import gui.LogicController;
import struct.Command;

public class LogicControllerTest {

	LogicController lc = new LogicController();
	
	// Ignoring all tests for default tasks and events
	// Given that the test cases need to be altered every day
	
	@Test
	// Test for getAllElementsCount(), and by extension getAllTasks() and getAllEvents()
	public void testCase1() {
		InterfaceController.initLogicControl();
		
		try {
			LogicController.getLogic().overwriteFile("");
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
		
		// Do first initialization of the test data
		lc.runCommandTest("add float1");
		lc.runCommandTest("add float2");
		lc.runCommandTest("add float3");
		lc.runCommandTest("add float4");
		lc.runCommandTest("add float5");
		lc.runCommandTest("add float6");
		lc.runCommandTest("add float7");
		lc.runCommandTest("add float8");
		lc.runCommandTest("add float9");
		lc.runCommandTest("add deadline1 by 081116");
		lc.runCommandTest("add deadline2 by 091116");
		lc.runCommandTest("add deadline3 by 101116");
		lc.runCommandTest("add deadline4 by 111116");
		lc.runCommandTest("add deadline5 by 111116");
		lc.runCommandTest("add deadline6 by 121116");
		lc.runCommandTest("add deadline7 by 121116");
		lc.runCommandTest("add event1 from 051116 0000 to 061116 0000");
		lc.runCommandTest("add event2 from 061116 0000 to 061116 2000");
		lc.runCommandTest("add event3 from 071116 0000 to 071116 2100");
		lc.runCommandTest("add event4 from 091116 0000 to 131116 0000");
		lc.runCommandTest("add event5 from 101116 0000 to 111116 0000");
		lc.runCommandTest("add event6 from 101116 0000 to 111116 0000");
		lc.runCommandTest("add event7 from 101116 0000 to 111116 0000");
		lc.runCommandTest("add event8 from 121116 0000 to 151116 0000");
		lc.runCommandTest("done 1");
		lc.runCommandTest("done 2");
		lc.runCommandTest("done 3");
		lc.runCommandTest("done 4");
		lc.runCommandTest("done 5");
		lc.runCommandTest("done 17");
		lc.runCommandTest("done 18");
		lc.runCommandTest("done 19");
		lc.runCommandTest("done 20");
		lc.runCommandTest("done 21");
		lc.runCommandTest("done 22");
		
		int expected = 13;
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
		
		int expected = 0;
		int actual = lc.getUnresElementsCount();
		
		assertEquals(expected, actual);
	}
	
	@Test
	// Test for getDoneElementsCount(), and by extension getDoneTasks() and getDoneEvents()
	public void testCase4() {
		
		int expected = 11;
		int actual = lc.getDoneElementsCount();
		
		assertEquals(expected, actual);
	}
	
	@Test
	// Test for getDefElementsCount(), and by extension getDefTasks() and getDefEvents()
	public void testCase5() {
		
		int expected = 4;
		int actual = lc.getDefElementsCount();
		
		assertEquals(expected, actual);
	}

	@Test
	// Test for getSummaryCount(), and by extension getDefTasks() and getDefEvents()
	public void testCase6() {
		
		int[] expected = {0, 0, 4, 0, 0};
		
		int[] actual = new int[5];
		actual = lc.getSummaryCount();
		
		// Remove the added lines
		for (int i = 0; i < 35; i++) {
			lc.runCommandTest("delete 1");
		}
		
		assertEquals(expected[0], actual[0]);
		assertEquals(expected[1], actual[1]);
		assertEquals(expected[2], actual[2]);
		assertEquals(expected[3], actual[3]);
		assertEquals(expected[4], actual[4]);
	}
}
