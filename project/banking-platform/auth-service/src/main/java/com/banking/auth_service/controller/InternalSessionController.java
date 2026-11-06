package com.banking.auth_service.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.banking.auth_service.dto.SessionValidationRequest;
import com.banking.auth_service.dto.SessionValidationResponse;
import com.banking.auth_service.service.UserSessionService;

@RestController
@RequestMapping("/api/internal/sessions")
public class InternalSessionController {

    private final UserSessionService sessionService;

    @Value("${internal.security.secret}")
    private String internalSecret;

    public InternalSessionController(UserSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/validate")
    public ResponseEntity<SessionValidationResponse> validate(
            @RequestHeader("X-Internal-Secret") String secret,
            @RequestBody SessionValidationRequest request) {

        if (!Objects.equals(internalSecret, secret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean active = sessionService.validateAndTouch(request.sessionId());

        return ResponseEntity.ok(new SessionValidationResponse(active));
    }
}