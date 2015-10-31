package gui;

import java.util.ArrayList;

public class ViewIndexMap {

	// Each of the individual elements is the file index of the ith
	// element in the array.
	// Array index = view index
	// Array element = file index
	private static ArrayList<Integer> defMap;
	private static ArrayList<Integer> allMap;
	private static ArrayList<Integer> searchMap;
	private static ArrayList<Integer> unresMap;
	
	public static void initAllMaps() {
		
		defMap = new ArrayList<Integer>();
		allMap = new ArrayList<Integer>();
		searchMap = new ArrayList<Integer>();
		unresMap = new ArrayList<Integer>();
		
		// Add an element to index 0 to offset the starting index
		defMap.add(-1);
		allMap.add(-1);
		searchMap.add(-1);
		unresMap.add(-1);
	}
	
	// ============================================================
	// Getters to access the individual view maps
	// ============================================================
	
	public static int getFromDefMap(int viewIndex) {
		if (viewIndex >= defMap.size()) {
			return -1;
		} else if (viewIndex <= 0) {
			return viewIndex;
		} else {
			return defMap.get(viewIndex);
		}
	}
	
	public static int getFromAllMap(int viewIndex) {
		if (viewIndex >= allMap.size()) {
			return -1;
		} else if (viewIndex <= 0) {
			return viewIndex;
		} else {
			return allMap.get(viewIndex);
		}
	}
	
	public static int getFromSearchMap(int viewIndex) {
		if (viewIndex >= searchMap.size()) {
			return -1;
		} else if (viewIndex <= 0) {
			return viewIndex;
		} else {
			return searchMap.get(viewIndex);
		}
	}
	
	public static int getFromUnresMap(int viewIndex) {
		if (viewIndex >= unresMap.size()) {
			return -1;
		} else if (viewIndex <= 0) {
			return viewIndex;
		} else {
			return unresMap.get(viewIndex);
		}
	}
	
	public static int get(int viewIndex) {
		switch (InterfaceController.getCurrentView()) {
		case DEFAULT:
			return getFromDefMap(viewIndex);
		case ALL:
			return getFromAllMap(viewIndex);
		case UNRESOLVED:
			return getFromUnresMap(viewIndex);
		case SEARCH:
			return getFromSearchMap(viewIndex);
		default:
			// Should not enter
			return -1;
		}
	}
	
	// ============================================================
	// Setters to access the individual view maps
	// ============================================================
	
	public static void addToDefMap(int fileIndex) {
		defMap.add(fileIndex);
	}
	
	public static void addToAllMap(int fileIndex) {
		allMap.add(fileIndex);
	}
	
	public static void addToSearchMap(int fileIndex) {
		searchMap.add(fileIndex);
	}
	
	public static void addToUnresMap(int fileIndex) {
		unresMap.add(fileIndex);
	}
	
	public static void setDefMap(int viewIndex, int fileIndex) {
		defMap.set(viewIndex, fileIndex);
	}
	
	public static void setAllMap(int viewIndex, int fileIndex) {
		allMap.set(viewIndex, fileIndex);
	}
	
	public static void setSearchMap(int viewIndex, int fileIndex) {
		searchMap.set(viewIndex, fileIndex);
	}
	
	public static void setUnresMap(int viewIndex, int fileIndex) {
		unresMap.set(viewIndex, fileIndex);
	}
	
	// ============================================================
	// Reset functions to reset the view maps during updates
	// ============================================================
	
	public static void resetDefMap() {
		defMap = new ArrayList<Integer>();
		defMap.add(-1);
	}
	
	public static void resetAllMap() {
		allMap = new ArrayList<Integer>();
		allMap.add(-1);
	}
	
	public static void resetSearchMap() {
		searchMap = new ArrayList<Integer>();
		searchMap.add(-1);
	}
	
	public static void resetUnresMap() {
		unresMap = new ArrayList<Integer>();
		unresMap.add(-1);
	}
}
