package com.banking.transaction.service;

import org.springframework.stereotype.Service;

import com.banking.transaction.dto.TransferRequest;
import com.banking.transaction.dto.TransferResponse;
import com.banking.transaction.entity.BankTransaction;
import com.banking.transaction.repository.BankTransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final BankTransactionRepository transactionRepository;

    public TransferResponse transfer(TransferRequest request) {

        String transactionId =
                "TXN" + System.currentTimeMillis();

        BankTransaction transaction =
                BankTransaction.builder()
                        .transactionId(transactionId)
                        .fromAccount(request.fromAccount())
                        .toAccount(request.toAccount())
                        .amount(request.amount())
                        .transactionType("TRANSFER")
                        .status("SUCCESS")
                        .build();

        transactionRepository.save(transaction);

        return TransferResponse.builder()
                .transactionId(transactionId)
                .fromAccount(request.fromAccount())
                .toAccount(request.toAccount())
                .amount(request.amount())
                .status(transaction.getStatus())
                .build();
    }
}