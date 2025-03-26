package managers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {
    private FileBackedTaskManager manager;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("test", "csv");
        manager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    void tearDown() throws IOException {
        tempFile.delete();
    }

    @Test
    void saveAndLoadEmptyManager() {
        manager.save();

        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);
        assertTrue(manager.getTasks().isEmpty(), "The task was not added to the history.");
        assertTrue(manager.getHistory().isEmpty(), "The task was not added to the history.");
        assertTrue(manager.getEpic().isEmpty(), "The epic was not added to the history.");
        assertTrue(manager.getSubtask().isEmpty(), "The subtask was not added to the history.");
    }

    @Test
    void saveAndLoadTasks() {
        Task task1 = new Task("Task 1", "Description 1");
        Epic epic1 = new Epic("Epic 1", "Description 1");
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic1.getId());

        manager.addTask(task1);
        manager.addTask(epic1);
        manager.addTask(subtask1);

        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

        List<Task> tasks = manager.getTasks();
        List<Epic> epics = manager.getEpic();
        List<Subtask> subtasks = manager.getSubtask();

        assertEquals(1, tasks.size(), "The task was not added to the history.");
        assertEquals("Task 1", tasks.get(0).getName(), "The task was not added to the history.");

        assertEquals(1, epics.size(), "The epic was not added to the history.");
        assertEquals("Epic 1", epics.get(0).getName(), "The epic was not added to the history.");

        assertEquals(1, subtasks.size(), "The subtask was not added to the history.");
        assertEquals(epic1.getId(), subtasks.get(0).getId(), "The epic was not added to the history.");
    }

}
