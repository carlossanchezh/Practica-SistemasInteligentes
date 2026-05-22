package es.upm.ssii.practica.prediccionClima.launcher;

public enum AgentModel {
    PERCEPCION("Percepcion"),
    ML("ML"),
    INTERFAZ("Interfaz"),
    ALERTAS("Alertas"),
    DESCONOCIDO("Desconocido");

    private final String value;

    AgentModel(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static AgentModel getEnum(String value) {
        switch (value) {
            case "Percepcion": return PERCEPCION;
            case "ML": return ML;
            case "Interfaz": return INTERFAZ;
            case "Alertas":    return ALERTAS;
            default: return DESCONOCIDO;
        }
    }
}
