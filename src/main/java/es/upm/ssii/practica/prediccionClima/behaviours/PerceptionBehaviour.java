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
    public void onStart() {
        // Primera ejecución inmediata (llama a onTick directamente)
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

            System.out.println("\n");
            System.out.println("Datos obtenidos de la API:");
            System.out.println(json);
            System.out.println("\n");

            //Envío al siguiente agente (ML)
            Utils.enviarInform(myAgent, "ML", json,"observacion");

            //Datos de prueba para probar el sistema
            //Utils.enviarInform(myAgent, "ML",escenarioVerano() ,"observacion");
            //Utils.enviarInform(myAgent, "ML",escenarioInvierno(),"observacion");
            //Utils.enviarInform(myAgent, "ML",escenarioLluvia(),"observacion");
            //Utils.enviarInform(myAgent, "ML",escenarioViento(),"observacion");
            //Utils.enviarInform(myAgent, "ML",escenarioTormenta(),"observacion");
            //Utils.enviarInform(myAgent, "ML",escenarioNormal(),"observacion");

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

    //Escenarios de prueba

    //Escenario 1: VERANO
    private String escenarioVerano() {
        return "{\n" +
                "  \"ciudad\": \"Madrid (Prueba - Verano)\",\n" +
                "  \"timestamp\": \"" + java.time.Instant.now() + "\",\n" +
                "  \"temperatura\": 38.5,\n" +
                "  \"sensacion_termica\": 39.0,\n" +
                "  \"humedad\": 35,\n" +
                "  \"presion\": 1015,\n" +
                "  \"viento\": 8.5,\n" +
                "  \"nubosidad\": 2,\n" +
                "  \"descripcion\": \"cielo despejado, calor extremo\"\n" +
                "}";
    }

    //Escenario 2: INVIERNO
    private String escenarioInvierno() {
        return "{\n" +
                "  \"ciudad\": \"Madrid (Prueba - Invierno)\",\n" +
                "  \"timestamp\": \"" + java.time.Instant.now() + "\",\n" +
                "  \"temperatura\": -2.0,\n" +
                "  \"sensacion_termica\": -6.0,\n" +
                "  \"humedad\": 85,\n" +
                "  \"presion\": 1030,\n" +
                "  \"viento\": 15.0,\n" +
                "  \"nubosidad\": 8,\n" +
                "  \"descripcion\": \"nieve, sensación térmica muy baja\"\n" +
                "}";
    }


    //Escenario 3: LLUVIA
    private String escenarioLluvia() {
        return "{\n" +
                "  \"ciudad\": \"Madrid (Prueba - Lluvia)\",\n" +
                "  \"timestamp\": \"" + java.time.Instant.now() + "\",\n" +
                "  \"temperatura\": 12.0,\n" +
                "  \"sensacion_termica\": 10.0,\n" +
                "  \"humedad\": 95,\n" +
                "  \"presion\": 1005,\n" +
                "  \"viento\": 20.0,\n" +
                "  \"nubosidad\": 10,\n" +
                "  \"descripcion\": \"lluvia intensa\"\n" +
                "}";
    }

    //Escenario 4: VIENTO
    private String escenarioViento() {
        return "{\n" +
                "  \"ciudad\": \"Madrid (Prueba - Viento)\",\n" +
                "  \"timestamp\": \"" + java.time.Instant.now() + "\",\n" +
                "  \"temperatura\": 18.0,\n" +
                "  \"sensacion_termica\": 16.0,\n" +
                "  \"humedad\": 60,\n" +
                "  \"presion\": 1010,\n" +
                "  \"viento\": 90.0,\n" +
                "  \"nubosidad\": 4,\n" +
                "  \"descripcion\": \"vientos fuertes\"\n" +
                "}";
    }

    //Escenario 5: TORMENTA
    private String escenarioTormenta() {
        return "{\n" +
                "  \"ciudad\": \"Madrid (Prueba - Tormenta)\",\n" +
                "  \"timestamp\": \"" + java.time.Instant.now() + "\",\n" +
                "  \"temperatura\": 15.0,\n" +
                "  \"sensacion_termica\": 12.0,\n" +
                "  \"humedad\": 90,\n" +
                "  \"presion\": 998,\n" +
                "  \"viento\": 50.0,\n" +
                "  \"nubosidad\": 9,\n" +
                "  \"descripcion\": \"tormenta eléctrica con granizo\"\n" +
                "}";
    }

    //Escenario 6: NORMAL
    private String escenarioNormal() {
        return "{\n" +
                "  \"ciudad\": \"Madrid (Prueba - Normal)\",\n" +
                "  \"timestamp\": \"" + java.time.Instant.now() + "\",\n" +
                "  \"temperatura\": 22.0,\n" +
                "  \"sensacion_termica\": 22.5,\n" +
                "  \"humedad\": 55,\n" +
                "  \"presion\": 1018,\n" +
                "  \"viento\": 12.0,\n" +
                "  \"nubosidad\": 3,\n" +
                "  \"descripcion\": \"cierto despejado\"\n" +
                "}";
    }
}