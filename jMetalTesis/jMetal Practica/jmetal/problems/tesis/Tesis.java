package jmetal.problems.tesis;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.IntRealSolutionType;
import jmetal.metaheuristics.smpso.SMPSOTesis;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import matlabcontrol.MatlabInvocationException;
import org.apache.log4j.Logger;

/**
 * @author Marcos Brizuela
 */
public class Tesis extends Problem {

    private static final Logger log = Logger.getLogger(Tesis.class.getName());

    int filas;
    int columnas;
    String nombreImagen;

    public Tesis() {

        upperLimit_ = new double[numeroDeVariables];
        lowerLimit_ = new double[numeroDeVariables];
        //dim
        lowerLimit_[0] = Configuration.dimMin;
        upperLimit_[0] = Configuration.dimMax;
        //n
        lowerLimit_[1] = Configuration.nMin;
        upperLimit_[1] = Configuration.mMax -1;

        //type
        lowerLimit_[2] = Configuration.typeMin;
        upperLimit_[2] = Configuration.typeMax;

        //alfa
        lowerLimit_[3] = Configuration.alfaMin;
        upperLimit_[3] = Configuration.alfaMax;

        tipoSolucion = new IntRealSolutionType(this, cantidadVarInt, cantidadVarReal);
    }

    @Override
    public void evaluate(Solution solution) throws JMException {
        Variable[] variables = solution.getDecisionVariables();
        double[] fx = new double[4];
        fx[0] = variables[0].getValue();
        fx[1] = variables[1].getValue();
        fx[2] = variables[2].getValue();
        fx[3] = variables[3].getValue();

        Metricas resultado = null;
        try {
            resultado = getMCL(fx);
        } catch (MatlabInvocationException ex) {
            log.error("error: " + ex.getMessage());
        }

        solution.setNombreImagenResultado(nombreImagen);
        solution.setObjective(0, resultado.getContraste());
        solution.setObjective(1, resultado.getEntropia());
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
//    
//    private int[] getHistograma(int[][] imagen, int fila, int columna){
//        int[] histograma = new int[256];
//        for (int i=0; i<256; i++){
//            histograma[i] = 0;
//        }
//        for (int i=0; i<fila; i++){
//            for (int j=0; j<columna; j++){
//                histograma[imagen[i][j]] = histograma[imagen[i][j]]+1;
//            }
//        }
//        return histograma;
//    }
//    
//    private double getEntropia(int[] histograma, int totalPixeles){
//        double entropia = 0;
//        for (int i=0; i<256; i++){
//            double probabilidad = (double) histograma[i]/totalPixeles;
//            if (probabilidad==0) entropia=0;
//            else entropia += -(probabilidad*(Math.log(probabilidad)/Math.log(2)));
//        }
//        return entropia;
//    }
//    private int[][] getCLAHE(int ventanaX, int ventanaY, double clipLimit){
//        try{
//            HttpClient client = new DefaultHttpClient();
//            Request solicitud = new Request(ventanaX, ventanaY, clipLimit);
//            String puertoimagenoriginal=String.valueOf(SMPSOTesis_main.puertoclahe);
//            HttpPost post = new HttpPost("http://localhost:"+puertoimagenoriginal+"/json");
//            //HttpPost post = new HttpPost("http://localhost:8080/json");
//            Gson gson = new Gson();
//            String test = gson.toJson(solicitud, Request.class);
//            StringEntity input = new StringEntity(test);
//            input.setContentType("application/json");
//            post.setEntity(input);
//            HttpResponse response = client.execute(post);
//            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//            Response resp = gson.fromJson(rd, Response.class);
//            int[][] matrizImagen = new int[resp.filas][resp.columnas];
//            int index=0;
//            for (int i=0; i<resp.filas; i++){
//                for (int j=0; j<resp.columnas; j++){
//                    matrizImagen[i][j] = resp.valores[index];
//                    index++;
//                }
//            }
//            ssim = resp.ssim;
//            nombreImagen = resp.nombreresultado;
//            return matrizImagen;
//        }catch(Exception e){}
//        return null;
//    }
    private Metricas getMCL(double[] fx) throws MatlabInvocationException {
        //String strInvocacion = "[e , c] = testMCLV2('mdb001.pgm',1,2,0.5,1,10)";
        StringBuilder builder = new StringBuilder();

        builder.append("[e , c] = testMCLV2('").append(Configuration.nombreImagen)
                .append("',")
                .append((int) fx[1]).append(",")
                .append(Configuration.mMax).append(",")
                .append(fx[3]).append(",")
                .append((int) fx[2]).append(",")
                .append((int) fx[0]).append(")");

        Metricas resultados = run(builder.toString());

        return resultados;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Metricas run(String comando) throws MatlabInvocationException {
        Metricas metricas = null;
        SMPSOTesis.proxy.setVariable("e", 0);
        SMPSOTesis.proxy.setVariable("c", 0);
        SMPSOTesis.proxy.eval(comando);
        double entropia = ((double[]) SMPSOTesis.proxy.getVariable("e"))[0];
        double contraste = ((double[]) SMPSOTesis.proxy.getVariable("c"))[0];
        metricas = new Metricas(entropia, contraste);
        return metricas;
    }

}
