package jmetal.util.archive;

import jmetal.core.Solution;
import jmetal.util.Distance;
import jmetal.util.comparators.CrowdingDistanceComparator;
import jmetal.util.comparators.DominanceComparator;
import jmetal.util.comparators.EqualSolutions;
import java.util.Comparator;

public class CrowdingArchive extends Archive {

    private int tamanhoMaximo;//Stores the maximum size of the archive.
    private int numeroObjetivos;//stores the number of the objectives.
    private Comparator dominance_;//Stores a <code>Comparator</code> for dominance checking. Es un DominanceComparator
    private Comparator equals_;//Stores a <code>Comparator</code> for equality checking (in the objective space).
    private Comparator crowdingDistance_;//Stores a <code>Comparator</code> for checking crowding distances.
    private Distance distance_;//Stores a <code>Distance</code> object, for distances utilities
    
    public CrowdingArchive(int maxSize, int numberOfObjectives) {
        super(maxSize);
        tamanhoMaximo = maxSize;
        numeroObjetivos = numberOfObjectives;        
        dominance_ = new DominanceComparator();
        equals_ = new EqualSolutions();
        crowdingDistance_ = new CrowdingDistanceComparator();
        distance_ = new Distance();
    }

    @Override
    public boolean add(Solution solucionCandidata){
        int flag;//bandera para saber si la solucion que viene como parametro es dominada o no respecto a los que ya estan en la lista de lideres
        int i = 0;
        Solution auxPareto;
        while (i < solutionsList_.size()){
            auxPareto = solutionsList_.get(i);
            flag = dominance_.compare(solucionCandidata,auxPareto);//retorna -1 si solution domina, 0 si no se dominan, 1 si aux domina (si da 0, solo se sabe que no se dominan, todavia no se sabe si son iguales o no)
            if (flag == 1) {//auxPareto domina, candidata no es mejor que un elemento del pareto
                return false;//no se debe añadir, descartar solucion y salir del metodo add
            } else if (flag == -1) {//candidato domina, elemento del pareto debe salir
                solutionsList_.remove(i);//quitarla de la poblacion de lideres y seguir el while
            } else {//no se dominan, posiblemente es candidato a entrar al pareto o es igual a algun elemento del pareto
                //aca se cambia el orden, aux es o1 y solution es o2 para el comparador de igualdad
                if (equals_.compare(auxPareto,solucionCandidata)==0) {//si da 0 son iguales, hay que salir del metodo
                    return false;//no hacer nada y salir del metodo add
                }
                i++;
            }
        }
        solutionsList_.add(solucionCandidata);//insertar solucion en el archivo de lideres
        if (size() > tamanhoMaximo) { //la poblacion se lleno
            distance_.crowdingDistanceAssignment(this,numeroObjetivos);      
            remove(indexWorst(crowdingDistance_));//quitamos la peor solucion
        }        
        return true;
    }
}