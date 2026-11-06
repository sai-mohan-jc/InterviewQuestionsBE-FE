package com.banking.auth_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import com.banking.auth_service.entity.UserSession;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM UserSession s WHERE s.sessionId = :sessionId")
    Optional<UserSession> findBySessionIdForUpdate(
            @Param("sessionId") String sessionId);
}