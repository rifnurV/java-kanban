import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtask();

    ArrayList<Epic> getEpic();

    void deleteTask();

    void deleteSubtask();

    void deleteEpic();

    Task getTaskById(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    ArrayList<Subtask> getSubtasksByEpikId(int id);

    void deleteTaskById(int id);

    void deleteSubtaskById(int id);

    void deleteEpicById(int id);

    Task addTask(Task task);

    Epic addEpic(Epic epic);

    Subtask addSubtask(Subtask subtask);

    Task updateTask(Task task);

    Epic updateEpic(Epic epic);

    Subtask updateSubtask(Subtask subtask);

    List<Task> getHistory();


}
