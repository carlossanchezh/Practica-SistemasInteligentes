package es.upm.ssii.practica.prediccionClima.utils;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.io.IOException;
import java.io.Serializable;

public class Utils {

    public static void enviarMensaje(Agent agente, String tipoServicio, Serializable contenido) {
        try {
            // Crear plantilla de búsqueda
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType(tipoServicio);
            template.addServices(sd);

            // Buscar en el DF
            DFAgentDescription[] results = DFService.search(agente, template);

            // Si encontró el servicio
            if (results.length > 0) {
                // Obtener el AID del agente
                AID destino = results[0].getName();

                // Crear y enviar mensaje
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                msg.addReceiver(destino);
                msg.setContentObject(contenido);
                agente.send(msg);

                System.out.println("Mensaje Enviado: " + agente.getLocalName() + " -> " + destino.getLocalName());
            } else {
                System.out.println("No se encontró servicio: " + tipoServicio);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void enviarInform(Agent agente, String tipoServicio, Serializable contenido) {
        try {
            // Crear plantilla de búsqueda
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType(tipoServicio);
            template.addServices(sd);

            // Buscar en el DF
            DFAgentDescription[] results = DFService.search(agente, template);

            // Si encontró el servicio
            if (results.length > 0) {
                // Obtener el AID del agente
                AID destino = results[0].getName();

                // Crear y enviar mensaje
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(destino);
                msg.setContentObject(contenido);
                agente.send(msg);

                System.out.println("Inform Enviado: " + agente.getLocalName() + " -> " + destino.getLocalName());
            } else {
                System.out.println("No se encontró servicio: " + tipoServicio);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
