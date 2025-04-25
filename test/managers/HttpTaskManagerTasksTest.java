package managers;

import com.google.gson.Gson;
import enums.TaskStatus;
import managers.http.EpicListTypeToken;
import managers.http.HttpTaskServer;
import managers.http.SubTaskListTypeToken;
import managers.http.TaskListTypeToken;
import managers.http.handlers.BaseHttpHandler;
import managers.http.typetoken.SubTaskTypeToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTest {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = BaseHttpHandler.getGson();

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteTask();
        manager.deleteSubtask();
        manager.deleteEpic();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testGetTask() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "Описание задачи 1",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        Task task2 = new Task("Задача 2", "Описание задачи 2",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(5));

        manager.addTask(task1);
        manager.addTask(task2);

        int id = manager.getTasks().getFirst().getId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        List<Task> parsed = gson.fromJson(response.body(), new TaskListTypeToken().getType());

        assertTrue(parsed.size() > 0, "Task should have been created");
        assertTrue(parsed.get(0).getId() == id, "Task should have been created");
        assertTrue(parsed.get(0).equals(task1), "Task should have been created");
        assertTrue(parsed.size() == 2, "Task should have been created");

    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());

        manager.addTask(task);

        int id = manager.getTasks().getFirst().getId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        Task parsed = gson.fromJson(response.body(), Task.class);

        assertEquals(task.getId(), parsed.getId(), "Task id mismatch");
        assertEquals(task.getName(), parsed.getName(), "Task name mismatch");
        assertEquals(task.getDescription(), parsed.getDescription(), "Task description mismatch");
        assertEquals(task.getStatus(), parsed.getStatus(), "Task status mismatch");
        assertTrue(parsed.equals(task), "Task object mismatch");
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Задача 1", "Описание задачи 1",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getTasks();
//        List<Task> parsed = gson.fromJson(response.body(), new TaskListTypeToken().getType());

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Задача 1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testDeleteByIdTask() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "Описание задачи 1",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        Task task2 = new Task("Задача 2", "Описание задачи 2",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(5));

        manager.addTask(task1);
        manager.addTask(task2);

        int id = manager.getTasks().getFirst().getId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Task should have been deleted");

        List<Task> tasksFromManager = manager.getTasks();
        assertTrue(tasksFromManager.size() == 1, "Task should have been deleted");
        assertTrue(tasksFromManager.get(0).equals(task2), "Task should have been deleted");
    }

    @Test
    public void testGetSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание эпик 1");
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        subtask1.setDuration(Duration.ofMinutes(5));
        subtask1.setStartTime(LocalDateTime.now());

        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epic.getId());
        subtask2.setDuration(Duration.ofMinutes(5));
        subtask2.setStartTime(LocalDateTime.now().plusMinutes(5));

        manager.addTask(subtask1);
        manager.addTask(subtask2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        List<Subtask> parsed = gson.fromJson(response.body(), new SubTaskListTypeToken().getType());

        assertTrue(parsed.size() > 0, "Task should have been created");
        assertTrue(parsed.size() == 2, "Task should have been created");
    }

    @Test
    public void testGetSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание эпик 1");
        manager.addEpic(epic);
        Subtask sutask = new Subtask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        sutask.setDuration(Duration.ofMinutes(5));
        sutask.setStartTime(LocalDateTime.now());
        manager.addSubtask(sutask);

        int id = manager.getSubtask().getFirst().getId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        Task parsed = gson.fromJson(response.body(), new SubTaskTypeToken().getType());

        assertEquals(sutask.getId(), parsed.getId(), "Task id mismatch");
        assertEquals(sutask.getName(), parsed.getName(), "Task name mismatch");
        assertEquals(sutask.getDescription(), parsed.getDescription(), "Task description mismatch");
        assertEquals(sutask.getStatus(), parsed.getStatus(), "Task status mismatch");
        assertTrue(parsed.equals(sutask), "Task object mismatch");
    }

    @Test
    public void testDeleteByIdSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание эпик 1");
        manager.addEpic(epic);
        Subtask sutask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        sutask1.setDuration(Duration.ofMinutes(5));
        sutask1.setStartTime(LocalDateTime.now());
        Subtask sutask2 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        sutask2.setDuration(Duration.ofMinutes(5));
        sutask2.setStartTime(LocalDateTime.now().plusMinutes(5));

        manager.addSubtask(sutask1);
        manager.addSubtask(sutask2);

        int id = manager.getSubtask().getFirst().getId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Task should have been deleted");

        List<Subtask> tasksFromManager = manager.getSubtask();
        assertTrue(tasksFromManager.size() == 1, "Task should have been deleted");
        assertTrue(tasksFromManager.get(0).equals(sutask2), "Task should have been deleted");
    }

    @Test
    public void testGetEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик 1", "Описание эпик 1");
        epic1.setStartTime(LocalDateTime.now());
        epic1.setDuration(Duration.ofMinutes(5));
        epic1.setEndTime(LocalDateTime.now().plusMinutes(5));
        Epic epic2 = new Epic("Эпик 2", "Описание эпик 2");
        epic2.setStartTime(LocalDateTime.now());
        epic2.setDuration(Duration.ofMinutes(5));
        epic2.setEndTime(LocalDateTime.now().plusMinutes(5));
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Accept", "application/json").GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        List<Epic> parsed = gson.fromJson(response.body(), new EpicListTypeToken().getType());

        assertTrue(parsed.size() > 0, "Task should have been created");
        assertTrue(parsed.size() == 2, "Task should have been created");
    }

    @Test
    public void testDeleteEpicById() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик 1", "Описание эпик 1");
        epic1.setStartTime(LocalDateTime.now());
        epic1.setDuration(Duration.ofMinutes(5));
        epic1.setEndTime(LocalDateTime.now().plusMinutes(5));
        Epic epic2 = new Epic("Эпик 2", "Описание эпик 2");
        epic2.setStartTime(LocalDateTime.now());
        epic2.setDuration(Duration.ofMinutes(5));
        epic2.setEndTime(LocalDateTime.now().plusMinutes(5));
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        int id = manager.getEpic().getFirst().getId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Epic> parsed = manager.getEpic();

        assertTrue(parsed.size() > 0, "Epic should have been created");
        assertTrue(parsed.size() == 1, "Epic should be 1");
        assertTrue(parsed.get(0).getName().equals("Эпик 2"), "Name mismatch");
        assertTrue(parsed.get(0).equals(epic2), "Epics mismatch");
    }


}