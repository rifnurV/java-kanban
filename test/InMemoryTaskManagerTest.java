import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
        assertNotEquals(task1.getId(), task2.getId(),"Задачи должны быть разными");
    }

    @Test
    void shouldSizeAllTasks(){
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(2, taskManager.getTasks().size(),"Размер списка должено быть равно 2");


    }


}

