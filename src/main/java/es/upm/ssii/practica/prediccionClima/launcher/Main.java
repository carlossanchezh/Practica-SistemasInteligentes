package es.upm.ssii.practica.prediccionClima.launcher;

import es.upm.ssii.practica.prediccionClima.agents.InterfaceAgent;
import es.upm.ssii.practica.prediccionClima.agents.AlertAgent;
import es.upm.ssii.practica.prediccionClima.agents.MLAgent;
import es.upm.ssii.practica.prediccionClima.agents.PerceptionAgent;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.io.IOException;

public class Main {

    private static AgentContainer cc;

    private static void loadBoot(){

        //Creamos una instancia de entorno de ejecución java
        Runtime rt = Runtime.instance();
        rt.setCloseVM(true);

        //ProfileImpl permite recuperar distintos parámetros de ejecución y de lanzamiento de JADE
        Profile profile = new ProfileImpl(null, 1200, null);

        //Creamos un contenedor JADE dentro de la instancia java
        System.out.println("Launching a whole in-process platform..."+profile);
        cc = rt.createMainContainer(profile);

        try{

            ProfileImpl pContainer = new ProfileImpl(null, 1200, null);
            rt.createAgentContainer(pContainer);
            System.out.println("Containers created");
            System.out.println("Launching the rma agent on the main container ...");

            cc.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]).start();

            cc.createNewAgent(PerceptionAgent.NICKNAME, PerceptionAgent.class.getName(), new Object[]{"o"}).start();

            cc.createNewAgent(MLAgent.NICKNAME, MLAgent.class.getName(), new Object[]{"o"}).start();

            cc.createNewAgent(InterfaceAgent.NICKNAME, InterfaceAgent.class.getName(), new Object[]{"o"}).start();
            
            cc.createNewAgent(AlertAgent.NICKNAME, AlertAgent.class.getName(), new Object[]{"o"}).start();

        } catch (StaleProxyException e) {
            System.err.println("Error during boot!!!");
            e.printStackTrace();
            System.exit(1);
        }

    }

    public static void main(String[] args)  throws IOException {
        System.out.println("Starting...");
        loadBoot();
        System.out.println("MAS loaded...");
    }
}
