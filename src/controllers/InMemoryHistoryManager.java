package controllers;


import model.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final int historyDeep;

    private final List<Task> history;

    public InMemoryHistoryManager() {
        this(10);
    }

    public InMemoryHistoryManager(int historyDeep) {
        this.historyDeep = historyDeep;
        history = new ArrayList<>(historyDeep);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }

    @Override
    public void add(Task task) {
        if (history.size() >= historyDeep) {
            history.removeFirst();
        }
        history.add(new Task(task));
    }
}
