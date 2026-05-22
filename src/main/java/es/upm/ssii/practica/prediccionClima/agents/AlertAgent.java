package es.upm.ssii.practica.prediccionClima.agents;

import es.upm.ssii.practica.prediccionClima.behaviours.AlertBehaviour;
import es.upm.ssii.practica.prediccionClima.launcher.AgentBase;
import es.upm.ssii.practica.prediccionClima.launcher.AgentModel;

public class AlertAgent extends AgentBase {

    private static final long serialVersionUID = 1L;
    public static final String NICKNAME = "Alertas";

    @Override
    protected void setup() {
        super.setup();
        this.type = AgentModel.ALERTAS;
        registerAgentDF();
        addBehaviour(new AlertBehaviour(this));
        System.out.println("[AlertAgent] Iniciado y registrado en DF.");
    }
}