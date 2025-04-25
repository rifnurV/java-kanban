package managers.http;

import com.sun.net.httpserver.HttpServer;
import enums.TaskStatus;
import managers.*;
import managers.http.handlers.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer server;
    private TaskManager taskManager;

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();

        LocalDateTime nowTime = LocalDateTime.now();
        Duration oneHour = Duration.ofHours(1);
        Task task1 = new Task("Задача 1", "Купить картошку 5 мешков");
        task1.setDuration(oneHour);
        task1.setStartTime(nowTime);
        taskManager.addTask(task1);
        Task task2 = new Task("Задача 2", "Купить бананов2 кг");
        task2.setDuration(oneHour);
        task2.setStartTime(nowTime.plusHours(2));
        task2.setDescription("Купить апельсинов10 кг");
        taskManager.addTask(task2);
        task2.setStatus(TaskStatus.IN_PROGRESS);
        Epic epic1 = taskManager.addEpic(new Epic("Эпик 1", "1"));

        Subtask subtask1 = new Subtask("Повесить полку", "В прихожей", epic1.getId());
        subtask1.setDuration(oneHour);
        subtask1.setStartTime(nowTime.plusHours(3));
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Повесить полку", "На кухне", epic1.getId());
        subtask2.setDuration(oneHour);
        subtask2.setStartTime(nowTime.plusHours(4));
        taskManager.addSubtask(subtask2);

        HistoryManager historyManager = Managers.getDefaultHistory();
        taskManager.setHistoryManager(historyManager);
        HttpTaskServer taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
    }

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/subtasks", new SubtasksHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        server.stop(0);
        System.out.println("HTTP-сервер остановлен");
    }


}
