package com.banking.account.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.account.dto.ReserveFundsRequest;
import com.banking.account.dto.ReserveFundsResponse;
import com.banking.account.entity.Account;
import com.banking.account.entity.AccountEarmark;
import com.banking.account.exception.AccountNotFoundException;
import com.banking.account.repository.AccountEarmarkRepository;
import com.banking.account.repository.AccountRepository;

@Service
public class AccountEarmarkService {

    private final AccountRepository accountRepository;
    private final AccountEarmarkRepository earmarkRepository;

    public AccountEarmarkService(
            AccountRepository accountRepository,
            AccountEarmarkRepository earmarkRepository) {
        this.accountRepository = accountRepository;
        this.earmarkRepository = earmarkRepository;
    }

    @Transactional
    public ReserveFundsResponse reserveFunds(
            String accountNumber,
            ReserveFundsRequest request) {

        BigDecimal amount = request.amount();

        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Reservation amount must be greater than zero");
        }

        // Lock the account row before checking available funds.
        Account account = accountRepository
                .findByAccountNumberForUpdate(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account not found: " + accountNumber));

        // Idempotency: do not reserve the same hold twice.
        var existing = earmarkRepository.findByHoldId(request.holdId());

        if (existing.isPresent()) {
            AccountEarmark hold = existing.get();

            if (!hold.getAccountNumber().equals(accountNumber)
                    || hold.getAmount().compareTo(amount) != 0) {
                throw new IllegalStateException(
                        "holdId already exists with different details");
            }

            if (!"ACTIVE".equals(hold.getStatus())) {
                throw new IllegalStateException(
                        "This hold has already been processed");
            }

            return toResponse(hold, account);
        }

        if (!"ACTIVE".equalsIgnoreCase(account.getStatus())) {
            throw new IllegalStateException(
                    "Account is not active: " + accountNumber);
        }

        if (account.getAvailableBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient available balance");
        }

        // Reserve funds without reducing the ledger balance.
        account.setReservedBalance(
                account.getReservedBalance().add(amount));

        AccountEarmark hold = AccountEarmark.builder()
                .holdId(request.holdId())
                .accountNumber(accountNumber)
                .amount(amount)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();

        accountRepository.save(account);
        AccountEarmark savedHold = earmarkRepository.save(hold);

        return toResponse(savedHold, account);
    }
    

@Transactional
public ReserveFundsResponse releaseFunds(
        String accountNumber,
        String holdId) {

    // Lock the account first so concurrent hold operations
    // for this account are serialized.
    Account account = accountRepository
            .findByAccountNumberForUpdate(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(
                    "Account not found: " + accountNumber));

    AccountEarmark hold = earmarkRepository.findByHoldId(holdId)
            .orElseThrow(() -> new IllegalArgumentException(
                    "Earmark not found: " + holdId));

    if (!hold.getAccountNumber().equals(accountNumber)) {
        throw new IllegalArgumentException(
                "Earmark does not belong to this account");
    }

    // Idempotent release: never subtract twice.
    if ("RELEASED".equals(hold.getStatus())) {
        return toResponse(hold, account);
    }

    if (!"ACTIVE".equals(hold.getStatus())) {
        throw new IllegalStateException(
                "Cannot release earmark with status: "
                        + hold.getStatus());
    }

    BigDecimal reserved = account.getReservedBalance();

    if (reserved == null || reserved.compareTo(hold.getAmount()) < 0) {
        throw new IllegalStateException(
                "Reserved balance is inconsistent with the earmark");
    }

    account.setReservedBalance(
            reserved.subtract(hold.getAmount()));

    hold.setStatus("RELEASED");

    accountRepository.save(account);
    earmarkRepository.save(hold);

    return toResponse(hold, account);
}


    private ReserveFundsResponse toResponse(
            AccountEarmark hold,
            Account account) {

        return new ReserveFundsResponse(
                hold.getHoldId(),
                hold.getAccountNumber(),
                hold.getAmount(),
                hold.getStatus(),
                account.getBalance(),
                account.getReservedBalance(),
                account.getAvailableBalance());
    }
}
