package com.banking.payment.orchestration.dto;

public record PaymentResponse(
        String paymentId,
        String status,
        String message
) {
}