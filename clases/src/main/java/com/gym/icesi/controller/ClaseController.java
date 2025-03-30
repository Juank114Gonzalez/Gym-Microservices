package com.gym.icesi.controller;

import com.gym.icesi.model.Clase;
import com.gym.icesi.model.Horario;
import com.gym.icesi.service.ClaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.gym.icesi.dto.InscripcionDTO;
import com.gym.icesi.model.Inscripcion;
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

    @Operation(
        summary = "Cambiar horario de una clase",
        description = "Cambiar el horario de una clase existente a partir del id de la misma",
        responses = {
            @ApiResponse(responseCode = "200", description = "Clase programada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la hora inválidos"),
            @ApiResponse(responseCode = "403", description = "No autorizado para programar clases")
        }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER')")
    @PatchMapping("/cambiarHorario/{id}")
    public ResponseEntity<Clase> cambiarHorario(@RequestBody Horario horario, @PathVariable Long id) {
        try {
            Clase claseModificada = claseService.editarHorario(horario, id);
            return ResponseEntity.ok(claseModificada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @Operation(
        summary = "Inscribir un mimebro a una clase",
        description = "Insribe un miembro a una clase existente a partir del id de la clase y el miembro",
        responses = {
            @ApiResponse(responseCode = "200", description = "Inscripción exitosa"),
            @ApiResponse(responseCode = "400", description = "Datos de la inscripción inválidos"),
            @ApiResponse(responseCode = "403", description = "No autorizado para Inscribir miembros")
        }
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER')")
    @PostMapping("/inscribirMiembro")
    public ResponseEntity<Inscripcion> inscribirMiembroEnClase(@RequestBody InscripcionDTO inscripcion) {
        try {
            Inscripcion inscripcionNueva = claseService.inscribirMiembro(inscripcion);
            return ResponseEntity.ok(inscripcionNueva);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/seeder")
    public ResponseEntity<Boolean> cargarDatos() {
        Boolean cositas = claseService.cargarDatosEjemplo();
        return ResponseEntity.ok(cositas);
    }
} 