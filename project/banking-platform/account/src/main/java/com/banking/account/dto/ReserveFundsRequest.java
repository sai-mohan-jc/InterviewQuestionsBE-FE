package com.banking.account.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReserveFundsRequest(
        @NotBlank String holdId,
        @NotNull @Positive BigDecimal amount
) {
}
