package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    private TaskManager taskManager;
    private Task task1;
    private Task task2;


    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
        task1 = new Task("Задача 1", "Описание задачи 1");
        task2 = new Task("Задача 2", "Описание задачи 2");
    }

    @Test
    void shouldNewTaskAdd(){
        taskManager.addTask(task1);
        assertNotNull(taskManager,"Задача не должна быть пустой");
    }


    @Test
    void shouldAddSubtask(){
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        Assertions.assertNotEquals(task1.getId(), task2.getId(),"Задачи должны быть разными");
    }

    @Test
    void shouldSizeAllTasks(){
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(2, taskManager.getTasks().size(),"Размер списка должено быть равно 2");
    }

    @Test
    void shouldDeleteSubtask(){
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.deleteTaskById(task1.getId());
        assertEquals(1, taskManager.getTasks().size(),"The size of the list does not match after deletion");
    }

    @Test
    void shouldDeleteSubtasks(){
        Epic epic1 = taskManager.addEpic(new Epic("Эпик 1", "1"));
        Subtask subtask1 = taskManager.addSubtask(new Subtask("Повесить полку", "В прихожей", epic1.getId()));
        Subtask subtask2 = taskManager.addSubtask(new Subtask("Повесить полку", "На кухне", epic1.getId()));
        taskManager.getSubtask(subtask1.getId());
        taskManager.getSubtask(subtask2.getId());

        taskManager.deleteSubtaskById(subtask1.getId());
        assertEquals(1, taskManager.getSubtask().size(),"Sub task was incorrectly deleted");
    }

    @Test
    void shouldDeleteEpics(){
        Epic epic1 = taskManager.addEpic(new Epic("Эпик 1", "1"));
        Subtask subtask1 = taskManager.addSubtask(new Subtask("Повесить полку", "В прихожей", epic1.getId()));
        Subtask subtask2 = taskManager.addSubtask(new Subtask("Повесить полку", "На кухне", epic1.getId()));
        taskManager.getSubtask(subtask1.getId());
        taskManager.getSubtask(subtask2.getId());
        taskManager.deleteEpicById(epic1.getId());

        assertEquals(0, taskManager.getSubtask().size(),"Not all epics have been deleted");

    }

}

