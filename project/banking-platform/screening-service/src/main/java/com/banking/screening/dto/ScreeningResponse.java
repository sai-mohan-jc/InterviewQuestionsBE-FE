package com.banking.screening.dto;

import com.banking.screening.model.ScreeningDecision;

public record ScreeningResponse(
        String paymentId,
        ScreeningDecision decision,
        String reason
) {
}