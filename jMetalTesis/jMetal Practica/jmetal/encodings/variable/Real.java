package jmetal.encodings.variable;

import jmetal.core.Variable;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

public class Real extends Variable{

    private double value_;//Stores the value of the real encodings.variable
    private double lowerBound_;//Stores the lower bound of the real encodings.variable
    private double upperBound_;//Stores the upper bound of the real encodings.variable

    public Real() {}

    public Real(double lowerBound, double upperBound){
        lowerBound_ = lowerBound;
        upperBound_ = upperBound;
        value_ = PseudoRandom.randDouble()*(upperBound-lowerBound)+lowerBound;        
    }

    public Real(double lowerBound, double upperBound, double value){
        lowerBound_ = lowerBound;
        upperBound_ = upperBound;
        value_ = value;
    }

    public Real(Variable variable) throws JMException{
        lowerBound_ = variable.getLowerBound();
        upperBound_ = variable.getUpperBound();
        value_ = variable.getValue();        
    }

    public double getValue() {
        return value_;
    }

    public void setValue(double value) {
        value_ = value;
    }

    public Variable deepCopy(){
        try {
            return new Real(this);
        } catch (JMException e) {
            Configuration.logger_.severe("Real.deepCopy.execute: JMException");
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
        lowerBound_ = lowerBound;
    }

    public void setUpperBound(double upperBound) {
        upperBound_ = upperBound;
    }

    public String toString(){
        return value_+"";
    }
}