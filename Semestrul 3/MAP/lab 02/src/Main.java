import Sorters.SortingMethod;
import Tasks.SortingTask;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> nr = new ArrayList<>();
        nr.add(1);
        nr.add(3);
        nr.add(2);
        SortingTask stDefault = new SortingTask("1", "task care sorteaza", nr);
        SortingTask stQuick = new SortingTask("1", "task care sorteaza", nr, SortingMethod.QUICK);
        stDefault.execute();
        stQuick.execute();
    }
}