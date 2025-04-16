package tasks;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class SubtaskTest {
    private Subtask subtask1;
    private Subtask subtask2;

    @Test
    public void shouldTrueSubtaskId(){
        subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1",1);
        subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2",1);
        subtask1.setId(1);
        subtask2.setId(1);
        assertTrue(subtask1.equals(subtask2));
    }
}
