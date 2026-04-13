package io.project.backend.domain.auth.repository;

import java.time.Duration;

public interface RefreshTokenRedisRepository {

  void save(Long userId, String refreshToken, Duration ttl);

  String findByUserId(Long userId);

  void delete(Long userId);
}
