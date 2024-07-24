import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static final String HOME = System.getProperty("user.dir");
    public static final Path PATH = Path.of(HOME + File.separator + "resources" + File.separator + "data.txt");

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            final TaskManager taskManager = Managers.getDefaultTaskManager(PATH, scanner);
            while (true) {
                System.out.println("Введите команду: \n 1) Создать задачу \n 2) Обновить задачу \n " +
                        "3) Посмотреть список задач \n" +
                        " 4) Посмотреть задачу по id \n 5) Удалить задачу \n 6) Очистить список задач \n " +
                        "7) Получить историю просмотров \n 8) Выход");
                try {
                    int command = Integer.parseInt(scanner.nextLine());
                    switch (command) {
                        case 1:
                            printCreatingTask(scanner, taskManager);
                            break;
                        case 2:
                            printUpdatingTask(scanner, taskManager);
                            break;
                        case 3:
                            printGettingLsTask(taskManager);
                            break;
                        case 4:
                            printGettingTask(scanner, taskManager);
                            break;
                        case 5:
                            printDeletingTask(scanner, taskManager);
                            break;
                        case 6:
                            taskManager.deleteAllTasks();
                            System.out.println("Список задач очищен");
                            break;
                        case 7:
                            System.out.println(taskManager.getHistory());
                            break;
                        case 8:
                            System.out.println("Выход");
                            return;
                    }
                } catch (NumberFormatException numberFormatException) {
                    System.out.println(Arrays.toString(numberFormatException.getStackTrace()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static void printGettingTask(Scanner scanner, TaskManager taskManager) {
        System.out.println("Введите id желаемой задачи: ");
        final int id = Integer.parseInt(scanner.nextLine());
        System.out.println(taskManager.getTaskId(id));
    }

    private static void printDeletingTask(Scanner scanner, TaskManager taskManager) {
        System.out.println("Введите id желаемой задачи: ");
        final int id = Integer.parseInt(scanner.nextLine());
        taskManager.deleteTaskId(id);
        System.out.println("Задача удалена");
    }

    private static void printGettingLsTask(TaskManager taskManager) {
        System.out.println("Созданные задачи: \n");
        System.out.println(taskManager.getAllTask());
    }

    private static void printCreatingTask(Scanner scanner, TaskManager taskManager) {
        System.out.println("Выберите тип задачи: \n 1) Task \n 2) Epic \n 3) Subtask");
        int command = Integer.parseInt(scanner.nextLine());
        switch (command) {
            case 1:
                System.out.println("Введите название задачи: ");
                String name = scanner.nextLine();
                System.out.println("Введите описание: ");
                String description = scanner.nextLine();
                Task task = new Task(name, description);
                taskManager.createTask(task);
                System.out.println("Задача создана");
                return;
            case 2:
                System.out.println("Введите название задачи: ");
                name = scanner.nextLine();
                System.out.println("Введите описание: ");
                description = scanner.nextLine();
                Epic epic = new Epic(name, description);
                taskManager.createTask(epic);
                System.out.println("Задача создана");
                return;
            case 3:
                System.out.println("Введите название задачи: ");
                name = scanner.nextLine();
                System.out.println("Введите описание: ");
                description = scanner.nextLine();
                System.out.println("К какому epic относится подзадача");
                final int epicId = Integer.parseInt(scanner.nextLine());
                SubTask subTask = new SubTask(name, description, epicId);
                taskManager.createTask(subTask);
                System.out.println("Задача создана");
                return;
        }
    }

    private static void printUpdatingTask(Scanner scanner, TaskManager taskManager) {
        System.out.println("Выберите тип задачи: \n 1) Task \n 2) Epic \n 3) Subtask");
        int command = Integer.parseInt(scanner.nextLine());
        switch (command) {
            case 1:
                System.out.println("Введите id задачи: ");
                int id = Integer.parseInt(scanner.nextLine());
                System.out.println("Введите status задачи: ");
                Status status = Status.valueOf(scanner.nextLine());
                System.out.println("Введите название задачи: ");
                String name = scanner.nextLine();
                System.out.println("Введите описание: ");
                String description = scanner.nextLine();
                Task task = new Task(id, name, description, status);
                taskManager.updateTask(task);
                System.out.println("Задача создана");
                return;
            case 2:
                System.out.println("Введите id задачи: ");
                id = Integer.parseInt(scanner.nextLine());
                System.out.println("Введите status задачи: ");
                status = Status.valueOf(scanner.nextLine());
                System.out.println("Введите название задачи: ");
                name = scanner.nextLine();
                System.out.println("Введите описание: ");
                description = scanner.nextLine();
                Epic epic = new Epic(id, name, description, status);
                taskManager.createTask(epic);
                System.out.println("Задача создана");
                return;
            case 3:
                System.out.println("Введите id задачи: ");
                id = Integer.parseInt(scanner.nextLine());
                System.out.println("Введите status задачи: ");
                status = Status.valueOf(scanner.nextLine());
                System.out.println("Введите название задачи: ");
                name = scanner.nextLine();
                System.out.println("Введите описание: ");
                description = scanner.nextLine();
                System.out.println("К какому epic относится подзадача");
                final int epicId = scanner.nextInt();
                SubTask subTask = new SubTask(id, name, description, status, epicId);
                taskManager.createTask(subTask);
                System.out.println("Задача создана");
        }
    }
}
