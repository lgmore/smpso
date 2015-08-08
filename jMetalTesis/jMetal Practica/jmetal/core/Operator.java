package jmetal.core;

import jmetal.util.JMException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class Operator implements Serializable {
   
    protected final Map<String , Object> parameters_;
    abstract public Object execute(Object object) throws JMException;
    
    public Operator(HashMap<String , Object> parameters) {
        parameters_ = parameters; 
    }
    
    public void setParameter(String name, Object value) {
        parameters_.put(name, value);
    }
  
    public Object getParameter(String name) {
        return parameters_.get(name);
    }
}