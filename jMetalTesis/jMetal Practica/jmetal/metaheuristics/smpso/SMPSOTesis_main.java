package jmetal.metaheuristics.smpso;

import java.io.IOException;
import java.util.HashMap;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.MutationFactory;
import jmetal.problems.tesis.Tesis;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import org.apache.log4j.Logger;

/**
 * @author Marcos Brizuela
 */
public class SMPSOTesis_main {

    private static final Logger log = Logger.getLogger(SMPSOTesis_main.class.getName());

    public static void main(String[] args) throws JMException, IOException, ClassNotFoundException {

        Problem problema = new Tesis();
        Algorithm algoritmo = new SMPSOTesis(problema);
        algoritmo.setInputParameter("tamanhoEnjambre", Configuration.cantidadParticulas); //tamaño del enjambre
        algoritmo.setInputParameter("tamanhoLideres", Configuration.cantidadLideres);//tamaño del archivo de lideres
        algoritmo.setInputParameter("maximoIteraciones", Configuration.cantidadIteraciones);//maximo de iteraciones

        HashMap parameters = new HashMap();//Operator parameters
        parameters.put("probability", 1.0 / problema.getNumberOfVariables());//para mutacion
        parameters.put("distributionIndex", 20.0);//para mutacion
        Mutation mutacion = MutationFactory.getMutationOperator("PolynomialMutation", parameters);//operador de turbulencia (mutacion)
        algoritmo.addOperator("mutation", mutacion);
        long initTime = System.currentTimeMillis();
        log.info("EJECUTAR EL PSO");
        SolutionSet poblacion = algoritmo.execute();//ejecutar el pso
        long estimatedTime = System.currentTimeMillis() - initTime;
        log.info("FIN DEL PSO");
        log.info("Tiempo total: " + estimatedTime + " ms");
        log.info("Objectives values have been writen to file FUN");
        poblacion.printObjectivesToFile();
        poblacion.insertToBD();
        log.info("Variables values have been writen to file VAR");
        poblacion.printVariablesToFile();
        log.info("END!!!!!");
    }
}
