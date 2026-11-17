package com.banking.payment.orchestration.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.payment.orchestration.entity.ScheduledTransaction;

public interface ScheduledTransactionRepository extends JpaRepository<ScheduledTransaction, Long> {

	List<ScheduledTransaction> findByStatusAndScheduledDateLessThanEqualAndScheduledIdGreaterThanOrderByScheduledIdAsc(
			String status, LocalDate scheduledDate, Long lastScheduledId, Pageable pageable);
}
