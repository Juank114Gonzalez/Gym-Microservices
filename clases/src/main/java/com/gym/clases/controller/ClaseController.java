package com.gym.clases.controller;

import com.gym.clases.model.Clase;
import com.gym.clases.service.ClaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clases")
@Tag(name = "Clases", description = "API para gestionar las clases del gimnasio")
public class ClaseController {

    @Autowired
    private ClaseService claseService;

    @Operation(
        summary = "Programar una nueva clase",
        description = "Crea una nueva clase en el sistema. Solo accesible para administradores y entrenadores.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Clase programada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la clase inválidos"),
            @ApiResponse(responseCode = "403", description = "No autorizado para programar clases")
        }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER')")
    @PostMapping
    public ResponseEntity<Clase> programarClase(@RequestBody Clase clase) {
        try {
            Clase nuevaClase = claseService.programarClase(clase);
            return ResponseEntity.ok(nuevaClase);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary = "Obtener todas las clases",
        description = "Recupera la lista de todas las clases programadas. Accesible para todos los usuarios autenticados.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de clases recuperada exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado para ver clases")
        }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER', 'ROLE_MEMBER')")
    @GetMapping
    public ResponseEntity<List<Clase>> obtenerTodasClases() {
        List<Clase> clases = claseService.obtenerTodasClases();
        return ResponseEntity.ok(clases);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/seeder")
    public ResponseEntity<Boolean> cargarDatos() {
        Boolean cositas = claseService.cargarDatosEjemplo();
        return ResponseEntity.ok(cositas);
    }
} 