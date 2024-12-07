namespace lab10;

public class QuickSorter(List<int> numbers) : AbstractSorter(numbers)
{
    public override void Sort()
    {
        QuickSort(numbers, 0, numbers.Count - 1);
    }

    private void QuickSort(List<int> list, int low, int high)
    {
        if (low >= high) return;
        var pivotIndex = low + (high - low) / 2;
        var pivotValue = list[pivotIndex];
        var i = low;
        var j = high;

        while (i <= j)
        {
            while (list[i] < pivotValue) i++;
            while (list[j] > pivotValue) j--;

            if (i > j) continue;
            (list[i], list[j]) = (list[j], list[i]);
            i++;
            j--;
        }

        if (low < j) QuickSort(list, low, j);
        if (i < high) QuickSort(list, i, high);
    }
}