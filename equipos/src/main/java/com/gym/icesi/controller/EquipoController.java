package com.gym.icesi.controller;

import com.gym.icesi.model.Equipo;
import com.gym.icesi.service.EquipoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipos")
@Tag(name = "Equipos", description = "API para gestionar los equipos del gimnasio")
public class EquipoController {
    @Autowired
    private EquipoService equipoService;
    
    @Operation(
        summary = "Agregar nuevo equipo",
        description = "Agrega un nuevo equipo al gimnasio. Solo accesible para administradores.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Equipo agregado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del equipo inválidos"),
            @ApiResponse(responseCode = "403", description = "No autorizado para agregar equipos")
        }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public Equipo agregarEquipo(@RequestBody Equipo equipo) {
        return equipoService.agregarEquipo(equipo);
    }

    @Operation(
        summary = "Obtener todos los equipos",
        description = "Recupera la lista de todos los equipos del gimnasio. Accesible para todos los usuarios autenticados.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de equipos recuperada exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado para ver equipos")
        }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER', 'ROLE_MEMBER')")
    @GetMapping()
    public List<Equipo> obtenerTodosEquipos() {
        return equipoService.obtenerTodosEquipos();
    }
}
