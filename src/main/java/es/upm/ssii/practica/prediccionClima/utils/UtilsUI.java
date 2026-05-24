package es.upm.ssii.practica.prediccionClima.utils;

import es.upm.ssii.practica.prediccionClima.models.PredictionResult;
import es.upm.ssii.practica.prediccionClima.ui.JFrameResultado;

import javax.swing.*;


public class UtilsUI {

    private UtilsUI() {
    	
    }

    
    //Parsea el JSON de predicción enviado por MLBehaviour y abre la ventana.
    public static void mostrarPrediccion(String jsonPrediccion) {
        if (jsonPrediccion == null || jsonPrediccion.isBlank()) {
            mostrarError("El mensaje de predicción recibido está vacío.");
            return;
        }
        try {
            PredictionResult prediccion = PredictionResult.desdeJSON(jsonPrediccion);
            SwingUtilities.invokeLater(() -> {
                try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
                catch (Exception ignored) {}
                JFrameResultado ventana = new JFrameResultado(prediccion);
                ventana.setVisible(true);
                System.out.println("[UtilsUI] Ventana abierta. " + prediccion);
            });
        } catch (Exception e) {
            mostrarError("Error al procesar la predicción: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Mensje de error
    public static void mostrarError(String mensaje) {
        System.err.println("[UtilsUI] ERROR: " + mensaje);
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                null, mensaje, "Error – Sistema Meteorológico", JOptionPane.ERROR_MESSAGE));
    }

    //Mensaje informativo
    //Usamos SwingUtilities.invokeLater para crear otro hilo que espere y así evitar que congele el sistema
    public static void mostrarMensaje(String mensaje) {
        System.out.println("[UtilsUI] INFO: " + mensaje);
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                null, mensaje, "Sistema Meteorológico · JADE", JOptionPane.INFORMATION_MESSAGE));
    }
    
    //Mensaje de Alertas Metereológicas. Intentamos sacar el mensaje en limpio
    public static void mostrarAlertas(String json) {

        try {
            String mensaje = "";

            // Extraer cada alerta individualmente
            if (json.contains("Tormenta")) {
                mensaje += "Tormenta: lluvia intensa y viento fuerte previstos\n";
            }
            if (json.contains("Lluvia")) {

                int idx = json.indexOf("Lluvia: probabilidad alta (");
                if (idx != -1) {
                    int start = idx + "Lluvia: probabilidad alta (".length();
                    int end = json.indexOf("%", start);
                    String pct = json.substring(start, end);
                    mensaje += "Lluvia: probabilidad alta (" + pct + "%)\n";
                }
            }
            if (json.contains("Viento fuerte")) {
                int idx = json.indexOf("Viento fuerte: ");
                if (idx != -1) {
                    int start = idx + "Viento fuerte: ".length();
                    int end = json.indexOf("km/h", start);
                    String viento = json.substring(start, end).trim();
                    mensaje += "Viento fuerte: " + viento + " km/h\n";
                }
            }
            if (json.contains("Calor extremo")) {
                int idx = json.indexOf("Calor extremo: máxima de ");
                if (idx != -1) {
                    int start = idx + "Calor extremo: máxima de ".length();
                    int end = json.indexOf("°C", start);
                    String temp = json.substring(start, end);
                    mensaje += "Calor extremo: máxima de " + temp + "°C\n";
                }
            }
            if (json.contains("Frío extremo")) {
                int idx = json.indexOf("Frío extremo: mínima de ");
                if (idx != -1) {
                    int start = idx + "Frío extremo: mínima de ".length();
                    int end = json.indexOf("°C", start);
                    String temp = json.substring(start, end);
                    mensaje += "Frío extremo: mínima de " + temp + "°C\n";
                }
            }

            if (mensaje.isEmpty()) {
                mensaje = json;
            }

            System.out.println("[UtilsUI] ALERTA: " + mensaje);

            final String msgFinal = mensaje;
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    null, msgFinal, "¡¡¡Alerta Meteorológica!!!", JOptionPane.WARNING_MESSAGE));

        } catch (Exception e) {
            System.err.println("[UtilsUI] Error al mostrar alerta: " + e.getMessage());
            mostrarMensaje("Error al procesar alerta: " + json);
        }
    }

}
