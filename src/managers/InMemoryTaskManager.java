package managers;

import enums.TaskStatus;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private int idTask = 1;

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private HistoryManager historyManager;
    private final TreeSet<Task> sortTasks;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
        this.sortTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId));
    }


    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public void setHistoryManager(HistoryManager manager) {
        this.historyManager = manager;
    }

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> taskLists = new ArrayList<>();
        for (Task t : tasks.values()) {
            taskLists.add(t);
        }
        return taskLists;
    }

    @Override
    public ArrayList<Subtask> getSubtask() {
        ArrayList<Subtask> subTasksList = new ArrayList<>();
        for (Subtask t : subTasks.values()) {
            subTasksList.add(t);
        }
        return subTasksList;
    }

    @Override
    public ArrayList<Epic> getEpic() {
        ArrayList<Epic> epicsList = new ArrayList<>();
        for (Epic t : epics.values()) {
            epicsList.add(t);
        }
        return epicsList;
    }

    @Override
    public void deleteTask() {
        tasks.clear();
    }

    @Override
    public void deleteSubtask() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            epic.setStatus(TaskStatus.NEW);
        }
    }

    @Override
    public void deleteEpic() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public ArrayList<Subtask> getSubtasksByEpikId(int id) {
        Epic epic = epics.get(id);
        ArrayList<Subtask> subtaskArrayList = epic.getSubtasksList();
        return subtaskArrayList;
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subTasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getIdEpic());
        ArrayList<Subtask> subtaskArrayList = epic.getSubtasksList();
        subtaskArrayList.remove(subtask);
        updateEpicStatus(epic);
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Subtask subtask : epic.getSubtasksList()) {
                subTasks.remove(subtask.getId());
                historyManager.remove(subtask.getId());
            }
            epics.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public Task addTask(Task task) {
        int idTask = getIdTask();
        task.setId(idTask);
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            addTaskWihtOverlap(task);
        }
        return task;
    }

    @Override
    public Epic addEpic(Epic epic) {
        int idEpic = getIdTask();
        epic.setId(idEpic);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        int idSubtask = getIdTask();
        subtask.setId(idSubtask);
        Epic epic = epics.get(subtask.getIdEpic());
        if (epic == null) {
            System.out.println("The epic " + subtask.getIdEpic() + " was not added to the history.");
            return null;
        }
        epic.addSubtask(subtask);
        subTasks.put(idSubtask, subtask);
        if (subtask.getStartTime() != null) {
            addTaskWihtOverlap(subtask);
            setTimeForEpic(epic);
        }
        return subtask;
    }

    @Override
    public Task updateTask(Task task) {
        int idTask = task.getId();
        if (tasks.containsKey(idTask)) {
            tasks.put(idTask, task);
            return task;
        }
        return null;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Integer epicID = epic.getId();
        if (epics.containsKey(epicID)) {
            Epic currentEpic = epics.get(epicID);
            ArrayList<Subtask> currentEpicSubtaskList = currentEpic.getSubtasksList();
            if (!currentEpicSubtaskList.isEmpty()) {
                for (Subtask subtask : currentEpicSubtaskList) {
                    subTasks.remove(subtask.getId());
                }
            }
            epics.replace(epicID, epic);

            ArrayList<Subtask> newEpicSubtaskList = epic.getSubtasksList();
            if (!newEpicSubtaskList.isEmpty()) {
                for (Subtask subtask : newEpicSubtaskList) {
                    subTasks.put(subtask.getId(), subtask);
                }
            }
            updateEpicStatus(epic);
            setTimeForEpic(epic);
            return epic;
        }
        return null;

    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        int idSubtask = subtask.getId();
        if (subTasks.containsKey(idSubtask)) {
            int epicID = subtask.getIdEpic();
            Subtask oldSubtask = subTasks.get(idSubtask);
            subTasks.replace(idSubtask, subtask);
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return sortTasks.stream().toList();
    }

    private void updateEpicStatus(Epic epic) {
        int countNew = 0;
        int countDONE = 0;
        ArrayList<Subtask> subtasksList = epic.getSubtasksList();

        for (Subtask subtask : subtasksList) {
            if (subtask.getStatus() == TaskStatus.DONE) {
                countDONE++;
            }
            if (subtask.getStatus() == TaskStatus.NEW) {
                countNew++;
            }
        }
        if (countDONE == subtasksList.size()) {
            epic.setStatus(TaskStatus.DONE);
        } else if (countNew != subtasksList.size()) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    private int getIdTask() {
        return idTask++;
    }

    private boolean isOverlap(Task a, Task b) {
        LocalDateTime aStart = a.getStartTime();
        LocalDateTime aEnd = a.getEndTime();
        LocalDateTime bStart = b.getStartTime();
        LocalDateTime bEnd = b.getEndTime();
        if (a == null || b == null) {
            return false;
        }

        return aStart.isBefore(bEnd) && bStart.isBefore(aEnd);
    }

    private void addTaskWihtOverlap(Task newTask) {
        sortTasks.stream()
                .filter(task -> isOverlap(newTask, task))
                .findFirst()
                .ifPresentOrElse(
                        overlappedTask -> {
                            if (newTask instanceof Subtask) {
                                throw new IllegalArgumentException("Подзадача пересекается с существующей подзадачей.");
                            } else {
                                throw new IllegalArgumentException("Задача пересекается с существующей задачей.");
                            }
                        },
                        () -> sortTasks.add(newTask)
                );
    }

    private void setTimeForEpic(Epic epic) {
        if (!epic.getSubtasksList().isEmpty()) {
            LocalDateTime startTime = LocalDateTime.MAX;
            LocalDateTime endTime = LocalDateTime.MIN;
            for (Subtask subtask : epic.getSubtasksList()) {
                if (subtask.getStartTime().isBefore(startTime)) {
                    startTime = subtask.getStartTime();
                }
                if (subtask.getEndTime().isAfter(endTime)) {
                    endTime = subtask.getEndTime();
                }
                epic.setStartTime(startTime);
                epic.setEndTime(endTime);
                epic.setDuration(Duration.between(startTime, endTime));
            }
        }
    }
}
