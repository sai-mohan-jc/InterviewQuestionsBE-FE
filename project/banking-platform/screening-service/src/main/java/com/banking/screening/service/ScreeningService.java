package com.banking.screening.service;

import java.math.BigDecimal;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.banking.screening.dto.ScreeningRequest;
import com.banking.screening.dto.ScreeningResponse;
import com.banking.screening.model.ScreeningDecision;

@Service
public class ScreeningService {

    public ScreeningResponse screen(ScreeningRequest request) {

        String beneficiaryName =
                request.beneficiaryName()
                       .trim()
                       .toUpperCase(Locale.ROOT);

        // Simulated test rule: exact name triggers a block
        if (beneficiaryName.equals("BLOCKED TEST BENEFICIARY")) {
            return new ScreeningResponse(
                    request.paymentId(),
                    ScreeningDecision.BLOCK,
                    "Matched simulated blocked test beneficiary"
            );
        }

        // Simulated test rule: amount above threshold requires review
        if (request.amount().compareTo(new BigDecimal("100000")) > 0) {
            return new ScreeningResponse(
                    request.paymentId(),
                    ScreeningDecision.REVIEW,
                    "Transfer requires additional review"
            );
        }

        return new ScreeningResponse(
                request.paymentId(),
                ScreeningDecision.PASS,
                "No simulated screening rule was triggered"
        );
    }
}