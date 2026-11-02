package com.banking.payment.orchestration.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.banking.payment.orchestration.dto.ScreeningRequest;
import com.banking.payment.orchestration.dto.ScreeningResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScreeningClient {

    private final RestClient.Builder restClientBuilder;

    public ScreeningResponse screen(ScreeningRequest request) {
        return restClientBuilder
                .baseUrl("http://localhost:8084")
                .build()
                .post()
                .uri("/api/screening")
                .body(request)
                .retrieve()
                .body(ScreeningResponse.class);
    }
}