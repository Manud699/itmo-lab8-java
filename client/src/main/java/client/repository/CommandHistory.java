package client.repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CommandHistory {

    private final LinkedList<String> historyList;
    private final int LIMIT = 11;

    public CommandHistory() {
        this.historyList = new LinkedList<>();
    }
    public void addCommand(String commandName) {
        if (historyList.size() >= LIMIT) {
            historyList.removeFirst();
        }
        historyList.addLast(commandName);
    }


    public List<String> getHistoryList() {
        return new ArrayList<>(historyList);
    }
}