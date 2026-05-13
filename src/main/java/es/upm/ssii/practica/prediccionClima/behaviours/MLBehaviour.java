package es.upm.ssii.practica.prediccionClima.behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public class MLBehaviour  extends CyclicBehaviour {

    public MLBehaviour(Agent agent) {
        super(agent);
    }

    public void action() {
        System.out.println("Prediciendo Clima...");
        // Prediccion del clima a taraves de un modelo (red nueronas) entrenado con datos el mundo real
        block();
    }

}
