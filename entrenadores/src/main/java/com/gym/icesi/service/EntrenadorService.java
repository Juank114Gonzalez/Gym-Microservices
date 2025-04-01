package com.gym.icesi.service;

import com.gym.icesi.model.Entrenador;
import com.gym.icesi.model.Especialidad;
import com.gym.icesi.model.ResumenEntrenamiento;
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

    @KafkaListener(topics = {CLASSES_TOPIC_NAME}, groupId = "classes-streaming")
    public void notificacionClasesTiempoReal(ClaseRegistradaDTO clase){
        System.out.println("Alguien se ha registrado a una clase:  " + clase.toString());
    }

}
