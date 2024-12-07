namespace lab10;

public class QueueContainer : StackQueue
{
    public override Task Remove()
    {
        if (!IsEmpty())
        {
            var taskToRemove = Tasks[0];
            Tasks.RemoveAt(0);
            return taskToRemove;
        }
        else
        {
            return null;
        }
    }
}
