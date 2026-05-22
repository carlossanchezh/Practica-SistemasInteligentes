package es.upm.ssii.practica.prediccionClima.behaviours;

import es.upm.ssii.practica.prediccionClima.utils.Utils;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class AlertBehaviour extends CyclicBehaviour {

    // Umbrales de alerta
    private static final double UMBRAL_LLUVIA       = 0.70; // 70%
    private static final double UMBRAL_VIENTO       = 30.0; // km/h
    private static final double UMBRAL_CALOR        = 38.0; // °C temp max
    private static final double UMBRAL_FRIO         = 2.0;  // °C temp min
    private static final double UMBRAL_TORMENTA_LLUVIA = 0.70;
    private static final double UMBRAL_TORMENTA_VIENTO = 25.0;

    //Filtro para recibir solo mensajes de tipo inform y de ontologia prediccion
    private static final MessageTemplate filtroPrediccion = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchOntology("prediccion"));

    public AlertBehaviour(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        ACLMessage mensaje = myAgent.receive(filtroPrediccion);

        if (mensaje != null) {
            System.out.println("[AlertBehaviour] Prediccion recibida, evaluando alertas...");
            try{
                String json = (String) mensaje.getContentObject();

                double tempMax          = extraerDouble(json, "temperatura_max");
                double tempMin          = extraerDouble(json, "temperatura_min");
                double viento           = extraerDouble(json, "velocidad_viento");
                double probLluvia       = extraerDouble(json, "probabilidad_lluvia");

                StringBuilder alertas = new StringBuilder();
                alertas.append("{\n");
                alertas.append("  \"alertas\": [\n");

                boolean hayAlertas = false;

                // Tormenta (combinada, tiene prioridad)
                if (probLluvia > UMBRAL_TORMENTA_LLUVIA && viento > UMBRAL_TORMENTA_VIENTO) {
                    alertas.append("    \"Tormenta: lluvia intensa y viento fuerte previstos\",\n");
                    hayAlertas = true;
                }

                // Lluvia
                if (probLluvia > UMBRAL_LLUVIA) {
                    int pct = (int) Math.round(probLluvia * 100);
                    alertas.append("    \"Lluvia: probabilidad alta (" + pct + "%)\",\n");
                    hayAlertas = true;
                }

                // Viento fuerte
                if (viento > UMBRAL_VIENTO) {
                    alertas.append("    \"Viento fuerte: " + String.format("%.1f", viento) + " km/h\",\n");
                    hayAlertas = true;
                }

                // Calor extremo
                if (tempMax > UMBRAL_CALOR) {
                    alertas.append("    \"Calor extremo: máxima de " + String.format("%.1f", tempMax) + "°C\",\n");
                    hayAlertas = true;
                }

                // Frío extremo
                if (tempMin < UMBRAL_FRIO) {
                    alertas.append("    \"Frío extremo: mínima de " + String.format("%.1f", tempMin) + "°C\",\n");
                    hayAlertas = true;
                }

                String resultado = alertas.toString();
                if (hayAlertas) {
                    resultado = resultado.substring(0, resultado.lastIndexOf(",\n")) + "\n";
                }

                resultado += "  ]\n}";

                if (hayAlertas) {
                    System.out.println("[AlertBehaviour] Alertas detectadas, enviando a Interfaz:");
                    System.out.println(resultado);
                    Utils.enviarInform(myAgent, "Interfaz", resultado, "alerta");
                } else {
                    System.out.println("[AlertBehaviour] Sin alertas para las condiciones actuales.");
                }

            } catch (UnreadableException e) {
                System.err.println("Error: " + e.getMessage());
            }

        } else {
            block();
        }
    }

    private double extraerDouble(String json, String campo) {
        String buscar = "\"" + campo + "\":";
        int inicio = json.indexOf(buscar);
        if (inicio == -1) return 0;
        inicio += buscar.length();
        int fin = json.indexOf(",", inicio);
        if (fin == -1) fin = json.indexOf("}", inicio);
        try {
            return Double.parseDouble(json.substring(inicio, fin).trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}