import manager.TaskManager;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest {
    TaskManager taskManager;

    @Test()
    void createTask() {
        Task task = new Task("ddcdc", "sddc", 14, LocalDateTime.now());
        taskManager.createTask(task);
        Task addedTask = taskManager.getTaskId(0);
        Assertions.assertNotNull(addedTask, "Задача не найдена");
        assertEquals(addedTask,task, "Задачи не совпадают");
    }

    @Test
    void getWrongId(){
        NullPointerException ex =  assertThrows(
                NullPointerException.class,
                () -> taskManager.getTaskId(0));
        assertEquals(NullPointerException.class, ex.getClass());
    }

}