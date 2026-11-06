package com.banking.auth_service.service;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.auth_service.dto.LoginRequest;
import com.banking.auth_service.dto.LoginResponse;
import com.banking.auth_service.dto.RefreshRequest;
import com.banking.auth_service.dto.RefreshResponse;
import com.banking.auth_service.dto.RegisterRequest;
import com.banking.auth_service.dto.RegisterResponse;
import com.banking.auth_service.entity.Role;
import com.banking.auth_service.entity.User;
import com.banking.auth_service.entity.UserSession;
import com.banking.auth_service.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final RefreshTokenService refreshTokenService;
    private final UserSessionService userSessionService;
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, JwtService jwtService,
			RefreshTokenService refreshTokenService,
			UserSessionService userSessionService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.refreshTokenService = refreshTokenService;
		this.userSessionService= userSessionService;
	}

	@Transactional
	public RegisterResponse register(RegisterRequest request) {

		if (userRepository.existsByUsername(request.username())) {
			throw new IllegalArgumentException("Username already exists");
		}

		if (userRepository.existsByEmail(request.email())) {
			throw new IllegalArgumentException("Email already exists");
		}

		User user = User.builder().username(request.username()).email(request.email())
				.password(passwordEncoder.encode(request.password())).role(Role.CUSTOMER)
				.customerCif(request.customerCif()).build();

		User savedUser = userRepository.save(user);

		return new RegisterResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(),
				savedUser.getRole(), savedUser.getCustomerCif(), "User registered successfully");
	}

	@Transactional
	public LoginResponse login(LoginRequest request) {

	    // 1. Authenticate username and password
	    authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(
	                    request.username(),
	                    request.password()
	            )
	    );

	    // 2. Fetch authenticated user
	    User user = userRepository.findByUsername(request.username())
	            .orElseThrow(() ->
	                    new UsernameNotFoundException("User not found"));

	    // 3. Create user session
	    UserSession session = userSessionService.createSession(user);

	    // 4. Generate access token with session ID
	    String accessToken = jwtService.generateToken(
	            user.getUsername(),
	            user.getRole().name(),
	            user.getCustomerCif(),
	            session.getSessionId()
	    );

	    // 5. Generate refresh token linked to session
	    String refreshToken =
	            refreshTokenService.generateRefreshToken(user, session);

	    // 6. Return tokens
	    return new LoginResponse(
	            accessToken,
	            refreshToken,
	            "Bearer",
	            1800,
	            28800,
	            user.getUsername(),
	            user.getRole().name()
	    );
	}

	@Transactional
	public RefreshResponse refreshToken(RefreshRequest request) {

	    RefreshTokenService.RotatedRefreshToken rotated =
	            refreshTokenService.rotateRefreshToken(request.refreshToken());

	    User user = rotated.user();

	    String accessToken = jwtService.generateToken(
	            user.getUsername(),
	            user.getRole().name(),
	            user.getCustomerCif(),
	            rotated.session().getSessionId()
	    );

	    long refreshExpiresIn = java.time.Duration.between(
	            LocalDateTime.now(),
	            rotated.session().getExpiresAt()
	    ).getSeconds();
	    
	    return new RefreshResponse(
	            accessToken,
	            rotated.rawToken(),
	            "Bearer",
	            1800,
	            refreshExpiresIn
	    );
	}

	@Transactional
	public void logout(RefreshRequest request) {

		refreshTokenService.revokeRefreshToken(request.refreshToken());
	}

}