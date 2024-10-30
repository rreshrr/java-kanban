import java.util.ArrayList;
import java.util.List;
public class Epic extends Task {

    private List<Long> subtasksIds;

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        subtasksIds = new ArrayList<>();
    }

    public Epic(Long id, String name, String description) {
        this(name, description);
        setId(id);
    }

    public List<Long> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(List<Long> subtasks) {
        this.subtasksIds = subtasks;
    }

    public void addSubtask(Long subtaskId){
        subtasksIds.add(subtaskId);
    }

    public void removeSubtask(Long subtaskId){
        subtasksIds.remove(subtaskId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasksIds=" + subtasksIds +
                ", primary info: " + super.toString() + "}";
    }
}
