package com.gym.entrenadores.repository;

import com.gym.entrenadores.model.Entrenador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntrenadorRepository extends JpaRepository<Entrenador, Long> {
}
