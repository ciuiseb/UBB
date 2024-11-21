package Factory;

import Containers.Container;
import Containers.QueueContainer;
import Containers.StackContainer;

public class TaskContainerFactory implements Factory{
    @Override
    public Container createContainer(Strategy strategy) {
        switch (strategy){
            case FIFO -> { return new QueueContainer(); }
            case LIFO -> { return new StackContainer(); }
        }
        return null;
    }
}
