package com.gym.icesi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import com.gym.icesi.dto.EspecialidadDTO;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Clase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private int capacidadMaxima;
    private Long entrenadorId;
    private int ocupacionActual = 0;
    
    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "horario_id")
    private Horario horario;
    
    @Transient
    private String nombreEntrenador;
    
    @Transient
    private EspecialidadDTO especialidadEntrenador;
    
    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Inscripcion> inscripciones = new ArrayList<>();
}
