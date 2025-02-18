import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {

    private Epic epic1;
    private Epic epic2;

    @Test
    public void shouldBeTrueEpicId(){
        epic1 = new Epic("Эпик 1","Описание эпик 1");
        epic2 = new Epic("Эпик 2","Описание эпик 2");
        epic1.setId(1);
        epic2.setId(1);

        assertTrue(epic1.equals(epic2));

    }

}
