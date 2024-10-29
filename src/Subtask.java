public class Subtask extends Task {

    Epic epic;

    public Subtask(String name, String description, Status status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
        epic.addSubtask(this);
    }

    public Subtask(Long id, String name, String description, Status status, Epic epic) {
        super(name, description, status);
        this.setId(id);
        this.epic = epic;
        epic.addSubtask(this);
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
        epic.updateStatus();
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epic=" + epic +
                "} ";
    }
}
