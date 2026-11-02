package com.banking.earmarking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.banking.earmarking.entity.ReviewStatus;

public record ReviewCaseResponse(
        String paymentId,
        String holdId,
        String fromAccount,
        String toAccount,
        BigDecimal amount,
        ReviewStatus status,
        String reason,
        LocalDateTime reviewDeadline
) {
}
