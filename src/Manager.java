import model.Task;

import java.util.List;

public interface Manager {
    List<Task> getAllTask();
    void deleteAllTasks();
    Task getTaskId(int idTask);
    void createTask(Task task);
    void updateTask(Task task);
    void deleteTaskId(int idTask);
    List<Integer> getSubtasks(int epicId);
}
