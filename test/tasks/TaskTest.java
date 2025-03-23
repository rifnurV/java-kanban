package tasks;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    private Task task1;
    private Task task2;

    @Test
    public void shouldTrueTaskId(){
        task1 = new Subtask("Подзадача 1", "Описание подзадачи 1",1);
        task2 = new Subtask("Подзадача 2", "Описание подзадачи 2",1);
        task1.setId(1);
        task2.setId(1);
        assertTrue(task1.equals(task2));
    }
}
