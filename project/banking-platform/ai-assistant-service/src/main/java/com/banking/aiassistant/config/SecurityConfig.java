package com.banking.aiassistant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		return http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth

				// Public health check
				.requestMatchers("/api/ai/health").permitAll()

				// Customer AI APIs
				.requestMatchers(HttpMethod.POST, "/api/ai/**").hasRole("CUSTOMER")
				.requestMatchers(HttpMethod.GET, "/api/ai/rag/test-auth").hasRole("CUSTOMER")
				// MCP discovery API
				.requestMatchers(HttpMethod.GET, "/api/ai/mcp/tools").hasRole("CUSTOMER")

				// All other endpoints denied
				.anyRequest().denyAll())
				.oauth2ResourceServer(
						oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationTokenConverter())))
				.build();
	}

	@Bean
	public org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter jwtAuthenticationTokenConverter() {

		var converter = new org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter();

		converter.setJwtGrantedAuthoritiesConverter(new JwtRoleConverter());

		return converter;
	}
}