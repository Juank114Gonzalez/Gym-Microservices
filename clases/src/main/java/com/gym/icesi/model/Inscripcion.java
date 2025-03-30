package com.gym.icesi.model;

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
    
    @Column(unique = true, nullable = false)
    private Long miembroId;

    @ManyToOne
    @JoinColumn(name = "clase_id", unique = true, nullable = false)
    private Clase clase;    
} 