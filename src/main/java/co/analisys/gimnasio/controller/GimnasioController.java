package co.analisys.gimnasio.controller;

import co.analisys.gimnasio.dto.*;
import co.analisys.gimnasio.service.GimnasioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gimnasio")
public class GimnasioController {
    @Autowired
    private GimnasioService gimnasioService;

    @PostMapping("/miembros")
    public MiembroDTO registrarMiembro(@RequestBody MiembroDTO miembro) {
        return gimnasioService.registrarMiembro(miembro);
    }

    @PostMapping("/clases")
    public ClaseDTO programarClase(@RequestBody ClaseDTO clase) {
        return gimnasioService.programarClase(clase);
    }

    @PostMapping("/entrenadores")
    public EntrenadorDTO agregarEntrenador(@RequestBody EntrenadorDTO entrenador) {
        return gimnasioService.agregarEntrenador(entrenador);
    }

    @PostMapping("/equipos")
    public EquipoDTO agregarEquipo(@RequestBody EquipoDTO equipo) {
        return gimnasioService.agregarEquipo(equipo);
    }

    @GetMapping("/miembros")
    public List<MiembroDTO> obtenerTodosMiembros() {
        return gimnasioService.obtenerTodosMiembros();
    }

    @GetMapping("/clases")
    public List<ClaseDTO> obtenerTodasClases() {
        return gimnasioService.obtenerTodasClases();
    }

    @GetMapping("/entrenadores")
    public List<EntrenadorDTO> obtenerTodosEntrenadores() {
        return gimnasioService.obtenerTodosEntrenadores();
    }

    @GetMapping("/equipos")
    public List<EquipoDTO> obtenerTodosEquipos() {
        return gimnasioService.obtenerTodosEquipos();
    }
}
