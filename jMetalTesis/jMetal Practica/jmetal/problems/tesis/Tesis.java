package jmetal.problems.tesis;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.IntRealSolutionType;
import jmetal.metaheuristics.smpso.SMPSOTesis_main;
import jmetal.util.JMException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * @author Marcos Brizuela
 */
public class Tesis extends Problem{

    int cantidadVarInt;
    int cantidadVarReal;
    int filas;
    int columnas;
    int[][] imagen;
    String nombreImagen;
    double ssim;
    
    public Tesis() {
        getImagenOriginal();
        cantidadVarInt = 2;
        cantidadVarReal = 1;
        numeroDeVariables = 3;
        numeroDeObjetivos = 2;
        nombreProblema = "Tesis";
        upperLimit_ = new double[numeroDeVariables];
        lowerLimit_ = new double[numeroDeVariables];
        lowerLimit_[0] = 2;
        upperLimit_[0] = filas/2;
        lowerLimit_[1] = 2;
        upperLimit_[1] = columnas/2;
        lowerLimit_[2] = 0.01;
        upperLimit_[2] = 128;
        tipoSolucion = new IntRealSolutionType(this,2,1);
    }
    
    @Override
    public void evaluate(Solution solution) throws JMException {
        Variable[] variables = solution.getDecisionVariables();
        double [] fx = new double[3];
        fx[0] = variables[0].getValue();
        fx[1] = variables[1].getValue();
        fx[2] = variables[2].getValue();
        int[][] imagenClahe = getCLAHE((int)fx[0],(int)fx[1],fx[2]);
        double entropia = getEntropia(getHistograma(imagenClahe,filas,columnas),filas*columnas);
        solution.setNombreImagenResultado(nombreImagen);
        solution.setObjective(0, entropia);
        solution.setObjective(1, ssim);
    }
    
    /*@Override
    public void evaluate(Solution solution) throws JMException {
        Variable[] variables = solution.getDecisionVariables();
        double [] fx = new double[3];
        fx[0] = variables[0].getValue();
        fx[1] = variables[1].getValue();
        fx[2] = variables[2].getValue();
        int[][] imagenClahe = getCLAHE((int)fx[0],(int)fx[1],fx[2]);
        double entropia = getEntropia(getHistograma(imagenClahe,filas,columnas),filas*columnas);
        solution.setNombreImagenResultado(nombreImagen);
        solution.setObjective(0, entropia*ssim);
    }*/
    
    private int[] getHistograma(int[][] imagen, int fila, int columna){
        int[] histograma = new int[256];
        for (int i=0; i<256; i++){
            histograma[i] = 0;
        }
        for (int i=0; i<fila; i++){
            for (int j=0; j<columna; j++){
                histograma[imagen[i][j]] = histograma[imagen[i][j]]+1;
            }
        }
        return histograma;
    }
    
    private double getEntropia(int[] histograma, int totalPixeles){
        double entropia = 0;
        for (int i=0; i<256; i++){
            double probabilidad = (double) histograma[i]/totalPixeles;
            if (probabilidad==0) entropia=0;
            else entropia += -(probabilidad*(Math.log(probabilidad)/Math.log(2)));
        }
        return entropia;
    }
    
    private void getImagenOriginal(){
        try{
            HttpClient client = new DefaultHttpClient();
            String puertoimagenoriginal=String.valueOf(SMPSOTesis_main.puertoclahe+1);
            HttpPost post = new HttpPost("http://localhost:"+puertoimagenoriginal+"/json");
            //HttpPost post = new HttpPost("http://localhost:8081/json");
            Gson gson = new Gson();
            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            Response resp = gson.fromJson(rd, Response.class);
            imagen = new int[resp.filas][resp.columnas];
            int index=0;
            for (int i=0; i<resp.filas; i++){
                for (int j=0; j<resp.columnas; j++){
                    imagen[i][j] = resp.valores[index];
                    index++;
                }
            }
            filas = resp.filas;
            columnas = resp.columnas;
        }catch(Exception e){}
    }
    
    private int[][] getCLAHE(int ventanaX, int ventanaY, double clipLimit){
        try{
            HttpClient client = new DefaultHttpClient();
            Request solicitud = new Request(ventanaX, ventanaY, clipLimit);
            String puertoimagenoriginal=String.valueOf(SMPSOTesis_main.puertoclahe);
            HttpPost post = new HttpPost("http://localhost:"+puertoimagenoriginal+"/json");
            //HttpPost post = new HttpPost("http://localhost:8080/json");
            Gson gson = new Gson();
            String test = gson.toJson(solicitud, Request.class);
            StringEntity input = new StringEntity(test);
            input.setContentType("application/json");
            post.setEntity(input);
            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            Response resp = gson.fromJson(rd, Response.class);
            int[][] matrizImagen = new int[resp.filas][resp.columnas];
            int index=0;
            for (int i=0; i<resp.filas; i++){
                for (int j=0; j<resp.columnas; j++){
                    matrizImagen[i][j] = resp.valores[index];
                    index++;
                }
            }
            ssim = resp.ssim;
            nombreImagen = resp.nombreresultado;
            return matrizImagen;
        }catch(Exception e){}
        return null;
    }
}