package com.banking.auth_service.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.banking.auth_service.dto.LoginRequest;
import com.banking.auth_service.dto.LoginResponse;
import com.banking.auth_service.dto.RefreshRequest;
import com.banking.auth_service.dto.RefreshResponse;
import com.banking.auth_service.dto.RegisterRequest;
import com.banking.auth_service.dto.RegisterResponse;
import com.banking.auth_service.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {

		return userService.register(request);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

		return ResponseEntity.ok(userService.login(request));
	}

	@GetMapping("/profile")
	public Map<String, Object> profile(@AuthenticationPrincipal Jwt jwt) {

		return Map.of("username", jwt.getSubject(), "role", jwt.getClaimAsString("role"), "issuer",
				jwt.getClaimAsString("iss"));
	}

	@GetMapping("/customer/dashboard")
	public Map<String, String> customerDashboard() {
		return Map.of("message", "Welcome to Customer Dashboard", "access", "CUSTOMER");
	}

	@GetMapping("/reviewer/dashboard")
	public Map<String, String> reviewerDashboard() {
		return Map.of("message", "Welcome to Reviewer Dashboard", "access", "REVIEWER");
	}

	@GetMapping("/admin/dashboard")
	public Map<String, String> adminDashboard() {
		return Map.of("message", "Welcome to Admin Dashboard", "access", "ADMIN");
	}

	@PostMapping("/refresh")
	public ResponseEntity<RefreshResponse> refreshToken(@Valid @RequestBody RefreshRequest request) {

		return ResponseEntity.ok(userService.refreshToken(request));
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@Valid @RequestBody RefreshRequest request) {

		userService.logout(request);

		return ResponseEntity.noContent().build();
	}

}