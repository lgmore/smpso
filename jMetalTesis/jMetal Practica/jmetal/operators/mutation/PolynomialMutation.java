package jmetal.operators.mutation;

import jmetal.core.Solution;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.wrapper.XReal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PolynomialMutation extends Mutation {

    private static final double ETA_M_DEFAULT_ = 20.0;
    private final double eta_m_=ETA_M_DEFAULT_;	
    private Double mutationProbability_ = null;
    private Double distributionIndex_ = eta_m_;

    public PolynomialMutation(HashMap<String, Object> parameters) {
        super(parameters);
        if (parameters.get("probability") != null) mutationProbability_ = (Double) parameters.get("probability");
        if (parameters.get("distributionIndex") != null) distributionIndex_ = (Double) parameters.get("distributionIndex");
    }

    public void doMutation(double probability, Solution solution) throws JMException {
        double rnd, delta1, delta2, mut_pow, deltaq;
        double y, yl, yu, val, xy;
        XReal x = new XReal(solution);
        int contador = 0;
        for (int var=0; var < solution.numberOfVariables(); var++) {
            if (PseudoRandom.randDouble() <= probability){
                y = x.getValue(var);
                yl = x.getLowerBound(var);
                yu = x.getUpperBound(var);
                delta1 = (y-yl)/(yu-yl);
                delta2 = (yu-y)/(yu-yl);
                rnd = PseudoRandom.randDouble();
                mut_pow = 1.0/(eta_m_+1.0);
                if (rnd <= 0.5){
                    xy = 1.0-delta1;
                    val = 2.0*rnd+(1.0-2.0*rnd)*(Math.pow(xy,(distributionIndex_+1.0)));
                    deltaq = java.lang.Math.pow(val,mut_pow) - 1.0;
                }else{
                    xy = 1.0-delta2;
                    val = 2.0*(1.0-rnd)+2.0*(rnd-0.5)*(java.lang.Math.pow(xy,(distributionIndex_+1.0)));
                    deltaq = 1.0 - (java.lang.Math.pow(val,mut_pow));
                }
                y = y + deltaq*(yu-yl);
                if (y<yl) y = yl;
                if (y>yu) y = yu;
                if (contador==0) x.setValue(var, (int)y);
                else x.setValue(var, y);
            }
            contador++;
        }
    }

    @Override
    public Object execute(Object object) throws JMException {
        Solution solution = (Solution)object;
        doMutation(mutationProbability_, solution);
        return solution;
    }
}