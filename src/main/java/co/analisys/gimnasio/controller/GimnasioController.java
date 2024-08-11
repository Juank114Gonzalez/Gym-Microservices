package co.analisys.gimnasio.controller;

import co.analisys.gimnasio.model.Clase;
import co.analisys.gimnasio.model.Entrenador;
import co.analisys.gimnasio.model.Equipo;
import co.analisys.gimnasio.model.Miembro;
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
    public Miembro registrarMiembro(@RequestBody Miembro miembro) {
        return gimnasioService.registrarMiembro(miembro);
    }

    @PostMapping("/clases")
    public Clase programarClase(@RequestBody Clase clase) {
        return gimnasioService.programarClase(clase);
    }

    @PostMapping("/entrenadores")
    public Entrenador agregarEntrenador(@RequestBody Entrenador entrenador) {
        return gimnasioService.agregarEntrenador(entrenador);
    }

    @PostMapping("/equipos")
    public Equipo agregarEquipo(@RequestBody Equipo equipo) {
        return gimnasioService.agregarEquipo(equipo);
    }

    @GetMapping("/miembros")
    public List<Miembro> obtenerTodosMiembros() {
        return gimnasioService.obtenerTodosMiembros();
    }

    @GetMapping("/clases")
    public List<Clase> obtenerTodasClases() {
        return gimnasioService.obtenerTodasClases();
    }

    @GetMapping("/entrenadores")
    public List<Entrenador> obtenerTodosEntrenadores() {
        return gimnasioService.obtenerTodosEntrenadores();
    }

    @GetMapping("/equipos")
    public List<Equipo> obtenerTodosEquipos() {
        return gimnasioService.obtenerTodosEquipos();
    }
}
