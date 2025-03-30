package com.gym.icesi.controller;

import com.gym.icesi.model.Entrenador;
import com.gym.icesi.service.EntrenadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/entrenadores")
@Tag(name = "Entrenador", description = "API para gestionar entrenadores del gimnasio")
public class EntrenadorController {
    @Autowired
    private EntrenadorService entrenadorService;
    
    @Operation(
        summary = "Agregar un nuevo entrenador",
        description = "Crea un nuevo entrenador en el sistema. Solo accesible para administradores.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Entrenador creado exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado para crear entrenadores"),
            @ApiResponse(responseCode = "400", description = "Datos del entrenador inválidos")
        }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public Entrenador agregarEntrenador(@RequestBody Entrenador entrenador) {
        return entrenadorService.agregarEntrenador(entrenador);
    }

    @Operation(
        summary = "Obtener todos los entrenadores",
        description = "Recupera la lista de todos los entrenadores registrados. Accesible para administradores y entrenadores.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de entrenadores recuperada exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado para ver entrenadores")
        }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER')")
    @GetMapping()
    public List<Entrenador> obtenerTodosEntrenadores() {
        return entrenadorService.obtenerTodosEntrenadores();
    }

    @Operation(
        summary = "Verificar existencia de entrenador",
        description = "Verifica si existe un entrenador con el ID especificado",
        parameters = {
            @Parameter(name = "id", description = "ID del entrenador a verificar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Verificación realizada exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado para verificar entrenadores")
        }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER')")
    @GetMapping("/{id}/existe")
    public boolean verificarExistencia(@PathVariable Long id) {
        return entrenadorService.existeEntrenador(id);
    }

    @Operation(
        summary = "Obtener entrenador por ID",
        description = "Recupera los detalles de un entrenador específico por su ID",
        parameters = {
            @Parameter(name = "id", description = "ID del entrenador a recuperar", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Entrenador encontrado"),
            @ApiResponse(responseCode = "404", description = "Entrenador no encontrado"),
            @ApiResponse(responseCode = "403", description = "No autorizado para ver el entrenador")
        }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER', 'ROLE_MEMBER')")
    @GetMapping("/{id}")
    public Entrenador obtenerEntrenador(@PathVariable Long id) {
        return entrenadorService.obtenerEntrenador(id);
    }

    @Operation(
        summary = "Verificar estado del servicio",
        description = "Endpoint público para verificar si el servicio está funcionando",
        responses = {
            @ApiResponse(responseCode = "200", description = "Servicio funcionando correctamente")
        }
    )
    @GetMapping("/public/status")
    public ResponseEntity<String> getPublicStatus() {
        return ResponseEntity.ok("El servicio de entrenamiento está funcionando correctamente");
    }
}
