/**
 * This class encapsulates memory of the application 
 * 
 * @@author A0127051U
 */

package backend;

import java.util.Stack;

import struct.State;

public class Memory {
	
	private Stack<State> undoStack; 
	private Stack<State> redoStack; 
	
	//============================================
	// Constructor
	//============================================
	
	public Memory() {
		undoStack = new Stack<State>(); 
		redoStack = new Stack<State>(); 
	}
	
	//============================================
	// Public methods
	//============================================
	
	public void savePrevState(State prevState){ 
		undoStack.push(prevState);
	}
	
	public State getUndoState(String currFileContents){ 
		return getState(currFileContents, undoStack, redoStack);
	}
	
	public State getRedoState(String currFileContents){ 
		return getState(currFileContents, redoStack, undoStack);
	}
	
	public void clearUndoStack(){
		undoStack.clear();
	}
	
	public void clearRedoStack(){
		redoStack.clear();
	}
	
	//============================================
	// Private method
	//============================================

	private State getState(String currFileContents, Stack<State> stackPop, Stack<State> stackPush){ 
		if(stackPop.isEmpty()){
			return null; 
		}else{ 
			State prevState = stackPop.pop(); 
			State currState = new State(currFileContents, prevState.getUserCommand()); 
			stackPush.push(currState);
			return prevState;
		}
	}
	
}
