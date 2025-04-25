package managers;

import enums.TaskStatus;
import enums.TaskType;
import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static managers.Managers.getFileBackedTaskManager;
import static tasks.Task.dateTimeFormatter;

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

        Task task1 = new Task("Задача 1", "Купить картошку 5 мешков");
        task1.setDuration(Duration.ofMinutes(5));
        task1.setStartTime(LocalDateTime.now().plusHours(1));
        manager.addTask(task1);
        Task task2 = new Task("Задача 2", "Купить бананов2 кг");
        task2.setDuration(Duration.ofMinutes(5));
        task2.setStartTime(LocalDateTime.now());
        manager.addTask(task2);
        Epic epic1 = manager.addEpic(new Epic("Эпик 1", "1"));
//        Epic epic2 = manager.addEpic(new Epic("Эпик 2", "2"));
        Subtask subtask1 = new Subtask("Повесить полку", "В прихожей", epic1.getId());
        subtask1.setDuration(Duration.ofMinutes(5));
        subtask1.setStartTime(LocalDateTime.now().plusHours(2));
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Повесить полку", "На кухне", epic1.getId());
        subtask2.setDuration(Duration.ofMinutes(5));
        subtask2.setStartTime(LocalDateTime.now().plusMinutes(15));
        manager.addSubtask(subtask2);

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
        String epicId = "";
        if (task instanceof Subtask) {
            epicId = String.valueOf(((Subtask) task).getIdEpic());
        }

        String start = "";
        if (task.getStartTime() != null) {
            start = task.getStartTime().format(dateTimeFormatter);
        }

        long durationLong = 0;
        if (task.getDuration() != null) {
            durationLong = task.getDuration().toMinutes();
        }
        if (task instanceof Subtask) {
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s", task.getId(), task.getType(), task.getName(), task.getStatus(), task.getDescription(),start,durationLong, epicId);
        } else {
            return String.format("%s,%s,%s,%s,%s,%s,%s", task.getId(), task.getType(), task.getName(), task.getStatus(), task.getDescription(),start,durationLong);
        }
    }

    private static Task fromString(String value) {
        String[] split = value.split(",");

        int id = Integer.parseInt(split[0]);
        TaskType type = TaskType.valueOf(split[1]);
        String name = split[2];
        TaskStatus status = TaskStatus.valueOf(split[3]);
        String description = split[4];
        LocalDateTime startTime = LocalDateTime.parse(split[5], dateTimeFormatter);
        int durationMinutes = Integer.valueOf(split[6]);
        switch (type) {
            case TASK:
                Task task = new Task(id, name, description, status);
                task.setStartTime(startTime);
                task.setDuration(Duration.ofMinutes(durationMinutes));
                return task;
            case SUBTASK:
                int epicId = Integer.parseInt(split[7]);
                Subtask subtask = new Subtask(id, name, description, status, epicId);
                subtask.setStartTime(startTime);
                subtask.setDuration(Duration.ofMinutes(durationMinutes));
                return subtask;
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

            bufferedWriter.write("id,type,name,status,description,startTime,duration,epic");
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
