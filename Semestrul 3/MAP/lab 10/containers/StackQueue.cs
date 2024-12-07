namespace lab10;

public abstract class StackQueue : IContainer
{
    protected List<Task> Tasks = new List<Task>();

    public void Add(Task task)
    {
        Tasks.Add(task);
    }

    public int Size()
    {
        return Tasks.Count;
    }

    public bool IsEmpty()
    {
        return Tasks.Count == 0;
    }

    public abstract Task Remove();
}
