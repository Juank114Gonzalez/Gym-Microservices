package com.gym.entrenadores.controller;

import com.gym.entrenadores.model.Entrenador;
import com.gym.entrenadores.service.EntrenadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entrenadores")
public class EntrenadorController {
    @Autowired
    private EntrenadorService entrenadorService;
    

    @PostMapping()
    public Entrenador agregarEntrenador(@RequestBody Entrenador entrenador) {
        return entrenadorService.agregarEntrenador(entrenador);
    }

    @GetMapping()
    public List<Entrenador> obtenerTodosEntrenadores() {
        return entrenadorService.obtenerTodosEntrenadores();
    }

    @GetMapping("/{id}/existe")
    public boolean verificarExistencia(@PathVariable Long id) {
        return entrenadorService.existeEntrenador(id);
    }

    @GetMapping("/{id}")
    public Entrenador obtenerEntrenador(@PathVariable Long id) {
        return entrenadorService.obtenerEntrenador(id);
    }
}
