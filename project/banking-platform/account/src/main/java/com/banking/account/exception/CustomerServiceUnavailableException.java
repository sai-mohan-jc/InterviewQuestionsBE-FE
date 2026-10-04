package com.banking.account.exception;

public class CustomerServiceUnavailableException extends RuntimeException {

    public CustomerServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}