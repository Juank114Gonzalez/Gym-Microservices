package com.gym.clases.dto;

import lombok.Data;

@Data
public class EntrenadorDTO {
    private Long id;
    private String nombre;
    private EspecialidadDTO especialidad;
} 