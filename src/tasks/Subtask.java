package tasks;

import enums.TaskStatus;
import enums.TaskType;
import java.time.format.DateTimeFormatter;

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
        String startTime = "";
        if (startTime.isEmpty()) {
            startTime = getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm"));
        }

        long durationLong = 0;
        if (getDuration() != null) {
            durationLong = getDuration().toMinutes();
        }

        return "Subtask{" +
                "idEpic=" + idEpic +
                " name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", start time=" + startTime +
                ", duration=" + durationLong +
                '}';
    }
}
