package com.banking.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.account.entity.AccountTransaction;

public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {
}
