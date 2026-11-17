
package com.banking.payment.orchestration.batch.reader;

import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.banking.payment.orchestration.entity.ScheduledTransaction;
import com.banking.payment.orchestration.repository.ScheduledTransactionRepository;

public class KeysetScheduledPaymentReader implements ItemReader<ScheduledTransaction> {

	private final ScheduledTransactionRepository repository;
	private final int pageSize;

	private final Queue<ScheduledTransaction> buffer = new ArrayDeque<>();

	private Long lastScheduledId = 0L;
	private boolean exhausted = false;

	public KeysetScheduledPaymentReader(ScheduledTransactionRepository repository, int pageSize) {

		this.repository = repository;
		this.pageSize = pageSize;
	}

	@Override
	public ScheduledTransaction read() {
      System.out.println("lastScheduledId "+lastScheduledId);
		if (buffer.isEmpty()) {

			if (exhausted) {
				return null;
			}

			List<ScheduledTransaction> records = repository
					.findByStatusAndScheduledDateLessThanEqualAndScheduledIdGreaterThanOrderByScheduledIdAsc(
							"SCHEDULED", LocalDate.now(), lastScheduledId,
							PageRequest.of(0, pageSize, Sort.by("scheduledId").ascending()));

			if (records.isEmpty()) {
				exhausted = true;
				return null;
			}

			buffer.addAll(records);
		}

		ScheduledTransaction payment = buffer.poll();

		lastScheduledId = payment.getScheduledId();

		return payment;
	}
}
