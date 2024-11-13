package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();

    void add(Task task);
}
