package tasks;

import enums.TaskStatus;
import enums.TaskType;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Subtask> subtasksList = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
        this.setType(TaskType.EPIC);
    }

    public Epic(String name, String description) {
        super(name, description);
        this.setType(TaskType.EPIC);
    }

    @Override
    public Duration getDuration() {
        return subtasksList.stream()
                .map(subtask -> subtask.getDuration()).reduce(Duration.ZERO, Duration::plus);
    }

    @Override
    public LocalDateTime getStartTime() {
        return subtasksList.stream()
                .map(Subtask::getStartTime)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    @Override
    public LocalDateTime getEndTime() {
        return subtasksList.stream()
                .map(subtask -> subtask.getStartTime().plus(subtask.getDuration()))
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    public void addSubtask(Subtask subtask) {
       for (Subtask subtask1 : subtasksList) {
           if (subtask.isOverlapTasks(subtask1)){
               throw new IllegalArgumentException("Subtask already overlaps with " + subtask1);
           }
       }
        subtasksList.add(subtask);
    }

    public ArrayList<Subtask> getSubtasksList() {
        return subtasksList;
    }

    public void updateSubtask(Subtask subtask) {
        subtasksList.remove(subtask);
        subtasksList.add(subtask);
    }

    public void clearSubtasks() {
        subtasksList.clear();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasksList +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtasksList = " + subtasksList.toArray() +
                ", id=" + id +
                '}';
    }
}
