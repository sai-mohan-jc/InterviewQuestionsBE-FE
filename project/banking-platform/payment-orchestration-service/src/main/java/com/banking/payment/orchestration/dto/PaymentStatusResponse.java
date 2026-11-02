package com.banking.payment.orchestration.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.banking.payment.orchestration.entity.PaymentStatus;

public record PaymentStatusResponse(
        String paymentId,
        String fromAccount,
        String toAccount,
        BigDecimal amount,
        PaymentStatus status,
        String holdId,
        String message,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
