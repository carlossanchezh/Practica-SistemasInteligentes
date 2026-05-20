package es.upm.ssii.practica.prediccionClima.agents;

import es.upm.ssii.practica.prediccionClima.launcher.AgentBase;
import es.upm.ssii.practica.prediccionClima.launcher.AgentModel;
import es.upm.ssii.practica.prediccionClima.behaviours.InterfaceBehaviour;

public class InterfaceAgent extends AgentBase {

    private static final long serialVersionUID = 1L;
    public static final String NICKNAME = "Interfaz";

    protected void setup() {

        System.out.println("Agente Interfaz iniciado - Muestra los resultados al usuario mediante una interfaz");

        super.setup(); //setup de AgentBase (inicializa el agente en JADE) y recoge parametros si tiene
        this.type = AgentModel.INTERFAZ; // asigna que es el agente de INTERFAZ
        registerAgentDF(); // registra el agente en el DF para que otros puedan encontrarlo

        addBehaviour(new InterfaceBehaviour(this));


    }
}
