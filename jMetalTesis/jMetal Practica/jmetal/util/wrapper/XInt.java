package jmetal.util.wrapper;

import jmetal.core.Solution;
import jmetal.core.SolutionType;
import jmetal.encodings.solutionType.ArrayIntSolutionType;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.encodings.variable.ArrayInt;
import jmetal.util.Configuration;
import jmetal.util.JMException;

public class XInt {
    
    private Solution solution_;
    private SolutionType type_;

    private XInt() {}

    public XInt(Solution solution) {
        this();
        type_ = solution.getType();
        solution_ = solution;
    }

    /**
     * Gets value of a encodings.variable
     * @param index Index of the encodings.variable
     * @return The value of the encodings.variable
     * @throws JMException
     */
    public int getValue(int index) throws JMException {
        if (type_.getClass() == IntSolutionType.class){
            return (int)solution_.getDecisionVariables()[index].getValue();
        } else if (type_.getClass() == ArrayIntSolutionType.class) {
            return ((ArrayInt)(solution_.getDecisionVariables()[0])).array_[index];
        } else Configuration.logger_.severe("jmetal.util.wrapper.XInt.getValue, solution type " + type_ + "+ invalid") ;
        return 0;
    }

    /**
     * Sets the value of a encodings.variable
     * @param index Index of the encodings.variable
     * @param value Value to be assigned
     * @throws JMException
     */
    public void setValue(int index, int value) throws JMException {
        if (type_.getClass() == IntSolutionType.class)
            solution_.getDecisionVariables()[index].setValue(value);
        else if (type_.getClass() == ArrayIntSolutionType.class)
            ((ArrayInt)(solution_.getDecisionVariables()[0])).array_[index]=value;
        else Configuration.logger_.severe("jmetal.util.wrapper.XInt.setValue, solution type " + type_ + "+ invalid") ;		
    }

    /**
     * Gets the lower bound of a encodings.variable
     * @param index Index of the encodings.variable
     * @return The lower bound of the encodings.variable
     * @throws JMException
     */
    public int getLowerBound(int index) throws JMException {
        if (type_.getClass() == IntSolutionType.class)
            return (int)solution_.getDecisionVariables()[index].getLowerBound();
        else if (type_.getClass() == ArrayIntSolutionType.class) 
            return (int)((ArrayInt)(solution_.getDecisionVariables()[0])).getLowerBound(index);
        else Configuration.logger_.severe("jmetal.util.wrapper.XInt.getLowerBound, solution type " + type_ + "+ invalid");
        return 0;
    }

    /**
     * Gets the upper bound of a encodings.variable
     * @param index Index of the encodings.variable
     * @return The upper bound of the encodings.variable
     * @throws JMException
     */
    public int getUpperBound(int index) throws JMException {
        if (type_.getClass() == IntSolutionType.class)		
            return (int)solution_.getDecisionVariables()[index].getUpperBound();
        else if (type_.getClass() == ArrayIntSolutionType.class) 
            return (int)((ArrayInt)(solution_.getDecisionVariables()[0])).getUpperBound(index);
        else Configuration.logger_.severe("jmetal.util.wrapper.XInt.getUpperBound, solution type " + type_ + "+ invalid") ;
        return 0;
    }

    /**
     * Returns the number of variables of the solution
     * @return
     */
    public int getNumberOfDecisionVariables() {
        if (type_.getClass() == IntSolutionType.class)
            return solution_.getDecisionVariables().length;
        else if (type_.getClass() == ArrayIntSolutionType.class)
            return ((ArrayInt)(solution_.getDecisionVariables()[0])).getLength();
        else Configuration.logger_.severe("jmetal.util.wrapper.XInt.size, solution type " + type_ + "+ invalid");
        return 0;
    }
}