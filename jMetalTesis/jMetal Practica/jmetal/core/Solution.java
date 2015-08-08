package jmetal.core;

import jmetal.encodings.variable.Binary;
import java.io.Serializable;

public class Solution implements Serializable {  
    
    private Problem problem_;
    private SolutionType type_;
    private Variable[] variable_;
    private final double [] objective_;
    private int numberOfObjectives_;
    private double crowdingDistance_;
    private String nombreImagenResultado;
    private double fitness_;//solo usado por algunos algoritmos
    private double overallConstraintViolation_;//Stores the overall constraint violation of the solution.
    private int numberOfViolatedConstraints_;//Stores the number of constraints violated by the solution.

    public Solution() {        
        problem_ = null;
        overallConstraintViolation_ = 0.0;
        numberOfViolatedConstraints_ = 0;
        crowdingDistance_ = 0.0;
        type_ = null;
        variable_ = null;
        objective_ = null;
    }

    public Solution(int numberOfObjectives) {
        numberOfObjectives_ = numberOfObjectives;
        objective_ = new double[numberOfObjectives];
    }

    public Solution(Problem problem) throws ClassNotFoundException{
        problem_ = problem;
        type_ = problem.getSolutionType();
        numberOfObjectives_ = problem.getNumberOfObjectives();
        objective_ = new double[numberOfObjectives_];
        fitness_ = 0.0;
        variable_ = type_.createVariables();
    }
  
    static public Solution getNewSolution(Problem problem) throws ClassNotFoundException {
        return new Solution(problem);
    }
  
    public Solution(Problem problem, Variable [] variables){
        problem_ = problem;
        type_ = problem.getSolutionType();
        numberOfObjectives_ = problem.getNumberOfObjectives();
        objective_ = new double[numberOfObjectives_];
        fitness_ = 0.0;
        variable_ = variables;
    }

    public Solution(Solution solution) {            
        problem_ = solution.problem_ ;
        type_ = solution.type_;
        numberOfObjectives_ = solution.getNumberOfObjectives();
        objective_ = new double[numberOfObjectives_];
        for (int i = 0; i < objective_.length;i++) {
            objective_[i] = solution.getObjective(i);
        }
        variable_ = type_.copyVariables(solution.variable_);
        overallConstraintViolation_ = solution.getOverallConstraintViolation();
        numberOfViolatedConstraints_ = solution.getNumberOfViolatedConstraint();
        fitness_ = solution.getFitness();
        nombreImagenResultado = solution.nombreImagenResultado;
    }

    public void setCrowdingDistance(double distance){
        crowdingDistance_ = distance;
    }

    public double getCrowdingDistance(){
        return crowdingDistance_;
    }
    
    public void setFitness(double fitness) {
        fitness_ = fitness;
    }

    public double getFitness() {
        return fitness_;
    }

    public void setObjective(int i, double value) {
        objective_[i] = value;
    }

    public double getObjective(int i) {
        return objective_[i];
    }

    public int getNumberOfObjectives() {
        if (objective_ == null) return 0;
        else return numberOfObjectives_;
    }

    public int numberOfVariables() {
        return problem_.getNumberOfVariables();
    }

    @Override
    public String toString() {
        String aux="";
        for (int i = 0; i < this.numberOfObjectives_; i++)
            aux = aux + this.getObjective(i) + "    ----    ";
        return aux;
    }

    public Variable[] getDecisionVariables() {
        return variable_;
    }

    public void setDecisionVariables(Variable [] variables) {
        variable_ = variables;
    }

    public Problem getProblem() {
        return problem_;
    }

    public void setOverallConstraintViolation(double value) {
        this.overallConstraintViolation_ = value;
    }

    public double getOverallConstraintViolation() {
        return this.overallConstraintViolation_;
    }

    public void setNumberOfViolatedConstraint(int value) {
        this.numberOfViolatedConstraints_ = value;
    }

    public int getNumberOfViolatedConstraint() {
        return this.numberOfViolatedConstraints_;
    }

    public void setType(SolutionType type) {
        type_ = type;
    }

    public SolutionType getType() {
        return type_;
    }

    public double getAggregativeValue() {
        double value = 0.0;                
        for (int i = 0; i < getNumberOfObjectives(); i++){
            value += getObjective(i);
        }                
        return value;
    }

    public int getNumberOfBits() {
        int bits = 0;
        for (int i = 0;  i < variable_.length  ; i++)
            if ((variable_[i].getVariableType() == jmetal.encodings.variable.Binary.class) || (variable_[i].getVariableType() == jmetal.encodings.variable.BinaryReal.class))
                bits += ((Binary)(variable_[i])).getNumberOfBits();
        return bits;
    }
    
    public void setNombreImagenResultado(String nombreImagenResultado){
        this.nombreImagenResultado = nombreImagenResultado;
    }
    
    public String getNombreImagenResultado(){
        return nombreImagenResultado;
    }
}