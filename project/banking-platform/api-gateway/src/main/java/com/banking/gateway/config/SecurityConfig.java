
package com.banking.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.banking.gateway.filter.SessionValidationWebFilter;
import com.banking.gateway.security.JwtRoleConverter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	private final SessionValidationWebFilter sessionValidationWebFilter;

	public SecurityConfig(SessionValidationWebFilter sessionValidationWebFilter) {
		this.sessionValidationWebFilter = sessionValidationWebFilter;
	}

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, JwtRoleConverter jwtRoleConverter) {

		ReactiveJwtAuthenticationConverter jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();

		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtRoleConverter);

		return http.csrf(ServerHttpSecurity.CsrfSpec::disable)

				.authorizeExchange(exchange -> exchange

						// Public authentication APIs
						.pathMatchers("/api/auth/register", "/api/auth/login", "/api/auth/refresh", "/api/auth/logout")
						.permitAll()

						// Auth Service test/dashboard APIs
						.pathMatchers("/api/auth/customer/**").hasRole("CUSTOMER").pathMatchers("/api/auth/reviewer/**")
						.hasRole("REVIEWER").pathMatchers("/api/auth/admin/**").hasRole("ADMIN")
						.pathMatchers("/api/auth/profile").authenticated()
						// AI health check
						.pathMatchers("/api/ai/health").permitAll()

						// Banking AI Assistant - authenticated customers only
						.pathMatchers("/api/ai/**").hasRole("CUSTOMER")

						// Customer Service
						.pathMatchers(HttpMethod.GET, "/api/customers/**").hasAnyRole("CUSTOMER", "ADMIN")

						.pathMatchers(HttpMethod.POST, "/api/customers/**").hasRole("ADMIN")

						// Account Service - read access only
						.pathMatchers(HttpMethod.GET, "/api/accounts/**").hasAnyRole("CUSTOMER", "ADMIN")

						.pathMatchers("/api/accounts/**").denyAll()

						// Payment Orchestration
						.pathMatchers(HttpMethod.POST, "/api/payments/**").hasRole("CUSTOMER")

						.pathMatchers(HttpMethod.GET, "/api/payments/**").hasAnyRole("CUSTOMER", "ADMIN")

						.pathMatchers("/api/payments/**").denyAll()

						// Centralised Earmarking / Review
						.pathMatchers("/api/review-cases/**").hasAnyRole("REVIEWER", "ADMIN")

						// Internal services must not be exposed through Gateway
						.pathMatchers("/api/transactions/**", "/api/screening/**", "/api/internal/**").denyAll()

						// Health check
						.pathMatchers("/actuator/**").permitAll()

						// Deny unmatched endpoints
						.anyExchange().denyAll())

				.oauth2ResourceServer(
						oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
				.addFilterAfter(sessionValidationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)

				.build();
	}
}
