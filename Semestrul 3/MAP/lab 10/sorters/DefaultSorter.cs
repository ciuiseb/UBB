namespace lab10;

public class DefaultSorter(List<int> numbers) : AbstractSorter(numbers)
{
    public override void Sort()
    {
        numbers.Sort();
    }
}