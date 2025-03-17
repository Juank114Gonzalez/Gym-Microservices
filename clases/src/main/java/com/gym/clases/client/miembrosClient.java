package com.gym.clases.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gym.clases.config.FeignConfig;
import com.gym.clases.dto.MiembroDTO;

@FeignClient(name = "miembros-config" , url = "http://localhost:8081/miembros", configuration = FeignConfig.class)
public interface miembrosClient { 

    @GetMapping("/miembro/{id}")
    public MiembroDTO getOneEntrenador(@PathVariable("id") Long id);
}