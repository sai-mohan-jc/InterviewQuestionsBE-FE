
package com.banking.gateway.config;

import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

@Configuration
public class JwtConfig {

    @Bean
    public ReactiveJwtDecoder jwtDecoder() throws Exception {

        ClassPathResource resource =
                new ClassPathResource("certs/jwt-public.cer");

        CertificateFactory factory =
                CertificateFactory.getInstance("X.509");

        try (InputStream inputStream = resource.getInputStream()) {

            X509Certificate certificate =
                    (X509Certificate) factory.generateCertificate(inputStream);

            RSAPublicKey publicKey =
                    (RSAPublicKey) certificate.getPublicKey();

            NimbusReactiveJwtDecoder decoder =
                    NimbusReactiveJwtDecoder.withPublicKey(publicKey).build();

            decoder.setJwtValidator(
                    JwtValidators.createDefaultWithIssuer("banking-auth-service")
            );

            return decoder;
        }
    }
}
