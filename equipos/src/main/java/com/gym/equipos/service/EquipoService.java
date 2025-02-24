package com.gym.equipos.service;


import com.gym.equipos.model.Equipo;
import com.gym.equipos.repository.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipoService {
    @Autowired
    private EquipoRepository equipoRepository;

    public Equipo agregarEquipo(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    public List<Equipo> obtenerTodosEquipos() {
        return equipoRepository.findAll();
    }
}
