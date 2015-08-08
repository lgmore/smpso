package jmetal.operators.mutation;

import jmetal.core.Operator;
import java.util.HashMap;

public abstract class Mutation extends Operator {

    public Mutation(HashMap<String, Object> parameters) {
        super(parameters);
    }
}