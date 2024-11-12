package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {
    List<? extends Task> getAllTypesTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    List<Task> getTasks();

    List<Task> getHistory();

    void clearTasks();

    void clearEpics();

    void clearSubtasks();

    Task getTask(Long taskId);

    Epic getEpic(Long epicId);

    List<Subtask> getEpicSubtasks(Long epicId);

    Subtask getSubtask(Long subtaskId);

    Long create(Task task);

    Long create(Epic epic);

    Long create(Subtask subtask);

    void update(Task task);

    void update(Epic epic);

    void update(Subtask subtask);

    Task removeTask(Long taskId);

    Epic removeEpic(Long epicId);

    Subtask removeSubtask(Long subtaskId);
}
