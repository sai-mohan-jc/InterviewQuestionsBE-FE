package com.banking.notification.event;

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