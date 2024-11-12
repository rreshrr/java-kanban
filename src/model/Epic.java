package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Long> subtasksIds;

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        subtasksIds = new ArrayList<>();
    }

    public Epic(Long id, String name, String description) {
        this(name, description);
        setId(id);
    }

    public Epic(Epic epic) {
        super(epic);
        this.subtasksIds = new ArrayList<>(epic.subtasksIds); // создаем копию списка подзадач
    }

    public List<Long> getSubtasksIds() {
        return subtasksIds;
    }

    public void addSubtask(Long subtaskId) {
        if (subtaskId.equals(getId())) {
            throw new IllegalArgumentException("Epic cannot be own subtask");
        }
        subtasksIds.add(subtaskId);
    }

    public void removeSubtask(Long subtaskId) {
        subtasksIds.remove(subtaskId);
    }

    public void clearSubtasks() {
        subtasksIds.clear();
    }

    @Override
    public String toString() {
        return "model.Epic{" +
                "subtasksIds=" + subtasksIds +
                ", primary info: " + super.toString() + "}";
    }
}
