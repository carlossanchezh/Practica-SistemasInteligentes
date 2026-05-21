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
}
