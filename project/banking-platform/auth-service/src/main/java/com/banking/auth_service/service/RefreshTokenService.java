package com.banking.auth_service.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.auth_service.entity.RefreshToken;
import com.banking.auth_service.entity.User;
import com.banking.auth_service.entity.UserSession;
import com.banking.auth_service.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	private final UserSessionService userSessionService;

	private final SecureRandom secureRandom = new SecureRandom();

	private static final long REFRESH_TOKEN_EXPIRY_DAYS = 7;

	public RefreshTokenService(RefreshTokenRepository repo, UserSessionService userSessionService) {
		this.refreshTokenRepository = repo;
		this.userSessionService = userSessionService;
	}

	// Used during login
	@Transactional
	public String generateRefreshToken(User user, UserSession session) {
		return createRefreshToken(user, session).rawToken();
	}

	// Used during refresh-token rotation
	@Transactional
	public RotatedRefreshToken rotateRefreshToken(String rawToken) {

		String tokenHash = hashToken(rawToken);

		RefreshToken oldToken = refreshTokenRepository.findByTokenHashForUpdate(tokenHash)
				.orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

		validateToken(oldToken);

		User user = oldToken.getUser();
		UserSession session = oldToken.getSession();

		if (!userSessionService.validateAndTouch(session.getSessionId())) {
			throw new IllegalArgumentException("Invalid refresh token");
		}

		IssuedRefreshToken newToken = createRefreshToken(user, session);

		oldToken.setRevoked(true);
		oldToken.setReplacedBy(newToken.entity().getId());

		refreshTokenRepository.save(oldToken);

		return new RotatedRefreshToken(user, session, newToken.rawToken());
	}

	private void validateToken(RefreshToken refreshToken) {

		if (refreshToken.isRevoked()) {
			throw new IllegalArgumentException("Invalid refresh token");
		}

		if (!refreshToken.getExpiryDate().isAfter(LocalDateTime.now())) {
			throw new IllegalArgumentException("Invalid refresh token");
		}

		if (!refreshToken.getUser().isEnabled()) {
			throw new IllegalArgumentException("Invalid refresh token");
		}
	}

	private IssuedRefreshToken createRefreshToken(User user, UserSession session) {

	    byte[] randomBytes = new byte[32];
	    secureRandom.nextBytes(randomBytes);

	    String rawToken = Base64.getUrlEncoder()
	            .withoutPadding().encodeToString(randomBytes);

	    RefreshToken refreshToken = RefreshToken.builder()
	            .tokenHash(hashToken(rawToken))
	            .user(user)
	            .session(session)
	            .expiryDate(session.getExpiresAt())
	            .createdAt(LocalDateTime.now())
	            .revoked(false)
	            .build();

	    RefreshToken savedToken = refreshTokenRepository.save(refreshToken);

	    return new IssuedRefreshToken(rawToken, savedToken);
	}

	private String hashToken(String rawToken) {

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			byte[] hashBytes = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));

			return Base64.getEncoder().withoutPadding().encodeToString(hashBytes);

		} catch (Exception e) {
			throw new IllegalStateException("Unable to hash refresh token", e);
		}
	}

	// Internal result containing raw token and saved database entity
	private record IssuedRefreshToken(String rawToken, RefreshToken entity) {
	}

	@Transactional
	public void revokeRefreshToken(String rawToken) {

		String tokenHash = hashToken(rawToken);

		refreshTokenRepository.findByTokenHashForUpdate(tokenHash).ifPresent(token -> {
			if (!token.isRevoked()) {
				token.setRevoked(true);
				refreshTokenRepository.save(token);

				userSessionService.revokeSession(token.getSession());
			}
		});
	}


	public record RotatedRefreshToken(User user, UserSession session, String rawToken) {
	}
}