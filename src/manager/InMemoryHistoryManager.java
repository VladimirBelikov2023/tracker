package manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    protected HashMap<Integer, Node> history = new HashMap<>();

    private class Node {
        Task task;
        Node next;
        Node prev;

        public Node(Task task) {
            this.task = task;
        }
    }

    public void add(final Task task) {
        Node node = new Node(task);
        if (history.containsKey(task.getId())) {
            remove(task.getId());
        }
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
            node.prev = tail;
        }
        tail = node;
        history.put(task.getId(), node);

    }

    public void remove(final int id) {
        Node node = history.get(id);
        if (head == node) {
            if (head.next != null) {
                head = head.next;
            } else {
                head = null;
            }
        } else if (tail == node) {
            tail = tail.prev;
        } else {
            node.prev.next = node.next;
        }
        history.remove(id);
    }

    public void clear() {
        history.clear();
    }


    public List<Task> getHistory() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = head;
        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }
        return tasks;
    }
}
