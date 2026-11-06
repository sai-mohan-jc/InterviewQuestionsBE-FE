
package com.banking.auth_service.dto;

public record LoginResponse(

        String accessToken,

        String refreshToken,

        String tokenType,

        long expiresIn,

        long refreshExpiresIn,

        String username,

        String role

) {}
