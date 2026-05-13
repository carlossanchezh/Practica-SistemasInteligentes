package es.upm.ssii.practica.prediccionClima.agents;

import es.upm.ssii.practica.prediccionClima.launcher.AgentBase;
import es.upm.ssii.practica.prediccionClima.launcher.AgentModel;
import es.upm.ssii.practica.prediccionClima.behaviours.PerceptionBehaviour;

public class PerceptionAgent extends AgentBase {

    private static final long serialVersionUID = 1L;
    public static final String NICKNAME = "Percepcion";

    protected void setup() {

        System.out.println("Agente Percepción iniciado - Consulta API del tiempo");

        super.setup();
        this.type = AgentModel.PERCEPCION;

        addBehaviour(new PerceptionBehaviour(this));

        registerAgentDF();

    }

}
