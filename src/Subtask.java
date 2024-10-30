public class Subtask extends Task {

    private Long epicId;

    public Subtask(String name, String description, Status status, Long epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(Long id, String name, String description, Status status, Long epicId) {
        this(name, description, status, epicId);
        this.setId(id);
    }

    public Long getEpicId() {
        return epicId;
    }

    public void setEpicId(Long epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", primary info: " + super.toString() + "}";
    }
}
