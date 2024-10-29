public class Subtask extends Task {

    Long epicId;

    public Subtask(String name, String description, Status status, Long epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(Long id, String name, String description, Status status, Epic epic) {
        super(name, description, status);
        this.setId(id);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                "} ";
    }
}
