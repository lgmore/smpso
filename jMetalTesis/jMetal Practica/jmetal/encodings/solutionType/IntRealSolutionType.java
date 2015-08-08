package jmetal.encodings.solutionType;

import jmetal.core.Problem;
import jmetal.core.SolutionType;
import jmetal.core.Variable;
import jmetal.encodings.variable.Int;
import jmetal.encodings.variable.Real;

public class IntRealSolutionType extends SolutionType {
    
    private final int cantidadVarInt;
    private final int cantidadVarReal;

    public IntRealSolutionType(Problem problem, int cantidadVariablesInt, int cantidadVariablesReal) {
        super(problem);
        cantidadVarInt = cantidadVariablesInt;
        cantidadVarReal = cantidadVariablesReal;
    }

    @Override
    public Variable[] createVariables() throws ClassNotFoundException {
        Variable [] variables = new Variable[problem_.getNumberOfVariables()];
        for (int var = 0; var < cantidadVarInt; var++)
            variables[var] = new Int((int)problem_.getLowerLimit(var), (int)problem_.getUpperLimit(var));
        for (int var = cantidadVarInt; var < (cantidadVarInt + cantidadVarReal); var++)
            variables[var] = new Real(problem_.getLowerLimit(var), problem_.getUpperLimit(var));
        return variables;
    }
}