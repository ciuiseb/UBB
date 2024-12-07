namespace lab10.factory;

public class TaskContainerFactorySingleton : IFactory
{
    private static TaskContainerFactorySingleton? _instance;
    private QueueContainer? _queue;
    private StackContainer? _stack;

    private TaskContainerFactorySingleton()
    {
    }

    public static TaskContainerFactorySingleton GetInstance()
    {
        return _instance ??= new TaskContainerFactorySingleton();
    }

    public IContainer? CreateContainer(Strategy strategy)
    {
        return strategy switch
        {
            Strategy.Fifo => _queue ??= new QueueContainer(),
            Strategy.Lifo => _stack ??= new StackContainer(),
            _ => null
        };
    }
}