import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Scanner;

public class Main {
    public static final String HOME = System.getProperty("user.dir");
    public static final Path PATH = Path.of(HOME + File.separator + "resources" + File.separator + "data.txt");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = Managers.getDefaultTaskManager(PATH, scanner);
        Task task = new Task("sdsds", "dcdvd", 15, LocalDateTime.of(2020, Month.DECEMBER, 12, 12, 15));
        Epic epic = new Epic("ddc", "ddddcd");
        SubTask subTask = new SubTask("scdsc", "dcdcd", 1,15,LocalDateTime.of(2020, Month.DECEMBER, 12, 12, 30));
        taskManager.createTask(subTask);
    }
}