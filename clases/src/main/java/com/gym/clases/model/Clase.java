package com.gym.clases.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import com.gym.clases.dto.EspecialidadDTO;
import lombok.Data;

@Data
@Entity
public class Clase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private int capacidadMaxima;
    private Long entrenadorId;
    
    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "horario_id")
    private Horario horario;
    
    @Transient
    private String nombreEntrenador;
    
    @Transient
    private EspecialidadDTO especialidadEntrenador;
}
