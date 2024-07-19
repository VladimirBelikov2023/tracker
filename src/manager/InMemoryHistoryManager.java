package manager;

import model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final LinkedList<Task> history = new LinkedList<>();
    public List<Task> getHistory() {
        return history;
    }
    public void add(Task task){
        history.addFirst(task);
        if(history.size()>10){
            history.removeLast();
        }
    }
}
