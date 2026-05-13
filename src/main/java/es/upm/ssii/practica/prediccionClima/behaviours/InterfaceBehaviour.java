package es.upm.ssii.practica.prediccionClima.behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public class InterfaceBehaviour extends CyclicBehaviour {

    public InterfaceBehaviour(Agent a) {
        super(a);
    }

    public void action() {
        System.out.println("Mostrando resultados al usuario...");
        // Logica para mostrar la interfaz grafica con los resultados
        block();
    }
}
