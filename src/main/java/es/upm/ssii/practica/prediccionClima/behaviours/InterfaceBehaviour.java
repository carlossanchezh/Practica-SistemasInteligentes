package es.upm.ssii.practica.prediccionClima.behaviours;

import es.upm.ssii.practica.prediccionClima.utils.UtilsUI;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class InterfaceBehaviour extends CyclicBehaviour {

    private static final MessageTemplate filtro =
            MessageTemplate.MatchPerformative(ACLMessage.INFORM);

    public InterfaceBehaviour(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        ACLMessage mensaje = myAgent.receive(filtro);

        if (mensaje != null) {
        	
        	System.out.println("MENSAJE RECIBIDO EN INTERFAZ");
        	
        	
        	
            System.out.println("\n==========================================");
            System.out.println("[InterfaceBehaviour] Mensaje recibido de: "
                    + mensaje.getSender().getName());

            String contenido = mensaje.getContent();

            System.out.println("Contenido:\n" + contenido);
            System.out.println("==========================================\n");

            if (contenido == null || contenido.isBlank()) {
                System.err.println("[InterfaceBehaviour] Contenido vacío, ignorando.");
                return;
            }

            if (!contenido.contains("\"tipo\"") || !contenido.contains("prediccion")) {
                System.err.println("[InterfaceBehaviour] Tipo de mensaje inesperado, ignorando.");
                return;
            }

            
            UtilsUI.mostrarPrediccion(contenido);
            

        } else {
            block();
        }
    }
    
    
}
