package com.banking.audit.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AuditRestClientService {

    private final RestClient notificationRestClient;

    public AuditRestClientService(RestClient notificationRestClient) {
        this.notificationRestClient = notificationRestClient;
    }

    public String sendAuditNotification() {

        System.out.println("RestClient request thread: "
                + Thread.currentThread().getName());

        String response = notificationRestClient.get()
                .uri("/api/notifications/send?message=AuditCompleted")
                .retrieve()
                .body(String.class);

        System.out.println("RestClient response received on: "
                + Thread.currentThread().getName());

        return response;
    }
}