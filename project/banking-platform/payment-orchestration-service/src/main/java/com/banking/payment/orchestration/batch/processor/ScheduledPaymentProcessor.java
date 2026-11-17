
package com.banking.payment.orchestration.batch.processor;

import java.math.BigDecimal;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.banking.payment.orchestration.batch.exception.InvalidScheduledPaymentException;
import com.banking.payment.orchestration.entity.ScheduledTransaction;

@Component
public class ScheduledPaymentProcessor
        implements ItemProcessor<ScheduledTransaction, ScheduledTransaction> {

    @Override
    public ScheduledTransaction process(ScheduledTransaction payment) {

        System.out.println(
            "Processing: " + payment.getPaymentReference()
        );

        if ("SCH1004".equals(payment.getPaymentReference())) {
            throw new InvalidScheduledPaymentException(
                "Invalid payment data for SCH1004"
            );
        }

        if (payment.getAmount() == null
                || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            payment.setStatus("FAILED");
            return payment;
        }

        if (payment.getFromAccount() == null
                || payment.getToAccount() == null
                || payment.getFromAccount().equals(payment.getToAccount())) {
            payment.setStatus("FAILED");
            return payment;
        }

        return payment;
    }
}
