package com.gym.miembros;

import com.gym.miembros.model.Miembro;
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
        miembroRepository.save(miembro1);

        Miembro miembro2 = new Miembro();
        miembro2.setNombre("María López");
        miembro2.setEmail("maria@email.com");
        miembroRepository.save(miembro2);

        Miembro miembro3 = new Miembro();
        miembro3.setNombre("Pedro Sánchez");
        miembro3.setEmail("pedro@email.com");
        miembroRepository.save(miembro3);

        Miembro miembro4 = new Miembro();
        miembro4.setNombre("Ana García");
        miembro4.setEmail("ana@email.com");
        miembroRepository.save(miembro4);

        System.out.println("Datos de miembros cargados exitosamente.");
    }
}
