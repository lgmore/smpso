package jmetal.util.comparators;

import jmetal.core.Solution;

public class OverallConstraintViolationComparator implements IConstraintViolationComparator {

    public int compare(Object o1, Object o2) {
        double overall1, overall2;
        overall1 = ((Solution)o1).getOverallConstraintViolation();
        overall2 = ((Solution)o2).getOverallConstraintViolation();
        if ((overall1 < 0) && (overall2 < 0)) {//ambos negativos
            if (overall1 > overall2) return -1;//si o1 es mayor a o2, retorna o1
            else if (overall2 > overall1) return 1;//si o2 es mayor a o1, retorna o2
            else return 0;//son iguales
        } else if ((overall1 == 0) && (overall2 < 0)) return -1;//o1 es cero y o2 negativo, retorna o1
        else if ((overall1 < 0) && (overall2 == 0)) return 1;//o1 es negativo y o2 es cero, retorna o2
        else return 0;//son iguales
    }

    public boolean needToCompare(Solution s1, Solution s2) {
        boolean needToCompare;
        needToCompare = (s1.getOverallConstraintViolation() < 0) || (s2.getOverallConstraintViolation() < 0);
        return needToCompare;
    }
}