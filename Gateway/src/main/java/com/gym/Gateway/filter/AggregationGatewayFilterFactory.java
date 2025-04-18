package com.gym.Gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@Component
public class AggregationGatewayFilterFactory extends AbstractGatewayFilterFactory<AggregationGatewayFilterFactory.Config> {

    private final WebClient.Builder webClientBuilder;

    public AggregationGatewayFilterFactory(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("enabled");
    }

    @Override
    public GatewayFilter apply(Config config) {
        if (!config.isEnabled()) {
            return (exchange, chain) -> chain.filter(exchange);
        }
        
        return new AggregationFilter(webClientBuilder);
    }

    public static class Config {
        private boolean enabled;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
} 