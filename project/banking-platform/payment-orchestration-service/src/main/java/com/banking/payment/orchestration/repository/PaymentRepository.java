package com.banking.payment.orchestration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.payment.orchestration.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
