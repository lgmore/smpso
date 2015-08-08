package jmetal.metaheuristics.smpso;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.MutationFactory;
import jmetal.problems.tesis.Tesis;
import jmetal.util.Configuration;
import jmetal.util.JMException;

/**
 * @author Marcos Brizuela
 */
public class SMPSOTesis_main {

    public static Logger logger_;
    public static FileHandler fileHandler_;
    public static int puertoclahe;

    public static int cantidadParticulas;
    public static int cantidadLideres;
    public static int cantidadIteraciones;

    public static final String DB_DRIVER = "org.postgresql.Driver";
    public static String DB_CONNECTION;
    public static final String DB_USER = "postgres";
    public static final String DB_PASSWORD = "luigi3626419";
    public static String nombreImagen;

    static {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("algoritmo.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            cantidadParticulas = Integer.valueOf(prop.getProperty("algoritmo.cantidadparticulas"));
            cantidadLideres = Integer.valueOf(prop.getProperty("algoritmo.cantidadlideres"));
            cantidadIteraciones = Integer.valueOf(prop.getProperty("algoritmo.cantidaditeraciones"));
            DB_CONNECTION=prop.getProperty("bd.conexion");
            nombreImagen=prop.getProperty("algoritmo.nombreimagen");
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) throws JMException, IOException, ClassNotFoundException {

        puertoclahe = Integer.valueOf(args[0]);

        logger_ = Configuration.logger_;
        fileHandler_ = new FileHandler("SMPSOTesis_main.log");
        logger_.addHandler(fileHandler_);
        Problem problema = new Tesis();//pasamos el numero de variables
        Algorithm algoritmo = new SMPSOTesis(problema);
        algoritmo.setInputParameter("tamañoEnjambre", cantidadParticulas);//tamaño del enjambre
        algoritmo.setInputParameter("tamañoLideres", cantidadLideres);//tamaño del archivo de lideres
        algoritmo.setInputParameter("maximoIteraciones", cantidadIteraciones);//maximo de iteraciones
        HashMap parameters = new HashMap();//Operator parameters
        parameters.put("probability", 1.0 / problema.getNumberOfVariables());//para mutacion
        parameters.put("distributionIndex", 20.0);//para mutacion
        Mutation mutacion = MutationFactory.getMutationOperator("PolynomialMutation", parameters);//operador de turbulencia (mutacion)
        algoritmo.addOperator("mutation", mutacion);
        long initTime = System.currentTimeMillis();
        SolutionSet poblacion = algoritmo.execute();//ejecutar el pso
        long estimatedTime = System.currentTimeMillis() - initTime;
        logger_.info("Tiempo total: " + estimatedTime + " ms");
        logger_.info("Objectives values have been writen to file FUN");
        poblacion.printObjectivesToFile("FUN");
        logger_.info("Variables values have been writen to file VAR");
        poblacion.printVariablesToFile("VAR");
    }
}
