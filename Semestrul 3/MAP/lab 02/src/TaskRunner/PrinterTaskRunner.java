package TaskRunner;

import java.time.LocalDateTime;

public class PrinterTaskRunner extends  AbstractTaskRunner{
    public PrinterTaskRunner(TaskRunner taskRunner) {
        super(taskRunner);
    }

    @Override
    public void executeOneTask() {
        super.executeOneTask();
        System.out.println("Task was executed at: " + LocalDateTime.now());
    }
}
