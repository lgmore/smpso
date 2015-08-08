package jmetal.util.comparators;

import jmetal.core.Solution;
import java.util.Comparator;

public class EqualSolutions implements Comparator{        
   
    @Override
    public int compare(Object object1, Object object2) {
        if (object1 == null) return 1;
        else if (object2 == null) return -1;
        Solution solution1 = (Solution)object1;
        Solution solution2 = (Solution)object2;
        int dominate1 = 0;
        int dominate2 = 0;
        int flag;
        double p1Objetivo1, p2Objetivo1, p1Objetivo2, p2Objetivo2;
        p1Objetivo1 = solution1.getObjective(0);
        p2Objetivo1 = solution2.getObjective(0);
        if (p1Objetivo1 < p2Objetivo1) flag = -1;
        else if (p1Objetivo1 > p2Objetivo1) flag = 1;
        else flag = 0;
        if (flag == -1) dominate2++;//2 domina en este objetivo
        if (flag == 1) dominate1++;//1 domina en este objetivo
        p1Objetivo2 = solution1.getObjective(1);
        p2Objetivo2 = solution2.getObjective(1);
        if (p1Objetivo2 < p2Objetivo2) flag = -1;
        else if (p1Objetivo2 > p2Objetivo2) flag = 1;
        else flag = 0;
        if (flag == -1) dominate2++;//1 domina en este objetivo
        if (flag == 1) dominate1++;//2 domina en este objetivo
        if (dominate1 == 0 && dominate2 == 0) return 0;//No se dominan (iguales, es lo que importa acá)
        return 2;
    }
    
    //mono objetivo ssim*entropia
    /*@Override
    public int compare(Object object1, Object object2) {
        if (object1 == null) return 1;//solution 2 dominate
        else if (object2 == null) return -1;//solution 1 dominate
        Solution solution1 = (Solution)object1;
        Solution solution2 = (Solution)object2;
        double p1Objetivo1, p2Objetivo1, p1Objetivo2, p2Objetivo2;
        p1Objetivo1 = solution1.getObjective(0);
        p2Objetivo1 = solution2.getObjective(0);
        p1Objetivo2 = solution1.getObjective(1);
        p2Objetivo2 = solution2.getObjective(1);
        if (p1Objetivo1*p1Objetivo2 < p2Objetivo1*p2Objetivo2) return 1;
        else if (p1Objetivo1*p1Objetivo2 > p2Objetivo1*p2Objetivo2) return -1;
        else return 0;
    }*/
    
}