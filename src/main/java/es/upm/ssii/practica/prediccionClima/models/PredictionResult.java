package es.upm.ssii.practica.prediccionClima.models;

/**
 * Modelo de datos que guarda la prediccion que viene en formato JSON
 *
 * Formato JSON recibido:
 * {
 *   "tipo" : "prediccion",
 *   "temperatura_max": ___,
 *   "temperatura_min": ___,
 *   "temperatura_media": ____,
 *   "nubosidad": ___,
 *   "probabilidad_lluvia": ___,
 *   "recomendacion": _____
 * }
 */
public class PredictionResult {

    private double temperaturaMax;
    private double temperaturaMin;
    private double temperaturaMedia;
    private double nubosidad;
    private double probabilidadLluvia;
    private String recomendacion;

    // Constructor vacío
    public PredictionResult() {}

    // Constructor completo
    public PredictionResult(double temperaturaMax, double temperaturaMin, double temperaturaMedia,
                            double nubosidad, double probabilidadLluvia, String recomendacion) {
        this.temperaturaMax     = temperaturaMax;
        this.temperaturaMin     = temperaturaMin;
        this.temperaturaMedia   = temperaturaMedia;
        this.nubosidad          = nubosidad;
        this.probabilidadLluvia = probabilidadLluvia;
        this.recomendacion      = recomendacion;
    }

    // Getters y Setters

    public double getTemperaturaMax()     { return temperaturaMax; }
    public void setTemperaturaMax(double v) { this.temperaturaMax = v; }

    public double getTemperaturaMin()     { return temperaturaMin; }
    public void setTemperaturaMin(double v) { this.temperaturaMin = v; }

    public double getTemperaturaMedia()   { return temperaturaMedia; }
    public void setTemperaturaMedia(double v) { this.temperaturaMedia = v; }

    public double getNubosidad()          { return nubosidad; }
    public void setNubosidad(double v)    { this.nubosidad = v; }

    public double getProbabilidadLluvia() { return probabilidadLluvia; }
    public void setProbabilidadLluvia(double v) { this.probabilidadLluvia = v; }

    public String getRecomendacion()      { return recomendacion; }
    public void setRecomendacion(String v) { this.recomendacion = v; }

    // ── Parser JSON (sin librerías externas) ────────────────────────────────

    /**
     * Parsea el JSON enviado por MLBehaviour y devuelve un PredictionResult.
     */
    public static PredictionResult desdeJSON(String json) {
        PredictionResult r = new PredictionResult();
        try {
            r.temperaturaMax     = extraerDouble(json, "temperatura_max");
            r.temperaturaMin     = extraerDouble(json, "temperatura_min");
            r.temperaturaMedia   = extraerDouble(json, "temperatura_media");
            r.nubosidad          = extraerDouble(json, "nubosidad");
            r.probabilidadLluvia = extraerDouble(json, "probabilidad_lluvia");
            r.recomendacion      = extraerString(json, "recomendacion");
        } catch (Exception e) {
            System.err.println("[PredictionResult] Error al parsear JSON: " + e.getMessage());
        }
        return r;
    }

    //Aux

    private static double extraerDouble(String json, String clave) {
        String patron = "\"" + clave + "\"";
        //Compruebo que tenga el formato correcto
        int idx = json.indexOf(patron);
        if (idx == -1) return 0.0;
        int separador = json.indexOf(":", idx);
        if (separador == -1) return 0.0;
        //Busco el principio del numero
        int inicio = separador + 1;
        while (inicio < json.length() && " \n\r\t".indexOf(json.charAt(inicio)) >= 0) 
        { 
        	inicio++;
        }
        //Busco el final del numero
        int fin = inicio;
        while (fin < json.length() && "0123456789.-".indexOf(json.charAt(fin)) >= 0) 
        { 
        	fin++;
        	}
        //Intento obtener el double
        try { return Double.parseDouble(json.substring(inicio, fin)); }
        catch (NumberFormatException e) 
        { 
        	return 0.0; 
        }
    }

    private static String extraerString(String json, String clave) {
        String patron = "\"" + clave + "\"";
        //Compruebo que tenga el formato correcto
        int idx = json.indexOf(patron);
        if (idx == -1) return "";
        int separador = json.indexOf(":", idx);
        if (separador == -1) return "";
        // Buscar la primera comilla de apertura del valor
        int inicio = json.indexOf("\"", separador + 1);
        if (inicio == -1) return "";
        // Busco la siguiente comilla al inicio y sigo buscando a partir de la anterior hasta obtener la última
        int fin = json.indexOf("\"", inicio + 1);
        // Saltar comillas escapadas
        while (fin > 0 && json.charAt(fin - 1) == '\\') 
        {
        	fin = json.indexOf("\"", fin + 1);
        }
        String resultado = "";
        if(fin!=-1) {
        	resultado=json.substring(inicio + 1, fin);
        }
        return resultado;
    }

    @Override
    public String toString() {
        return "PredictionResult{" +
               "max=" + temperaturaMax +
               ", min=" + temperaturaMin +
               ", media=" + temperaturaMedia +
               ", nubosidad=" + nubosidad +
               ", prob_lluvia=" + probabilidadLluvia +
               ", recomendacion='" + recomendacion + '\'' +
               '}';
    }
}
