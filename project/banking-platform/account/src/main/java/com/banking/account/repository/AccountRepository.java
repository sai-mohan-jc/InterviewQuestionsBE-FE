package com.banking.account.repository;

import com.banking.account.entity.Account;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByCif(String cif);

    boolean existsByAccountNumber(String accountNumber);
    
    @EntityGraph(attributePaths = "transactions")
    List<Account> findAll();
    
    @Query("""
            SELECT DISTINCT a
            FROM Account a
            LEFT JOIN FETCH a.transactions
            """)
     List<Account> findAllWithTransactions();
}