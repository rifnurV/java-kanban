package managers;

import enums.TaskStatus;
import enums.TaskType;
import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static managers.Managers.getFileBackedTaskManager;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public void deleteTask() {
        super.deleteTask();
        save();
    }

    @Override
    public void deleteSubtask() {
        super.deleteSubtask();
        save();
    }

    @Override
    public void deleteEpic() {
        super.deleteEpic();
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public Task addTask(Task task) {
        Task newTask = super.addTask(task);
        save();
        return newTask;
    }

    @Override
    public Epic addEpic(Epic epic) {
        Epic newEpic = super.addEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        Subtask newSubtask = super.addSubtask(subtask);
        save();
        return newSubtask;
    }

    @Override
    public Task updateTask(Task task) {
        Task newTask = super.updateTask(task);
        save();
        return newTask;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        Subtask newSubtask = super.updateSubtask(subtask);
        save();
        return newSubtask;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic newEpic = super.updateEpic(epic);
        save();
        return newEpic;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        String content = null;
        int maxId = 0;
        try {
            content = Files.readString(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла" + e.getMessage());
        }

        if (content.isEmpty()) {
            return manager;
        }

        String[] lines = content.split("\n");

        if (lines.length <= 1) {
            return manager;
        }

        for (int i = 1; i < lines.length; i++) {

            String line = lines[i].trim();
            if (line.isEmpty()) {
                continue;
            }

            Task task = fromString(line);

            if (task.getId() > maxId) {
                maxId = task.getId();
            }

            if (task instanceof Epic) {
                manager.addEpic((Epic) task);
            } else if (task instanceof Subtask) {
                manager.addSubtask((Subtask) task);
            } else if (task instanceof Task) {
                manager.addTask(task);
            } else {
                throw new ManagerSaveException("Invalid task type");
            }
        }
        manager.setMaxId(maxId);
        return manager;
    }

    public void setMaxId(int maxId) {
        setIdTask(maxId);
    }

    public static void main(String[] args) {

        File file = new File("resources/tasks.csv");

        TaskManager manager = getFileBackedTaskManager(file);

        Task task1 = manager.addTask(new Task("Задача 1", "Купить картошку 5 мешков"));
        Task task2 = manager.addTask(new Task("Задача 2", "Купить бананов2 кг"));
        Epic epic1 = manager.addEpic(new Epic("Эпик 1", "1"));
        Epic epic2 = manager.addEpic(new Epic("Эпик 2", "2"));
        Subtask subtask1 = manager.addSubtask(new Subtask("Повесить полку", "В прихожей", epic1.getId()));
        Subtask subtask2 = manager.addSubtask(new Subtask("Повесить полку", "На кухне", epic1.getId()));

        printAllTasks(manager);
    }

    public static void printAllTasks(TaskManager manager) {
        System.out.println("Tasks:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Epics:");
        for (Task epic : manager.getEpic()) {
            System.out.println(epic);

            for (Task task : manager.getSubtasksByEpikId(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Subtasks:");
        for (Task subtask : manager.getSubtask()) {
            System.out.println(subtask);
        }
    }

    private String toString(Task task) {
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            return String.format("%s,%s,%s,%s,%s,%s", task.getId(), task.getType(), task.getName(), task.getStatus(), task.getDescription(), subtask.getIdEpic());
        } else {
            return String.format("%s,%s,%s,%s,%s", task.getId(), task.getType(), task.getName(), task.getStatus(), task.getDescription());
        }
    }

    private static Task fromString(String value) {
        String[] split = value.split(",");

        int id = Integer.parseInt(split[0]);
        TaskType type = TaskType.valueOf(split[1]);
        String name = split[2];
        TaskStatus status = TaskStatus.valueOf(split[3]);
        String description = split[4];
        switch (type) {
            case TASK:
                return new Task(id, name, description, status);
            case SUBTASK:
                int epicId = Integer.parseInt(split[5]);
                return new Subtask(id, name, description, status, epicId);
            case EPIC:
                return new Epic(id, name, description, status);
            default:
                throw new ManagerSaveException("Invalid task type");
        }
    }

    private void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {

            List<Task> tasks = getTasks();
            List<Subtask> subtasks = getSubtask();
            List<Epic> epics = getEpic();

            bufferedWriter.write("id,type,name,status,description,epic");
            bufferedWriter.newLine();

            for (Task task : tasks) {
                bufferedWriter.write(toString(task));
                bufferedWriter.newLine();
            }
            for (Epic epic : epics) {
                bufferedWriter.write(toString(epic));
                bufferedWriter.newLine();
            }
            for (Subtask subtask : subtasks) {
                bufferedWriter.write(toString(subtask));
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Error saving to a file" + e.getMessage());
        }
    }
}
