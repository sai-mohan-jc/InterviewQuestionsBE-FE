package com.banking.transaction.dto;

import java.math.BigDecimal;

import lombok.Builder;
@Builder
public record TransferResponse(
        String transactionId,
        String fromAccount,
        String toAccount,
        BigDecimal amount,
        String status
) {
}
