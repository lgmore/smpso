package jmetal.core;

import jmetal.util.Configuration;
import jmetal.util.JMException;
import java.io.Serializable;

public abstract class Variable implements Serializable {

    public abstract Variable deepCopy();

    public double getValue() throws JMException {
        Class cls = java.lang.String.class;
        String name = cls.getName();
        Configuration.logger_.severe("Class " + name + " does not implement " + "method getValue");
        throw new JMException("Exception in " + name + ".getValue()");
    }

    public void setValue(double value) throws JMException {
        Class cls = java.lang.String.class;
        String name = cls.getName();
        Configuration.logger_.severe("Class " + name + " does not implement " + "method setValue");
        throw new JMException("Exception in " + name + ".setValue()");
    }

    public double getLowerBound() throws JMException { 
        Class cls = java.lang.String.class;
        String name = cls.getName(); 
        Configuration.logger_.severe("Class " + name + " does not implement method getLowerBound()");
        throw new JMException("Exception in " + name + ".getLowerBound()");
    }
  
    public double getUpperBound() throws JMException {
        Class cls = java.lang.String.class;
        String name = cls.getName();
        Configuration.logger_.severe("Class " + name + " does not implement method getUpperBound()");
        throw new JMException("Exception in " + name + ".getUpperBound()");
    }

    public void setLowerBound(double lowerBound) throws JMException {
        Class cls = java.lang.String.class;
        String name = cls.getName();
        Configuration.logger_.severe("Class " + name + " does not implement method setLowerBound()");
        throw new JMException("Exception in " + name + ".setLowerBound()");
    }
  
    public void setUpperBound(double upperBound) throws JMException {
        Class cls = java.lang.String.class;
        String name = cls.getName();
        Configuration.logger_.severe("Class " + name + " does not implement method setUpperBound()");
        throw new JMException("Exception in " + name + ".setUpperBound()");
    }

    public Class getVariableType() {
        return this.getClass();
    }
}