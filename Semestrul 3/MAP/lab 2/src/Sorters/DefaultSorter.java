package Sorters;

import java.util.Collections;
import java.util.List;

public class DefaultSorter extends AbstractSorter{
    public DefaultSorter(List<Integer> numbers) {
        super(numbers);
    }

    @Override
    public void sort() {
        Collections.sort(numbers);
    }
}
