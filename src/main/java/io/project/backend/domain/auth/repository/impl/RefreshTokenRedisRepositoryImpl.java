package io.project.backend.domain.auth.repository.impl;

import io.project.backend.domain.auth.repository.RefreshTokenRedisRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisRepositoryImpl implements RefreshTokenRedisRepository {

  private final String KEY_PREFIX = "refresh_token:";
  private final RedisTemplate<String, String> redisTemplate;

  @Override
  public void save(Long userId, String refreshToken, Duration ttl) {
    String key = KEY_PREFIX + userId;
    redisTemplate.opsForValue().set(key, refreshToken, ttl);
  }

  @Override
  public String findByUserId(Long userId) {
    String key = KEY_PREFIX + userId;
    return redisTemplate.opsForValue().get(key);
  }

  @Override
  public void delete(Long userId) {
    String key = KEY_PREFIX + userId;
    redisTemplate.delete(key);
  }
}
