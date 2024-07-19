package manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private final HashMap<Integer, Node> history = new HashMap<>();

    private class Node {
        Task task;
        Node next;
        Node prev;

        public Node(Task task) {
            this.task = task;
        }
    }

    public void add(Task task) {
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

    public void remove(int id) {
        Node node = history.get(id);
        if (head == null) {
            return;
        } else if (head == node) {
            head = head.next;
        } else if (tail == node) {
            tail = tail.prev;
        } else {
            node.prev.next = node.next;
        }
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
