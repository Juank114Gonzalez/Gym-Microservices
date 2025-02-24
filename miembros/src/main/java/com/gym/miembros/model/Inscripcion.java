package com.gym.miembros.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fecha;
    
    @OneToOne
    @JoinColumn(name = "miembro_id")
    @JsonBackReference
    private Miembro miembro;
} 