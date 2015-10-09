package gui;

import java.nio.file.FileSystemException;

import backend.Logic;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LogicController {

	private static final String MESSAGE_ERROR_FILESYSTEM = "Failed to create the file.";
	
	private static Logic logic;
	private static CommandHistory commandHistory;
	
	public LogicController() {
		try {
			logic = new Logic();
		} catch (FileSystemException e) {
			logic = null;
		}
		
		commandHistory = new CommandHistory();
	}
	
	public static String getFilePath() {
		
		if (logic != null) {
			return logic.getFilepath();
		} else {
			return MESSAGE_ERROR_FILESYSTEM;
		}
	}
	
	public UpKeyHandler getUpKeyHandler() {
		return new UpKeyHandler();
	}
	
	public DownKeyHandler getDownKeyHandler() {
		return new DownKeyHandler();
	}
	
	private static class UpKeyHandler implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.UP) {
                InterfaceController.textField.setText(commandHistory.getPrevious());
            }
        }
    }

    private static class DownKeyHandler implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.DOWN) {
                InterfaceController.textField.setText(commandHistory.getNext());
            }
        }
    }
}
