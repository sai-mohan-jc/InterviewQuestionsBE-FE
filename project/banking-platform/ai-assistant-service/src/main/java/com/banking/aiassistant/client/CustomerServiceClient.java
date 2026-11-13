package com.banking.aiassistant.client;

import com.banking.aiassistant.dto.CustomerProfileResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CustomerServiceClient {

    private final RestClient customerRestClient;

    public CustomerServiceClient(@Qualifier("customerRestClient")RestClient customerRestClient) {
        this.customerRestClient = customerRestClient;
    }

    public CustomerProfileResponse getCustomerProfile(
            String cif, String accessToken) {

        return customerRestClient.get()
                .uri("/api/customers/{cif}", cif)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                )
                .retrieve()
                .body(CustomerProfileResponse.class);
    }
}