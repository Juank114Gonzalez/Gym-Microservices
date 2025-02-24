package com.gym.miembros;

import com.gym.miembros.model.Miembro;
import com.gym.miembros.model.Inscripcion;
import com.gym.miembros.repository.MiembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private MiembroRepository miembroRepository;

    @Override
    public void run(String... args) throws Exception {
        // Cargar miembros de ejemplo
        Miembro miembro1 = new Miembro();
        miembro1.setNombre("Juan Pérez");
        miembro1.setEmail("juan@email.com");
        Inscripcion inscripcion1 = new Inscripcion();
        inscripcion1.setFecha(LocalDate.now());
        miembro1.setInscripcion(inscripcion1);
        inscripcion1.setMiembro(miembro1);
        miembroRepository.save(miembro1);

        Miembro miembro2 = new Miembro();
        miembro2.setNombre("María López");
        miembro2.setEmail("maria@email.com");
        Inscripcion inscripcion2 = new Inscripcion();
        inscripcion2.setFecha(LocalDate.now().minusDays(30));
        miembro2.setInscripcion(inscripcion2);
        inscripcion2.setMiembro(miembro2);
        miembroRepository.save(miembro2);

        Miembro miembro3 = new Miembro();
        miembro3.setNombre("Pedro Sánchez");
        miembro3.setEmail("pedro@email.com");
        Inscripcion inscripcion3 = new Inscripcion();
        inscripcion3.setFecha(LocalDate.now().minusDays(15));
        miembro3.setInscripcion(inscripcion3);
        inscripcion3.setMiembro(miembro3);
        miembroRepository.save(miembro3);

        Miembro miembro4 = new Miembro();
        miembro4.setNombre("Ana García");
        miembro4.setEmail("ana@email.com");
        Inscripcion inscripcion4 = new Inscripcion();
        inscripcion4.setFecha(LocalDate.now().minusDays(45));
        miembro4.setInscripcion(inscripcion4);
        inscripcion4.setMiembro(miembro4);
        miembroRepository.save(miembro4);

        System.out.println("Datos de miembros cargados exitosamente.");
    }
}
