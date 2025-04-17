package com.gym.Gateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.core.env.Environment;

@Configuration
public class GatewayConfig {

    private final Environment env;

    public GatewayConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("miembros-service", r -> r
                        .path("/miembros/**")
                        .uri("http://localhost:8081"))
                .route("entrenadores-service", r -> r
                        .path("/entrenadores/**")
                        .uri("http://localhost:8082"))
                .route("clases-service", r -> r
                        .path("/clases/**")
                        .uri("http://localhost:8083"))
                .route("equipos-service", r -> r
                        .path("/equipos/**")
                        .uri("http://localhost:8084"))
                .build();
    }

    @Bean
    public GlobalFilter customGlobalFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader != null) {
                return chain.filter(exchange.mutate()
                        .request(request.mutate()
                                .header(HttpHeaders.AUTHORIZATION, authHeader)
                                .build())
                        .build());
            }
            return chain.filter(exchange);
        };
    }
}