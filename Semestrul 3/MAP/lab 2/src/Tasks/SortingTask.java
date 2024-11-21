package Tasks;

import Sorters.AbstractSorter;
import Sorters.BubbleSorter;
import Sorters.DefaultSorter;
import Sorters.QuickSorter;
import Sorters.SortingMethod;
import java.util.List;

public class SortingTask extends Task{
    List<Integer> numbers;
    AbstractSorter sorter;
    public SortingTask(String taskID, String description, List<Integer> nr) {
        super(taskID, description);
//        this.numbers = new List<Integer>(nr); ?? cum sa fac sa mearga
        this.numbers = nr;
        sorter = new DefaultSorter(numbers);
    }
    public SortingTask(String taskID, String description, List<Integer> nr, SortingMethod method) {
        super(taskID, description);
        this.numbers = nr;
        sorter = new DefaultSorter(numbers);
        switch (method){
            case QUICK -> sorter = new QuickSorter(numbers);
            case BUBBLE -> sorter = new BubbleSorter(numbers);
        }
    }
    @Override
    public void execute() {
        sorter.sort();
        for(var number: numbers){
            System.out.print(number + " ");
        }
        System.out.println();
    }

}
