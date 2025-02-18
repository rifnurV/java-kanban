
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Task> historyMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (historyMap.size()<10) {
            historyMap.put(task.getId(), task);
        } else {
            historyMap.remove(0);
            historyMap.put(task.getId(), task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyMap.values().stream().collect(Collectors.toList());
    }
}
