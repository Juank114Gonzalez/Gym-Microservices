package com.gym.icesi.service;


import com.gym.icesi.model.Miembro;
import com.gym.icesi.repository.MiembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MiembroService {
    @Autowired
    private MiembroRepository miembroRepository;

    public Miembro registrarMiembro(Miembro miembro) {
        return miembroRepository.save(miembro);
    }

    public List<Miembro> obtenerTodosMiembros() {
        return miembroRepository.findAll();
    }

    public Miembro obtenerMiembroPorId(Long id) {
        return miembroRepository.findById(id).orElse(null);
    }

    public void enviarNotificacion(String notificacion) {
        // Simular envío de notificación
        System.out.println("Notificación enviada: " + notificacion);
    }
}
