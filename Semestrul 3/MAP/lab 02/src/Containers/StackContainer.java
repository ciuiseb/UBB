package Containers;

import Tasks.Task;

public class StackContainer extends StackQueue {

    @Override
    public Task remove() {
        if (!tasks.isEmpty()) {
            return tasks.remove(tasks.size() - 1);
        } else {
            return null;
        }
    }
}
