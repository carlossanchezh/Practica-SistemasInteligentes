# ==============================================================
# ADAPTACIONES REALIZADAS EN LA RED NEURONAL PARA PREDECIR DATOS
# ==============================================================

# 1. ARQUITECTURA DEL MODELO
#       - Flatten para 4 características numéricas

# 2. FUNCIONES DE ACTIVACIÓN
#       - linear para regresión (temperaturas, nubosidad)
#       - sigmoid para clasificación binaria (lluvia sí/no)

# 3. FUNCIONES DE PÉRDIDA (LOSS)
#       - mse para regresión
#       - binary_crossentropy para clasificación binaria

# 4. OPTIMIZADOR
#       - Adam

# 5. REGULARIZACIÓN
#       - Dropout
#       - EarlyStopping

# 6. NORMALIZACIÓN
#       - StandardScaler para datos

# 7. FORMATO DE GUARDADO
#       - .keras (formato nativo de Keras, requerido por TF 2.21+)
#       - .hdf5 no soportado por TensorFlow 2.21

# ================================================
# CONFIGURACION DE LA RED NEURONAL Y ENTRENAMIENTO
# ================================================

# Parámetros del modelo

EPOCHS = 50           # Número de épocas de entrenamiento
BATCH_SIZE = 32       # Tamaño del lote
PATIENCE_STOP = 10    # Paciencia para EarlyStopping
PATIENCE_REDUCE = 5   #Paciencia en ReduceLROnPlateau
DROPOUT_RATE = 0.3    # Tasa de Dropout

# Parámetros de la red neuronal
NEURONAS_CAPA1 = 128  # Neuronas en la primera capa oculta
NEURONAS_CAPA2 = 64   # Neuronas en la segunda capa oculta
NEURONAS_CAPA3 = 32   # Neuronas en la tercera capa oculta

# Parámetros del modelo para un mayor rendimiento en el entrenamiento
'''
EPOCHS = 300
BATCH_SIZE = 8
PATIENCE_STOP = 30    
PATIENCE_REDUCE = 10  
DROPOUT_RATE = 0.15

NEURONAS_CAPA1 = 512
NEURONAS_CAPA2 = 256
NEURONAS_CAPA3 = 128
'''


# ============================================
# IMPORTS PARA TRATAMIENTO DE DATOS
# ============================================

# Leer y manejar archivos CSV
import pandas as pd

# Operaciones numéricas
import numpy as np

# Crear carpetas
import os

# ==========================================================
# IMPORTS PARA DIVISIÓN DE DATOS ENTRENAMIENTO/VALIDACION
# ==========================================================

# Dividir datos en entrenamiento/validación
from sklearn.model_selection import train_test_split

# ============================================
# IMPORTS PARA NORMALIZACIÓN DE DATOS
# ============================================

# Normalizar datos
from sklearn.preprocessing import StandardScaler

# Guardar normalizadores
import joblib

# ============================================
# IMPORTS PARA RED NEURONAL
# ============================================

# Crear modelo secuencial
from tensorflow.keras.models import Sequential

# Capa densa de neuronas
from tensorflow.keras.layers import Dense

# Técnica para evitar sobreaprendizaje
from tensorflow.keras.layers import Dropout

# Pasar datos a vector de entrada
from tensorflow.keras.layers import Flatten

# Funciones de activación
from tensorflow.keras.layers import Activation

# ============================================
# IMPORTS PARA CALLBACKS
# ============================================

# Detiene entrenamiento si no mejora
from tensorflow.keras.callbacks import EarlyStopping

# Reduce learning rate si se estanca
from tensorflow.keras.callbacks import ReduceLROnPlateau

# Guarda el mejor modelo
from tensorflow.keras.callbacks import ModelCheckpoint

# Termina si aparece error un nulo
from tensorflow.keras.callbacks import TerminateOnNaN

# ==========================
# CREAR CARPETAS NECESARIAS
# ==========================

os.makedirs('modelos', exist_ok=True)
os.makedirs('normalizadores', exist_ok=True)

# ============================================
# CARGAR DATOS
# ============================================

#Dtaset: https://www.kaggle.com/datasets/mahdiehhajian/madrid-daily-weather?resource=download
#Guaradar el csv en una variable
df = pd.read_csv('Madrid Daily Weather 1997-2015.csv')

# ============================================
# LIMPIAR DATOS
# ============================================

#Se eliminan las filas con valores nulos en lso datos que vamos a usar
columnas_necesarias = ['Mean TemperatureC', 'Max Humidity', ' Mean Wind SpeedKm/h', ' CloudCover']
df = df.dropna(subset=columnas_necesarias)

# ============================================
# CREAR VARIABLE OBJETIVO
# ============================================

col_events = ' Events'

# Funcion para detectar si llovio
def tiene_lluvia(evento):
    if pd.isna(evento):
        return False
    return 'Rain' in str(evento)

#Se desplazan hacia arriba todas las columnas para tener el valor del dia siguiente
df['temp_max_manana'] = df['Max TemperatureC'].shift(-1)
df['temp_min_manana'] = df['Min TemperatureC'].shift(-1)
df['temp_mean_manana'] = df['Mean TemperatureC'].shift(-1)
df['cloud_cover_manana'] = df[' CloudCover'].shift(-1)
df['events_manana'] = df[' Events'].shift(-1)

df['lluvia_manana'] = df['events_manana'].apply(tiene_lluvia).astype(int)

# ============================================
# ELIMINAR FILAS CON nulos
# ============================================

#Al hacer shift la ultima fila se quedara con nulos hay que limpiarla
#Eliminar filas donde las columnas tengan nulos
columnas_desplazadas = ['temp_max_manana', 'temp_min_manana', 'temp_mean_manana', 'cloud_cover_manana', 'lluvia_manana']
df = df.dropna(subset=columnas_desplazadas)



# ============================================
# SELECCIONAR FEATURES
# ============================================

#Entradas de la red neuronal
features = ['Mean TemperatureC', 'Max Humidity', ' Mean Wind SpeedKm/h', ' CloudCover']
X = df[features].values

y_temp_max = df['temp_max_manana'].values
y_temp_min = df['temp_min_manana'].values
y_temp_mean = df['temp_mean_manana'].values
y_cloud = df['cloud_cover_manana'].values
y_rain = df['lluvia_manana'].values


# ============================================
# NORMALIZAR DATOS
# ============================================

#Normalizar datos de entrada
scaler_X = StandardScaler()
X_scaled = scaler_X.fit_transform(X)

#Guarda el normalizador para normalizar los datos de entrada
joblib.dump(scaler_X, 'normalizadores/scaler_X.pkl')


# =====================================================
# DIVIDIR ENTRENAMIENTO (80%) Y VALIDACIÓN (20%)
# =====================================================

#El csv original no cuenta con datos divididos para entrenamiento/validadcion hay que realizar esta division
#train_test_split divide aleatoriamente los datos en grupos
X_train, X_val, \
    y_temp_max_train, y_temp_max_val, \
    y_temp_min_train, y_temp_min_val, \
    y_temp_mean_train, y_temp_mean_val, \
    y_cloud_train, y_cloud_val, \
    y_rain_train, y_rain_val = train_test_split(
    X_scaled,
    y_temp_max, y_temp_min, y_temp_mean,
    y_cloud, y_rain,
    test_size=0.2, #20% vaidacion
    random_state=0 #la division de los datos siempre sera la misma en cada entrenamiento
)

# ============================================
# DEFINIR FUNCIÓN PARA CREAR MODELO
# ============================================

#input_dim numero de datos de entrada (columnas)
#output_activation funcion de activacion para la capa de salida
def crear_modelo(input_dim, output_activation):
    model = Sequential()

    model.add(Flatten(input_shape=(input_dim,)))

    # Primera capa oculta
    model.add(Dense(NEURONAS_CAPA1))
    model.add(Activation('relu'))
    model.add(Dropout(DROPOUT_RATE))

    # Segunda capa oculta
    model.add(Dense(NEURONAS_CAPA2))
    model.add(Activation('relu'))
    model.add(Dropout(DROPOUT_RATE))

    # Tercera capa oculta
    model.add(Dense(NEURONAS_CAPA3))
    model.add(Activation('relu'))
    model.add(Dropout(DROPOUT_RATE))

    # Capa de salida
    model.add(Dense(1))
    model.add(Activation(output_activation))

    model.summary()
    return model

# ============================================
# CONFIGURACIÓN DE CALLBACKS
# ============================================
#Un checkpoint para cada modelo
checkpoint_temp_max = ModelCheckpoint('modelos/mejor_modelo_temp_max.keras', monitor='val_loss', verbose=1, save_best_only=True)
checkpoint_temp_min = ModelCheckpoint('modelos/mejor_modelo_temp_min.keras', monitor='val_loss', verbose=1, save_best_only=True)
checkpoint_temp_mean = ModelCheckpoint('modelos/mejor_modelo_temp_mean.keras', monitor='val_loss', verbose=1, save_best_only=True)
checkpoint_cloud = ModelCheckpoint('modelos/mejor_modelo_cloud.keras', monitor='val_loss', verbose=1, save_best_only=True)
checkpoint_rain = ModelCheckpoint('modelos/mejor_modelo_rain.keras', monitor='val_loss', verbose=1, save_best_only=True)

early_stop = EarlyStopping(monitor='val_loss', patience=PATIENCE_STOP, verbose=1)
reduce_lr = ReduceLROnPlateau('val_loss', factor=0.1, patience=PATIENCE_REDUCE, verbose=1)
terminate = TerminateOnNaN()

# ============================================
# ENTRENAR MODELO 1: temp_max
# ============================================
#Normalizador especifico para la temperatura maxima
scaler_temp_max = StandardScaler()

#Normalizar datos de entranamiento y validarlos
y_train_scaled = scaler_temp_max.fit_transform(y_temp_max_train.reshape(-1, 1)).flatten()
y_val_scaled = scaler_temp_max.transform(y_temp_max_val.reshape(-1, 1)).flatten()

#Guardar normalizador
joblib.dump(scaler_temp_max, 'normalizadores/scaler_temp_max.pkl')

# Crear modelo
model = crear_modelo(X_train.shape[1], 'linear')
model.compile(optimizer='adam', loss='mse', metrics=['mae'])

#Entrenar modelo
model.fit(X_train, y_train_scaled, validation_data=(X_val, y_val_scaled),
          epochs=EPOCHS, batch_size=BATCH_SIZE, callbacks = [checkpoint_temp_max, reduce_lr, early_stop, terminate], verbose=1)

# ============================================
# ENTRENAR MODELO 2: temp_min
# ============================================

#Normalizador especifico para la temperatura minima
scaler_temp_min = StandardScaler()

#Normalizar datos de entrenamiento y validacion
y_train_scaled = scaler_temp_min.fit_transform(y_temp_min_train.reshape(-1, 1)).flatten()
y_val_scaled = scaler_temp_min.transform(y_temp_min_val.reshape(-1, 1)).flatten()

#Guardar normalizador
joblib.dump(scaler_temp_min, 'normalizadores/scaler_temp_min.pkl')

# Crear modelo
model = crear_modelo(X_train.shape[1], 'linear')
model.compile(optimizer='adam', loss='mse', metrics=['mae'])

#Entrenar modelo
model.fit(X_train, y_train_scaled, validation_data=(X_val, y_val_scaled),
          epochs=EPOCHS, batch_size=BATCH_SIZE, callbacks = [checkpoint_temp_min, reduce_lr, early_stop, terminate], verbose=1)

# ============================================
# ENTRENAR MODELO 3: temp_mean
# ============================================

#Normalizador especifico para la temperatura media
scaler_temp_mean = StandardScaler()

#Normalizar datos de entrenamiento y validacion
y_train_scaled = scaler_temp_mean.fit_transform(y_temp_mean_train.reshape(-1, 1)).flatten()
y_val_scaled = scaler_temp_mean.transform(y_temp_mean_val.reshape(-1, 1)).flatten()

#Guardar normalizador
joblib.dump(scaler_temp_mean, 'normalizadores/scaler_temp_mean.pkl')

# Crear modelo
model = crear_modelo(X_train.shape[1], 'linear')
model.compile(optimizer='adam', loss='mse', metrics=['mae'])

#Entrenar modelo
model.fit(X_train, y_train_scaled, validation_data=(X_val, y_val_scaled),
          epochs=EPOCHS, batch_size=BATCH_SIZE, callbacks = [checkpoint_temp_mean, reduce_lr, early_stop, terminate], verbose=1)

# ============================================
# ENTRENAR MODELO 4: cloud_cover
# ============================================

#Normalizador especifico para la nubosidad
scaler_cloud = StandardScaler()

#Normalizar datos de entrenamiento y validacion
y_train_scaled = scaler_cloud.fit_transform(y_cloud_train.reshape(-1, 1)).flatten()
y_val_scaled = scaler_cloud.transform(y_cloud_val.reshape(-1, 1)).flatten()

#Guardar normalizador
joblib.dump(scaler_cloud, 'normalizadores/scaler_cloud.pkl')

# Crear modelo
model = crear_modelo(X_train.shape[1], 'linear')
model.compile(optimizer='adam', loss='mse', metrics=['mae'])

#Entrenar modelo
model.fit(X_train, y_train_scaled, validation_data=(X_val, y_val_scaled),
          epochs=EPOCHS, batch_size=BATCH_SIZE, callbacks = [checkpoint_cloud, reduce_lr, early_stop, terminate], verbose=1)

# ============================================
# ENTRENAR MODELO 5: rain
# ============================================

#Para clasificacion binaria no se normaliza la salida (es 0 o 1)

# Crear modelo con activacion sigmoid (para probabilidad)
model = crear_modelo(X_train.shape[1], 'sigmoid')
model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])

#Entrenar modelo
model.fit(X_train, y_rain_train, validation_data=(X_val, y_rain_val),
          epochs=EPOCHS, batch_size=BATCH_SIZE, callbacks = [checkpoint_rain, reduce_lr, early_stop, terminate], verbose=1)
