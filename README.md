# Sistema Multiagente para Predicción Meteorológica

## Autores
- Pablo Álvarez Avendaño
- Álvaro Gonzalo Rodríguez
- Asier Rioja Perales
- Carlos Sánchez Herrero
- Javier Ustáriz García

## Descripción

Sistema multiagente desarrollado con **JADE** que predice las condiciones meteorológicas del día siguiente en Madrid utilizando **redes neuronales** entrenadas con datos históricos.

Sistema diseñado para ser desplegado a una hora del día en la cual la temperatura sea media (sobre las 12:00, 00:00) y trabajar en segundo plano realizando una predicción de los datos meteorológicos del día siguiente y alertando en caso de condiciones extremas.

## Arquitectura

### Agentes

- **PerceptionAgent** (Percepción)  
  Obtiene datos meteorológicos actuales y los envía a MLAgent.

- **MLAgent** (Procesamiento)  
  Ejecuta modelos de red neuronal para procesar los datos recibidos y obtener una predicción que envía a AlertAgent e InterfaceAgent.

- **AlertAgent** (Notificación)  
  Genera alertas por condiciones extremas basándose en los datos recibidos y las envía a InterfaceAgent.

- **InterfaceAgent** (Interfaz)  
  Muestra predicciones y alertas recibidas en una ventana gráfica.

### Comunicación

- **Directory Facilitator (DF)**
  - Todos los agentes se registran en el DF de JADE al iniciarse, indicando el tipo de servicio que ofrecen
- **Mensajes ACL**
  - Los agentes se comunican mediante mensajes ACL de JADE
- **Tipo de Mensaje**
  - Todos los mensajes contemplados entre los agentes implementados son de tipo INFORM
- **Ontologías**
  - Los agentes envían mensajes utilizando ontologías de JADE para comunicarse
- **Formato JSON**
  - El contenido de los mensajes se envia en formato JSON
- **Filtros bloqueantes**
  - Los agentes utilizan MessageTemplate con filtros bloqueantes para recibir únicamente mensajes del tipo y ontología que les interese

Ver [DiagramaArquitectura.svg](docs/DiagramaArquitectura.svg)

### Tecnologías

- **Java 8 - 21** + **JADE 4.6** 
- **Python 3.8 - 3.11** 
- **OpenWeatherMap API** (En caso de que caduque la API key renovarla en el codigo)

### Modelos de predicción

Se entrenaron 6 redes neuronales para predecir:

| Variable | Tipo |
|----------|------|
| Temperatura máxima | Regresión |
| Temperatura mínima | Regresión |
| Temperatura media | Regresión |
| Nubosidad | Regresión |
| Velocidad del viento | Regresión |
| Probabilidad de lluvia | Clasificación binaria |

## Estructura del proyecto

```plaintext
Practica-SistemasInteligentes/
├── docs/ # Documentación
│ ├── arquitectura.svg
│ ├── declaracion-IA.md
│ └── instrucciones.md
├── modelo/ # Código Python
│ ├── modelos/ # Redes entrenadas (.keras) 
│ ├── normalizadores/ # Configuaraciones de Normalizacion (.pkl) 
│ ├── entrenar_modelos.py
│ ├── Madrid Daily Weather 1997-2015.csv
│ ├── prediccion.py
│ └── requirements.txt 
├── lib/ # Librerías JADE
│ ├── commons-codec-1.3.jar
│ └── jade.jar
├── src/main/java/es/upm/ssii/practica/prediccionClima # Código Java
│ ├── agents/ #Codigo de los Agentes
│ │ ├── AlertAgent.java
│ │ ├── InterfaceAgent.java
│ │ ├── MLAgent.java
│ │ └── PerceptionAgent.java
│ ├── behaviours/ #Codigo del coportamiento los Agentes
│ │ ├── AlertBehaviour.java
│ │ ├── InterfaceBehaviour.java
│ │ ├── MLBehaviour.java
│ │ └── PerceptionBehaviour.java
│ ├── connectors/ #Codigo de los conectores
│ │ ├── PythonConnector.java
│ │ └── WeatherConnector.java
│ ├── launcher/ #Codigo base de los agentes, tipos de agentes del sistema y main
│ │ ├── AgentBase.java
│ │ ├── AgentModel.java
│ │ └── Main.java
│ ├── models/ 
│ │ └── PredictionResult.java
│ ├── ui/ 
│ │ └── JFrameResultado.java
│ ├── utils/ #Funciones 
│ │ ├── Utils.java #Mensajeria
│ │ └── UtilsUI.java #Representacion de la informacion en interfaz grafica
├── .gitignore
├── pom.xml # Configuración Maven
└── README.md
```
Nota:
Los directorios **modelos/** y **normalizadores/** se crean automáticamente al ejecutar entrenar_modelos.py

## Instalación y ejecución

Ver [instrucciones.md](docs/instrucciones.md)

## Presentación

Ver [Presentacion.pptx](docs/Presentacion.pptx)