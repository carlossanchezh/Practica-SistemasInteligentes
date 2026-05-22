package es.upm.ssii.practica.prediccionClima.behaviours;

import es.upm.ssii.practica.prediccionClima.connectors.WeatherConnector;
import es.upm.ssii.practica.prediccionClima.utils.Utils;
import jade.core.behaviours.TickerBehaviour;
import jade.core.Agent;
import java.util.Map;

public class PerceptionBehaviour extends TickerBehaviour {

    public PerceptionBehaviour(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        WeatherConnector conector = new WeatherConnector();    
        try {
            //Obtengo datos de la API
            String bruto = conector.obtenerDatosBrutos();
            Map<String, Object> datos = conector.extraerTodo(bruto);
            //Construyo el JSON manualmente 
            String json = construirJSON(datos);
            //Para depurar, quitar en el futuro cuando esté la interfaz instalada y funcional
            System.out.println("\n==========================================");
            System.out.println("ENVIANDO OBSERVACIÓN (JSON):");
            System.out.println(json);
            System.out.println("==========================================\n");
            //Envío al siguiente agente (ML)
            Utils.enviarInform(myAgent, "ML", json,"observacion");
            System.out.println("Mensaje enviado al agente ML\n");
        } catch (Exception e) {
            System.err.println("Error en PerceptionBehaviour: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String construirJSON(Map<String, Object> datos) {
    	//Para no tener problemas con los caracteres especiales
        String ciudad = escaparJSON((String) datos.get("city"));
        String timestamp = escaparJSON((String) datos.get("timestamp"));
        String descripcion = escaparJSON((String) datos.get("description"));
        //construyo JSON
        return "{\n" +
               "  \"ciudad\": \"" + ciudad + "\",\n" +
               "  \"timestamp\": \"" + timestamp + "\",\n" +
               "  \"temperatura\": " + datos.get("temp") + ",\n" +
               "  \"sensacion_termica\": " + datos.get("feels_like") + ",\n" +
               "  \"humedad\": " + datos.get("humidity") + ",\n" +
               "  \"presion\": " + datos.get("pressure") + ",\n" +
               "  \"viento\": " + datos.get("wind") + ",\n" +
               "  \"nubosidad\": " + datos.get("clouds") + ",\n" +
               "  \"descripcion\": \"" + descripcion + "\"\n" +
               "}";
    }
    //Evito los prosibles problemas de caracteres especiales
    private String escaparJSON(String texto) {
        if (texto == null) return "";
        return texto.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}