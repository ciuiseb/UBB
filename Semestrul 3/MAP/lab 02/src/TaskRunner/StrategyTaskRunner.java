package TaskRunner;

import Containers.Container;
import Factory.Strategy;
import Factory.TaskContainerFactorySingleton;
import Tasks.Task;

public class StrategyTaskRunner implements TaskRunner{
    private Container container;

    public StrategyTaskRunner(Strategy strategy) {
        this.container = TaskContainerFactorySingleton.getInstance().createContainer(strategy);
    }

    @Override
    public void executeOneTask() {
        var task = container.remove();
        task.execute();
    }

    @Override
    public void executeAll() {
        while(hasTask()){
            executeOneTask();
        }
    }

    @Override
    public void addTask(Task t) {
        container.add(t);
    }

    @Override
    public boolean hasTask() {
        return !container.isEmpty();
    }
}
