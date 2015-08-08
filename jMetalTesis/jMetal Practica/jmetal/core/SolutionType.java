package jmetal.core;

public abstract class SolutionType {
    
    public final Problem problem_;

    public SolutionType(Problem problem) {
        problem_ = problem;
    }

    public abstract Variable[] createVariables() throws ClassNotFoundException;

    public Variable[] copyVariables(Variable[] vars) {
        Variable[] variables;
        variables = new Variable[vars.length];
        for (int var = 0; var < vars.length; var++) {
            variables[var] = vars[var].deepCopy();
        }
        return variables;
    }
}