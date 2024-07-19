import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        Task task1= new Task("Купить машину", "описание", Status.New);
        Task task2 = new Task("2 задание", "описание 2задания", Status.New);
        Epic epic1 = new Epic("Эпик1", "описание 1эпик", Status.New);
        Epic epic2 = new Epic("эпик 2", "описание 2 эпика", Status.New);
        SubTask subtask1=new SubTask("под 1 эпика", "описание 1 под эпика", Status.New,3);
        SubTask subtask2 = new SubTask("под2 эпика", "описание 2 под эпика", Status.New, 3);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(epic1);
        taskManager.createTask(epic2);
        System.out.println(task1);
        System.out.println(task2);
        System.out.println(epic1);
        System.out.println(epic2);
        taskManager.createTask(subtask1);
        taskManager.createTask(subtask2);
        subtask2.setStatus(Status.DONE);
        subtask1.setStatus(Status.DONE);
        taskManager.updateTask(subtask2);
        System.out.println(epic2);
        taskManager.getTaskId(1);
        taskManager.getTaskId(2);
        System.out.println(taskManager.getHistory());
    }
}
