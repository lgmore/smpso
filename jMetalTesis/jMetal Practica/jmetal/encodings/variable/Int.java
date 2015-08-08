package jmetal.encodings.variable;

import jmetal.core.Variable;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

public class Int extends Variable {
	
    private int value_;
    private int lowerBound_;
    private int upperBound_;

    public Int() {
        lowerBound_ = java.lang.Integer.MIN_VALUE;
        upperBound_ = java.lang.Integer.MAX_VALUE;
        value_ = 0;
    }

    public Int(int lowerBound, int upperBound){
        lowerBound_ = lowerBound;
        upperBound_ = upperBound;
        value_ = PseudoRandom.randInt(lowerBound, upperBound);
    }

    public Int(int value, int lowerBound, int upperBound) {
        super();
        value_ = value;
        lowerBound_ = lowerBound;
        upperBound_ = upperBound;
    }

    public Int(Variable variable) throws JMException{
        lowerBound_ = (int)variable.getLowerBound();
        upperBound_ = (int)variable.getUpperBound();
        value_ = (int)variable.getValue();        
    }

    public double getValue() {
        return value_;
    }

    public void setValue(double value) {
        value_ = (int)value;
    }

    public Variable deepCopy(){
        try {
            return new Int(this);
        } catch (JMException e) {
            Configuration.logger_.severe("Int.deepCopy.execute: JMException");
            return null;
        }
    }

    public double getLowerBound() {
        return lowerBound_;
    }

    public double getUpperBound() {
        return upperBound_;
    }

    public void setLowerBound(double lowerBound)  {
        lowerBound_ = (int)lowerBound;
    }

    public void setUpperBound(double upperBound) {
        upperBound_ = (int)upperBound;
    }

    public String toString(){
        return value_+"";
    }
}