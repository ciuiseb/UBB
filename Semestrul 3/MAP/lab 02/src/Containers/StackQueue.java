package Containers;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;
// TODO 5.3
public abstract class StackQueue implements Container {
    protected List<Task> tasks = new ArrayList<Task>();

    @Override
    public void add(Task task) {
        tasks.add(task);
    }

    @Override
    public int size() {
        return tasks.size();
    }

    @Override
    public boolean isEmpty() {
        return tasks.isEmpty();
    }
}
