package com.gym.entrenadores;

import com.gym.entrenadores.model.Entrenador;
import com.gym.entrenadores.model.Especialidad;
import com.gym.entrenadores.repository.EntrenadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EntrenadorDataLoader implements CommandLineRunner {

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    @Override
    public void run(String... args) throws Exception {
        // Cargar entrenadores de ejemplo
        Entrenador entrenador1 = new Entrenador();
        entrenador1.setNombre("Carlos Rodríguez");
        Especialidad especialidad1 = new Especialidad();
        especialidad1.setNombre("Yoga");
        especialidad1.setDescripcion("Instructor certificado en Hatha y Vinyasa Yoga");
        entrenador1.setEspecialidad(especialidad1);
        especialidad1.setEntrenador(entrenador1);
        entrenadorRepository.save(entrenador1);

        Entrenador entrenador2 = new Entrenador();
        entrenador2.setNombre("Ana Martínez");
        Especialidad especialidad2 = new Especialidad();
        especialidad2.setNombre("Spinning");
        especialidad2.setDescripcion("Instructora certificada en ciclismo indoor");
        entrenador2.setEspecialidad(especialidad2);
        especialidad2.setEntrenador(entrenador2);
        entrenadorRepository.save(entrenador2);

        Entrenador entrenador3 = new Entrenador();
        entrenador3.setNombre("Pedro Sánchez");
        Especialidad especialidad3 = new Especialidad();
        especialidad3.setNombre("CrossFit");
        especialidad3.setDescripcion("Entrenador certificado nivel 2 en CrossFit");
        entrenador3.setEspecialidad(especialidad3);
        especialidad3.setEntrenador(entrenador3);
        entrenadorRepository.save(entrenador3);

        Entrenador entrenador4 = new Entrenador();
        entrenador4.setNombre("Laura Gómez");
        Especialidad especialidad4 = new Especialidad();
        especialidad4.setNombre("Pilates");
        especialidad4.setDescripcion("Instructora certificada en Pilates mat y reformer");
        entrenador4.setEspecialidad(especialidad4);
        especialidad4.setEntrenador(entrenador4);
        entrenadorRepository.save(entrenador4);

        System.out.println("Datos de entrenadores cargados exitosamente.");
    }
}