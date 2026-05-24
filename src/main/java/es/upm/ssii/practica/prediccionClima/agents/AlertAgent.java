package es.upm.ssii.practica.prediccionClima.agents;

import es.upm.ssii.practica.prediccionClima.behaviours.AlertBehaviour;
import es.upm.ssii.practica.prediccionClima.launcher.AgentBase;
import es.upm.ssii.practica.prediccionClima.launcher.AgentModel;

public class AlertAgent extends AgentBase {

    private static final long serialVersionUID = 1L;
    public static final String NICKNAME = "Alertas";

    @Override
    protected void setup() {

        System.out.println("Iniciando: " + getLocalName());

        super.setup(); // setup de AgentBase
        this.type = AgentModel.ALERTAS; // asigna que es el agente de ALERTAS
        registerAgentDF(); // registra el agente en el DF para que otros puedan encontrarlo

        addBehaviour(new AlertBehaviour(this));

        System.out.println("[AlertAgent] Registrado en DF y esperando predicciones.");
    }
}