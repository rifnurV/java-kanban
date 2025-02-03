import java.util.ArrayList;

public class Epic extends Task{

   private ArrayList<Subtask>  subtasksList = new ArrayList<>();

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public void addSubtask(Subtask subtask) {
        subtasksList.add(subtask);
    }

    public ArrayList<Subtask> getSubtasksList() {
        return subtasksList;
    }

    public void updateSubtask(Subtask subtask) {
        subtasksList.remove(subtask);
        subtasksList.add(subtask);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasksList +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtasksList = "+subtasksList.toArray()+
                ", id=" + id +
                '}';
    }
}
