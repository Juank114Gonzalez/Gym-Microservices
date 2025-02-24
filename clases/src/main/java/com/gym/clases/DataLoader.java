package com.gym.clases;

import com.gym.clases.model.Clase;
import com.gym.clases.model.Horario;
import com.gym.clases.service.ClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClaseService claseService;
    
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            // Verificar si el servicio de entrenadores está disponible
            restTemplate.getForObject("http://localhost:8082/entrenadores/1", Object.class);
            
            // Si llegamos aquí, el servicio de entrenadores está disponible
            cargarDatosEjemplo();
        } catch (Exception e) {
            System.out.println("El servicio de entrenadores no está disponible. Los datos de ejemplo se cargarán cuando el servicio esté activo.");
        }
    }

    private void cargarDatosEjemplo() {
        // Crear clases de ejemplo
        Clase yogaMatutina = new Clase();
        yogaMatutina.setNombre("Yoga Matutino");
        Horario horarioYoga = new Horario();
        horarioYoga.setFechaHora(LocalDateTime.now().plusDays(1).withHour(8).withMinute(0));
        horarioYoga.setDuracionMinutos(60);
        yogaMatutina.setHorario(horarioYoga);
        yogaMatutina.setCapacidadMaxima(20);
        yogaMatutina.setEntrenadorId(1L);
        claseService.programarClase(yogaMatutina);

        Clase spinningVespertino = new Clase();
        spinningVespertino.setNombre("Spinning Vespertino");
        Horario horarioSpinning = new Horario();
        horarioSpinning.setFechaHora(LocalDateTime.now().plusDays(1).withHour(18).withMinute(0));
        horarioSpinning.setDuracionMinutos(45);
        spinningVespertino.setHorario(horarioSpinning);
        spinningVespertino.setCapacidadMaxima(15);
        spinningVespertino.setEntrenadorId(2L);
        claseService.programarClase(spinningVespertino);

        Clase pilates = new Clase();
        pilates.setNombre("Pilates Intermedio");
        Horario horarioPilates = new Horario();
        horarioPilates.setFechaHora(LocalDateTime.now().plusDays(2).withHour(10).withMinute(0));
        horarioPilates.setDuracionMinutos(50);
        pilates.setHorario(horarioPilates);
        pilates.setCapacidadMaxima(12);
        pilates.setEntrenadorId(3L);
        claseService.programarClase(pilates);

        Clase crossfit = new Clase();
        crossfit.setNombre("CrossFit Avanzado");
        Horario horarioCrossfit = new Horario();
        horarioCrossfit.setFechaHora(LocalDateTime.now().plusDays(2).withHour(17).withMinute(0));
        horarioCrossfit.setDuracionMinutos(55);
        crossfit.setHorario(horarioCrossfit);
        crossfit.setCapacidadMaxima(10);
        crossfit.setEntrenadorId(4L);
        claseService.programarClase(crossfit);

        System.out.println("Datos de ejemplo de clases cargados exitosamente.");
    }
} 