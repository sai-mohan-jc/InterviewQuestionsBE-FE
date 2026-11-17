
package com.banking.payment.orchestration.batch.writer;

import java.util.List;

import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.banking.payment.orchestration.entity.ScheduledTransaction;
import com.banking.payment.orchestration.repository.ScheduledTransactionRepository;

@Component
public class ScheduledPaymentWriter implements ItemWriter<ScheduledTransaction> {

	private final ScheduledTransactionRepository repository;

	public ScheduledPaymentWriter(ScheduledTransactionRepository repository) {
		this.repository = repository;
	}

	@Override
	public void write(Chunk<? extends ScheduledTransaction> chunk) {

		List<? extends ScheduledTransaction> payments = chunk.getItems();

		for (ScheduledTransaction payment : payments) {
			
			 if ("SCH1004".equals(payment.getPaymentReference())) {

			        throw new RuntimeException(
			                "Simulated database processing failure for SCH1004");
			    }

			if (!"FAILED".equals(payment.getStatus())) {
				payment.setStatus("PROCESSED");
			}

			System.out
					.println("Writing payment: " + payment.getPaymentReference() + " | Status: " + payment.getStatus());
		}

		repository.saveAll(payments);
	}
}
