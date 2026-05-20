package es.upm.ssii.practica.prediccionClima.agents;

import es.upm.ssii.practica.prediccionClima.behaviours.MLBehaviour;
import es.upm.ssii.practica.prediccionClima.launcher.AgentBase;
import es.upm.ssii.practica.prediccionClima.launcher.AgentModel;

public class MLAgent extends AgentBase {

    private static final long serialVersionUID = 1L;
    public static final String NICKNAME = "ML";

    protected void setup() {

        System.out.println("Agente ML iniciado - Sen encarga de predecir el clima");

        super.setup(); //setup de AgentBase (inicializa el agente en JADE) y recoge parametros si tiene
        this.type = AgentModel.ML; // asigna que es el agente de ML
        registerAgentDF();

        addBehaviour(new MLBehaviour(this));


    }

}
