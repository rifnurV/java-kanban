package tasks;

import enums.TaskStatus;
import enums.TaskType;

public class Subtask extends Task {

    private final int idEpic;
    private final TaskType type;

    public Subtask(String name, String description, int idEpic) {
        super(name, description);
        this.idEpic = idEpic;
        this.type = TaskType.SUBTASK;
    }

    public Subtask(int id, String name, String description, TaskStatus status, int idEpic) {
        super(id, name, description, status);
        this.idEpic = idEpic;
        this.type = TaskType.SUBTASK;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "idEpic=" + idEpic +
                " name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}';
    }
}
