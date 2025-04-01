package com.gym.icesi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatosEntrenamiento {
    private String id; // Identificador único del entrenamiento
    private String usuarioId; // Identificador del usuario al que pertenecen los datos de entrenamiento
    private String tipoEjercicio; // Tipo de ejercicio realizado (e.g., cardio, fuerza)
    private int duracion; // Duración del ejercicio en minutos
    private int caloriasQuemadas; // Cantidad de calorías quemadas
    private int frecuenciaCardiacaPromedio; // Frecuencia cardíaca promedio durante el ejercicio
}
