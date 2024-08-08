package manager;

import model.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTask();

    void deleteAllTasks();

    Task getTaskId(int idTask);

    void createTask(Task task);

    void updateTask(Task task);

    void deleteTaskId(int idTask);

    List<Integer> getSubtasks(int epicId);

    List<Task> getHistory();
    List<Task> getPrioritizedTask();
}
