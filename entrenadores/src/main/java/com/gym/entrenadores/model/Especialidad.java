package com.gym.entrenadores.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Especialidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;
    
    @JsonBackReference
    @OneToOne(mappedBy = "especialidad")
    private Entrenador entrenador;
}
