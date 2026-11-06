package com.banking.gateway.filter;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class SessionValidationWebFilter implements WebFilter {

    private final WebClient webClient;

    public SessionValidationWebFilter(
            @Value("${auth.service.base-url:http://localhost:8087}") String authServiceUrl,
            @Value("${internal.security.secret}") String internalSecret) {

        this.webClient = WebClient.builder()
                .baseUrl(authServiceUrl)
                .defaultHeader("X-Internal-Secret", internalSecret)
                .build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        return exchange.getPrincipal()
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(optionalPrincipal -> {

                    if (optionalPrincipal.isEmpty()) {
                        return chain.filter(exchange);
                    }

                    Principal principal = optionalPrincipal.get();

                    if (!(principal instanceof JwtAuthenticationToken jwtAuth)) {
                        return chain.filter(exchange);
                    }

                    String sessionId = jwtAuth.getToken().getClaimAsString("sid");

                    if (sessionId == null || sessionId.isBlank()) {
                        return unauthorized(exchange);
                    }

                    return webClient.post()
                            .uri("/api/internal/sessions/validate")
                            .bodyValue(Map.of("sessionId", sessionId))
                            .retrieve()
                            .bodyToMono(SessionValidationResponse.class)
                            .flatMap(response -> response.active()
                                    ? chain.filter(exchange)
                                    : unauthorized(exchange))
                            .onErrorResume(exception -> {
                                // Fail closed if Auth Service is unavailable.
                                exchange.getResponse().setStatusCode(
                                        HttpStatus.SERVICE_UNAVAILABLE);
                                return exchange.getResponse().setComplete();
                            });
                });
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private record SessionValidationResponse(boolean active) {}
}