package com.banking.account.service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.banking.account.client.CustomerClient;
import com.banking.account.dto.AccountRequest;
import com.banking.account.dto.AccountResponse;
import com.banking.account.dto.TransactionResponse;
import com.banking.account.entity.Account;
import com.banking.account.repository.AccountRepository;
import com.banking.account.service.AccountService;
import com.banking.account.util.AccountNumberGenerator;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerClient customerClient;
    private final AccountNumberGenerator accountNumberGenerator;

    private static final AtomicLong ACCOUNT_SEQUENCE =
            new AtomicLong(10000);

    public AccountServiceImpl(AccountRepository accountRepository,CustomerClient customerClient,AccountNumberGenerator accountNumberGenerator) {
        this.accountRepository = accountRepository;
        this.customerClient = customerClient; 
        this.accountNumberGenerator= accountNumberGenerator;
    }

    @Override
    public AccountResponse createAccount(AccountRequest request) {

      customerClient.customerExists(request.getCif());
      
        String accountNumber =
                accountNumberGenerator.generate();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .cif(request.getCif())
                .accountType(request.getAccountType())
                .balance(request.getBalance())
                .status(request.getStatus())
                .build();

        Account savedAccount = accountRepository.save(account);

        return new AccountResponse(
                savedAccount.getAccountNumber(),
                savedAccount.getCif(),
                savedAccount.getAccountType(),
                savedAccount.getBalance(),
                savedAccount.getStatus(),
                account.getTransactions().stream().map(transaction -> new TransactionResponse(transaction.getTransactionType(),transaction.getAmount())).toList()
        );
    }
    
    @Override
    public AccountResponse getAccount(String accountNumber) {

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Account not found: " + accountNumber));

        return new AccountResponse(
                account.getAccountNumber(),
                account.getCif(),
                account.getAccountType(),
                account.getBalance(),
                account.getStatus(),
                account.getTransactions().stream().map(transaction -> new TransactionResponse(transaction.getTransactionType(),transaction.getAmount())).toList()
        );
    }
    
    @Override
    public List<AccountResponse> getAllAccounts() {

        List<Account> accounts = accountRepository.findAll();
    	//List<Account> accounts = accountRepository.findAllWithTransactions();

        return accounts.stream()
                .map(account -> new AccountResponse(
                        account.getAccountNumber(),
                        account.getCif(),
                        account.getAccountType(),
                        account.getBalance(),
                        account.getStatus(),
                        account.getTransactions().stream().map(transaction -> new TransactionResponse(transaction.getTransactionType(),transaction.getAmount())).toList()
                ))
                .toList();
    }
}