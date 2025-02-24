package co.analisys.gimnasio.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HorarioDTO {
    private Long id;
    private LocalDateTime fechaHora;
    private Integer duracionMinutos;
} 