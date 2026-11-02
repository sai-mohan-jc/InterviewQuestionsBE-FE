package com.banking.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.account.entity.AccountEarmark;

public interface AccountEarmarkRepository extends JpaRepository<AccountEarmark, Long> {

	Optional<AccountEarmark> findByHoldId(String holdId);
	
}
