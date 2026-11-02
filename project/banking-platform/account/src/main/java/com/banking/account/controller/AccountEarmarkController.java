package com.banking.account.controller;

import com.banking.account.dto.ReserveFundsRequest;
import com.banking.account.dto.ReserveFundsResponse;
import com.banking.account.service.AccountEarmarkService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountEarmarkController {

	private final AccountEarmarkService earmarkService;

	public AccountEarmarkController(AccountEarmarkService earmarkService) {
		this.earmarkService = earmarkService;
	}

	@PostMapping("/{accountNumber}/earmarks")
	public ReserveFundsResponse reserveFunds(@PathVariable String accountNumber,
			@Valid @RequestBody ReserveFundsRequest request) {

		return earmarkService.reserveFunds(accountNumber, request);
	}

	@PostMapping("/{accountNumber}/earmarks/{holdId}/release")
	public ReserveFundsResponse releaseFunds(@PathVariable String accountNumber, @PathVariable String holdId) {

		return earmarkService.releaseFunds(accountNumber, holdId);
	}

}
