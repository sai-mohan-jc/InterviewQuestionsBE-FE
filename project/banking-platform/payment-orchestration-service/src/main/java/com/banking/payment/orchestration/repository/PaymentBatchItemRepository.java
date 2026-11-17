package com.banking.payment.orchestration.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.banking.payment.orchestration.entity.PaymentBatchItem;

public interface PaymentBatchItemRepository
        extends JpaRepository<PaymentBatchItem, Long> {
	
	List<PaymentBatchItem>
	findByBatch_BatchIdAndItemStatusAndItemIdGreaterThanOrderByItemIdAsc(
	        Long batchId,
	        String itemStatus,
	        Long lastItemId,
	        Pageable pageable);

}