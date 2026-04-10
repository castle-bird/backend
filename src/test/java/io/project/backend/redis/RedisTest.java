package io.project.backend.redis;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class RedisTest {

  @Container
  static GenericContainer<?> redisContainer =
      new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
          .withExposedPorts(6379);

  @DynamicPropertySource
  static void redisProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.redis.host", redisContainer::getHost);
    registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort);
    registry.add("spring.data.redis.username", () -> "");
    registry.add("spring.data.redis.password", () -> "");
  }

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Autowired
  private CacheManager cacheManager;

  @AfterEach
  void tearDown() {
    redisTemplate.execute((RedisCallback<Void>) connection -> {
      connection.serverCommands().flushAll();
      return null;
    });
  }

  @Test
  @DisplayName("Redis 서버 연결 확인")
  void ping() {
    String pong = redisTemplate.execute((RedisCallback<String>) connection -> connection.ping());
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
