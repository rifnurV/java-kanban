package managers.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void getRequest(HttpExchange httpExchange) throws IOException {
        try {
            List<Task> tasks = taskManager.getHistoryManager().getHistory();
            sendJson(httpExchange, tasks);
        } catch (Exception e) {
            sendSuccess(httpExchange);
        }
    }
}
