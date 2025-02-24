package co.analisys.gimnasio.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class InscripcionDTO {
    private Long id;
    private LocalDate fecha;
} 