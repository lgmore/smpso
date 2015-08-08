package jmetal.problems.tesis;

public class Request {
    
    public int ventanax;
    public int ventanay;
    public double clipLimit;
    
    public Request(int x, int y, double clip){
        ventanax = x;
        ventanay = y;
        clipLimit = clip;
    }
}
