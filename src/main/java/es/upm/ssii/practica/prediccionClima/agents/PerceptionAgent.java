package es.upm.ssii.practica.prediccionClima.agents;

import es.upm.ssii.practica.prediccionClima.launcher.AgentBase;
import es.upm.ssii.practica.prediccionClima.launcher.AgentModel;
import es.upm.ssii.practica.prediccionClima.behaviours.PerceptionBehaviour;

public class PerceptionAgent extends AgentBase {

    private static final long serialVersionUID = 1L; 
    public static final String NICKNAME = "Percepcion"; //para que sea más fácil de modificar en el main

    @Override
    protected void setup() {
        addBehaviour(new PerceptionBehaviour(this, 30000)); //cada 30 segundos se envía info actualizada
    }

}
