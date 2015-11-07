package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import struct.View;

public class Listeners {
	
    // ======================================================================
    // Getters to allow GUI components in InterfaceController to access the
    // private ChangeListener classes
    // ======================================================================
	
	// ChangeListeners
	protected static AutoCompleteListener getAutoCompleteListener() {
		return new AutoCompleteListener();
	}
	
	protected static LostFocusListener getLostFocusListener() {
		return new LostFocusListener();
	}
	
	protected static CloseHelpListener getCloseHelpListener() {
		return new CloseHelpListener();
	}
	
	protected static ScrollListener getScrollListener(View scrollpane) {
		return new ScrollListener(scrollpane);
	}
	
	protected static WidthPositionListener getWidthPositionListener() {
		return new WidthPositionListener();
	}
	
	protected static HeightPositionListener getHeightPositionListener() {
		return new HeightPositionListener();
	}
	
	protected static HeightListener getHeightListener() {
		return new HeightListener();
	}
	
	protected static WidthListener getWidthListener() {
		return new WidthListener();
	}
	
    // ======================================================================
    // Private ChangeListener class definitions
    // ======================================================================
	
    /**
     * This method implements a listener that displays and updates the autocomplete
     * popup in real time based on the current input that is being entered by the 
     * user
     */
    protected static class AutoCompleteListener implements ChangeListener<String> {
    	@Override
    	public void changed(ObservableValue<? extends String> observable, 
    			String oldValue, String newValue) {
    		// Only perform autocompletion when the string is within one word
    		// and is not empty
    		if (newValue.split(" ").length == 1 && !newValue.equals(LogicController.NULL_STRING)) {
    			AutoComplete.updatePopup(newValue);
    		} else {
    			AutoComplete.closePopup();
    		}
    	}
    }
    
    /**
     * This class implements a listener for the main stage that decides whether
     * to keep the autocomplete listener open when focus on the main stage is lost
     */
    private static class LostFocusListener implements ChangeListener<Boolean> {
    	boolean showingBeforeLostFocus = false;
    	
    	@Override
    	public void changed(ObservableValue<? extends Boolean> observable, 
    			Boolean oldValue, Boolean newValue) {
    		        	
    		if (!newValue) {
    			showingBeforeLostFocus = AutoComplete.isShowing();
    			AutoComplete.closePopup();
    		} else {
    			if (showingBeforeLostFocus) {
    				AutoComplete.showPopup();
    			}
    		}
    	}
    }
    
    /**
     * This class implements a listener for the help stage to detect if the window
     * has been closed with a mouse click, and respond with the correct method call
     * to compensate (set isShowing status and toggling the button)
     */
    private static class CloseHelpListener implements ChangeListener<Boolean> {
    	@Override
    	public void changed(ObservableValue<? extends Boolean> observable, 
    			Boolean oldValue, Boolean newValue) {
    		// If the help window was closed by user's mouse click
    		if (newValue == false) {
    			// Do not toggle to avoid resetting the help open/close state
    			HelpController.closeHelpDialog();
    		}
    	}
    }
    
    /**
     * This class implements a listener for the scroll pane in the history view that
     * automatically scrolls the window to the most recently added element at the bottom
     */
    private static class ScrollListener implements ChangeListener<Number> {
    	View scrollpane;
    	
    	ScrollListener(View scrollpane) {
    		this.scrollpane = scrollpane;
    	}
    	
    	@Override
    	public void changed(ObservableValue<? extends Number> observable, 
    			Number oldValue, Number newValue) {
    		
    		// Set the v-value of the scroll pane to the height of the content box
    		switch (scrollpane) {
    		case HISTORY:
    			HistoryViewController.getHistScroll().setVvalue((Double)newValue);
    			break;
    		default:
    			// Ignore, should not enter
    			break;
    		}
    	}
    }
    
    /**
     * This class implements a listener for the autocomplete popup that adjusts its
     * horizontal position based on the location of the window
     */
    private static class WidthPositionListener implements ChangeListener<Number> {
    	@Override
    	public void changed(ObservableValue<? extends Number> observable, 
    			Number oldValue, Number newValue) {
    		AutoComplete.setX((double)newValue);
    	}
    }
    
    /**
     * This class implements a listener for the autocomplete popup that adjusts its
     * vertical position based on the location of the window
     */
    private static class HeightPositionListener implements ChangeListener<Number> {
    	@Override
    	public void changed(ObservableValue<? extends Number> observable, 
    			Number oldValue, Number newValue) {
    		AutoComplete.setY((double)newValue);
    	}
    }
    
    /**
     * This class implements a listener for the main window to resize all of its
     * internal components when the overall height changes
     */
    private static class HeightListener implements ChangeListener<Number> {
    	@Override
    	public void changed(ObservableValue<? extends Number> observable,
    			Number oldValue, Number newValue) {

    		// Set the height of the sidebar separator to window height
    		InterfaceController.getSbLine().setEndY((Double)newValue);

    		// Set the height of the scroll pane separator to
    		// window height - height of the filepath bar(31) -
    		// height of feedback bar(31) - height of text bar(40) - 
    		// height of viewLine(1)
    		DefaultViewController.getDefScrollLine().setEndY((Double)newValue - 
    				InterfaceController.HEIGHT_FILEPATH - 
    				InterfaceController.HEIGHT_FEEDBACK - 
    				InterfaceController.HEIGHT_TEXT_BOX - 
    				InterfaceController.HEIGHT_HORIZ_LINE);
    		
    		AutoComplete.setY(MainApp.stage.getY());
    	}
    }

    /**
     * This class implements a listener for the main window to resize all of its
     * internal components when the overall width changes
     */
    private static class WidthListener implements ChangeListener<Number> {
    	@Override
    	public void changed(ObservableValue<? extends Number> observable,
    			Number oldValue, Number newValue) {

    		// Set the width of the feedback, filepath and view box separators to
    		// window width - size of sidebar(50) - width of line(1)
    		InterfaceController.getFeedbackLine().setEndX(
    				(Double)newValue - InterfaceController.WIDTH_SIDEBAR);
    		InterfaceController.getViewLine().setEndX(
    				(Double)newValue - InterfaceController.WIDTH_SIDEBAR);
    		InterfaceController.getFilepathLine().setEndX(
    				(Double)newValue - InterfaceController.WIDTH_SIDEBAR);
    	}
    }
}
