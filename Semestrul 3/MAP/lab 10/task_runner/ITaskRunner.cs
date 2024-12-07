using lab10.factory;

namespace lab10.task_runner;

public interface ITaskRunner
{
    void ExecuteOneTask();
    void ExecuteAll();
    void AddTask(Task task);
    bool HasTask();
}

public class StrategyTaskRunner(Strategy strategy) : ITaskRunner
{
    private IContainer? container = TaskContainerFactorySingleton.GetInstance().CreateContainer(strategy);

    public void ExecuteOneTask()
    {
        var task = container?.Remove();
        if (task != null)
        {
            task.Execute();
        }
    }

    public void ExecuteAll()
    {
        while (HasTask())
        {
            ExecuteOneTask();
        }
    }

    public void AddTask(Task task)
    {
        container?.Add(task);
    }

    public bool HasTask()
    {
        return !container.IsEmpty();
    }
}

public abstract class AbstractTaskRunner(ITaskRunner taskRunner) : ITaskRunner
{
    public virtual void ExecuteOneTask()
    {
        taskRunner.ExecuteOneTask();
    }

    public void ExecuteAll()
    {
        while (HasTask())
        {
            ExecuteOneTask();
        }
    }

    public void AddTask(Task task)
    {
        taskRunner.AddTask(task);
    }

    public bool HasTask()
    {
        return taskRunner.HasTask();
    }
}

public class PrinterTaskRunner(ITaskRunner taskRunner) : AbstractTaskRunner(taskRunner)
{
    public override void ExecuteOneTask()
    {
        base.ExecuteOneTask();
        Console.WriteLine($"Task was executed at: {DateTime.Now}");
    }
}

public class DelayTaskRunner(ITaskRunner taskRunner) : AbstractTaskRunner(taskRunner)
{
    public override void ExecuteOneTask()
    {
        try
        {
            Thread.Sleep(3000);
            base.ExecuteOneTask();
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Exception occurred: {ex.Message}");
        }
    }
}