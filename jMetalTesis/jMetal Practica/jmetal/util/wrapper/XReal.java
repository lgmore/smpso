package jmetal.util.wrapper;

import jmetal.core.Solution;
import jmetal.core.SolutionType;
import jmetal.util.JMException;

public class XReal {
    
    private Solution solution_;
    private SolutionType type_;

    public XReal() {}

    public XReal(Solution solution) {
        this();
        type_ = solution.getType();
        solution_ = solution;
    }

    public double getValue(int index) throws JMException {
        return solution_.getDecisionVariables()[index].getValue();
    }
    
    public void setValue(int index, double value) throws JMException {
        solution_.getDecisionVariables()[index].setValue(value);
    }

    public double getLowerBound(int index) throws JMException {
        return solution_.getDecisionVariables()[index].getLowerBound();
    }

    public double getUpperBound(int index) throws JMException {
        return solution_.getDecisionVariables()[index].getUpperBound();
    }

    public int getNumberOfDecisionVariables() {
        return solution_.getDecisionVariables().length;
    }

    public int size() {
        return solution_.getDecisionVariables().length;
    }
}