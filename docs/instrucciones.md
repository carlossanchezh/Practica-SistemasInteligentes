Instrucciones de instalación y ejecución
Requisitos previos

Python 3.8+


Ejecución desde Eclipse
1. Importar el proyecto
Desde Eclipse, ir a File → Import → Git → Projects from Git (with smart import), seleccionar Clone URI e introducir:
https://github.com/carlossanchezh/Practica-SistemasInteligentes.git
Seguir el asistente hasta Finish. Eclipse importará el proyecto automáticamente.
2. Preparar el entorno
Añadir los JARs de la carpeta lib/ al build path del proyecto:

Botón derecho sobre el proyecto → Build Path → Configure Build Path
Pestaña Libraries → Add JARs...
Seleccionar los tres ficheros de lib/:

jade.jar
commons-codec-1.3.jar
json-20210307.jar


Pulsar Apply and Close

3. Ejecutar
Botón derecho sobre la raíz del proyecto → Run As → Java Application → seleccionar Main - es.upm.ssii.practica.prediccionClima.launcher.
Al arrancar, se iniciará la GUI de JADE (RMA) y los tres agentes:

Percepcion — consulta la API de OpenWeatherMap cada 30 segundos y envía los datos por ACL al MLAgent
ML — recibe el mensaje y queda en espera
Interfaz — queda en espera 