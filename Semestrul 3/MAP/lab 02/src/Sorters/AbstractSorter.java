package Sorters;

import java.util.List;
// TODO 3
public abstract class AbstractSorter {
    protected List<Integer> numbers;

    public AbstractSorter(List<Integer> numbers) {
        this.numbers = numbers;
    }

    public abstract void sort();
}