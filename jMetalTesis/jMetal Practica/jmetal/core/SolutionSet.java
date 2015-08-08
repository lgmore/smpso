package jmetal.core;

import java.beans.Statement;
import jmetal.util.Configuration;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jmetal.metaheuristics.smpso.SMPSOTesis_main.nombreImagen;

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

    /*public void printObjectivesToFile(String path){
     try {
     FileOutputStream fos = new FileOutputStream(path);
     OutputStreamWriter osw = new OutputStreamWriter(fos);
     BufferedWriter bw = new BufferedWriter(osw);
     //bw.write("Entropía        ----        SSIM");
     bw.write("Entropía*SSIM");
     bw.newLine();
     for (Solution aSolutionsList_ : solutionsList_) {
     bw.write(aSolutionsList_.toString());
     //bw.write(String.valueOf(aSolutionsList_.getIntensidadMedia()));
     bw.newLine();
     }
     bw.close();
     }catch (IOException e) {
     Configuration.logger_.severe("Error acceding to the file");
     e.printStackTrace();
     }
     }*/
    public void printObjectivesToFile(String path) {

        Connection dbConnection = null;
        Statement statement = null;

        String insertTableSQL;

        try {

            FileOutputStream fosE = new FileOutputStream("ENTROPIA");
            FileOutputStream fosS = new FileOutputStream("SSIM");
            FileOutputStream fosP = new FileOutputStream("POND");
            OutputStreamWriter oswE = new OutputStreamWriter(fosE);
            OutputStreamWriter oswS = new OutputStreamWriter(fosS);
            OutputStreamWriter oswP = new OutputStreamWriter(fosP);
            BufferedWriter bwE = new BufferedWriter(oswE);
            BufferedWriter bwS = new BufferedWriter(oswS);
            BufferedWriter bwP = new BufferedWriter(oswP);
            for (Solution aSolutionsList_ : solutionsList_) {

                insertTableSQL = getSentenciaSQL(aSolutionsList_.getDecisionVariables()[0].toString(), //X
                        aSolutionsList_.getDecisionVariables()[1].toString(), //Y
                        aSolutionsList_.getDecisionVariables()[2].toString(), //CLIP
                        aSolutionsList_.getObjective(0), //entropia
                        aSolutionsList_.getObjective(1) //ssim
                );

                // execute insert SQL stetement
                dbConnection = getDBConnection();
                statement = (Statement) dbConnection.prepareStatement(insertTableSQL);
                
                
                System.out.println("Record is inserted into DBUSER table!");

                bwE.write(String.valueOf(aSolutionsList_.getObjective(0)));
                bwS.write(String.valueOf(aSolutionsList_.getObjective(1)));
                bwP.write(String.valueOf(aSolutionsList_.getObjective(0) * aSolutionsList_.getObjective(1)));
                bwE.newLine();
                bwS.newLine();
                bwP.newLine();
            }
            bwE.close();
            bwS.close();
            bwP.close();
            Configuration.logger_.info("cantidad de registros insertados: "+solutionsList_.size());
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(SolutionSet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*public void printVariablesToFile(String path){
     try {
     FileOutputStream fos = new FileOutputStream(path);
     OutputStreamWriter osw = new OutputStreamWriter(fos);
     BufferedWriter bw = new BufferedWriter(osw);
     if (size()>0) {
     int numberOfVariables = solutionsList_.get(0).getDecisionVariables().length;
     bw.write("X    ----    Y    ----    ClipLimit    ----    Nombre Imagen");
     bw.newLine();
     for (Solution aSolutionsList_ : solutionsList_) {
     for (int j = 0; j < numberOfVariables; j++)
     bw.write(aSolutionsList_.getDecisionVariables()[j].toString() + "    ----    ");
     bw.write(aSolutionsList_.getNombreImagenResultado());
     bw.newLine();
     }
     }
     bw.close();
     }catch (IOException e) {
     Configuration.logger_.severe("Error acceding to the file");
     e.printStackTrace();
     }
     }*/
    public void printVariablesToFile(String path) {
        try {
            FileOutputStream fosX = new FileOutputStream("X");
            FileOutputStream fosY = new FileOutputStream("Y");
            FileOutputStream fosClip = new FileOutputStream("CLIP");
            FileOutputStream fosNom = new FileOutputStream("NOM");
            OutputStreamWriter oswX = new OutputStreamWriter(fosX);
            OutputStreamWriter oswY = new OutputStreamWriter(fosY);
            OutputStreamWriter oswClip = new OutputStreamWriter(fosClip);
            OutputStreamWriter oswNom = new OutputStreamWriter(fosNom);
            BufferedWriter bwX = new BufferedWriter(oswX);
            BufferedWriter bwY = new BufferedWriter(oswY);
            BufferedWriter bwClip = new BufferedWriter(oswClip);
            BufferedWriter bwNom = new BufferedWriter(oswNom);
            if (size() > 0) {
                for (Solution aSolutionsList_ : solutionsList_) {
                    bwX.write(aSolutionsList_.getDecisionVariables()[0].toString());
                    bwX.newLine();
                    bwY.write(aSolutionsList_.getDecisionVariables()[1].toString());
                    bwY.newLine();
                    bwClip.write(aSolutionsList_.getDecisionVariables()[2].toString());
                    bwClip.newLine();
                    bwNom.write(aSolutionsList_.getNombreImagenResultado());
                    bwNom.newLine();
                }
            }
            bwX.close();
            bwY.close();
            bwClip.close();
            bwNom.close();
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
            System.out.println("" + solutionsList_.get(i));
        }
    }

    public void setCapacity(int capacity) {
        capacity_ = capacity;
    }

    public int getCapacity() {
        return capacity_;
    }

    private Connection getDBConnection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String getSentenciaSQL(String x, String y, String clip, double entropia, double ssim) {
        String resultado = "INSERT INTO resultados"
                + "(x,y,clip,entropia,nombre,dominado,nrocorrida,ssim) " + "VALUES"
                + "(" + x + "," + y + "," + clip + "," + entropia + "," + nombreImagen + ",'N',0," + ssim + ")";
        return resultado;
    }

}
