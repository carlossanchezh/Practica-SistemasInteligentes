# Instrucciones de instalación y ejecución
## Requisitos previos

- **Java 8 - 21** 
- **JADE 4.6**
- **Python 3.8 - 3.11** (versiones superiores pueden tener incompatibilidades con TensorFlow)
- **Eclipse IDE** (con soporte para Git y Maven)
- **Conexión a Internet** (para la API de OpenWeatherMap)

---

## Configuración del entorno Python

### 1. Instalar dependencias de Python

Abrir una terminal en la carpeta `modelo/` del proyecto y ejecutar:

```bash
pip install -r requirements.txt
```

### Entrenar los modelos de red neuronal

Abrir una terminal en la carpeta `modelo/` del proyecto y ejecutar:

```bash
python entrenar_modelos.py
```

---

## Ejecución desde Eclipse

### 1. Importar el proyecto
Desde Eclipse, ir a File → Import → Git → Projects from Git (with smart import), seleccionar Clone URI e introducir:
https://github.com/carlossanchezh/Practica-SistemasInteligentes.git \
Seguir el asistente hasta Finish. Eclipse importará el proyecto automáticamente.

### 2. Preparar el entorno

Añadir los JARs de la carpeta lib/ al build path del proyecto: \
Botón derecho sobre el proyecto → Build Path → Configure Build Path
Pestaña Libraries → Add JARs... 

Seleccionar los tres ficheros de lib/:
- jade.jar 
- commons-codec-1.3.jar

Pulsar Apply and Close

### 3. Ejecutar
Botón derecho sobre la raíz del proyecto → Run As → Java Application → seleccionar Main - es.upm.ssii.practica.prediccionClima.launcher. \
Al arrancar, se iniciará la GUI de JADE (RMA) y los cuatro agentes: