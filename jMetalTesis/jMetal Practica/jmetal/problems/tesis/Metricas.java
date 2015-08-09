/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmetal.problems.tesis;

/**
 *
 * @author Estefanis
 */
public class Metricas {

    private double entropia;
    private double contraste;

    public Metricas(double entropia, double contraste) {
        this.entropia = entropia;
        this.contraste = contraste;
    }

    @Override
    public String toString() {
        return "Metricas{" + "entropia=" + getEntropia() + ", contraste=" + getContraste() + '}';
    }

    /**
     * @return the entropia
     */
    public double getEntropia() {
        return entropia;
    }

    /**
     * @param entropia the entropia to set
     */
    public void setEntropia(double entropia) {
        this.entropia = entropia;
    }

    /**
     * @return the contraste
     */
    public double getContraste() {
        return contraste;
    }

    /**
     * @param contraste the contraste to set
     */
    public void setContraste(double contraste) {
        this.contraste = contraste;
    }

}
