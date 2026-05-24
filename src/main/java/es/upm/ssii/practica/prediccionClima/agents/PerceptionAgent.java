package es.upm.ssii.practica.prediccionClima.agents;

import es.upm.ssii.practica.prediccionClima.launcher.AgentBase;
import es.upm.ssii.practica.prediccionClima.launcher.AgentModel;
import es.upm.ssii.practica.prediccionClima.behaviours.PerceptionBehaviour;

public class PerceptionAgent extends AgentBase {

    private static final long serialVersionUID = 1L; 
    public static final String NICKNAME = "Percepcion"; //para que sea más fácil de modificar en el main

    @Override
    protected void setup() {

        System.out.println("Iniciando: " + getLocalName());

        super.setup(); //setup de AgentBase (inicializa el agente en JADE) y recoge parametros si tiene
        this.type = AgentModel.PERCEPCION; // asigna que es el agente de RECEPCION
        registerAgentDF(); // registra el agente en el DF para que otros puedan encontrarlo

        int minuto = 60000;
        int hora = minuto * 60;

        int period = hora * 12; //Cada cuanto se consultaran los datos de la API

        addBehaviour(new PerceptionBehaviour(this, period));

        System.out.println("[PerceptionAgent] Registrado en DF y esperando datos de la API.");
    }

}
