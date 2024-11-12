package model;

public class Subtask extends Task {

    private Long epicId;

    public Subtask(String name, String description, Status status, Long epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(Long id, String name, String description, Status status, Long epicId) {
        this(name, description, status, epicId);
        if (epicId.equals(id)) {
            throw new IllegalArgumentException("Subtask cannot have same id as epic");
        }
        this.setId(id);
    }

    public Subtask(Subtask subtask) {
        super(subtask);
        this.epicId = subtask.epicId;
    }

    public Long getEpicId() {
        return epicId;
    }

    public void setEpicId(Long epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "model.Subtask{" +
                "epicId=" + epicId +
                ", primary info: " + super.toString() + "}";
    }
}
