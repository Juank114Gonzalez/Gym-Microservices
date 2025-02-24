package co.analisys.gimnasio.dto;

import lombok.Data;

@Data
public class EquipoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private int cantidad;
} 