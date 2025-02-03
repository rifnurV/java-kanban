public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Task task1 = new Task("Купить картошку","5 мешков");
        Task task1Add = manager.addTask(task1);
        System.out.println(task1Add);

        Task task2 = new Task("Купить бананов","2 кг");
        Task task2Add  = manager.addTask(task2);
        System.out.println(task2Add);

        Task task2Update = new Task(task2.getId(), "Купить апельсинов","10 кг" , TaskStatus.IN_PROGRESS);
        Task task2Add11 =  manager.updateTask(task2Update);
        System.out.println("update "+task2Add11);
        System.out.println(manager.getTasks());


        Epic epic1 = new Epic("Эпик 1","1");
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Повесить полку","В прихожей",epic1.getId());
        Subtask subtask2 = new Subtask("Повесить полку","На кухне",epic1.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        System.out.println(epic1);

        subtask2.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask2);
        System.out.println(epic1);
    }
}
