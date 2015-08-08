package jmetal.util.comparators;

import jmetal.core.Solution;
import java.util.Comparator;

public class CrowdingDistanceComparator implements Comparator{
   
    @Override
    public int compare(Object o1, Object o2) {
        if (o1==null) return 1;//si o1 es null, o2 entrara en la lista de lideres
        else if (o2 == null) return -1;//si o2 es null, o1 entrara en la lista de lideres
        double distance1 = ((Solution)o1).getCrowdingDistance();
        double distance2 = ((Solution)o2).getCrowdingDistance();
        if (distance1 > distance2) return -1;//o1 entrara en la lista, la distancia de o1 es mayor a o2
        if (distance1 < distance2) return 1;//o2 entrara en la lista, la distancia de o2 es mayor a o1
        return 0;//son iguales, entrara o1
    }
}