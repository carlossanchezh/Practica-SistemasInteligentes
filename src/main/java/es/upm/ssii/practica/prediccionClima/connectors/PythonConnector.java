package es.upm.ssii.practica.prediccionClima.connectors;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PythonConnector {

    private static final String SCRIPT_PATH = "modelo/prediccion.py";

    private static final String PYTHON_CMD = "python";

    public static double[] ejecutarPrediccion(double temperatura, int humedad, double viento, int nubosidad) {
        try {
            // Construye el argumento para pasar al script
            String argumento = temperatura + "," + humedad + "," + viento + "," + nubosidad;

            ProcessBuilder pb = new ProcessBuilder(PYTHON_CMD, SCRIPT_PATH, argumento);
            pb.redirectErrorStream(true);

            Process dp = pb.start();
            dp.waitFor();

            //reader para leer la salida del script
            BufferedReader reader = new BufferedReader(new InputStreamReader(dp.getInputStream()));

            String linea;
            double[] resultado = null;

            while((linea = reader.readLine()) != null ){ //Buscar la linea resultado (evitar warnings y logs)

                linea = linea.trim();
                if (linea.matches("^[\\d\\.,\\s-]+$")) { //formato de la salida python

                    String[] partes = linea.split(","); //Trata de dividir la linea en partes separadas por ','

                    if (partes.length == 6) { //si hay 6 partes

                        resultado = new double[6];

                        for (int i = 0; i < 6; i++) {
                            resultado[i] = Double.parseDouble(partes[i]); //añade las partes al array resultado
                        }

                        break;  // Salir del bucle
                    }
                }
            }



            if (resultado != null) {
                return resultado;
            } else {
                System.err.println("No se encontró el resultado en la salida del script");
                return null;
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

}
