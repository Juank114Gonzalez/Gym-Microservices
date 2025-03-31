package com.gym.icesi.service;

import com.gym.icesi.dto.EntrenadorDTO;
import com.gym.icesi.dto.InscripcionDTO;
import com.gym.icesi.dto.ClaseRegistradaDTO;
import com.gym.icesi.model.Clase;
import com.gym.icesi.model.Inscripcion;
import com.gym.icesi.model.Horario;
import com.gym.icesi.repository.ClaseRepository;
import com.gym.icesi.repository.InscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.annotation.Transactional;
import com.gym.icesi.client.EntrenadoresClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import java.time.LocalDate;

import com.gym.icesi.model.Horario;
import java.time.LocalDateTime;

import java.util.List;
import com.gym.icesi.client.MiembrosClient;
import com.gym.icesi.dto.MiembroDTO;

@Service
public class ClaseService {
    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EntrenadoresClient entrenadoresClient;

    @Autowired
    private MiembrosClient miembrosClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private KafkaTemplate<String, ClaseRegistradaDTO> kafkaTemplate;

    private final String CLASSES_TOPIC_NAME = "ocupacion-clases";

    @Transactional
    public Clase programarClase(Clase clase) {
        if (clase.getHorario() == null) {
            throw new IllegalArgumentException("La clase debe tener un horario programado");
        }
        
        // Verificar si el entrenador existe y obtener sus datos
        try {
            EntrenadorDTO entrenador = entrenadoresClient.getOneEntrenador(clase.getEntrenadorId());
            
            if (entrenador != null) {
                clase.setNombreEntrenador(entrenador.getNombre());
                clase.setEspecialidadEntrenador(entrenador.getEspecialidad());
                clase.getHorario().setClase(clase);

                return claseRepository.save(clase);
            } else {
                throw new RuntimeException("El entrenador con ID " + clase.getEntrenadorId() + " no existe");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar el entrenador con ID " + clase.getEntrenadorId());
        }
    }

    public List<Clase> obtenerTodasClases() {
        List<Clase> clases = claseRepository.findAll();
        
        for (Clase clase : clases) {
            try {
                EntrenadorDTO entrenador = entrenadoresClient.getOneEntrenador(clase.getEntrenadorId());
                
                if (entrenador != null) {
                    clase.setNombreEntrenador(entrenador.getNombre());
                    clase.setEspecialidadEntrenador(entrenador.getEspecialidad());
                }
            } catch (Exception e) {
                clase.setNombreEntrenador("No disponible");
                clase.setEspecialidadEntrenador(null);
            }
        }
        
        return clases;
    }

    
    @Transactional
    public Clase editarHorario(Horario horario, Long id) {
        Clase clase = claseRepository.findById(id).orElse(null);
        if (clase == null) {
            throw new RuntimeException("La clase con ID " + id + " no existe");
        }

        clase.setHorario(horario);
        String notificacion="";

        if(horario.getFechaHora().isBefore(LocalDateTime.now())){
            notificacion="imposible cambiar a la fecha :"+horario.getFechaHora();
            rabbitTemplate.convertAndSend("dlx-exchange", "dlq", notificacion);
            System.out.println("NOTIFICACION ENVIADA A COLA MUERTA");
            throw new RuntimeException("La fecha y hora de la clase no puede ser anterior a la fecha y hora actual");
        }

        notificacion = "La clase " + clase.getNombre() + " ha cambiado de horario a las " + horario.getFechaHora();
        rabbitTemplate.convertAndSend("clase.exchange", "horario.routingkey",
        notificacion);
        return claseRepository.save(clase);   
    }

   


    private void saveClase(Clase claseNueva) {
        if (claseNueva.getHorario() == null) {
            throw new IllegalArgumentException("La clase debe tener un horario programado");
        }
        
        // Verificar si el entrenador existe y obtener sus datos
        try {
            EntrenadorDTO entrenador = entrenadoresClient.getOneEntrenador(claseNueva.getEntrenadorId());
            
            if (entrenador != null) {
                claseNueva.setNombreEntrenador(entrenador.getNombre());
                claseNueva.setEspecialidadEntrenador(entrenador.getEspecialidad());
                claseNueva.getHorario().setClase(claseNueva);
                claseRepository.save(claseNueva);
            } else {
                throw new RuntimeException("El entrenador con ID " + claseNueva.getEntrenadorId() + " no existe");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar el entrenador con ID " + claseNueva.getEntrenadorId());
        }
    }
    

    @Transactional
    public Inscripcion inscribirMiembro(InscripcionDTO inscripcionDTO) {
        Clase clase = claseRepository.findById(inscripcionDTO.getClaseId())
            .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        // Verificar si hay cupo disponible
        if (clase.getOcupacionActual() >= clase.getCapacidadMaxima()) {
            throw new RuntimeException("La clase está llena");
        }

        // Verificar si el miembro existe
        MiembroDTO miembro = miembrosClient.getOneMiembro(inscripcionDTO.getMiembroId());
        if (miembro == null) {
            throw new RuntimeException("Miembro no encontrado");
        }

        // Verificar si el miembro ya está inscrito
        if (inscripcionRepository.findByMiembroIdAndClase(inscripcionDTO.getMiembroId(), clase) != null) {
            throw new RuntimeException("El miembro ya está inscrito en esta clase");
        }

        // Crear la inscripción
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setMiembroId(inscripcionDTO.getMiembroId());
        inscripcion.setClase(clase);
        inscripcion.setFecha(LocalDate.now());

        // Actualizar la ocupación de la clase
        clase.setOcupacionActual(clase.getOcupacionActual() + 1);
        claseRepository.save(clase);

        // Notificar por Kafka la actualización de ocupación
        ClaseRegistradaDTO claseRegistrada = ClaseRegistradaDTO.builder()
            .id(clase.getId())
            .nombre(clase.getNombre())
            .capacidadMaxima(clase.getCapacidadMaxima())
            .entrenadorId(clase.getEntrenadorId())
            .ocupacionActual(clase.getOcupacionActual())
            .nombreEntrenador(clase.getNombreEntrenador())
            .build();
        kafkaTemplate.send("ocupacion-clases", claseRegistrada);

        // Notificar al miembro por RabbitMQ
        String mensaje = "El miembro con ID " + inscripcionDTO.getMiembroId() + " se ha inscrito a la clase " + clase.getNombre();
        rabbitTemplate.convertAndSend("clase.exchange", "inscripcion.routingkey", mensaje);

        return inscripcionRepository.save(inscripcion);
    }

    @Transactional
    public Boolean cargarDatosEjemplo() {
        // Crear clases de ejemplo
        Clase yogaMatutina = new Clase();
        yogaMatutina.setNombre("Yoga Matutino");
        Horario horarioYoga = new Horario();
        horarioYoga.setFechaHora(LocalDateTime.now().plusDays(1).withHour(8).withMinute(0));
        horarioYoga.setDuracionMinutos(60);
        yogaMatutina.setHorario(horarioYoga);
        yogaMatutina.setCapacidadMaxima(20);
        yogaMatutina.setEntrenadorId(1L);
        saveClase(yogaMatutina);

        Clase spinningVespertino = new Clase();
        spinningVespertino.setNombre("Spinning Vespertino");
        Horario horarioSpinning = new Horario();
        horarioSpinning.setFechaHora(LocalDateTime.now().plusDays(1).withHour(18).withMinute(0));
        horarioSpinning.setDuracionMinutos(45);
        spinningVespertino.setHorario(horarioSpinning);
        spinningVespertino.setCapacidadMaxima(15);
        spinningVespertino.setEntrenadorId(2L);
        saveClase(spinningVespertino);

        Clase pilates = new Clase();
        pilates.setNombre("Pilates Intermedio");
        Horario horarioPilates = new Horario();
        horarioPilates.setFechaHora(LocalDateTime.now().plusDays(2).withHour(10).withMinute(0));
        horarioPilates.setDuracionMinutos(50);
        pilates.setHorario(horarioPilates);
        pilates.setCapacidadMaxima(12);
        pilates.setEntrenadorId(3L);
        saveClase(pilates);

        Clase crossfit = new Clase();
        crossfit.setNombre("CrossFit Avanzado");
        Horario horarioCrossfit = new Horario();
        horarioCrossfit.setFechaHora(LocalDateTime.now().plusDays(2).withHour(17).withMinute(0));
        horarioCrossfit.setDuracionMinutos(55);
        crossfit.setHorario(horarioCrossfit);
        crossfit.setCapacidadMaxima(10);
        crossfit.setEntrenadorId(4L);
        saveClase(crossfit);

        System.out.println("Datos de ejemplo de clases cargados exitosamente.");

        return true;
    }
}
