package com.banking.account.service.impl;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.banking.account.client.CustomerClient;
import com.banking.account.dto.AccountRequest;
import com.banking.account.dto.AccountResponse;
import com.banking.account.dto.AccountSummaryResponse;
import com.banking.account.dto.TransactionResponse;
import com.banking.account.entity.Account;
import com.banking.account.repository.AccountRepository;
import com.banking.account.service.AccountAuditService;
import com.banking.account.service.AccountService;
import com.banking.account.util.AccountNumberGenerator;

@Service
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;
	private final CustomerClient customerClient;
	private final AccountNumberGenerator accountNumberGenerator;
	private final AccountAuditService accountAuditService;
	private final ExecutorService executorService;
	private static final AtomicLong ACCOUNT_SEQUENCE = new AtomicLong(10000);

	public AccountServiceImpl(AccountRepository accountRepository, CustomerClient customerClient,
			AccountNumberGenerator accountNumberGenerator, AccountAuditService accountAuditService) {
		this.accountRepository = accountRepository;
		this.customerClient = customerClient;
		this.accountNumberGenerator = accountNumberGenerator;
		this.accountAuditService = accountAuditService;
		this.executorService = Executors.newFixedThreadPool(5);
	}

	@Override
	public AccountResponse createAccount(AccountRequest request) {

		customerClient.customerExists(request.getCif());

		String accountNumber = accountNumberGenerator.generate();

		Account account = Account.builder().accountNumber(accountNumber).cif(request.getCif())
				.accountType(request.getAccountType()).balance(request.getBalance()).status(request.getStatus())
				.build();

		Account savedAccount = accountRepository.save(account);

		executorService.submit(() -> accountAuditService.auditAccountCreation(savedAccount.getAccountNumber()));

		return new AccountResponse(savedAccount.getAccountNumber(), savedAccount.getCif(),
				savedAccount.getAccountType(), savedAccount.getBalance(), savedAccount.getStatus(), null,
				System.currentTimeMillis());
	}

	@Override
	public AccountResponse getAccount(String accountNumber) {

		Account account = accountRepository.findByAccountNumber(accountNumber)
				.orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));

		return new AccountResponse(account.getAccountNumber(), account.getCif(), account.getAccountType(),
				account.getBalance(), account.getStatus(),
				account.getTransactions().stream()
						.map(transaction -> new TransactionResponse(transaction.getTransactionType(),
								transaction.getAmount()))
						.toList(),
				System.currentTimeMillis());
	}

	@Override
	public List<AccountResponse> getAllAccounts() {

		List<Account> accounts = accountRepository.findAll();
		// List<Account> accounts = accountRepository.findAllWithTransactions();

		return accounts
				.stream().map(
						account -> new AccountResponse(account.getAccountNumber(), account.getCif(),
								account.getAccountType(), account.getBalance(), account.getStatus(),
								account.getTransactions().stream()
										.map(transaction -> new TransactionResponse(transaction.getTransactionType(),
												transaction.getAmount()))
										.toList(),
								System.currentTimeMillis()))
				.toList();
	}

	@Override
	public List<AccountSummaryResponse> getAccountSummaries() {
		return accountRepository.findAccountSummaries();
	}
}