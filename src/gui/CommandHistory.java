package gui;

import java.util.ArrayList;

public class CommandHistory {

    private ArrayList<String> history;
    private int currentIndex;

    public CommandHistory() {
        history = new ArrayList<String>();
        currentIndex = history.size() - 1;
    }

    public void add(String newCommand) {
        history.add(newCommand);
        currentIndex++;
    }

    public void resetIndex() {
        currentIndex = history.size() - 1;
    }

    public String getPrevious() {

        // If command history is empty or
        // currentIndex = -1, meaning the user has viewed till the very first command
        if (history.isEmpty() || currentIndex == -1) {
            return "";
        } else {
            String returnedCommand = history.get(currentIndex);
            if (currentIndex != 0) {
                currentIndex--;
            }
            return returnedCommand;
        }
    }

    public String getNext() {

        // If command history is empty or
        // currentIndex = history.size() - 1, meaning the user has viewed till the very last command
        if (history.isEmpty() || currentIndex > history.size() - 1) {
            return "";
        } else {
            String returnedCommand = history.get(currentIndex);
            if (currentIndex != history.size() - 1) {
                currentIndex++;
            }
            return returnedCommand;
        }
    }
}
