package com.banking.customer.service;

import org.springframework.stereotype.Service;

@Service
public class AccountAuditService {

    public void auditAccountCreation(String accountNumber) {

        System.out.println(
            "Audit started for " + accountNumber
            + " | Thread: " + Thread.currentThread().getName()
        );

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println(
            "Audit completed for " + accountNumber
            + " | Thread: " + Thread.currentThread().getName()
        );
    }
}