package com.banking.aiassistant.config;

import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtConfig {

    @Value("classpath:cert/jwt-public.cer")
    private Resource certificateResource;

    @Bean
    public JwtDecoder jwtDecoder(
            @Value("${app.security.jwt.issuer}") String issuer)
            throws Exception {

        CertificateFactory factory =
                CertificateFactory.getInstance("X.509");

        try (InputStream inputStream =
                     certificateResource.getInputStream()) {

            X509Certificate certificate =
                    (X509Certificate) factory.generateCertificate(inputStream);

            RSAPublicKey publicKey =
                    (RSAPublicKey) certificate.getPublicKey();

            NimbusJwtDecoder decoder =
                    NimbusJwtDecoder.withPublicKey(publicKey).build();

            decoder.setJwtValidator(
                    JwtValidators.createDefaultWithIssuer(issuer));

            return decoder;
        }
    }
}