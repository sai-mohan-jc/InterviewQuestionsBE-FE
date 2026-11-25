package com.banking.audit.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AuditWebClientService {

    private final WebClient notificationWebClient;

    public AuditWebClientService(
            WebClient notificationWebClient) {
        this.notificationWebClient = notificationWebClient;
    }

    public CompletableFuture<String> sendAuditNotification() {

        return notificationWebClient.get()
                .uri("/api/notifications/send?message=AuditCompleted")
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response ->
                    System.out.println(
                        "Response received on: " +
                        Thread.currentThread().getName()
                    )
                )
                .toFuture();
    }
}
