import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaskManager {

    private static final AtomicLong TASK_COUNTER = new AtomicLong(133);
    Map<Long, Task> tasks;
    Map<Long, Epic> epics;
    Map<Long, Subtask> subtasks;

    public TaskManager(){
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    public Collection<Task> getAllTypesTasks(){
       return Stream.of(tasks, epics, subtasks)
               .flatMap(map -> map.values().stream())
               .collect(Collectors.toList());
    }

    public Collection<Epic> getEpics(){
        return epics.values();
    }

    public Collection<Subtask> getSubtasks(){
        return  subtasks.values();
    }

    public Collection<Task> getTasks(){
        return  tasks.values();
    }

    public void clearAll(){
        clearTasks();
        clearEpics();
        clearSubtasks();
    }

    public void clearTasks(){
        tasks.clear();
    }

    public void clearEpics(){
        epics.clear();
    }

    public void clearSubtasks(){
        subtasks.clear();
    }

    public Task getTask(Long taskId){
        checkTaskId(taskId, tasks);
        return tasks.get(taskId);
    }

    public Epic getEpic(Long epicId){
        checkTaskId(epicId, epics);
        return epics.get(epicId);
    }

    Subtask getSubtask(Long subtaskId){
        checkTaskId(subtaskId, subtasks);
        return subtasks.get(subtaskId);
    }

    public Long create(Task task){
        return saveTask(task);
    }

    public Long create(Subtask subtask){
        saveTask(subtask);
        Epic epic = getEpic(subtask.getEpicId());
        //добавлям subtask к списку subtasks его эпика
        epic.addSubtask(subtask.getId());
        updateEpicStatus(epic);
        return subtask.getId();
    }

    public void update(Task task){
        checkTaskId(task.getId(), tasks);
        saveTask(task);
    }

    public void update(Epic epic){
        checkTaskId(epic.getId(), epics);
        saveTask(epic);
    }

    public void update(Subtask subtask){
        checkTaskId(subtask.getId(), subtasks);
        saveTask(subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    public Task removeTask(Long taskId){
        checkTaskId(taskId, tasks);
        return tasks.remove(taskId);
    }

    public Epic removeEpic(Long epicId){
        checkTaskId(epicId, epics);
        Epic epic = epics.get(epicId);
        List<Long> subtaskIdsCopy = new ArrayList<>(epic.getSubtasksIds());
        subtaskIdsCopy.forEach(this::removeSubtask);
        return epics.remove(epicId);
    }

    public Subtask removeSubtask(Long subtaskId){
        checkTaskId(subtaskId, subtasks);
        Subtask subtask = subtasks.remove(subtaskId);
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(subtaskId);
        updateEpicStatus(epic);
        return subtask;
    }

    private void checkTaskId(Long taskId, Map<Long, ? extends Task> tasks){
        if (!tasks.containsKey(taskId)){
            throw new IllegalArgumentException("There is no item with this ID: %s in %s".formatted(taskId, tasks));
        }
    }

    private void updateEpicStatus(Long epicId){
        updateEpicStatus(getEpic(epicId));
    }

    private void updateEpicStatus(Epic epic){
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

    private static Long getIdForNewTask(){
        return TASK_COUNTER.getAndIncrement();
    }

    private Long saveTask(Task task) {
        if (task.getId() == null) {
            task.setId(getIdForNewTask());
        }

        if (task instanceof Subtask) {
            subtasks.put(task.getId(), (Subtask) task);
        } else if (task instanceof Epic) {
            epics.put(task.getId(), (Epic) task);
        } else {
            tasks.put(task.getId(), task);
        }

        return task.getId();
    }

}
