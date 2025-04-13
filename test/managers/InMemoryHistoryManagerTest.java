package managers;

import enums.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

public class InMemoryHistoryManagerTest {

    private TaskManager manager;
    private HistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void shouldAddTaskToHistory() {
        Task task = new Task("Task 1", "Description 1");
        historyManager.add(task);
        Assertions.assertEquals(1, historyManager.getHistory().size(),"The task was not added to the history.");
        Assertions.assertEquals(task, historyManager.getHistory().get(0),"The task was not added to the history.");
        Assertions.assertNotNull(historyManager.getHistory().get(0),"The task was not added to the history.");
    }

    @Test
    void shouldAddTasksToHistory() {
        for (int i = 0; i < 5 ; i++) {
            Task task = new Task(i,"Task "+i, "Description "+i, TaskStatus.NEW);
            historyManager.add(task);
        }
        Assertions.assertEquals(5, historyManager.getHistory().size(),"The task was not added to the history.");
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        Task task1 = new Task("Task 1", "Description 1");
        historyManager.add(task1);
        historyManager.remove(task1.getId());
        Assertions.assertEquals(0, historyManager.getHistory().size(),"The task was not removed from the history.");
    }

    @Test
    void shouldRemoveTasksFromHistory() {
        Task task1 = new Task(1,"Task 1", "Description 1",TaskStatus.NEW);
        Task task2 = new Task(2, "Task 2", "Description 2",TaskStatus.NEW);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());
        Assertions.assertEquals(1, historyManager.getHistory().size(),"The task was not removed from the history.");
    }

    @Test
    void shouldShowTasksFromHistory() {
        Task task1 = new Task(1,"Task 1", "Description 1",TaskStatus.NEW);
        Task task2 = new Task(2, "Task 2", "Description 2",TaskStatus.NEW);

        historyManager.add(task1);
        historyManager.add(task2);
        Assertions.assertEquals(2, historyManager.getHistory().size(),"The task was not added to the history.");
    }
}
