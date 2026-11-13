package com.banking.aiassistant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient customerRestClient(
            RestClient.Builder builder,
            @Value("${customer.service.base-url}") String baseUrl) {

        return builder
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    public RestClient accountRestClient(
            RestClient.Builder builder,
            @Value("${account.service.base-url}") String baseUrl) {

        return builder
                .baseUrl(baseUrl)
                .build();
    }
}