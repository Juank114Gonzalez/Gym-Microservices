package com.gym.clases.controller;

import com.gym.clases.model.Clase;
import com.gym.clases.service.ClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clases")
public class ClaseController {

    @Autowired
    private ClaseService claseService;

    @PostMapping
    public ResponseEntity<Clase> programarClase(@RequestBody Clase clase) {
        try {
            Clase nuevaClase = claseService.programarClase(clase);
            return ResponseEntity.ok(nuevaClase);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Clase>> obtenerTodasClases() {
        List<Clase> clases = claseService.obtenerTodasClases();
        return ResponseEntity.ok(clases);
    }
} 