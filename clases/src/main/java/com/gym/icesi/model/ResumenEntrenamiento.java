package com.gym.icesi.model;

import lombok.Data;

@Data
public class ResumenEntrenamiento {
    private int totalEntrenamientos; // Número total de entrenamientos procesados
    private int duracionTotal; // Suma total de la duración de los entrenamientos
    private int caloriasQuemadasTotal; // Suma total de las calorías quemadas
    private int frecuenciaCardiacaPromedio; // Promedio de la frecuencia cardíaca

    public ResumenEntrenamiento() {
        this.totalEntrenamientos = 0;
        this.duracionTotal = 0;
        this.caloriasQuemadasTotal = 0;
        this.frecuenciaCardiacaPromedio = 0;
    }

    // Método para actualizar el resumen con un nuevo "DatosEntrenamiento"
    public ResumenEntrenamiento actualizar(DatosEntrenamiento datosEntrenamiento) {
        this.totalEntrenamientos += 1;
        this.duracionTotal += datosEntrenamiento.getDuracion();
        this.caloriasQuemadasTotal += datosEntrenamiento.getCaloriasQuemadas();

        // Actualizar el promedio ponderado de la frecuencia cardíaca
        this.frecuenciaCardiacaPromedio = (this.frecuenciaCardiacaPromedio * (this.totalEntrenamientos - 1)
                + datosEntrenamiento.getFrecuenciaCardiacaPromedio()) / this.totalEntrenamientos;

        return this;
    }
}
