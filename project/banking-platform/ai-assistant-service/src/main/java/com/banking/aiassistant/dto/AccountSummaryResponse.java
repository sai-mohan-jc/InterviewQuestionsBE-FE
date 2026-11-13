package com.banking.aiassistant.dto;

import java.math.BigDecimal;

public record AccountSummaryResponse(
        String accountNumber,
        String cif,
        BigDecimal balance,
        Long transactionCount
) {}