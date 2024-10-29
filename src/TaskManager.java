import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {

    private static long TASK_COUNTER = 0;
    Map<Long, Task> tasks;

    public TaskManager(){
        tasks = new HashMap<>();
    }

    public Collection<Task> getAllTypesTasks(){
        return tasks.values();
    }

    public Collection<Task> getEpics(){
        return  tasks.values().parallelStream().filter(this::isEpic).toList();
    }

    public Collection<Task> getSubtasks(){
        return  tasks.values().parallelStream().filter(this::isSubtask).toList();
    }

    public Collection<Task> getCommonTasks(){
        return  tasks.values().parallelStream().filter(this::isTask).toList();
    }

    public void clearTasks(){
        tasks.clear();
    }

    public Task getTask(Long taskId){
        checkTaskId(taskId);
        return tasks.get(taskId);
    }

    public Long createTask(Task task){
        Long taskId = getIdForNewTask();
        task.setId(taskId);
        tasks.put(taskId, task);
        return taskId;
    }

    public void updateTask(Task task){
        Long taskId = task.getId();
        checkTaskId(taskId);
        tasks.put(taskId, task);
    }

    public void removeTask(Long taskId){
        checkTaskId(taskId);
        tasks.remove(taskId);
    }

    public Collection<Subtask> getSubtasks(Long epicId){
        Epic epic = getEpicById(epicId);
        return epic.getSubtasks();
    }

    private void checkTaskId(Long taskId){
        if (!tasks.containsKey(taskId)){
            throw new IllegalArgumentException("There is no task with this ID: %s".formatted(taskId));
        }
    }

    private Epic getEpicById(Long epicId){
        Task epicTask = getTask(epicId);
        Epic epic;
        if (isEpic(epicTask)){
            epic = (Epic) epicTask;
        } else {
            throw new IllegalArgumentException("There is no task with this ID: %s".formatted(epicId));
        }
        return epic;
    }

    private boolean isEpic(Task task){
        return task instanceof Epic;
    }

    private boolean isSubtask(Task task){
        return task instanceof Subtask;
    }

    private boolean isTask(Task task){
        return task.getClass().equals(Task.class);
    }

    private static Long getIdForNewTask(){
        return TASK_COUNTER++;
    }

}
