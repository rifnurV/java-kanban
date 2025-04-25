package managers.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import managers.http.typetoken.SubTaskTypeToken;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public SubtasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void getRequest(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().split("/");
        if (path.length > 2) {
            int taskId = Integer.parseInt(path[2]);
            Subtask subtask = taskManager.getSubtask(taskId);
            if (subtask != null) {
                sendJson(exchange, subtask);
            } else {
                sendNotFound(exchange);
            }
        } else {
            List<Subtask> subtasks = taskManager.getSubtask();
            if (subtasks != null) {
                sendJson(exchange, subtasks);
            } else {
                sendNotFound(exchange);
            }
        }
    }

    public void postRequest(HttpExchange exchange) throws IOException {
        try {
            String[] path = exchange.getRequestURI().getPath().split("/");
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Subtask inSubtask = gson.fromJson(body, new SubTaskTypeToken().getType());

            if (path.length > 2) {
                int subTaskId = Integer.parseInt(path[2]);
                Subtask subtask = taskManager.getSubtask(subTaskId);
                if (subtask != null) {
                    taskManager.updateSubtask(inSubtask);
                    sendStatus(exchange, 201);
                } else {
                    sendStatus(exchange, 404);
                }
            } else {
                taskManager.addSubtask(inSubtask);
                sendStatus(exchange, 201);
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
            int subTaskId = Integer.parseInt(path[2]);
            Subtask subtask = taskManager.getSubtask(subTaskId);
            if (subtask != null) {
                taskManager.deleteSubtaskById(subTaskId);
                sendStatus(exchange, 200);
            } else {
                sendNotFound(exchange);
            }
        }
    }
}
