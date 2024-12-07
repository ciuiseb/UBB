namespace lab10;

public class SortingTask : Task
{
    private List<int> _numbers;
    private AbstractSorter _sorter;

    public SortingTask(string taskId, string description, List<int> numbers)
        : base(taskId, description)
    {
        this._numbers = numbers;
        this._sorter = new DefaultSorter(numbers);
    }

    public SortingTask(string taskId, string description, List<int> numbers, SortingStrategy method)
        : base(taskId, description)
    {
        this._numbers = numbers;

        this._sorter = method switch
        {
            SortingStrategy.Quick => new QuickSorter(numbers),
            SortingStrategy.Bubble => new BubbleSorter(numbers),
            _ => new DefaultSorter(numbers)
        };
    }

    public override void Execute()
    {
        _sorter.Sort();

        foreach (var number in _numbers)
        {
            Console.Write(number + " ");
        }
        Console.WriteLine();
    }
}