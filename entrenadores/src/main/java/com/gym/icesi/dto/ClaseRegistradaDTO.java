package com.gym.icesi.dto;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ClaseRegistradaDTO implements Serializable {
    private Long id;
    private String nombre;
    private int capacidadMaxima;
    private Long entrenadorId;
    private int ocupacionActual;
    private String nombreEntrenador;

    @Override
    public String toString() {
        return "ClaseRegistradaDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", capacidadMaxima=" + capacidadMaxima +
                ", entrenadorId=" + entrenadorId +
                ", ocupacionActual=" + ocupacionActual +
                ", nombreEntrenador='" + nombreEntrenador + '\'' +
                '}';
    }
}