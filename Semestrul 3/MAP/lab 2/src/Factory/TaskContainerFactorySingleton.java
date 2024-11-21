package Factory;

import Containers.Container;
import Containers.QueueContainer;
import Containers.StackContainer;
// TODO 7
public class TaskContainerFactorySingleton implements Factory{
    private static TaskContainerFactorySingleton instance;
    private QueueContainer queue;
    private StackContainer stack;

    private TaskContainerFactorySingleton() {}
    static public TaskContainerFactorySingleton getInstance(){
        if(instance == null){
            instance = new TaskContainerFactorySingleton();
        }
        return instance;
    }
    @Override
    public Container createContainer(Strategy strategy) {
        switch (strategy){
            case FIFO -> {
                if(queue == null){
                    queue = new QueueContainer();
                }
                return queue;
            }
            case LIFO -> {
                if(stack == null){
                    stack = new StackContainer();
                }
                return stack;
            }
        }
        return null;
    }
}
