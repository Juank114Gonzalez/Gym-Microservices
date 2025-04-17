package com.gym.Gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TokenRelayGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final ReactiveOAuth2AuthorizedClientService clientService;

    public TokenRelayGatewayFilterFactory(ReactiveOAuth2AuthorizedClientService clientService) {
        super(Object.class);
        this.clientService = clientService;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> exchange.getPrincipal()
                .filter(principal -> principal instanceof OAuth2AuthenticationToken)
                .cast(OAuth2AuthenticationToken.class)
                .flatMap(this::getAccessToken)
                .map(token -> withBearerAuth(exchange, token))
                .defaultIfEmpty(exchange)
                .flatMap(chain::filter);
    }

    private Mono<String> getAccessToken(OAuth2AuthenticationToken oauthToken) {
        String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
        return clientService.loadAuthorizedClient(clientRegistrationId, oauthToken.getName())
                .map(OAuth2AuthorizedClient::getAccessToken)
                .map(token -> token.getTokenValue());
    }

    private ServerWebExchange withBearerAuth(ServerWebExchange exchange, String token) {
        log.debug("Relaying OAuth2 token to downstream services");
        return exchange.mutate()
                .request(r -> r.headers(headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token)))
                .build();
    }

    @Override
    public String name() {
        return "TokenRelay";
    }
} 