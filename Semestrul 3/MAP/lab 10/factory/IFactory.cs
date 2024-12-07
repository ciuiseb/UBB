namespace lab10.factory;

public interface IFactory
{
    IContainer? CreateContainer(Strategy strategy);
}
