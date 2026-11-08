
package com.banking.earmarking.config;

import java.io.InputStream;
import java.net.http.HttpClient;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class AccountServiceRestClientConfig {

    @Bean
    public RestClient accountServiceRestClient(
            RestClient.Builder builder,
            ResourceLoader resourceLoader,
            @Value("${ces.account-service.base-url}") String baseUrl,
            @Value("${ces.mtls.key-store}") String keyStoreLocation,
            @Value("${ces.mtls.key-store-password}") String keyStorePassword,
            @Value("${ces.mtls.trust-store}") String trustStoreLocation,
            @Value("${ces.mtls.trust-store-password}") String trustStorePassword)
            throws Exception {

        // Load CES client keystore
        KeyStore clientKeyStore = loadKeyStore(
                resourceLoader, keyStoreLocation, keyStorePassword);

        KeyManagerFactory keyManagerFactory =
                KeyManagerFactory.getInstance(
                        KeyManagerFactory.getDefaultAlgorithm());

        keyManagerFactory.init(
                clientKeyStore, keyStorePassword.toCharArray());

        // Load CES truststore
        KeyStore trustStore = loadKeyStore(
                resourceLoader, trustStoreLocation, trustStorePassword);

        TrustManagerFactory trustManagerFactory =
                TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm());

        trustManagerFactory.init(trustStore);

        // Configure mutual TLS
        SSLContext sslContext = SSLContext.getInstance("TLS");

        sslContext.init(
                keyManagerFactory.getKeyManagers(),
                trustManagerFactory.getTrustManagers(),
                null);

        HttpClient httpClient = HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();

        return builder
                .baseUrl(baseUrl)
                .requestFactory(
                        new JdkClientHttpRequestFactory(httpClient))
                .build();
    }

    private KeyStore loadKeyStore(
            ResourceLoader resourceLoader,
            String location,
            String password) throws Exception {

        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        Resource resource = resourceLoader.getResource(location);

        try (InputStream inputStream = resource.getInputStream()) {
            keyStore.load(inputStream, password.toCharArray());
        }

        return keyStore;
    }
}
