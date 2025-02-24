package co.analisys.gimnasio.service;

import co.analisys.gimnasio.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class GimnasioService {
    @Autowired
    private RestTemplate restTemplate;

    private static final String MIEMBROS_URL = "http://localhost:8081/miembros";
    private static final String ENTRENADORES_URL = "http://localhost:8082/entrenadores";
    private static final String CLASES_URL = "http://localhost:8083/clases";
    private static final String EQUIPOS_URL = "http://localhost:8084/equipos";

    public MiembroDTO registrarMiembro(MiembroDTO miembro) {
        if (miembro.getInscripcion() == null) {
            InscripcionDTO inscripcion = new InscripcionDTO();
            inscripcion.setFecha(LocalDate.now());
            miembro.setInscripcion(inscripcion);
        }
        return restTemplate.postForObject(MIEMBROS_URL, miembro, MiembroDTO.class);
    }

    public ClaseDTO programarClase(ClaseDTO clase) {
        if (clase.getHorario() == null) {
            throw new IllegalArgumentException("La clase debe tener un horario programado");
        }
        
        if (clase.getHorario().getFechaHora() == null) {
            throw new IllegalArgumentException("El horario debe tener una fecha y hora");
        }
        
        if (clase.getHorario().getDuracionMinutos() == null || 
            clase.getHorario().getDuracionMinutos() <= 0) {
            throw new IllegalArgumentException("El horario debe tener una duración válida en minutos");
        }

        if (clase.getEntrenadorId() == null) {
            throw new IllegalArgumentException("La clase debe tener un entrenador asignado");
        }

        return restTemplate.postForObject(CLASES_URL, clase, ClaseDTO.class);
    }

    public EntrenadorDTO agregarEntrenador(EntrenadorDTO entrenador) {
        if (entrenador.getEspecialidad() != null && 
            entrenador.getEspecialidad().getNombre() == null) {
            throw new IllegalArgumentException("La especialidad debe tener un nombre");
        }
        return restTemplate.postForObject(ENTRENADORES_URL, entrenador, EntrenadorDTO.class);
    }

    public EquipoDTO agregarEquipo(EquipoDTO equipo) {
        return restTemplate.postForObject(EQUIPOS_URL, equipo, EquipoDTO.class);
    }

    public List<MiembroDTO> obtenerTodosMiembros() {
        MiembroDTO[] miembros = restTemplate.getForObject(MIEMBROS_URL, MiembroDTO[].class);
        return miembros != null ? Arrays.asList(miembros) : List.of();
    }

    public List<ClaseDTO> obtenerTodasClases() {
        ClaseDTO[] clases = restTemplate.getForObject(CLASES_URL, ClaseDTO[].class);
        return clases != null ? Arrays.asList(clases) : List.of();
    }

    public List<EntrenadorDTO> obtenerTodosEntrenadores() {
        EntrenadorDTO[] entrenadores = restTemplate.getForObject(ENTRENADORES_URL, EntrenadorDTO[].class);
        return entrenadores != null ? Arrays.asList(entrenadores) : List.of();
    }

    public List<EquipoDTO> obtenerTodosEquipos() {
        EquipoDTO[] equipos = restTemplate.getForObject(EQUIPOS_URL, EquipoDTO[].class);
        return equipos != null ? Arrays.asList(equipos) : List.of();
    }
}
