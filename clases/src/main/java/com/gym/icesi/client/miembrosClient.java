package com.gym.icesi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gym.icesi.config.FeignConfig;
import com.gym.icesi.dto.MiembroDTO;

@FeignClient(name = "miembros-config" , url = "http://localhost:8081/miembros", configuration = FeignConfig.class)
public interface miembrosClient { 

    @GetMapping("/miembro/{id}")
    public MiembroDTO getOneEntrenador(@PathVariable("id") Long id);
}