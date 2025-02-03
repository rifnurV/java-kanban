import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private int idTask = 1;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer,Subtask> subTasks = new HashMap<>();
    private HashMap<Integer,Epic> epics = new HashMap<>();


    public int getIdTask() {
        return idTask++;
    }

    public ArrayList<Task> getTasks() {
       ArrayList<Task> taskLists = new ArrayList<>();
       for (Task t : tasks.values()) {
           taskLists.add(t);
       }
       return taskLists;
    }

    public ArrayList<Subtask> getSubtask() {
        ArrayList<Subtask> subTasksList = new ArrayList<>();
        for (Subtask t : subTasks.values()) {
            subTasksList.add(t);
        }
        return subTasksList;
    }

    public ArrayList<Epic> getEpic() {
        ArrayList<Epic> epicsList = new ArrayList<>();
        for (Epic t : epics.values()) {
            epicsList.add(t);
        }
        return epicsList;
    }

    public void deleteTask(){
        tasks.clear();
    }

    public void deleteSubtask(){
        subTasks.clear();
    }

    public void deleteEpic(){
        epics.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Subtask getSubtask(int id) {
        return subTasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public Task addTask(Task task) {
        int idTask =getIdTask();
        task.setId(idTask);
        tasks.put(idTask, task);
        return task;
    }

    public Epic addEpic(Epic epic) {
        epic.setId(getIdTask());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Subtask addSubtask(Subtask subtask) {
        int idSubtask =getIdTask();
        subtask.setId(idSubtask);
        Epic epic = epics.get(subtask.getIdEpic());
        epic.addSubtask(subtask);
        subTasks.put(subtask.getId(), subtask);


        return subtask;
    }

    public Task updateTask(Task task) {
        int idTask =task.getId();
        if (tasks.containsKey(idTask)) {
            tasks.put(idTask, task);
            return task;
        }
        return null;

    }

    public Subtask updateSubtask(Subtask subtask) {
        int idSubtask =subtask.getId();
        if (subTasks.containsKey(idSubtask)) {
            int epicID = subtask.getIdEpic();
            Subtask oldSubtask = subTasks.get(idSubtask);
            subTasks.replace(idSubtask, subtask);
            // обновляем подзадачу в списке подзадач эпика и проверяем статус эпика
            Epic epic = epics.get(epicID);
            ArrayList<Subtask> subtaskList = epic.getSubtasksList();
            subtaskList.remove(oldSubtask);
            subtaskList.add(subtask);
            epic.updateSubtask(subtask);
            updateEpicStatus(epic);
            return subtask;

        }
        return null;
    }

    private void updateEpicStatus(Epic epic) {
        int counterNEW = 0;
        int counterDONE = 0;
        ArrayList<Subtask> list = epic.getSubtasksList();

        for (Subtask subtask : list) {
            if (subtask.getStatus() == TaskStatus.DONE) {
                counterDONE++;
            }
            if (subtask.getStatus() == TaskStatus.NEW) {
                counterNEW++;
            }
        }
        if (counterDONE == list.size()) {
            epic.setStatus(TaskStatus.DONE);
        } else if (counterNEW != list.size()) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }


}
