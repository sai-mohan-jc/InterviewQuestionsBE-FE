
package com.banking.gateway.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;

@Component
public class JwtRoleConverter
        implements Converter<Jwt, Flux<GrantedAuthority>> {

    @Override
    public Flux<GrantedAuthority> convert(Jwt jwt) {

        String role = jwt.getClaimAsString("role");

        if (role == null || role.isBlank()) {
            return Flux.empty();
        }

        return Flux.just(
                new SimpleGrantedAuthority("ROLE_" + role)
        );
    }
}
