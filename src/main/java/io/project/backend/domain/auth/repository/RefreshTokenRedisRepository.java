package io.project.backend.domain.auth.repository;

import java.time.Duration;
import java.time.Instant;

public interface RefreshTokenRedisRepository {

  void save(Long userId, String tokenId, String refreshToken, Instant issuedAt, Duration ttl);

  boolean exists(Long userId, String refreshToken);

  void delete(Long userId, String refreshToken);

  void deleteAllByUserId(Long userId);
}
