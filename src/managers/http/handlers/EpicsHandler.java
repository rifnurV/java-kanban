package managers.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import managers.http.typetoken.EpicTypeToken;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public EpicsHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void getRequest(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().split("/");
        if (path.length > 3) {
            try {
                int id = Integer.parseInt(path[2]);
                List<Subtask> epics = taskManager.getSubtasksByEpikId(id);
                sendJson(exchange, epics);
            } catch (Exception e) {
                sendNotFound(exchange);
            }
        } else if (path.length > 2) {
            try {
                int id = Integer.parseInt(path[2]);
                Epic epic = taskManager.getEpic(id);
                sendJson(exchange, epic);
            } catch (Exception e) {
                sendNotFound(exchange);
            }
        } else {
            List<Epic> epics = taskManager.getEpic();
            if (epics != null) {
                sendJson(exchange, epics);
            } else {
                sendNotFound(exchange);
            }
        }

    }

    public void postRequest(HttpExchange exchange) throws IOException {
        try {
            String[] path = exchange.getRequestURI().getPath().split("/");
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Epic newEpic = gson.fromJson(body, new EpicTypeToken().getType());

            if (path.length > 2) {
                int id = Integer.parseInt(path[2]);
                Epic oldEpic = taskManager.getEpic(id);
                if (oldEpic != null) {
                    taskManager.updateEpic(newEpic);
                    sendCreated(exchange);
                } else {
                    sendNotFound(exchange);
                }
            } else {
                taskManager.addEpic(newEpic);
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
                taskManager.deleteEpicById(id);
                sendSuccess(exchange);
            } catch (Exception e) {
                sendNotFound(exchange);
            }
        }
    }
}
