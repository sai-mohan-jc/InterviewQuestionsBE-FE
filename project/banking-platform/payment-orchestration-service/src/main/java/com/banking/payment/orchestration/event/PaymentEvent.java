package com.banking.payment.orchestration.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentEvent(
        String eventId,
        String paymentId,
        String status,
        BigDecimal amount,
        LocalDateTime eventTime
) {
}