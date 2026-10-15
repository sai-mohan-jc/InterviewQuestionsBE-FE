package com.banking.transaction.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.transaction.dto.TransferRequest;
import com.banking.transaction.dto.TransferResponse;
import com.banking.transaction.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public TransferResponse transfer(
            @Valid @RequestBody TransferRequest request) {

        return transactionService.transfer(request);
    }
}
