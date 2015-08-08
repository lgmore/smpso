package jmetal.metaheuristics.smpso;

import jmetal.core.*;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.archive.CrowdingArchive;
import jmetal.util.comparators.CrowdingDistanceComparator;
import jmetal.util.comparators.DominanceComparator;
import jmetal.util.wrapper.XReal;
import java.io.IOException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SMPSOTesis extends Algorithm {

    private int tamanhoEnjambre;//numero de particulas usadas
    private int tamanhoLideres;//tamaño maximo del archivo
    private int maximoIteraciones;//numero maximo de iteraciones
    private int iteracion;//numero de iteracion actual
    private SolutionSet particulas;//particulas
    private Solution[] mejoresLocales;//soluciones locales
    private CrowdingArchive mejoresGlobales;//lideres del enjambre (mejores globales)
    private double[][] matrizVelocidad;//guarda la velocidad de cada particula
    private Comparator dominance_;//comparador para verificar dominancia
    private Comparator crowdingDistanceComparator_;//Stores a comparator for crowding checking
    private Distance distance_;//Stores a <code>Distance</code> object
    private Operator mutacion;//operador para mutacion no uniforme
    double r1Max_;//maximo del valor aleatorio 1
    double r1Min_;//minimo del valor aleatorio 1
    double r2Max_;//maximo del valor aleatorio 2
    double r2Min_;//minimo del valor aleatorio 2
    double C1Max_;//maximo para constante de aceleracion 1
    double C1Min_;//minimo para constante de aceleracion 1
    double C2Max_;//maximo para constante de aceleracion 2
    double C2Min_;//minimo para constante de aceleracion 2
    double WMax_;//maximo para factor de inercia
    double WMin_;//minimo para factor de inercia
    double ChVel1_;
    double ChVel2_;
    private double velocidadMaxima[];//maximo para velocidad
    private double velocidadMinima[];//minimo para velocidad
    boolean exito;//bandera de exito del algoritmo

    public SMPSOTesis(Problem problema) {
        super(problema);
        r1Max_ = 1.0;//numero aleatorio 1 maximo 1
        r1Min_ = 0.0;//numero aleatorio 1 minimo 0
        r2Max_ = 1.0;//numero aleatorio 2 maximo 1
        r2Min_ = 0.0;//numero aleatorio 2 minimo 0
        C1Max_ = 2.5;//constante de aceleracion 1 maximo 2.5
        C1Min_ = 1.5;//constante de aceleracion 1 minimo 1.5
        C2Max_ = 2.5;//constante de aceleracion 2 maximo 2.5
        C2Min_ = 1.5;//constante de aceleracion 2 minimo 1.5
        WMax_ = 4;//Wmax y Wmin son iguales en este problema, W es constante
        WMin_ = 0.1;
        ChVel1_ = -1;
        ChVel2_ = -1;
    }

    public void iniciarParametros() {
        tamanhoEnjambre = ((Integer) getInputParameter("tamanhoEnjambre")).intValue();
        tamanhoLideres = ((Integer) getInputParameter("tamanhoLideres")).intValue();
        maximoIteraciones = ((Integer) getInputParameter("maximoIteraciones")).intValue();
        mutacion = operators_.get("mutation");
        iteracion = 0;
        exito = false;
        particulas = new SolutionSet(tamanhoEnjambre);//coleccion de particulas
        mejoresLocales = new Solution[tamanhoEnjambre];//vector de mejores locales
        mejoresGlobales = new CrowdingArchive(tamanhoLideres, problem_.getNumberOfObjectives());//lideres global best
        // Create comparators for dominance and crowding distance
        dominance_ = new DominanceComparator();
        crowdingDistanceComparator_ = new CrowdingDistanceComparator();
        distance_ = new Distance();
        matrizVelocidad = new double[tamanhoEnjambre][problem_.getNumberOfVariables()];//matriz de velocidad de las particulas
        velocidadMaxima = new double[problem_.getNumberOfVariables()];//maximo de velocidad
        velocidadMinima = new double[problem_.getNumberOfVariables()];//minimo de velocidad
        velocidadMaxima[0] = (problem_.getUpperLimit(0) - problem_.getLowerLimit(0)) / 2;
        velocidadMinima[0] = -velocidadMaxima[0];
        velocidadMaxima[1] = (problem_.getUpperLimit(1) - problem_.getLowerLimit(1)) / 2;
        velocidadMinima[1] = -velocidadMaxima[1];
        velocidadMaxima[2] = (problem_.getUpperLimit(2) - problem_.getLowerLimit(2)) / 2.0;
        velocidadMinima[2] = -velocidadMaxima[2];
    }

    //factor de inercia, puede retornar algun calculo de w, o simplemente una w constante
    private double factorInercia(int iter, int miter, double wma, double wmin) {
        //return wma - ((double)iter*(wma - wmin))/miter;//W es variable y depende del numero de iteracion actual
        return wma;//W es constante
    }
    
    // constriction coefficient (M. Clerc) recibe las constantes de aprendizaje
    private double restringirAceleracion(double c1, double c2) {
        double rho = c1 + c2;
        if (rho <= 4) return 1.0;//si la suma de las constantes es menor o igual a 4 retorna 1
        else return 2 / (2 - rho - Math.sqrt(Math.pow(rho, 2.0) - 4.0 * rho));//sino, 2/(2-(c1+c2)-raiz cuadrada de [(c1+c2) al cuadrado - 4*(c1+c2)])
    }

    // velocity bounds, recibe velocidad, maximo y minimo, el indice de la variable de la particula y el indice de la particula (de la matriz de velocidad)
    private double restringirVelocidad(double velocidad, double[] velMaxima, double[] velMinima, int variableIndex, int particleIndex) throws IOException {
        double resultado;
        double maximo = velMaxima[variableIndex];
        double minimo = velMinima[variableIndex];
        resultado = velocidad;
        if (velocidad > maximo) resultado = maximo;//este es el limite maximo de velocidad, si se supera, se retorna el delta max
        if (velocidad < minimo) resultado = minimo;//igual q max pero para limite minimo..
        return resultado;
    }

    //Update the speed of each particle, acotada por un limite maximo y uno minimo, aqui el numero de iteracion y el maximo de iteracion solo se usa para el calculo de w
    private void calcularVelocidad(int numeroIteracion, int maximoIteraciones) throws JMException, IOException {
        double r1, r2, C1, C2;
        double wmax, wmin;
        XReal mejorGlobal;
        for (int i = 0; i < tamanhoEnjambre; i++) {//por cada particula hacemos el calculo de velocidad
            XReal particula = new XReal(particulas.get(i));//la particula en cuestion
            XReal mejorLocal = new XReal(mejoresLocales[i]);//el mejor valor que ha visto la particula en cuestion hasta ahora (local best para la particula)
            //se seleccionan dos lideres aleatoriamente, el que de valor menor, por minimizacion de objetivos es el mas apto para global best
            int pos1 = PseudoRandom.randInt(0, mejoresGlobales.size() - 1);
            int pos2 = PseudoRandom.randInt(0, mejoresGlobales.size() - 1);
            Solution candidato1 = mejoresGlobales.get(pos1);//lider uno random, candidato a global best
            Solution candidato2 = mejoresGlobales.get(pos2);//lider dos random, candidato a global best
            if (crowdingDistanceComparator_.compare(candidato1, candidato2) < 1) {//si da -1 o 0, la distancia de one es mayor o igual a two
                mejorGlobal = new XReal(candidato1);//one es global best si su distancia es mayor
            } else {//aca entra si sale 1, la distancia de two es mayor a la de one
                mejorGlobal = new XReal(candidato2);//two es global best si su distancia es mayor
            }
            r1 = PseudoRandom.randDouble(r1Min_, r1Max_);//r1 constantes aleatorias
            r2 = PseudoRandom.randDouble(r2Min_, r2Max_);//r2 constantes aleatorias
            C1 = PseudoRandom.randDouble(C1Min_, C1Max_);//C1 constante de aprendizaje o aceleracion
            C2 = PseudoRandom.randDouble(C2Min_, C2Max_);//C2 constante de aprendizaje o aceleracion
            //W = PseudoRandom.randDouble(WMin_, WMax_);//factor de inercia
            wmax = WMax_;//wmax limite superior de factor de inercia (opcional)
            wmin = WMin_;//wmin limite inferior de factor de inercia (opcional)
            matrizVelocidad[i][0] = (int)restringirVelocidad(restringirAceleracion(C1,C2)*(factorInercia(numeroIteracion,maximoIteraciones,wmax,wmin)*matrizVelocidad[i][0]+C1*r1*(mejorLocal.getValue(0)-particula.getValue(0))+C2*r2*(mejorGlobal.getValue(0)-particula.getValue(0))),velocidadMaxima,velocidadMinima,0,i);
            matrizVelocidad[i][1] = (int)restringirVelocidad(restringirAceleracion(C1,C2)*(factorInercia(numeroIteracion,maximoIteraciones,wmax,wmin)*matrizVelocidad[i][1]+C1*r1*(mejorLocal.getValue(1)-particula.getValue(1))+C2*r2*(mejorGlobal.getValue(1)-particula.getValue(1))),velocidadMaxima,velocidadMinima,1,i);
            matrizVelocidad[i][2] = restringirVelocidad(restringirAceleracion(C1,C2)*(factorInercia(numeroIteracion,maximoIteraciones,wmax,wmin)*matrizVelocidad[i][2]+C1*r1*(mejorLocal.getValue(2)-particula.getValue(2))+C2*r2*(mejorGlobal.getValue(2)-particula.getValue(2))),velocidadMaxima,velocidadMinima,2,i);
        }
    }

    //Update the position of each particle, el valor de la particula se cambia aqui, se le suma al valor de la particula la velocidad de la particula
    private void calcularNuevasPosiciones() throws JMException {
        for (int i = 0; i < tamanhoEnjambre; i++) {//por cada particula del swarm
            XReal particle = new XReal(particulas.get(i));//se trae la particula
            for (int var = 0; var < particle.getNumberOfDecisionVariables(); var++) {// por cada variable de la particula
                particle.setValue(var, particle.getValue(var) + matrizVelocidad[i][var]);//pone el valor de la particula en el indice de la variable de desicion
                if (particle.getValue(var) < problem_.getLowerLimit(var)) {//si el valor es menor al limite inferior, se setea dicho limite y se modifica la velocidad de nuevo
                    particle.setValue(var, problem_.getLowerLimit(var));
                    matrizVelocidad[i][var] = matrizVelocidad[i][var] * ChVel1_;
                }
                if (particle.getValue(var) > problem_.getUpperLimit(var)) {//igual que el minimo pero para el limite maximo
                    particle.setValue(var, problem_.getUpperLimit(var));
                    matrizVelocidad[i][var] = matrizVelocidad[i][var] * ChVel2_;
                }
            }
        }
    }

    //Apply a mutation operator to some particles in the swarm, puede o no darse la mutacion
    private void mutar(int actualIteration, int totalIterations) throws JMException {
        for (int i = 0; i < particulas.size(); i++) {//por cada particula se puede hacer una mutacion o turbulencia
            if ((i % 6) == 0) mutacion.execute(particulas.get(i));// si se cumple, se muta
        }
    }

    @Override
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        iniciarParametros();
        System.out.println("Empezó: "+Calendar.getInstance().getTime().toString());
        //->Step 1 (and 3) poblacion inicial, creamos cada una de las particulas con un valor aleatorio para cada una
        for (int i = 0; i < tamanhoEnjambre; i++) {
            Solution particula = new Solution(problem_);//crea la particula con valores random en sus variables, del tipo de solucion de la particula, para este caso es "Real"
            problem_.evaluate(particula);//evaluacion de la particula, pondra valores en sus objetivos
            particulas.add(particula);//se añade al enjambre
            System.out.println("Particula "+i+". Entropia: "+particula.getObjective(0)+". SSIM: "+particula.getObjective(1)+". E*SSIM="+particula.getObjective(0)*particula.getObjective(1));
            System.out.println("Particula "+i+". X: "+particula.getDecisionVariables()[0].toString()+". Y: "+particula.getDecisionVariables()[1].toString()+". Clip: "+particula.getDecisionVariables()[2].toString());
            //System.out.println("Particula "+i+". Entropia*SSIM: "+particula.getObjective(0));
        }
        //-> Step2. iniciar velocidad de cada particula a 0. Matriz cantidad de particulas x cantidad de variables
        for (int i = 0; i < tamanhoEnjambre; i++) {//por cada particula
            for (int j = 0; j < problem_.getNumberOfVariables(); j++) {//por cada variable
                matrizVelocidad[i][j] = 0.0;
            }
        }
        // Step4 and 5. lideres iniciales. archivo de global best inicial, solo entraran los dominantes (minimizacion es este problema, por lo tanto solo entran los que son menores en valor)
        for (int i = 0; i < particulas.size(); i++) {//por cada particula del swarm iniciamos a los que seran lideres desde el inicio
            Solution particula = new Solution(particulas.get(i));//creamos un posible lider con la particula
            mejoresGlobales.add(particula);//solo se añade al archivo si la particula es dominante respecto a las que ya estan en el archivo de lideres
        }
        //-> Step 6. Initialize the memory of each particle, al principio todos son mejores locales
        for (int i = 0; i < particulas.size(); i++) {
            Solution particula = new Solution(particulas.get(i));
            mejoresLocales[i] = particula;
        }
        //Crowding the mejoresGlobales, distancia entre los lideres, se calcula en base a los valores de sus objetivos y los de sus vecinos, ordenados de forma ascendente
        distance_.crowdingDistanceAssignment(mejoresGlobales, problem_.getNumberOfObjectives());
        //-> Step 7. Iteraciones
        while (iteracion < maximoIteraciones) {
            System.out.println("Iteración: "+iteracion+", "+Calendar.getInstance().getTime().toString());
            try {
                calcularVelocidad(iteracion, maximoIteraciones);
            } catch (IOException ex) {
                Logger.getLogger(SMPSOTesis.class.getName()).log(Level.SEVERE, null, ex);
            }
            calcularNuevasPosiciones();
            //mutar(iteracion, maximoIteraciones);
            //evaluacion de las particulas con los nuevos valores
            for (int i = 0; i < particulas.size(); i++) {//por cada particula
                Solution particula = particulas.get(i);
                problem_.evaluate(particula);//evaluar funciones del problema
                System.out.println("Particula "+i+". Entropia: "+particula.getObjective(0)+". SSIM: "+particula.getObjective(1)+". E*SSIM="+particula.getObjective(0)*particula.getObjective(1));
                System.out.println("Particula "+i+". X: "+particula.getDecisionVariables()[0].toString()+". Y: "+particula.getDecisionVariables()[1].toString()+". Clip: "+particula.getDecisionVariables()[2].toString());
                //System.out.println("Particula "+i+". Entropia*SSIM: "+particula.getObjective(0));
            }
            //Actualizar el archivo de lideres no dominados, solo entran los q no son dominados, los dominados se van quitando de la lista
            for (int i = 0; i < particulas.size(); i++) {
                Solution particula = new Solution(particulas.get(i));
                mejoresGlobales.add(particula);
            }
            //Actualizar los mejores locales que ven cada una de las particulas (valores anteriores)
            for (int i = 0; i < particulas.size(); i++) {
                int flag = dominance_.compare(particulas.get(i), mejoresLocales[i]);//compara el nuevo valor de la particula con el valor local mejor que venia actualizando
                if (flag != 1) {//si es 1, best[i] domina a la particula, sino, son iguales o la particula domina a best[i], solo debe entrar en caso que best[i] es dominada por la particula o sean iguales
                    Solution particula = new Solution(particulas.get(i));
                    mejoresLocales[i] = particula;
                }
            }
            //se vuelve a computar la distancia entre los lideres
            distance_.crowdingDistanceAssignment(mejoresGlobales, problem_.getNumberOfObjectives());
            iteracion++;//aumentamos la iteracion
        }
        return mejoresGlobales;
    }
}