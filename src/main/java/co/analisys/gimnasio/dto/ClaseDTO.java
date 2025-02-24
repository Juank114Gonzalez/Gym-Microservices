package co.analisys.gimnasio.dto;

import lombok.Data;

@Data
public class ClaseDTO {
    private Long id;
    private String nombre;
    private HorarioDTO horario;
    private int capacidadMaxima;
    private Long entrenadorId;
    private String nombreEntrenador;
    private EspecialidadDTO especialidadEntrenador;
} 