package com.banking.audit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient notificationRestClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8091")
                .build();
    }
}