package com.gym.icesi.service;

import com.gym.icesi.model.Entrenador;
import com.gym.icesi.dto.ClaseRegistradaDTO;
import com.gym.icesi.repository.EntrenadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;

@Service
public class EntrenadorService {
    @Autowired
    private EntrenadorRepository entrenadorRepository;

    private final String CLASSES_TOPIC_NAME = "ocupacion-clases";

    public void enviarNotificacion(String notificacion) {
        // Simular envío de notificación
        System.out.println("Notificación enviada: " + notificacion);
    }   

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
    
    /**
     * Obtiene el entrenador asignado a un miembro específico
     * En una implementación real, esto consultaría una tabla de asignaciones
     * Esta es una implementación simulada que asigna un entrenador aleatoriamente
     * @param miembroId ID del miembro
     * @return Entrenador asignado al miembro
     */
    public Entrenador obtenerEntrenadorPorMiembro(Long miembroId) {
        // No necesitamos verificar si el miembro existe porque la
        // selección del entrenador se basa solo en el ID del miembro
        // y no utiliza ninguna información adicional del miembro
        
        // Obtenemos todos los entrenadores disponibles
        List<Entrenador> entrenadores = entrenadorRepository.findAll();
        if (entrenadores.isEmpty()) {
            throw new RuntimeException("No hay entrenadores disponibles");
        }
        
        // Usar el ID del miembro para obtener siempre el mismo entrenador para el mismo miembro
        int index = (int)(miembroId % entrenadores.size());
        return entrenadores.get(index);
    }

    @KafkaListener(topics = {CLASSES_TOPIC_NAME}, groupId = "classes-streaming")
    public void notificacionClasesTiempoReal(ClaseRegistradaDTO clase){
        System.out.println("Alguien se ha registrado a una clase:  " + clase.toString());
    }

}
