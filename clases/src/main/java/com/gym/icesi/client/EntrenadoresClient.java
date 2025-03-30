package com.gym.icesi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gym.icesi.config.FeignConfig;
import com.gym.icesi.dto.EntrenadorDTO;

@FeignClient(name = "entrenadores-config" , url = "http://localhost:8082", configuration = FeignConfig.class)
public interface EntrenadoresClient { 

    @GetMapping("/entrenadores/{id}")
    public EntrenadorDTO getOneEntrenador(@PathVariable("id") Long id);

    @GetMapping("/entrenadores/public/status")
    public String checkForEntrenadoresHealth();
}