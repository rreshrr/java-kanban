import java.util.ArrayList;
import java.util.List;
public class Epic extends Task {

    private List<Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description, null);
        subtasks = new ArrayList<>();
        setStatus(Status.NEW);
    }

    public Epic(Long id, String name, String description) {
        this(name, description);
        setId(id);
        subtasks = new ArrayList<>();
        setStatus(Status.NEW);
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public void addSubtask(Subtask subtask){
        subtasks.add(subtask);
        updateStatus();
    }


    public void updateStatus() {
        if (subtasks.stream().allMatch(subtask -> subtask.getStatus().equals(Status.NEW))) {
            setStatus(Status.NEW);
        } else if (subtasks.stream().allMatch(subtask -> subtask.getStatus().equals(Status.DONE))) {
            setStatus(Status.DONE);
        } else {
            setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks size=" + subtasks.size() +
                ", primary info: {" + super.toString() + "}";
    }
}
