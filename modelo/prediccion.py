# ============================================
# IMPORTS
# ============================================

# Para leer argumentos de línea de comandos (los datos que llegan de Java)
import sys

# Para obtener la ruta del script y construir rutas absolutas a los modelos y normalizadores
import os

# Para cargar los normalizadores guardados tras el entrenamiento y normalizar los datos de entrada
import joblib

# Para cargar los 5 modelos de red neuronal guardados
from tensorflow.keras.models import load_model

# ======================
# OBTENER RUTAS
# ======================

#Ruta donde esta el script
script_dir = os.path.dirname(os.path.abspath(__file__))

#Rutas a las carpetas donde estan los modelos y normalizadores
normalizadores_dir = os.path.join(script_dir, 'normalizadores')
modelos_dir = os.path.join(script_dir, 'modelos')

# ============================================
# CARGAR MODELOS Y NORMALIZADORES
# ============================================

try:

    # Cargar normalizador de ENTRADAS
    scaler_X = joblib.load(os.path.join(normalizadores_dir, 'scaler_X.pkl'))

    # Cargar normalizadores de las salidas (para desnormalizar)
    scaler_temp_max = joblib.load(os.path.join(normalizadores_dir, 'scaler_temp_max.pkl'))
    scaler_temp_min = joblib.load(os.path.join(normalizadores_dir, 'scaler_temp_min.pkl'))
    scaler_temp_mean = joblib.load(os.path.join(normalizadores_dir, 'scaler_temp_mean.pkl'))
    scaler_cloud = joblib.load(os.path.join(normalizadores_dir, 'scaler_cloud.pkl'))

    # Cargar los 5 modelos entrenados
    model_temp_max = load_model(os.path.join(modelos_dir, 'mejor_modelo_temp_max.keras'))
    model_temp_min = load_model(os.path.join(modelos_dir, 'mejor_modelo_temp_min.keras'))
    model_temp_mean = load_model(os.path.join(modelos_dir, 'mejor_modelo_temp_mean.keras'))
    model_cloud = load_model(os.path.join(modelos_dir, 'mejor_modelo_cloud.keras'))
    model_rain = load_model(os.path.join(modelos_dir, 'mejor_modelo_rain.keras'))

except Exception as e:
    print(f"ERROR al cargar modelos: {e}", file=sys.stderr)
    print("0,0,0,0,0", file=sys.stdout)
    sys.exit(1)

# ==================================================================
# RECIBIR LOS DATOS Y REALIZAR PREDICCION CON LOS MODELOS ENTRENADOS
# ==================================================================

def main():
    # Verificar que se recibieron datos
    if len(sys.argv) < 2:
        print("0,0,0,0,0", file=sys.stdout)
        return

    # Leer datos desde Java
    try:
        datos_str = sys.argv[1]
        datos = [float(x) for x in datos_str.split(',')]

        # Verificar que tenemos 4 datos
        if len(datos) != 4:
            print("0,0,0,0,0", file=sys.stdout)
            return

    except Exception as e:
        print(f"Error al parsear datos: {e}", file=sys.stderr)
        print("0,0,0,0,0", file=sys.stdout)
        return

    # ========================================
    # NORMALIZAR LOS DATOS DE ENTRADA
    # ========================================

    #Aplicar la misma normalizacion que a los datos del entanamiento
    datos_norm = scaler_X.transform([datos])

    # ========================================
    # HACER PREDICCIONES
    # ========================================

    # Los modelos predicen valores normalizados
    pred_max_norm = model_temp_max.predict(datos_norm, verbose=0)[0][0]
    pred_min_norm = model_temp_min.predict(datos_norm, verbose=0)[0][0]
    pred_mean_norm = model_temp_mean.predict(datos_norm, verbose=0)[0][0]
    pred_cloud_norm = model_cloud.predict(datos_norm, verbose=0)[0][0]

    # Predicciones de fenómenos (probabilidades)
    pred_rain = model_rain.predict(datos_norm, verbose=0)[0][0]

    # ========================================
    # DESNORMALIZAR LAS PREDICCIONES
    # ========================================

    # Convertir de valores normalizados a valores REALES
    temp_max = scaler_temp_max.inverse_transform([[pred_max_norm]])[0][0]
    temp_min = scaler_temp_min.inverse_transform([[pred_min_norm]])[0][0]
    temp_mean = scaler_temp_mean.inverse_transform([[pred_mean_norm]])[0][0]
    cloud_cover = scaler_cloud.inverse_transform([[pred_cloud_norm]])[0][0]

    # ========================================
    # DEVOLVER RESULTADO PARA JAVA
    # ========================================

    # Formato: temp_max,temp_min,temp_mean,cloud_cover,llovera

    print(f"{temp_max:.1f},{temp_min:.1f},{temp_mean:.1f},{cloud_cover:.0f},{pred_rain:.2f}", file=sys.stdout)

if __name__ == "__main__":
    main()