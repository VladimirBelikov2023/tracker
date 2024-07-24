package manager;

import exception.WrongDataTaskException;
import exception.WrongIdEpicException;
import exception.WrongIdException;
import model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class InMemoryTaskManager implements TaskManager {
    protected int id = 0;
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Scanner scanner;
    protected final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistoryManager();

    public List<Task> getAllTask() {
        return List.copyOf(tasks.values());
    }

    public void deleteAllTasks() {
        inMemoryHistoryManager.clear();
        tasks.clear();
    }

    public Task getTaskId(final int idTask) {
        try {
            final Task task = tasks.get(idTask);
            if (task == null) {
                throw new WrongIdException("Задача не найдена", scanner);
            }
            inMemoryHistoryManager.add(task);
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
                Task isEpic = getTaskId(subTask.getEpicId());
                Epic epic = (Epic) isEpic;
                if (checkTypeTask(isEpic) != TypeTask.EPIC) {
                    throw new WrongIdException("Неверное указан epic id", scanner);
                }
                epic.addSubTask(subTask.getId());
            }
            tasks.put(id, task);
            id++;
        } catch (WrongIdException ex) {
            System.out.println(ex.getMessage());
            createTask(getTaskId(ex.changeId()));
        } catch (WrongDataTaskException dataEx) {
            changeDataTask(dataEx, task);
        }
    }

    public void updateTask(final Task task) {
        try {
            if (!isExist(task.getId())) {
                throw new WrongIdException("Задача не найдена!", scanner);
            }
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
            }
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
            final Task task = getTaskId(idTask);
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
            }
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
