package com.banking.transaction.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import com.banking.transaction.client.AccountClient;
import com.banking.transaction.dto.TransferRequest;
import com.banking.transaction.dto.TransferResponse;
import com.banking.transaction.entity.BankTransaction;
import com.banking.transaction.repository.BankTransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

	private final BankTransactionRepository transactionRepository;
	private final AccountClient accountClient;

	public TransferResponse transfer(String idempotencyKey,
	        TransferRequest request) {

	    var existing = transactionRepository
	            .findByIdempotencyKey(idempotencyKey);

	    if (existing.isPresent()) {
	        BankTransaction transaction = existing.get();

	        return TransferResponse.builder()
	                .transactionId(transaction.getTransactionId())
	                .fromAccount(transaction.getFromAccount())
	                .toAccount(transaction.getToAccount())
	                .amount(transaction.getAmount())
	                .status(transaction.getStatus())
	                .build();
	    }

		String transactionId = "TXN" + System.currentTimeMillis();

		BankTransaction transaction = BankTransaction.builder().transactionId(transactionId)
				.fromAccount(request.fromAccount()).toAccount(request.toAccount()).amount(request.amount()).idempotencyKey(idempotencyKey)
				.transactionType("TRANSFER").status("PROCESSING").build();

		transactionRepository.save(transaction);

		// Step 1: Debit source account
		accountClient.updateBalance(request.fromAccount(), request.amount(), "DEBIT");

		try {
			// Step 2: Credit destination account
			accountClient.updateBalance(request.toAccount(), request.amount(), "CREDIT");

			transaction.setStatus("SUCCESS");

		} catch (org.springframework.web.client.RestClientResponseException creditException) {

			// Step 3: Destination credit failed; compensate the debit
			try {
				accountClient.updateBalance(request.fromAccount(), request.amount(), "CREDIT");

				transaction.setStatus("FAILED");

			} catch (Exception compensationException) {
				// Refund also failed: this requires recovery
				transaction.setStatus("COMPENSATION_FAILED");
			}

			transactionRepository.save(transaction);

			throw creditException;
		}

		transactionRepository.save(transaction);

		return TransferResponse.builder().transactionId(transactionId).fromAccount(request.fromAccount())
				.toAccount(request.toAccount()).amount(request.amount()).status(transaction.getStatus()).build();
	}

}