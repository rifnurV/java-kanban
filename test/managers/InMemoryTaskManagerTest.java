package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
        historyManager = Managers.getDefaultHistory();
        taskManager.setHistoryManager(historyManager);
    }

    @Test
    void testEpicTimeCalculation() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Desc", epic.getId());
        subtask1.setStartTime(LocalDateTime.of(2023, 1, 1, 10, 0));
        subtask1.setDuration(Duration.ofMinutes(60));
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Desc", epic.getId());
        subtask2.setStartTime(LocalDateTime.of(2023, 1, 1, 11, 0));
        subtask2.setDuration(Duration.ofMinutes(30));
        taskManager.addSubtask(subtask2);

        assertEquals(Duration.ofMinutes(90), epic.getDuration());
        assertEquals(LocalDateTime.of(2023, 1, 1, 10, 0), epic.getStartTime());
        assertEquals(LocalDateTime.of(2023, 1, 1, 11, 30), epic.getEndTime());
    }

    @Test
    void shouldDeleteEpics() {
        Epic epic1 = taskManager.addEpic(new Epic("Эпик 1", "1"));
        Subtask subtask1 = taskManager.addSubtask(new Subtask("Повесить полку", "В прихожей", epic1.getId()));
        Subtask subtask2 = taskManager.addSubtask(new Subtask("Повесить полку", "На кухне", epic1.getId()));
        taskManager.getSubtask(subtask1.getId());
        taskManager.getSubtask(subtask2.getId());
        taskManager.deleteEpicById(epic1.getId());

        assertEquals(0, taskManager.getSubtask().size(), "Not all epics have been deleted");

    }

}

