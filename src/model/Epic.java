package model;

import java.util.ArrayList;

public class Epic extends Task{
    private final ArrayList<Integer> lsSubTasks = new ArrayList<>();
    public Epic(String name, String description, Status status) {
        super(name, description, status);
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

    public void addSubTask(Integer id){
        lsSubTasks.add(id);
    }
    public ArrayList<Integer> getAllSubTask(){
        return lsSubTasks;
    }


}
