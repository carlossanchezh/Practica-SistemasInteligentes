package es.upm.ssii.practica.prediccionClima.agents;

import es.upm.ssii.practica.prediccionClima.behaviours.InterfaceBehaviour;
import es.upm.ssii.practica.prediccionClima.launcher.AgentBase;
import es.upm.ssii.practica.prediccionClima.launcher.AgentModel;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;


public class InterfaceAgent extends AgentBase {

    private static final long serialVersionUID = 1L;
    public static final String NICKNAME = "Interfaz";

    @Override
    protected void setup() {
        System.out.println("[InterfaceAgent] Iniciando: " + getLocalName());

        super.setup();                      // setup de AgentBase
        this.type = AgentModel.INTERFAZ;    
        registerAgentDF();                  
        addBehaviour(new InterfaceBehaviour(this));

        System.out.println("[InterfaceAgent] Registrado en DF y esperando predicciones.");
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
            System.out.println("[InterfaceAgent] Desregistrado del DF.");
        } catch (FIPAException e) {
            System.err.println("[InterfaceAgent] Error al desregistrar: " + e.getMessage());
        }
    }
}
