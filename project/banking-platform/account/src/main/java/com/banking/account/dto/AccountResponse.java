package com.banking.account.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountResponse {

    private String accountNumber;
    private String cif;
    private String accountType;
    private BigDecimal balance;
    private String status;
    private List<TransactionResponse> transactions;
}