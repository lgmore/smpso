package jmetal.util.comparators;

import jmetal.core.Solution;
import java.util.Comparator;

public interface IConstraintViolationComparator extends Comparator {
    public int compare(Object o1, Object o2);
    public boolean needToCompare(Solution s1, Solution s2);
}
