package jmetal.util.comparators;

import jmetal.core.Solution;
import java.util.Comparator;

public class ObjectiveComparator implements Comparator {

    private int nObj;
    private boolean ascendingOrder_;

    public ObjectiveComparator(int nObj) {
        this.nObj = nObj;
        ascendingOrder_ = true;
    }

    public ObjectiveComparator(int nObj, boolean descendingOrder) {
        this.nObj = nObj;
        if (descendingOrder) ascendingOrder_ = false;
        else ascendingOrder_ = true;
    }

    public int compare(Object o1, Object o2) {
        if (o1 == null) return 1;//equivale a o2 es menor (no se puede comparar o2 con null)
        else if (o2 == null) return -1;//equivale a o1 es menor (no se puede comparar o1 con null)
        double objetive1 = ((Solution) o1).getObjective(this.nObj);
        double objetive2 = ((Solution) o2).getObjective(this.nObj);
        if (ascendingOrder_) {
            if (objetive1 < objetive2) return -1;
            else if (objetive1 > objetive2) return 1;
            else return 0;
        } else {
            if (objetive1 < objetive2) return 1;
            else if (objetive1 > objetive2) return -1;
            else return 0;
        }
    }
}