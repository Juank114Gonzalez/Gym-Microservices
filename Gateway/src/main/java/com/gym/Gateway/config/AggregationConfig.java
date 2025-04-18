package com.gym.Gateway.config;

import com.gym.Gateway.filter.AggregationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AggregationConfig {

    private final AggregationFilter aggregationFilter;
    
    public AggregationConfig(WebClient.Builder webClientBuilder) {
        this.aggregationFilter = new AggregationFilter(webClientBuilder);
    }
    
    @Bean
    public RouteLocator aggregationRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("miembro-agregado", r -> r
                .path("/miembro-agregado/**")
                .filters(f -> f.filter(aggregationFilter))
                .uri("no://op"))
            .build();
    }
} 