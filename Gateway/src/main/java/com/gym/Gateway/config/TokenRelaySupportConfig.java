package com.gym.Gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;

@Configuration
@Slf4j
public class TokenRelaySupportConfig {

    @Bean
    public GlobalFilter jwtTokenRelayFilter() {
        return new JwtTokenRelayFilter();
    }

    public static class JwtTokenRelayFilter implements GlobalFilter, Ordered {

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            ServerHttpRequest request = exchange.getRequest();
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                log.debug("Relaying JWT token to downstream service");
                ServerHttpRequest mutatedRequest = request.mutate()
                        .header(HttpHeaders.AUTHORIZATION, authHeader)
                        .build();
                
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            }
            
            log.debug("No JWT token found in request headers");
            return chain.filter(exchange);
        }

        @Override
        public int getOrder() {
            // Execute after the SecurityWebFilterChain but before other filters
            return Ordered.HIGHEST_PRECEDENCE + 100;
        }
    }
} 