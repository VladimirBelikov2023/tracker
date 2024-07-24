package model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> lsSubTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public void addSubTask(final Integer id) {
        lsSubTasks.add(id);
    }

    public ArrayList<Integer> getAllSubTask() {
        return lsSubTasks;
    }


    public void deleteSubtask(final int subTaskId) {
        lsSubTasks.remove((Integer) subTaskId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "lsSubTasks=" + lsSubTasks +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
