package com.banking.auth_service.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.auth_service.entity.User;
import com.banking.auth_service.entity.UserSession;
import com.banking.auth_service.repository.UserSessionRepository;

@Service
public class UserSessionService {

    private static final long IDLE_TIMEOUT_MINUTES = 15;
    private static final long ABSOLUTE_TIMEOUT_HOURS = 8;

    private final UserSessionRepository repository;

    public UserSessionService(UserSessionRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UserSession createSession(User user) {
        LocalDateTime now = LocalDateTime.now();

        UserSession session = UserSession.builder()
                .sessionId(UUID.randomUUID().toString())
                .user(user)
                .createdAt(now)
                .lastActivityAt(now)
                .expiresAt(now.plusHours(ABSOLUTE_TIMEOUT_HOURS))
                .revoked(false)
                .build();

        return repository.save(session);
    }

    @Transactional
    public boolean validateAndTouch(String sessionId) {

        UserSession session = repository.findBySessionIdForUpdate(sessionId)
                .orElse(null);

        if (session == null || session.isRevoked()) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();

        boolean idleExpired = session.getLastActivityAt()
                .plusMinutes(IDLE_TIMEOUT_MINUTES).isBefore(now);

        boolean absoluteExpired = !session.getExpiresAt().isAfter(now);

        if (idleExpired || absoluteExpired || !session.getUser().isEnabled()) {
            session.setRevoked(true);
            repository.save(session);
            return false;
        }

        session.setLastActivityAt(now);
        repository.save(session);

        return true;
    }

    @Transactional
    public void revokeSession(UserSession session) {
        if (session != null && !session.isRevoked()) {
            session.setRevoked(true);
            repository.save(session);
        }
    }
}