package es.upm.ssii.practica.prediccionClima.agents;

import es.upm.ssii.practica.prediccionClima.behaviours.MLBehaviour;
import es.upm.ssii.practica.prediccionClima.launcher.AgentBase;
import es.upm.ssii.practica.prediccionClima.launcher.AgentModel;

public class MLAgent extends AgentBase {

    private static final long serialVersionUID = 1L;
    public static final String NICKNAME = "ML";

    protected void setup() {

        System.out.println("Agente ML iniciado - Sen encarga de predecir el clima");

        super.setup();
        this.type = AgentModel.ML;

        addBehaviour(new MLBehaviour(this));

        registerAgentDF();

    }

}
