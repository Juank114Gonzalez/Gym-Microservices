package com.gym.miembros.service;


import com.gym.miembros.model.Miembro;
import com.gym.miembros.model.Inscripcion;
import com.gym.miembros.repository.MiembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MiembroService {
    @Autowired
    private MiembroRepository miembroRepository;

    public Miembro registrarMiembro(Miembro miembro) {
        if (miembro.getInscripcion() == null) {
            Inscripcion inscripcion = new Inscripcion();
            inscripcion.setFecha(LocalDate.now());
            miembro.setInscripcion(inscripcion);
            inscripcion.setMiembro(miembro);
        }
        return miembroRepository.save(miembro);
    }

    public List<Miembro> obtenerTodosMiembros() {
        return miembroRepository.findAll();
    }
}
