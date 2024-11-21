package Tasks;

import java.util.Objects;

public abstract class Task {
    public String taskID;
    public String description;

    public Task(String taskID, String description) {
        this.taskID = taskID;
        this.description = description;
    }

    public abstract void execute();
    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskID, task.taskID) && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskID, description);
    }

    @Override
    public String toString() {
        return "Tasks.Task{" +
                "taskID='" + taskID + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
