//  Configuration.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
package jmetal.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * This class contain types and constant definitions
 */
public class Configuration implements Serializable {

    /**
     * Logger object
     */
    public static Logger logger_ = Logger.getLogger("jMetal");

    public static int cantidadParticulas;
    public static int cantidadLideres;
    public static int cantidadIteraciones;
    public static String bdConexion;
    public static String bdUser;
    public static String bdPass;
    public static String bdDriver;
    public static String nombreImagen;
    public static String nombreProblema;
    public static int cantidadVarReal;
    public static int cantidadVarInt;
    public static int numeroVariables;
    public static int numeroObjetivos;
    public static int  dimMin;
    public static int  dimMax;
    public static int  nMin;
    public static int  mMax;
    public static int  typeMin;
    public static int  typeMax;
    public static double alfaMin;
    public static double alfaMax;

    static {
        
        logger_.info("Cargando algoritmo.properties.. ");
        Properties prop = new Properties();
        InputStream input = null;
        PropertyConfigurator.configure("logger.properties");

        try {

            input = new FileInputStream("algoritmo.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            cantidadParticulas = Integer.valueOf(prop.getProperty("algoritmo.cantidadparticulas"));
            cantidadLideres = Integer.valueOf(prop.getProperty("algoritmo.cantidadlideres"));
            cantidadIteraciones = Integer.valueOf(prop.getProperty("algoritmo.cantidaditeraciones"));
            cantidadVarReal = Integer.valueOf(prop.getProperty("algoritmo.cantidadvarreal"));
            cantidadVarInt = Integer.valueOf(prop.getProperty("algoritmo.cantidadvarint"));
            numeroVariables = Integer.valueOf(prop.getProperty("algoritmo.numerovariables"));
            numeroObjetivos = Integer.valueOf(prop.getProperty("algoritmo.numeroobjetivos"));
            nombreImagen = prop.getProperty("algoritmo.nombreimagen");
            nombreProblema = prop.getProperty("algoritmo.nombre.problema");

            bdConexion = prop.getProperty("bd.conexion");
            bdDriver = prop.getProperty("bd.driver");
            bdUser = prop.getProperty("bd.user");
            bdPass = prop.getProperty("bd.pass");
            
            
            dimMin = Integer.valueOf(prop.getProperty("dimMin"));
            dimMax = Integer.valueOf(prop.getProperty("dimMax"));
            nMin = Integer.valueOf(prop.getProperty("nMin"));
            mMax = Integer.valueOf(prop.getProperty("mMax"));
            typeMin = Integer.valueOf(prop.getProperty("typeMin"));
            typeMax = Integer.valueOf(prop.getProperty("typeMax"));
            alfaMin = Double.parseDouble(prop.getProperty("alfaMin"));
            alfaMax = Double.parseDouble(prop.getProperty("alfaMax"));
            
            logger_.info("Cargado algoritmo.properties");

        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

} // Configuration
