package io.project.backend.redis;

import static org.assertj.core.api.Assertions.assertThat;

import io.project.backend.support.IntegrationTest;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

class RedisTest extends IntegrationTest {

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Autowired
  private CacheManager cacheManager;

  @AfterEach
  void tearDown() {
    // 테스트 간 데이터 격리를 위해 매 테스트 후 Redis 전체 초기화
    redisTemplate.execute((RedisCallback<Void>) connection -> {
      connection.serverCommands().flushAll();
      return null;
    });
  }

  @Test
  @DisplayName("Redis 서버 연결 확인")
  void ping() {
    String pong = redisTemplate.execute(RedisConnectionCommands::ping);
    assertThat(pong).isEqualTo("PONG");
  }

  @Test
  @DisplayName("문자열 저장/조회")
  void setAndGet() {
    redisTemplate.opsForValue().set("test:key", "hello");
    assertThat(redisTemplate.opsForValue().get("test:key")).isEqualTo("hello");
  }

  @Test
  @DisplayName("TTL 설정 후 만료 시간 확인")
  void ttl() {
    redisTemplate.opsForValue().set("test:ttl", "value", Duration.ofMinutes(5));
    Long ttl = redisTemplate.getExpire("test:ttl", TimeUnit.SECONDS);
    assertThat(ttl).isPositive();
  }

  @Test
  @DisplayName("CacheManager 빈이 RedisCacheManager인지 확인")
  void cacheManagerIsRedis() {
    assertThat(cacheManager).isInstanceOf(RedisCacheManager.class);
  }
}
