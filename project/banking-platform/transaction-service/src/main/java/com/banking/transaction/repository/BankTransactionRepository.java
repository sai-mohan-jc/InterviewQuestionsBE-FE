package com.banking.transaction.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.transaction.entity.BankTransaction;

@Repository
public interface BankTransactionRepository
        extends JpaRepository<BankTransaction, Long> {

    Optional<BankTransaction> findByTransactionId(String transactionId);
}