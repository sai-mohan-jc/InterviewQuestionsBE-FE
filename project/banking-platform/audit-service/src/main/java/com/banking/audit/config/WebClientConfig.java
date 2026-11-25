package com.banking.audit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient notificationWebClient() {

        return WebClient.builder()
                .baseUrl("http://localhost:8091")
                .build();
    }
}