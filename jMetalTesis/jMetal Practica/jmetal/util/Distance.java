package jmetal.util;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.comparators.ObjectiveComparator;
import jmetal.util.wrapper.XReal;

public class Distance {

    public Distance() {}

    public double [][] distanceMatrix(SolutionSet solutionSet) {
        Solution solutionI, solutionJ;
        double [][] distance = new double [solutionSet.size()][solutionSet.size()];
        for (int i = 0; i < solutionSet.size(); i++){
            distance[i][i] = 0.0;
            solutionI = solutionSet.get(i);
            for (int j = i + 1; j < solutionSet.size(); j++){
                solutionJ = solutionSet.get(j);
                distance[i][j] = this.distanceBetweenObjectives(solutionI,solutionJ);
                distance[j][i] = distance[i][j];
            }
        }
        return distance;
    }

    public double distanceToSolutionSetInObjectiveSpace(Solution solution, SolutionSet solutionSet) throws JMException{
        double distance = Double.MAX_VALUE;
        for (int i = 0; i < solutionSet.size();i++){
            double aux = this.distanceBetweenObjectives(solution,solutionSet.get(i));
            if (aux < distance) distance = aux;
        }
        return distance;
    }

    public double distanceToSolutionSetInSolutionSpace(Solution solution, SolutionSet solutionSet) throws JMException{
        double distance = Double.MAX_VALUE;
        for (int i = 0; i < solutionSet.size();i++){
            double aux = this.distanceBetweenSolutions(solution,solutionSet.get(i));
            if (aux < distance) distance = aux;
        }
        return distance;
    }

    public double distanceBetweenSolutions(Solution solutionI, Solution solutionJ) throws JMException{
        double distance = 0.0;
        XReal solI = new XReal(solutionI);
        XReal solJ = new XReal(solutionJ);
        double diff;
        for (int i = 0; i < solI.getNumberOfDecisionVariables(); i++){
            diff = solI.getValue(i) - solJ.getValue(i);
            distance += Math.pow(diff,2.0);
        }
        return Math.sqrt(distance);
    }

    public double distanceBetweenObjectives(Solution solutionI, Solution solutionJ){
        double diff;
        double distance = 0.0;
        for (int nObj = 0; nObj < solutionI.getNumberOfObjectives();nObj++){
          diff = solutionI.getObjective(nObj) - solutionJ.getObjective(nObj);
          distance += Math.pow(diff,2.0);
        }
        return Math.sqrt(distance);
    }

    public int indexToNearestSolutionInSolutionSpace(Solution solution, SolutionSet solutionSet) {
        int index = -1 ;
        double minimumDistance = Double.MAX_VALUE ;
        try {
            for (int i = 0 ; i < solutionSet.size(); i++) {
                double distance = 0;
                distance = distanceBetweenSolutions(solution, solutionSet.get(i));
                if (distance < minimumDistance) {
                    minimumDistance = distance;
                    index = i;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return index ;
    }

    public void crowdingDistanceAssignment(SolutionSet solutionSet, int numeroObjetivos) {
        int size = solutionSet.size();
        if (size == 0) return;//la lista esta vacía de soluciones
        if (size == 1) {//solo hay un candidato
            solutionSet.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
            return;
        }
        if (size == 2) {//solo hay dos candidatos
            solutionSet.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
            solutionSet.get(1).setCrowdingDistance(Double.POSITIVE_INFINITY);
            return;
        }
        SolutionSet front = new SolutionSet(size);//Use a new SolutionSet to evite alter original solutionSet
        for (int i = 0; i < size; i++){
            front.add(solutionSet.get(i));
        }
        for (int i = 0; i < size; i++){
            front.get(i).setCrowdingDistance(0.0);
        }
        double objetivoMaximo;
        double objetivoMinimo;
        double distancia;
        for (int i = 0; i<numeroObjetivos; i++) {//por cada objetivo
            // Sort the population by Obj n
            front.sort(new ObjectiveComparator(i));//orden ascendente segun valor del objetivo
            objetivoMinimo = front.get(0).getObjective(i);//valor del objetivo de la primera particula, minimo valor
            objetivoMaximo = front.get(front.size()-1).getObjective(i);//valor de la ultima, maximo valor
            //Set de crowding distance
            front.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);//al primero le pone infinito
            front.get(size-1).setCrowdingDistance(Double.POSITIVE_INFINITY);//al ultimo tambien
            for (int j = 1; j < size-1; j++) {//a las particulas entre la primera y la ultima le calcula de esta forma
                distancia = front.get(j+1).getObjective(i) - front.get(j-1).getObjective(i);//la diferencia entre los valores de los objetivos de las particulas anterior y posterior a la particula j (vecinos directos en la lista ordenada)
                distancia = distancia / (objetivoMaximo - objetivoMinimo);//ese valor lo divide entre la diferencia entre los objetivos maximo y minimo
                distancia += front.get(j).getCrowdingDistance();//ese valor le suma a su valor de distancia..
                front.get(j).setCrowdingDistance(distancia);// y pasa a ser su nuevo valor de distancia
            }
        }
    }
}