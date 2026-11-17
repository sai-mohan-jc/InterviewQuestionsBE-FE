package com.banking.payment.orchestration.batch.config;

import org.springframework.batch.core.listener.SkipListener;
import org.springframework.stereotype.Component;

import com.banking.payment.orchestration.entity.ScheduledTransaction;

@Component
public class ScheduledPaymentSkipListener implements SkipListener<ScheduledTransaction, ScheduledTransaction> {

	@Override
	public void onSkipInProcess(ScheduledTransaction payment, Throwable exception) {

		System.out.println("Skipped payment: " + payment.getPaymentReference());

		System.out.println("Reason: " + exception.getMessage());
	}

	@Override
	public void onSkipInRead(Throwable exception) {
		System.out.println("Reader skipped: " + exception.getMessage());
	}

	@Override
	public void onSkipInWrite(ScheduledTransaction payment, Throwable exception) {

		System.out.println("Writer skipped: " + payment.getPaymentReference());
	}
}
