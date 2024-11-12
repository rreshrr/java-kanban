package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final int HISTORY_DEEP;

    private final List<Task> history;

    public InMemoryHistoryManager() {
        this(10);
    }

    public InMemoryHistoryManager(int historyDeep) {
        HISTORY_DEEP = historyDeep;
        history = new ArrayList<>(historyDeep);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }

    @Override
    public void add(Task task) {
        if (history.size() >= HISTORY_DEEP) {
            history.removeFirst();
        }
        history.add(new Task(task));
    }

    @Override
    public void add(Epic epic) {
        if (history.size() >= HISTORY_DEEP) {
            history.removeFirst();
        }
        history.add(new Epic(epic));
    }

    @Override
    public void add(Subtask subtask) {
        if (history.size() >= HISTORY_DEEP) {
            history.removeFirst();
        }
        history.add(new Subtask(subtask));
    }

}
