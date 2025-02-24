package com.gym.equipos;

import com.gym.equipos.model.Equipo;
import com.gym.equipos.repository.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private EquipoRepository equipoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Cargar equipos de ejemplo
        Equipo mancuernas = new Equipo();
        mancuernas.setNombre("Mancuernas");
        mancuernas.setDescripcion("Set de mancuernas de 5kg");
        mancuernas.setCantidad(20);
        equipoRepository.save(mancuernas);

        Equipo bicicleta = new Equipo();
        bicicleta.setNombre("Bicicleta estática");
        bicicleta.setDescripcion("Bicicleta para spinning");
        bicicleta.setCantidad(15);
        equipoRepository.save(bicicleta);

        Equipo pesoRuso = new Equipo();
        pesoRuso.setNombre("Pesa Rusa");
        pesoRuso.setDescripcion("Kettlebell de 10kg");
        pesoRuso.setCantidad(10);
        equipoRepository.save(pesoRuso);

        Equipo colchoneta = new Equipo();
        colchoneta.setNombre("Colchoneta");
        colchoneta.setDescripcion("Colchoneta para yoga y pilates");
        colchoneta.setCantidad(25);
        equipoRepository.save(colchoneta);

        Equipo bandaElastica = new Equipo();
        bandaElastica.setNombre("Banda Elástica");
        bandaElastica.setDescripcion("Banda de resistencia media");
        bandaElastica.setCantidad(30);
        equipoRepository.save(bandaElastica);

        System.out.println("Datos de ejemplo de equipos cargados exitosamente.");
    }
} 