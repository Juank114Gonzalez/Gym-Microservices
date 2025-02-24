package com.gym.clases.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaHora;
    private Integer duracionMinutos;
    
    @JsonBackReference
    @OneToOne(mappedBy = "horario")
    private Clase clase;
}
