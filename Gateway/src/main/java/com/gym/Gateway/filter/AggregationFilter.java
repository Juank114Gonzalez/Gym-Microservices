package com.gym.Gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.core.ParameterizedTypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AggregationFilter implements GatewayFilter {
    
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;
    private final Pattern idPattern = Pattern.compile("/miembro-agregado/(\\d+)");
    
    public AggregationFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        Matcher matcher = idPattern.matcher(path);
        
        if (matcher.matches()) {
            String idMiembro = matcher.group(1);
            return aggregateResponses(exchange, idMiembro);
        }
        return chain.filter(exchange);
    }
    
    private Mono<Void> aggregateResponses(ServerWebExchange exchange, String idMiembro) {
        // Obtener el token de autorización del request original
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        
        // Llamar al servicio de miembros para obtener información básica
        Mono<Map<String, Object>> miembroInfo = webClientBuilder.build()
                .get()
                .uri("lb://miembros-service/miembros/miembro/" + idMiembro)
                .header(HttpHeaders.AUTHORIZATION, authHeader) // Propagar el token
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .onErrorResume(e -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("error", "No se pudo obtener información del miembro: " + e.getMessage());
                    return Mono.just(error);
                });
        
        // Llamar al servicio de clases para obtener las clases inscritas del miembro
        Mono<Map<String, Object>> clasesInfo = webClientBuilder.build()
                .get()
                .uri("lb://clases-service/clases/miembro/" + idMiembro)
                .header(HttpHeaders.AUTHORIZATION, authHeader) // Propagar el token
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .onErrorResume(e -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("error", "No se pudo obtener información de clases: " + e.getMessage());
                    return Mono.just(error);
                });
        
        // Llamar al servicio de entrenadores para obtener información del entrenador asignado al miembro
        Mono<Map<String, Object>> entrenadorInfo = webClientBuilder.build()
                .get()
                .uri("lb://entrenadores-service/entrenadores/miembro/" + idMiembro)
                .header(HttpHeaders.AUTHORIZATION, authHeader) // Propagar el token
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .onErrorResume(e -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("error", "No se pudo obtener información del entrenador: " + e.getMessage());
                    return Mono.just(error);
                });
        
        // Combinar todas las respuestas
        return Mono.zip(miembroInfo, clasesInfo, entrenadorInfo)
                .flatMap(tuple -> {
                    Map<String, Object> result = new HashMap<>();
                    
                    // Agregar información del miembro
                    result.put("miembro", tuple.getT1());
                    
                    // Agregar información de clases (limpiando datos redundantes)
                    Map<String, Object> clasesData = new HashMap<>(tuple.getT2());
                    if (clasesData.containsKey("clasesInscritas") && clasesData.get("clasesInscritas") instanceof List) {
                        List<Map<String, Object>> clasesLimpiadas = new ArrayList<>();
                        ((List<Map<String, Object>>) clasesData.get("clasesInscritas")).forEach(clase -> {
                            Map<String, Object> claseSimplificada = new HashMap<>(clase);
                            
                            // Eliminar campos redundantes
                            claseSimplificada.remove("nombreEntrenador");
                            claseSimplificada.remove("especialidadEntrenador");
                            
                            // Simplificar inscripciones si existen
                            if (claseSimplificada.containsKey("inscripciones")) {
                                List<Map<String, Object>> inscripciones = (List<Map<String, Object>>) claseSimplificada.get("inscripciones");
                                claseSimplificada.put("cantidadInscripciones", inscripciones.size());
                                
                                // Solo mantener IDs de inscripciones
                                List<Long> inscripcionesIds = new ArrayList<>();
                                inscripciones.forEach(inscripcion -> {
                                    if (inscripcion.containsKey("id")) {
                                        inscripcionesIds.add(((Number) inscripcion.get("id")).longValue());
                                    }
                                });
                                claseSimplificada.put("inscripcionesIds", inscripcionesIds);
                                claseSimplificada.remove("inscripciones");
                            }
                            
                            clasesLimpiadas.add(claseSimplificada);
                        });
                        clasesData.put("clasesInscritas", clasesLimpiadas);
                    }
                    result.put("clases", clasesData);
                    
                    // Agregar información del entrenador
                    result.put("entrenador", tuple.getT3());
                    
                    try {
                        byte[] responseBody = objectMapper.writeValueAsBytes(result);
                        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                        exchange.getResponse().setStatusCode(HttpStatus.OK);
                        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(responseBody);
                        return exchange.getResponse().writeWith(Mono.just(buffer));
                    } catch (JsonProcessingException e) {
                        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                        return exchange.getResponse().setComplete();
                    }
                })
                .onErrorResume(e -> {
                    exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    String errorMessage = "Error al agregar datos: " + e.getMessage();
                    byte[] responseBody;
                    try {
                        responseBody = objectMapper.writeValueAsBytes(Map.of("error", errorMessage));
                    } catch (JsonProcessingException ex) {
                        responseBody = errorMessage.getBytes();
                    }
                    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(responseBody);
                    return exchange.getResponse().writeWith(Mono.just(buffer));
                });
    }
} 