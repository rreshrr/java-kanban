package controllers;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import util.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class InMemoryTaskManager implements TaskManager {

    private static final AtomicLong TASK_COUNTER = new AtomicLong(133);
    private final Map<Long, Task> tasks;
    private final Map<Long, Epic> epics;
    private final Map<Long, Subtask> subtasks;
    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    private static Long getIdForNewTask() {
        return TASK_COUNTER.getAndIncrement();
    }

    @Override
    public List<? extends Task> getAllTypesTasks() {
        return Stream.of(tasks, epics, subtasks)
                .flatMap(map -> map.values().stream())
                .toList();
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            updateEpicStatus(epic.getId());
        }
        subtasks.clear();
    }

    @Override
    public Task getTask(Long taskId) {
        checkTaskId(taskId, tasks);
        Task task = tasks.get(taskId);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpic(Long epicId) {
        checkTaskId(epicId, epics);
        Epic epic = epics.get(epicId);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public List<Subtask> getEpicSubtasks(Long epicId) {
        checkTaskId(epicId, epics);
        return epics
                .get(epicId)
                .getSubtasksIds()
                .stream()
                .map(subtasks::get)
                .toList();
    }

    @Override
    public Subtask getSubtask(Long subtaskId) {
        checkTaskId(subtaskId, subtasks);
        Subtask subtask = subtasks.get(subtaskId);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Long create(Task task) {
        task.setId(getIdForNewTask());
        return save(task);
    }

    @Override
    public Long create(Epic epic) {
        epic.setId(getIdForNewTask());
        return save(epic);
    }

    @Override
    public Long create(Subtask subtask) {
        subtask.setId(getIdForNewTask());
        save(subtask);
        Long epicId = subtask.getEpicId();
        checkTaskId(epicId, epics);
        Epic epic = epics.get(epicId);
        //добавлям subtask к списку subtasks его эпика
        epic.addSubtask(subtask.getId());
        updateEpicStatus(epic);
        return subtask.getId();
    }

    @Override
    public void update(Task task) {
        checkTaskId(task.getId(), tasks);
        save(task);
    }

    @Override
    public void update(Epic epic) {
        checkTaskId(epic.getId(), epics);
        save(epic);
    }

    @Override
    public void update(Subtask subtask) {
        checkTaskId(subtask.getId(), subtasks);
        save(subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    @Override
    public Task removeTask(Long taskId) {
        checkTaskId(taskId, tasks);
        return tasks.remove(taskId);
    }

    @Override
    public Epic removeEpic(Long epicId) {
        checkTaskId(epicId, epics);
        Epic epic = epics.get(epicId);
        List<Long> subtaskIdsCopy = new ArrayList<>(epic.getSubtasksIds());
        subtaskIdsCopy.forEach(this::removeSubtask);
        return epics.remove(epicId);
    }

    @Override
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
        checkTaskId(epicId, epics);
        updateEpicStatus(epics.get(epicId));
    }

    private void updateEpicStatus(Epic epic) {
        List<Subtask> subtasksOfEpic = epic.getSubtasksIds()
                .stream()
                .map(subtasks::get)
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
