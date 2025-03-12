public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        Task task1 = manager.addTask(new Task("Задача 1", "Купить картошку 5 мешков"));
        Task task2 = manager.addTask(new Task("Задача 2", "Купить бананов2 кг"));

        Task taskUpdate1 = new Task(task1.getId(), "Задача 1", "Купить апельсинов10 кг", TaskStatus.IN_PROGRESS);
        Task task2Add11 = manager.updateTask(taskUpdate1);

        manager.getTaskById(task1.getId());
        System.out.println("История задачи 1: " + manager.getHistory());
        manager.getTaskById(task2.getId());
        System.out.println("История задачи 2: " + manager.getHistory());
        manager.getTaskById(task2Add11.getId());

        Epic epic1 = manager.addEpic(new Epic("Эпик 1", "1"));
        Epic epic2 = manager.addEpic(new Epic("Эпик 2", "2"));
        manager.getEpic(epic1.getId());
        manager.getEpic(epic2.getId());
        System.out.println("История эпик: " + manager.getHistory());

        Subtask subtask1 = manager.addSubtask(new Subtask("Повесить полку", "В прихожей", epic1.getId()));
        Subtask subtask2 = manager.addSubtask(new Subtask("Повесить полку", "На кухне", epic1.getId()));
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());
        System.out.println("История подзадачи: " + manager.getHistory());

        subtask2.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask2);

        System.out.println("Все истории: ");
        printAllTasks(manager);

        System.out.println("Удалить эпик");
        manager.deleteEpicById(epic1.getId());
        printAllTasks(manager);

    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpic()) {
            System.out.println(epic);
            for (Task task : manager.getSubtasksByEpikId(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtask()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
