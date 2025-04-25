package managers.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler {
    TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void getRequest(HttpExchange httpExchange) throws IOException {
        try {
            sendJson(httpExchange, taskManager.getPrioritizedTasks());
        } catch (Exception e) {
            sendStatus(httpExchange, 200);
        }
    }
}
