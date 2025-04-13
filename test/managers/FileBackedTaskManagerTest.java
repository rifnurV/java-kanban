package managers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("test", "csv");
        taskManager = taskManager.loadFromFile(tempFile);
    }

    @AfterEach
    void tearDown() {
        tempFile.delete();
    }

    @Test
    void saveAndLoadEmptyManager() {

        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(tempFile);
        assertTrue(manager.getTasks().isEmpty(), "The task was not added to the history.");
        assertTrue(manager.getHistory().isEmpty(), "The task was not added to the history.");
        assertTrue(manager.getEpic().isEmpty(), "The epic was not added to the history.");
        assertTrue(manager.getSubtask().isEmpty(), "The subtask was not added to the history.");
    }

    @Test
    void saveAndLoadTasks() {
        Task task1 = new Task("Задача", "Описание задачи");
        taskManager.addTask(task1);
        Epic epic1 = new Epic("Эпик", "Описание  эпика");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача", "Описание подзадачи", epic1.getId());
        taskManager.addSubtask(subtask1);

        FileBackedTaskManager loadManager = FileBackedTaskManager.loadFromFile(tempFile);

        List<Task> tasks = loadManager.getTasks();
        List<Epic> epics = loadManager.getEpic();
        List<Subtask> subtasks = loadManager.getSubtask();

        assertEquals(1, tasks.size(), "The task was not added to the history.");
        assertEquals("Задача", tasks.get(0).getName(), "The task was not added to the history.");

        assertEquals(1, epics.size(), "The epic was not added to the history.");
        assertEquals("Эпик", epics.get(0).getName(), "The epic was not added to the history.");

        assertEquals(1, subtasks.size(), "The subtask was not added to the history.");
        assertEquals(epic1.getId(), subtasks.get(0).getIdEpic(), "The epic was not added to the history.");
    }

}
