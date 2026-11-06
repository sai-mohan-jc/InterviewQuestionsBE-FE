package com.banking.auth_service.config;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

@Configuration
public class JwtConfig {

    @Value("${jwt.keystore.path}")
    private String keystorePath;

    @Value("${jwt.keystore.password}")
    private String keystorePassword;

    @Value("${jwt.keystore.alias}")
    private String keystoreAlias;

    private KeyStore loadKeyStore() throws Exception {

        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        try (FileInputStream inputStream =
                     new FileInputStream(keystorePath)) {

            keyStore.load(inputStream, keystorePassword.toCharArray());
        }

        return keyStore;
    }

    @Bean
    public JwtEncoder jwtEncoder() throws Exception {

        KeyStore keyStore = loadKeyStore();

        PrivateKey privateKey = (PrivateKey)
                keyStore.getKey(
                        keystoreAlias,
                        keystorePassword.toCharArray()
                );

        Certificate certificate =
                keyStore.getCertificate(keystoreAlias);

        RSAPublicKey publicKey =
                (RSAPublicKey) certificate.getPublicKey();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey((RSAPrivateKey) privateKey)
                .keyID(keystoreAlias)
                .build();

        ImmutableJWKSet<com.nimbusds.jose.proc.SecurityContext> jwkSource =
                new ImmutableJWKSet<>(new JWKSet(rsaKey));

        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {

        KeyStore keyStore = loadKeyStore();

        Certificate certificate =
                keyStore.getCertificate(keystoreAlias);

        RSAPublicKey publicKey =
                (RSAPublicKey) certificate.getPublicKey();

        NimbusJwtDecoder decoder =
                NimbusJwtDecoder.withPublicKey(publicKey).build();

        decoder.setJwtValidator(
                JwtValidators.createDefaultWithIssuer("banking-auth-service")
        );

        return decoder;
    }
}
