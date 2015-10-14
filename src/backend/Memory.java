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
	
	public void clearRedoStack(){
		redoStack.clear();
	}
	
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
		
//	public State getUndoState(String currFileContents){ 
//		if(undoStack.isEmpty()){
//			return null; 
//		}else{ 
//			State prevState = undoStack.pop(); 
//			State currState = new State(currFileContents, prevState.getUserCommand()); 
//			redoStack.push(currState);
//			return prevState;
//		}
//	}
//	
//	public State getRedoState(){ 
//		if(redoStack.isEmpty()){
//			return null; 
//		}else{ 
//			State redoState = redoStack.pop(); 
//			return redoState; 
//		}
//	}
	

}
