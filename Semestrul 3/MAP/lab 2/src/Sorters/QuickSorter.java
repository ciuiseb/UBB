package Sorters;

import java.util.List;

public class QuickSorter extends AbstractSorter{

    public QuickSorter(List<Integer> numbers) {
        super(numbers);
    }
    @Override
    public void sort() {
        quickSort(numbers, 0, numbers.size() - 1);
    }

    private void quickSort(List<Integer> list, int low, int high) {
        if (low < high) {
            int pivotIndex = low + (high - low) / 2;
            int pivotValue = list.get(pivotIndex);
            int i = low, j = high;

            while (i <= j) {
                while (list.get(i) < pivotValue) i++;
                while (list.get(j) > pivotValue) j--;

                if (i <= j) {
                    int temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                    i++;
                    j--;
                }
            }

            if (low < j) quickSort(list, low, j);
            if (i < high) quickSort(list, i, high);
        }
    }
}
