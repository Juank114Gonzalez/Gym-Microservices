package com.gym.entrenadores.dto;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ClaseRegistradaDTO implements Serializable {
    private String nombre;
    private int capacidadMaxima;
    private Long entrenadorId;
}