package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected HistoryManager historyManager;

    protected abstract T createTaskManager();

    @BeforeEach
    void setUp() throws IOException {
        taskManager = createTaskManager();
    }

    @Test
    void addTask() {
        Task task = new Task("Задача", "Описание задачи");
        taskManager.addTask(task);
        Assertions.assertEquals(1, taskManager.getTasks().size());
    }

    @Test
    void addSubtask() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subTask = new Subtask("Подзадача", "Описание подзадачи", epic.getId());
        subTask.setStartTime(LocalDateTime.now());
        subTask.setDuration(Duration.ofMinutes(5));
        taskManager.addSubtask(subTask);
        Assertions.assertEquals(1, taskManager.getSubtask().size());
    }

    @Test
    void addEpic() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager.addEpic(epic);
        Assertions.assertEquals(1, taskManager.getEpic().size());
    }

    @Test
    void getTasks() {
        Task task = new Task("Задача", "Описание задачи");
        taskManager.addTask(task);
        Assertions.assertEquals(1, taskManager.getTasks().size());
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        taskManager.addTask(task2);
        Assertions.assertEquals(2, taskManager.getTasks().size());
    }

    @Test
    void getSubtask() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subTask = new Subtask("Подзадача", "Описание подзадачи", epic.getId());
        subTask.setStartTime(LocalDateTime.now());
        subTask.setDuration(Duration.ofMinutes(5));
        taskManager.addSubtask(subTask);
        Assertions.assertEquals(1, taskManager.getSubtask().size());
        Subtask subTask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epic.getId());
        subTask2.setStartTime(LocalDateTime.now().plusMinutes(15));
        subTask2.setDuration(Duration.ofMinutes(5));
        taskManager.addSubtask(subTask2);
        Assertions.assertEquals(2, taskManager.getSubtask().size());
    }

    @Test
    void getEpics() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager.addEpic(epic);
        Assertions.assertEquals(1, taskManager.getEpic().size());
        taskManager.addEpic(epic);
        Assertions.assertEquals(2, taskManager.getEpic().size());
    }

    @Test
    void getTaskById() {
        Task task = new Task("Задача", "Описание задачи");
        taskManager.addTask(task);
        Assertions.assertEquals(task, taskManager.getTaskById(task.getId()));
    }

    @Test
    void getSubtaskById() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subTask = new Subtask("Подзадача", "Описание подзадачи", epic.getId());
        subTask.setStartTime(LocalDateTime.now());
        subTask.setDuration(Duration.ofMinutes(5));
        taskManager.addSubtask(subTask);
        Assertions.assertEquals(subTask, taskManager.getSubtask(subTask.getId()));
    }

    @Test
    void getEpicById() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager.addEpic(epic);
        Assertions.assertEquals(epic, taskManager.getEpic(epic.getId()));
    }

    @Test
    void deleteTasks() {
        Task task = new Task("Задача", "Описание задачи");
        taskManager.addTask(task);
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        taskManager.addTask(task2);
        Assertions.assertEquals(2, taskManager.getTasks().size());
        taskManager.deleteTask();
        Assertions.assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void deleteSubtasks() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subTask = new Subtask("Подзадача", "Описание подзадачи", epic.getId());
        subTask.setStartTime(LocalDateTime.now());
        subTask.setDuration(Duration.ofMinutes(5));
        taskManager.addSubtask(subTask);
        Subtask subTask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epic.getId());
        subTask2.setStartTime(LocalDateTime.now().plusMinutes(10));
        subTask2.setDuration(Duration.ofMinutes(5));
        taskManager.addSubtask(subTask2);
        Assertions.assertEquals(2, taskManager.getSubtask().size());
        taskManager.deleteSubtask();
        Assertions.assertEquals(0, taskManager.getSubtask().size());
    }

    @Test
    void deleteEpics() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager.addEpic(epic);
        taskManager.addEpic(epic);
        Assertions.assertEquals(2, taskManager.getEpic().size());
        taskManager.deleteEpic();
        Assertions.assertEquals(0, taskManager.getEpic().size());
    }

    @Test
    void deleteTaskByID() {
        Task task = new Task("Задача", "Описание задачи");
        taskManager.addTask(task);
        Assertions.assertEquals(1, taskManager.getTasks().size());
        taskManager.deleteTaskById(task.getId());
        Assertions.assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void deleteEpicById() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager.addEpic(epic);
        Assertions.assertEquals(1, taskManager.getEpic().size());
        taskManager.deleteEpicById(epic.getId());
        Assertions.assertTrue(taskManager.getEpic().isEmpty());
    }

    @Test
    void updateTask() {
        Task task = new Task("Задача", "Описание задачи");
        taskManager.addTask(task);
        Assertions.assertEquals(1, taskManager.getTasks().size());
        task.setDescription("описание изменили");
        taskManager.updateTask(task);
        Assertions.assertEquals(task, taskManager.getTaskById(task.getId()));
    }

    @Test
    void updateSubtask() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subTask = new Subtask("Подзадача", "Описание подзадачи", epic.getId());
        subTask.setStartTime(LocalDateTime.now());
        subTask.setDuration(Duration.ofMinutes(5));
        taskManager.addSubtask(subTask);
        Assertions.assertEquals(1, taskManager.getSubtask().size());
        subTask.setDescription("описание подзадачи изменили");
        taskManager.updateSubtask(subTask);
        Assertions.assertEquals(subTask, taskManager.getSubtask(subTask.getId()));
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager.addEpic(epic);
        Assertions.assertEquals(1, taskManager.getEpic().size());
        epic.setDescription("описание эпика изменили");
        taskManager.updateEpic(epic);
        Assertions.assertEquals(epic, taskManager.getEpic(epic.getId()));
    }
}
