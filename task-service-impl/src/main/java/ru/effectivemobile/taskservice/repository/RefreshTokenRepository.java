package ru.effectivemobile.taskservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.effectivemobile.taskservice.entity.RefreshTokenEntity;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

    void deleteByExpiredAtBefore(LocalDateTime expiredAt);
}
