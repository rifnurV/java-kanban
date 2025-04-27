package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    HistoryManager getHistoryManager();

    void setHistoryManager(HistoryManager manager);

    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtask();

    ArrayList<Epic> getEpic();

    void deleteTask();

    void deleteSubtask();

    void deleteEpic();

    Task getTaskById(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    List<Subtask> getSubtasksByEpikId(int id);

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

    List<Task> getPrioritizedTasks();
}
