package tasks;

import enums.TaskStatus;
import enums.TaskType;

public class Subtask extends Task {

    private final int idEpic;

    public Subtask(String name, String description, int idEpic) {
        super(name, description);
        this.idEpic = idEpic;
        this.setType(TaskType.SUBTASK);
    }

    public Subtask(int id, String name, String description, TaskStatus status, int idEpic) {
        super(id, name, description, status);
        this.idEpic = idEpic;
        this.setType(TaskType.SUBTASK);
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public String toString() {
        String taskStirng = super.toString();
        return taskStirng + ", idEpic " + idEpic;
    }
}
