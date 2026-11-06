package com.banking.auth_service.dto;

public record RefreshResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    long expiresIn,
    long refreshExpiresIn
) {}