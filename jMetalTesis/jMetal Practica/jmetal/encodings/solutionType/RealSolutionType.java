package jmetal.encodings.solutionType;

import jmetal.core.Problem;
import jmetal.core.SolutionType;
import jmetal.core.Variable;
import jmetal.encodings.variable.Real;

public class RealSolutionType extends SolutionType {

    public RealSolutionType(Problem problem) {
        super(problem);
    }

    public Variable[] createVariables() {
        Variable[] variables = new Variable[problem_.getNumberOfVariables()];
        for (int var = 0; var < problem_.getNumberOfVariables(); var++)
            variables[var] = new Real(problem_.getLowerLimit(var), problem_.getUpperLimit(var)); 
        return variables;
    }
}