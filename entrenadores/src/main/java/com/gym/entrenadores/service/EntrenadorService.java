package com.gym.entrenadores.service;

import com.gym.entrenadores.model.Entrenador;
import com.gym.entrenadores.model.Especialidad;
import com.gym.entrenadores.repository.EntrenadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntrenadorService {
    @Autowired
    private EntrenadorRepository entrenadorRepository;

    public Entrenador agregarEntrenador(Entrenador entrenador) {
        if (entrenador.getEspecialidad() != null) {
            entrenador.getEspecialidad().setEntrenador(entrenador);
        }
        return entrenadorRepository.save(entrenador);
    }

    public List<Entrenador> obtenerTodosEntrenadores() {
        return entrenadorRepository.findAll();
    }

    public boolean existeEntrenador(Long id) {
        return entrenadorRepository.existsById(id);
    }

    public Entrenador obtenerEntrenador(Long id) {
        return entrenadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado con ID: " + id));
    }
}
