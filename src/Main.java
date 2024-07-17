import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

public class Main {
    public static void main(String[] args) {
        Manager manager= new Manager();
        Task task1= new Task("Купить машину", "описание", Status.New);
        Task task2 = new Task("2 задание", "описание 2задания", Status.New);
        Epic epic1 = new Epic("Эпик1", "описание 1эпик", Status.New);
        Epic epic2 = new Epic("эпик 2", "описание 2 эпика", Status.New);
        SubTask subtask1=new SubTask("под 1 эпика", "описание 1 под эпика", Status.New,3);
        SubTask subtask2 = new SubTask("под2 эпика", "описание 2 под эпика", Status.New, 3);
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(epic1);
        manager.createTask(epic2);
        System.out.println(task1);
        System.out.println(task2);
        System.out.println(epic1);
        System.out.println(epic2);
        manager.createTask(subtask1);
        manager.createTask(subtask2);
        subtask2.setStatus(Status.DONE);
        subtask1.setStatus(Status.DONE);
        manager.updateTask(subtask2);
        System.out.println(epic2);
    }
}
