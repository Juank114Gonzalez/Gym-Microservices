package com.gym.miembros.controller;

import com.gym.miembros.model.Miembro;
import com.gym.miembros.service.MiembroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/miembros")
public class MiembroController {
    @Autowired
    private MiembroService miembroService;
    

    @PostMapping()
    public Miembro registrarMiembro(@RequestBody Miembro miembro) {
        // La inscripción se maneja automáticamente en el servicio si no se proporciona
        return miembroService.registrarMiembro(miembro);
    }

    @GetMapping()
    public List<Miembro> obtenerTodosMiembros() {
        return miembroService.obtenerTodosMiembros();
    }
}
