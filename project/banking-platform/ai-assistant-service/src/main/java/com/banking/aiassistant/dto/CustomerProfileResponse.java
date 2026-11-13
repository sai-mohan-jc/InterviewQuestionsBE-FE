package com.banking.aiassistant.dto;

public record CustomerProfileResponse(
        String cif,
        String name,
        String email,
        String mobile,
        String kycStatus,
        String status
) {
}