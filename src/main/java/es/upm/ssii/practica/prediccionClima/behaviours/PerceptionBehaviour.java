package es.upm.ssii.practica.prediccionClima.behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public class PerceptionBehaviour extends CyclicBehaviour {

    public PerceptionBehaviour(Agent agent) {
        super(agent);
    }

    public void action() {
        System.out.println("Consultando API del tiempo...");
        // Lógica de consulta a OpenWeatherMap
        block();
    }

}
