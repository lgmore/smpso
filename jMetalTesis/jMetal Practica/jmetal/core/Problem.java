package jmetal.core;

import jmetal.util.JMException;
import java.io.Serializable;

public abstract class Problem implements Serializable {

    private final static int DEFAULT_PRECISSION = 16;//Defines the default precision of binary-coded variables
    protected int numeroDeVariables;//numero de variables del problema
    protected int numeroDeObjetivos;//numero de objetivos del problema
    protected String nombreProblema;//nombre del problema
    protected SolutionType tipoSolucion;//tipo de dato de la solucion
    protected double [] lowerLimit_;//Stores the lower bound values for each encodings.variable (only if needed)
    protected double [] upperLimit_;//Stores the upper bound values for each encodings.variable (only if needed)
    private int [] precision_;//Stores the number of bits used by binary-coded variables (e.g., BinaryReal variables). By default, they are initialized to DEFAULT_PRECISION)
    protected int [] length_;//Stores the length of each encodings.variable when applicable (e.g., Binary and Permutation variables)
    
    public abstract void evaluate(Solution solution) throws JMException;
    
    public Problem() {
        tipoSolucion = null;
    }

    public Problem(SolutionType solutionType) {
        tipoSolucion = solutionType;
    }
    
    public int getNumberOfVariables() {
        return numeroDeVariables;
    }

    public void setNumberOfVariables(int numberOfVariables) {
        numeroDeVariables = numberOfVariables;   
    }

    public int getNumberOfObjectives() {
        return numeroDeObjetivos;
    }

    public double getLowerLimit(int i) {
        return lowerLimit_[i];
    }

    public double getUpperLimit(int i) {
        return upperLimit_[i];
    }

    public int getPrecision(int var) {
        return precision_[var];
    }

    public int [] getPrecision() {
        return precision_;
    }

    public void setPrecision(int [] precision) {
        precision_ = precision;
    }

    public int getLength(int var) {
        if (length_ == null) return DEFAULT_PRECISSION;
        return length_[var];
    }

    public void setSolutionType(SolutionType type) {
        tipoSolucion = type;
    }

    public SolutionType getSolutionType() {
        return tipoSolucion;
    }

    public String getName() {
        return nombreProblema;
    }

    public int getNumberOfBits() {
        int result = 0;
        for (int var = 0; var < numeroDeVariables; var++) {
            result += getLength(var);
        }
        return result;
    }
}