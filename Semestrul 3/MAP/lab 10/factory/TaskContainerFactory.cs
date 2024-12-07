namespace lab10.factory;

public class TaskContainerFactory : IFactory
{
    public IContainer? CreateContainer(Strategy strategy)
    {
        return strategy switch
        {
            Strategy.Fifo => new QueueContainer(),
            Strategy.Lifo => new StackContainer(),
            _ => null
        };
    }
}