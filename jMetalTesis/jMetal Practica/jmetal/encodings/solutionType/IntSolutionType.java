package jmetal.encodings.solutionType;

import jmetal.core.Problem;
import jmetal.core.SolutionType;
import jmetal.core.Variable;
import jmetal.encodings.variable.Int;

/**
 * Class representing the solution type of solutions composed of Int variables 
 */
public class IntSolutionType extends SolutionType {

    public IntSolutionType(Problem problem) {
        super(problem);
    }

    public Variable[] createVariables() {
        Variable[] variables = new Variable[problem_.getNumberOfVariables()];
        for (int var = 0; var < problem_.getNumberOfVariables(); var++)
            variables[var] = new Int((int)problem_.getLowerLimit(var), (int)problem_.getUpperLimit(var));
        return variables;
    }
}