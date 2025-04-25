package managers.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import managers.http.typetoken.TaskTypeToken;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TasksHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void getRequest(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().split("/");
        if (path.length > 2) {
            try {
                int id = Integer.parseInt(path[2]);
                Task task = taskManager.getTaskById(id);
                sendJson(exchange, task);
            } catch (IOException e) {
                sendNotFound(exchange);
            }
        } else {
            List<Task> tasks = taskManager.getTasks();
            if (tasks.size() > 0) {
                sendJson(exchange, tasks);
            } else {
                sendNotFound(exchange);
            }
        }


    }

    public void postRequest(HttpExchange exchange) throws IOException {
        try {
            InputStream is = exchange.getRequestBody();
            String[] path = exchange.getRequestURI().getPath().split("/");
            String boby = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Task taskHttp = gson.fromJson(boby, new TaskTypeToken().getType());

            if (path.length > 2) {
                int id = Integer.parseInt(path[2]);
                Task taskUpdate = taskManager.getTaskById(id);
                if (taskUpdate != null) {
                    taskManager.updateTask(taskHttp);
                    sendCreated(exchange);
                } else {
                    sendNotFound(exchange);
                }
            } else {
                taskManager.addTask(taskHttp);
                sendCreated(exchange);
            }
        } catch (IllegalArgumentException e) {
            sendHasInteractions(exchange);
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    public void deleteRequest(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().split("/");
        if (path.length > 2) {
            try {
                int id = Integer.parseInt(path[2]);
                Task task = taskManager.getTaskById(id);
                if (task != null) {
                    taskManager.deleteTaskById(id);
                    sendStatus(exchange, 200);
                } else {
                    sendNotFound(exchange);
                }

            } catch (IOException e) {
                sendNotFound(exchange);
            }
        }
    }

}
