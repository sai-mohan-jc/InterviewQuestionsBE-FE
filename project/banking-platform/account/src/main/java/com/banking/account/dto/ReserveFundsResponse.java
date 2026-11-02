package com.banking.account.dto;

import java.math.BigDecimal;

public record ReserveFundsResponse(
        String holdId,
        String accountNumber,
        BigDecimal amount,
        String status,
        BigDecimal ledgerBalance,
        BigDecimal reservedBalance,
        BigDecimal availableBalance
) {
}
