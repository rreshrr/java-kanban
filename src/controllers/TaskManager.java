package controllers;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class TaskManager {

    private static final AtomicLong TASK_COUNTER = new AtomicLong(133);
    private final Map<Long, Task> tasks;
    private final Map<Long, Epic> epics;
    private final Map<Long, Subtask> subtasks;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    private static Long getIdForNewTask() {
        return TASK_COUNTER.getAndIncrement();
    }

    public List<? extends Task> getAllTypesTasks() {
        return Stream.of(tasks, epics, subtasks)
                .flatMap(map -> map.values().stream())
                .toList();
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public Collection<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void clearSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            updateEpicStatus(epic.getId());
        }
        subtasks.clear();
    }

    public Task getTask(Long taskId) {
        checkTaskId(taskId, tasks);
        return tasks.get(taskId);
    }

    public Epic getEpic(Long epicId) {
        checkTaskId(epicId, epics);
        return epics.get(epicId);
    }

    public Subtask getSubtask(Long subtaskId) {
        checkTaskId(subtaskId, subtasks);
        return subtasks.get(subtaskId);
    }

    public Long create(Task task) {
        task.setId(getIdForNewTask());
        return save(task);
    }

    public Long create(Epic epic) {
        epic.setId(getIdForNewTask());
        return save(epic);
    }

    public Long create(Subtask subtask) {
        subtask.setId(getIdForNewTask());
        save(subtask);
        Epic epic = getEpic(subtask.getEpicId());
        //добавлям subtask к списку subtasks его эпика
        epic.addSubtask(subtask.getId());
        updateEpicStatus(epic);
        return subtask.getId();
    }

    public void update(Task task) {
        checkTaskId(task.getId(), tasks);
        save(task);
    }

    public void update(Epic epic) {
        checkTaskId(epic.getId(), epics);
        save(epic);
    }

    public void update(Subtask subtask) {
        checkTaskId(subtask.getId(), subtasks);
        save(subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    public Task removeTask(Long taskId) {
        checkTaskId(taskId, tasks);
        return tasks.remove(taskId);
    }

    public Epic removeEpic(Long epicId) {
        checkTaskId(epicId, epics);
        Epic epic = epics.get(epicId);
        List<Long> subtaskIdsCopy = new ArrayList<>(epic.getSubtasksIds());
        subtaskIdsCopy.forEach(this::removeSubtask);
        return epics.remove(epicId);
    }

    public Subtask removeSubtask(Long subtaskId) {
        checkTaskId(subtaskId, subtasks);
        Subtask subtask = subtasks.remove(subtaskId);
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(subtaskId);
        updateEpicStatus(epic);
        return subtask;
    }

    private void checkTaskId(Long taskId, Map<Long, ? extends Task> tasks) {
        if (!tasks.containsKey(taskId)) {
            throw new IllegalArgumentException("There is no item with this ID: %s in %s".formatted(taskId, tasks));
        }
    }

    private void updateEpicStatus(Long epicId) {
        updateEpicStatus(getEpic(epicId));
    }

    private void updateEpicStatus(Epic epic) {
        List<Subtask> subtasksOfEpic = epic.getSubtasksIds()
                .stream()
                .map(this::getSubtask)
                .toList();

        if (subtasksOfEpic.stream().allMatch(s -> s.getStatus() == Status.NEW)) {
            epic.setStatus(Status.NEW);
        } else if (subtasksOfEpic.stream().allMatch(s -> s.getStatus() == Status.DONE)) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    private Long save(Task task) {
        tasks.put(task.getId(), task);
        return task.getId();
    }

    private Long save(Epic epic) {
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    private Long save(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        return subtask.getId();
    }
}
