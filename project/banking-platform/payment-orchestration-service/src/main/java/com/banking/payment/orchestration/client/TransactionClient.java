package com.banking.payment.orchestration.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.banking.payment.orchestration.dto.PaymentRequest;

@Component
public class TransactionClient {

    private final RestClient restClient;

    public TransactionClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:8083")
                .build();
    }

    public Object executeTransfer(
            String paymentId,
            PaymentRequest request) {

        return restClient.post()
                .uri("/api/transactions/transfer")
                .header("Idempotency-Key", paymentId)
                .body(new TransactionRequest(
                        request.fromAccount(),
                        request.toAccount(),
                        request.amount()))
                .retrieve()
                .body(Object.class);
    }

    public record TransactionRequest(
            String fromAccount,
            String toAccount,
            java.math.BigDecimal amount) {
    }
}
