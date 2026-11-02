package com.banking.earmarking.dto;

import jakarta.validation.constraints.NotNull;

import com.banking.earmarking.entity.ReviewStatus;

public record ReviewDecisionRequest(
        @NotNull ReviewStatus decision
) {
}
