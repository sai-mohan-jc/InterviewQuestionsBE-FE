package com.banking.payment.orchestration.dto;

import java.math.BigDecimal;

public record CreateReviewRequest(
        String paymentId,
        String fromAccount,
        String toAccount,
        BigDecimal amount,
        String reason
) {}
