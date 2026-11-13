package com.banking.account.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.banking.account.dto.AccountRequest;
import com.banking.account.dto.AccountResponse;
import com.banking.account.dto.AccountSummaryResponse;
import com.banking.account.dto.BalanceUpdateRequest;
import com.banking.account.service.AccountService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public AccountResponse createAccount(@RequestBody @Valid AccountRequest request) {
		long start = System.currentTimeMillis();
		AccountResponse response = accountService.createAccount(request);
		response.setResponseTimeMs(System.currentTimeMillis() - start);
		return response;
	}

	@GetMapping("/{accountNumber}")
	public AccountResponse getAccount(@PathVariable String accountNumber) {

		return accountService.getAccount(accountNumber);
	}

	@GetMapping
	public List<AccountResponse> getAllAccounts() {
		return accountService.getAllAccounts();
	}

	@GetMapping("/summary")
	public List<AccountSummaryResponse> getAccountSummaries() {
		return accountService.getAccountSummaries();
	}

	@PutMapping("/{accountNumber}/balance")
	public AccountResponse updateBalance(@PathVariable String accountNumber,
			@RequestBody @Valid BalanceUpdateRequest request) {

		return accountService.updateBalance(accountNumber, request);
	}
	
	@GetMapping("/my-accounts")
	public List<AccountSummaryResponse> getMyAccounts(
	        @AuthenticationPrincipal Jwt jwt) {

	    String cif = jwt.getClaimAsString("customerCif");

	    return accountService.getAccountsByCif(cif);
	}
}