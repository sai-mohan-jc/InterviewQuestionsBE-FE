package com.banking.earmarking.dto;

import com.banking.earmarking.entity.ReviewStatus;

public record ReviewDecisionNotification(
        String paymentId,
        String holdId,
        ReviewStatus status
) {}
