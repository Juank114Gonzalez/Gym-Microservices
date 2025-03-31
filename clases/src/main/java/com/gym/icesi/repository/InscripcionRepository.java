package com.gym.icesi.repository;

import com.gym.icesi.model.Clase;
import com.gym.icesi.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> findByMiembroId(Long miembroId);
    Inscripcion findByMiembroIdAndClase(Long miembroId, Clase clase);
}