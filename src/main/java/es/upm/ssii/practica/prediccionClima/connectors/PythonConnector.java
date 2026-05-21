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

    //=============================================================================================================
    //¡¡¡¡¡¡¡¡¡¡¡¡¡SOLO PARA DEPURAR EL CONECTOR BORRAR ESTA PARTE AL JUNTARLO CON EL COMPORTAMIENTO ML!!!!!!!!!!!!!
    //=============================================================================================================
    public static void main(String[] args) {
        System.out.println("=== Test PythonConnector ===\n");

        System.out.println("Test 1 - Entrada: 15°C, 65%, 8km/h, 3/10");
        double[] r1 = ejecutarPrediccion(15.0, 65, 8.0, 3);
        if (r1 != null) {
            System.out.println("  Resultado: " + r1[0] + "," + r1[1] + "," +
                    r1[2] + "," + (int) r1[3] + "," + r1[4] + "," + r1[5]);
        } else {
            System.out.println("  ERROR");
        }

        System.out.println("\nTest 2 - Entrada: 28°C, 45%, 12km/h, 2/10");
        double[] r2 = ejecutarPrediccion(28.0, 45, 12.0, 2);
        if (r2 != null) {
            System.out.println("  Resultado: " + r2[0] + "," + r2[1] + "," +
                    r2[2] + "," + (int) r2[3] + "," + r2[4] + "," + r2[5]);
        } else {
            System.out.println("  ERROR");
        }

        System.out.println("\n=== Test completado ===");
    }
}
