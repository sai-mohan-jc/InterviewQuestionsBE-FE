package com.banking.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountSummaryResponse {

    private String accountNumber;
    private String cif;
    private BigDecimal balance;
    private Long transactionCount;
}