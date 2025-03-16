package com.gym.miembros.controller;

import com.gym.miembros.model.Miembro;
import com.gym.miembros.service.MiembroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/miembros")
@Tag(name = "Miembros", description = "API para gestionar los miembros del gimnasio")
public class MiembroController {
    @Autowired
    private MiembroService miembroService;
    
    @Operation(
        summary = "Registrar nuevo miembro",
        description = "Registra un nuevo miembro en el sistema. Solo accesible para administradores.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Miembro registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del miembro inválidos"),
            @ApiResponse(responseCode = "403", description = "No autorizado para registrar miembros")
        }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public Miembro registrarMiembro(@RequestBody Miembro miembro) {
        // La inscripción se maneja automáticamente en el servicio si no se proporciona
        return miembroService.registrarMiembro(miembro);
    }

    @Operation(
        summary = "Obtener todos los miembros",
        description = "Recupera la lista de todos los miembros registrados. Accesible para administradores y entrenadores.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de miembros recuperada exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado para ver miembros")
        }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER')")
    @GetMapping()
    public List<Miembro> obtenerTodosMiembros() {
        return miembroService.obtenerTodosMiembros();
    }
}
