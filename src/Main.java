import enums.TaskStatus;
import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        inMemoryTaskManagerCreator();

    }

    private static void inMemoryTaskManagerCreator() {
        LocalDateTime nowTime = LocalDateTime.now();
        Duration oneHour = Duration.ofHours(1);

        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Задача 1", "Купить картошку 5 мешков");
        task1.setDuration(oneHour);
        task1.setStartTime(nowTime);
        manager.addTask(task1);
        Task task2 = new Task("Задача 2", "Купить бананов2 кг");
        task2.setDuration(oneHour);
        task2.setStartTime(nowTime.plusHours(2));
        task2.setDescription("Купить апельсинов10 кг");
        manager.addTask(task2);
        task2.setStatus(TaskStatus.IN_PROGRESS);
//        Task taskUpdate1 = new Task("Задача 1", "Купить апельсинов10 кг", TaskStatus.IN_PROGRESS,nowTime,oneHour);
        Task task2Add11 = manager.updateTask(task2);

        manager.getTaskById(task1.getId());
        System.out.println("История задачи 1: " + manager.getHistory());
        manager.getTaskById(task2.getId());
        System.out.println("История задачи 2: " + manager.getHistory());
        manager.getTaskById(task2Add11.getId());

        Epic epic1 = manager.addEpic(new Epic("Эпик 1", "1"));

        Subtask subtask1 = new Subtask("Повесить полку", "В прихожей", epic1.getId());
        subtask1.setDuration(oneHour);
        subtask1.setStartTime(nowTime.plusHours(3));
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Повесить полку", "На кухне", epic1.getId());
        subtask2.setDuration(oneHour);
        subtask2.setStartTime(nowTime.plusHours(4));
        manager.addSubtask(subtask2);
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
