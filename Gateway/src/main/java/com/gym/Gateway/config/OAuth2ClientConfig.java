package com.gym.Gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class OAuth2ClientConfig {

    @Value("http://localhost:8080/realms/gimnasio")
    private String issuerUri;

    @Bean
    public WebClient webClient(ReactiveClientRegistrationRepository clientRegistrations,
                              ServerOAuth2AuthorizedClientRepository authorizedClients) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth =
            new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrations, authorizedClients);
        
        oauth.setDefaultOAuth2AuthorizedClient(true);
        
        return WebClient.builder()
            .filter(oauth)
            .build();
    }
} 