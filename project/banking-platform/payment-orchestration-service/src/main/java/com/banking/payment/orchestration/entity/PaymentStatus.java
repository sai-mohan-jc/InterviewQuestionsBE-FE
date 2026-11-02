package com.banking.payment.orchestration.entity;

public enum PaymentStatus {
    INITIATED,
    SCREENING,
    REVIEW_PENDING,
    PROCESSING,
    COMPLETED,
    REJECTED,
    FAILED
}
