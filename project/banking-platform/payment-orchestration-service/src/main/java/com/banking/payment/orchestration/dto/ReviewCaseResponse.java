
package com.banking.payment.orchestration.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReviewCaseResponse(
        String paymentId,
        String holdId,
        String fromAccount,
        String toAccount,
        BigDecimal amount,
        String status,
        String reason,
        LocalDateTime reviewDeadline
) {}
