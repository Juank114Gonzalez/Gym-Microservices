package co.analisys.gimnasio.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MiembroDTO {
    private Long id;
    private String nombre;
    private String email;
    private InscripcionDTO inscripcion;
} 