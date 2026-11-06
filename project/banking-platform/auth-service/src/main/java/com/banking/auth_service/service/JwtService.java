package com.banking.auth_service.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.banking.auth_service.entity.Role;


@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(
            String username,
            String role,
            String customerCif,
            String sessionId) {

        Instant now = Instant.now();

        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .issuer("banking-auth-service")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .subject(username)
                .claim("role", role)
                .claim("sid", sessionId);

        // Add CIF only for CUSTOMER users
        if ("CUSTOMER".equals(role)) {

            if (customerCif == null || customerCif.isBlank()) {
                throw new IllegalStateException(
                        "Customer CIF is required for CUSTOMER role");
            }

            claimsBuilder.claim("customerCif", customerCif);
        }

        JwtClaimsSet claims = claimsBuilder.build();

        JwsHeader header = JwsHeader
                .with(SignatureAlgorithm.RS256)
                .keyId("jwt-signing")
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();
    }
}
