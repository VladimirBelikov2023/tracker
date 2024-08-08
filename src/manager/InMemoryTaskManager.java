package manager;

import exception.WrongDataTaskException;
import exception.WrongIdEpicException;
import exception.WrongIdException;
import model.*;

import java.time.Duration;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int id = 0;
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Scanner scanner;
    protected TreeSet<Task> priority = new TreeSet<>((Task task1, Task task2) -> {
        if(task1.getStartTime().isAfter(task2.getStartTime())){
            return 1;
        }else if(task1.getStartTime().isBefore(task2.getStartTime())){
            return -1;
        }else{
            return 0;
        }
    });
    protected final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistoryManager();

    public List<Task> getAllTask() {
        return List.copyOf(tasks.values());
    }

    public void deleteAllTasks() {
        inMemoryHistoryManager.clear();
        tasks.clear();
        priority.clear();
    }
    public List<Task> getPrioritizedTask(){
        return List.copyOf(priority);
    }
    public Task getTaskId(final int idTask){
        Task task =  getTaskIdBase(idTask);
        inMemoryHistoryManager.add(task);
        return task;
    }

    private Task getTaskIdBase(final int idTask) {
        try {
            final Task task = tasks.get(idTask);
            if (task == null) {
                throw new WrongIdException("Задача не найдена", scanner);
            }
            return task;
        } catch (WrongIdException ex) {
            System.out.println(ex.getMessage());
            return getTaskId(ex.changeId());
        }
    }

    public void createTask(final Task task) {
        try {
            if (task.getName().isEmpty() || task.getDescription().isBlank()) {
                throw new WrongDataTaskException("Неверные данные задачи", scanner);
            }
            task.setId(id);
            if (task instanceof SubTask) {
                final SubTask subTask = (SubTask) task;
                Task isEpic = getTaskIdBase(subTask.getEpicId());
                Epic epic = (Epic) isEpic;
                if (checkTypeTask(isEpic) != TypeTask.EPIC) {
                    throw new WrongIdException("Неверное указан epic id", scanner);
                }
                setEpicTime(epic, subTask);
                epic.addSubTask(subTask.getId());
            }
            priority.add(task);
            tasks.put(id, task);
            id++;
        } catch (WrongIdException ex) {
            System.out.println(ex.getMessage());
            createTask(getTaskId(ex.changeId()));
        } catch (WrongDataTaskException dataEx) {
            changeDataTask(dataEx, task);
        }
    }

    private void setEpicTime(Epic epic, SubTask subTask){
        if(epic.getStartTime() == null){
            epic.setStartTime(subTask.getStartTime());
            epic.setDuration(subTask.getDuration());
        }else if(epic.getStartTime().isAfter(subTask.getStartTime())){
            epic.setDuration(epic.getDuration() + Duration.between(epic.getStartTime(), subTask.getStartTime()).getSeconds()/60);
        }else if(epic.getEndTime().isBefore(subTask.getEndTime())){
            epic.setDuration(epic.getDuration() + Duration.between(epic.getEndTime(), subTask.getEndTime()).getSeconds()/60);
        }
    }
    private void delEpicTime(Epic epic, SubTask subTask){
        if(epic.getStartTime().isEqual(subTask.getStartTime())){
            epic.setStartTime(epic.getStartTime().plus(Duration.between(subTask.getStartTime(),subTask.getEndTime())));
            epic.setDuration(epic.getDuration() - subTask.getDuration());
        }else if(epic.getEndTime().isEqual(subTask.getEndTime())){
            epic.setDuration(epic.getDuration() - subTask.getDuration());
        }
    }

    public void updateTask(final Task task) {
        try {
            if (!isExist(task.getId())) {
                throw new WrongIdException("Задача не найдена!", scanner);
            }
            Task oldTask = getTaskIdBase(task.getId());
            priority.remove(oldTask);
            if (task.getName().isEmpty() || task.getDescription().isBlank()) {
                throw new WrongDataTaskException("Неверные данные задачи", scanner);
            }

            if (checkTypeTask(task) == TypeTask.SUBTASK) {
                final SubTask subTask = (SubTask) task;
                Task isEpic = getTaskId(subTask.getEpicId());
                if (checkTypeTask(isEpic) != TypeTask.EPIC) {
                    throw new WrongIdEpicException("Неверное указан epic id", scanner);
                }
                final Epic epic = (Epic) getTaskId(subTask.getEpicId());
                boolean isDone = true;
                for (Integer idSubtask : epic.getAllSubTask()) {
                    final SubTask sub = (SubTask) getTaskId(idSubtask);
                    if (sub.getStatus() != Status.DONE) {
                        isDone = false;
                        break;
                    }
                }
                if (isDone) {
                    epic.setStatus(Status.DONE);
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                }
                setEpicTime(epic, subTask);
            }
            priority.add(task);
            tasks.put(task.getId(), task);
        } catch (WrongIdEpicException epicEx) {
            int id = epicEx.changeId();
            SubTask sub = (SubTask) task;
            sub.setEpicId(id);
            updateTask(sub);

        } catch (WrongIdException ex) {
            int id = ex.changeId();
            task.setId(id);
            updateTask(task);
        } catch (WrongDataTaskException dataEx) {
            changeDataTask(dataEx, task);
        }
    }

    public void deleteTaskId(final int idTask) {
        try {
            if (!isExist(idTask)) {
                throw new WrongIdException("Неверный id", scanner);
            }
            final Task task = getTaskIdBase(idTask);
            final TypeTask typeTask = checkTypeTask(task);
            if (typeTask == TypeTask.EPIC) {
                final Epic epic = (Epic) task;
                for (int subId : epic.getAllSubTask()) {
                    deleteTaskId(subId);
                }
            } else if (typeTask == TypeTask.SUBTASK) {
                final SubTask subTask = (SubTask) task;
                final Epic epic = (Epic) getTaskId(subTask.getEpicId());
                epic.deleteSubtask(subTask.getId());
                delEpicTime(epic,subTask);
                setEpicTime(epic, subTask);
            }
            priority.remove(task);
            inMemoryHistoryManager.remove(idTask);
            tasks.remove(idTask);
        } catch (WrongIdException ex) {
            deleteTaskId(ex.changeId());
        }
    }

    public List<Integer> getSubtasks(final int epicId) {
        final Epic epic = (Epic) tasks.get(epicId);
        return epic.getAllSubTask();
    }

    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    private boolean isExist(int id) {
        return tasks.containsKey(id);
    }

    private TypeTask checkTypeTask(Task task) {
        if (task instanceof SubTask) {
            return TypeTask.SUBTASK;
        } else if (task instanceof Epic) {
            return TypeTask.EPIC;
        } else {
            return TypeTask.TASK;
        }
    }

    private void changeDataTask(WrongDataTaskException dataEx, Task task) {
        String name = dataEx.changeName();
        String description = dataEx.changeDescription();
        task.setName(name);
        task.setDescription(description);
        createTask(task);
    }
}
