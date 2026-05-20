package es.upm.ssii.practica.prediccionClima.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WeatherConnector {
    private final String API_KEY = "3f7565d3894eb8f8e4c23c0dd987fb58";
    private final String CIUDAD = "Madrid";

    public String obtenerDatosBrutos() throws Exception {
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + CIUDAD + "&appid=" + API_KEY + "&units=metric";
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET"); //petición GET para obtener la info

        if (con.getResponseCode() != 200) { //por si falla el GET
            throw new Exception("Error API: "+con.getResponseCode());
        }
        //obtengo toda la info, más adelante ya la trato
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())); 
        StringBuilder contenido = new StringBuilder();
        String linea;
        while ((linea= in.readLine())!=null) contenido.append(linea);
        in.close();
        return contenido.toString();
    }

    public Map<String, Object> extraerTodo(String json) {
        Map<String, Object> datos= new HashMap<>(); 
        //Saco los datos para la futura predicción
        datos.put("temp", extraerDato(json, "\"temp\":", ","));
        datos.put("feels_like", extraerDato(json, "\"feels_like\":", ","));
        datos.put("humidity", extraerEntero(json, "\"humidity\":", ","));
        datos.put("pressure", extraerDato(json, "\"pressure\":", ","));
        datos.put("wind", extraerDato(json, "\"speed\":", ","));
        datos.put("clouds", extraerNubosidad(json));
        datos.put("city", extraerCiudad(json));
        datos.put("timestamp", java.time.Instant.now().toString());
        datos.put("description", extraerDescripcion(json)); //Descripción del clima
        
        return datos;
    }

    private double extraerDato(String json, String clave, String fin) {  //Encaragado de buscar el param clave 
    																	 //en el JSON y devolver su valor como double.
        try {
            int inicio =json.indexOf(clave) + clave.length();
            int finalIndice =json.indexOf(fin, inicio);
            return Double.parseDouble(json.substring(inicio, finalIndice));
        } catch (Exception e) {
            return 0.0; //Valor por defecto 
        }
    }
    
    private int extraerEntero(String json, String clave, String fin) { //Lo mismo q el anterior pero devuelve un int
        try {
            int inicio= json.indexOf(clave) + clave.length();
            int finalIndice= json.indexOf(fin, inicio);
            return Integer.parseInt(json.substring(inicio, finalIndice));
        } catch (Exception e) {
            return 0; //Valor por defecto 
        }
    }
    
    private int extraerNubosidad(String json) { //Busca el porcentaje de nubosidad
        try {
            int inicio= json.indexOf("\"clouds\":{\"all\":") + 15;
            int fin= json.indexOf("}", inicio);
            return Integer.parseInt(json.substring(inicio, fin));
        } catch (Exception e) {
            return 0; //Valor por defecto 
        }
    }
    
    private String extraerCiudad(String json) { //Extrae el nombre de la ciudad
        try {
            int inicio= json.indexOf("\"name\":\"")+8; //le sumo 8 porque "name":" son 8 caracteres
            int fin = json.indexOf("\"", inicio);
            return json.substring(inicio, fin);
        } catch (Exception e) {
            return "Madrid"; //Valor por defecto 
        }
    }
    
    private String extraerDescripcion(String json) { //Extrae la descripción del clima
        try {
            int inicio= json.indexOf("\"description\":\"")+15; //le sumo 15 porq "description":" son 15 caracteres
            int fin= json.indexOf("\"", inicio);
            return json.substring(inicio, fin);
        } catch (Exception e) {
            return "clear sky"; //Valor por defecto 
        }
    }
}