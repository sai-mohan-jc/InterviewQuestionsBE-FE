
package com.banking.payment.orchestration.dto;

public record ReviewDecisionNotification(
        String paymentId,
        String holdId,
        String status
) {}
