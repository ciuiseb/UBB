namespace lab10;

public class BubbleSorter(List<int> numbers) : AbstractSorter(numbers)
{
    public override void Sort()
    {
        for (var i = 0; i < numbers.Count - 1; i++)
        {
            for (var j = 0; j < numbers.Count - i - 1; j++)
            {
                if (numbers[j] > numbers[j + 1])
                {
                    (numbers[j], numbers[j + 1]) = (numbers[j + 1], numbers[j]);
                }
            }
        }
    }
}