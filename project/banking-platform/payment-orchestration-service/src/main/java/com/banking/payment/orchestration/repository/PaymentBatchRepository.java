package com.banking.payment.orchestration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.payment.orchestration.entity.PaymentBatch;

public interface PaymentBatchRepository
        extends JpaRepository<PaymentBatch, Long> {
	
    List<PaymentBatch> findByBatchStatus(String batchStatus);
	
}