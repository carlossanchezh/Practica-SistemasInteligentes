package es.upm.ssii.practica.prediccionClima.agents;

import es.upm.ssii.practica.prediccionClima.launcher.AgentBase;
import es.upm.ssii.practica.prediccionClima.launcher.AgentModel;
import es.upm.ssii.practica.prediccionClima.behaviours.InterfaceBehaviour;

public class InterfaceAgent extends AgentBase {

    private static final long serialVersionUID = 1L;
    public static final String NICKNAME = "Interfaz";

    protected void setup() {

        System.out.println("Agente Interfaz iniciado - Muestra los resultados al usuario mediante una interfaz");

        super.setup();
        this.type = AgentModel.INTERFAZ;

        addBehaviour(new InterfaceBehaviour(this));

        registerAgentDF();

    }
}
