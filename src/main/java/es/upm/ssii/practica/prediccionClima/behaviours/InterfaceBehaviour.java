package es.upm.ssii.practica.prediccionClima.behaviours;

import es.upm.ssii.practica.prediccionClima.utils.UtilsUI;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class InterfaceBehaviour extends CyclicBehaviour {

    //Filtro para recibir solo mensajes de tipo inform y de ontologia prediccion
    private static final MessageTemplate filtroPrediccion = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchOntology("prediccion"));

    //Filtro para recibir solo mensajes de tipo inform y de ontologia alerta
    private static final MessageTemplate filtroAlerta = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchOntology("alerta"));

    public InterfaceBehaviour(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        //Primero comprobar inform de prediccion
        ACLMessage prediccion = myAgent.receive(filtroPrediccion);

        if (prediccion != null) {
            try {

                String contenido = (String) prediccion.getContentObject();

                System.out.println("\n");
                System.out.println("Prediccion recibida en Interfaz");
                System.out.println(contenido);
                System.out.println("\n");


                if (contenido == null || contenido.trim().isEmpty()) {
                    System.err.println("[InterfaceBehaviour] Contenido vacío, ignorando.");
                    return;
                }

                UtilsUI.mostrarPrediccion(contenido);
                return;
            } catch (UnreadableException e) {
                System.err.println("Error: " + e.getMessage());
                return;
            }
        }

        //Si el mensaje no era de ontologia prediccion es de alerta
        ACLMessage alerta = myAgent.receive(filtroAlerta);

        if (alerta != null) {
            try{

                String contenido = (String) alerta.getContentObject();

                System.out.println("\n");
                System.out.println("Alerta recibida en Interfaz");
                System.out.println(contenido);
                System.out.println("\n");


                if (contenido == null || contenido.trim().isEmpty()) {
                    System.err.println("[InterfaceBehaviour] Contenido de alerta vacío, ignorando.");
                    return;
                }

                // Mostrar las alertas
               UtilsUI.mostrarAlertas(contenido);
                return;
            } catch (UnreadableException e) {
                System.err.println("Error al leer alerta: " + e.getMessage());
                return;
            }
        }

        else{
            block();
        }
    }
    

}
