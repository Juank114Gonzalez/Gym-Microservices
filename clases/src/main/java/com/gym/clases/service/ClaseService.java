package com.gym.clases.service;

import com.gym.clases.dto.EntrenadorDTO;
import com.gym.clases.model.Clase;
import com.gym.clases.repository.ClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClaseService {
    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public Clase programarClase(Clase clase) {
        if (clase.getHorario() == null) {
            throw new IllegalArgumentException("La clase debe tener un horario programado");
        }
        
        // Verificar si el entrenador existe y obtener sus datos
        try {
            EntrenadorDTO entrenador = restTemplate.getForObject(
                "http://localhost:8082/entrenadores/" + clase.getEntrenadorId(),
                EntrenadorDTO.class);
            
            if (entrenador != null) {
                clase.setNombreEntrenador(entrenador.getNombre());
                clase.setEspecialidadEntrenador(entrenador.getEspecialidad());
                clase.getHorario().setClase(clase);
                return claseRepository.save(clase);
            } else {
                throw new RuntimeException("El entrenador con ID " + clase.getEntrenadorId() + " no existe");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar el entrenador con ID " + clase.getEntrenadorId());
        }
    }

    public List<Clase> obtenerTodasClases() {
        List<Clase> clases = claseRepository.findAll();
        
        for (Clase clase : clases) {
            try {
                EntrenadorDTO entrenador = restTemplate.getForObject(
                    "http://localhost:8082/entrenadores/" + clase.getEntrenadorId(),
                    EntrenadorDTO.class);
                
                if (entrenador != null) {
                    clase.setNombreEntrenador(entrenador.getNombre());
                    clase.setEspecialidadEntrenador(entrenador.getEspecialidad());
                }
            } catch (Exception e) {
                clase.setNombreEntrenador("No disponible");
                clase.setEspecialidadEntrenador(null);
            }
        }
        
        return clases;
    }
}
