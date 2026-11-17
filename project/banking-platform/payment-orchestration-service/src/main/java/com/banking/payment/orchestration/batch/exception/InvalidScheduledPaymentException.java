package com.banking.payment.orchestration.batch.exception;

public class InvalidScheduledPaymentException extends RuntimeException {

    public InvalidScheduledPaymentException(String message) {
        super(message);
    }
}
