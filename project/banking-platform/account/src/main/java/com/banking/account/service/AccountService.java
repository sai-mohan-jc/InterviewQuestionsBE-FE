package com.banking.account.service;

import java.util.List;

import com.banking.account.dto.AccountRequest;
import com.banking.account.dto.AccountResponse;
import com.banking.account.dto.AccountSummaryResponse;

public interface AccountService {

    AccountResponse createAccount(AccountRequest request);

    AccountResponse getAccount(String accountNumber);
    
    List<AccountResponse> getAllAccounts();
    
    List<AccountSummaryResponse> getAccountSummaries();
}
