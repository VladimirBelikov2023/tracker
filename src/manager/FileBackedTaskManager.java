package manager;

import exception.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Path path;
    private final static DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy/HH:mm");

    public FileBackedTaskManager(Path path, Scanner scanner) {
        this.path = path;
        super.scanner = scanner;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public Task getTaskId(int idTask) {
        Task task = super.getTaskId(idTask);
        save();
        return task;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTaskId(int idTask) {
        super.deleteTaskId(idTask);
        save();
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter(path.toFile()); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            for (Task task : getAllTask()) {
                bufferedWriter.write(taskCsv(task) + '\n');
            }
            bufferedWriter.write('\n' + historyToString());
        } catch (IOException ex) {
            throw new ManagerSaveException();
        }
    }

    public static FileBackedTaskManager load(final Path path, final Scanner scanner) {
        try {
            int id = 0;
            final Map<Integer, Task> data = new HashMap<>();
            final FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(path, scanner);
            final String[] lines = Files.readString(path).split(System.lineSeparator(), -1);
            for (int i = 0; i < lines.length - 2; i++) {
                Task task = toTask(lines[i].split(","));
                if(task instanceof SubTask){
                    SubTask sub = (SubTask) task;
                    Epic epic = (Epic) data.get(sub.getEpicId());
                    epic.addSubTask(sub.getId());
                }
                id = Integer.max(task.getId(), id);
                data.put(task.getId(), task);
            }
            String history = lines[lines.length - 1];
            if (!history.isBlank()) {
                for (String hs : history.split(",")) {
                    Task task = data.get(Integer.parseInt(hs));
                    fileBackedTaskManager.inMemoryHistoryManager.add(task);
                }
            }
            fileBackedTaskManager.tasks = data;
            fileBackedTaskManager.id = ++id;
            return fileBackedTaskManager;
        }catch (IOException ex){
            throw new ManagerSaveException();
        }
    }

    private static Task toTask(final String[] args) {
        final int id = Integer.parseInt(args[0]);
        final TypeTask typeOfTask = TypeTask.valueOf(args[1]);
        final String name = args[2];
        final String description = args[4];
        final Status status = Status.valueOf(args[3]);
        final long duration = Long.parseLong(args[6]);
        if(!args[5].equals("null")) {
            final LocalDateTime startTime = LocalDateTime.parse(args[5], format);
            switch (typeOfTask) {
                case TASK:
                    return new Task(id, name, description, status, duration, startTime);
                case EPIC:
                    return new Epic(id, name, description, status, duration, startTime);
                default:
                    final int epicId = Integer.parseInt(args[7]);
                    return new SubTask(id, name, description, status, epicId, duration, startTime);
            }
        }
        return new Epic(id, name, description, status, 0, null);
    }

    private String taskCsv(final Task task) {
        if (task instanceof SubTask) {
            return String.format("%d,%S,%s,%S,%s,%s,%s,%d", task.getId(), TypeTask.SUBTASK, task.getName(), task.getStatus(), task.getDescription(), task.getStartTime().format(format), task.getDuration(),((SubTask) task).getEpicId());
        } else if (task instanceof Epic) {
            LocalDateTime startTime = task.getStartTime();
            if(startTime == null){
                return String.format("%d,%S,%s,%S,%s,%s,%s", task.getId(), TypeTask.EPIC, task.getName(), task.getStatus(), task.getDescription(), "null", task.getDuration());
            }
            return String.format("%d,%S,%s,%S,%s,%s,%s", task.getId(), TypeTask.EPIC, task.getName(), task.getStatus(), task.getDescription(), startTime.format(format), task.getDuration());
        } else {
            return String.format("%d,%S,%s,%S,%s,%s,%s", task.getId(), TypeTask.TASK, task.getName(), task.getStatus(), task.getDescription(), task.getStartTime().format(format), task.getDuration());
        }
    }

    private String historyToString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Task task : getHistory()) {
            stringBuilder.append(task.getId()).append(",");
        }
        return stringBuilder.toString();
    }
}
