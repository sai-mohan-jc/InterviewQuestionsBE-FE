package com.banking.payment.orchestration.dto;

public record ScreeningResponse(
        String paymentId,
        String decision,
        String reason
) {
}