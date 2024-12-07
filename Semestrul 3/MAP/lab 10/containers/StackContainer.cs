namespace lab10;

public class StackContainer : StackQueue
{
    public override Task Remove()
    {
        if (!IsEmpty())
        {
            var taskToRemove = Tasks[Tasks.Count - 1];
            Tasks.RemoveAt(Tasks.Count - 1);
            return taskToRemove;
        }
        else
        {
            return null;
        }
    }
}