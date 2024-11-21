package Containers;

import Tasks.Task;
// TODO 5.2
public class QueueContainer extends StackQueue {
    @Override
    public Task remove() {
        if (isEmpty()) {
            return null;
        }
        return tasks.remove(0);
    }
}
