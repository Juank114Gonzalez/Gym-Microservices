package com.gym.Gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/token")
    public ResponseEntity<Map<String, String>> testToken(@RequestHeader HttpHeaders headers) {
        Map<String, String> response = new HashMap<>();
        
        // Verificar si hay un token de autorización en la solicitud
        String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null) {
            response.put("token", "presente");
            // Solo mostrar los primeros 20 caracteres para seguridad
            response.put("token_preview", authHeader.substring(0, Math.min(authHeader.length(), 20)) + "...");
        } else {
            response.put("token", "ausente");
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/miembro/{id}")
    public Mono<ResponseEntity<Map>> testMiembroCall(
            @PathVariable String id,
            @RequestHeader HttpHeaders headers) {
        
        String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        
        return webClientBuilder.build()
                .get()
                .uri("lb://miembros-service/miembros/miembro/" + id)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> ResponseEntity.ok(response))
                .onErrorResume(e -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Error llamando al microservicio: " + e.getMessage());
                    return Mono.just(ResponseEntity.status(500).body(error));
                });
    }
    
    @GetMapping("/entrenador/{id}")
    public Mono<ResponseEntity<Map>> testEntrenadorCall(
            @PathVariable String id,
            @RequestHeader HttpHeaders headers) {
        
        String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        
        return webClientBuilder.build()
                .get()
                .uri("lb://entrenadores-service/entrenadores/" + id)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> ResponseEntity.ok(response))
                .onErrorResume(e -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Error llamando al microservicio: " + e.getMessage());
                    return Mono.just(ResponseEntity.status(500).body(error));
                });
    }
} 