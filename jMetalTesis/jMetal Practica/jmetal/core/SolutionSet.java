package jmetal.core;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.util.Configuration;

public class SolutionSet implements Serializable {

    protected final List<Solution> solutionsList_;// Stores a list of <code>solution</code> objects
    private int capacity_ = 0; //Maximum size of the solution set 

    public SolutionSet() {
        solutionsList_ = new ArrayList<Solution>();
    }

    public SolutionSet(int maximumSize) {
        solutionsList_ = new ArrayList<Solution>();
        capacity_ = maximumSize;
    }

    public boolean add(Solution solution) {
        if (solutionsList_.size() == capacity_) {
            Configuration.logger_.severe("The population is full");
            Configuration.logger_.severe("Capacity is : " + capacity_);
            Configuration.logger_.severe("\t Size is: " + this.size());
            return false;
        }
        solutionsList_.add(solution);
        return true;
    }

    public boolean add(int index, Solution solution) {
        solutionsList_.add(index, solution);
        return true;
    }

    public Solution get(int i) {
        if (i >= solutionsList_.size()) {
            throw new IndexOutOfBoundsException("Index out of Bound " + i);
        }
        return solutionsList_.get(i);
    }

    public int getMaxSize() {
        return capacity_;
    }

    public void sort(Comparator comparator) {
        if (comparator == null) {
            Configuration.logger_.severe("No criterium for comparing exist");
            return;
        }
        Collections.sort(solutionsList_, comparator);
    }

    int indexBest(Comparator comparator) {
        if ((solutionsList_ == null) || (this.solutionsList_.isEmpty())) {
            return -1;
        }
        int index = 0;
        Solution bestKnown = solutionsList_.get(0), candidateSolution;
        int flag;
        for (int i = 1; i < solutionsList_.size(); i++) {
            candidateSolution = solutionsList_.get(i);
            flag = comparator.compare(bestKnown, candidateSolution);
            if (flag == +1) {
                index = i;
                bestKnown = candidateSolution;
            }
        }
        return index;
    }

    public Solution best(Comparator comparator) {
        int indexBest = indexBest(comparator);
        if (indexBest < 0) {
            return null;
        } else {
            return solutionsList_.get(indexBest);
        }
    }

    public int indexWorst(Comparator comparator) {
        if ((solutionsList_ == null) || (this.solutionsList_.isEmpty())) {
            return -1;
        }
        int index = 0;
        Solution worstKnown = solutionsList_.get(0), candidateSolution;
        int flag;
        for (int i = 1; i < solutionsList_.size(); i++) {
            candidateSolution = solutionsList_.get(i);
            flag = comparator.compare(worstKnown, candidateSolution);
            if (flag == -1) {
                index = i;
                worstKnown = candidateSolution;
            }
        }
        return index;
    }

    public Solution worst(Comparator comparator) {
        int index = indexWorst(comparator);
        if (index < 0) {
            return null;
        } else {
            return solutionsList_.get(index);
        }
    }

    public int size() {
        return solutionsList_.size();
    }

    public void printObjectivesToFile() {
        try {
            Configuration.logger_.info("Cargando el archivo OBJETIVO...");
            FileOutputStream fosE = new FileOutputStream("OBJETIVOS");
            OutputStreamWriter oswE = new OutputStreamWriter(fosE);
            BufferedWriter bwE = new BufferedWriter(oswE);
            for (Solution aSolutionsList_ : solutionsList_) {
                bwE.write(String.valueOf(aSolutionsList_.getObjective(0)) + "|"
                        + String.valueOf(aSolutionsList_.getObjective(1)));
                bwE.newLine();
            }
            bwE.close();
            Configuration.logger_.info("Cargado el archivo OBJETIVO");
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    }

    public void insertToBD() {
        Connection dbConnection = null;
        Statement statement = null;
        try {
            Class.forName(Configuration.bdDriver);
            dbConnection = (Connection) DriverManager.getConnection(Configuration.bdConexion, Configuration.bdUser, Configuration.bdPass);
            statement = (Statement) dbConnection.createStatement();
            for (Solution aSolutionsList_ : solutionsList_) {
                // int n, int m, double alfa, int type, int dim, double entropia, double contraste
                String insertTableSQL = getSentenciaSQL(
                        aSolutionsList_.getDecisionVariables()[1].toString(), //n
                        Configuration.mMax + "", // m
                        aSolutionsList_.getDecisionVariables()[0].toString(), //alfa
                        aSolutionsList_.getDecisionVariables()[2].toString(), //type
                        aSolutionsList_.getDecisionVariables()[3].toString(), //dim
                        aSolutionsList_.getObjective(1) + "", //entropia
                        aSolutionsList_.getObjective(0) + "" //contraste
                );
                Configuration.logger_.info(insertTableSQL);                
                statement.executeUpdate(insertTableSQL);

            }
            Configuration.logger_.info("cantidad de registros insertados: " + solutionsList_.size());
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        } catch (ClassNotFoundException ex) {
            Configuration.logger_.severe("Error en la conexion con la bd " + ex.getMessage());
        } catch (SQLException ex) {
            Configuration.logger_.severe("Error en la conexion con la bd " + ex.getMessage());
        }
    }

    public void printVariablesToFile() {
        try {
            Configuration.logger_.info("Cargando el archivo VARIABLES...");
            FileOutputStream fosE = new FileOutputStream("VARIABLES");
            OutputStreamWriter oswE = new OutputStreamWriter(fosE);
            BufferedWriter bwE = new BufferedWriter(oswE);
            for (Solution aSolutionsList_ : solutionsList_) {
                bwE.write(aSolutionsList_.getDecisionVariables()[1].toString() + "|" + //n
                        Configuration.mMax + "|" + // m
                        aSolutionsList_.getDecisionVariables()[0].toString() + "|" + //alfa
                        aSolutionsList_.getDecisionVariables()[2].toString() + "|" +//type
                        aSolutionsList_.getDecisionVariables()[3].toString() //dim
                ); //contraste)
                bwE.newLine();
            }
            bwE.close();
            Configuration.logger_.info("Cargado el archivo VARIABLES");
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    }

    public void printFeasibleFUN(String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            for (Solution aSolutionsList_ : solutionsList_) {
                if (aSolutionsList_.getOverallConstraintViolation() == 0.0) {
                    bw.write(aSolutionsList_.toString());
                    bw.newLine();
                }
            }
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    }

    public void printFeasibleVAR(String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            if (size() > 0) {
                int numberOfVariables = solutionsList_.get(0).getDecisionVariables().length;
                for (Solution aSolutionsList_ : solutionsList_) {
                    if (aSolutionsList_.getOverallConstraintViolation() == 0.0) {
                        for (int j = 0; j < numberOfVariables; j++) {
                            bw.write(aSolutionsList_.getDecisionVariables()[j].toString() + " ");
                        }
                        bw.newLine();
                    }
                }
            }
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
    }

    public void clear() {
        solutionsList_.clear();
    }

    public void remove(int i) {
        if (i > solutionsList_.size() - 1) {
            Configuration.logger_.severe("Size is: " + this.size());
        }
        solutionsList_.remove(i);
    }

    public Iterator<Solution> iterator() {
        return solutionsList_.iterator();
    }

    public SolutionSet union(SolutionSet solutionSet) {
        int newSize = this.size() + solutionSet.size();
        if (newSize < capacity_) {
            newSize = capacity_;
        }
        SolutionSet union = new SolutionSet(newSize);
        for (int i = 0; i < this.size(); i++) {
            union.add(this.get(i));
        }
        for (int i = this.size(); i < (this.size() + solutionSet.size()); i++) {
            union.add(solutionSet.get(i - this.size()));
        }
        return union;
    }

    public void replace(int position, Solution solution) {
        if (position > this.solutionsList_.size()) {
            solutionsList_.add(solution);
        }
        solutionsList_.remove(position);
        solutionsList_.add(position, solution);
    }

    public double[][] writeObjectivesToMatrix() {
        if (this.size() == 0) {
            return null;
        }
        double[][] objectives;
        objectives = new double[size()][get(0).getNumberOfObjectives()];
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < get(0).getNumberOfObjectives(); j++) {
                objectives[i][j] = get(i).getObjective(j);
            }
        }
        return objectives;
    }

    public void printObjectives() {
        for (int i = 0; i < solutionsList_.size(); i++) {
            Configuration.logger_.info("" + solutionsList_.get(i));
        }
    }

    public void setCapacity(int capacity) {
        capacity_ = capacity;
    }

    public int getCapacity() {
        return capacity_;
    }

    private String getSentenciaSQL(String n, String m, String alfa, String type, String dim, String entropia, String contraste) {
        String resultado = "INSERT INTO resultados"
                + "(n, m , alfa, type, dim, entropia, contraste, nombre, dominado, nrocorrida) " + "VALUES"
                + "(" + n + "," + m + "," + alfa + "," + type + "," + dim + "," + entropia + "," + contraste + ",'" + Configuration.nombreImagen + "','N',0)";
        return resultado;
    }

}
