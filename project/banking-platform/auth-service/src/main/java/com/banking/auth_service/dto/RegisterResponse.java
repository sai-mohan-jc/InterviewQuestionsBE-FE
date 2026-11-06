package com.banking.auth_service.dto;

import com.banking.auth_service.entity.Role;

public record RegisterResponse(
        Long id,
        String username,
        String email,
        Role role,
        String customerCif,
        String message
) {}