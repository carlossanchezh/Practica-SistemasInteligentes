package es.upm.ssii.practica.prediccionClima.behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import es.upm.ssii.practica.prediccionClima.connectors.PythonConnector;
import es.upm.ssii.practica.prediccionClima.utils.Utils;

public class MLBehaviour  extends CyclicBehaviour {

	//Filtro para recibir solo mensajes de tipo inform y de ontologia observacion
	private static final MessageTemplate filtroObservacion = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchOntology("observacion"));

    public MLBehaviour(Agent agent) {
        super(agent);
    }

	@Override
    public void action() {
        //recibe mensajes inform que le manda el agente de percepcion
		ACLMessage mensaje = myAgent.receive(filtroObservacion);
        if(mensaje != null) {
			try {
				String json = (String) mensaje.getContentObject();//obtener json del mensaje

				System.out.println("\n");
				System.out.println("Observacion recibida en ML:");
        		System.out.println(json);
				System.out.println("\n");
        		
        		//extraemso los datos necesarios para enviarlos al modelo
        		double temperatura = extraerDouble(json, "temperatura");
        		int humedad = extraerInt(json, "humedad");
        		double viento = extraerDouble(json, "viento");
        		int nubosidad = extraerInt(json, "nubosidad");

				System.out.println("Ejecutando predicción con el modelo de red neuronal");
				System.out.println("\n");

        		//llamar al modelo de python para obtener la prediccion
        		double[] prediccion = PythonConnector.ejecutarPrediccion(temperatura, humedad, viento, nubosidad);
        		if(prediccion == null) {//por si falla la llamada
        			System.out.println("No se ha podido obtener prediccion desde python");
        			return;
        		}
        		System.out.println("Prediccion recibida desde Python:");
        		System.out.println("Temperatura max: "+ prediccion[0]);
        		System.out.println("Temperatura min: "+ prediccion[1]);
        		System.out.println("Temperatura media: "+ prediccion[2]);
        		System.out.println("Nubosidad: "+ prediccion[3]);
				System.out.println("Velocidad del viento: "+ prediccion[4]);
        		System.out.println("Probabilidad lluvia: "+ prediccion[5]);
				System.out.println("\n");
        		
        		//genera recomendacion sencilla de ropa segun prediccion
        		String recomendacion = generarRecomendacion(prediccion[2], prediccion[5]);
        		
        		//creamos json de la prediccion final
        		String jsonPrediccion = crearJsonPrediccion(prediccion, recomendacion);
        		System.out.println("Prediccion lista para enviar:");
        		System.out.println(jsonPrediccion);
				System.out.println("\n");
        		
        		//envia peticion al agente de interfaz
        		Utils.enviarInform(myAgent, "Interfaz", jsonPrediccion, "prediccion");

        		Utils.enviarInform(myAgent, "Alertas", jsonPrediccion, "prediccion");


			} catch (UnreadableException e) {
				System.err.println("Error: " + e.getMessage());
			}
        }
        else { //si no llega mensaje el agente espera
        	block();
        }
    }
    
    //extrae valores decimales del json
    private double extraerDouble(String json, String campo) {
    	String buscar = "\"" + campo + "\":";
    	int inicio = json.indexOf(buscar);
    	if(inicio == -1) return 0;
    	
    	inicio = inicio + buscar.length();
    	int fin = json.indexOf(",", inicio);
    	if(fin == -1) fin = json.indexOf("}", inicio);
    	
    	String valor = json.substring(inicio, fin).trim();
    	return Double.parseDouble(valor);
    }
    //extrae los valores enteros
    private int extraerInt(String json, String campo) {
    	String buscar = "\"" + campo + "\":";
    	int inicio = json.indexOf(buscar);
    	if(inicio == -1) return 0;
    	
    	inicio = inicio + buscar.length();
    	int fin = json.indexOf(",", inicio);
    	if(fin == -1) fin = json.indexOf("}", inicio);
    	
    	String valor = json.substring(inicio, fin).trim();
    	return Integer.parseInt(valor);
    }
    //generamos recomendacion simple con la temperatura media y prob de lluvia
    private String generarRecomendacion(double temperaturaMedia, double probabilidadLluvia) {
    	String recomendacion = "";
    	if(temperaturaMedia < 10) {
    		recomendacion = "Ropa de abrigo";
    	}
    	else if(temperaturaMedia < 20) {
    		recomendacion =  "Chaqueta ligera";
    	}
    	else {
    		recomendacion = "Ropa ligera";
    	}
    	if(probabilidadLluvia > 0.5) {
    		recomendacion = recomendacion + " y paraguas";
    	}
    	return recomendacion;
    }
    //creamos el json con toda la informacion finl
    private String crearJsonPrediccion(double[] prediccion, String recomendacion) {
		String json = "{\n" +
    			"  \"temperatura_max\": "+ prediccion[0] +",\n" +
    			"  \"temperatura_min\": "+ prediccion[1] +",\n" +
    			"  \"temperatura_media\": "+ prediccion[2] +",\n" +
    			"  \"nubosidad\": "+ prediccion[3] +",\n" +
				"  \"velocidad_viento\": "+ prediccion[4] +",\n" +
    			"  \"probabilidad_lluvia\": "+ prediccion[5] +",\n" +
    			"  \"recomendacion\": \"" + recomendacion + "\"\n" +
                "}";
    	return json;
    }

}
