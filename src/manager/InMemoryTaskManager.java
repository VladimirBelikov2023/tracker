package manager;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private static int id = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistoryManager();
    public List<Task> getAllTask(){
        return List.copyOf(tasks.values());
    }
    public void deleteAllTasks(){
        tasks.clear();
    }
    public Task getTaskId(int idTask){
        final Task task = tasks.get(idTask);
        inMemoryHistoryManager.add(task);
        return task;
    }
    public void createTask(Task task){
        task.setId(id);
        if(task instanceof SubTask){
            SubTask subTask = (SubTask)task;
            Epic epic = (Epic) tasks.get(subTask.getEpicId());
            epic.addSubTask(subTask.getId());
        }
        tasks.put(id,task);
        id++;
    }
    public void updateTask(Task task){
        if(task instanceof SubTask && task.getStatus()==Status.DONE){
            final SubTask subTask = (SubTask)task;
            final Epic epic = (Epic)tasks.get(subTask.getEpicId());
            boolean isDone = true;
            for(Integer idSubtask: epic.getAllSubTask()){
                final SubTask sub = (SubTask) tasks.get(idSubtask);
                if (sub.getStatus()!=Status.DONE){
                    isDone = false;
                    break;
                }
            }
            if(isDone){
                epic.setStatus(Status.DONE);
            }else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
        tasks.put(id, task);
    }
    public void deleteTaskId(int idTask){
        tasks.remove(idTask);
    }
    public List<Integer> getSubtasks(int epicId){
        final Epic epic = (Epic)tasks.get(epicId);
        return epic.getAllSubTask();
    }
    public List<Task>getHistory(){
        return inMemoryHistoryManager.getHistory();
    }
}
